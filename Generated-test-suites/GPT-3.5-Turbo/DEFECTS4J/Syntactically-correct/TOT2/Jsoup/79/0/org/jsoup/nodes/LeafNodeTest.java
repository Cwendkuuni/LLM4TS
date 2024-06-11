import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {
    
    @Test
    public void testHasAttributes() {
        // Test when value is an Attributes object
        LeafNode leafNode = new LeafNode() {
            @Override
            protected void ensureAttributes() {
                value = new Attributes();
            }
        };
        
        assertTrue(leafNode.hasAttributes());
        
        // Test when value is not an Attributes object
        leafNode = new LeafNode();
        assertFalse(leafNode.hasAttributes());
    }
    
    @Test
    public void testAttributes() {
        // Test attributes method when value is an Attributes object
        LeafNode leafNode = new LeafNode() {
            @Override
            protected void ensureAttributes() {
                value = new Attributes();
            }
        };
        
        assertEquals(Attributes.class, leafNode.attributes().getClass());
        
        // Test attributes method when value is not an Attributes object
        leafNode = new LeafNode() {
            @Override
            protected void ensureAttributes() {
                value = "value";
            }
        };
        
        assertNull(leafNode.attributes());
    }
    
    // Add more individual test methods for each public method in LeafNode
    
}