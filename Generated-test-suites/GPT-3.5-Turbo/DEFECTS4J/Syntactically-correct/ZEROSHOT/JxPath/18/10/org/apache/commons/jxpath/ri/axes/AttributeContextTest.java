import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {

    private AttributeContext attributeContext;
    private EvalContext parentContext;
    private NodeTest nodeTest;

    @Before
    public void setUp() {
        parentContext = new EvalContext();
        nodeTest = new NodeNameTest(new QName());
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testGetCurrentNodePointer() {
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        Assert.assertNull(currentNodePointer);
    }

    @Test
    public void testReset() {
        attributeContext.reset();

        boolean setStarted = attributeContext.isSetStarted();
        Assert.assertFalse(setStarted);

        NodeIterator iterator = attributeContext.getIterator();
        Assert.assertNull(iterator);

        String currentExpression = attributeContext.getCurrentExpression();
        Assert.assertNull(currentExpression);
    }

    @Test
    public void testSetPosition() {
        int position = 1;

        boolean setPosition = attributeContext.setPosition(position);
        Assert.assertFalse(setPosition);

        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        Assert.assertNull(currentNodePointer);
    }

    @Test
    public void testNextNode() {
        boolean nextNode = attributeContext.nextNode();
        Assert.assertFalse(nextNode);

        boolean setStarted = attributeContext.isSetStarted();
        Assert.assertTrue(setStarted);

        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        Assert.assertNull(currentNodePointer);
    }
}