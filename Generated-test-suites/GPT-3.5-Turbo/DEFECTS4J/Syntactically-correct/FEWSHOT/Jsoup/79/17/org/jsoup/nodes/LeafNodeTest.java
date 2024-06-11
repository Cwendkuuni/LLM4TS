package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setUp() {
        leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "leaf";
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
        assertNull(leafNode.attributes());

        Attributes attributes = new Attributes();
        leafNode.value = attributes;
        assertEquals(attributes, leafNode.attributes());
    }

    @Test
    public void testEnsureAttributes() {
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());

        Object coreValue = "value";
        leafNode.value = coreValue;
        leafNode.ensureAttributes();

        assertTrue(leafNode.hasAttributes());
        assertEquals(coreValue, leafNode.attributes().get("leaf"));
    }

    @Test
    public void testCoreValue() {
        assertNull(leafNode.coreValue());

        String value = "value";
        leafNode.coreValue(value);
        assertEquals(value, leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        String key = "key";
        String value = "value";
        leafNode.value = value;

        assertEquals(value, leafNode.attr(key));
        assertEquals(value, leafNode.attr("leaf"));
        assertNull(leafNode.attr("non-existent"));
    }

    @Test
    public void testAttrSet() {
        String key = "key";
        String value = "value";
        leafNode.attr(key, value);

        assertEquals(value, leafNode.value);
        assertNull(leafNode.attributes().get(key));
        assertEquals(value, leafNode.attributes().get("leaf"));
    }

    @Test
    public void testHasAttr() {
        assertFalse(leafNode.hasAttr("key"));

        leafNode.ensureAttributes();
        assertFalse(leafNode.hasAttr("key"));

        String key = "key";
        String value = "value";
        leafNode.attr(key, value);

        assertTrue(leafNode.hasAttr(key));
    }

    @Test
    public void testRemoveAttr() {
        assertNull(leafNode.removeAttr("key"));

        leafNode.attr("key", "value");
        assertNotNull(leafNode.removeAttr("key"));
        assertFalse(leafNode.hasAttr("key"));
        assertNull(leafNode.removeAttr("non-existent"));
    }

    @Test
    public void testAbsUrl() {
        assertNull(leafNode.absUrl("key"));

        leafNode.ensureAttributes();
        assertNull(leafNode.absUrl("key"));
    }

    @Test
    public void testBaseUri() {
        assertEquals("", leafNode.baseUri());

        LeafNode parentNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "parent";
            }
        };
        parentNode.value = "parent value";

        leafNode.setParent(parentNode);
        assertEquals(parentNode.baseUri(), leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        leafNode.doSetBaseUri("baseUri");
        // No operation
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