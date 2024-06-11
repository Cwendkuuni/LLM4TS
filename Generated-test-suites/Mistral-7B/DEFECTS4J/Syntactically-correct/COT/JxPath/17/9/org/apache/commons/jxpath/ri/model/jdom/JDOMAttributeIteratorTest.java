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

public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructor_NullParent() {
        try {
            new JDOMAttributeIterator(null, new QName("", "attrName"));
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testConstructor_EmptyElement() {
        Element element = new Element("root");
        JDOMAttribeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", "attrName"));
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testConstructor_SingleAttribute() {
        Element element = new Element("root");
        Attribute attribute = new Attribute("attrName", "attrValue");
        element.addAttribute(attribute);
        NodePointer nodePointer = new NodePointer(element);
        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "attrName"));
        Attribute attributeFromIterator = (Attribute) iterator.getNodePointer().getNode();
        Assert.assertEquals(attribute, attributeFromIterator);
    }

    @Test
    public void testConstructor_MultipleAttributes() {
        Element element = new Element("root");
        Attribute attribute1 = new Attribute("attrName1", "attrValue1");
        Attribute attribute2 = new Attribute("attrName2", "attrValue2");
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);
        NodePointer nodePointer = new NodePointer(element);
        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "*"));
        int count = 0;
        while (iterator.hasNext()) {
            Attribute attribute = (Attribute) iterator.getNodePointer().getNode();
            if (count++ < 2) {
                Assert.assertNotNull(attribute);
            } else {
                Assert.assertNull(attribute);
            }
            iterator.next();
        }
    }

    @Test
    public void testGetPosition_InitialPosition() {
        Element element = new Element("root");
        Attribute attribute = new Attribute("attrName", "attrValue");
        element.addAttribute(attribute);
        NodePointer nodePointer = new NodePointer(element);
        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "attrName"));
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testGetPosition_MovedToNextPosition() {
        Element element = new Element("root");
        Attribute attribute1 = new Attribute("attrName1", "attrValue1");
        Attribute attribute2 = new Attribute("attrName2", "attrValue2");
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);
        NodePointer nodePointer = new NodePointer(element);
        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "*"));
        iterator.next();
        Assert.assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testGetPosition_MovedToLastPosition() {
        Element element = new Element("root");
        Attribute attribute1 = new Attribute("attrName1", "attrValue1");
        Attribute attribute2 = new Attribute("attrName2", "attrValue2");
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);
        NodePointer nodePointer = new NodePointer(element);
        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "*"));
        iterator.next();
        iterator.next();
        Assert.assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPosition_InvalidPosition() {
        Element element = new Element("root");
        Attribute attribute = new Attribute("attrName", "attrValue");
        element.addAttribute(attribute);
        NodePointer nodePointer = new NodePointer(element);
        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "attrName"));
        Assert.assertFalse(iterator.setPosition(-1));
        Assert.assertFalse(iterator.setPosition(3));
    }

    @Test
    public void testSetPosition_ValidPosition() {
        Element element = new Element("root");
        Attribute attribute1 = new Attribute("attrName1", "attrValue1");
        Attribute attribute2 = new Attribute("attrName2", "attrValue2");
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);
        NodePointer nodePointer = new NodePointer(element);
        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "*"));
        iterator.next();
        iterator.setPosition(1);
        Assert.assertEquals(1, iterator.getPosition());
    }
}