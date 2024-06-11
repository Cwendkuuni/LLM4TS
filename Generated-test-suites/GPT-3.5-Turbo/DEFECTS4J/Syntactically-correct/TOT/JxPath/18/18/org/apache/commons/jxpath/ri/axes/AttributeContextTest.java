import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {

    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        attributeContext = new AttributeContext(null, new NodeNameTest("testAttribute"));
    }

    @Test
    public void testGetCurrentNodePointer() {
        Assert.assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.reset();
        Assert.assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testSetPosition() {
        Assert.assertTrue(attributeContext.setPosition(0));
        Assert.assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testSetPositionInvalidPosition() {
        Assert.assertFalse(attributeContext.setPosition(1));
        Assert.assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testNextNode() {
        attributeContext.setPosition(0);
        Assert.assertTrue(attributeContext.nextNode());
        Assert.assertNotNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeNoIterator() {
        attributeContext.setPosition(0);
        attributeContext.reset();
        Assert.assertFalse(attributeContext.nextNode());
        Assert.assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeInvalidIteratorPosition() {
        attributeContext = new AttributeContext(null, new NodeNameTest("invalidAttribute"));
        attributeContext.setPosition(0);
        Assert.assertFalse(attributeContext.nextNode());
        Assert.assertNull(attributeContext.getCurrentNodePointer());
    }

}