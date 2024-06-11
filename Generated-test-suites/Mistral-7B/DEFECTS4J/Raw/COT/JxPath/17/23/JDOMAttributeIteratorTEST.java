package org.apache.commons.jxpath.ri.model.jdom.test;

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
    public void testConstructorWithNameAndPrefix() {
        Element element = new Element("root");
        Namespace namespace = Namespace.getNamespace("prefix", "uri");
        Attribute attribute = new Attribute("attribute", "value", namespace);
        element.addAttribute(attribute);

        NodePointer parent = new JDOMElementPointer(null, element);
        QName name = new QName("prefix:attribute");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Assert.assertNotNull(iterator);
        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertEquals(attribute, iterator.getNodePointer().getNode());
    }

    @Test
    public void testConstructorWithNameAndNullPrefix() {
        Element element = new Element("root");
        Attribute attribute = new Attribute("attribute", "value");
        element.addAttribute(attribute);

        NodePointer parent = new JDOMElementPointer(null, element);
        QName name = new QName("attribute");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Assert.assertNotNull(iterator);
        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertEquals(attribute, iterator.getNodePointer().getNode());
    }

    @Test
    public void testConstructorWithNameAndNoMatchingAttributes() {
        Element element = new Element("root");

        NodePointer parent = new JDOMElementPointer(null, element);
        QName name = new QName("nonExistent:attribute");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Assert.assertNull(iterator);
    }

    @Test
    public void testNext() {
        Element element = new Element("root");
        Namespace namespace = Namespace.getNamespace("prefix", "uri");
        Attribute attribute1 = new Attribute("attribute1", "value1", namespace);
        Attribute attribute2 = new Attribute("attribute2", "value2", namespace);
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);

        NodePointer parent = new JDOMElementPointer(null, element);
        QName name = new QName("prefix:*");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertEquals(attribute1, iterator.getNodePointer().getNode());

        iterator.next();
        Assert.assertEquals(2, iterator.getPosition());
        Assert.assertEquals(attribute2, iterator.getNodePointer().getNode());

        iterator.next();
        Assert.assertEquals(-1, iterator.getPosition());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextAfterEnd() {
        Element element = new Element("root");
        Namespace namespace = Namespace.getNamespace("prefix", "uri");
        Attribute attribute1 = new Attribute("attribute1", "value1", namespace);
        Attribute attribute2 = new Attribute("attribute2", "value2", namespace);
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);

        NodePointer parent = new JDOMElementPointer(null, element);
        QName name = new QName("prefix:*");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        iterator.next();
        iterator.next();
        iterator.next();
    }

    @Test
    public void testHasNext() {
        Element element = new Element("root");
        Namespace namespace = Namespace.getNamespace("prefix", "uri");
        Attribute attribute1 = new Attribute("attribute1", "value1", namespace);
        Attribute attribute2 = new Attribute("attribute2", "value2", namespace);
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);

        NodePointer parent = new JDOMElementPointer(null, element);
        QName name = new QName("prefix:*");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertFalse(iterator.hasNext());
    }
}