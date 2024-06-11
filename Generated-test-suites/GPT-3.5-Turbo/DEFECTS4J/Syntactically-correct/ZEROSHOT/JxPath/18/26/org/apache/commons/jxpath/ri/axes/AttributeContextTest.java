import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {
    
    private AttributeContext attributeContext;
    
    @Before
    public void setUp() {
        attributeContext = new AttributeContext(null, null);
    }
    
    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());
    }
    
    @Test
    public void testReset() {
        attributeContext.setPosition(1);
        attributeContext.reset();
        assertNull(attributeContext.getCurrentNodePointer());
    }
    
    @Test
    public void testSetPosition() {
        attributeContext.setPosition(1);
        assertNotNull(attributeContext.getCurrentNodePointer());
    }
    
    @Test
    public void testNextNode() {
        attributeContext.nextNode();
        assertNotNull(attributeContext.getCurrentNodePointer());
    }
}