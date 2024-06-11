import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.apache.commons.jxpath.ri.QName;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIteratorConstructor() {
        // Test constructor with valid parent and name
        DOMNodePointer parent = new DOMNodePointer();
        QName name = new QName("test", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator);
    }

    @Test
    public void testGetPositionAndSetPosition() {
        // Test initial position
        DOMNodePointer parent = new DOMNodePointer();
        QName name = new QName("test", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertEquals(0, iterator.getPosition());

        // Test setting position to a valid value
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());

        // Test setting position to an invalid value
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(100));
    }

    @Test
    public void testGetNodePointer() {
        // Test getting node pointer at position 1
        DOMNodePointer parent = new DOMNodePointer();
        QName name = new QName("test", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertTrue(iterator.setPosition(1));
        assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testExceptions() {
        // Test constructor with invalid parent
        DOMNodePointer parent = null;
        QName name = new QName("test", "test");
        try {
            DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test setting invalid position
        DOMNodePointer parent = new DOMNodePointer();
        QName name = new QName("test", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        try {
            iterator.setPosition(0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEdgeCases() {
        // Test edge case of empty attributes list
        DOMNodePointer parent = new DOMNodePointer();
        QName name = new QName("test", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertFalse(iterator.setPosition(1));
    }
}