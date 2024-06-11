import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeContextTest {

    private AttributeContext attributeContext;
    private EvalContext parentContext;
    private NodeTest nodeTest;

    @Before
    public void setUp() {
        parentContext = new EvalContext(null);
        nodeTest = new NodeNameTest(new QName("", "test"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.reset();
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testSetPosition() {
        assertFalse(attributeContext.setPosition(1));
        attributeContext.setPosition(0);
        assertNull(attributeContext.iterator);
        assertEquals(0, attributeContext.getCurrentPosition());

        parentContext.setCurrentNodePointer(new NodePointer());
        assertFalse(attributeContext.setPosition(1));
        attributeContext.setPosition(0);
        assertNotNull(attributeContext.iterator);
        assertEquals(1, attributeContext.getCurrentPosition());
    }

    @Test
    public void testNextNode() {
        assertFalse(attributeContext.nextNode());

        parentContext.setCurrentNodePointer(new NodePointer());
        assertFalse(attributeContext.nextNode());

        parentContext.setCurrentNodePointer(new NodePointer());
        nodeTest = new NodeNameTest(new QName("", "test"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
        assertTrue(attributeContext.nextNode());
    }
}