import static org.junit.Assert.*;
import org.junit.Test;

public class DOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        NodePointer parent = null;
        QName name = null;
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        
        assertNotNull(iterator);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer parent = null;
        QName name = null;
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        NodePointer parent = null;
        QName name = null;
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        NodePointer parent = null;
        QName name = null;
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        
        assertFalse(iterator.setPosition(0));
        assertTrue(iterator.setPosition(1));
    }
}