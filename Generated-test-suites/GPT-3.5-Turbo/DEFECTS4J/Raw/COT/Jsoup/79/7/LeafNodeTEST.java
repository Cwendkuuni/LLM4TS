
package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class LeafNodeTest {

    private LeafNode leafNode;
    private Attributes attributes;

    @Before
    public void setUp() {
        leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return null;
            }
        };
        attributes = new Attributes();
    }

    @Test
    public void testHasAttributes() {
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        assertNull(leafNode.attributes());

        leafNode.value = attributes;
        assertSame(attributes, leafNode.attributes());
    }

    @Test
    public void testEnsureAttributes_noExistingAttributes() {
        assertNull(leafNode.value);

        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
        assertTrue(leafNode.attributes().isEmpty());
    }

    @Test
    public void testEnsureAttributes_existingAttributes() {
        assertNull(leafNode.value);

        leafNode.value = "value";
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
        assertTrue(leafNode.attributes().isEmpty());
        assertTrue(leafNode.attributes().hasKey("value"));
    }

    @Test
    public void testCoreValue() {
        assertNull(leafNode.coreValue());

        leafNode.attr("attr", "value");
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testCoreValue_setValue() {
        assertNull(leafNode.coreValue());

        leafNode.coreValue("value");
        assertEquals("value", leafNode.coreValue());
        assertEquals("value", leafNode.attr("attr"));
    }

    @Test
    public void testAttr_noAttributes() {
        assertNull(leafNode.attr("key"));
    }

    @Test
    public void testAttr_withAttributes() {
        leafNode.value = attributes;
        assertNull(leafNode.attr("key"));

        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
    }

    @Test
    public void testAttr_nullKey() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            leafNode.attr(null);
        });
        assertEquals("Key must not be null", exception.getMessage());
    }

    @Test
    public void testAttr_nullValue() {
        leafNode.value = attributes;

        leafNode.attr("key", null);
        assertNull(leafNode.attr("key"));
    }

    @Test
    public void testHasAttr() {
        assertFalse(leafNode.hasAttr("key"));

        leafNode.value = attributes;
        assertFalse(leafNode.hasAttr("key"));

        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr() {
        assertNull(leafNode.removeAttr("key"));

        leafNode.value = attributes;
        assertNull(leafNode.removeAttr("key"));

        leafNode.attr("key", "value");
        assertEquals("value", leafNode.removeAttr("key"));
        assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void testAbsUrl_noParent() {
        assertEquals("", leafNode.absUrl("key"));
    }

    @Test
    public void testAbsUrl_withParent() {
        LeafNode parent = new LeafNode() {
            @Override
            protected String nodeName() {
                return null;
            }

            @Override
            public String absUrl(String key) {
                return "http://example.com";
            }
        };
        parent.value = attributes;
        leafNode.setParentNode(parent);

        assertEquals("http://example.com", leafNode.absUrl("key"));
    }

    @Test
    public void testBaseUri_noParent() {
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testBaseUri_withParent() {
        LeafNode parent = new LeafNode() {
            @Override
            protected String nodeName() {
                return null;
            }

            @Override
            public String baseUri() {
                return "http://example.com";
            }
        };
        leafNode.setParentNode(parent);

        assertEquals("http://example.com", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        assertDoesNotThrow(() -> {
            leafNode.doSetBaseUri("http://example.com");
        });
    }

    @Test
    public void testChildNodeSize() {
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test
    public void testEnsureChildNodes() {
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> {
            leafNode.ensureChildNodes();
        });
        assertEquals("Leaf Nodes do not have child nodes.", exception.getMessage());
    }
}
