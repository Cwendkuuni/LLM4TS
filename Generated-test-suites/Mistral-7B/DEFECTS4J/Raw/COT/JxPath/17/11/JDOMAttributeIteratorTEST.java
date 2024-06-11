package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;

/**
 * Test class for JDOMAttributeIterator
 */
public class JDOMAttributeIteratorTest {

    /**
     * Test constructor with exact attribute name
     */
    @Test
    public void testConstructorWithExactName() {
        Element root = new Element("root");
        Attribute attribute = new Attribute("test", "value");
        root.addAttribute(attribute);

        QName attributeName = new QName("*, test");
        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeName);

        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertTrue(nodePointer instanceof JDOMAttributePointer);
        Attribute attributeFromIterator = ((JDOMAttributePointer) nodePointer).getAttribute();
        Assert.assertEquals(attribute, attributeFromIterator);
    }

    /**
     * Test constructor with wildcard attribute name
     */
    @Test
    public void testConstructorWithWildcardName() {
        Element root = new Element("root");
        Attribute attribute1 = new Attribute("test1", "value1");
        Attribute attribute2 = new Attribute("test2", "value2");
        Attribute attribute3 = new Attribute("test3", "value3");
        root.addAttribute(attribute1);
        root.addAttribute(attribute2);
        root.addAttribute(attribute3);

        QName attributeName = new QName("*", "*");
        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeName);

        int expectedSize = 3;
        for (int i = 0; i < expectedSize; i++) {
            NodePointer nodePointer = iterator.getNodePointer();
            Assert.assertNotNull(nodePointer);
            Assert.assertTrue(nodePointer instanceof JDOMAttributePointer);
        }
    }

    /**
     * Test setPosition with valid position
     */
    @Test
    public void testSetPositionWithValidPosition() {
        Element root = new Element("root");
        Attribute attribute1 = new Attribute("test1", "value1");
        Attribute attribute2 = new Attribute("test2", "value2");
        Attribute attribute3 = new Attribute("test3", "value3");
        root.addAttribute(attribute1);
        root.addAttribute(attribute2);
        root.addAttribute(attribute3);

        QName attributeName = new QName("*", "*");
        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeName);

        iterator.setPosition(2);
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertTrue(nodePointer instanceof JDOMAttributePointer);
        Attribute attributeFromIterator = ((JDOMAttributePointer) nodePointer).getAttribute();
        Assert.assertEquals(attribute3, attributeFromIterator);
    }

    /**
     * Test setPosition with invalid position
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionWithInvalidPosition() {
        Element root = new Element("root");
        Attribute attribute1 = new Attribute("test1", "value1");
        Attribute attribute2 = new Attribute("test2", "value2");
        Attribute attribute3 = new Attribute("test3", "value3");
        root.addAttribute(attribute1);
        root.addAttribute(attribute2);
        root.addAttribute(attribute3);

        QName attributeName = new QName("*", "*");
        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeName);

        iterator.setPosition(4);
    }

    /**
     * Test getPosition with valid position
     */
    @Test
    public void testGetPositionWithValidPosition() {
        Element root = new Element("root");
        Attribute attribute1 = new Attribute("test1", "value1");
        Attribute attribute2 = new Attribute("test2", "value2");
        Attribute attribute3 = new Attribute("test3", "value3");
        root.addAttribute(attribute1);
        root.addAttribute(attribute2);
        root.addAttribute(attribute3);

        QName attributeName = new QName("*", "*");
        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeName);

        iterator.setPosition(2);
        int expectedPosition = 2;
        int position = iterator.getPosition();
        Assert.assertEquals(expectedPosition, position);
    }

    /**
     * Test getPosition with invalid position
     */
    @Test
    public void testGetPositionWithInvalidPosition() {
        Element root = new Element("root");
        Attribute attribute1 = new Attribute("test1", "value1");
        Attribute attribute2 = new Attribute("test2", "value2");
        Attribute attribute3 = new Attribute("test3", "value3");
        root.addAttribute(attribute1);
        root.addAttribute(attribute2);
        root.addAttribute(attribute3);

        QName attributeName = new QName("*", "*");
        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeName);

        int expectedPosition = 0;
        int position = iterator.getPosition();
        Assert.assertEquals(expectedPosition, position);
    }
}