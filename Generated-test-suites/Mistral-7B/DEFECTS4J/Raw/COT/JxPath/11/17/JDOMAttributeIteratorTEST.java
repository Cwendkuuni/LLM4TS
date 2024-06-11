package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.JDOMException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_constructor_NullParent() {
        Assert.assertNull("Expected null for JDOMAttributeIterator when parent is null",
                new JDOMAttributeIterator(null, new QName("", "")));
    }

    @Test
    public void testJDOMAttributeIterator_constructor_EmptyList() {
        Element element = new Element("root");
        NodePointer parentPointer = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("", ""));
        Assert.assertSame("Expected empty list", Collections.EMPTY_LIST, iterator.attributes);
    }

    @Test
    public void testJDOMAttributeIterator_constructor_SingleAttribute() throws JDOMException {
        Element element = new Element("root", "root");
        element.addAttribute("attr1", "value1");
        NodePointer parentPointer = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("", "attr1"));
        List<Attribute> attributes = iterator.getAttributes();
        Assert.assertNotNull("Expected not null for attributes", attributes);
        Assert.assertEquals("Expected size 1 for attributes", 1, attributes.size());
        Attribute attribute = (Attribute) attributes.get(0);
        Assert.assertEquals("Expected 'attr1' for attribute name", "attr1", attribute.getName());
        Assert.assertEquals("Expected 'value1' for attribute value", "value1", attribute.getValue());
    }

    @Test
    public void testJDOMAttributeIterator_constructor_MultipleAttributes() throws JDOMException {
        Element element = new Element("root", "root");
        element.addAttribute("attr1", "value1");
        element.addAttribute("attr2", "value2");
        NodePointer parentPointer = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("", ""));
        List<Attribute> attributes = iterator.getAttributes();
        Assert.assertNotNull("Expected not null for attributes", attributes);
        Assert.assertEquals("Expected size 2 for attributes", 2, attributes.size());
        Attribute attr1 = (Attribute) attributes.get(0);
        Attribute attr2 = (Attribute) attributes.get(1);
        Assert.assertEquals("Expected 'attr1' for first attribute name", "attr1", attr1.getName());
        Assert.assertEquals("Expected 'value1' for first attribute value", "value1", attr1.getValue());
        Assert.assertEquals("Expected 'attr2' for second attribute name", "attr2", attr2.getName());
        Assert.assertEquals("Expected 'value2' for second attribute value", "value2", attr2.getValue());
    }

    @Test
    public void testJDOMAttributeIterator_constructor_MultipleNamespaces() throws JDOMException {
        Element element = new Element("root", "root");
        String xmlNs = "xml";
        String prefix1 = "p1";
        String prefix2 = "p2";
        Element childElement = new Element("child", "child");
        childElement.addAttribute("attr1", "value1", Namespace.getNamespace(xmlNs));
        childElement.addAttribute("attr2", "value2", new Namespace(prefix1));
        childElement.addAttribute("attr3", "value3", new Namespace(prefix2));
        element.addContent(childElement);
        NodePointer parentPointer = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName(prefix1, "attr3"));
        List<Attribute> attributes = iterator.getAttributes();
        Assert.assertNotNull("Expected not null for attributes", attributes);
        Assert.assertEquals("Expected size 1 for attributes", 1, attributes.size());
        Attribute attribute = (Attribute) attributes.get(0);
        Assert.assertEquals("Expected 'attr3' for attribute name", "attr3", attribute.getName());
        Assert.assertEquals("Expected 'value3' for attribute value", "value3", attribute.getValue());
        Assert.assertEquals("Expected 'p2' for namespace prefix", prefix2, attribute.getNamespace().getPrefix());
    }

    @Test
    public void testJDOMAttributeIterator_getNodePointer_NullIterator() {
        NodePointer parentPointer = new JDOMNodePointer(new Element("root"));
        Assert.assertNull("Expected null for getNodePointer when iterator is null",
                JDOMAttributeIterator.getNodePointer(null, parentPointer));
    }

    @Test
    public void testJDOMAttributeIterator_getNodePointer_EmptyList() {
        Element element = new Element("root");
        NodePointer parentPointer = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("", ""));
        Assert.assertNull("Expected null for getNodePointer when position is out of bounds",
                iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_getNodePointer_SingleAttribute() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        NodePointer parentPointer = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("", "attr1"));
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull("Expected not null for getNodePointer", nodePointer);
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;
        Attribute attribute = attributePointer.getAttribute();
        Assert.assertEquals("Expected 'attr1' for attribute name", "attr1", attribute.getName());
        Assert.assertEquals("Expected 'value1' for attribute value", "value1", attribute.getValue());
    }

    @Test
    public void testJDOMAttributeIterator_getNodePointer_MultipleAttributes() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        element.addAttribute("attr2", "value2");
        NodePointer parentPointer = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("", ""));
        NodePointer nodePointer1 = iterator.getNodePointer();
        NodePointer nodePointer2 = iterator.getNodePointer();
        Assert.assertNotNull("Expected not null for getNodePointer", nodePointer1);
        Assert.assertNotNull("Expected not null for getNodePointer", nodePointer2);
        JDOMAttributePointer attributePointer1 = (JDOMAttributePointer) nodePointer1;
        Attribute attribute1 = attributePointer1.getAttribute();
        JDOMAttributePointer attributePointer2 = (JDOMAttributePointer) nodePointer2;
        Attribute attribute2 = attributePointer2.getAttribute();
        Assert.assertNotSame("Expected different attributes", attribute1, attribute2);
        Assert.assertEquals("Expected 'attr1' for first attribute name", "attr1", attribute1.getName());
        Assert.assertEquals("Expected 'value1' for first attribute value", "value1", attribute1.getValue());
        Assert.assertEquals("Expected 'attr2' for second attribute name", "attr2", attribute2.getName());
        Assert.assertEquals("Expected 'value2' for second attribute value", "value2", attribute2.getValue());
    }

    @Test
    public void testJDOMAttributeIterator_getNodePointer_OutOfBounds() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        NodePointer parentPointer = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("", ""));
        iterator.setPosition(2);
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNull("Expected null for getNodePointer when position is out of bounds", nodePointer);
    }

    @Test
    public void testJDOMAttributeIterator_getPosition_NullIterator() {
        NodePointer parentPointer = new JDOMNodePointer(new Element("root"));
        Assert.assertTrue("Expected -1 for getPosition when iterator is null",
                JDOMAttributeIterator.getPosition(null, parentPointer) == -1);
    }

    @Test
    public void testJDOMAttributeIterator_getPosition_EmptyList() {
        Element element = new Element("root");
        NodePointer parentPointer = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("", ""));
        Assert.assertEquals("Expected 0 for getPosition", 0, iterator.getPosition());
    }

    @Test
    public void testJDOMAttributeIterator_getPosition_SingleAttribute() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        NodePointer parentPointer = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("", "attr1"));
        iterator.setPosition(1);
        Assert.assertEquals("Expected 1 for getPosition", 1, iterator.getPosition());
    }

    @Test
    public void testJDOMAttributeIterator_getPosition_MultipleAttributes() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        element.addAttribute("attr2", "value2");
        NodePointer parentPointer = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("", ""));
        iterator.setPosition(2);
        Assert.assertEquals("Expected 2 for getPosition", 2, iterator.getPosition());
    }

    @Test
    public void testJDOMAttributeIterator_setPosition_NullIterator() {
        NodePointer parentPointer = new JDOMNodePointer(new Element("root"));
        Assert.assertFalse("Expected false for setPosition when iterator is null",
                JDOMAttributeIterator.setPosition(null, parentPointer, 1));
    }

    @Test
    public void testJDOMAttributeIterator_setPosition_OutOfBounds() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        NodePointer parentPointer = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("", ""));
        Assert.assertFalse("Expected false for setPosition when position is out of bounds",
                iterator.setPosition(3));
    }

    @Test
    public void testJDOMAttributeIterator_setPosition_InBounds() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        element.addAttribute("attr2", "value2");
        NodePointer parentPointer = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("", ""));
        Assert.assertTrue("Expected true for setPosition when position is in bounds",
                iterator.setPosition(2));
        Assert.assertEquals("Expected 2 for getPosition after setPosition", 2, iterator.getPosition());
    }
}