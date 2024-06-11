import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;

public class AttributeContextTest {
    private NodePointer dummyNodePointer;

    @Test
    public void testConstructor() {
        NodeTest nodeTest = new NodeNameTest(null, "testAttribute");
        AttributeContext context = new AttributeContext(dummyNodePointer, nodeTest);
        assertEquals(dummyNodePointer, context.getParentContext());
        assertEquals(nodeTest, context.getNodeTest());
        assertNull(context.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        NodeTest nodeTest = new NodeNameTest(null, "testAttribute");
        AttributeContext context = new AttributeContext(dummyNodePointer, nodeTest);

        context.setPosition(5);
        context.reset();

        assertFalse(context.isSetStarted());
        assertNull(context.getIterator());
        assertEquals(0, context.getCurrentPosition());
    }

    @Test
    public void testSetPosition() {
        NodeTest nodeTest = new NodeNameTest(null, "testAttribute");
        AttributeContext context = new AttributeContext(dummyNodePointer, nodeTest);

        context.setIterator(dummyIterator);
        context.setPosition(3);

        assertEquals(3, context.getCurrentPosition());
    }

    @Test
    public void testNextNode() {
        NodeTest nodeTest = new NodeNameTest(null, "testAttribute");
        AttributeContext context = new AttributeContext(dummyNodePointer, nodeTest);

        context.setIterator(dummyIterator);
        assertTrue(context.nextNode());
        assertEquals(dummyNodePointer, context.getCurrentNodePointer());

        // Test for null iterator
        context.setIterator(null);
        assertFalse(context.nextNode());

        // Test iterator next position returns false
        context.setIterator(dummyIterator);
        dummyIterator.setPosition(7);
        assertFalse(context.nextNode());
    }
}
