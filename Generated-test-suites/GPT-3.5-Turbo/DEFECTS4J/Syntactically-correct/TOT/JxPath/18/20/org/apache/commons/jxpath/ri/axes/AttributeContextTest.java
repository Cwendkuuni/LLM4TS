import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {

    private AttributeContext attributeContext;
    private EvalContext parentContext;
    private NodeTest nodeTest;

    @Before
    public void setUp() {
        parentContext = new EvalContext();
        nodeTest = new NodeNameTest("AttributeName");
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @After
    public void tearDown() {
        attributeContext = null;
        parentContext = null;
        nodeTest = null;
    }

    @Test
    public void testGetPosition_EmptyIterator_ReturnsFalse() {
        // Empty iterator
        assertFalse(attributeContext.setPosition(1));
    }

    @Test
    public void testGetPosition_PositionLessThanCurrentPosition_ResetAndMove() {
        // Setup
        attributeContext.setPosition(2);

        // Test
        assertTrue(attributeContext.setPosition(1));
        assertEquals(1, attributeContext.getCurrentPosition());
    }

    @Test
    public void testGetPosition_PositionGreaterThanCurrentPosition_MoveToPosition() {
        // Setup
        attributeContext.setPosition(1);

        // Test
        assertTrue(attributeContext.setPosition(3));
        assertEquals(3, attributeContext.getCurrentPosition());
    }

    @Test
    public void testNextNode_ValidIterator_ReturnsTrue() {
        // Setup
        parentContext.setCurrentNodePointer(new NodePointer());
        NodeIterator iterator = new NodeIterator();
        iterator.setPosition(1);
        parentContext.getCurrentNodePointer().setAttributeIterator(iterator);

        // Test
        assertTrue(attributeContext.nextNode());
        assertEquals(iterator.getNodePointer(), attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNode_NullIterator_ReturnsFalse() {
        // Setup
        parentContext.setCurrentNodePointer(new NodePointer());
        parentContext.getCurrentNodePointer().setAttributeIterator(null);

        // Test
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNode_MoveToNextPosition_ReturnsTrue() {
        // Setup
        parentContext.setCurrentNodePointer(new NodePointer());
        NodeIterator iterator = new NodeIterator();
        iterator.setPosition(1);
        parentContext.getCurrentNodePointer().setAttributeIterator(iterator);

        // Test
        assertTrue(attributeContext.nextNode());
        assertEquals(2, attributeContext.getCurrentPosition());
    }

    @Test
    public void testNextNode_NoMoreNodes_ReturnsFalse() {
        // Setup
        parentContext.setCurrentNodePointer(new NodePointer());
        NodeIterator iterator = new NodeIterator();
        iterator.setPosition(1);
        parentContext.getCurrentNodePointer().setAttributeIterator(iterator);

        // Move to the next position
        attributeContext.nextNode();

        // Test
        assertFalse(attributeContext.nextNode());
    }

}