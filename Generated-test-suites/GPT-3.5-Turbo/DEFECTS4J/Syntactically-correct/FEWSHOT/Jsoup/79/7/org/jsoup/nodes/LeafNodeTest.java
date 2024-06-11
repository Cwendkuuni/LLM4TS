package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.List;
import org.junit.Test;

public class LeafNodeTest {

    private class LeafNodeImpl extends LeafNode {

        @Override
        public String nodeName() {
            return "leafNode";
        }
    }

    private LeafNodeImpl createLeafNode() {
        return new LeafNodeImpl();
    }

    @Test
    public void testHasAttributes() {
        LeafNodeImpl leafNode = createLeafNode();
        leafNode.value = new Attributes();
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributesWhenHasAttributes() {
        LeafNodeImpl leafNode = createLeafNode();
        Attributes attributes = new Attributes();
        attributes.put("attribute", "value");
        leafNode.value = attributes;
        assertEquals(attributes, leafNode.attributes());
    }

    @Test
    public void testAttributesWhenNoAttributes() {
        LeafNodeImpl leafNode = createLeafNode();
        assertNull(leafNode.attributes());
    }

    @Test
    public void testEnsureAttributesWhenNoAttributes() {
        LeafNodeImpl leafNode = createLeafNode();
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testEnsureAttributesWhenHasAttributes() {
        LeafNodeImpl leafNode = createLeafNode();
        Attributes attributes = new Attributes();
        attributes.put("attribute", "value");
        leafNode.value = attributes;
        leafNode.ensureAttributes();
        assertEquals(attributes, leafNode.attributes());
    }

    @Test
    public void testCoreValueWhenHasValue() {
        LeafNodeImpl leafNode = createLeafNode();
        leafNode.value = "value";
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testCoreValueWhenNoValue() {
        LeafNodeImpl leafNode = createLeafNode();
        assertNull(leafNode.coreValue());
    }

    @Test
    public void testCoreValueSetter() {
        LeafNodeImpl leafNode = createLeafNode();
        leafNode.coreValue("value");
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testAttrWhenNoAttributes() {
        LeafNodeImpl leafNode = createLeafNode();
        leafNode.value = "value";
        assertEquals("value", leafNode.attr("leafNode"));
        assertEquals("", leafNode.attr("attribute"));
    }

    @Test
    public void testAttrWhenHasAttributes() {
        LeafNodeImpl leafNode = createLeafNode();
        Attributes attributes = new Attributes();
        attributes.put("attribute", "value");
        leafNode.value = attributes;
        assertEquals("", leafNode.attr("leafNode"));
        assertEquals("value", leafNode.attr("attribute"));
    }

    @Test
    public void testAttrSetterWhenNoAttributes() {
        LeafNodeImpl leafNode = createLeafNode();
        leafNode.attr("leafNode", "value");
        assertEquals("value", leafNode.value);
    }

    @Test
    public void testAttrSetterWhenHasAttributes() {
        LeafNodeImpl leafNode = createLeafNode();
        Attributes attributes = new Attributes();
        attributes.put("attribute", "value");
        leafNode.value = attributes;
        leafNode.attr("leafNode", "newValue");
        assertEquals("newValue", leafNode.value);
    }

    @Test
    public void testHasAttrWhenNoAttributes() {
        LeafNodeImpl leafNode = createLeafNode();
        leafNode.value = "value";
        assertTrue(leafNode.hasAttr("leafNode"));
        assertFalse(leafNode.hasAttr("attribute"));
    }

    @Test
    public void testHasAttrWhenHasAttributes() {
        LeafNodeImpl leafNode = createLeafNode();
        Attributes attributes = new Attributes();
        attributes.put("attribute", "value");
        leafNode.value = attributes;
        assertFalse(leafNode.hasAttr("leafNode"));
        assertTrue(leafNode.hasAttr("attribute"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNodeImpl leafNode = createLeafNode();
        Attributes attributes = new Attributes();
        attributes.put("attribute", "value");
        leafNode.value = attributes;
        leafNode.removeAttr("attribute");
        assertFalse(leafNode.hasAttr("attribute"));
    }

    @Test
    public void testAbsUrl() {
        LeafNodeImpl leafNode = createLeafNode();
        Attributes attributes = new Attributes();
        attributes.put("attribute", "value");
        leafNode.value = attributes;
        assertEquals("", leafNode.absUrl("attribute"));
    }

    @Test
    public void testBaseUriWhenHasParent() {
        LeafNodeImpl leafNode = createLeafNode();
        Node parent = new Element("parent");
        parent.setBaseUri("http://example.com");
        leafNode.parent = parent;
        assertEquals("http://example.com", leafNode.baseUri());
    }

    @Test
    public void testBaseUriWhenNoParent() {
        LeafNodeImpl leafNode = createLeafNode();
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        LeafNodeImpl leafNode = createLeafNode();
        leafNode.doSetBaseUri("http://example.com");
        // Noop, nothing to assert here
    }

    @Test
    public void testChildNodeSize() {
        LeafNodeImpl leafNode = createLeafNode();
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNodeImpl leafNode = createLeafNode();
        leafNode.ensureChildNodes();
    }
}