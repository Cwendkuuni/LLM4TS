import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeContextTest {

    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        attributeContext = new AttributeContext(null, null);
    }

    @Test
    public void testGetCurrentNodePointer() {
        // Test when current node pointer is null
        assertNull(attributeContext.getCurrentNodePointer());

        // Test when current node pointer is not null
        NodePointer nodePointer = new NodePointer();
        attributeContext.currentNodePointer = nodePointer;
        assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.setPosition(2);
        attributeContext.setStarted = true;

        attributeContext.reset();

        assertEquals(0, attributeContext.getCurrentPosition());
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        attributeContext.reset();
        attributeContext.setPosition(2);
        assertEquals(2, attributeContext.getCurrentPosition());

        // Test when position is less than current position
        attributeContext.setPosition(1);
        assertEquals(0, attributeContext.getCurrentPosition());
        assertNull(attributeContext.getCurrentNodePointer());

        // Test when position is greater than current position
        attributeContext.setPosition(3);
        assertEquals(2, attributeContext.getCurrentPosition());
        assertNotNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNode() {
        // Test when set started is false
        attributeContext.setStarted = false;
        assertFalse(attributeContext.nextNode());

        // Test when set started is true and iterator is null
        attributeContext.setStarted = true;
        attributeContext.iterator = null;
        assertFalse(attributeContext.nextNode());

        // Test when set started is true, iterator is not null, and iterator position increment is successful
        attributeContext.nodeTest = new NodeNameTest();
        attributeContext.iterator = new NodeIterator();
        attributeContext.iterator.setPosition(1);

        assertTrue(attributeContext.nextNode());
        assertEquals(1, attributeContext.getCurrentPosition());
        assertNotNull(attributeContext.getCurrentNodePointer());

        // Test when set started is true, iterator is not null, and iterator position increment is unsuccessful
        attributeContext.iterator.setPosition(2);

        assertFalse(attributeContext.nextNode());
        assertEquals(2, attributeContext.getCurrentPosition());
        assertNull(attributeContext.getCurrentNodePointer());
    }

}