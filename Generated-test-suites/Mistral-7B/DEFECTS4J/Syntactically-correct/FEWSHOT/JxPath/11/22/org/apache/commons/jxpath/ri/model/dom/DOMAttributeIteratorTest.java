package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {
    @Test
    public void testDOMAttributeIterator_EmptyNode() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, QName.createQName("", ""));
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIterator_AttributeExistence() {
        Element element = new org.w3c.dom.ElementImpl();
        element.setAttribute("testAttr", "testValue");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), QName.createQName("", "testAttr"));
        assertNotNull(iterator.getNodePointer());
        assertEquals("testValue", iterator.getNodePointer().getNodeValue());
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIterator_AttributeNonExistence() {
        Element element = new org.w3c.dom.ElementImpl();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), QName.createQName("", "nonExistentAttr"));
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIterator_MultipleAttributes() {
        Element element = new org.w3c.dom.ElementImpl();
        element.setAttribute("testAttr1", "testValue1");
        element.setAttribute("testAttr2", "testValue2");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), QName.createQName("", "testAttr1"));
        assertNotNull(iterator.getNodePointer());
        assertEquals("testValue1", iterator.getNodePointer().getNodeValue());
        assertEquals(1, iterator.getPosition());

        iterator = new DOMAttributeIterator(new DOMPoint(element), QName.createQName("", "testAttr2"));
        assertNotNull(iterator.getNodePointer());
        assertEquals("testValue2", iterator.getNodePointer().getNodeValue());
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIterator_AttributeNameMatching() {
        Element element = new org.w3c.dom.ElementImpl();
        element.setAttribute("testAttr", "testValue");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), QName.createQName("", "testAttr"));
        assertNotNull(iterator.getNodePointer());
        assertEquals("testValue", iterator.getNodePointer().getNodeValue());
        assertEquals(1, iterator.getPosition());

        iterator = new DOMAttributeIterator(new DOMPoint(element), QName.createQName("", "testAttr2"));
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIterator_AttributePrefixMatching() {
        Element element = new org.w3c.dom.ElementImpl();
        element.setAttributeNS("xmlns:x", "x:testAttr", "testValue");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), QName.createQName("x", "testAttr"));
        assertNotNull(iterator.getNodePointer());
        assertEquals("testValue", iterator.getNodePointer().getNodeValue());
        assertEquals(1, iterator.getPosition());

        iterator = new DOMAttributeIterator(new DOMPoint(element), QName.createQName("x", "nonExistentAttr"));
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIterator_AttributeNSMatching() {
        Element element = new org.w3c.dom.ElementImpl();
        element.setAttributeNS("http://example.com", "prefix:testAttr", "testValue");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), QName.createQName("prefix", "testAttr"));
        assertNotNull(iterator.getNodePointer());
        assertEquals("testValue", iterator.getNodePointer().getNodeValue());
        assertEquals(1, iterator.getPosition());

        iterator = new DOMAttributeIterator(new DOMPoint(element), QName.createQName("prefix", "nonExistentAttr"));
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIterator_AttributeNSNoPrefix() {
        Element element = new org.w3c.dom.ElementImpl();
        element.setAttributeNS("http://example.com", "testAttr", "testValue");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), QName.createQName("", "testAttr"));
        assertNotNull(iterator.getNodePointer());
        assertEquals("testValue", iterator.getNodePointer().getNodeValue());
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIterator_AttributeNSNoPrefixNoNamespace() {
        Element element = new org.w3c.dom.ElementImpl();
        element.setAttribute("testAttr", "testValue");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), QName.createQName("", "testAttr", "http://example.com"));
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }
}