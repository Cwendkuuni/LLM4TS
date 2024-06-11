
package org.junit.tests.junit4.junit4testcases.junit4testsuite;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.Attribute;
import org.junit.Assert;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("test", null, null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        // Add more assertions based on the expected behavior of the constructor

        Assert.assertNotNull(iterator);
        Assert.assertEquals(parent, iterator.getParent());
        Assert.assertEquals(name.getLocalPart(), iterator.getName().getLocalPart());
        // Add more assertions if needed
    }

    @Test
    public void testGetNodePointer() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("test", null, null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Attribute attribute = new Attribute("test", "testValue", null);
        iterator.setAttribute(attribute);

        NodePointer nodePointer = iterator.getNodePointer();
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;

        Assert.assertEquals(parent, attributePointer.getParent());
        Assert.assertEquals(attribute, attributePointer.getAttribute());
    }

    @Test
    public void testGetPosition() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("test", null, null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Attribute attribute1 = new Attribute("test1", "testValue1", null);
        Attribute attribute2 = new Attribute("test2", "testValue2", null);
        Attribute attribute3 = new Attribute("test3", "testValue3", null);
        iterator.setAttribute(attribute1);
        iterator.setAttribute(attribute2);
        iterator.setAttribute(attribute3);

        int position = iterator.getPosition();

        Assert.assertEquals(0, position);
    }

    @Test
    public void testSetPosition() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("test", null, null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Attribute attribute1 = new Attribute("test1", "testValue1", null);
        Attribute attribute2 = new Attribute("test2", "testValue2", null);
        Attribute attribute3 = new Attribute("test3", "testValue3", null);
        iterator.setAttribute(attribute1);
        iterator.setAttribute(attribute2);
        iterator.setAttribute(attribute3);

        int position = iterator.getPosition();

        Assert.assertTrue(iterator.setPosition(1));
        NodePointer nodePointer = iterator.getNodePointer();
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;
        Attribute expectedAttribute = (Attribute) iterator.getAttribute();

        Assert.assertEquals(attributePointer.getAttribute(), expectedAttribute);

        position = iterator.getPosition();
        Assert.assertEquals(2, position);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPositionWithNegativePosition() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("test", null, null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        iterator.setPosition(-1);
    }

    @Test
    public void testGetNodePointerWhenEndOfIterator() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("test", null, null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Attribute attribute1 = new Attribute("test1", "testValue1", null);
        Attribute attribute2 = new Attribute("test2", "testValue2", null);
        Attribute attribute3 = new Attribute("test3", "testValue3", null);
        iterator.setAttribute(attribute1);
        iterator.setAttribute(attribute2);
        iterator.setAttribute(attribute3);

        NodePointer nodePointer = iterator.getNodePointer();

        Assert.assertNull(nodePointer);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionWithOutOfBoundsPosition() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("test", null, null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Attribute attribute1 = new Attribute("test1", "testValue1", null);
        Attribute attribute2 = new Attribute("test2", "testValue2", null);
        Attribute attribute3 = new Attribute("test3", "testValue3", null);
        iterator.setAttribute(attribute1);
        iterator.setAttribute(attribute2);
        iterator.setAttribute(attribute3);

        iterator.setPosition(4);
    }

    @Test
    public void testGetNodePointerWhenIteratorAtLastAttribute() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("test", null, null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Attribute attribute1 = new Attribute("test1", "testValue1", null);
        Attribute attribute2 = new Attribute("test2", "testValue2", null);
        Attribute attribute3 = new Attribute("test3", "testValue3", null);
        iterator.setAttribute(attribute1);
        iterator.setAttribute(attribute2);
        iterator.setAttribute(attribute3);

        NodePointer nodePointer = iterator.getNodePointer();
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;

        Assert.assertEquals(attributePointer.getAttribute(), attribute3);
    }

    @Test
    public void testGetNodePointerWhenParentIsNull() {
        NodePointer parent = null;
        QName name = new QName("test", null, null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Assert.assertNull(iterator.getNodePointer());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullQName() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = null;
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
    }
}