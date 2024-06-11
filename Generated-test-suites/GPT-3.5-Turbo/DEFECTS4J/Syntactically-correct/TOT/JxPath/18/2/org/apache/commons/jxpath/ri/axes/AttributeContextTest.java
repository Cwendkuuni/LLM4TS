import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class AttributeContextTest {

    // Test case for getCurrentNodePointer() method
    @Test
    public void testGetCurrentNodePointer() {
        // Create a parent context with a node pointer
        EvalContext parentContext = new EvalContext(null);
        NodePointer nodePointer = new NodePointer();
        parentContext.setCurrentNodePointer(nodePointer);

        // Create an attribute context with a node test
        NodeNameTest nodeTest = new NodeNameTest(null, "attributeName");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        // Set the current node pointer and verify
        attributeContext.setCurrentNodePointer(nodePointer);
        assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
    }

    // Test case for reset() method
    @Test
    public void testReset() {
        // Create a parent context with a node pointer
        EvalContext parentContext = new EvalContext(null);
        NodePointer nodePointer = new NodePointer();
        parentContext.setCurrentNodePointer(nodePointer);

        // Create an attribute context with a node test
        NodeNameTest nodeTest = new NodeNameTest(null, "attributeName");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        // Set some values to the context
        attributeContext.setStarted = true;
        attributeContext.iterator = new NodeIterator();
        attributeContext.setCurrentNodePointer(nodePointer);
        attributeContext.setCurrentPosition(5);

        // Reset the context and verify the values are reset
        attributeContext.reset();
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
        assertNull(attributeContext.getCurrentNodePointer());
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    // Test case for setPosition(int position) method
    @Test
    public void testSetPosition() {
        // Create a parent context with a node pointer
        EvalContext parentContext = new EvalContext(null);
        NodePointer nodePointer = new NodePointer();
        parentContext.setCurrentNodePointer(nodePointer);

        // Create an attribute context with a node test
        NodeNameTest nodeTest = new NodeNameTest(null, "attributeName");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        // Set the position to 5 and verify it is set correctly
        assertTrue(attributeContext.setPosition(5));
        assertEquals(5, attributeContext.getCurrentPosition());

        // Set the position to 3 and verify it is set correctly
        assertTrue(attributeContext.setPosition(3));
        assertEquals(3, attributeContext.getCurrentPosition());

        // Set the position to 1 and verify it is set correctly
        assertTrue(attributeContext.setPosition(1));
        assertEquals(1, attributeContext.getCurrentPosition());

        // Set the position to 0 and verify it is reset
        assertFalse(attributeContext.setPosition(0));
        assertEquals(0, attributeContext.getCurrentPosition());

        // Set the position to -1 and verify it is reset
        assertFalse(attributeContext.setPosition(-1));
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    // Test case for nextNode() method
    @Test
    public void testNextNode() {
        // Create a parent context with a node pointer
        EvalContext parentContext = new EvalContext(null);
        NodePointer nodePointer = new NodePointer();
        parentContext.setCurrentNodePointer(nodePointer);

        // Create an attribute context with a node test
        NodeNameTest nodeTest = new NodeNameTest(null, "attributeName");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        // Verify that nextNode returns false without setting the context
        assertFalse(attributeContext.nextNode());

        // Set the context and verify that nextNode returns true
        attributeContext.setStarted = true;
        attributeContext.iterator = new NodeIterator();
        assertTrue(attributeContext.nextNode());

        // Verify that nextNode returns false after setting position to 2
        attributeContext.iterator.setPosition(1);
        assertFalse(attributeContext.nextNode());

        // Reset the iterator and verify that nextNode returns true again
        attributeContext.iterator.setPosition(0);
        assertTrue(attributeContext.nextNode());
    } 

}