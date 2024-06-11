package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.test.TestUtils;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    private NodePointer parentNode;
    private JDOMAttributeIterator attributeIterator;

    @Before
    public void setUp() {
        Element root = new Element("root");
        Attribute attr1 = new Attribute("attr1", "value1", "http://test.com");
        Attribute attr2 = new Attribute("attr2", "value2", null);
        root.addContent(new Element("child").addContent(attr1));
        root.addContent(new Element("child").addContent(attr2));
        parentNode = new NodePointer(root);
    }

    @Test
    public void testConstructor_NullParent() {
        try {
            new JDOMAttributeIterator(null, new QName("*"));
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testConstructor_NullName() {
        try {
            new JDOMAttributeIterator(parentNode, null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testConstructor_EmptyList() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("*"));
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testConstructor_SingleAttribute() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("attr1"));
        Assert.assertEquals(1, iterator.getPosition());
        NodePointer attributePointer = iterator.getNodePointer();
        Assert.assertNotNull(attributePointer);
        Assert.assertEquals("attr1", attributePointer.getName().getLocalPart());
        Assert.assertEquals("value1", attributePointer.getStringValue());
        Assert.assertEquals("http://test.com", attributePointer.getNamespace().getURI());
    }

    @Test
    public void testConstructor_MultipleAttributes() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("*"));
        Assert.assertEquals(2, iterator.getPosition());
        NodePointer firstAttributePointer = iterator.getNodePointer();
        Assert.assertEquals("attr1", firstAttributePointer.getName().getLocalPart());
        Assert.assertEquals("value1", firstAttributePointer.getStringValue());
        Assert.assertEquals("http://test.com", firstAttributePointer.getNamespace().getURI());

        Assert.assertEquals(1, iterator.getPosition());
        NodePointer secondAttributePointer = iterator.getNodePointer();
        Assert.assertEquals("attr2", secondAttributePointer.getName().getLocalPart());
        Assert.assertNull(secondAttributePointer.getStringValue());
        Assert.assertNull(secondAttributePointer.getNamespace());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNodePointer_OutOfBounds() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("*"));
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.getNodePointer();
    }

    @Test
    public void testSetPosition_InvalidPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("*"));
        Assert.assertFalse(iterator.setPosition(-1));
        Assert.assertFalse(iterator.setPosition(3));
    }

    @Test
    public void testSetPosition_ValidPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("*"));
        iterator.next();
        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testToString() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("*"));
        Assert.assertEquals("JDOMAttributeIterator[position=" + iterator.getPosition() + "]", iterator.toString());
    }

    @Test
    public void testHasNext() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("*"));
        Assert.assertTrue(iterator.hasNext());
        iterator.next();
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testNext() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("*"));
        NodePointer firstAttributePointer = iterator.getNodePointer();
        iterator.next();
        NodePointer secondAttributePointer = iterator.getNodePointer();
        Assert.assertEquals(secondAttributePointer, firstAttributePointer.nextSibling());
    }

    @Test
    public void testHasNext_EmptyIterator() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(TestUtils.getEmptyDocument()), new QName("*"));
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testNext_EmptyIterator() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(TestUtils.getEmptyDocument()), new QName("*"));
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition_EmptyIterator() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(TestUtils.getEmptyDocument()), new QName("*"));
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testGetPosition_IteratorWithNextCall() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("*"));
        iterator.next();
        Assert.assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testGetPosition_IteratorWithMultipleNextCalls() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("*"));
        iterator.next();
        iterator.next();
        Assert.assertEquals(2, iterator.getPosition());
    }
}