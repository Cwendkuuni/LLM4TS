import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {

    private AttributeContext attributeContext;
    private NodeTest nodeTest;
    private NodePointer nodePointer;

    @Before
    public void setUp() {
        EvalContext parentContext = new EvalContext();
        QName attributeName = new QName("test");
        nodeTest = new NodeNameTest(NodeNameTest.EQ, attributeName);
        attributeContext = new AttributeContext(parentContext, nodeTest);
        nodePointer = new NodePointer();
    }

    @Test
    public void testGetCurrentNodePointer() {
        attributeContext.setCurrentNodePointer(nodePointer);
        assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.setCurrentPosition(1);
        attributeContext.setStarted(true);
        attributeContext.setIterator(new NodeIterator());
        attributeContext.reset();
        assertEquals(0, attributeContext.getCurrentPosition());
        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.getIterator());
    }

    @Test
    public void testSetPosition_positionLowerThanCurrentPosition_resetsContext() {
        attributeContext.setCurrentPosition(3);
        attributeContext.setPosition(1);
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testSetPosition_positionGreaterThanCurrentPosition_movesToPosition() {
        attributeContext.setCurrentPosition(1);
        attributeContext.setSetStarted(true);

        NodeIterator iterator = new NodeIterator();
        iterator.setPosition(1);
        attributeContext.setIterator(iterator);
        assertFalse(attributeContext.setPosition(5));

        assertEquals(5, attributeContext.getCurrentPosition());
        assertTrue(attributeContext.isSetStarted());
        assertSame(iterator, attributeContext.getIterator());
        assertSame(iterator.getNodePointer(), attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNode_setNotStarted_returnsFalse() {
        attributeContext.setStarted(false);
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNode_nodeTestNotInstanceOfNodeNameTest_returnFalse() {
        attributeContext.setStarted(true);
        attributeContext.setIterator(null);
        attributeContext.setNodeTest(new NodeNameTest());
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNode_iteratorIsNull_returnFalse() {
        attributeContext.setStarted(true);
        attributeContext.setIterator(null);
        attributeContext.setNodeTest(new NodeNameTest(NodeNameTest.EQ, new QName("test")));
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNode_iteratorPositionExceedsLength_returnFalse() {
        attributeContext.setStarted(true);
        NodeIterator iterator = new NodeIterator();
        iterator.setPosition(5);
        attributeContext.setIterator(iterator);
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNode_iteratorPositionsIncrements_setCurrentNodePointer() {
        attributeContext.setStarted(true);
        NodeIterator iterator = new NodeIterator();
        iterator.setPosition(1);
        attributeContext.setIterator(iterator);
        attributeContext.nextNode();
        assertEquals(2, attributeContext.getCurrentPosition());
        assertSame(iterator.getNodePointer(), attributeContext.getCurrentNodePointer());
    }
}