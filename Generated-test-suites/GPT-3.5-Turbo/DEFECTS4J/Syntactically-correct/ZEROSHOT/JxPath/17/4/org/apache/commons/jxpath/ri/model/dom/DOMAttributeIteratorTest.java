import org.junit.Test;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        NodePointer parent = new NodePointer();
        QName name = new QName();
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        
        assertNotNull(iterator);
    }
    
    @Test
    public void testGetNodePointer() {
        NodePointer parent = new NodePointer();
        QName name = new QName();
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        
        NodePointer nodePointer = iterator.getNodePointer();
        
        assertNotNull(nodePointer);
    }
    
    @Test
    public void testGetPosition() {
        NodePointer parent = new NodePointer();
        QName name = new QName();
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        
        int position = iterator.getPosition();
        
        assertEquals(0, position);
    }
    
    @Test
    public void testSetPosition() {
        NodePointer parent = new NodePointer();
        QName name = new QName();
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        
        boolean result = iterator.setPosition(1);
        
        assertTrue(result);
        assertEquals(1, iterator.getPosition());
    }
    
    @Test
    public void testSetPositionOutOfBounds() {
        NodePointer parent = new NodePointer();
        QName name = new QName();
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        
        boolean result = iterator.setPosition(0);
        
        assertFalse(result);
        assertEquals(0, iterator.getPosition());
    }
    
    @Test
    public void testSetPositionGreaterThanSize() {
        NodePointer parent = new NodePointer();
        QName name = new QName();
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        
        boolean result = iterator.setPosition(2);
        
        assertFalse(result);
        assertEquals(0, iterator.getPosition());
    }
}