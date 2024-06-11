import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    
    private NodePointer parent;
    private JDOMAttributeIterator iterator;
    
    @Before
    public void setUp() {
        parent = new JDOMAttributePointer(); // Set up parent for testing.
    }
    
    @Test
    public void testConstructor_withValidParentAndName() {
        QName name = new QName("attributeName");
        iterator = new JDOMAttributeIterator(parent, name);
        
        assertNotNull(iterator);
        // Check that the iterator is positioned at the first attribute.
        assertEquals(1, iterator.getPosition());
    }
    
    @Test
    public void testConstructor_withInvalidParent() {
        // Test with parent that is not an Element
        parent = new JDOMElementPointer(); // Create a JDOMElementPointer object as parent instead of JDOMAttributePointer.
        QName name = new QName("attributeName");
        iterator = new JDOMAttributeIterator(parent, name);
        
        assertNotNull(iterator);
        // Check that the iterator does not have any attribute and is not positioned.
        assertEquals(0, iterator.getPosition());
    }
    
    @Test
    public void testConstructor_withInvalidName() {
        // Test with name that does not exist in the element's attributes.
        QName name = new QName("nonexistentAttribute");
        iterator = new JDOMAttributeIterator(parent, name);
        
        assertNotNull(iterator);
        // Check that the iterator does not have any attribute and is not positioned.
        assertEquals(0, iterator.getPosition());
    }
    
    @Test
    public void testGetNodePointer_withValidPosition() {
        // Set up the iterator with valid parent and name.
        QName name = new QName("attributeName");
        iterator = new JDOMAttributeIterator(parent, name);
        
        // Get the first attribute pointer in the iterator.
        NodePointer nodePointer = iterator.getNodePointer();
        
        // Check that the returned nodePointer is not null and is the correct attribute.
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals("attributeName", ((JDOMAttributePointer)nodePointer).getAttributeName());
    }
    
    @Test
    public void testGetNodePointer_withInvalidPosition() {
        // Set up the iterator with valid parent and name.
        QName name = new QName("attributeName");
        iterator = new JDOMAttributeIterator(parent, name);
        
        // Set the position of the iterator to an invalid value.
        iterator.setPosition(0);
        
        // Get the node pointer at the invalid position.
        NodePointer nodePointer = iterator.getNodePointer();
        
        // Check that the returned nodePointer is null.
        assertNull(nodePointer);
    }
    
    @Test
    public void testGetPosition_withValidPosition() {
        // Set up the iterator with valid parent and name.
        QName name = new QName("attributeName");
        iterator = new JDOMAttributeIterator(parent, name);
        
        // Set the position of the iterator to a valid value.
        iterator.setPosition(2);
        
        // Get the current position of the iterator.
        int position = iterator.getPosition();
        
        // Check that the returned position is the same as set.
        assertEquals(2, position);
    }
    
    @Test
    public void testGetPosition_withInvalidPosition() {
        // Set up the iterator with valid parent and name.
        QName name = new QName("attributeName");
        iterator = new JDOMAttributeIterator(parent, name);
        
        // Set the position of the iterator to an invalid value.
        iterator.setPosition(0);
        
        // Get the current position of the iterator.
        int position = iterator.getPosition();
        
        // Check that the returned position is 0.
        assertEquals(0, position);
    }
    
    @Test
    public void testSetPosition_withValidPosition() {
        // Set up the iterator with valid parent and name.
        QName name = new QName("attributeName");
        iterator = new JDOMAttributeIterator(parent, name);
        
        // Set the position of the iterator to a valid value.
        boolean result = iterator.setPosition(2);
        
        // Check that the result is true and the iterator is positioned at the specified position.
        assertTrue(result);
        assertEquals(2, iterator.getPosition());
    }
    
    @Test
    public void testSetPosition_withInvalidPosition() {
        // Set up the iterator with valid parent and name.
        QName name = new QName("attributeName");
        iterator = new JDOMAttributeIterator(parent, name);
        
        // Set the position of the iterator to an invalid value.
        boolean result = iterator.setPosition(0);
        
        // Check that the result is false and the iterator is not positioned.
        assertFalse(result);
        assertEquals(0, iterator.getPosition());
    }
}