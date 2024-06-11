
import org.junit.Test;

public class LeafNodeTest {
    @Test
    public void testHasAttributes() {
        // Test with null value
        LeafNode leafNode = new LeafNodeImpl();
        boolean hasAttributes = leafNode.hasAttributes();
        assertFalse(hasAttributes);

        // Test with Attributes value
        Attributes attributes = new Attributes();
        leafNode.value = attributes;
        hasAttributes = leafNode.hasAttributes();
        assertTrue(hasAttributes);
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNodeImpl();

        // Test without Attributes value
        Attributes attributes = leafNode.attributes();
        assertNotNull(attributes);
        assertTrue(attributes.isEmpty());

        // Test with Attributes value
        leafNode.value = new Attributes();
        attributes = leafNode.attributes();
        assertNotNull(attributes);
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNodeImpl();

        // Test without attributes
        String coreValue = leafNode.coreValue();
        assertNull(coreValue);

        // Test with attributes
        leafNode.coreValue("value");
        coreValue = leafNode.coreValue();
        assertEquals("value", coreValue);
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNodeImpl();

        // Test without attributes
        String attrValue = leafNode.attr("key");
        assertNull(attrValue);

        // Test with attributes
        leafNode.value = new Attributes();
        attrValue = leafNode.attr("key");
        assertNull(attrValue);

        // Test with matching attribute
        leafNode.attr("key", "value");
        attrValue = leafNode.attr("key");
        assertEquals("value", attrValue);
    }

    @Test
    public void testAttrWithValue() {
        LeafNode leafNode = new LeafNodeImpl();

        // Test without attributes
        Node result = leafNode.attr("key", "value");
        assertNotNull(leafNode.value);
        assertTrue(leafNode.value instanceof String);
        assertEquals("value", leafNode.value);

        // Test with attributes
        leafNode.value = new Attributes();
        result = leafNode.attr("key", "value");
        assertNotNull(leafNode.value);
        assertTrue(leafNode.value instanceof Attributes);
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNodeImpl();

        // Test without attributes
        boolean hasAttr = leafNode.hasAttr("key");
        assertFalse(hasAttr);

        // Test with attributes
        leafNode.value = new Attributes();
        hasAttr = leafNode.hasAttr("key");
        assertFalse(hasAttr);

        // Test with matching attribute
        leafNode.attr("key", "value");
        hasAttr = leafNode.hasAttr("key");
        assertTrue(hasAttr);
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNodeImpl();

        // Test without attributes
        Node result = leafNode.removeAttr("key");
        assertNull(leafNode.value);

        // Test with attributes
        leafNode.value = new Attributes();
        result = leafNode.removeAttr("key");
        assertNotNull(leafNode.value);
        assertTrue(leafNode.value instanceof Attributes);
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNodeImpl();

        // Test without attributes
        String absUrl = leafNode.absUrl("key");
        assertNull(absUrl);

        // Test with attributes
        leafNode.value = new Attributes();
        absUrl = leafNode.absUrl("key");
        assertNull(absUrl);
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNodeImpl();

        // Test without parent
        String baseUri = leafNode.baseUri();
        assertEquals("", baseUri);

        // Test with parent
        Node parent = new ParentNode();
        leafNode.parentNode = parent;
        parent.baseUri = "http://www.example.com";
        baseUri = leafNode.baseUri();
        assertEquals("http://www.example.com", baseUri);
    }

    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new LeafNodeImpl();
        int childNodeSize = leafNode.childNodeSize();
        assertEquals(0, childNodeSize);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.ensureChildNodes();
    }
}

class LeafNodeImpl extends LeafNode {
    @Override
    protected String nodeName() {
        return "";
    }
}

class ParentNode extends Node {
    @Override
    protected String nodeName() {
        return "";
    }

    @Override
    public int childNodeSize() {
        return 0;
    }
}
