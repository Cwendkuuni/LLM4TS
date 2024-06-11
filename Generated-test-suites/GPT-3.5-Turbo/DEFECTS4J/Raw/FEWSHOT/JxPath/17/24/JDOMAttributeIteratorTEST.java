package org.apache.commons.jxpath.ri.model.jdom;

import org.junit.Test;

import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

import org.jdom.Element;
import org.jdom.Attribute;
import org.jdom.Namespace;
import org.jdom.Document;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        QName name = new QName("name", "prefix");
        NodePointer parent = new NodePointer();
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parent, name);

        assertEquals(parent, attributeIterator.parent);
        assertEquals(0, attributeIterator.position);
        assertNull(attributeIterator.attributes);
    }

    @Test
    public void testSetPosition() {
        QName name = new QName("name", "prefix");
        NodePointer parent = new NodePointer();
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parent, name);

        assertFalse(attributeIterator.setPosition(0));
        assertFalse(attributeIterator.setPosition(100));

        assertNull(attributeIterator.attributes);

        attributeIterator.attributes = new ArrayList<>();
        attributeIterator.attributes.add(new Attribute("test", "value"));

        assertTrue(attributeIterator.setPosition(1));
    }

    @Test
    public void testGetPosition() {
        QName name = new QName("name", "prefix");
        NodePointer parent = new NodePointer();
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parent, name);

        assertEquals(0, attributeIterator.getPosition());

        attributeIterator.attributes = new ArrayList<>();
        attributeIterator.attributes.add(new Attribute("test", "value"));

        attributeIterator.setPosition(1);

        assertEquals(1, attributeIterator.getPosition());
    }

    @Test
    public void testGetNodePointer() {
        QName name = new QName("name", "prefix");
        NodePointer parent = new NodePointer();
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parent, name);

        assertNull(attributeIterator.getNodePointer());

        attributeIterator.attributes = new ArrayList<>();
        attributeIterator.attributes.add(new Attribute("test", "value"));

        attributeIterator.setPosition(1);

        Attribute attribute = new Attribute("test", "value");
        JDOMAttributePointer expectedPointer = new JDOMAttributePointer(parent, attribute);

        assertEquals(expectedPointer, attributeIterator.getNodePointer());
    }
}