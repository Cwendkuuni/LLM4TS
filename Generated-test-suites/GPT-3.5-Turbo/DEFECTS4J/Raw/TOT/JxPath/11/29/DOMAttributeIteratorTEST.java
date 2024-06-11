import org.junit.Test;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;

import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

    @Test
    public void testGetNodePointer() {
        // Typical use case: DOMAttributeIterator has attributes, position is 0
        // Expected: returns the first attribute
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(), new QName());
        iterator.setPosition(0);
        assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        // Typical use case: position is 3
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(), new QName());
        iterator.setPosition(3);
        assertEquals(3, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        // Typical use case: valid position
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(), new QName());
        assertTrue(iterator.setPosition(5));

        // Edge case: position is lower than 1
        assertFalse(iterator.setPosition(0));

        // Edge case: position is higher than the number of attributes
        assertFalse(iterator.setPosition(10));
    }
}