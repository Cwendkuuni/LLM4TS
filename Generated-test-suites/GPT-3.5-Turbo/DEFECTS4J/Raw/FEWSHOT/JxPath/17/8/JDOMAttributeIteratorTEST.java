package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Element;

public class JDOMAttributeIteratorTest {
    
    // test constructor with valid parameters
    @Test
    public void testConstructor() {
        // create a parent NodePointer
        NodePointer parent = new NodePointer();
        // create a valid QName
        QName name = new QName("test", "attribute");
        // create a JDOMAttributeIterator object using the constructor
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        // check if the iterator object is not null
        assertNotNull(iterator);
    }
    
    // test constructor with null parent NodePointer
    @Test
    public void testConstructorWithNullParent() {
        // create a null parent NodePointer
        NodePointer parent = null;
        // create a valid QName
        QName name = new QName("test", "attribute");
        // create a JDOMAttributeIterator object using the constructor
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        // check if the iterator object is null
        assertNull(iterator);
    }
    
    // test constructor with null QName
    @Test
    public void testConstructorWithNullQName() {
        // create a parent NodePointer
        NodePointer parent = new NodePointer();
        // create a null QName
        QName name = null;
        // create a JDOMAttributeIterator object using the constructor
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        // check if the iterator object is null
        assertNull(iterator);
    }
    
    // test getNodePointer method when position = 0
    @Test
    public void testGetNodePointerWithZeroPosition() {
        // create a parent NodePointer
        NodePointer parent = new NodePointer();
        // create a valid QName
        QName name = new QName("test", "attribute");
        // create a JDOMAttributeIterator object using the constructor
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        // set position to 0
        iterator.setPosition(0);
        // call getNodePointer method
        NodePointer nodePointer = iterator.getNodePointer();
        // check if the returned nodePointer is null
        assertNull(nodePointer);
    }
    
    // test getNodePointer method when position is valid
    @Test
    public void testGetNodePointerWithValidPosition() {
        // create a parent NodePointer
        NodePointer parent = new NodePointer();
        // create a valid QName
        QName name = new QName("test", "attribute");
        // create a JDOMAttributeIterator object using the constructor
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        // set position to a valid value
        iterator.setPosition(1);
        // call getNodePointer method
        NodePointer nodePointer = iterator.getNodePointer();
        // check if the returned nodePointer is not null
        assertNotNull(nodePointer);
    }
    
    // test getPosition method
    @Test
    public void testGetPosition() {
        // create a parent NodePointer
        NodePointer parent = new NodePointer();
        // create a valid QName
        QName name = new QName("test", "attribute");
        // create a JDOMAttributeIterator object using the constructor
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        // set position to a valid value
        iterator.setPosition(3);
        // call getPosition method
        int position = iterator.getPosition();
        // check if the returned position is equal to the set position
        assertEquals(3, position);
    }
    
    // test setPosition method with valid position
    @Test
    public void testSetPositionWithValidPosition() {
        // create a parent NodePointer
        NodePointer parent = new NodePointer();
        // create a valid QName
        QName name = new QName("test", "attribute");
        // create a JDOMAttributeIterator object using the constructor
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        // call setPosition method with a valid value
        boolean isSet = iterator.setPosition(2);
        // check if the position is set correctly
        assertTrue(isSet);
    }
    
    // test setPosition method with invalid position
    @Test
    public void testSetPositionWithInvalidPosition() {
        // create a parent NodePointer
        NodePointer parent = new NodePointer();
        // create a valid QName
        QName name = new QName("test", "attribute");
        // create a JDOMAttributeIterator object using the constructor
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        // call setPosition method with an invalid value
        boolean isSet = iterator.setPosition(0);
        // check if the position is not set
        assertFalse(isSet);
    }
    
}