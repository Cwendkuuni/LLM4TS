import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNodeMock();
        
        assertFalse(leafNode.hasAttributes());
        
        leafNode.value = "value";
        assertFalse(leafNode.hasAttributes());
        
        leafNode.value = new Attributes();
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNodeMock();
        
        assertNull(leafNode.attributes());
        
        leafNode.value = new Attributes();
        assertNotNull(leafNode.attributes());
        assertTrue(leafNode.attributes() instanceof Attributes);
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNodeMock();
        
        assertNull(leafNode.coreValue());
        
        leafNode.value = "value";
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testCoreValueWithSetValue() {
        LeafNode leafNode = new LeafNodeMock();
        
        assertNull(leafNode.coreValue());

        leafNode.coreValue("new value");
        assertEquals("new value", leafNode.coreValue());
        
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNodeMock();
        
        assertNull(leafNode.attr("key"));
        
        leafNode.value = "value";
        assertNull(leafNode.attr("key"));

        leafNode.value = new Attributes();
        assertEquals("value", leafNode.attr("key"));
        
        leafNode.attr("key", "new value");
        assertEquals("new value", leafNode.attr("key"));
    }

    @Test
    public void testAttrWithSetValue() {
        LeafNode leafNode = new LeafNodeMock();
        
        assertNull(leafNode.attr("key"));

        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
        
        leafNode.value = "new value";
        assertEquals("new value", leafNode.attr("key"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNodeMock();
        
        assertFalse(leafNode.hasAttr("key"));
        
        leafNode.value = "value";
        assertTrue(leafNode.hasAttr("key"));

        leafNode.value = new Attributes();
        assertFalse(leafNode.hasAttr("key"));
        
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNodeMock();
        
        assertNull(leafNode.removeAttr("key"));

        leafNode.value = "value";
        assertNull(leafNode.removeAttr("key"));

        leafNode.value = new Attributes();
        assertNull(leafNode.removeAttr("key"));
        
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.removeAttr("key"));
    }
    
    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNodeMock();
        
        assertNull(leafNode.absUrl("key"));

        leafNode.value = "value";
        assertNull(leafNode.absUrl("key"));

        leafNode.value = new Attributes();
        assertNull(leafNode.absUrl("key"));
        
        leafNode.attr("key", "value");
        assertNull(leafNode.absUrl("key"));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNodeMock();
        
        assertEquals("", leafNode.baseUri());

        leafNode.setParent(new ParentMock());
        assertEquals("parent baseUri", leafNode.baseUri());

        leafNode.setParent(null);
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        LeafNode leafNode = new LeafNodeMock();
        
        // No-op, nothing to assert
        leafNode.doSetBaseUri("baseUri");
    }

    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new LeafNodeMock();
        
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new LeafNodeMock();
        
        leafNode.ensureChildNodes();
    }

    private static class LeafNodeMock extends LeafNode {
        private LeafNodeMock() {
            value = null;
        }
    }

    private static class ParentMock extends Node {
        @Override
        protected void doSetBaseUri(String baseUri) {
            // noop
        }
    }
}