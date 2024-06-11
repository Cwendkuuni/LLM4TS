package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    private JDOMAttributeIterator attributeIterator;
    private NodePointer parent;
    
    @Before
    public void setUp() {
        Element element = new Element("testElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");
        parent = new NodePointer(element);
        
        attributeIterator = new JDOMAttributeIterator(parent, new QName("*"));
    }
    
    @Test
    public void testGetNodePointer() {
        NodePointer pointer = attributeIterator.getNodePointer();
        Attribute attribute = (Attribute) parent.getNode().getAttributes().get(0);
        assertEquals(attribute, pointer.getNode());
    }
    
    @Test
    public void testGetPosition() {
        assertEquals(0, attributeIterator.getPosition());
    }
    
    @Test
    public void testSetPosition() {
        attributeIterator.setPosition(2);
        assertEquals(2, attributeIterator.getPosition());
    }
}