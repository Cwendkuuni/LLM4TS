package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for JDOMAttributeIterator.
 */
public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructor_NullParent() {
        Assert.assertNull(new JDOMAttributeIterator(null, new QName("", "")));
    }

    @Test
    public void testConstructor_EmptyElement() {
        Element element = new Element("root");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", ""));
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testConstructor_EmptyAttributeList() {
        Element element = new Element("root");
        JDOMAttributeIterator iterator = new JDOMAttribeIterator(new NodePointer(element), new QName("", ""));
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testConstructor_SimpleAttribute() {
        Element element = new Element("root", "root");
        element.setAttribute("attr", new Attribute("attr", "value"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", "attr"));
        Assert.assertEquals(1, iterator.getPosition());
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("attr", nodePointer.getName());
        Assert.assertEquals("value", nodePointer.getValue());
    }

    @Test
    public void testConstructor_MultipleAttributes() {
        Element element = new Element("root", "root");
        element.setAttribute("attr1", new Attribute("attr1", "value1"));
        element.setAttribute("attr2", new Attribute("attr2", "value2"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", ""));
        Assert.assertEquals(2, iterator.getPosition());
        NodePointer nodePointer1 = iterator.getNodePointer();
        Assert.assertEquals("attr1", nodePointer1.getName());
        Assert.assertEquals("value1", nodePointer1.getValue());

        Assert.assertEquals(1, iterator.getPosition());
        NodePointer nodePointer2 = iterator.getNodePointer();
        Assert.assertEquals("attr2", nodePointer2.getName());
        Assert.assertEquals("value2", nodePointer2.getValue());
    }

    @Test
    public void testConstructor_NamespaceAttribute() {
        Element element = new Element("root", "root");
        element.setNamespace("xml");
        element.setAttribute("xml:attr", new Attribute("xml:attr", "value"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("xml", "attr"));
        Assert.assertEquals(1, iterator.getPosition());
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertEquals("xml:attr", nodePointer.getName());
        Assert.assertEquals("value", nodePointer.getValue());
    }

    @Test
    public void testConstructor_EmptyPrefix() {
        Element element = new Element("root", "root");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", "*"));
        List attributes = iterator.getAttributes();
        Assert.assertEquals(1, attributes.size());
        Attribute attribute = (Attribute) attributes.get(0);
        Assert.assertNull(attribute.getNamespace());
    }

    @Test
    public void testConstructor_NonEmptyPrefix() {
        Element element = new Element("root", "root");
        element.setNamespace("xml");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("xml", "*"));
        List attributes = iterator.getAttributes();
        Assert.assertEquals(1, attributes.size());
        Attribute attribute = (Attribute) attributes.get(0);
        Assert.assertEquals("xml", attribute.getNamespace().getURI());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNodePointer_NoAttributes() {
        Element element = new Element("root", "root");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", ""));
        iterator.setPosition(1);
        iterator.getNodePointer();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNodePointer_OutOfBounds() {
        Element element = new Element("root", "root");
        element.setAttribute("attr1", new Attribute("attr1", "value1"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", ""));
        iterator.setPosition(3);
        iterator.getNodePointer();
    }

    @Test
    public void testGetAttributes() {
        Element element = new Element("root", "root");
        element.setAttribute("attr1", new Attribute("attr1", "value1"));
        element.setAttribute("attr2", new Attribute("attr2", "value2"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", ""));
        List attributes = iterator.getAttributes();
        Assert.assertEquals(2, attributes.size());
        Attribute attribute1 = (Attribute) attributes.get(0);
        Assert.assertEquals("attr1", attribute1.getName());
        Assert.assertEquals("value1", attribute1.getValue());

        Attribute attribute2 = (Attribute) attributes.get(1);
        Assert.assertEquals("attr2", attribute2.getName());
        Assert.assertEquals("value2", attribute2.getValue());
    }

    @Test
    public void testGetAttribute() {
        Element element = new Element("root", "root");
        element.setAttribute("attr1", new Attribute("attr1", "value1"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", "attr1"));
        Attribute attribute = iterator.getNodePointer().getAttribute();
        Assert.assertEquals("attr1", attribute.getName());
        Assert.assertEquals("value1", attribute.getValue());
    }

    @Test
    public void testGetName() {
        Element element = new Element("root", "root");
        element.setAttribute("attr1", new Attribute("attr1", "value1"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", ""));
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertEquals("attr1", nodePointer.getName());
    }

    @Test
    public void testGetValue() {
        Element element = new Element("root", "root");
        element.setAttribute("attr1", new Attribute("attr1", "value1"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", "attr1"));
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertEquals("value1", nodePointer.getValue());
    }

    @Test
    public void testGetNamespaceURI() {
        Element element = new Element("root", "root");
        element.setAttribute("xml:attr1", new Attribute("xml:attr1", "value1"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("xml", "attr1"));
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertEquals("xml", nodePointer.getNamespaceURI());
    }

    @Test
    public void testHasAttribute() {
        Element element = new Element("root", "root");
        element.setAttribute("attr1", new Attribute("attr1", "value1"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", "attr1"));
        boolean hasAttribute = iterator.hasNext();
        Assert.assertTrue(hasAttribute);

        hasAttribute = iterator.hasNext();
        Assert.assertFalse(hasAttribute);

        hasAttribute = iterator.hasNext();
        Assert.assertFalse(hasAttribute);
    }

    @Test
    public void testHasNext() {
        Element element = new Element("root", "root");
        element.setAttribute("attr1", new Attribute("attr1", "value1"));
        element.setAttribute("attr2", new Attribute("attr2", "value2"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", ""));
        boolean hasNext = iterator.hasNext();
        Assert.assertTrue(hasNext);

        hasNext = iterator.hasNext();
        Assert.assertTrue(hasNext);

        iterator.next();
        hasNext = iterator.hasNext();
        Assert.assertFalse(hasNext);
    }

    @Test
    public void testNext() {
        Element element = new Element("root", "root");
        element.setAttribute("attr1", new Attribute("attr1", "value1"));
        element.setAttribute("attr2", new Attribute("attr2", "value2"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", ""));
        NodePointer nodePointer1 = iterator.next();
        Assert.assertEquals("attr1", nodePointer1.getName());
        Assert.assertEquals("value1", nodePointer1.getValue());

        NodePointer nodePointer2 = iterator.next();
        Assert.assertEquals("attr2", nodePointer2.getName());
        Assert.assertEquals("value2", nodePointer2.getValue());

        iterator.next();
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testReset() {
        Element element = new Element("root", "root");
        element.setAttribute("attr1", new Attribute("attr1", "value1"));
        element.setAttribute("attr2", new Attribute("attr2", "value2"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", ""));
        iterator.next();
        iterator.next();
        iterator.reset();
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertEquals("attr1", nodePointer.getName());
        Assert.assertEquals("value1", nodePointer.getValue());
    }
}