
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {

    private AttributeContext attributeContext;
    private EvalContext parentContext;

    @Before
    public void setUp() {
        parentContext = new EvalContext();
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
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
        attributeContext.reset();

        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
        assertEquals(0, attributeContext.getCurrentPosition());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        assertFalse(attributeContext.setPosition(1));

        attributeContext.setPosition(0);

        assertTrue(attributeContext.setPosition(0));
        assertFalse(attributeContext.setPosition(-1));

        attributeContext.reset();
        attributeContext.setPosition(1);

        assertTrue(attributeContext.setPosition(1));
        assertFalse(attributeContext.setPosition(2));
    }

    @Test
    public void testNextNode() {
        attributeContext.setStarted = true;

        assertFalse(attributeContext.nextNode());

        attributeContext.setStarted = false;

        assertFalse(attributeContext.nextNode());

        attributeContext.setStarted = true;
        attributeContext.parentContext = null;

        assertFalse(attributeContext.nextNode());

        attributeContext.parentContext = new EvalContext();
        attributeContext.parentContext.currentNodePointer = new NodePointer();
        attributeContext.iterator = new NodeIterator(attributeContext.parentContext.currentNodePointer);

        assertFalse(attributeContext.nextNode());

        attributeContext.nodeTest = new NodeNameTest(new QName("attribute"));
        attributeContext.iterator = new NodeIterator(attributeContext.parentContext.currentNodePointer);
        attributeContext.iterator.setPosition(0);
        attributeContext.currentNodePointer = attributeContext.iterator.getNodePointer();

        assertTrue(attributeContext.nextNode());
    }
}
