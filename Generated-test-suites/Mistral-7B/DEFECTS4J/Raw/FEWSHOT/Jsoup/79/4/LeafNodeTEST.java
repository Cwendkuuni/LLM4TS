package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new Elements.SingleTagElement("test") {};
        assertFalse(leafNode.hasAttributes());

        Attributes attributes = new Attributes();
        attributes.put("attrKey", "attrValue");
        leafNode.coreValue("test-value");
        leafNode.value = attributes;
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new Elements.SingleTagElement("test") {};
        assertNull(leafNode.attributes());

        Attributes attributes = new Attributes();
        attributes.put("attrKey1", "attrValue1");
        attributes.put("attrKey2", "attrValue2");
        leafNode.value = attributes;
        assertEquals(attributes, leafNode.attributes());
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode leafNode = new Elements.SingleTagElement("test") {};
        assertNull(leafNode.attributes());
        leafNode.value = "test-value";
        leafNode.ensureAttributes();
        Attributes attributes = new Attributes();
        attributes.put("attrKey", "test-value");
        assertEquals(attributes, leafNode.attributes());
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new Elements.SingleTagElement("test") {};
        assertEquals("test", leafNode.coreValue());

        leafNode.coreValue("test-value");
        assertEquals("test-value", leafNode.coreValue());
    }

    @Test
    public void testCoreValueSetter() {
        LeafNode leafNode = new Elements.SingleTagElement("test") {};
        leafNode.coreValue("test-value");
        assertEquals("test-value", leafNode.coreValue());

        leafNode.coreValue("updated-value");
        assertEquals("updated-value", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new Elements.SingleTagElement("test") {};
        assertEquals("", leafNode.attr("key"));

        LeafNode leafNodeWithAttr = new Elements.SingleTagElement("test") {};
        leafNodeWithAttr.attr("key", "value");
        assertEquals("value", leafNodeWithAttr.attr("key"));
        assertEquals("test", leafNodeWithAttr.coreValue());

        leafNodeWithAttr.attr("key2", "value2");
        assertEquals("value", leafNodeWithAttr.attr("key"));
        assertEquals("value2", leafNodeWithAttr.attr("key2"));
        assertEquals("test value", leafNodeWithAttr.toString());
    }

    @Test
    public void testAttrSetter() {
        LeafNode leafNode = new Elements.SingleTagElement("test") {};
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
        assertEquals("test", leafNode.coreValue());

        leafNode.attr("key", "updated-value");
        assertEquals("updated-value", leafNode.attr("key"));
        assertEquals("test", leafNode.coreValue());

        leafNode.attr("key2", "value2");
        assertEquals("value", leafNode.attr("key"));
        assertEquals("updated-value", leafNode.attr("key2"));
        assertEquals("test updated-value", leafNode.toString());
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new Elements.SingleTagElement("test") {};
        assertFalse(leafNode.hasAttr("key"));

        LeafNode leafNodeWithAttr = new Elements.SingleTagElement("test") {};
        leafNodeWithAttr.attr("key", "value");
        assertTrue(leafNodeWithAttr.hasAttr("key"));
        assertFalse(leafNodeWithAttr.hasAttr("non-existent-key"));

        leafNodeWithAttr.attr("key2", "value2");
        assertTrue(leafNodeWithAttr.hasAttr("key"));
        assertTrue(leafNodeWithAttr.hasAttr("key2"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNodeWithAttr = new Elements.SingleTagElement("test") {};
        leafNodeWithAttr.attr("key", "value");
        leafNodeWithAttr.attr("key2", "value2");
        assertEquals("test", leafNodeWithAttr.coreValue());
        assertEquals("value", leafNodeWithAttr.attr("key"));
        assertEquals("value2", leafNodeWithAttr.attr("key2"));

        leafNodeWithAttr.removeAttr("key");
        assertEquals("test", leafNodeWithAttr.coreValue());
        assertNull(leafNodeWithAttr.attr("key"));

        leafNodeWithAttr.removeAttr("key2");
        assertEquals("test", leafNodeWithAttr.coreValue());
        assertNull(leafNodeWithAttr.attr("key2"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new Elements.SingleTagElement("test") {};
        assertEquals("", leafNode.absUrl("key"));

        LeafNode leafNodeWithBaseUri = new Elements.SingleTagElement("test") {};
        leafNodeWithBaseUri.parent().attr("href", "http://test.com");
        assertEquals("http://test.com/test", leafNodeWithBaseUri.absUrl(""));
        assertEquals("", leafNodeWithBaseUri.absUrl("non-existent-key"));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new Elements.SingleTagElement("test") {};
        assertEquals("", leafNode.baseUri());

        LeafNode leafNodeWithParent = new Elements.SingleTagElement("test") {};
        Elements parent = new Elements.SingleTagElement("parent") {};
        parent.attr("href", "http://test.com");
        leafNodeWithParent.parent(parent);
        assertEquals("http://test.com", leafNodeWithParent.baseUri());

        LeafNode leafNodeWithoutParent = new Elements.SingleTagElement("test") {};
        assertEquals("", leafNodeWithoutParent.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        LeafNode leafNode = new Elements.SingleTagElement("test") {};
        leafNode.doSetBaseUri("http://test.com");
        assertEquals("", leafNode.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildNodeSize() {
        LeafNode leafNode = new Elements.SingleTagElement("test") {};
        leafNode.childNodeSize();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new Elements.SingleTagElement("test") {};
        leafNode.ensureChildNodes();
    }
}