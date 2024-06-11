package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * JDOMAttributeIteratorTest. Java class for testing JDOMAttributeIterator class.
 */
public class JDOMAttributeIteratorTest {

    private NodePointer nodePointer;
    private JDOMAttributeIterator iterator;

    @Before
    public void setup() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        element.addAttribute("attr2", "value2");
        nodePointer = new NodePointer(element);
    }

    @Test
    public void testConstructorWithValidName() {
        QName name = new QName("*");
        iterator = new JDOMAttributeIterator(nodePointer, name);
        Assert.assertFalse(iterator.attributes.isEmpty());
    }

    @Test
    public void testConstructorWithEmptyNameSpace() {
        QName name = new QName("", "attr1");
        iterator = new JDOMAttributeIterator(nodePointer, name);
        Attribute expectedAttribute = nodePointer.getNode().getAttribute("attr1");
        Assert.assertEquals(expectedAttribute, iterator.getNodePointer().getNode().getAttribute("attr1"));
    }

    @Test
    public void testConstructorWithValidPrefixAndName() {
        String namespaceUri = "http://test.com";
        Namespace namespace = new Namespace("prefix", namespaceUri);
        QName name = new QName("prefix:attr1");
        iterator = new JDOMAttributeIterator(nodePointer, name);
        Attribute expectedAttribute = nodePointer.getNode().getAttribute("attr1", namespace);
        Assert.assertEquals(expectedAttribute, iterator.getNodePointer().getNode().getAttribute("attr1", namespace));
    }

    @Test
    public void testConstructorWithInvalidPrefixAndName() {
        String namespaceUri = "http://test.com";
        Namespace namespace = new Namespace("prefix", namespaceUri);
        QName name = new QName("invalid:attr1");
        iterator = new JDOMAttributeIterator(nodePointer, name);
        Assert.assertNotNull(iterator.attributes);
        Assert.assertTrue(iterator.attributes.isEmpty());
    }

    @Test
    public void testGetNodePointer() {
        QName name = new QName("*");
        iterator = new JDOMAttributeIterator(nodePointer, name);
        Attribute expectedAttribute = nodePointer.getNode().getAttribute("attr2");
        Assert.assertEquals(expectedAttribute, iterator.getNodePointer().getNode().getAttribute("attr2"));
    }

    @Test
    public void testGetPosition() {
        QName name = new QName("*");
        iterator = new JDOMAttributeIterator(nodePointer, name);
        Assert.assertEquals(0, iterator.getPosition());
        iterator.next();
        Assert.assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPositionNegative() {
        QName name = new QName("*");
        iterator = new JDOMAttributeIterator(nodePointer, name);
        Assert.assertFalse(iterator.setPosition(-1));
    }

    @Test
    public void testSetPositionZero() {
        QName name = new QName("*");
        iterator = new JDOMAttributeIterator(nodePointer, name);
        Assert.assertTrue(iterator.setPosition(0));
    }

    @Test
    public void testSetPositionGreaterThanSize() {
        QName name = new QName("*");
        iterator = new JDOMAttributeIterator(nodePointer, name);
        Assert.assertFalse(iterator.setPosition(3));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNodePointerWithInvalidPosition() {
        QName name = new QName("*");
        iterator = new JDOMAttributeIterator(nodePointer, name);
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNull(nodePointer);
    }
}