import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {
    
    private AttributeContext attributeContext;
    private NodeTest nodeTest;
    
    @Before
    public void setUp() {
        attributeContext = new AttributeContext(null, nodeTest);
    }
    
    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());
    }
    
    @Test
    public void testReset() {
        attributeContext.reset();
        assertFalse(attributeContext.setPosition(1));
    }
    
    @Test
    public void testSetPosition() {
        attributeContext.setPosition(1);
        assertEquals(1, attributeContext.getCurrentPosition());
    }
    
    @Test
    public void testNextNode() {
        assertFalse(attributeContext.nextNode());
    }
}