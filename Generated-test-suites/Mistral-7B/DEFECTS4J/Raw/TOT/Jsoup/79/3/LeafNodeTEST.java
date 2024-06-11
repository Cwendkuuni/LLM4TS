
import org.junit.BeforeClass;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import static org.junit.Assert.*;

public class LeafNodeTest {
    private static LeafNode leafNodeWithAttributes;
    private static LeafNode leafNodeWithoutAttributes;

    @BeforeClass
    public static void setup() {
        leafNodeWithAttributes = new MyLeafNode("My Node").attr("attr1", "value1").attr("attr2", "value2");
        leafNodeWithoutAttributes = new MyLeafNode("My Node");
    }

    @Test
    public void testHasAttributes() {
        LeafNodeTest.assertFalse(leafNodeWithoutAttributes.hasAttributes());
        LeafNodeTest.assertTrue(leafNodeWithAttributes.hasAttributes());
    }

    @Test
    public void testCoreValue() {
        LeafNodeTest.assertEquals("My Node", leafNodeWithoutAttributes.coreValue());
        LeafNodeTest.assertEquals("My Node", leafNodeWithAttributes.coreValue());
        LeafNodeTest.assertEquals("value1", leafNodeWithAttributes.attr("attr1"));
        LeafNodeTest.assertEquals("value2", leafNodeWithAttributes.attr("attr2"));
        LeafNodeTest.assertNull(leafNodeWithoutAttributes.attr("attr1"));
        LeafNodeTest.assertNull(leafNodeWithoutAttributes.attr("attr2"));
    }

    @Test
    public void testAttributes() {
        LeafNodeTest.assertAttributesAreEqual(new Attributes(), leafNodeWithoutAttributes.attributes());
        LeafNodeTest.assertAttributesAreEqual(new Attributes().put("attr1", "value1").put("attr2", "value2"), leafNodeWithAttributes.attributes());

        Element parentElement = new Element("parent");
        parentElement.appendChild(leafNodeWithAttributes);
        Attributes parentAttributes = parentElement.attributes();
        LeafNodeTest.assertEquals("My Node", parentAttributes.get("nodeName"));
        LeafNodeTest.assertTrue(parentAttributes.hasKey("attr1"));
        LeafNodeTest.assertTrue(parentAttributes.hasKey("attr2"));

        parentElement.removeChild(0);
        LeafNodeTest.assertNull(parentElement.children());
        LeafNodeTest.assertNull(parentElement.attr("attr1"));
        LeafNodeTest.assertNull(parentElement.attr("attr2"));
    }

    private static void assertAttributesAreEqual(Attributes expected, Attributes actual) {
        for (String key : expected.keySet()) {
            assertEquals(expected.get(key), actual.get(key));
        }
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.isEmpty() == actual.isEmpty());
    }

    @Test
    public void testAttr() {
        LeafNodeTest.assertNull(leafNodeWithoutAttributes.attr("attr1"));
        LeafNodeTest.assertNull(leafNodeWithoutAttributes.attr("attr2"));

        leafNodeWithAttributes.attr("attr1", "newValue1");
        leafNodeWithAttributes.attr("attr2", "newValue2");
        assertEquals("newValue1", leafNodeWithAttributes.attr("attr1"));
        assertEquals("newValue2", leafNodeWithAttributes.attr("attr2"));

        Element parentElement = new Element("parent");
        parentElement.appendChild(leafNodeWithAttributes);
        assertEquals("My Node", parentElement.attr("nodeName"));
        assertEquals("newValue1", parentElement.attr("attr1"));
        assertEquals("newValue2", parentElement.attr("attr2"));

        leafNodeWithAttributes.attr("attr1", null);
        leafNodeWithAttributes.attr("attr2", null);
        assertEquals(null, leafNodeWithAttributes.attr("attr1"));
        assertEquals(null, leafNodeWithAttributes.attr("attr2"));
        assertEquals(0, leafNodeWithAttributes.childNodeSize());

        parentElement.removeChild(0);
        assertEquals(null, parentElement.attr("attr1"));
        assertEquals(null, parentElement.attr("attr2"));
    }

    @Test
    public void testHasAttr() {
        assertFalse(leafNodeWithoutAttributes.hasAttr("attr1"));
        assertFalse(leafNodeWithoutAttributes.hasAttr("attr2"));

        assertTrue(leafNodeWithAttributes.hasAttr("attr1"));
        assertTrue(leafNodeWithAttributes.hasAttr("attr2"));

        Element parentElement = new Element("parent");
        parentElement.appendChild(leafNodeWithAttributes);
        assertTrue(parentElement.hasAttr("nodeName"));
        assertTrue(parentElement.hasAttr("attr1"));
        assertTrue(parentElement.hasAttr("attr2"));

        parentElement.removeChild(0);
        assertFalse(parentElement.hasAttr("attr1"));
        assertFalse(parentElement.hasAttr("attr2"));
    }

    @Test
    public void testRemoveAttr() {
        Element parentElement = new Element("parent");
        parentElement.appendChild(leafNodeWithAttributes);

        leafNodeWithAttributes.removeAttr("attr1");
        assertEquals(0, leafNodeWithAttributes.childNodeSize());
        assertFalse(leafNodeWithAttributes.hasAttr("attr1"));
        assertFalse(parentElement.hasAttr("attr1"));

        leafNodeWithAttributes.removeAttr("attr2");
        assertEquals(0, leafNodeWithAttributes.childNodeSize());
        assertFalse(leafNodeWithAttributes.hasAttr("attr2"));
        assertFalse(parentElement.hasAttr("attr2"));

        parentElement.removeChild(0);
        assertFalse(parentElement.hasAttr("attr1"));
        assertFalse(parentElement.hasAttr("attr2"));
    }

    @Test
    public void testAbsUrl() {
        Element parentElement = new Element("parent");
        parentElement.appendChild(leafNodeWithAttributes);
        parentElement.attr("baseUri", "http://example.com/");

        String expected = "http://example.com/My%20Node";
        assertEquals(expected, leafNodeWithAttributes.absUrl(""));
        assertEquals(expected, leafNodeWithAttributes.absUrl("nodeName"));

        leafNodeWithAttributes.removeAttr("nodeName");
        assertEquals(EmptyString.INSTANCE, leafNodeWithAttributes.absUrl(""));
        assertEquals(EmptyString.INSTANCE, leafNodeWithAttributes.absUrl("nodeName"));

        parentElement.removeChild(0);
        assertEquals(EmptyString.INSTANCE, parentElement.absUrl(""));
        assertEquals(EmptyString.INSTANCE, parentElement.absUrl("nodeName"));
    }

    @Test
    public void testBaseUri() {
        Element parentElement = new Element("parent");
        parentElement.appendChild(leafNodeWithAttributes);
        LeafNode childWithBaseUri = new Element("child").attr("nodeName", "Child").baseUri("http://example.com/");
        parentElement.appendChild(childWithBaseUri);

        assertEquals("http://example.com/", leafNodeWithAttributes.baseUri());
        assertEquals("http://example.com/My%20Node", childWithBaseUri.absUrl(""));

        parentElement.removeChild(1);
        parentElement.removeChild(0);
        assertEquals(EmptyString.INSTANCE, parentElement.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetBaseUri() {
        leafNodeWithAttributes.setBaseUri("http://example.com");
    }

    @Test
    public void testChildNodeSize() {
        assertEquals(0, leafNodeWithoutAttributes.childNodeSize());
        assertEquals(0, leafNodeWithAttributes.childNodeSize());

        Element parentElement = new Element("parent");
        parentElement.appendChild(leafNodeWithAttributes);
        assertEquals(0, parentElement.childNodeSize());

        parentElement.appendChild(new Element("child1"));
        parentElement.appendChild(new Element("child2"));
        assertEquals(2, parentElement.childNodeSize());

        parentElement.removeChild(0);
        assertEquals(1, parentElement.childNodeSize());

        parentElement.removeChild(0);
        assertEquals(0, parentElement.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        leafNodeWithAttributes.ensureChildNodes();
    }
}

class MyLeafNode extends LeafNode {
    MyLeafNode(String value) {
        super();
        this.value = value;
    }
}
