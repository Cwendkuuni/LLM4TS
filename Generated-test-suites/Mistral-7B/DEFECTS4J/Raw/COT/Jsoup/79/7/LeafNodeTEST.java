import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.EmptyString;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                value.put("testKey", "testValue");
            }
        };

        boolean actual = leafNode.hasAttributes();
        assertTrue(actual);

        leafNode = new LeafNode() {
            {
                value = "testValue";
            }
        };

        actual = leafNode.hasAttributes();
        assertFalse(actual);
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                value.put("testKey1", "testValue1");
                value.put("testKey2", "testValue2");
            }
        };

        Attributes actual = leafNode.attributes();
        Attributes expected = new Attributes() {{
            put("testKey1", "testValue1");
            put("testKey2", "testValue2");
        }};

        assertEquals(expected, actual);

        leafNode = new LeafNode();
        actual = leafNode.attributes();
        assertNull(actual);
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode leafNode = new LeafNode() {
            {
                value = "testValue";
            }
        };

        leafNode.ensureAttributes();
        Attributes actual = (Attributes) leafNode.value;
        assertTrue(actual.isEmpty());

        leafNode.attr("testKey", "testValue");
        actual = leafNode.attributes();
        Attributes expected = new Attributes() {{
            put("testKey", "testValue");
        }};

        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testAttrNullKey() {
        LeafNode leafNode = new LeafNode();
        leafNode.attr(null, "testValue");
    }

    @Test(expected = NullPointerException.class)
    public void testAttrNullValue() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                value.put("testKey", "testValue");
            }
        };
        leafNode.attr("testKey", null);
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNode() {
            {
                value = "testValue";
            }
        };

        String actual = leafNode.coreValue();
        String expected = "testValue";

        assertEquals(expected, actual);

        leafNode.coreValue("newTestValue");;
        actual = leafNode.coreValue();
        expected = "newTestValue";

        assertEquals(expected, actual);
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                value.put("testKey", "testValue");
            }
        };

        leafNode.attr("testKey", "newTestValue");
        String actual = leafNode.attr("testKey");
        String expected = "newTestValue";

        assertEquals(expected, actual);

        leafNode = new LeafNode();
        actual = leafNode.attr("testKey");
        expected = EmptyString.EMPTY;

        assertEquals(expected, actual);
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                value.put("testKey", "testValue");
            }
        };

        boolean actual = leafNode.hasAttr("testKey");
        assertTrue(actual);

        leafNode = new LeafNode();
        actual = leafNode.hasAttr("testKey");
        assertFalse(actual);
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                value.put("testKey1", "testValue1");
                value.put("testKey2", "testValue2");
            }
        };

        leafNode.removeAttr("testKey1");
        Attributes actual = leafNode.attributes();
        Attributes expected = new Attributes() {{
            put("testKey2", "testValue2");
        }};

        assertEquals(expected, actual);

        leafNode = new LeafNode() {
            {
                value = new Attributes();
                value.put("testKey", "testValue");
            }
        };

        leafNode.removeAttr("testKey");
        actual = leafNode.attributes();
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                value.put("abs:src", "http://test.com/test.jpg");
            }
        };

        String actual = leafNode.absUrl("src");
        String expected = "http://test.com/test.jpg";

        assertEquals(expected, actual);

        leafNode = new LeafNode();
        actual = leafNode.absUrl("testKey");
        assertNull(actual);
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                value.put("testKey", "http://test.com/test1.html");
                Node parent = new Element("div");
                parent.value = "testDiv";
                parent.appendText("testContent");
                parent.appendChild(this);
                this.setParent(parent);
            }
        };

        String actual = leafNode.baseUri();
        String expected = "http://test.com/test1.html";

        assertEquals(expected, actual);

        leafNode = new LeafNode();
        actual = leafNode.baseUri();
        assertEquals("", actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildNodeSize() {
        LeafNode leafNode = new LeafNode();
        int actual = leafNode.childNodeSize();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new LeafNode();
        List<Node> expected = null;
        List<Node> actual = leafNode.ensureChildNodes();
        assertEquals(expected, actual);
    }
}