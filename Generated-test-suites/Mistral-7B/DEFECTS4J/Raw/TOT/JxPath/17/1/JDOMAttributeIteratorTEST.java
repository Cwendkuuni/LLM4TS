package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.JDOMException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link JDOMAttributeIterator}.
 */
public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyIterator() throws Exception {
        NodePointer parent = new NodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);

        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testIteratorWithOneAttribute() throws Exception {
        Namespace xmlNamespace = Namespace.getNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        String xmlNsPrefix = xmlNamespace.getPrefix();
        Element root = new Element("root").addNamespaceDeclaration(xmlNamespace);
        root.addAttribute(new Attribute(xmlNsPrefix, "xml:version", "1.0"));
        NodePointer parent = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(xmlNsPrefix, "xml:version"));

        Attribute attribute = iterator.next();
        assertEquals("1.0", attribute.getValue());
        assertTrue(iterator.hasNext());

        assertNull(iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithMultipleAttributes() throws Exception {
        Namespace xmlNamespace = Namespace.getNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        String xmlNsPrefix = xmlNamespace.getPrefix();
        Element root = new Element("root");
        root.addNamespaceDeclaration(xmlNamespace);
        root.addAttribute(new Attribute(xmlNsPrefix, "xml:version", "1.0"));
        root.addAttribute(new Attribute("attr1", "val1"));
        root.addAttribute(new Attribute("attr2", "val2"));
        NodePointer parent = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(null, "*"));

        Attribute firstAttribute = iterator.next();
        assertEquals("xml:version", firstAttribute.getName().getLocalPart());
        assertEquals("1.0", firstAttribute.getValue());

        Attribute secondAttribute = iterator.next();
        assertEquals("attr1", secondAttribute.getName().getLocalPart());
        assertEquals("val1", secondAttribute.getValue());

        Attribute thirdAttribute = iterator.next();
        assertEquals("attr2", thirdAttribute.getName().getLocalPart());
        assertEquals("val2", thirdAttribute.getValue());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithEmptyNamespace() throws Exception {
        Element root = new Element("root");
        root.addAttribute(new Attribute("attr1", "val1"));
        root.addAttribute(new Attribute("xml:version", "1.0"));
        NodePointer parent = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(null, "*"));

        Attribute firstAttribute = iterator.next();
        assertEquals("attr1", firstAttribute.getName().getLocalPart());
        assertEquals("val1", firstAttribute.getValue());

        Attribute secondAttribute = iterator.next();
        assertEquals("xml:version", secondAttribute.getName().getLocalPart());
        assertEquals("1.0", secondAttribute.getValue());

        assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalStateException.class)
    public void testIteratorWithInvalidPrefix() throws Exception {
        Namespace xmlNamespace = Namespace.getNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        String xmlNsPrefix = xmlNamespace.getPrefix();
        String invalidPrefix = "xxml";
        Element root = new Element("root");
        root.addNamespaceDeclaration(xmlNamespace);
        root.addAttribute(new Attribute(xmlNsPrefix, "xml:version", "1.0"));
        NodePointer parent = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(invalidPrefix, "xml:version"));
        iterator.next();
    }

    @Test(expected = IllegalStateException.class)
    public void testIteratorWithNullAttributeList() throws Exception {
        Namespace xmlNamespace = Namespace.getNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        String xmlNsPrefix = xmlNamespace.getPrefix();
        Element root = new Element("root");
        NodePointer parent = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(xmlNsPrefix, "xml:version"));
    }

    @Test(expected = JDOMException.class)
    public void testIteratorWithNullParentNode() throws Exception {
        NodePointer parent = null;
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
    }
}