package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_withEmptyElement() {
        NodePointer parent = new JDOMNodePointer(null, null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_withAttribute() {
        Element element = new Element("root");
        Attribute attribute = new Attribute("attr", "value", "http://example.com/ns");
        element.addAttribute(attribute);

        NodePointer parent = new JDOMNodePointer(null, null, element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("http://example.com/ns", "attr"));
        NodePointer attributePointer = iterator.getNodePointer();
        assertNotNull(attributePointer);
        assertNotNull(attributePointer.getNode());
        assertEquals(attribute, attributePointer.getNode());
    }

    @Test
    public void testJDOMAttributeIterator_withMultipleAttributes() {
        Element element = new Element("root");
        Attribute attribute1 = new Attribute("attr1", "value1", "http://example.com/ns1");
        Attribute attribute2 = new Attribute("attr2", "value2", "http://example.com/ns2");
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);

        NodePointer parent = new JDOMNodePointer(null, null, element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("http://example.com/ns1", "*"));
        NodePointer attributePointer1 = iterator.getNodePointer();
        assertNotNull(attributePointer1);
        assertNotNull(attributePointer1.getNode());
        assertEquals(attribute1, attributePointer1.getNode());

        NodePointer attributePointer2 = iterator.getNodePointer();
        assertNotNull(attributePointer2);
        assertNotNull(attributePointer2.getNode());
        assertEquals(attribute2, attributePointer2.getNode());

        iterator.setPosition(2); // out of bounds
        assertNull(iterator.getNodePointer());

        iterator.setPosition(1);
        NodePointer attributePointer3 = iterator.getNodePointer();
        assertNull(attributePointer3);
    }

    @Test
    public void testJDOMAttributeIterator_withEmptyNamespace() {
        Element element = new Element("root");
        Attribute attribute = new Attribute("attr", "value");
        element.addAttribute(attribute);

        NodePointer parent = new JDOMNodePointer(null, null, element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "attr"));
        NodePointer attributePointer = iterator.getNodePointer();
        assertNotNull(attributePointer);
        assertNotNull(attributePointer.getNode());
        assertEquals(attribute, attributePointer.getNode());
    }

    @Test
    public void testJDOMAttributeIterator_withNullParent() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, new QName("", ""));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_withNullName() {
        NodePointer parent = new JDOMNodePointer(null, null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
        assertNull(iterator.getNodePointer());
    }
}