package org.apache.commons.jxpath.ri.model.jdom;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator() {
        Element element = new Element("test");
        element.setAttribute(new Attribute("attr1", "value1"));
        element.setAttribute(new Attribute("attr2", "value2"));
        
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(element), new QName("attr1"));
        
        assertNotNull(iterator);
    }

    @Test
    public void testGetNodePointer() {
        Element element = new Element("test");
        element.setAttribute(new Attribute("attr1", "value1"));
        element.setAttribute(new Attribute("attr2", "value2"));

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(element), new QName("attr1"));

        assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        Element element = new Element("test");
        element.setAttribute(new Attribute("attr1", "value1"));
        element.setAttribute(new Attribute("attr2", "value2"));

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(element), new QName("attr1"));

        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        Element element = new Element("test");
        element.setAttribute(new Attribute("attr1", "value1"));
        element.setAttribute(new Attribute("attr2", "value2"));

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(element), new QName("attr1"));

        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        assertNotNull(iterator.getNodePointer());
    }
}