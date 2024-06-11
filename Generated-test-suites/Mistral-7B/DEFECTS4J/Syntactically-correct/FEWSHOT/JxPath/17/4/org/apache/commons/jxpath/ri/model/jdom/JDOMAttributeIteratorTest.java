package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_SingleAttribute() {
        Element root = new Element("root");
        root.addAttribute("attr1", "val1");

        NodePointer parent = new JDOMNodePointer(root);
        QName name = new QName("*", null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        assertNotNull(iterator.getNodePointer());
        assertEquals("attr1", iterator.getNodePointer().getQName().getName());
        assertEquals("val1", iterator.getNodePointer().getStringValue());
        assertEquals(1, iterator.getPosition());

        assertNull(iterator.getNodePointer());
        assertEquals(1, iterator.getPosition());

        iterator.next();
        assertNull(iterator.getNodePointer());
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testJDOMAttributeIterator_MultipleAttributes() {
        Element root = new Element("root");
        root.addAttribute("attr1", "val1");
        root.addAttribute("attr2", "val2");
        root.addAttribute("attr3", "val3");

        NodePointer parent = new JDOMNodePointer(root);
        QName name = new QName("*", null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        assertNotNull(iterator.getNodePointer());
        assertEquals("attr1", iterator.getNodePointer().getQName().getName());
        assertEquals("val1", iterator.getNodePointer().getStringValue());
        assertEquals(1, iterator.getPosition());

        iterator.next();
        assertEquals("attr2", iterator.getNodePointer().getQName().getName());
        assertEquals("val2", iterator.getNodePointer().getStringValue());
        assertEquals(2, iterator.getPosition());

        iterator.next();
        assertEquals("attr3", iterator.getNodePointer().getQName().getName());
        assertEquals("val3", iterator.getNodePointer().getStringValue());
        assertEquals(3, iterator.getPosition());

        iterator.next();
        assertNull(iterator.getNodePointer());
        assertEquals(4, iterator.getPosition());
    }

    @Test
    public void testJDOMAttributeIterator_NamespaceSpecified() {
        Element root = new Element("root");
        root.addNamespaceDeclaration("xml", "http://www.w3.org/XML/1998/namespace");
        Element child = new Element("child", "ns:child");
        child.addAttribute("attr1", "val1");
        root.addContent(child);

        NodePointer parent = new JDOMNodePointer(root);
        QName name = new QName("ns:child/@attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        assertNotNull(iterator.getNodePointer());
        assertEquals("attr1", iterator.getNodePointer().getQName().getName());
        assertEquals("val1", iterator.getNodePointer().getStringValue());
        assertEquals(1, iterator.getPosition());

        assertNull(iterator.getNodePointer());
        assertEquals(1, iterator.getPosition());

        iterator.next();
        assertNull(iterator.getNodePointer());
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testJDOMAttributeIterator_EmptyElement() {
        Element root = new Element("root");
        NodePointer parent = new JDOMNodePointer(root);
        QName name = new QName("*", null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testJDOMAttributeIterator_NullElement() {
        NodePointer parent = null;
        QName name = new QName("*", null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIterator_InvalidPrefix() {
        Element root = new Element("root");
        NodePointer parent = new JDOMNodePointer(root);
        QName name = new QName("xml:child/@attr1", "invalid");
        new JDOMAttributeIterator(parent, name);
    }
}