package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_emptyElement() {
        Element emptyElement = new Element("empty");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(emptyElement), new QName("", ""));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_singleAttribute() {
        Element element = new Element("element");
        Attribute attribute = new Attribute("attribute", "value", "http://example.com/");
        element.addContent(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("http://example.com/", "attribute"));
        NodePointer attributePointer = iterator.getNodePointer();
        assertEquals("value", attributePointer.getNodeValue());
        assertEquals("http://example.com/attribute", attributePointer.getNodeName().getLocalPart());
    }

    @Test
    public void testJDOMAttributeIterator_multipleAttributes() {
        Element element = new Element("element");
        Attribute attribute1 = new Attribute("attribute1", "value1", "http://example.com/");
        Attribute attribute2 = new Attribute("attribute2", "value2", "http://example.com/");
        Attribute attribute3 = new Attribute("attribute3", null, null);
        element.addContent(attribute1);
        element.addContent(attribute2);
        element.addContent(attribute3);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("http://example.com/", "*"));

        NodePointer attributePointer1 = iterator.getNodePointer();
        assertEquals("value1", attributePointer1.getNodeValue());
        assertEquals("http://example.com/attribute1", attributePointer1.getNodeName().getLocalPart());

        NodePointer attributePointer2 = iterator.getNodePointer();
        assertEquals("value2", attributePointer2.getNodeValue());
        assertEquals("http://example.com/attribute2", attributePointer2.getNodeName().getLocalPart());

        NodePointer attributePointer3 = iterator.getNodePointer();
        assertNull(attributePointer3);
    }

    @Test
    public void testJDOMAttributeIterator_noAttributes() {
        Element element = new Element("element");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", ""));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_wrongNamespace() {
        Element element = new Element("element");
        Attribute attribute = new Attribute("attribute", "value", "http://example.com/");
        element.addContent(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("http://other.com/", "attribute"));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_position() {
        Element element = new Element("element");
        Attribute attribute1 = new Attribute("attribute1", "value1", "http://example.com/");
        Attribute attribute2 = new Attribute("attribute2", "value2", "http://example.com/");
        Attribute attribute3 = new Attribute("attribute3", null, null);
        element.addContent(attribute1);
        element.addContent(attribute2);
        element.addContent(attribute3);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("http://example.com/", "*"));

        NodePointer attributePointer1 = iterator.getNodePointer();
        iterator.setPosition(2);
        NodePointer attributePointer2 = iterator.getNodePointer();
        assertEquals(attributePointer2, attributePointer3);
    }
}