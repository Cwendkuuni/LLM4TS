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
    public void testConstructor_ValidName() {
        Element element = new Element("root");
        QName name = new QName("ns:name");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), name);
        Assert.assertNotNull(iterator.attributes);
        Assert.assertEquals(1, iterator.attributes.size());
    }

    @Test
    public void testConstructor_EmptyList() {
        Element element = new Element("root");
        QName name = new QName("", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), name);
        Assert.assertNotNull(iterator.attributes);
        Assert.assertEquals(0, iterator.attributes.size());
    }

    @Test
    public void testConstructor_NullName() {
        Element element = new Element("root");
        QName name = null;
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), name);
        Assert.assertNull(iterator.attributes);
    }

    @Test
    public void testConstructor_NonExistingElement() {
        Element element = new Element("root");
        NodePointer parent = new NodePointer(null);
        QName name = new QName("ns:name");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Assert.assertNull(iterator.attributes);
    }

    @Test
    public void testConstructor_EmptyElement() {
        Element element = new Element("root");
        NodePointer parent = new NodePointer(element);
        QName name = new QName("", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Assert.assertNotNull(iterator.attributes);
        Assert.assertEquals(0, iterator.attributes.size());
    }

    @Test
    public void testConstructor_SingleAttribute() {
        Element element = new Element("root");
        element.addAttribute("name", "value");
        NodePointer parent = new NodePointer(element);
        QName name = new QName("", "name");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Assert.assertNotNull(iterator.attributes);
        Assert.assertEquals(1, iterator.attributes.size());
    }

    @Test
    public void testConstructor_MultipleAttributes() {
        Element element = new Element("root");
        element.addAttribute("name1", "value1");
        element.addAttribute("name2", "value2");
        NodePointer parent = new NodePointer(element);
        QName name = new QName("", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Assert.assertNotNull(iterator.attributes);
        Assert.assertEquals(2, iterator.attributes.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullParent() {
        Element element = new Element("root");
        QName name = new QName("", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, name);
        Assert.fail("Should throw IllegalArgumentException");
    }

    @Test
    public void testGetNodePointer_ValidPosition() {
        Element element = new Element("root");
        element.addAttribute("name1", "value1");
        element.addAttribute("name2", "value2");
        NodePointer parent = new NodePointer(element);
        QName name = new QName("", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        NodePointer expectedNodePointer = new JDOMAttributePointer(parent,
                (Attribute) iterator.attributes.get(0));
        NodePointer actualNodePointer = iterator.getNodePointer();

        Assert.assertEquals(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testGetNodePointer_InvalidPosition() {
        Element element = new Element("root");
        element.addAttribute("name1", "value1");
        element.addAttribute("name2", "value2");
        NodePointer parent = new NodePointer(element);
        QName name = new QName("", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        iterator.setPosition(2);
        NodePointer actualNodePointer = iterator.getNodePointer();
        Assert.assertNull(actualNodePointer);
    }

    @Test
    public void testGetPosition_ValidPosition() {
        Element element = new Element("root");
        element.addAttribute("name1", "value1");
        element.addAttribute("name2", "value2");
        NodePointer parent = new NodePointer(element);
        QName name = new QName("", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        iterator.setPosition(1);
        int expectedPosition = 1;
        int actualPosition = iterator.getPosition();

        Assert.assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void testGetPosition_InvalidPosition() {
        Element element = new Element("root");
        element.addAttribute("name1", "value1");
        element.addAttribute("name2", "value2");
        NodePointer parent = new NodePointer(element);
        QName name = new QName("", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        iterator.setPosition(-1);
        int expectedPosition = 0;
        int actualPosition = iterator.getPosition();

        Assert.assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void testSetPosition_ValidPosition() {
        Element element = new Element("root");
        element.addAttribute("name1", "value1");
        element.addAttribute("name2", "value2");
        NodePointer parent = new NodePointer(element);
        QName name = new QName("", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        iterator.setPosition(1);
        boolean expectedResult = true;
        boolean actualResult = iterator.setPosition(1);

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSetPosition_InvalidPosition() {
        Element element = new Element("root");
        element.addAttribute("name1", "value1");
        element.addAttribute("name2", "value2");
        NodePointer parent = new NodePointer(element);
        QName name = new QName("", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        iterator.setPosition(3);
        boolean expectedResult = false;
        boolean actualResult = iterator.setPosition(3);

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSetPosition_ResetPosition() {
        Element element = new Element("root");
        element.addAttribute("name1", "value1");
        element.addAttribute("name2", "value2");
        NodePointer parent = new NodePointer(element);
        QName name = new QName("", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        iterator.setPosition(1);
        iterator.setPosition(0);
        int expectedPosition = 0;
        int actualPosition = iterator.getPosition();

        Assert.assertEquals(expectedPosition, actualPosition);
    }
}