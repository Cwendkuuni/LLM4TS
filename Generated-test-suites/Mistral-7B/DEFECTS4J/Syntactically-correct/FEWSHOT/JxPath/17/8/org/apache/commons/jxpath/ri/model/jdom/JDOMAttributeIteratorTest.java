package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator() {
        Element element = new Element("root");
        element.addAttribute(new Attribute("attr1", "value1"));
        element.addAttribute(new Attribute("attr2", "value2"));
        element.addAttribute(new Attribute("xml:lang", "en"));

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(element), new QName("*"));

        assertNotNull(iterator.getNodePointer());
        assertEquals("attr1", iterator.getNodePointer().getName().getLocalPart());
        assertEquals("value1", iterator.getNodePointer().getValue());
        assertEquals("attr2", iterator.getNodePointer().getName().getLocalPart());
        assertEquals("value2", iterator.getNodePointer().getValue());
        assertNotNull(iterator.getNodePointer().getNamespaceURI());
        assertEquals("xml", iterator.getNodePointer().getName().getPrefix());
        assertEquals("http://www.w3.org/XML/1998/namespace", iterator.getNodePointer().getNamespaceURI());

        iterator.next();
        assertEquals("xml:lang", iterator.getNodePointer().getName().getLocalPart());
        assertEquals("en", iterator.getNodePointer().getValue());

        iterator.next();
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorWithPrefix() {
        Element element = new Element("root");
        element.addAttribute(new Attribute("xml:lang", "en"));

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(element), new QName("xml", "*"));

        assertNotNull(iterator.getNodePointer());
        assertEquals("xml:lang", iterator.getNodePointer().getName().getLocalPart());
        assertEquals("en", iterator.getNodePointer().getValue());

        iterator.next();
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorWithNoAttributes() {
        Element element = new Element("root");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(element), new QName("*"));

        assertNull(iterator.getNodePointer());
    }
}