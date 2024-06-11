package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructorWithEmptyList() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, null);
        assertNull(iterator.getAttributes());
    }

    @Test
    public void testConstructorWithSingleAttribute() {
        String namespaceURI = "http://example.com";
        String prefix = "ex";
        String localName = "attr";
        Element element = new Element("root", null);
        Attribute attribute = new Attribute("attr", "value", null, namespaceURI);
        element.setAttribute(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName(prefix, localName));
        List<Attribute> attributes = iterator.getAttributes();
        assertEquals(1, attributes.size());
        assertEquals(attribute, attributes.get(0));
    }

    @Test
    public void testConstructorWithMultipleAttributes() {
        String namespaceURI = "http://example.com";
        String prefix = "ex";
        String localName1 = "attr1";
        String localName2 = "attr2";
        Element element = new Element("root", null);
        Attribute attribute1 = new Attribute(localName1, "value1", null, namespaceURI);
        Attribute attribute2 = new Attribute(localName2, "value2", null, namespaceURI);
        element.setAttribute(attribute1);
        element.setAttribute(attribute2);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName(prefix, "*"));
        List<Attribute> attributes = iterator.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals(attribute1, attributes.get(0));
        assertEquals(attribute2, attributes.get(1));
    }

    @Test
    public void testConstructorWithNoMatchingAttributes() {
        String namespaceURI = "http://example.com";
        String prefix = "ex";
        String localName = "attr";
        Element element = new Element("root", null);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName(prefix, localName));
        List<Attribute> attributes = iterator.getAttributes();
        assertSame(Collections.emptyList(), attributes);
    }

    @Test
    public void testGetNodePointerWithFirstAttribute() {
        String namespaceURI = "http://example.com";
        String prefix = "ex";
        String localName = "attr";
        Element element = new Element("root", null);
        Attribute attribute = new Attribute(localName, "value", null, namespaceURI);
        element.setAttribute(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName(prefix, localName));
        NodePointer nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals(attribute, nodePointer.getNode());
    }

    @Test
    public void testGetNodePointerWithMiddleAttribute() {
        String namespaceURI = "http://example.com";
        String prefix = "ex";
        String localName1 = "attr1";
        String localName2 = "attr2";
        Element element = new Element("root", null);
        Attribute attribute1 = new Attribute(localName1, "value1", null, namespaceURI);
        Attribute attribute2 = new Attribute(localName2, "value2", null, namespaceURI);
        element.setAttribute(attribute1);
        element.setAttribute(attribute2);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName(prefix, "*"));
        iterator.setPosition(1);
        NodePointer nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals(attribute2, nodePointer.getNode());
    }

    @Test
    public void testGetNodePointerAfterEndPosition() {
        String namespaceURI = "http://example.com";
        String prefix = "ex";
        String localName = "attr";
        Element element = new Element("root", null);
        Attribute attribute = new Attribute(localName, "value", null, namespaceURI);
        element.setAttribute(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName(prefix, localName));
        iterator.setPosition(2);
        NodePointer nodePointer = iterator.getNodePointer();
        assertNull(nodePointer);
    }

    @Test
    public void testGetPositionWithFirstAttribute() {
        String namespaceURI = "http://example.com";
        String prefix = "ex";
        String localName = "attr";
        Element element = new Element("root", null);
        Attribute attribute = new Attribute(localName, "value", null, namespaceURI);
        element.setAttribute(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName(prefix, localName));
        int position = iterator.getPosition();
        assertEquals(1, position);
    }

    @Test
    public void testSetPositionWithFirstAttribute() {
        String namespaceURI = "http://example.com";
        String prefix = "ex";
        String localName = "attr";
        Element element = new Element("root", null);
        Attribute attribute = new Attribute(localName, "value", null, namespaceURI);
        element.setAttribute(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName(prefix, localName));
        iterator.setPosition(1);
        assertFalse(iterator.getNodePointer() instanceof JDOMAttributePointer);
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPositionWithMiddleAttribute() {
        String namespaceURI = "http://example.com";
        String prefix = "ex";
        String localName1 = "attr1";
        String localName2 = "attr2";
        Element element = new Element("root", null);
        Attribute attribute1 = new Attribute(localName1, "value1", null, namespaceURI);
        Attribute attribute2 = new Attribute(localName2, "value2", null, namespaceURI);
        element.setAttribute(attribute1);
        element.setAttribute(attribute2);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName(prefix, "*"));
        iterator.setPosition(2);
        assertTrue(iterator.getNodePointer() instanceof JDOMAttributePointer);
        assertEquals(attribute2, iterator.getNodePointer().getNode());
    }

    @Test
    public void testSetPositionWithLastAttribute() {
        String namespaceURI = "http://example.com";
        String prefix = "ex";
        String localName = "attr";
        Element element = new Element("root", null);
        Attribute attribute1 = new Attribute(localName, "value1", null, namespaceURI);
        Attribute attribute2 = new Attribute(localName, "value2", null, namespaceURI);
        element.setAttribute(attribute1);
        element.setAttribute(attribute2);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName(prefix, "*"));
        iterator.setPosition(3);
        assertNull(iterator.getNodePointer());
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPositionWithInvalidPosition() {
        String namespaceURI = "http://example.com";
        String prefix = "ex";
        String localName = "attr";
        Element element = new Element("root", null);
        Attribute attribute = new Attribute(localName, "value", null, namespaceURI);
        element.setAttribute(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName(prefix, localName));
        iterator.setPosition(0);
        assertFalse(iterator.getNodePointer() instanceof JDOMAttributePointer);
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPositionWithPositionGreaterThanSize() {
        String namespaceURI = "http://example.com";
        String prefix = "ex";
        String localName = "attr";
        Element element = new Element("root", null);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName(prefix, localName));
        iterator.setPosition(3);
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPositionWithFirstAttributeAfterReset() {
        String namespaceURI = "http://example.com";
        String prefix = "ex";
        String localName = "attr";
        Element element = new Element("root", null);
        Attribute attribute = new Attribute(localName, "value", null, namespaceURI);
        element.setAttribute(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName(prefix, localName));
        iterator.setPosition(1);
        assertFalse(iterator.getNodePointer() instanceof JDOMAttributePointer);
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(1);
        assertTrue(iterator.getNodePointer() instanceof JDOMAttributePointer);
        assertEquals(attribute, iterator.getNodePointer().getNode());
    }

    @Test
    public void testSetPositionWithMiddleAttributeAfterReset() {
        String namespaceURI = "http://example.com";
        String prefix = "ex";
        String localName1 = "attr1";
        String localName2 = "attr2";
        Element element = new Element("root", null);
        Attribute attribute1 = new Attribute(localName1, "value1", null, namespaceURI);
        Attribute attribute2 = new Attribute(localName2, "value2", null, namespaceURI);
        element.setAttribute(attribute1);
        element.setAttribute(attribute2);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName(prefix, "*"));
        iterator.setPosition(2);
        assertNull(iterator.getNodePointer());
        assertEquals(2, iterator.getPosition());
        iterator.setPosition(1);
        assertTrue(iterator.getNodePointer() instanceof JDOMAttributePointer);
        assertEquals(attribute2, iterator.getNodePointer().getNode());
    }

}