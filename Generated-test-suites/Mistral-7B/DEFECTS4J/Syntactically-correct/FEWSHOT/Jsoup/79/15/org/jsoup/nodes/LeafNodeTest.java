package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode node = new Elements.SingleTagElement("testTag");
        assertFalse(node.hasAttributes());

        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        node.coreValue("testValue");
        assertTrue(node.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode node = new Elements.SingleTagElement("testTag");
        Attributes attributes = node.attributes();
        assertNull(attributes);

        Attributes attributesWithValue = new Attributes();
        attributesWithValue.put("testKey", "testValue");
        node.coreValue("testValue");
        assertEquals(attributesWithValue, node.attributes());
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode node = new Elements.SingleTagElement("testTag");
        assertNull(node.value);
        node.coreValue("testValue");

        Attributes attributes = new Attributes();
        assertSame(attributes, node.value);
        attributes.put("testKey", "testValue");

        Attributes attributesAfterCoreValueChange = node.attributes();
        assertEquals(attributes, attributesAfterCoreValueChange);
    }

    @Test
    public void testCoreValue() {
        LeafNode node = new Elements.SingleTagElement("testTag");
        node.coreValue("testValue");
        assertEquals("testValue", node.text());
    }

    @Test
    public void testCoreValueSet() {
        LeafNode node = new Elements.SingleTagElement("testTag");
        node.coreValue("testValue");

        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValueSet");
        node.attr("testKey", "testValueSet");

        assertEquals("testValue", node.coreValue());
        assertEquals("testValueSet", node.attr("testKey"));
    }

    @Test
    public void testAttr() {
        LeafNode node = new Elements.SingleTagElement("testTag");
        String key = "testKey";

        node.attr(key, "testValue");
        assertEquals("testValue", node.attr(key));

        node.coreValue("testValue");
        assertEquals("testValue", node.attr(key));

        node.attr(key, null);
        assertNull(node.attr(key));

        node.ensureAttributes();
        node.attr(key, "testValue");
        assertEquals("testValue", node.attr(key));
    }

    @Test
    public void testHasAttr() {
        LeafNode node = new Elements.SingleTagElement("testTag");
        String key = "testKey";

        assertFalse(node.hasAttr(key));

        node.coreValue("testValue");
        assertTrue(node.hasAttr(key));

        node.attr(key, null);
        assertFalse(node.hasAttr(key));

        node.ensureAttributes();
        node.attr(key, "testValue");
        assertTrue(node.hasAttr(key));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode node = new Elements.SingleTagElement("testTag");
        String key = "testKey";

        node.coreValue("testValue");
        node.attr(key, "testValue");
        node.removeAttr(key);
        assertNull(node.attr(key));
    }

    @Test
    public void testAbsUrl() {
        LeafNode node = new TextNode("testValue");
        assertNull(node.absUrl("testKey"));

        Attributes attributes = new Attributes();
        attributes.put("href", "testUrl");
        node.coreValue("testValue");
        node.attr("href", "testUrl");
        assertEquals("testUrl", node.absUrl("href"));
    }

    @Test
    public void testBaseUri() {
        LeafNode node = new Elements.SingleTagElement("testTag");
        Element parent = new Elements.SingleTagElement("testParent");
        parent.coreValue("testParentValue");
        parent.attr("id", "testParentId");
        node.parent(parent);
        assertEquals("testParentValue", node.baseUri());

        node = new TextNode("testValue");
        assertTrue(node.baseUri().isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetBaseUri() {
        LeafNode node = new TextNode("testValue");
        node.setBaseUri("testBaseUri");
    }

    @Test
    public void testChildNodeSize() {
        LeafNode node = new TextNode("testValue");
        assertEquals(0, node.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode node = new TextNode("testValue");
        node.ensureChildNodes();
    }
}