package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new Element("test");
        assertFalse(leafNode.hasAttributes());

        Attributes attributes = new Attributes();
        attributes.put("attr1", "value1");
        leafNode.coreValue("attr1");
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new Element("test");
        Attributes attributes = new Attributes();
        leafNode.coreValue(attributes);
        assertEquals(attributes, leafNode.attributes());
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode leafNode = new Element("test");
        assertNull(leafNode.value);
        leafNode.coreValue("value");
        Attributes attributes = new Attributes();
        attributes.put("attr1", "value1");
        leafNode.ensureAttributes();
        assertEquals(attributes, leafNode.value);

        leafNode.coreValue(attributes);
        assertEquals(attributes, leafNode.value);
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new Element("test");
        leafNode.coreValue("value");
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testCoreValueWithAttributes() {
        LeafNode leafNode = new Element("test");
        Attributes attributes = new Attributes();
        leafNode.coreValue(attributes);
        assertEquals(attributes, leafNode.value);
        assertEquals(attributes.toString(), leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new Element("test");
        leafNode.attr("attr1", "value1");
        assertEquals("value1", leafNode.attr("attr1"));
        assertEquals("", leafNode.attr("nonExistentAttribute"));
        assertEquals("test", leafNode.attr("nodeName"));
    }

    @Test
    public void testAttrWithExistingCoreValue() {
        LeafNode leafNode = new Element("test");
        leafNode.coreValue("value1");
        leafNode.attr("attr1", "value2");
        assertEquals("value2", leafNode.attr("attr1"));
        assertEquals("value1", leafNode.coreValue());
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new Element("test");
        leafNode.coreValue("value1");
        leafNode.attr("attr1", "value2");
        leafNode.removeAttr("attr1");
        assertNull(leafNode.attr("attr1"));
        assertEquals("value1", leafNode.coreValue());
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new Element("test");
        leafNode.coreValue("value");
        assertFalse(leafNode.hasAttr("nonExistentAttribute"));

        leafNode.attr("attr1", "value1");
        assertTrue(leafNode.hasAttr("attr1"));

        leafNode.removeAttr("attr1");
        assertFalse(leafNode.hasAttr("attr1"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new Element("img");
        leafNode.attr("src", "test.html");
        assertEquals("test.html", leafNode.absUrl("src"));
    }

    @Test
    public void testBaseUri() {
        Element parent = new Element("div");
        Element leafNode = new Element("img");
        leafNode.attr("src", "test.html");
        parent.add(leafNode);
        assertEquals("", leafNode.baseUri());

        Element grandParent = new Element("body");
        grandParent.add(parent);
        parent.baseUri("http://test.com");
        assertEquals("http://test.com", leafNode.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildNodeSize() {
        LeafNode leafNode = new Element("test");
        leafNode.childNodeSize();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new Element("test");
        leafNode.ensureChildNodes();
    }
}