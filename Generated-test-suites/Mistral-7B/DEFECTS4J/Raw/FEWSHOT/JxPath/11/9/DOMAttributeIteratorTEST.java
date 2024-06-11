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
    public void testDOMAttributeIterator_ElementWithSpecificAttribute() {
        Element element = createTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("test"));

        assertNotNull(iterator.getNodePointer());
        assertEquals("testValue", iterator.getNodePointer().getNodeValue());
        assertNull(iterator.getNodePointer().getNextSibling());

        iterator.setPosition(1);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_ElementWithMultipleAttributes() {
        Element element = createTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("*"));

        assertNotNull(iterator.getNodePointer());
        assertEquals("testAttribute1", iterator.getNodePointer().getNodeValue());
        iterator.next();
        assertEquals("testAttribute2", iterator.getNodePointer().getNodeValue());
        iterator.next();
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_ElementWithNoAttributes() {
        Element element = createTestElementNoAttributes();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("*"));

        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_NullParent() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, new QName("*"));

        assertNull(iterator);
    }

    @Test
    public void testDOMAttributeIterator_NullName() {
        NodePointer parent = new DOMPointer(createTestElement());
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, null);

        assertNull(iterator);
    }

    private Element createTestElement() {
        Element element = DocumentBuilderFactory.newInstance().newDocumentBuilder().newElement("test");
        element.setAttribute("testAttribute1", "testValue");
        element.setAttribute("testAttribute2", "testValue2");
        return element;
    }

    private Element createTestElementNoAttributes() {
        Element element = DocumentBuilderFactory.newInstance().newDocumentBuilder().newElement("test");
        return element;
    }
}