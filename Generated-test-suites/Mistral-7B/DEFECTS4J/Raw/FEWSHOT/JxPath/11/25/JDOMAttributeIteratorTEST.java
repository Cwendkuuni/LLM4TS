package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIteratorWithSingleAttribute() {
        Element element = new Element("root");
        Attribute attribute = new Attribute("attrName", "attrValue", "namespaceURI");
        element.addAttribute(attribute);

        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attrName", "namespaceURI"));

        assertEquals(1, iterator.getPosition());
        NodePointer attributePointer = iterator.getNodePointer();
        assertEquals("attrValue", attributePointer.getValue().getValue());
        assertEquals("namespaceURI", attributePointer.getValue().getNamespaceURI());
        assertTrue(attributePointer instanceof JDOMAttributePointer);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIteratorWithMultipleAttributes() {
        Element element = new Element("root");
        Attribute attribute1 = new Attribute("attrName1", "attrValue1", "namespaceURI1");
        Attribute attribute2 = new Attribute("attrName2", "attrValue2", "namespaceURI2");
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);

        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*", ""));

        assertEquals(2, iterator.getPosition());
        NodePointer attributePointer1 = iterator.getNodePointer();
        assertEquals("attrValue1", attributePointer1.getValue().getValue());
        assertEquals("namespaceURI1", attributePointer1.getValue().getNamespaceURI());
        assertTrue(attributePointer1 instanceof JDOMAttributePointer);

        iterator.next();

        NodePointer attributePointer2 = iterator.getNodePointer();
        assertEquals("attrValue2", attributePointer2.getValue().getValue());
        assertEquals("namespaceURI2", attributePointer2.getValue().getNamespaceURI());
        assertTrue(attributePointer2 instanceof JDOMAttributePointer);

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIteratorWithNoAttributes() {
        Element element = new Element("root");

        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attrName", "namespaceURI"));

        assertNull(iterator.getNodePointer());
    }
}