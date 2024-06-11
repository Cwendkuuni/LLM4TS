
import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.axes.EvalContext;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {

    @Test
    public void testAttributeContextConstructor() {
        NodeTest nodeTest = new NodeNameTest("test");
        EvalContext parentContext = new EvalContext(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNotNull(attributeContext.getCurrentNodePointer());
        assertNotNull(attributeContext.iterator);
    }

    @Test
    public void testGetCurrentNodePointer() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        assertEquals(null, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testGetCurrentNodePointer_whenAttributeContextHasCurrentNodePointer_thenReturnCurrentNodePointer() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        NodePointer nodePointer = new NodePointer("testNode");
        attributeContext.currentNodePointer = nodePointer;
        assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset_whenAttributeContextIsNotStarted_thenResetIsCalledSuccessfully() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        attributeContext.reset();
        assertFalse(attributeContext.setStarted);
    }

    @Test
    public void testReset_whenAttributeContextIsStarted_thenResetIsCalledSuccessfullyAndSetStartedIsSetToFalse() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        attributeContext.setStarted = true;
        attributeContext.reset();
        assertFalse(attributeContext.setStarted);
    }

    @Test
    public void testNextNode_whenAttributeContextIsNotStarted_thenNextNodeReturnsFalse() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNode_whenAttributeContextIsStartedAndIteratorHasNextNode_thenNextNodeReturnsTrue() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        attributeContext.setStarted = true;
        NodeIterator iterator = new MockNodeIterator();
        attributeContext.iterator = iterator;
        iterator.setHasNext(true);
        assertTrue(attributeContext.nextNode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNextNode_whenAttributeContextIsStartedButIteratorHasNoNextNode_thenNextNodeThrowsUnsupportedOperationException() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        attributeContext.setStarted = true;
        NodeIterator iterator = new MockNodeIterator();
        attributeContext.iterator = iterator;
        iterator.setHasNext(false);
        assertFalse(attributeContext.nextNode());
    }
}

class MockNodeIterator implements NodeIterator {
    private boolean hasNext = false;

    public boolean hasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public NodePointer getNodePointer() {
        return new NodePointer("testNode");
    }
}
