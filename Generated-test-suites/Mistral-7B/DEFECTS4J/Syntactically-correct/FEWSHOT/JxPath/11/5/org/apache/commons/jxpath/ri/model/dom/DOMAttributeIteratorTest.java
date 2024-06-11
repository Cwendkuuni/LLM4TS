package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator_EmptyNode() {
        NodePointer parentPointer = new DOMPointer("", "");
        QName name = new QName("*", "");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_SingleAttribute_NoPrefix() {
        String xml = "<root attr=\"value\" />";
        NodePointer parentPointer = new DOMPointer(xml, "root");
        QName name = new QName("attr", "");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);
        DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
        assertSame(parentPointer, attributePointer.getParent());
        assertEquals("value", attributePointer.getName().getNodeValue());
    }

    @Test
    public void testDOMAttributeIterator_SingleAttribute_WithPrefix() {
        String xml = "<root xmlns:x=\"http://example.com\" x:attr=\"value\" />";
        NodePointer parentPointer = new DOMPointer(xml, "root");
        QName name = new QName("{http://example.com}attr", "x");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);
        DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
        assertSame(parentPointer, attributePointer.getParent());
        assertEquals("value", attributePointer.getName().getNodeValue());
    }

    @Test
    public void testDOMAttributeIterator_MultipleAttributes_NoPrefix() {
        String xml = "<root attr1=\"value1\" attr2=\"value2\" attr3=\"value3\" />";
        NodePointer parentPointer = new DOMPointer(xml, "root");
        QName name = new QName("*", "");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);

        List<DOMAttributePointer> attributeList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
            attributeList.add(attributePointer);
            iterator.next();
        }

        assertSame(parentPointer, attributeList.get(0).getParent());
        assertEquals("attr1", attributeList.get(0).getName().getNodeName());
        assertEquals("value1", attributeList.get(0).getName().getNodeValue());

        assertSame(parentPointer, attributeList.get(1).getParent());
        assertEquals("attr2", attributeList.get(1).getName().getNodeName());
        assertEquals("value2", attributeList.get(1).getName().getNodeValue());

        assertSame(parentPointer, attributeList.get(2).getParent());
        assertEquals("attr3", attributeList.get(2).getName().getNodeName());
        assertEquals("value3", attributeList.get(2).getName().getNodeValue());
    }

    @Test
    public void testDOMAttributeIterator_MultipleAttributes_WithPrefix() {
        String xml = "<root xmlns:x=\"http://example.com\" x:attr1=\"value1\" x:attr2=\"value2\" x:attr3=\"value3\" />";
        NodePointer parentPointer = new DOMPointer(xml, "root");
        QName name = new QName("{http://example.com}attr", "x");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);

        List<DOMAttributePointer> attributeList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
            attributeList.add(attributePointer);
            iterator.next();
        }

        assertSame(parentPointer, attributeList.get(0).getParent());
        assertEquals("attr1", attributeList.get(0).getName().getNodeName());
        assertEquals("value1", attributeList.get(0).getName().getNodeValue());

        assertSame(parentPointer, attributeList.get(1).getParent());
        assertEquals("attr2", attributeList.get(1).getName().getNodeName());
        assertEquals("value2", attributeList.get(1).getName().getNodeValue());

        assertSame(parentPointer, attributeList.get(2).getParent());
        assertEquals("attr3", attributeList.get(2).getName().getNodeName());
        assertEquals("value3", attributeList.get(2).getName().getNodeValue());
    }

    @Test
    public void testDOMAttributeIterator_NoMatchingAttributes_NoPrefix() {
        String xml = "<root attr1=\"value1\" attr2=\"value2\" />";
        NodePointer parentPointer = new DOMPointer(xml, "root");
        QName name = new QName("attr3", "");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_NoMatchingAttributes_WithPrefix() {
        String xml = "<root xmlns:x=\"http://example.com\" attr1=\"value1\" attr2=\"value2\" />";
        NodePointer parentPointer = new DOMPointer(xml, "root");
        QName name = new QName("{http://example.com}attr3", "x");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_Looping() {
        String xml = "<root attr1=\"value1\" attr2=\"value2\" attr1=\"value3\" />";
        NodePointer parentPointer = new DOMPointer(xml, "root");
        QName name = new QName("attr1", "");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);

        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }

        assertEquals(2, count);
    }

    @Test
    public void testDOMAttributeIterator_LoopingWithPrefix() {
        String xml = "<root xmlns:x=\"http://example.com\" x:attr1=\"value1\" x:attr1=\"value2\" />";
        NodePointer parentPointer = new DOMPointer(xml, "root");
        QName name = new QName("{http://example.com}attr1", "x");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);

        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }

        assertEquals(2, count);
    }
}