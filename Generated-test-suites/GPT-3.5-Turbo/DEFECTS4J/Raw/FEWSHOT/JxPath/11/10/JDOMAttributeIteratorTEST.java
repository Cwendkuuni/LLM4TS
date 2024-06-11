package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    
    private NodePointer parent;
    private List<Attribute> attributes;
    
    @Before
    public void setup() {
        parent = mock(NodePointer.class);
        Element element = mock(Element.class);
        Attribute attribute = mock(Attribute.class);
        attributes = new ArrayList<>();
        attributes.add(attribute);
        
        when(parent.getNode()).thenReturn(element);
        when(element.getAttribute(any(String.class), any(Namespace.class))).thenReturn(attribute);
        when(element.getAttributes()).thenReturn(attributes);
        
    }
    
    @Test
    public void testConstructor() {
        QName name = new QName("test");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertEquals(1, iterator.attributes.size());
    }
    
    @Test
    public void testGetNodePointer() {
        QName name = new QName("test");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        NodePointer nodePointer = iterator.getNodePointer();
        assertEquals(attribute, nodePointer.getNode());
    }
    
    @Test
    public void testGetPosition() {
        QName name = new QName("test");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        int position = iterator.getPosition();
        assertEquals(0, position);
    }
    
    @Test
    public void testSetPosition() {
        QName name = new QName("test");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        boolean result = iterator.setPosition(1);
        assertEquals(true, result);
    }
}