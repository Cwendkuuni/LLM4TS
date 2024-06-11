
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class LeafNodeTest {

    private LeafNode leafNode;
    
    @Before
    public void setUp() {
        leafNode = new LeafNodeImpl();
    }

    @Test
    public void testHasAttributes_WhenNoAttributes_ReturnsFalse() {
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testHasAttributes_WhenAttributesExist_ReturnsTrue() {
        leafNode.attr("attr1", "value1");
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes_WhenNoAttributes_ReturnsEmptyAttributes() {
        Attributes attributes = leafNode.attributes();
        assertNotNull(attributes);
        assertTrue(attributes.isEmpty());
    }

    @Test
    public void testAttributes_WhenAttributesExist_ReturnsAttributes() {
        leafNode.attr("attr1", "value1");
        Attributes attributes = leafNode.attributes();
        assertNotNull(attributes);
        assertEquals(1, attributes.size());
        assertEquals("value1", attributes.get("attr1"));
    }

    @Test
    public void testAttr_WhenNoAttributesAndKeyIsNodeName_ReturnsCoreValue() {
        leafNode.coreValue("coreValue");
        String attributeValue = leafNode.attr("nodeName");
        assertEquals("coreValue", attributeValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttr_WhenNoAttributesAndKeyIsNotNodeName_ThrowsException() {
        leafNode.attr("attr1");
    }

    @Test
    public void testAttr_WhenAttributesExistAndKeyIsNodeName_ReturnsCoreValue() {
        leafNode.attr("nodeName", "coreValue");
        leafNode.attr("attr1", "value1");
        String attributeValue = leafNode.attr("nodeName");
        assertEquals("coreValue", attributeValue);
    }

    @Test
    public void testAttr_WhenAttributesExistAndKeyIsNotNodeName_ReturnsAttributeValue() {
        leafNode.attr("nodeName", "coreValue");
        leafNode.attr("attr1", "value1");
        String attributeValue = leafNode.attr("attr1");
        assertEquals("value1", attributeValue);
    }

    @Test
    public void testAttr_WhenAttributesExistAndKeyDoesNotExist_ReturnsEmptyString() {
        leafNode.attr("nodeName", "coreValue");
        leafNode.attr("attr1", "value1");
        String attributeValue = leafNode.attr("attr2");
        assertEquals("", attributeValue);
    }

    @Test
    public void testAttr_WhenAttributeValueIsNull_ReturnsEmptyString() {
        leafNode.attr("nodeName", null);
        String attributeValue = leafNode.attr("nodeName");
        assertEquals("", attributeValue);
    }

    @Test(expected = NullPointerException.class)
    public void testAttr_WhenKeyIsNull_ThrowsException() {
        leafNode.attr(null);
    }

    @Test
    public void testAttr_WhenNoAttributesAndKeyIsNodeName_ChangesCoreValue() {
        leafNode.attr("attr1", "value1");
        leafNode.attr("nodeName", "coreValue");
        String attributeValue = leafNode.attr("nodeName");
        assertEquals("coreValue", attributeValue);
        assertEquals("coreValue", leafNode.coreValue());
    }

    @Test
    public void testAttr_WhenAttributesExistAndKeyIsNodeName_ChangesCoreValue() {
        leafNode.attr("attr1", "value1");
        leafNode.attr("nodeName", "coreValue");
        String attributeValue = leafNode.attr("nodeName");
        assertEquals("coreValue", attributeValue);
        assertEquals("coreValue", leafNode.coreValue());
    }

    @Test
    public void testAttr_WhenAttributesExistAndKeyIsNotNodeName_AddsAttribute() {
        leafNode.attr("attr1", "value1");
        leafNode.attr("attr2", "value2");
        assertEquals("value1", leafNode.attr("attr1"));
        assertEquals("value2", leafNode.attr("attr2"));
    }

    @Test
    public void testHasAttr_WhenAttributeExists_ReturnsTrue() {
        leafNode.attr("attr1", "value1");
        assertTrue(leafNode.hasAttr("attr1"));
    }

    @Test
    public void testHasAttr_WhenAttributeDoesNotExist_ReturnsFalse() {
        assertFalse(leafNode.hasAttr("attr1"));
    }

    @Test
    public void testRemoveAttr_WhenAttributeExists_RemovesAttribute() {
        leafNode.attr("attr1", "value1");
        assertEquals("value1", leafNode.removeAttr("attr1").attr("attr1"));
    }

    @Test
    public void testRemoveAttr_WhenAttributeDoesNotExist_ReturnsOriginalNode() {
        assertEquals(leafNode, leafNode.removeAttr("attr1"));
    }

    @Test
    public void testAbsUrl_WhenAttributeExists_ReturnsAbsUrl() {
        leafNode.attr("attr1", "/relative/url");
        assertEquals("http://example.com/relative/url", leafNode.absUrl("attr1"));
    }

    @Test
    public void testAbsUrl_WhenAttributeDoesNotExist_ReturnsEmptyString() {
        assertEquals("", leafNode.absUrl("attr1"));
    }

    @Test
    public void testBaseUri_WhenHasParent_ReturnsParentBaseUri() {
        Node parent = new LeafNodeImpl();
        parent.setBaseUri("http://parent.com");
        leafNode.setParent(parent);
        assertEquals("http://parent.com", leafNode.baseUri());
    }

    @Test
    public void testBaseUri_WhenNoParent_ReturnsEmptyString() {
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri_ShouldNotDoAnything() {
        leafNode.doSetBaseUri("http://example.com");
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testChildNodeSize_ReturnsZero() {
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes_ThrowsException() {
        leafNode.ensureChildNodes();
    }
    
    private class LeafNodeImpl extends LeafNode {
        
        @Override
        protected void doSetBaseUri(String baseUri) {
            // noop
        }
        
        @Override
        protected List<Node> ensureChildNodes() {
            throw new UnsupportedOperationException("Leaf Nodes do not have child nodes.");
        }
    }

}
