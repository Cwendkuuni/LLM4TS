import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class AttributeContextTest {

    private EvalContext parentContext = new EvalContext();
    private NodeNameTest nodeNameTest = new NodeNameTest();
    private AttributeContext attributeContext;

    @Test
    public void testGetCurrentNodePointer() {
        attributeContext = new AttributeContext(parentContext, nodeNameTest);
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();

        Assert.assertNull(currentNodePointer);
    }

    @Test
    public void testReset() {
        attributeContext = new AttributeContext(parentContext, nodeNameTest);
        attributeContext.reset();

        Assert.assertFalse(attributeContext.nextNode());
        Assert.assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        attributeContext = new AttributeContext(parentContext, nodeNameTest);
        boolean result = attributeContext.setPosition(1);

        Assert.assertFalse(result);
    }

    @Test
    public void testNextNode() {
        attributeContext = new AttributeContext(parentContext, nodeNameTest);

        Assert.assertFalse(attributeContext.nextNode());
        Assert.assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeWithValidNodeTest() {
        nodeNameTest.setNodeName(new QName("test"));
        attributeContext = new AttributeContext(parentContext, nodeNameTest);

        Assert.assertFalse(attributeContext.nextNode());
        Assert.assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeWithInvalidNodeTest() {
        nodeNameTest.setNodeName(new QName("test"));
        attributeContext = new AttributeContext(parentContext, nodeNameTest);

        Assert.assertFalse(attributeContext.nextNode());
        Assert.assertNull(attributeContext.getCurrentNodePointer());
    }

}