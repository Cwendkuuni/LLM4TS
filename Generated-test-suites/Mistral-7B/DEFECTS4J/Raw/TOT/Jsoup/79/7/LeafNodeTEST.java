package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {
    @Test
    public void testHasAttributes() {
        LeafNode leafNode1 = new Elements().element("div").text("Test"); // create a LeafNode without attributes
        assertFalse(leafNode1.hasAttributes());

        LeafNode leafNode2 = new Elements().element("div", new Attributes().put("class", "test-class")); // create a LeafNode with attributes
        assertTrue(leafNode2.hasAttributes());
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode1 = new Elements().element("div").text("Test"); // create a LeafNode with a string value
        assertEquals("Test", leafNode1.coreValue());

        LeafNode leafNode2 = new Elements().element("div", new Attributes().put("class", "test-class")).text("Test2"); // create a LeafNode with an attribute map
        assertEquals("Test2", leafNode2.coreValue());
    }

    @Test
    public void testAttrCoreValue() {
        LeafNode leafNode = new Elements().element("div"); // create a LeafNode without an initial value

        // set core value and check if the attribute value is updated
        leafNode.coreValue("Test");
        assertEquals("Test", leafNode.attr(nodeName()));

        // set attribute value and check if the core value is updated
        leafNode.attr("test", "Test2");
        assertEquals("Test2", leafNode.coreValue());

        // test ensuring attributes are set when accessing an attribute that doesn't exist
        leafNode.attr("test3", "Test3");
        assertTrue(leafNode.hasAttributes());
        assertEquals("Test3", leafNode.attr("test3"));
    }
}