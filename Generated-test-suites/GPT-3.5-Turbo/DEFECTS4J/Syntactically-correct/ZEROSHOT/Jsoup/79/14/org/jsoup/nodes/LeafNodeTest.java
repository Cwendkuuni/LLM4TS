import org.junit.Test;
import org.jsoup.helper.Validate;
import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new DummyLeafNode();
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new DummyLeafNode();
        assertNull(leafNode.attributes());

        Attributes attributes = new Attributes();
        leafNode.value = attributes;

        assertEquals(attributes, leafNode.attributes());
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode leafNode = new DummyLeafNode();
        leafNode.ensureAttributes();

        assertTrue(leafNode.hasAttributes());
        assertTrue(leafNode.value instanceof Attributes);
        assertEquals(leafNode.nodeName(), ((Attributes) leafNode.value).get(leafNode.nodeName()));
    }

    @Test
    public void testCoreValue() {
        DummyLeafNode leafNode = new DummyLeafNode();
        assertNull(leafNode.coreValue());

        String value = "testValue";
        leafNode.attr(leafNode.nodeName(), value);
        assertEquals(value, leafNode.coreValue());
    }

    @Test
    public void testCoreValueWithNewValue() {
        DummyLeafNode leafNode = new DummyLeafNode();
        assertNull(leafNode.coreValue());

        String value1 = "testValue1";
        leafNode.coreValue(value1);
        assertEquals(value1, leafNode.coreValue());

        String value2 = "testValue2";
        leafNode.coreValue(value2);
        assertEquals(value2, leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new DummyLeafNode();
        assertNull(leafNode.attr("testKey"));

        String key = "testKey";
        String value = "testValue";
        leafNode.attr(key, value);

        assertEquals(value, leafNode.attr(key));
    }

    @Test
    public void testHasAttrWithoutAttributes() {
        LeafNode leafNode = new DummyLeafNode();
        assertFalse(leafNode.hasAttr("testKey"));
    }

    @Test
    public void testHasAttrWithAttributesWithoutKey() {
        LeafNode leafNode = new DummyLeafNode();
        leafNode.ensureAttributes();

        assertTrue(leafNode.hasAttr(leafNode.nodeName()));
        assertFalse(leafNode.hasAttr("testKey"));
    }

    @Test
    public void testHasAttrWithAttributesWithKey() {
        LeafNode leafNode = new DummyLeafNode();
        leafNode.ensureAttributes();
        
        leafNode.attr("testKey", "testValue");

        assertTrue(leafNode.hasAttr("testKey"));
    }

    @Test
    public void testRemoveAttrWithoutAttributes() {
        LeafNode leafNode = new DummyLeafNode();
        assertNull(leafNode.removeAttr("testKey"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new DummyLeafNode();
        leafNode.ensureAttributes();
        
        leafNode.attr("testKey", "testValue");

        assertEquals("testValue", leafNode.removeAttr("testKey").attr("testKey"));
    }

    @Test
    public void testAbsUrlWithoutAttributes() {
        LeafNode leafNode = new DummyLeafNode();
        assertNull(leafNode.absUrl("testKey"));
    }

    @Test
    public void testAbsUrlWithAttributesWithoutKey() {
        LeafNode leafNode = new DummyLeafNode();
        leafNode.ensureAttributes();
        
        assertEquals("", leafNode.absUrl(leafNode.nodeName()));
        assertNull(leafNode.absUrl("testKey"));
    }

    @Test
    public void testAbsUrlWithAttributesWithKey() {
        LeafNode leafNode = new DummyLeafNode();
        leafNode.ensureAttributes();
        
        leafNode.attr("testKey", "testValue");

        assertNull(leafNode.absUrl("testKey"));
    }

    @Test
    public void testBaseUriWithoutParent() {
        LeafNode leafNode = new DummyLeafNode();
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testBaseUriWithParent() {
        DummyLeafNode leafNode = new DummyLeafNode();
        leafNode.setParent(new Element("parent"));

        assertEquals("parent", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        LeafNode leafNode = new DummyLeafNode();
        leafNode.doSetBaseUri("testBaseUri");
        // No assertion as it is a noop
    }
    
    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new DummyLeafNode();
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new DummyLeafNode();
        leafNode.ensureChildNodes();
    }
  
    private class DummyLeafNode extends LeafNode {
        @Override
        protected String nodeName() {
            return "testNodeName";
        }
    }
}