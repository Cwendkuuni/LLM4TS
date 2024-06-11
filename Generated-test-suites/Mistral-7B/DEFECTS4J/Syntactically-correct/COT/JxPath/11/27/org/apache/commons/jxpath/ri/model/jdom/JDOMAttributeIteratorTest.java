package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructorWithQNameAndNamespace() {
        Element root = new Element("root");
        root.addAttribute(new Attribute("attr1", "value1", "xml"));
        root.addAttribute(new Attribute("attr2", "value2"));
        NodePointer parentPointer = new JDOMElementPointer(root);
        QName attrName = new QName("xml", "attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, attrName);

        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertEquals("attr1", iterator.getNodePointer().getName().getName());
        Assert.assertEquals("xml", iterator.getNodePointer().getName().getPrefix());
        Assert.assertEquals("value1", iterator.getNodePointer().getValue());

        iterator.next();
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testConstructorWithQNameAndNoNamespace() {
        Element root = new Element("root");
        root.addAttribute(new Attribute("attr1", "value1"));
        NodePointer parentPointer = new JDOMElementPointer(root);
        QName attrName = new QName("attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, attrName);

        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertEquals("attr1", iterator.getNodePointer().getName().getName());
        Assert.assertNull(iterator.getNodePointer().getName().getPrefix());
        Assert.assertEquals("value1", iterator.getNodePointer().getValue());

        iterator.next();
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testConstructorWithQNameAndElementNamespace() {
        Element root = new Element("root");
        root.addNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        root.addAttribute(new Attribute("xml:attr1", "value1", root.getNamespace("xml")));
        NodePointer parentPointer = new JDOMElementPointer(root);
        QName attrName = new QName("xml", "attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, attrName);

        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertEquals("xml:attr1", iterator.getNodePointer().getName().getLocalPart());
        Assert.assertEquals("xml", iterator.getNodePointer().getName().getPrefix());
        Assert.assertEquals("value1", iterator.getNodePointer().getValue());

        iterator.next();
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullParentPointer() {
        QName attrName = new QName("attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, attrName);
    }

    @Test
    public void testConstructorWithEmptyListOfAttributes() {
        Element root = new Element("root");
        NodePointer parentPointer = new JDOMElementPointer(root);
        QName attrName = new QName("attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, attrName);

        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testNextMethodWhenAtLastElement() {
        Element root = new Element("root");
        root.addAttribute(new Attribute("attr1", "value1"));
        NodePointer parentPointer = new JDOMElementPointer(root);
        QName attrName = new QName("attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, attrName);

        iterator.next();
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testNextMethodWhenAtFirstElement() {
        Element root = new Element("root");
        root.addAttribute(new Attribute("attr1", "value1"));
        NodePointer parentPointer = new JDOMElementPointer(root);
        QName attrName = new QName("attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, attrName);

        iterator.next();
        Assert.assertEquals(0, iterator.getPosition());

        iterator.next();
        Assert.assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testNextMethodWhenInMiddleOfList() {
        Element root = new Element("root");
        root.addAttribute(new Attribute("attr1", "value1"));
        root.addAttribute(new Attribute("attr2", "value2"));
        NodePointer parentPointer = new JDOMElementPointer(root);
        QName attrName = new QName("attr2");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, attrName);

        iterator.next();
        iterator.next();

        Assert.assertEquals(2, iterator.getPosition());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionWithNegativeIndex() {
        Element root = new Element("root");
        root.addAttribute(new Attribute("attr1", "value1"));
        NodePointer parentPointer = new JDOMElementPointer(root);
        QName attrName = new QName("attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, attrName);

        iterator.setPosition(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionWithIndexGreaterThanSize() {
        Element root = new Element("root");
        root.addAttribute(new Attribute("attr1", "value1"));
        NodePointer parentPointer = new JDOMElementPointer(root);
        QName attrName = new QName("attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, attrName);

        iterator.setPosition(2);
    }

    @Test
    public void testSetPositionWithValidIndex() {
        Element root = new Element("root");
        root.addAttribute(new Attribute("attr1", "value1"));
        root.addAttribute(new Attribute("attr2", "value2"));
        NodePointer parentPointer = new JDOMElementPointer(root);
        QName attrName = new QName("attr2");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, attrName);

        iterator.setPosition(1);

        Assert.assertEquals(1, iterator.getPosition());
    }
}