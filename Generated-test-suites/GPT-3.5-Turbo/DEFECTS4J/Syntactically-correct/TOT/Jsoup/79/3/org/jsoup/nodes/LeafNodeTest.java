import org.jsoup.nodes.*;
import org.junit.*;
import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new ConcreteLeafNode("value");
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new ConcreteLeafNode("value");
        assertNotNull(leafNode.attributes());
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttrWithoutAttributes() {
        LeafNode leafNode = new ConcreteLeafNode("value");
        assertEquals("value", leafNode.attr("value"));
    }
  
    @Test
    public void testAttrWithAttributes() {
        LeafNode leafNode = new ConcreteLeafNode();
        leafNode.attr("key1", "value1");
        leafNode.attr("key2", "value2");
        leafNode.attr("key3", "value3");
      
        assertEquals("value1", leafNode.attr("key1"));
        assertEquals("value2", leafNode.attr("key2"));
        assertEquals("value3", leafNode.attr("key3"));
    }
  
    @Test
    public void testAttrNotFound() {
        LeafNode leafNode = new ConcreteLeafNode("value");
        assertNull(leafNode.attr("key"));
    }
  
    @Test
    public void testAttrWithNullKey() {
        LeafNode leafNode = new ConcreteLeafNode("value");
        assertNotNull(leafNode.attr(null));
    }
  
    @Test
    public void testHasAttrWithAttributes() {
        LeafNode leafNode = new ConcreteLeafNode();
        leafNode.attr("key1", "value1");
        assertTrue(leafNode.hasAttr("key1"));
    }
  
    @Test
    public void testHasAttrNotFound() {
        LeafNode leafNode = new ConcreteLeafNode("value");
        assertFalse(leafNode.hasAttr("key"));
    }
  
    @Test
    public void testRemoveAttrWithAttributes() {
        LeafNode leafNode = new ConcreteLeafNode();
        leafNode.attr("key1", "value1");
        leafNode.attr("key2", "value2");
        leafNode.attr("key3", "value3");
        
        leafNode.removeAttr("key2");
        
        assertNull(leafNode.attr("key2"));
        assertEquals("value1", leafNode.attr("key1"));
        assertEquals("value3", leafNode.attr("key3"));
    }
  
    @Test
    public void testRemoveAttrNotFound() {
        LeafNode leafNode = new ConcreteLeafNode("value");
        assertNull(leafNode.removeAttr("key"));
    }
  
    @Test
    public void testAbsUrlWithAttributes() {
        LeafNode leafNode = new ConcreteLeafNode();
        leafNode.attr("href", "/example");
        
        assertEquals("/example", leafNode.absUrl("href"));
    }
  
    @Test
    public void testAbsUrlNotFound() {
        LeafNode leafNode = new ConcreteLeafNode("value");
        assertNull(leafNode.absUrl("href"));
    }
  
    @Test
    public void testBaseUriWithParent() {
        LeafNode leafNode = new ConcreteLeafNode("value");
        Document document = Jsoup.parse("<html><head><title>Page Title</title></head><body></body></html>");
        document.body().appendChild(leafNode);
        
        assertEquals("", leafNode.baseUri());
    }
  
    @Test
    public void testBaseUriWithoutParent() {
        LeafNode leafNode = new ConcreteLeafNode("value");
        
        assertEquals("", leafNode.baseUri());
    }
  
    @Test
    public void testDoSetBaseUri() {
        LeafNode leafNode = new ConcreteLeafNode("value");
        leafNode.doSetBaseUri("new_base_uri");
        
        // No operation is done, so baseUri should remain the same as before
        assertEquals("", leafNode.baseUri());
    }
  
    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new ConcreteLeafNode("value");
        assertEquals(0, leafNode.childNodeSize());
    }
  
    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new ConcreteLeafNode("value");
        leafNode.ensureChildNodes();
    }
  
    // Additional cases for LeafNode specific methods
  
    @Test
    public void testCoreValue() {
        LeafNode leafNode = new ConcreteLeafNode("value");
      
        assertEquals("value", leafNode.coreValue());
    }
  
    @Test
    public void testCoreValueChange() {
        LeafNode leafNode = new ConcreteLeafNode("value");
        leafNode.coreValue("new_value");
      
        assertEquals("new_value", leafNode.attr("value"));
    }
  
    private static class ConcreteLeafNode extends LeafNode {
        public ConcreteLeafNode(String value) {
            this.value = value;
        }

        public ConcreteLeafNode() {
            this.value = null;
        }
      
        @Override
        public String nodeName() {
            return "value";
        }
    }
}