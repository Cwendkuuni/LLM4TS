package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new TextNode("Test");
        assertFalse(leafNode.hasAttributes());

        LeafNode leafNodeWithAttributes = new Elements.Element("test", new Attributes());
        assertTrue(leafNodeWithAttributes.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new TextNode("Test");
        assertNull(leafNode.attributes());

        LeafNode leafNodeWithAttributes = new Elements.Element("test", new Attributes().put("attr1", "val1").put("attr2", "val2"));
        Attributes attributes = leafNodeWithAttributes.attributes();
        assertEquals("val1", attributes.get("attr1"));
        assertEquals("val2", attributes.get("attr2"));
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.ensureAttributes();
        assertNull(leafNode.value);

        LeafNode leafNodeWithAttributes = new Elements.Element("test", new Attributes().put("attr1", "val1").put("attr2", "val2"));
        leafNodeWithAttributes.ensureAttributes();
        assertEquals(new Attributes(), leafNodeWithAttributes.value);
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new TextNode("Test");
        assertEquals("Test", leafNode.coreValue());

        LeafNode leafNodeWithAttributes = new Elements.Element("test", new Attributes().put("attr1", "val1").put("test", "Test"));
        assertEquals("Test", leafNodeWithAttributes.coreValue());
    }

    @Test
    public void testCoreValueSetter() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.coreValue("New Test");
        assertEquals("New Test", leafNode.coreValue());

        LeafNode leafNodeWithAttributes = new Elements.Element("test", new Attributes().put("attr1", "val1").put("test", "Test"));
        leafNodeWithAttributes.coreValue("New Test");
        assertEquals("New Test", leafNodeWithAttributes.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new TextNode("Test");
        assertEquals("Test", leafNode.attr("nodeName"));

        LeafNode leafNodeWithAttributes = new Elements.Element("test", new Attributes().put("attr1", "val1"));
        assertEquals("val1", leafNodeWithAttributes.attr("attr1"));
        assertEquals("", leafNodeWithAttributes.attr("nonExistentAttr"));
    }

    @Test
    public void testAttrSetter() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.attr("nodeName", "New Test");
        assertEquals("New Test", leafNode.attr("nodeName"));

        LeafNode leafNodeWithAttributes = new Elements.Element("test", new Attributes().put("attr1", "val1"));
        leafNodeWithAttributes.attr("attr1", "New Val");
        assertEquals("New Val", leafNodeWithAttributes.attr("attr1"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new TextNode("Test");
        assertFalse(leafNode.hasAttr("nodeName"));

        LeafNode leafNodeWithAttributes = new Elements.Element("test", new Attributes().put("attr1", "val1"));
        assertTrue(leafNodeWithAttributes.hasAttr("attr1"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.removeAttr("nodeName");
        assertNull(leafNode.attr("nodeName"));

        LeafNode leafNodeWithAttributes = new Elements.Element("test", new Attributes().put("attr1", "val1"));
        leafNodeWithAttributes.removeAttr("attr1");
        assertFalse(leafNodeWithAttributes.hasAttr("attr1"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new TextNode("Test");
        assertNull(leafNode.absUrl("key"));

        LeafNode leafNodeWithBaseUri = new Elements.Element("test", new Attributes().put("src", "/test.html"));
        assertEquals("/test.html", leafNodeWithBaseUri.absUrl("src"));
    }

    @Test
    public void testBaseUri() {
        Element parent = new Elements.Element("parent");
        TextNode child = new TextNode("Test");
        parent.appendChild(child);
        assertEquals("", child.baseUri());

        TextNode sibling = new TextNode("Sibling");
        parent.appendChild(child);
        parent.appendChild(sibling);
        assertEquals(parent.baseUri(), sibling.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildNodes() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.childNodes();
    }
}