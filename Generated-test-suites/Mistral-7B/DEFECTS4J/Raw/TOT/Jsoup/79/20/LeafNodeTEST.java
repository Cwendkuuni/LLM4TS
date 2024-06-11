package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;

public class LeafNodeTest {
    @org.junit.Test
    public void testHasAttributes() {
        LeafNode leafNode = new Elements().element("div"); // create an instance of LeafNode

        // Test when hasAttributes() returns false
        assertFalse(leafNode.hasAttributes());
        leafNode.coreValue("someValue");
        assertFalse(leafNode.hasAttributes());

        // Test when hasAttributes() returns true
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        leafNode.value = attributes;
        assertTrue(leafNode.hasAttributes());

        // Test when the value is an empty Attributes object
        leafNode.value = new Attributes();
        assertTrue(leafNode.hasAttributes());
    }
}