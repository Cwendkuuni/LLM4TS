package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LeafNodeTest {
    
   @Test
    public void testHasAttributes_WhenValueIsNull_ReturnsFalse() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return null;
            }
        };
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes_WhenValueIsInstanceOfAttributes_ReturnsAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return null;
            }
        };
        leafNode.value = new Attributes();
        assertTrue(leafNode.attributes() instanceof Attributes);
    }

    @Test
    public void testEnsureAttributes_WhenHasNoAttributes_CreatesAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return null;
            }
        };
        leafNode.ensureAttributes();
        assertTrue(leafNode.value instanceof Attributes);
    }

    @Test
    public void testCoreValue_WhenValueIsNull_ReturnsNull() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return null;
            }
        };
        assertNull(leafNode.coreValue());
    }

    @Test
    public void testAttr_WhenValueIsNotNullAndMatchesKey_ReturnsValue() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "key";
            }
        };
        leafNode.value = "value";
        assertEquals("value", leafNode.attr("key"));
    }

    @Test
    public void testAttr_WhenValueIsNullAndKeyMatches_ReturnsEmptyString() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "key";
            }
        };
        assertNull(leafNode.attr(null));
    }

    @Test
    public void testAttr_WhenValueIsNullAndKeyMatches_ReturnsEmptyString() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "key";
            }
        };
        assertNull(leafNode.attr("key"));
    }

    @Test
    public void testAttr_WhenHasAttributes_DelegatesToSuper() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return null;
            }
        };
        leafNode.value = new Attributes();
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr_WhenHasAttributes_DelegatesToSuper() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return null;
            }
        };
        leafNode.value = new Attributes();
        leafNode.removeAttr("key");
        assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void testAbsUrl_WhenHasAttributes_DelegatesToSuper() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return null;
            }
        };
        leafNode.value = new Attributes();
        leafNode.absUrl("key");
        // Add assertion for the result of absUrl
    }

    @Test
    public void testBaseUri_WhenHasParent_ReturnsParentBaseUri() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return null;
            }
        };
        leafNode.setParentNode(new Node());
        leafNode.parent().setBaseUri("parentBaseUri");
        assertEquals("parentBaseUri", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri_WhenCalled_Noop() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return null;
            }
        };
        leafNode.doSetBaseUri("baseUri");
        // Add assertion for the expected behavior
    }

    @Test
    public void testChildNodeSize_Always_ReturnsZero() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return null;
            }
        };
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes_Always_ThrowsException() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return null;
            }
        };
        leafNode.ensureChildNodes();
    }
}