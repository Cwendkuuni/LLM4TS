import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {

    @Test
    public void testGetCurrentNodePointer() {
        EvalContext parentContext = new EvalContext();
        NodeTest nodeTest = new NodeTest();
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        EvalContext parentContext = new EvalContext();
        NodeTest nodeTest = new NodeTest();
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        
        attributeContext.reset();
        assertFalse(attributeContext.setPosition(1));
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext();
        NodeTest nodeTest = new NodeTest();
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        
        assertTrue(attributeContext.setPosition(0));
        assertFalse(attributeContext.setPosition(1));
    }

    @Test
    public void testNextNode() {
        EvalContext parentContext = new EvalContext();
        NodeTest nodeTest = new NodeTest();
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        
        assertFalse(attributeContext.nextNode());
    }
}