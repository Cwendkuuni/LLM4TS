package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

public class LeafNodeTest {

    LeafNode leafNode;

    @Before
    public void setUp() {
        leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "test";
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

        leafNode.value = new Attributes();
        assertTrue(leafNode.attributes() instanceof Attributes);
    }

    @Test
    public void testEnsureAttributes() {
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
        assertTrue(leafNode.attributes() instanceof Attributes);

        leafNode.value = "value";
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
        assertTrue(leafNode.attributes() instanceof Attributes);
        assertEquals("value", leafNode.attributes().get("test"));
    }

    @Test
    public void testCoreValue() {
        assertNull(leafNode.coreValue());

        leafNode.attr("test", "value");
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testSetCoreValue() {
        leafNode.coreValue("value");
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        assertNull(leafNode.attr("key"));

        leafNode.attr("test", "value");
        assertEquals("value", leafNode.attr("test"));

        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
    }

    @Test
    public void testHasAttr() {
        assertFalse(leafNode.hasAttr("key"));

        leafNode.attr("test", "value");
        assertTrue(leafNode.hasAttr("test"));
    }

    @Test
    public void testRemoveAttr() {
        assertFalse(leafNode.hasAttr("key"));

        leafNode.attr("test", "value");
        assertTrue(leafNode.hasAttr("test"));

        leafNode.removeAttr("test");
        assertFalse(leafNode.hasAttr("test"));
    }

    @Test
    public void testAbsUrl() {
        leafNode.attr("href", "/path");

        assertEquals("/path", leafNode.absUrl("href"));
    }

    @Test
    public void testBaseUri() {
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        leafNode.doSetBaseUri("baseUri");
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