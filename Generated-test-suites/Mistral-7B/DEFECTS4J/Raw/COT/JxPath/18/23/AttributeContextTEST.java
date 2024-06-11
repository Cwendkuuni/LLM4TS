import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.axes.EvalContext;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the AttributeContext class.
 */
public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        parentContext = new EvalContext(null, null);
        nodeTest = new NodeNameTest(new QName("test:attr"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(attributeContext);
        Assert.assertNotNull(attributeContext.nodeTest);
        Assert.assertNull(attributeContext.iterator);
        Assert.assertNull(attributeContext.currentNodePointer);
        Assert.assertFalse(attributeContext.setStarted);
    }

    @Test
    public void testGetCurrentNodePointer() {
        Assert.assertNull(attributeContext.getCurrentNodePointer());
        attributeContext.nextNode();
        Assert.assertNotNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.nextNode();
        attributeContext.reset();
        Assert.assertFalse(attributeContext.setStarted);
        Assert.assertNull(attributeContext.iterator);
        Assert.assertNull(attributeContext.currentNodePointer);
    }

    @Test
    public void testSetPosition() {
        NodePointer nodePointer = new NodePointer(new Object(), 0);
        parentContext.setCurrentNodePointer(nodePointer);
        attributeContext.nextNode();
        Assert.assertTrue(attributeContext.setPosition(0));
        Assert.assertSame(nodePointer, attributeContext.getCurrentNodePointer());
        Assert.assertTrue(attributeContext.nextNode());
        Assert.assertFalse(attributeContext.setPosition(-1));
        Assert.assertFalse(attributeContext.setPosition(Integer.MAX_VALUE));
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPositionWhenNotStarted() {
        Assert.assertFalse(attributeContext.setPosition(0));
    }

    @Test
    public void testNextNode() {
        NodePointer nodePointer = new NodePointer(new Object(), 0);
        parentContext.setCurrentNodePointer(nodePointer);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Assert.assertFalse(attributeContext.nextNode());
        parentContext.setCurrentNodePointer(new NodePointer(new Object(), 1));
        Assert.assertTrue(attributeContext.nextNode());
        Assert.assertNotNull(attributeContext.iterator);
        Assert.assertNotNull(attributeContext.currentNodePointer);
    }

    @Test(expected = IllegalStateException.class)
    public void testNextNodeWhenNotStarted() {
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Assert.assertFalse(attributeContext.nextNode());
    }

    @Test(expected = NullPointerException.class)
    public void testNextNodeWhenParentHasNoCurrentNode() {
        parentContext = new EvalContext(null, null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Assert.assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeWithEmptyAttributeIterator() {
        NodePointer nodePointer = new NodePointer(new Object(), 0);
        parentContext.setCurrentNodePointer(nodePointer);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodeIterator iterator = new NodeIterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public NodePointer next() {
                return null;
            }
        };
        attributeContext.iterator = iterator;
        Assert.assertFalse(attributeContext.nextNode());
    }
}