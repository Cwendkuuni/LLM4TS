package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new Elements().htmlParser().parse("<div attr='value'></div>").first();
        assertTrue(leafNode.hasAttributes());

        LeafNode leafNodeWithoutAttributes = new Elements().htmlParser().parse("<div></div>").first();
        assertFalse(leafNodeWithoutAttributes.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new Elements().htmlParser().parse("<div attr='value'></div>").first();
        Attributes attributes = leafNode.attributes();
        assertEquals("value", attributes.get("attr"));

        leafNode.coreValue("newValue");
        attributes = leafNode.attributes();
        assertEquals("newValue", attributes.get("attr"));
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new Elements().htmlParser().parse("<div attr='value'></div>").first();
        assertEquals("value", leafNode.attr("attr"));
        assertNull(leafNode.attr("nonExistentAttr"));

        leafNode.coreValue("newValue");
        assertEquals("newValue", leafNode.attr("attr"));
    }

    @Test
    public void testAttrWithValue() {
        LeafNode leafNode = new Elements().htmlParser().parse("<div></div>").first();
        leafNode.attr("attr", "value");
        assertEquals("value", leafNode.attr("attr"));

        leafNode.coreValue("newValue");
        assertEquals("newValue", leafNode.attr("attr"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new Elements().htmlParser().parse("<div attr='value'></div>").first();
        assertTrue(leafNode.hasAttr("attr"));

        leafNode.coreValue("newValue");
        assertTrue(leafNode.hasAttr("attr"));

        LeafNode leafNodeWithoutAttr = new Elements().htmlParser().parse("<div></div>").first();
        assertFalse(leafNodeWithoutAttr.hasAttr("attr"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new Elements().htmlParser().parse("<div attr='value'></div>").first();
        leafNode.removeAttr("attr");
        assertNull(leafNode.attr("attr"));

        leafNode.coreValue("newValue");
        assertNull(leafNode.attr("attr"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new Elements().htmlParser().parse("<img src='test.html'></img>").first();
        assertEquals("test.html", leafNode.absUrl("src"));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new Elements().htmlParser().parse("<div></div>").first();
        assertEquals("", leafNode.baseUri());

        Element parentElement = new Elements().htmlParser().parse("<html><body><div></div></body></html>").first();
        leafNode = new Elements().htmlParser().parse("<div></div>", parentElement).first();
        assertEquals(parentElement.baseUri(), leafNode.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildNodeSize() {
        LeafNode leafNode = new Elements().htmlParser().parse("<div></div>").first();
        leafNode.childNodeSize();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new Elements().htmlParser().parse("<div></div>").first();
        leafNode.ensureChildNodes();
    }
}