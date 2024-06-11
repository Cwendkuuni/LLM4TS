package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        // Test when the value is an instance of Attributes
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };
        Attributes attributes = new Attributes();
        leafNode.value = attributes;

        assertTrue(leafNode.hasAttributes());

        // Test when the value is not an instance of Attributes
        leafNode.value = "testValue";
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        // Test when the value is an instance of Attributes
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };
        Attributes attributes = new Attributes();
        leafNode.value = attributes;

        assertEquals(attributes, leafNode.attributes());

        // Test when the value is not an instance of Attributes
        leafNode.value = "testValue";
        assertEquals(new Attributes(), leafNode.attributes());
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }

            public Object getValue() {
                return value;
            }
        };

        // Test when hasAttributes() is true
        leafNode.value = new Attributes();
        leafNode.ensureAttributes();
        assertEquals(new Attributes(), leafNode.getValue());

        // Test when hasAttributes() is false and value is null
        leafNode.value = null;
        leafNode.ensureAttributes();
        assertEquals(new Attributes(), leafNode.getValue());

        // Test when hasAttributes() is false and value is not null
        leafNode.value = "testValue";
        leafNode.ensureAttributes();
        assertEquals(new Attributes(), leafNode.getValue());
        assertEquals("testValue", leafNode.attributes().get("testNode"));
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        leafNode.attr("testNode", "testValue");
        assertEquals("testValue", leafNode.coreValue());
    }

    @Test
    public void testCoreValueWithNoAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        assertEquals("", leafNode.coreValue());
    }

    @Test
    public void testCoreValueSetter() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        leafNode.coreValue("testValue");
        assertEquals("testValue", leafNode.attr("testNode"));
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        // Test when hasAttributes() is false and key equals nodeName()
        leafNode.value = "testValue";
        assertEquals("testValue", leafNode.attr("testNode"));

        // Test when hasAttributes() is false and key does not equal nodeName()
        leafNode.value = "testValue";
        assertEquals("", leafNode.attr("otherNode"));

        // Test when hasAttributes() is true
        leafNode.value = new Attributes();
        assertEquals("", leafNode.attr("testNode"));
    }

    @Test
    public void testAttrSetter() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        // Test when hasAttributes() is false and key equals nodeName()
        leafNode.attr("testNode", "testValue");
        assertEquals("testValue", leafNode.value);

        // Test when hasAttributes() is false and key does not equal nodeName()
        leafNode.value = "testValue";
        leafNode.attr("otherNode", "otherValue");
        assertEquals(new Attributes(), leafNode.value);

        // Test when hasAttributes() is true
        leafNode.value = new Attributes();
        leafNode.attr("testNode", "testValue");
        assertEquals("testValue", leafNode.value);
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        // Test when hasAttributes() is false
        assertFalse(leafNode.hasAttr("testNode"));

        // Test when hasAttributes() is true
        leafNode.value = new Attributes();
        assertFalse(leafNode.hasAttr("testNode"));
        leafNode.attr("testNode", "testValue");
        assertTrue(leafNode.hasAttr("testNode"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        // Test when hasAttributes() is false
        assertFalse(leafNode.hasAttr("testNode"));
        assertEquals(leafNode, leafNode.removeAttr("testNode"));

        // Test when hasAttributes() is true
        leafNode.value = new Attributes();
        assertFalse(leafNode.hasAttr("testNode"));
        assertEquals(leafNode, leafNode.removeAttr("testNode"));
        leafNode.attr("testNode", "testValue");
        assertTrue(leafNode.hasAttr("testNode"));
        assertEquals(leafNode, leafNode.removeAttr("testNode"));
        assertFalse(leafNode.hasAttr("testNode"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        // Test when hasAttributes() is false
        assertNull(leafNode.absUrl("testNode"));

        // Test when hasAttributes() is true
        leafNode.value = new Attributes();
        assertNull(leafNode.absUrl("testNode"));
        leafNode.attr("testNode", "testValue");
        assertNull(leafNode.absUrl("testNode"));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        // Test when hasParent() is true
        Node parent = new Node() {
            @Override
            public String baseUri() {
                return "parentBaseUri";
            }
        };
        leafNode.parentNode = parent;
        assertEquals("parentBaseUri", leafNode.baseUri());

        // Test when hasParent() is false
        leafNode.parentNode = null;
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        leafNode.doSetBaseUri("testBaseUri");
        // No assertions as there is no functionality implemented in the method
    }

    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        leafNode.ensureChildNodes();
    }
}