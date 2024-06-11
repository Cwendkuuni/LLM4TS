import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.axes.EvalContext;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

public class AttributeContextTest {

    @Test
    public void testAttributeContextConstructor() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("testAttribute");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Assert.assertNotNull(attributeContext.getNodeTest());
    }

    @Test
    public void testgetCurrentNodePointer() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("testAttribute");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer expectedNodePointer = new NodePointer(1, 1);
        attributeContext.setCurrentNodePointer(expectedNodePointer);
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        Assert.assertSame(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testReset() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("testAttribute");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.reset();
        Assert.assertFalse(attributeContext.isSetStarted());
        Assert.assertNull(attributeContext.getIterator());
        Assert.assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionWithValidPosition() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("testAttribute");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setPosition(5);
        NodePointer expectedNodePointer = new NodePointer(5, 1);
        attributeContext.setCurrentNodePointer(expectedNodePointer);
        Assert.assertTrue(attributeContext.isSetStarted());
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        Assert.assertSame(expectedNodePointer, actualNodePointer);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPositionWithInvalidPosition() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("testAttribute");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setPosition(1);
    }

    @Test
    public void testNextNodeWithSetStartedFalse() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("testAttribute");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Assert.assertFalse(attributeContext.nextNode());
        Assert.assertFalse(attributeContext.isSetStarted());
    }

    @Test
    public void testNextNodeWithSetStartedTrueAndIteratorNotNull() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("testAttribute");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer currentNodePointer = new NodePointer(1, 1);
        attributeContext.setCurrentNodePointer(currentNodePointer);
        NodeIterator iterator = new NodeIterator(null);
        attributeContext.setIterator(iterator);
        attributeContext.nextNode();
        Assert.assertTrue(attributeContext.isSetStarted());
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        Assert.assertNotSame(currentNodePointer, actualNodePointer);
    }

    @Test
    public void testNextNodeWithSetStartedTrueAndIteratorNull() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("testAttribute");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer currentNodePointer = new NodePointer(1, 1);
        attributeContext.setCurrentNodePointer(currentNodePointer);
        attributeContext.nextNode();
        Assert.assertTrue(attributeContext.isSetStarted());
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        Assert.assertNull(actualNodePointer);
    }
}