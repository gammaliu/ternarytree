package de.mpii.ternarytree;

import gnu.trove.list.TCharList;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TCharArrayList;
import gnu.trove.list.array.TIntArrayList;

/**
 * JH: Some general comments
 * - please add doc-comments to public methods
 * - please add more comments to parts of the code, e.g. the more higher-up
 *   parts of the while/if constructs to get a quick picture of what is being done.
 *   Even better, follow Diego's suggestion and add all the primitive methods.
 */
public class TernaryTriePrimitive implements Trie{
    // JH: Why not use a char list? This way you are independent of the encoding.
    TCharList labels;
    TIntList nodes;
    // JH: Seeing from the unit tests, you allow the same string to be inserted
    // with the same value. I don't think this is necessary, a regular Map-style
    // single key-value should be fine. This way, you can store the single pointer
    // in nodes and do not need values.
    int root;
    
    public TernaryTriePrimitive() {
        labels = new TCharArrayList();
        nodes = new TIntArrayList();
        root = -1;
    }

    public int get(String key, int defaultValue) {
        int node = root;
        int pos = 0;
        while (node != -1) {
            if (key.charAt(pos) < labels.get(node/4)) {
                node = nodes.get(node);
            } else if(key.charAt(pos) == labels.get(node/4)) {
                if (pos == key.length() - 1) {
                    break;
                } else {
                    node = nodes.get(node + 1);
                    pos++;
                }
            } else {
                node = nodes.get(node + 2);
            }
        }
        if (node == -1) {
            return defaultValue;
        } else {
            return nodes.get(node + 3);
        }
    }
    
    public void put(String key, int value) {
        root = put(root, key, 0, value);
    }
    
    private int put(int node, String key, int pos, int value) {
        char chr = key.charAt(pos);
        if (node == -1) {
            node = nodes.size();
            nodes.add(new int[]{-1, -1, -1, -1});
            labels.add(chr);
        }
        if (chr < labels.get(node/4)) {
            nodes.set(node, put(nodes.get(node), key, pos, value));
        } else if (chr == labels.get(node/4)) {
            if (pos < key.length()  - 1) {
                nodes.set(node + 1, put(nodes.get(node + 1), key, pos + 1, value));
            } else {
                nodes.set(node + 3, value);
            }
        } else {
             nodes.set(node + 2, put(nodes.get(node + 2), key, pos, value));
        }
        return node;
    }
    
    public String getContent() {
        // JH: It's not a good idea to have the whole trie represented in the
        // toString method - this is also used in the debugger and should be concise,
        // e.g.: TT (X nodes).
        String repr = getContent(root, "", "");
        return repr;
    }
    
    private String getContent(int node, String repr, String prefix) {
        if (node != -1) {
            if (nodes.get(node + 3) != -1) {
                repr += prefix + (char)labels.get(node/4) + " , " + nodes.get(node + 3) + "\n";
            }
            repr = getContent(nodes.get(node), repr, prefix);
            repr = getContent(nodes.get(node + 1), repr, prefix + (char)labels.get(node/4));
            repr = getContent(nodes.get(node + 2), repr, prefix);
        }
        return repr;
    }
}
