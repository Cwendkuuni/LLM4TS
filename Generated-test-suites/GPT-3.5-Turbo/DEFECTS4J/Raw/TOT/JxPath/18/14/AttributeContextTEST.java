import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {

    private AttributeContext attributeContext;
    private NodePointer currentNodePointer;

    @Before
    public void setup() {
        NodeTest nodeTest = new NodeNameTest("attribute");
        attributeContext = new AttributeContext(null, nodeTest);

        currentNodePointer = new NodePointer();
        attributeContext.parentContext.setCurrentNodePointer(currentNodePointer);
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertEquals(currentNodePointer, attributeContext.getCurrentNodePointer());
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
    }

    @Test
    public void testReset() {
        attributeContext.setStarted = true;
        attributeContext.iterator = new NodeIterator();
        attributeContext.setCurrentPosition(5);

        attributeContext.reset();

        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testSetPosition_validPosition() {
        boolean result = attributeContext.setPosition(2);

        assertTrue(result);
        assertEquals(2, attributeContext.getCurrentPosition());
        assertEquals(2, attributeContext.iterator.getPosition());
        assertEquals(currentNodePointer, attributeContext.iterator.getNodePointer());
    }

    @Test
    public void testSetPosition_lessThanCurrentPosition() {
        attributeContext.setCurrentPosition(3);

        boolean result = attributeContext.setPosition(1);

        assertTrue(result);
        assertEquals(1, attributeContext.getCurrentPosition());
        assertEquals(1, attributeContext.iterator.getPosition());
        assertEquals(currentNodePointer, attributeContext.iterator.getNodePointer());
    }

    @Test
    public void testSetPosition_invalidPosition() {
        attributeContext.setCurrentPosition(2);

        boolean result = attributeContext.setPosition(4);

        assertFalse(result);
        assertEquals(2, attributeContext.getCurrentPosition());
        assertEquals(2, attributeContext.iterator.getPosition());
        assertEquals(currentNodePointer, attributeContext.iterator.getNodePointer());
    }

    @Test
    public void testNextNode_success() {
        NodeIterator iterator = new NodeIterator();
        NodePointer nodePointer = new NodePointer();
        iterator.setNodePointer(nodePointer);
        attributeContext.parentContext.setCurrentNodePointerIterator(iterator);

        assertTrue(attributeContext.nextNode());
        assertEquals(1, attributeContext.getCurrentPosition());
        assertEquals(2, attributeContext.iterator.getPosition());
        assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNode_noIterator() {
        attributeContext.iterator = null;

        assertFalse(attributeContext.nextNode());
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testNextNode_invalidIteratorPosition() {
        NodeIterator iterator = new NodeIterator();
        iterator.setPosition(3);
        attributeContext.parentContext.setCurrentNodePointerIterator(iterator);

        assertFalse(attributeContext.nextNode());
        assertEquals(1, attributeContext.getCurrentPosition());
    }
}