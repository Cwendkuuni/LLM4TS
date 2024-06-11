package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIteratorEmpty() {
        NodePointer parent = new JDOMPointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testJDOMAttributeIteratorSingleAttribute() {
        QName name = new QName("test", "xmlns:test", "http://test.com/");
        Element element = new Element("root");
        element.addAttribute(new Attribute("test", "value", "http://test.com/"));
        NodePointer parent = new JDOMPointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertTrue(iterator.hasNext());
        Attribute attribute = (Attribute) iterator.next();
        assertEquals("test", attribute.getName().getLocalPart());
        assertEquals("value", attribute.getValue());
        assertEquals("http://test.com/", attribute.getNamespaceURI());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIteratorMultipleAttributes() {
        QName name = new QName("test", null, null);
        Element element = new Element("root");
        element.addAttribute(new Attribute("test1", "value1"));
        element.addAttribute(new Attribute("test2", "value2"));
        NodePointer parent = new JDOMPointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertTrue(iterator.hasNext());
        Attribute attribute1 = (Attribute) iterator.next();
        assertEquals("test1", attribute1.getName().getLocalPart());
        assertEquals("value1", attribute1.getValue());
        assertEquals(Namespace.NO_NAMESPACE, attribute1.getNamespace());
        assertTrue(iterator.hasNext());
        Attribute attribute2 = (Attribute) iterator.next();
        assertEquals("test2", attribute2.getName().getLocalPart());
        assertEquals("value2", attribute2.getValue());
        assertEquals(Namespace.NO_NAMESPACE, attribute2.getNamespace());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIteratorNamespace() {
        QName name = new QName("test", "xmlns:test", "http://test.com/");
        Element element = new Element("root");
        Attribute attribute = new Attribute("test", "value", "http://test.com/");
        attribute.setNamespace(Namespace.getNamespace("xml", "http://www.w3.org/XML/1998/namespace"));
        element.addAttribute(attribute);
        NodePointer parent = new JDOMPointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testJDOMAttributeIteratorWildcard() {
        QName name = new QName("test", null, null);
        Element element = new Element("root");
        element.addAttribute(new Attribute("test1", "value1"));
        element.addAttribute(new Attribute("test2", "value2"));
        NodePointer parent = new JDOMPointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertTrue(iterator.hasNext());
        Attribute attribute1 = (Attribute) iterator.next();
        assertEquals("test1", attribute1.getName().getLocalPart());
        assertEquals("value1", attribute1.getValue());
        assertEquals(Namespace.NO_NAMESPACE, attribute1.getNamespace());
        assertTrue(iterator.hasNext());
        Attribute attribute2 = (Attribute) iterator.next();
        assertEquals("test2", attribute2.getName().getLocalPart());
        assertEquals("value2", attribute2.getValue());
        assertEquals(Namespace.NO_NAMESPACE, attribute2.getNamespace());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIteratorSetPosition() {
        QName name = new QName("test", null, null);
        Element element = new Element("root");
        element.addAttribute(new Attribute("test1", "value1"));
        element.addAttribute(new Attribute("test2", "value2"));
        NodePointer parent = new JDOMPointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        iterator.next();
        iterator.setPosition(2);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
        iterator.setPosition(1);
        assertTrue(iterator.hasNext());
        Attribute attribute = (Attribute) iterator.next();
        assertEquals("test2", attribute.getName().getLocalPart());
        assertEquals("value2", attribute.getValue());
        assertEquals(Namespace.NO_NAMESPACE, attribute.getNamespace());
        assertFalse(iterator.hasNext());
    }
}