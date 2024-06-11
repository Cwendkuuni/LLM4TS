import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        // Test when value is an Attributes object
        LeafNode node = new LeafNode() {
            @Override
            protected void ensureAttributes() {
                value = new Attributes();
            }
        };
        assertTrue(node.hasAttributes());

        // Test when value is not an Attributes object
        node = new LeafNode() {
            @Override
            protected void ensureAttributes() {
                value = "test";
            }
        };
        assertFalse(node.hasAttributes());
    }

    @Test
    public void testAttributes() {
        // Test getting attributes when they are already set
        LeafNode node = new LeafNode() {
            @Override
            protected void ensureAttributes() {
                value = new Attributes();
            }
        };
        assertNotNull(node.attributes());
        
        // Test getting attributes when they are not set initially
        node = new LeafNode();
        assertNotNull(node.attributes());
    }

    // Add more test cases for the remaining methods
    
}