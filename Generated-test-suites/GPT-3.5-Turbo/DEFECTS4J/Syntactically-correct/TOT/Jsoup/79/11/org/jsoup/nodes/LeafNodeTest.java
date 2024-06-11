package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testNode";
            }
        };

        assertFalse(leafNode.hasAttributes());

        leafNode.attr("attr1", "value1");
        assertTrue(leafNode.hasAttributes());

        leafNode.removeAttr("attr1");
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testNode";
            }
        };

        assertEquals(0, leafNode.attributes().size());

        leafNode.attr("attr1", "value1");
        assertEquals(1, leafNode.attributes().size());
        assertTrue(leafNode.attributes().hasKey("attr1"));
        assertEquals("value1", leafNode.attributes().get("attr1"));

        leafNode.attr("attr2", "value2");
        assertEquals(2, leafNode.attributes().size());
        assertTrue(leafNode.attributes().hasKey("attr2"));
        assertEquals("value2", leafNode.attributes().get("attr2"));

        leafNode.removeAttr("attr1");
        assertEquals(1, leafNode.attributes().size());
        assertFalse(leafNode.attributes().hasKey("attr1"));
        assertTrue(leafNode.attributes().hasKey("attr2"));
        assertEquals("value2", leafNode.attributes().get("attr2"));
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testNode";
            }
        };

        assertNull(leafNode.coreValue());

        leafNode.coreValue("value1");
        assertEquals("value1", leafNode.coreValue());

        leafNode.coreValue("value2");
        assertEquals("value2", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testNode";
            }
        };

        assertNull(leafNode.attr("attr1"));

        leafNode.attr("attr1", "value1");
        assertEquals("value1", leafNode.attr("attr1"));
        assertNull(leafNode.attr("attr2"));

        leafNode.attr("attr2", "value2");
        assertEquals("value1", leafNode.attr("attr1"));
        assertEquals("value2", leafNode.attr("attr2"));

        leafNode.removeAttr("attr2");
        assertEquals("value1", leafNode.attr("attr1"));
        assertNull(leafNode.attr("attr2"));

        leafNode.removeAttr("attr1");
        assertNull(leafNode.attr("attr1"));
        assertNull(leafNode.attr("attr2"));

        leafNode.attr("testNode", "value3");
        assertEquals("value3", leafNode.attr("testNode"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testNode";
            }
        };

        assertFalse(leafNode.hasAttr("attr1"));

        leafNode.attr("attr1", "value1");
        assertTrue(leafNode.hasAttr("attr1"));

        leafNode.removeAttr("attr1");
        assertFalse(leafNode.hasAttr("attr1"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testNode";
            }
        };

        assertNull(leafNode.absUrl("attr1"));

        leafNode.attr("attr1", "value1");
        assertNull(leafNode.absUrl("attr1"));

        leafNode.attr("href", "http://example.com/page");
        assertEquals("http://example.com/page", leafNode.absUrl("href"));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testNode";
            }
        };

        assertEquals("", leafNode.baseUri());

        Node parentNode = new Element("parent");
        parentNode.appendChild(leafNode);
        assertEquals("", leafNode.baseUri());

        parentNode.setBaseUri("http://example.com");
        assertEquals("http://example.com", leafNode.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDoSetBaseUri() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testNode";
            }
        };
        
        leafNode.setBaseUri("http://example.com");
        assertEquals("http://example.com", leafNode.baseUri());
    }

    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testNode";
            }
        };

        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testNode";
            }
        };

        leafNode.ensureChildNodes();
    }
}