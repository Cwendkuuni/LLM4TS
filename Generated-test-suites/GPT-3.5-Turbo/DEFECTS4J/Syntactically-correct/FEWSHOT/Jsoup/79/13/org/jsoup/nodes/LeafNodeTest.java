package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

public class LeafNodeTest {
    private LeafNode leafNode;

    @Before
    public void setUp() {
        leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leafNode";
            }
        };
    }

    @Test
    public void testHasAttributes() {
        assertFalse(leafNode.hasAttributes());
        leafNode.value = new Attributes();
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        assertFalse(leafNode.hasAttributes());
        Attributes attributes = new Attributes();
        leafNode.value = attributes;
        assertEquals(attributes, leafNode.attributes());
    }

    @Test
    public void testEnsureAttributes() {
        assertFalse(leafNode.hasAttributes());

        leafNode.ensureAttributes();

        assertTrue(leafNode.hasAttributes());
        assertTrue(leafNode.value instanceof Attributes);

        Attributes attributes = (Attributes) leafNode.value;
        assertNull(attributes.get("leafNode"));

        leafNode.value = "value";
        leafNode.ensureAttributes();

        assertTrue(leafNode.hasAttributes());
        assertTrue(leafNode.value instanceof Attributes);

        attributes = (Attributes) leafNode.value;
        assertEquals("value", attributes.get("leafNode"));
    }

    @Test
    public void testCoreValue() {
        leafNode.coreValue("value");
        assertEquals("value", leafNode.coreValue());

        leafNode.value = new Attributes();
        assertNull(leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        Validate.notNull("key");

        assertFalse(leafNode.hasAttributes());
        assertEquals("key", leafNode.attr("key"));

        leafNode.value = new Attributes();
        Attributes attributes = (Attributes) leafNode.value;
        attributes.put("leafNode", "value");

        assertEquals("value", leafNode.attr("leafNode"));
    }

    @Test
    public void testSetAttr() {
        String key = "key";
        String value = "value";

        assertFalse(leafNode.hasAttributes());

        leafNode.attr(key, value);
        assertTrue(leafNode.hasAttributes());

        Attributes attributes = (Attributes) leafNode.value;
        assertEquals(value, attributes.get(key));

        leafNode.attr("otherKey", "otherValue");
        attributes = (Attributes) leafNode.value;
        assertEquals(value, attributes.get(key));
        assertEquals("otherValue", attributes.get("otherKey"));
    }

    @Test
    public void testHasAttr() {
        assertFalse(leafNode.hasAttr("key"));

        leafNode.value = new Attributes();
        Attributes attributes = (Attributes) leafNode.value;
        attributes.put("key", "value");

        assertTrue(leafNode.hasAttr("key"));
        assertFalse(leafNode.hasAttr("otherKey"));
    }

    @Test
    public void testRemoveAttr() {
        assertFalse(leafNode.hasAttributes());

        leafNode.removeAttr("key");

        leafNode.value = new Attributes();
        Attributes attributes = (Attributes) leafNode.value;
        attributes.put("key", "value");

        leafNode.removeAttr("key");
        assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void testAbsUrl() {
        assertFalse(leafNode.hasAttributes());
        assertEquals("", leafNode.absUrl("key"));

        leafNode.value = new Attributes();
        Attributes attributes = (Attributes) leafNode.value;
        attributes.put("absUrl", "value");

        assertEquals("value", leafNode.absUrl("absUrl"));
    }

    @Test
    public void testBaseUri() {
        assertFalse(leafNode.hasParent());
        assertEquals("", leafNode.baseUri());

        element.parent = new Element("tag");
        assertFalse(leafNode.hasParent());
        assertEquals("", leafNode.baseUri());

        element.parent.baseUri = "http://example.com";
        assertTrue(leafNode.hasParent());
        assertEquals("http://example.com", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        assertFalse(leafNode.hasAttributes());

        leafNode.doSetBaseUri("http://example.com");
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testChildNodeSize() {
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        leafNode.ensureChildNodes();
    }
}