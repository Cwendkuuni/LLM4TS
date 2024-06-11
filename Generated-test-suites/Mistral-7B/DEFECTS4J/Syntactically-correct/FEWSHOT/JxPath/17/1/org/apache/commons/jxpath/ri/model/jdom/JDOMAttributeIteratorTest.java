package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmpty() {
        NodePointer parent = new JDOMNodePointer(null, null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
        assertNull(iterator.getNodePointer());
        assertEquals(-1, iterator.getPosition());
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
    }

    @Test
    public void testSingleAttribute() {
        Element element = new Element("root", "root");
        Attribute attribute = new Attribute("test", "testValue", "testNS");
        element.addAttribute(attribute);

        NodePointer parent = new JDOMNodePointer(null, null, element);
        JDOMAttributeIterator iterator = new JDOMAttribeIterator(parent, new QName("testNS", "test"));
        NodePointer attributePointer = iterator.getNodePointer();
        assertEquals("test", attributePointer.getNodeName().getLocalPart());
        assertEquals("testNS", attributePointer.getNodeName().getNamespaceURI());
        assertEquals("testValue", attributePointer.getNodeValue().getValue());

        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
    }

    @Test
    public void testMultipleAttributes() {
        Element element = new Element("root", "root");
        Attribute attribute1 = new Attribute("test1", "testValue1", "testNS1");
        Attribute attribute2 = new Attribute("test2", "testValue2", "testNS2");
        Attribute attribute3 = new Attribute("test3", "testValue3", "testNS3");
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);
        element.addAttribute(attribute3);

        NodePointer parent = new JDOMNodePointer(null, null, element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);

        NodePointer attributePointer1 = iterator.getNodePointer();
        assertEquals("test1", attributePointer1.getNodeName().getLocalPart());
        assertEquals("testNS1", attributePointer1.getNodeName().getNamespaceURI());
        assertEquals("testValue1", attributePointer1.getNodeValue().getValue());

        NodePointer attributePointer2 = iterator.getNodePointer();
        assertEquals("test2", attributePointer2.getNodeName().getLocalPart());
        assertEquals("testNS2", attributePointer2.getNodeName().getNamespaceURI());
        assertEquals("testValue2", attributePointer2.getNodeValue().getValue());

        assertEquals(2, iterator.getPosition());
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasPrevious());

        iterator.setPosition(0);
        NodePointer attributePointer3 = iterator.getNodePointer();
        assertEquals("test3", attributePointer3.getNodeName().getLocalPart());
        assertEquals("testNS3", attributePointer3.getNodeName().getNamespaceURI());
        assertEquals("testValue3", attributePointer3.getNodeValue().getValue());

        iterator.setPosition(3); // out of bounds
        assertNull(iterator.getNodePointer());
        assertEquals(-1, iterator.getPosition());
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
    }

    @Test
    public void testWildcardAttribute() {
        Element element = new Element("root", "root");
        Attribute attribute1 = new Attribute("test1", "testValue1", "testNS1");
        Attribute attribute2 = new Attribute("test2", "testValue2", "testNS2");
        Attribute attribute3 = new Attribute("test3", "testValue3", "testNS3");
        Attribute attribute4 = new Attribute("test4", "testValue4", "testNS4");
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);
        element.addAttribute(attribute3);
        element.addAttribute(attribute4);

        NodePointer parent = new JDOMNodePointer(null, null, element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        int count = 0;

        while (iterator.hasNext()) {
            NodePointer attributePointer = iterator.getNodePointer();
            ++count;
            iterator.next();
        }

        assertEquals(4, count);
    }

    @Test
    public void testGetNamespaceURI() {
        Element element = new Element("root", "root");
        Attribute attribute1 = new Attribute("test1", "testValue1", "testNS1");
        Attribute attribute2 = new Attribute("test2", "testValue2", null);
        Attribute attribute3 = new Attribute("test3", "testValue3", "testNS3");
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);
        element.addAttribute(attribute3);

        NodePointer parent = new JDOMNodePointer(null, null, element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);

        NodePointer attributePointer1 = iterator.getNodePointer();
        assertEquals("testNS1", attributePointer1.getNodeName().getNamespaceURI());

        NodePointer attributePointer2 = iterator.getNodePointer();
        assertNull(attributePointer2.getNodeName().getNamespaceURI());

        NodePointer attributePointer3 = iterator.getNodePointer();
        assertEquals("testNS3", attributePointer3.getNodeName().getNamespaceURI());
    }
}