
import org.junit.Assert;
import org.junit.Test;

public class AttributeContextTest {

    @Test
    public void testGetCurrentNodePointer() {
        // Create a parent context and node test
        EvalContext parentContext = new EvalContext();
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));

        // Create an AttributeContext
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        // Verify that the current node pointer is null initially
        Assert.assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        // Create a parent context and node test
        EvalContext parentContext = new EvalContext();
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));

        // Create an AttributeContext
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        // Set the position and verify that it has been set
        attributeContext.setPosition(10);
        Assert.assertEquals(10, attributeContext.getCurrentPosition());

        // Reset the AttributeContext
        attributeContext.reset();

        // Verify that the position has been reset to 0 and other attributes have been reset
        Assert.assertEquals(0, attributeContext.getCurrentPosition());
        Assert.assertFalse(attributeContext.setStarted);
        Assert.assertNull(attributeContext.iterator);
        Assert.assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        // Create a parent context and node test
        EvalContext parentContext = new EvalContext();
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));

        // Create an AttributeContext
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        // Set the position to 5 and verify that it has been set correctly
        Assert.assertTrue(attributeContext.setPosition(5));
        Assert.assertEquals(5, attributeContext.getCurrentPosition());

        // Set the position to a previous position and verify that the AttributeContext has been reset
        Assert.assertFalse(attributeContext.setPosition(4));
        Assert.assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testNextNode() {
        // Create a parent context and node test
        EvalContext parentContext = new EvalContext();
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));

        // Create an AttributeContext
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        // Set the position and verify that it has been set
        attributeContext.setPosition(1);
        Assert.assertEquals(1, attributeContext.getCurrentPosition());

        // Call nextNode and verify that it returns true and updates the current node pointer
        Assert.assertTrue(attributeContext.nextNode());
        Assert.assertNotNull(attributeContext.getCurrentNodePointer());

        // Call nextNode again and verify that it returns false since there are no further nodes to iterate
        Assert.assertFalse(attributeContext.nextNode());
    }
}
