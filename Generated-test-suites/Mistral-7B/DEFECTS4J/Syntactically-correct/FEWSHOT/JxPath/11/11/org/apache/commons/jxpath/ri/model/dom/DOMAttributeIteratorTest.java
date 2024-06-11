package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIteratorEmpty() {
        NodePointer parent = new DOMPointer("", "");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, null);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorSingleAttribute() {
        Element element = new org.apache.commons.jxpath.ri.model.DOMElement("root", null, null);
        Attr attribute = new org.w3c.dom.Attr();
        attribute.setName(new QName("test"));
        attribute.setValue("testValue");
        element.setAttributeNode(attribute);
        NodePointer parent = new DOMPointer(element, "");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test"));
        DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
        assertNotNull(attributePointer);
        assertEquals("test", attributePointer.getName().getName());
        assertEquals("testValue", attributePointer.getNodeValue());
    }

    @Test
    public void testDOMAttributeIteratorMultipleAttributes() {
        Element element = new org.apache.commons.jxpath.ri.model.DOMElement("root", null, null);
        Attr attribute1 = new org.w3c.dom.Attr();
        attribute1.setName(new QName("test1"));
        attribute1.setValue("testValue1");
        Attr attribute2 = new org.w3c.dom.Attr();
        attribute2.setName(new QName("test2"));
        attribute2.setValue("testValue2");
        element.setAttributeNode(attribute1);
        element.setAttributeNode(attribute2);

        NodePointer parent = new DOMPointer(element, "");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test1"));
        DOMAttributePointer attributePointer1 = (DOMAttributePointer) iterator.getNodePointer();
        assertNotNull(attributePointer1);
        assertEquals("test1", attributePointer1.getName().getName());
        assertEquals("testValue1", attributePointer1.getNodeValue());

        iterator = new DOMAttributeIterator(parent, new QName("test2"));
        DOMAttributePointer attributePointer2 = (DOMAttributePointer) iterator.getNodePointer();
        assertNotNull(attributePointer2);
        assertEquals("test2", attributePointer2.getName().getName());
        assertEquals("testValue2", attributePointer2.getNodeValue());

        iterator = new DOMAttributeIterator(parent, new QName("test3"));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorNamespace() {
        Element element = new org.apache.commons.jxpath.ri.model.DOMElement("root", null, null);
        Attr attribute = new org.w3c.dom.Attr();
        attribute.setName(new QName("{http://test.com}test"));
        attribute.setValue("testValue");
        element.setAttributeNodeNS(attribute, "http://test.com");

        NodePointer parent = new DOMPointer(element, "http://test.com");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("{http://test.com}test"));
        DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
        assertNotNull(attributePointer);
        assertEquals("{http://test.com}test", attributePointer.getName().getName());
        assertEquals("testValue", attributePointer.getNodeValue());
    }

    @Test
    public void testDOMAttributeIteratorNullParent() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, new QName("test"));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorNullName() {
        NodePointer parent = new DOMPointer(new org.apache.commons.jxpath.ri.model.DOMElement("root", null, null), "");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, null);
        assertNull(iterator.getNodePointer());
    }
}