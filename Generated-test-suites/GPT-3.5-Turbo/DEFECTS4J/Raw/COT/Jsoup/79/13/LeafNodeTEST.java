
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.junit.Test;

import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        // Arrange
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };

        // Act
        boolean hasAttributes = leafNode.hasAttributes();

        // Assert
        assertFalse(hasAttributes);
    }

    @Test
    public void testAttributes() {
        // Arrange
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };

        // Act
        Attributes attributes = leafNode.attributes();

        // Assert
        assertNotNull(attributes);
    }

    @Test
    public void testCoreValue() {
        // Arrange
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };

        // Act
        String coreValue = leafNode.coreValue();

        // Assert
        assertNull(coreValue);
    }

    @Test
    public void testCoreValue_withValue() {
        // Arrange
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        leafNode.coreValue("testValue");

        // Act
        String coreValue = leafNode.coreValue();

        // Assert
        assertEquals("testValue", coreValue);
    }

    @Test
    public void testAttr() {
        // Arrange
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        leafNode.coreValue("testValue");

        // Act
        String attr = leafNode.attr("key");

        // Assert
        assertNull(attr);
    }

    @Test
    public void testAttr_withAttributes() {
        // Arrange
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "nodeName";
            }
        };
        leafNode.attr("key", "value");

        // Act
        String attr = leafNode.attr("key");

        // Assert
        assertEquals("value", attr);
    }

    @Test
    public void testAttr_withNullKey() {
        // Arrange
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "nodeName";
            }
        };

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> leafNode.attr(null));
    }

    @Test
    public void testAttr_withNullValue() {
        // Arrange
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };

        // Act
        leafNode.attr("key", null);
        String attr = leafNode.attr("key");

        // Assert
        assertNull(attr);
    }

    @Test
    public void testHasAttr() {
        // Arrange
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "nodeName";
            }
        };
        leafNode.attr("key", "value");

        // Act
        boolean hasAttr = leafNode.hasAttr("key");

        // Assert
        assertTrue(hasAttr);
    }

    @Test
    public void testRemoveAttr() {
        // Arrange
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "nodeName";
            }
        };
        leafNode.attr("key", "value");

        // Act
        Node removedAttrNode = leafNode.removeAttr("key");
        String attr = leafNode.attr("key");

        // Assert
        assertEquals("value", removedAttrNode);
        assertNull(attr);
    }

    @Test
    public void testAbsUrl() {
        // Arrange
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "nodeName";
            }
        };
        leafNode.attr("href", "/test");

        // Act
        String absUrl = leafNode.absUrl("href");

        // Assert
        assertEquals("http://localhost/test", absUrl);
    }

    @Test
    public void testBaseUri_withParent() {
        // Arrange
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        Node parent = new Node() {
            @Override
            public String baseUri() {
                return "http://example.com";
            }
        };
        leafNode.parent = parent;

        // Act
        String baseUri = leafNode.baseUri();

        // Assert
        assertEquals("http://example.com", baseUri);
    }

    @Test
    public void testBaseUri_withoutParent() {
        // Arrange
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };

        // Act
        String baseUri = leafNode.baseUri();

        // Assert
        assertEquals("", baseUri);
    }

    @Test
    public void testChildNodeSize() {
        // Arrange
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };

        // Act
        int childNodeSize = leafNode.childNodeSize();

        // Assert
        assertEquals(0, childNodeSize);
    }

    @Test
    public void testEnsureChildNodes() {
        // Arrange
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };

        // Act & Assert
        assertThrows(UnsupportedOperationException.class, leafNode::ensureChildNodes);
    }
}
