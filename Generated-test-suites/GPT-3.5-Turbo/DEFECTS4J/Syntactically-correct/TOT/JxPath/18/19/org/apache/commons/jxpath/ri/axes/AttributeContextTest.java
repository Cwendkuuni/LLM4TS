import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {

    private AttributeContext context;
    private EvalContext parentContext;
    private NodeTest nodeTest;

    @Before
    public void setUp() {
        parentContext = new EvalContext();
        nodeTest = new NodeNameTest(new QName(""));
        context = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(context.getCurrentNodePointer());

        // Set current node pointer
        NodePointer nodePointer = new NodePointer();
        context.setCurrentNodePointer(nodePointer);
        assertEquals(nodePointer, context.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        context.setPosition(1);

        context.reset();

        assertEquals(0, context.getCurrentPosition());
        assertFalse(context.isSetStarted());
        assertNull(context.getIterator());
        assertNull(context.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        assertFalse(context.setPosition(1));

        context.reset();
        context.setStarted(true);
        context.getNodeTest() = new NodeNameTest(new QName("test"));
        parentContext.setCurrentNodePointer(new NodePointer());

        assertTrue(context.setPosition(1));
    }

    @Test
    public void testNextNode() {
        assertFalse(context.nextNode());

        // Set started
        context.setStarted(true);

        // Set node test
        NodeNameTest nodeNameTest = new NodeNameTest(new QName("test"));
        context.setNodeTest(nodeNameTest);

        // Set parent context
        NodePointer nodePointer = new NodePointer();
        nodePointer.attributeIterator(nodeNameTest.getNodeName());
        parentContext.setCurrentNodePointer(nodePointer);

        // Set iterator
        context.setIterator(nodePointer.attributeIterator(nodeNameTest.getNodeName()));

        assertFalse(context.nextNode());

        // Set iterator position
        context.getIterator().setPosition(1);

        // Set current node pointer
        NodePointer currentNodePointer = context.getIterator().getNodePointer();
        context.setCurrentNodePointer(currentNodePointer);

        assertTrue(context.nextNode());
        assertEquals(currentNodePointer, context.getCurrentNodePointer());
    }
}