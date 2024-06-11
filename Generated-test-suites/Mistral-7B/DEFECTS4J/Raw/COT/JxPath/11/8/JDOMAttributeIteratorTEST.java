package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.NodeIterator;
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
 * JDOMAttributeIteratorTest
 */
public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructorWithSingleAttribute() {
        QName name = new QName("namespace:localName");
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Attribute attribute = new Attribute("name", "value", "namespace:prefix");
        Element rootElement = new Element("root");
        rootElement.addContent(attribute);

        NodePointer expected = new JDOMAttributePointer(parent, attribute);
        NodePointer actual = iterator.getNodePointer();

        Assert.assertNotNull(actual);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testConstructorWithEmptyList() {
        QName name = new QName("namespace:localName");
        NodePointer parent = new JDOMNodePointer(new Element("root"));;
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Assert.assertEquals(Collections.emptyList(), iterator.attributes);
    }

    @Test
    public void testConstructorWithMultipleAttributes() {
        QName name = new QName("namespace:localName");
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        JDOMAttribeIterator iterator = new JDOMAttributeIterator(parent, name);

        Attribute attr1 = new Attribute("name1", "value1", "namespace:prefix1");
        Attribute attr2 = new Attribute("name2", "value2", "namespace:prefix2");

        Element rootElement = new Element("root");
        rootElement.addContent(attr1);
        rootElement.addContent(attr2);

        List<Attribute> expectedAttributes = new ArrayList<>();
        expectedAttributes.add(attr1);
        expectedAttributes.add(attr2);

        List<Attribute> actualAttributes = iterator.attributes;

        Assert.assertEquals(expectedAttributes, actualAttributes);
    }

    @Test
    public void testGetNodePointerWithValidPosition() {
        QName name = new QName("namespace:localName");
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Attribute attr1 = new Attribute("name1", "value1", "namespace:prefix1");
        Attribute attr2 = new Attribute("name2", "value2", "namespace:prefix2");

        Element rootElement = new Element("root");
        rootElement.addContent(attr1);
        rootElement.addContent(attr2);

        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attr1);
        attributes.add(attr2);
        iterator.attributes = attributes;

        NodePointer expected = new JDOMAttributePointer(parent, attr2);

        iterator.setPosition(2);
        NodePointer actual = iterator.getNodePointer();

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNodePointerWithInvalidPosition() {
        QName name = new QName("namespace:localName");
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Attribute attr1 = new Attribute("name1", "value1", "namespace:prefix1");
        Attribute attr2 = new Attribute("name2", "value2", "namespace:prefix2");

        Element rootElement = new Element("root");
        rootElement.addContent(attr1);
        rootElement.addContent(attr2);

        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attr1);
        attributes.add(attr2);
        iterator.attributes = attributes;

        iterator.setPosition(3);
        NodePointer nodePointer = iterator.getNodePointer();
    }

    @Test
    public void testSetPositionWithValidPosition() {
        QName name = new QName("namespace:localName");
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Attribute attr1 = new Attribute("name1", "value1", "namespace:prefix1");
        Attribute attr2 = new Attribute("name2", "value2", "namespace:prefix2");

        Element rootElement = new Element("root");
        rootElement.addContent(attr1);
        rootElement.addContent(attr2);

        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attr1);
        attributes.add(attr2);
        iterator.attributes = attributes;

        iterator.setPosition(2);
        boolean actual = iterator.setPosition(1);

        Assert.assertTrue(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPositionWithInvalidPosition() {
        QName name = new QName("namespace:localName");
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Attribute attr1 = new Attribute("name1", "value1", "namespace:prefix1");
        Attribute attr2 = new Attribute("name2", "value2", "namespace:prefix2");

        Element rootElement = new Element("root");
        rootElement.addContent(attr1);
        rootElement.addContent(attr2);

        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attr1);
        attributes.add(attr2);
        iterator.attributes = attributes;

        iterator.setPosition(3);
        iterator.setPosition(0);
    }

    @Test
    public void testGetPositionWithValidPosition() {
        QName name = new QName("namespace:localName");
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Attribute attr1 = new Attribute("name1", "value1", "namespace:prefix1");
        Attribute attr2 = new Attribute("name2", "value2", "namespace:prefix2");

        Element rootElement = new Element("root");
        rootElement.addContent(attr1);
        rootElement.addContent(attr2);

        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attr1);
        attributes.add(attr2);
        iterator.attributes = attributes;

        iterator.setPosition(2);
        int expected = 2;
        int actual = iterator.getPosition();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetPositionWithInvalidPosition() {
        QName name = new QName("namespace:localName");
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Attribute attr1 = new Attribute("name1", "value1", "namespace:prefix1");
        Attribute attr2 = new Attribute("name2", "value2", "namespace:prefix2");

        Element rootElement = new Element("root");
        rootElement.addContent(attr1);
        rootElement.addContent(attr2);

        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attr1);
        attributes.add(attr2);
        iterator.attributes = attributes;

        iterator.setPosition(3);
        int expected = -1;
        int actual = iterator.getPosition();

        Assert.assertEquals(expected, actual);
    }
}