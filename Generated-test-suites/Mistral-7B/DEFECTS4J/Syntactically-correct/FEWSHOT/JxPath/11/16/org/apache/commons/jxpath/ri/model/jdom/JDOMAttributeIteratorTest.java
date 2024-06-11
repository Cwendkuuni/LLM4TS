package org.apache.commons.cli.jxpath.ri.model.jdom;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {
    @Test
    public void testJDOMAttributeIterator_whenGivenElementWithOneAttribute_thenReturnAttributeIterator() {
        Element element = new Element("root");
        element.addAttribute(new Attribute("attr1", "value1"));

        NodePointer parent = new NodePointer(element);
        NodeIterator iterator = new JDOMAttributeIterator(parent, new QName("*:attr1"));

        assertEquals(1, iterator.getPosition());
        NodePointer attributePointer = iterator.getNodePointer();
        assertTrue(attributePointer instanceof JDOMAttributePointer);
        Attribute attribute = (Attribute) attributePointer.getNode();
        assertEquals("attr1", attribute.getName().getLocalPart());
        assertEquals("value1", attribute.getValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIterator_whenGivenElementWithMultipleAttributes_thenReturnAttributeIterator() {
        Element element = new Element("root");
        element.addAttribute(new Attribute("attr1", "value1"));
        element.addAttribute(new Attribute("attr2", "value2"));

        NodePointer parent = new NodePointer(element);
        NodeIterator iterator = new JDOMAttributeIterator(parent, new QName("*:attr2"));

        assertEquals(1, iterator.getPosition());
        NodePointer attributePointer = iterator.getNodePointer();
        assertTrue(attributePointer instanceof JDOMAttributePointer);
        Attribute attribute = (Attribute) attributePointer.getNode();
        assertEquals("attr2", attribute.getName().getLocalPart());
        assertEquals("value2", attribute.getValue());
        assertTrue(iterator.hasNext());

        iterator.next();

        attributePointer = iterator.getNodePointer();
        assertTrue(attributePointer instanceof JDOMAttributePointer);
        attribute = (Attribute) attributePointer.getNode();
        assertEquals("attr1", attribute.getName().getLocalPart());
        assertEquals("value1", attribute.getValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIterator_whenGivenElementWithNoAttributes_thenReturnEmptyIterator() {
        Element element = new Element("root");

        NodePointer parent = new NodePointer(element);
        NodeIterator iterator = new JDOMAttributeIterator(parent, new QName("*:attr1"));

        assertFalse(iterator.hasNext());
    }
}