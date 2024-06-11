package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    
    @Test
    public void testConstructor() {
        NodePointer parent = mock(NodePointer.class);
        QName name = mock(QName.class);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        assertEquals(parent, iterator.parent);
        assertNull(iterator.attributes);
        assertEquals(0, iterator.position);
    }
    
    @Test
    public void testSetPosition() {
        NodePointer parent = mock(NodePointer.class);
        QName name = mock(QName.class);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(-1));
        assertFalse(iterator.setPosition(2));
        assertEquals(0, iterator.position);
        
        iterator.attributes = new ArrayList();
        iterator.attributes.add(mock(Attribute.class));
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.position);
    }
    
    @Test
    public void testGetPosition() {
        NodePointer parent = mock(NodePointer.class);
        QName name = mock(QName.class);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        assertEquals(0, iterator.getPosition());
        iterator.position = 2;
        assertEquals(2, iterator.getPosition());
    }
    
    @Test
    public void testGetNodePointer() {
        NodePointer parent = mock(NodePointer.class);
        QName name = mock(QName.class);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        assertNull(iterator.getNodePointer());
        
        Attribute attribute = mock(Attribute.class);
        iterator.position = 1;
        iterator.attributes = new ArrayList();
        iterator.attributes.add(attribute);
        assertNotNull(iterator.getNodePointer());
        assertEquals(JDOMAttributePointer.class, iterator.getNodePointer().getClass());
    }
}