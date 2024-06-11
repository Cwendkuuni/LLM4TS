import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;

public class AttributeContextTest {
    
    AttributeContext attributeContext;
    
    @Before
    public void setUp() {
        NodeTest nodeTest = new NodeNameTest("attribute");
        attributeContext = new AttributeContext(null, nodeTest);
    }
    
    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());
        
        // Setting current node pointer
        attributeContext.nextNode();
        assertNotNull(attributeContext.getCurrentNodePointer());
    }
    
    @Test
    public void testReset() {
        // Setting current node pointer
        attributeContext.nextNode();
        assertNotNull(attributeContext.getCurrentNodePointer());
        
        // Resetting context
        attributeContext.reset();
        
        // Asserting reset values
        assertNull(attributeContext.getCurrentNodePointer());
        assertFalse(attributeContext.setPosition(2));
    }
    
    @Test
    public void testSetPosition() {
        // Setting current node pointer
        attributeContext.nextNode();
        assertNotNull(attributeContext.getCurrentNodePointer());
        
        // Setting the position to 1
        assertTrue(attributeContext.setPosition(1));
        assertEquals(1, attributeContext.getCurrentPosition());
        
        // Setting the position to 2
        assertFalse(attributeContext.setPosition(2));
    }
    
    @Test
    public void testNextNode() {
        // Set the current node pointer to the first attribute
        assertTrue(attributeContext.nextNode());
        assertNotNull(attributeContext.getCurrentNodePointer());
        
        // Set the current node pointer to the next attribute
        assertTrue(attributeContext.nextNode());
        assertNotNull(attributeContext.getCurrentNodePointer());
        
        // Set the current node pointer to the next attribute (out of bounds)
        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }
}