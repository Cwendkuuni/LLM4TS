import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeContextTest {

    private AttributeContext attributeContext;
    private EvalContext parentContext;
    private NodeTest nodeTest;

    @Before
    public void setup() {
        parentContext = new EvalContext();
        nodeTest = new NodeTest();
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.setPosition(1);
        attributeContext.reset();
        assertFalse(attributeContext.setPosition(2));
    }

    @Test
    public void testSetPosition() {
        assertTrue(attributeContext.setPosition(1));
        assertFalse(attributeContext.setPosition(0));
        assertTrue(attributeContext.setPosition(1));
    }

    @Test
    public void testNextNode() {
        assertFalse(attributeContext.nextNode());
        attributeContext.reset();
        assertTrue(attributeContext.nextNode());
    }
}