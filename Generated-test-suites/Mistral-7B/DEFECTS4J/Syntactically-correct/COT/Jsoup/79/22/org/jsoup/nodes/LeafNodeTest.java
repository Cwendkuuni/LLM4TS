import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.jsoup.helper.Validate;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                putAttribute("test", "value");
            }
        };

        assertTrue(leafNode.hasAttributes());

        leafNode = new LeafNode() {
            {
                value = "testValue";
            }
        };

        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                putAttribute("test", "value");
            }
        };

        Attributes attributes = leafNode.attributes();
        assertEquals("value", attributes.get("test"));

        leafNode = new LeafNode() {
            {
                value = "testValue";
            }
        };

        assertNull(leafNode.attributes());
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                putAttribute("test", "value");
            }
        };

        leafNode.ensureAttributes();
        Attributes attributes = (Attributes) leafNode.value;
        assertEquals("value", attributes.get("test"));

        leafNode = new LeafNode() {
            {
                value = "testValue";
            }
        };

        leafNode.ensureAttributes();
        assertSame(leafNode.value, leafNode.value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCoreValueNullValue() {
        LeafNode leafNode = new LeafNode();
        leafNode.coreValue(null);
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNode() {
            {
                value = "testValue";
            }
        };

        assertEquals("testValue", leafNode.coreValue());

        leafNode = new LeafNode() {
            {
                value = new Attributes();
                putAttribute("test", "value");
            }
        };

        assertEquals("test", leafNode.coreValue());
    }

    @Test
    public void testCoreValueSet() {
        LeafNode leafNode = new LeafNode() {
            {
                value = "testValue";
            }
        };

        leafNode.coreValue("newTestValue");
        assertEquals("newTestValue", leafNode.coreValue());

        leafNode = new LeafNode() {
            {
                value = new Attributes();
                putAttribute("test", "value");
            }
        };

        leafNode.coreValue("newTestValue");
        assertEquals("newTestValue", leafNode.coreValue());
    }

    @Test
    public void testAttrNullKey() {
        LeafNode leafNode = new LeafNode();
        assertNull(leafNode.attr(null));
    }

    @Test
    public void testAttrEmptyStringValue() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                putAttribute("test", "");
            }
        };

        assertEquals("", leafNode.attr("test"));
    }

    @Test
    public void testAttrWithValue() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                putAttribute("test", "value");
            }
        };

        assertEquals("value", leafNode.attr("test"));
    }

    @Test
    public void testAttrSet() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
            }
        };

        leafNode.attr("test", "value");
        assertEquals("value", leafNode.attr("test"));

        leafNode.attr("test2", "value2");
        assertEquals("value2", leafNode.attr("test2"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                putAttribute("test", "value");
            }
        };

        assertTrue(leafNode.hasAttr("test"));
        assertFalse(leafNode.hasAttr("test2"));

        leafNode = new LeafNode() {
            {
                value = "testValue";
            }
        };

        assertFalse(leafNode.hasAttr("test"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                putAttribute("test", "value");
            }
        };

        leafNode.removeAttr("test");
        assertFalse(leafNode.hasAttr("test"));
    }

    @Test
    public void testAbsUrlNullKey() {
        LeafNode leafNode = new LeafNode();
        assertNull(leafNode.absUrl(null));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNode() {
            Node parent = new Element("parent");
        };
        leafNode.parent = parent;
        parent.appendElement(leafNode);

        String baseUri = "http://test.com";
        parent.baseUri(baseUri);

        assertEquals(baseUri, leafNode.baseUri());

        leafNode = new LeafNode();
        assertEquals("", leafNode.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetBaseUri() {
        LeafNode leafNode = new LeafNode();
        leafNode.setBaseUri("testUri");
    }

    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new LeafNode();
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new LeafNode();
        leafNode.ensureChildNodes();
    }
}