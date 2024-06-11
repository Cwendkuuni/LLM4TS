package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import org.junit.Test;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_EmptyElement() {
        NodePointer parent = new JDOMNodePointer(new Element("empty"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_EmptyNamespace() {
        Element parentElement = new Element("parent");
        NodePointer parent = new JDOMNodePointer(parentElement);
        QName name = new QName("name");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_SingleAttribute() {
        Element parentElement = new Element("parent");
        parentElement.addAttribute("id", "1");
        NodePointer parent = new JDOMNodePointer(parentElement);
        QName name = new QName("id");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        NodePointer attributePointer = iterator.getNodePointer();
        assertNotNull(attributePointer);
        assertSame(attributePointer, new JDOMAttributePointer(parent, parentElement.getAttribute("id")));
    }

    @Test
    public void testJDOMAttributeIterator_MultipleAttributes_SameNamespace() {
        Element parentElement = new Element("parent");
        parentElement.addAttribute("attribute1", "value1");
        parentElement.addAttribute("attribute1", "value2");
        NodePointer parent = new JDOMNodePointer(parentElement);
        QName name = new QName("attribute1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        NodePointer attributePointer1 = iterator.getNodePointer();
        NodePointer attributePointer2 = iterator.getNodePointer();
        assertNotNull(attributePointer1);
        assertSame(attributePointer1, new JDOMAttributePointer(parent, parentElement.getAttribute("attribute1")));
        assertNotNull(attributePointer2);
        assertSame(attributePointer2, new JDOMAttributePointer(parent, parentElement.getAttribute("attribute1")));
        assertEquals(attributePointer1.getPosition(), attributePointer2.getPosition());
    }

    @Test
    public void testJDOMAttributeIterator_MultipleAttributes_DifferentNamespaces() {
        Element parentElement = new Element("parent");
        parentElement.addAttribute("namespace1:attribute1", "value1");
        parentElement.addAttribute("namespace2:attribute2", "value2");
        NodePointer parent = new JDOMNodePointer(parentElement);
        QName name1 = new QName("namespace1:attribute1");
        JDOMAttributeIterator iterator1 = new JDOMAttributeIterator(parent, name1);
        NodePointer attributePointer1 = iterator1.getNodePointer();
        assertNotNull(attributePointer1);
        assertSame(attributePointer1, new JDOMAttributePointer(parent, parentElement.getAttribute("namespace1:attribute1")));

        QName name2 = new QName("namespace2:attribute2");
        JDOMAttributeIterator iterator2 = new JDOMAttributeIterator(parent, name2);
        NodePointer attributePointer2 = iterator2.getNodePointer();
        assertNotNull(attributePointer2);
        assertSame(attributePointer2, new JDOMAttributePointer(parent, parentElement.getAttribute("namespace2:attribute2")));
        assertFalse(attributePointer1.equals(attributePointer2));
    }

    @Test
    public void testJDOMAttributeIterator_MultipleAttributes_DifferentNamespaces_SameValue() {
        Element parentElement = new Element("parent");
        parentElement.addAttribute("namespace1:attribute1", "value");
        parentElement.addAttribute("namespace2:attribute1", "value");
        NodePointer parent = new JDOMNodePointer(parentElement);
        QName name = new QName("namespace1:attribute1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        NodePointer attributePointer1 = iterator.getNodePointer();
        NodePointer attributePointer2 = iterator.getNodePointer();
        assertNotNull(attributePointer1);
        assertSame(attributePointer1, new JDOMAttributePointer(parent, parentElement.getAttribute("namespace1:attribute1")));
        assertNotNull(attributePointer2);
        assertSame(attributePointer2, new JDOMAttributePointer(parent, parentElement.getAttribute("namespace2:attribute1")));
        assertTrue(attributePointer1.equals(attributePointer2));
    }

    @Test
    public void testJDOMAttributeIterator_MultipleAttributes_SameName() {
        Element parentElement = new Element("parent");
        parentElement.addAttribute("attribute", "value1");
        parentElement.addAttribute("attribute", "value2");
        NodePointer parent = new JDOMNodePointer(parentElement);
        QName name = new QName("attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        NodePointer attributePointer1 = iterator.getNodePointer();
        NodePointer attributePointer2 = iterator.getNodePointer();
        assertNotNull(attributePointer1);
        assertSame(attributePointer1, new JDOMAttributePointer(parent, parentElement.getAttribute("attribute")));
        assertNotNull(attributePointer2);
        assertSame(attributePointer2, new JDOMAttributePointer(parent, parentElement.getAttribute("attribute")));
        assertFalse(attributePointer1.equals(attributePointer2));
        iterator.setPosition(1);
        NodePointer attributePointer3 = iterator.getNodePointer();
        assertTrue(attributePointer1.equals(attributePointer3));
    }

    @Test
    public void testJDOMAttributeIterator_MultipleAttributes_SameName_DifferentValues() {
        Element parentElement = new Element("parent");
        parentElement.addAttribute("attribute", "value1");
        parentElement.addAttribute("attribute", "value2");
        NodePointer parent = new JDOMNodePointer(parentElement);
        QName name = new QName("attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        NodePointer attributePointer1 = iterator.getNodePointer();
        NodePointer attributePointer2 = iterator.getNodePointer();
        assertNotNull(attributePointer1);
        assertSame(attributePointer1, new JDOMAttributePointer(parent, parentElement.getAttribute("attribute")));
        assertNotNull(attributePointer2);
        assertSame(attributePointer2, new JDOMAttributePointer(parent, parentElement.getAttribute("attribute")));
        assertFalse(attributePointer1.equals(attributePointer2));
        iterator.setPosition(1);
        NodePointer attributePointer3 = iterator.getNodePointer();
        assertFalse(attributePointer1.equals(attributePointer3));
    }

    @Test
    public void testJDOMAttributeIterator_MoveToNextAttribute() {
        Element parentElement = new Element("parent");
        parentElement.addAttribute("attribute1", "value1");
        parentElement.addAttribute("attribute2", "value2");
        NodePointer parent = new JDOMNodePointer(parentElement);
        QName name = new QName("attribute1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        NodePointer attributePointer1 = iterator.getNodePointer();
        assertNotNull(attributePointer1);
        NodePointer attributePointer2 = iterator.getNodePointer();
        assertNotNull(attributePointer2);
        assertFalse(attributePointer1.equals(attributePointer2));
        assertEquals(attributePointer1.getPosition(), 0);
        assertEquals(attributePointer2.getPosition(), 1);
        iterator.setPosition(1);
        NodePointer attributePointer3 = iterator.getNodePointer();
        assertFalse(attributePointer1.equals(attributePointer3));
        assertEquals(attributePointer2.getPosition(), 1);
        assertEquals(attributePointer3.getPosition(), 2);
    }

    @Test
    public void testJDOMAttributeIterator_MoveToPreviousAttribute() {
        Element parentElement = new Element("parent");
        parentElement.addAttribute("attribute1", "value1");
        parentElement.addAttribute("attribute2", "value2");
        NodePointer parent = new JDOMNodePointer(parentElement);
        QName name = new QName("attribute1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        NodePointer attributePointer1 = iterator.getNodePointer();
        NodePointer attributePointer2 = iterator.getNodePointer();
        assertNotNull(attributePointer1);
        assertNotNull(attributePointer2);
        assertFalse(attributePointer1.equals(attributePointer2));
        assertEquals(attributePointer1.getPosition(), 0);
        assertEquals(attributePointer2.getPosition(), 1);
        iterator.setPosition(1);
        NodePointer attributePointer3 = iterator.getNodePointer();
        assertFalse(attributePointer1.equals(attributePointer3));
        assertEquals(attributePointer2.getPosition(), 1);
        assertEquals(attributePointer3.getPosition(), 0);
    }

    @Test
    public void testJDOMAttributeIterator_MoveToFirstAttribute() {
        Element parentElement = new Element("parent");
        parentElement.addAttribute("attribute1", "value1");
        parentElement.addAttribute("attribute2", "value2");
        NodePointer parent = new JDOMNodePointer(parentElement);
        QName name = new QName("attribute1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        NodePointer attributePointer1 = iterator.getNodePointer();
        NodePointer attributePointer2 = iterator.getNodePointer();
        NodePointer attributePointer3 = iterator.getNodePointer();
        assertNotNull(attributePointer1);
        assertNotNull(attributePointer2);
        assertNotNull(attributePointer3);
        assertFalse(attributePointer1.equals(attributePointer2));
        assertFalse(attributePointer1.equals(attributePointer3));
        assertEquals(attributePointer1.getPosition(), 0);
        assertEquals(attributePointer2.getPosition(), 1);
        assertEquals(attributePointer3.getPosition(), 2);
        iterator.setPosition(2);
        NodePointer attributePointer4 = iterator.getNodePointer();
        assertFalse(attributePointer1.equals(attributePointer4));
        assertEquals(attributePointer3.getPosition(), 2);
        assertEquals(attributePointer4.getPosition(), 0);
    }

    @Test
    public void testJDOMAttributeIterator_MoveToLastAttribute() {
        Element parentElement = new Element("parent");
        parentElement.addAttribute("attribute1", "value1");
        parentElement.addAttribute("attribute2", "value2");
        NodePointer parent = new JDOMNodePointer(parentElement);
        QName name = new QName("attribute1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        NodePointer attributePointer1 = iterator.getNodePointer();
        NodePointer attributePointer2 = iterator.getNodePointer();
        NodePointer attributePointer3 = iterator.getNodePointer();
        NodePointer attributePointer4 = iterator.getNodePointer();
        assertNotNull(attributePointer1);
        assertNotNull(attributePointer2);
        assertNotNull(attributePointer3);
        assertNotNull(attributePointer4);
        assertFalse(attributePointer1.equals(attributePointer2));
        assertFalse(attributePointer1.equals(attributePointer3));
        assertFalse(attributePointer1.equals(attributePointer4));
        assertEquals(attributePointer1.getPosition(), 0);
        assertEquals(attributePointer2.getPosition(), 1);
        assertEquals(attributePointer3.getPosition(), 2);
        assertEquals(attributePointer4.getPosition(), 2);
        iterator.setPosition(2);
        NodePointer attributePointer5 = iterator.getNodePointer();
        assertFalse(attributePointer1.equals(attributePointer5));
        assertFalse(attributePointer2.equals(attributePointer5));
        assertEquals(attributePointer3.getPosition(), 2);
        assertEquals(attributePointer4.getPosition(), 2);
        assertEquals(attributePointer5.getPosition(), 0);
    }

    @Test
    public void testJDOMAttributeIterator_MoveToNextAttribute_EndOfIteration() {
        Element parentElement = new Element("parent");
        parentElement.addAttribute("attribute1", "value1");
        NodePointer parent = new JDOMNodePointer(parentElement);
        QName name = new QName("attribute2");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_MoveToPreviousAttribute_BeginOfIteration() {
        Element parentElement = new Element("parent");
        parentElement.addAttribute("attribute1", "value1");
        NodePointer parent = new JDOMNodePointer(parentElement);
        QName name = new QName("attribute1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
    }
}