
package org.jsoup.nodes;

import org.jsoup.helper.Validate;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "testNode";
            }
        };

        assertFalse(leafNode.hasAttributes());

        leafNode.value = new Attributes();
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "testNode";
            }
        };

        assertNull(leafNode.attributes());

        leafNode.value = new Attributes();
        assertNotNull(leafNode.attributes());
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "testNode";
            }
        };

        assertNull(leafNode.coreValue());

        leafNode.attr("testNode", "value");
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testCoreValueSet() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "testNode";
            }
        };

        assertNull(leafNode.coreValue());

        leafNode.coreValue("value");
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "testNode";
            }
        };

        assertNull(leafNode.attr("testAttr"));

        leafNode.attr("testAttr", "testValue");
        assertEquals("testValue", leafNode.attr("testAttr"));
    }

    @Test
    public void testAttrWithNullKey() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "testNode";
            }
        };

        try {
            leafNode.attr(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void testAttrWithValueAsNodeName() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "testNode";
            }
        };

        assertNull(leafNode.attr("testNode"));

        leafNode.value = "value";
        assertEquals("value", leafNode.attr("testNode"));
    }

    @Test
    public void testAttrWithAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "testNode";
            }
        };

        assertNull(leafNode.attr("testAttr"));

        leafNode.value = new Attributes();
        leafNode.attr("testAttr", "testValue");
        assertEquals("testValue", leafNode.attr("testAttr"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "testNode";
            }
        };

        assertFalse(leafNode.hasAttr("testAttr"));

        leafNode.value = new Attributes();
        assertFalse(leafNode.hasAttr("testAttr"));

        leafNode.attr("testAttr", "testValue");
        assertTrue(leafNode.hasAttr("testAttr"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "testNode";
            }
        };

        assertNull(leafNode.removeAttr("testAttr"));

        leafNode.value = new Attributes();
        assertNull(leafNode.removeAttr("testAttr"));

        leafNode.attr("testAttr", "testValue");
        assertEquals("testValue", leafNode.removeAttr("testAttr"));
        assertFalse(leafNode.hasAttr("testAttr"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "testNode";
            }
        };

        assertNull(leafNode.absUrl("testAttr"));

        leafNode.value = new Attributes();
        assertNull(leafNode.absUrl("testAttr"));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "testNode";
            }
        };

        assertEquals("", leafNode.baseUri());

        leafNode.setParentNode(new Element("testParentNode"));

        assertEquals("testParentNode", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "testNode";
            }
        };

        // No-op method, no need to test
    }

    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "testNode";
            }
        };

        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "testNode";
            }
        };

        leafNode.ensureChildNodes();
    }
}
