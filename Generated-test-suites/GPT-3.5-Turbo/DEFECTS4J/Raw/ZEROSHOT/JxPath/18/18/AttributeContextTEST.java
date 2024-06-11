import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {

    private AttributeContext attributeContext;

    @Test
    public void testGetCurrentNodePointer() {
        NodePointer nodePointer = new NodePointer();
        attributeContext = new AttributeContext(new EvalContext(), new NodeTest());
        attributeContext.currentNodePointer = nodePointer;
        assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext = new AttributeContext(new EvalContext(), new NodeTest());
        attributeContext.reset();
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
    }

    @Test
    public void testSetPosition() {
        attributeContext = new AttributeContext(new EvalContext(), new NodeTest());
        assertFalse(attributeContext.setPosition(1));
    }

    @Test
    public void testNextNode() {
        attributeContext = new AttributeContext(new EvalContext(), new NodeTest());
        assertFalse(attributeContext.nextNode());
    }
}