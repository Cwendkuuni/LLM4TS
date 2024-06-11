
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    
    private JDOMAttributeIterator attributeIterator;
    
    @Before
    public void setUp() {
        NodePointer parent = new MockNodePointer();
        QName name = new QName("attr");
        attributeIterator = new JDOMAttributeIterator(parent, name);
    }
    
    // Test getNodePointer() method
    
    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = attributeIterator.getNodePointer();
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof JDOMAttributePointer);
    }
    
    // Test getPosition() method
    
    @Test
    public void testGetPosition() {
        int position = attributeIterator.getPosition();
        assertEquals(0, position);
    }
    
    // Test setPosition() method
    
    @Test
    public void testSetPosition() {
        assertTrue(attributeIterator.setPosition(1));
        assertEquals(1, attributeIterator.getPosition());
        
        assertFalse(attributeIterator.setPosition(0));
        assertEquals(1, attributeIterator.getPosition());
        
        assertFalse(attributeIterator.setPosition(2));
        assertEquals(1, attributeIterator.getPosition());
        
    }
}
