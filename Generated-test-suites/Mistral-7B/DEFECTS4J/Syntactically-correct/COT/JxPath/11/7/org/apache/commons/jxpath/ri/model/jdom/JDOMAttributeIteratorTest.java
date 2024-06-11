package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.JDOMException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    private NodePointer parentNode;
    private JDOMAttributeIterator attributeIterator;

    @Before
    public void setUp() throws JDOMException {
        Element root = new Element("root");
        Attribute attribute1 = new Attribute("attr1", "value1", "http://test.com");
        Attribute attribute2 = new Attribute("attr2", "value2", null);
        Attribute attribute3 = new Attribute("attr3", "value3", "http://test.com");
        root.addContent(new Element("child1").addContent(attribute1));
        root.addContent(new Element("child2").addContent(attribute2));
        root.addContent(new Element("child3").addContent(attribute3));
        parentNode = new NodePointer(root);
    }

    @Test
    public void testJDOMAttributeIteratorConstructorWithQNameAndNoPrefix() {
        QName attributeName = new QName("attr1");
        attributeIterator = new JDOMAttributeIterator(parentNode, attributeName);
        Assert.assertNotNull(attributeIterator);
        NodePointer nodePointer = attributeIterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("attr1", nodePointer.getNodeName().getName());
        Assert.assertEquals("http://test.com", nodePointer.getNodeName().getNamespaceURI());
    }

    @Test
    public void testJDOMAttributeIteratorConstructorWithQNameAndPrefix() {
        String prefix = "http://test.com";
        QName attributeName = new QName(prefix, "attr1");
        attributeIterator = new JDOMAttributeIterator(parentNode, attributeName);
        Assert.assertNotNull(attributeIterator);
        NodePointer nodePointer = attributeIterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("attr1", nodePointer.getNodeName().getName());
        Assert.assertEquals(prefix, nodePointer.getNodeName().getNamespaceURI());
    }

    @Test
    public void testJDOMAttributeIteratorConstructorWithNullQName() {
        attributeIterator = new JDOMAttributeIterator(parentNode, null);
        Assert.assertNull(attributeIterator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIteratorConstructorWithNullParentNode() {
        QName attributeName = new QName("attr1");
        attributeIterator = new JDOMAttributeIterator(null, attributeName);
        Assert.fail("IllegalArgumentException expected but not thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIteratorConstructorWithEmptyList() {
        QName attributeName = new QName("*");
        attributeIterator = new JDOMAttributeIterator(parentNode, attributeName);
        Assert.assertNotNull(attributeIterator);
        Assert.assertTrue(attributeIterator.hasNext());
        Attribute firstAttribute = (Attribute) attributeIterator.next().getNode();
        Assert.assertNotNull(firstAttribute);
        Assert.assertFalse(attributeIterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIteratorGetNodePointer() {
        QName attributeName = new QName("attr1");
        attributeIterator = new JDOMAttributeIterator(parentNode, attributeName);
        NodePointer nodePointer = attributeIterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("attr1", nodePointer.getNodeName().getName());
        Assert.assertEquals(attributeName.getNamespaceURI(), nodePointer.getNodeName().getNamespaceURI());
        Attribute attribute = (Attribute) nodePointer.getNode();
        Assert.assertNotNull(attribute);
        Assert.assertEquals("value1", attribute.getValue());
    }

    @Test
    public void testJDOMAttributeIteratorGetPosition() {
        QName attributeName = new QName("attr1");
        attributeIterator = new JDOMAttributeIterator(parentNode, attributeName);
        Assert.assertEquals(1, attributeIterator.getPosition());
        Attribute firstAttribute = (Attribute) attributeIterator.next().getNode();
        Assert.assertEquals(2, attributeIterator.getPosition());
        Assert.assertFalse(attributeIterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIteratorHasNextWithFirstAttribute() {
        QName attributeName = new QName("attr1");
        attributeIterator = new JDOMAttributeIterator(parentNode, attributeName);
        Assert.assertTrue(attributeIterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIteratorHasNextWithLastAttribute() {
        QName attributeName = new QName("attr3");
        attributeIterator = new JDOMAttributeIterator(parentNode, attributeName);
        Assert.assertTrue(attributeIterator.hasNext());
        Attribute currentAttribute = (Attribute) attributeIterator.next().getNode();
        Assert.assertEquals("attr3", currentAttribute.getName());
        Assert.assertFalse(attributeIterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIteratorHasNextWithMiddleAttribute() {
        QName attributeName = new QName("attr2");
        attributeIterator = new JDOMAttributeIterator(parentNode, attributeName);
        Assert.assertTrue(attributeIterator.hasNext());
        Attribute currentAttribute = (Attribute) attributeIterator.next().getNode();
        Assert.assertEquals("attr2", currentAttribute.getName());
        Assert.assertTrue(attributeIterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testJDOMAttributeIteratorNoSuchElement() {
        QName attributeName = new QName("attr4");
        attributeIterator = new JDOMAttributeIterator(parentNode, attributeName);
        Assert.assertFalse(attributeIterator.hasNext());
        attributeIterator.next();
        Assert.fail("NoSuchElementException expected but not thrown");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testJDOMAttributeIteratorIndexOutOfBounds() {
        QName attributeName = new QName("attr1");
        attributeIterator = new JDOMAttributeIterator(parentNode, attributeName);
        attributeIterator.next();
        attributeIterator.next();
        attributeIterator.next();
        attributeIterator.next();
        attributeIterator.next();
        attributeIterator.next();
        Assert.fail("IndexOutOfBoundsException expected but not thrown");
    }
}