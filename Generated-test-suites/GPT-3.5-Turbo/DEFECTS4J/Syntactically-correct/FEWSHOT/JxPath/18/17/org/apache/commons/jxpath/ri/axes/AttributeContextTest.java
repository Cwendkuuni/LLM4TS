import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {
    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext context;

    @Before
    public void setUp() {
        parentContext = new EvalContext();
        nodeTest = new NodeNameTest(new QName("attribute"));
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
        assertEquals(0, context.getIterations());
    }

    @Test
    public void testSetPosition() {
        // Test when position is less than current position
        context.setPosition(5);
        context.reset();
        assertTrue(context.setPosition(3));

        // Test when position is equal to current position
        context.reset();
        assertTrue(context.setPosition(0));

        // Test when position is greater than current position
        context.reset();
        assertTrue(context.setPosition(7));
    }

    @Test
    public void testNextNode() {
        context.nextNode();
        assertFalse(context.setStarted);
        assertNull(context.iterator);
        assertNull(context.getCurrentNodePointer());
    }
}