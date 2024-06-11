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

/**
 * Test cases for JDOMAttributeIterator class
 */
public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructor_NullParent() {
        try {
            new JDOMAttributeIterator(null, new QName("", ""));
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
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
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", "*"));
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testConstructor_SingleAttribute() {
        Element element = new Element("root", "root", null);
        Attribute attribute = new Attribute("attr1", "value1");
        element.addAttribute(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", "attr1"));
        Assert.assertEquals(1, iterator.getPosition());
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(attribute, nodePointer.getNode());

        iterator.setPosition(0);
        nodePointer = iterator.getNodePointer();
        Assert.assertNull(nodePointer);
    }

    @Test
    public void testConstructor_MultipleAttributes() {
        Element element = new Element("root", "root", null);
        Attribute attr1 = new Attribute("attr1", "value1");
        Attribute attr2 = new Attribute("attr2", "value2");
        element.addAttribute(attr1);
        element.addAttribute(attr2);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", "*"));
        Assert.assertEquals(2, iterator.getPosition());
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(attr1, nodePointer.getNode());

        iterator.setPosition(1);
        nodePointer = iterator.getNodePointer();
        Assert.assertEquals(attr2, nodePointer.getNode());

        iterator.setPosition(3); // Out of bounds
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testSetPosition_OutOfBounds() {
        Element element = new Element("root", "root", null);
        Attribute attr1 = new Attribute("attr1", "value1");
        Attribute attr2 = new Attribute("attr2", "value2");
        element.addAttribute(attr1);
        element.addAttribute(attr2);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", "*"));
        iterator.setPosition(3); // Out of bounds
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testSetPosition_ValidPosition() {
        Element element = new Element("root", "root", null);
        Attribute attr1 = new Attribute("attr1", "value1");
        Attribute attr2 = new Attribute("attr2", "value2");
        element.addAttribute(attr1);
        element.addAttribute(attr2);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", "*"));
        iterator.setPosition(2); // Out of bounds
        Assert.assertNull(iterator.getNodePointer());

        iterator.setPosition(1);
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertEquals(attr2, nodePointer.getNode());
    }

    @Test
    public void testGetNodePointer_EndOfList() {
        Element element = new Element("root", "root", null);
        Attribute attr1 = new Attribute("attr1", "value1");
        Attribute attr2 = new Attribute("attr2", "value2");
        element.addAttribute(attr1);
        element.addAttribute(attr2);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", "*"));
        iterator.setPosition(2); // Out of bounds
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testGetNodePointer_BeginningOfList() {
        Element element = new Element("root", "root", null);
        Attribute attr1 = new Attribute("attr1", "value1");
        Attribute attr2 = new Attribute("attr2", "value2");
        element.addAttribute(attr1);
        element.addAttribute(attr2);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", "*"));
        iterator.setPosition(0);
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNull(nodePointer);

        iterator.setPosition(1);
        nodePointer = iterator.getNodePointer();
        Assert.assertEquals(attr1, nodePointer.getNode());
    }

    @Test
    public void testGetNodePointer_MiddleOfList() {
        Element element = new Element("root", "root", null);
        Attribute attr1 = new Attribute("attr1", "value1");
        Attribute attr2 = new Attribute("attr2", "value2");
        Attribute attr3 = new Attribute("attr3", "value3");
        element.addAttribute(attr1);
        element.addAttribute(attr2);
        element.addAttribute(attr3);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", "*"));
        iterator.setPosition(2);
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertEquals(attr3, nodePointer.getNode());
    }
}