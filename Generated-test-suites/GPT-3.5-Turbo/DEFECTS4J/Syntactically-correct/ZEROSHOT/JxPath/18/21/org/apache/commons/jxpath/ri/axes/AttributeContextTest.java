import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {

    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        attributeContext = new AttributeContext(null, null);
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());
        
        NodePointer nodePointer = new NodePointer();
        attributeContext.currentNodePointer = nodePointer;
        
        assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
    }
    
    @Test
    public void testReset() {
        attributeContext.setStarted = true;
        attributeContext.iterator = new NodeIterator();
        attributeContext.reset();
        
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
        assertNull(attributeContext.getCurrentNodePointer());
    }
    
    @Test
    public void testSetPosition() {
        assertFalse(attributeContext.setPosition(1));
        
        attributeContext.setCurrentPosition(1);
        assertTrue(attributeContext.setPosition(1));
        
        attributeContext.setCurrentPosition(2);
        assertFalse(attributeContext.setPosition(1));
    }
    
    @Test
    public void testNextNode() {
        attributeContext.setStarted = false;
        attributeContext.nodeTest = new NodeNameTest();
        attributeContext.parentContext = new EvalContext();
        attributeContext.parentContext.setCurrentNodePointer(new NodePointer());
        
        assertFalse(attributeContext.nextNode());
        
        attributeContext.setStarted = true;
        assertFalse(attributeContext.nextNode());
        
        attributeContext.iterator = new NodeIterator();
        attributeContext.iterator.setPosition(1);
        assertTrue(attributeContext.nextNode());
        assertNotNull(attributeContext.currentNodePointer);
        
        attributeContext.iterator.setPosition(2);
        assertFalse(attributeContext.nextNode());
    }
}