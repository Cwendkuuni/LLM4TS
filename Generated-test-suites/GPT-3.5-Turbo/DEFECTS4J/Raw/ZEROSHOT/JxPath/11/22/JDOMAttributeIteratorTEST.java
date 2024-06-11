import static org.junit.Assert.*;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    @Test
    public void testGetNodePointer() {
        // Test the getNodePointer method
        NodePointer parent = new NodePointer();
        QName name = new QName("attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        NodePointer pointer = iterator.getNodePointer();
        
        // Verify that the pointer is not null
        assertNotNull(pointer);
        
        // Verify that the pointer has the correct parent and attribute
        assertEquals(parent, pointer.getParent());
        assertEquals("attribute", pointer.getAttributeName());
    }
    
    @Test
    public void testGetPosition() {
        // Test the getPosition method
        NodePointer parent = new NodePointer();
        QName name = new QName("attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        // Verify that the initial position is 0
        assertEquals(0, iterator.getPosition());
        
        // Verify that setting a valid position updates the position value
        iterator.setPosition(2);
        assertEquals(2, iterator.getPosition());
        
        // Verify that setting an invalid position does not update the position value
        iterator.setPosition(-1);
        assertEquals(2, iterator.getPosition());
    }
    
    @Test
    public void testSetPosition() {
        // Test the setPosition method
        NodePointer parent = new NodePointer();
        QName name = new QName("attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        // Verify that setting a valid position returns true
        assertTrue(iterator.setPosition(1));
        
        // Verify that setting an invalid position returns false
        assertFalse(iterator.setPosition(-1));
    }
}