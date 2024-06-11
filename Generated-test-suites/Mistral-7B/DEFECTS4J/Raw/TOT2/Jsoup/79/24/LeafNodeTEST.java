import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.junit.Test;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
            }
        };
        assertTrue(leafNode.hasAttributes());

        LeafNode leafNodeWithoutAttributes = new LeafNode();
        assertFalse(leafNodeWithoutAttributes.hasAttributes());
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNode() {
            {
                value = "Test Value";
            }
        };
        assertEquals("Test Value", leafNode.coreValue());

        LeafNode leafNodeWithAttributes = new LeafNode() {
            {
                value = new Attributes();
            }
        };
        assertEquals("nodeName", leafNodeWithAttributes.coreValue());
    }

    @Test
    public void testCoreValueAssignment() {
        LeafNode leafNode = new LeafNode();
        leafNode.coreValue("Test Value");
        assertEquals("Test Value", leafNode.coreValue());
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeyException() {
        LeafNode leafNode = new LeafNode();
        leafNode.attr(null, "value");
    }

    @Test
    public void testEmptyStringKey() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
            }
        };
        assertEquals("", leafNode.attr(""));
    }

    @Test
    public void testEmptyStringValue() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
            }
        };
        leafNode.attr("key", "");
        assertEquals("", leafNode.attr("key"));
    }

    @Test
    public void testInvalidKey() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
            }
        };
        assertNull(leafNode.attr("invalidKey"));
    }

    @Test(expected = ClassCastException.class)
    public void testInvalidValueType() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
            }
        };
        leafNode.attr("key", new Object());
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNodeWithAttribute = new LeafNode() {
            {
                value = new Attributes();
                value.put("key", "value");
            }
        };
        assertTrue(leafNodeWithAttribute.hasAttr("key"));

        LeafNode leafNodeWithoutAttribute = new LeafNode();
        assertFalse(leafNodeWithoutAttribute.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNodeWithAttribute = new LeafNode() {
            {
                value = new Attributes();
                value.put("key", "value");
            }
        };
        leafNodeWithAttribute.removeAttr("key");
        assertFalse(leafNodeWithAttribute.hasAttr("key"));

        LeafNode leafNodeWithoutAttribute = new LeafNode();
        assertFalse(leafNodeWithoutAttribute.hasAttr("key"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                value.put("abs:url", "http://test.com");
            }
        };
        assertEquals("http://test.com", leafNode.absUrl("abs:url"));

        LeafNode leafNodeWithoutAbsoluteUrl = new LeafNode();
        assertNull(leafNodeWithoutAbsoluteUrl.absUrl("key"));
    }

    @Test
    public void testBaseUri() {
        LeafNode parentNode = new Elements.Element("parent");
        parentNode.coreValue("http://parent.com");
        LeafNode childNode = new LeafNode() {
            {
                parent = parentNode;
            }
        };
        assertEquals("http://parent.com", childNode.baseUri());

        LeafNode leafNodeWithoutParent = new LeafNode();
        assertEquals("", leafNodeWithoutParent.baseUri());
    }
}