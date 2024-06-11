package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import org.junit.Test;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_EmptyList() {
        NodePointer parent = new JDOMPointer(new Element("root"));
        QName name = new QName("", "");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_SingleAttribute() {
        NodePointer parent = new JDOMPointer(new Element("parent", new Namespace("ns", "http://test.com/")));
        parent.getNode().setAttribute("name", "value");
        QName name = new QName("ns", "name");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Attribute attribute = iterator.getNodePointer().getNode();
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testJDOMAttributeIterator_MultipleAttributes() {
        NodePointer parent = new JDOMPointer(new Element("parent", new Namespace("ns", "http://test.com/")));
        parent.getNode().addAttribute("name1", "value1");
        parent.getNode().addAttribute("name2", "value2");
        QName name = new QName("ns", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Attribute attribute1 = iterator.getNodePointer().getNode();
        assertEquals("name1", attribute1.getName());
        assertEquals("value1", attribute1.getValue());

        Attribute attribute2 = iterator.getNodePointer().getNode();
        assertEquals("name2", attribute2.getName());
        assertEquals("value2", attribute2.getValue());

        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_NoMatchingAttributes() {
        NodePointer parent = new JDOMPointer(new Element("parent", new Namespace("ns", "http://test.com/")));
        QName name = new QName("ns2", "name");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_Position() {
        NodePointer parent = new JDOMPointer(new Element("parent", new Namespace("ns", "http://test.com/")));
        parent.getNode().addAttribute("name1", "value1");
        parent.getNode().addAttribute("name2", "value2");
        QName name = new QName("ns", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Attribute attribute1 = iterator.getNodePointer().getNode();
        assertEquals("name1", attribute1.getName());
        assertEquals("value1", attribute1.getValue());

        iterator.next();

        Attribute attribute2 = iterator.getNodePointer().getNode();
        assertEquals("name2", attribute2.getName());
        assertEquals("value2", attribute2.getValue());

        assertNull(iterator.getNodePointer());
    }
}