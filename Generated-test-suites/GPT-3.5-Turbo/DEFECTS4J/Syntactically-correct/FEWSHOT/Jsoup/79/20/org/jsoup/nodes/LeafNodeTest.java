package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LeafNodeTest {
    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leaf";
            }
        };

        assertFalse(leafNode.hasAttributes());
        leafNode.attr("attr1", "value1");
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leaf";
            }
        };

        assertNull(leafNode.attributes());
        leafNode.attr("attr1", "value1");
        assertEquals("value1", leafNode.attributes().get("attr1"));
    }

    @Test
    public void testCoreValueGetAndSet() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leaf";
            }
        };

        assertNull(leafNode.coreValue());
        leafNode.coreValue("value1");
        assertEquals("value1", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leaf";
            }
        };

        assertNull(leafNode.attr("attr1"));
        leafNode.attr("attr1", "value1");
        assertEquals("value1", leafNode.attr("attr1"));

        leafNode.coreValue("value2");
        assertEquals("value2", leafNode.attr("leaf"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leaf";
            }
        };

        assertFalse(leafNode.hasAttr("attr1"));
        leafNode.attr("attr1", "value1");
        assertTrue(leafNode.hasAttr("attr1"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leaf";
            }
        };

        assertNull(leafNode.removeAttr("attr1"));
        leafNode.attr("attr1", "value1");
        assertEquals("value1", leafNode.removeAttr("attr1"));

        leafNode.coreValue("value2");
        assertEquals("value2", leafNode.removeAttr("leaf"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leaf";
            }
        };

        assertEquals("", leafNode.absUrl("attr1"));
        leafNode.attr("attr1", "value1");
        assertEquals("value1", leafNode.absUrl("attr1"));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leaf";
            }
        };

        assertEquals("", leafNode.baseUri());

        Document document = new Document("");
        document.appendChild(leafNode);
        assertEquals("", leafNode.baseUri());

        document.setBaseUri("http://example.com");
        assertEquals("http://example.com", leafNode.baseUri());
    }

    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leaf";
            }
        };

        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leaf";
            }
        };

        leafNode.ensureChildNodes();
    }
}