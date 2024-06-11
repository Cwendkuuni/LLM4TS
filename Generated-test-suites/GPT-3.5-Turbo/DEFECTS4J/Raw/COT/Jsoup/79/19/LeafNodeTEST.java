
import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        // Create a LeafNode instance
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "test";
            }
        };
        
        // Test the hasAttributes() method
        assertFalse(leafNode.hasAttributes());
        
        // Add attributes to the node
        leafNode.coreValue("value");
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        // Create a LeafNode instance
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "test";
            }
        };
        
        // Test the attributes() method
        assertNull(leafNode.attributes());

        // Add attributes to the node
        leafNode.coreValue("value");
        assertNotNull(leafNode.attributes());
    }

    @Test
    public void testAttr() {
        // Create a LeafNode instance
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "test";
            }
        };
        
        // Test the attr() method
        assertNull(leafNode.attr("key"));
        
        // Add an attribute to the node
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
    }

    @Test
    public void testRemoveAttr() {
        // Create a LeafNode instance
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "test";
            }
        };
        
        // Add attributes to the node
        leafNode.coreValue("value");
        leafNode.attr("key1", "value1");
        leafNode.attr("key2", "value2");
        
        // Test the removeAttr() method
        assertTrue(leafNode.hasAttr("key1"));
        leafNode.removeAttr("key1");
        assertFalse(leafNode.hasAttr("key1"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        // Create a LeafNode instance
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "test";
            }
        };
        
        // Test the ensureChildNodes() method
        leafNode.ensureChildNodes();
    }

    // Add other test methods for the remaining public methods in the LeafNode class

}
