
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {

    private AttributeContext attributeContext;
    private NodePointer nodePointer;

    @Before
    public void setUp() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeTest();
        attributeContext = new AttributeContext(parentContext, nodeTest);
        nodePointer = new NodePointer();
    }

    @Test
    public void testGetCurrentNodePointer() {
        attributeContext.currentNodePointer = nodePointer;
        Assert.assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.reset();
        Assert.assertFalse(attributeContext.setStarted);
        Assert.assertNull(attributeContext.iterator);
    }

    @Test
    public void testSetPosition() {
        attributeContext.setPosition(5);
        Assert.assertEquals(5, attributeContext.getCurrentPosition());
    }

    @Test
    public void testSetPosition_resetsContext() {
        attributeContext.setPosition(3);
        attributeContext.setPosition(2);
        Assert.assertFalse(attributeContext.setStarted);
        Assert.assertNull(attributeContext.iterator);
    }

    @Test
    public void testSetPosition_returnsFalseIfPositionInvalid() {
        attributeContext.setPosition(5);
        boolean result = attributeContext.setPosition(8);
        Assert.assertFalse(result);
    }

    @Test
    public void testNextNode() {
        attributeContext.setStarted = false;
        attributeContext.nodeTest = new NodeTest();
        attributeContext.parentContext.setCurrentNodePointer(nodePointer);
        attributeContext.parentContext.getCurrentNodePointer().attributeIterator(null);
        attributeContext.iterator = new NodeIterator();
        attributeContext.iterator.setPosition(1);

        boolean result = attributeContext.nextNode();
        Assert.assertTrue(result);
        Assert.assertEquals(1, attributeContext.getCurrentPosition());
        Assert.assertEquals(nodePointer, attributeContext.currentNodePointer);
    }

    @Test
    public void testNextNode_setStarted_True() {
        attributeContext.setStarted = true;

        boolean result = attributeContext.nextNode();
        Assert.assertFalse(result);
    }

    @Test
    public void testNextNode_attributeIterator_Null() {
        attributeContext.setStarted = false;
        attributeContext.nodeTest = new NodeTest();
        attributeContext.parentContext.setCurrentNodePointer(nodePointer);
        attributeContext.parentContext.getCurrentNodePointer().attributeIterator(null);
        attributeContext.iterator = null;

        boolean result = attributeContext.nextNode();
        Assert.assertFalse(result);
    }

    @Test
    public void testNextNode_iterator_setPosition_returnsFalse() {
        attributeContext.setStarted = false;
        attributeContext.nodeTest = new NodeTest();
        attributeContext.parentContext.setCurrentNodePointer(nodePointer);
        attributeContext.parentContext.getCurrentNodePointer().attributeIterator(null);
        attributeContext.iterator = new NodeIterator();
        attributeContext.iterator.setPosition(2);

        boolean result = attributeContext.nextNode();
        Assert.assertFalse(result);
    }
}
