package org.junit.platform.commons.testing.junit4;

import org.apache.commons.jxpath.ri.model.DOMPoint;
import org.apache.commons.jxpath.ri.model.QName;
import org.junit.Test;
import org.junit.platform.commons.testing.answers.Answer;
import org.junit.platform.commons.testing.answers.Answers;

import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {
    @Test
    public void testDOMAttributeIteratorConstructor() {
        NodePointer parent = new DOMPoint("test:root");
        QName name = new QName("test:attrName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        // Check if the constructor initializes the attributes list correctly
        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorgetNodePointer() {
        NodePointer parent = new DOMPoint("test:root");
        QName name = new QName("test:attrName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Attr testAttr = new Attr("test:namespace", "test:attrValue");
        Element testElement = new Element("test:element");
        testElement.setAttributeNode(testAttr);
        parent.setNode(testElement);
        iterator.next();
        DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
        assertEquals(testAttr, attributePointer.getNode());
    }

    @Test
    public void testDOMAttributeIteratorGetPosition() {
        NodePointer parent = new DOMPoint("test:root");
        QName name = new QName("test:attrName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Attr testAttr1 = new Attr("test:namespace1", "test:attrValue1");
        Attr testAttr2 = new Attr("test:namespace2", "test:attrValue2");
        Element testElement = new Element("test:element");
        testElement.setAttributeNode(testAttr1);
        testElement.setAttributeNode(testAttr2);
        parent.setNode(testElement);
        iterator.next();
        iterator.next();
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIteratorSetPosition() {
        NodePointer parent = new DOMPoint("test:root");
        QName name = new QName("test:attrName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Attr testAttr1 = new Attr("test:namespace1", "test:attrValue1");
        Attr testAttr2 = new Attr("test:namespace2", "test:attrValue2");
        Element testElement = new Element("test:element");
        testElement.setAttributeNode(testAttr1);
        testElement.setAttributeNode(testAttr2);
        parent.setNode(testElement);
        iterator.next();
        iterator.next();
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        assertFalse(iterator.setPosition(5));
    }

    @Test
    public void testDOMAttributeIteratorTestAttr() {
        NodePointer parent = new DOMPoint("test:root");
        QName name = new QName("test:attrName");
        Element testElement1 = new Element("test:element1");
        testElement1.setAttributeNode(new Attr("test:namespace1", "test:attrValue1"));
        Element testElement2 = new Element("test:element2");
        testElement2.setAttributeNode(new Attr("xmlns:test", "test:namespace"));
        parent.addChild(testElement1);
        parent.addChild(testElement2);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertFalse(iterator.next().getNode() instanceof Attr);
        iterator.next();
        Attr testAttr = (Attr) iterator.getNodePointer().getNode();
        assertEquals("test:attrName", testAttr.getName().getLocalPart());
        assertEquals("test:namespace1", testAttr.getNamespaceURI());
    }

    @Test(expected = NoSuchElementException.class)
    public void testDOMAttributeIteratorAttributeNotFound() {
        NodePointer parent = new DOMPoint("test:root");
        QName name = new QName("test:attrName");
        Element testElement = new Element("test:element");
        parent.setNode(testElement);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
    }
}