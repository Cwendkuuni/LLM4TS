import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {

    private AttributeContext context;
    private MockEvalContext parentContext;
    private NodeTest nodeTest;

    @Before
    public void setUp() {
        nodeTest = new NodeNameTest(new QName("attribute"));
        parentContext = new MockEvalContext();
        context = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(context.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        context.reset();
        assertFalse(context.setStarted);
        assertNull(context.iterator);
        assertEquals(0, context.getCurrentPosition());
    }

    @Test
    public void testSetPosition() {
        assertTrue(context.setPosition(1));
        assertEquals(1, context.getCurrentPosition());
        assertNotNull(context.iterator);
        assertNotNull(context.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition_reset() {
        context.setPosition(1);
        context.setPosition(0);
        assertFalse(context.setStarted);
        assertNull(context.iterator);
        assertEquals(0, context.getCurrentPosition());
    }

    @Test
    public void testNextNode() {
        assertTrue(context.nextNode());
        assertEquals(1, context.getCurrentPosition());
        assertNotNull(context.iterator);
        assertNotNull(context.getCurrentNodePointer());
    }

    @Test
    public void testNextNode_reset() {
        context.nextNode();
        context.nextNode();
        assertFalse(context.setStarted);
        assertNull(context.iterator);
        assertEquals(0, context.getCurrentPosition());
    }
}