package org.apache.commons.cli.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyIterator() {
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testIteratorWithOneAttribute() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "attr1"));
        NodePointer attributePointer = iterator.getNodePointer();
        assertNotNull(attributePointer);
        assertSame(attributePointer.getNode().getQualifiedName(), new QName("", "attr1").getQualifiedName());
        assertEquals(attributePointer.getNode().getValue(), "value1");
    }

    @Test
    public void testIteratorWithMultipleAttributes() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        element.addAttribute("attr2", "value2");
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        NodePointer attributePointer1 = iterator.getNodePointer();
        NodePointer attributePointer2 = iterator.getNodePointer();

        assertNotNull(attributePointer1);
        assertSame(attributePointer1.getNode().getQualifiedName(), new QName("", "attr1").getQualifiedName());
        assertEquals(attributePointer1.getNode().getValue(), "value1");

        assertNotNull(attributePointer2);
        assertSame(attributePointer2.getNode().getQualifiedName(), new QName("", "attr2").getQualifiedName());
        assertEquals(attributePointer2.getNode().getValue(), "value2");
    }

    @Test
    public void testIteratorWithNamespace() {
        Element element = new Element("root");
        Namespace ns = Namespace.getNamespace("prefix");
        element.addNamespaceDeclaration(ns);
        element.addAttribute("attr1", "value1", ns);
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("prefix", "attr1"));
        NodePointer attributePointer = iterator.getNodePointer();
        assertNotNull(attributePointer);
        assertSame(attributePointer.getNode().getQualifiedName(), new QName("prefix", "attr1").getQualifiedName());
        assertEquals(attributePointer.getNode().getValue(), "value1");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIteratorOutOfBounds() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "attr1"));
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.getNodePointer();
    }

    @Test
    public void testIteratorWithEmptyAttributeList() {
        Element element = new Element("root");
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        assertNull(iterator.getNodePointer());
    }
}