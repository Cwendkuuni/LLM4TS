package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.*;
import org.junit.Test;

public class DOMAttributeIteratorTest {
  
    @Test
    public void testConstructor() {
        // Test with valid parent and name
        NodePointer parent = new DOMNodePointer();
        QName name = new QName("", "");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator);
        
        // Test with invalid parent and valid name
        NodePointer invalidParent = null;
        iterator = new DOMAttributeIterator(invalidParent, name);
        assertNotNull(iterator);
        
        // Test with valid parent and invalid name
        QName invalidName = null;
        iterator = new DOMAttributeIterator(parent, invalidName);
        assertNotNull(iterator);
    }
    
    @Test
    public void testGetNodePointer() {
        // Test when attributes list is empty
        NodePointer parent = new DOMNodePointer();
        QName name = new QName("", "");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
        
        // Test when attributes list is not empty
        // Add attributes to the list
        iterator.attributes.add(new Attr());
        iterator.attributes.add(new Attr());
        assertNotNull(iterator.getNodePointer());
    }
    
    @Test
    public void testGetPosition() {
        // Test when position is greater than 0
        NodePointer parent = new DOMNodePointer();
        QName name = new QName("", "");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.position = 2;
        assertEquals(2, iterator.getPosition());
        
        // Test when position is less than 0
        iterator.position = -1;
        assertEquals(-1, iterator.getPosition());
    }
    
    @Test
    public void testSetPosition() {
        // Test with valid position
        NodePointer parent = new DOMNodePointer();
        QName name = new QName("", "");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.position);
        
        // Test with invalid position
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(-1));
        assertFalse(iterator.setPosition(2));
    }
}