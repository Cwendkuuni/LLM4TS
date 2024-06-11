package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator_SingleAttribute() {
        Element element = createElement("root", "xmlns:p prefix1", "xmlns:q prefix2");
        element.setAttribute("attr1", "value1");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("attr1"));

        assertEquals(element.getAttributeNode("attr1"), iterator.getNodePointer().getNode());
        assertTrue(iterator.setPosition(1));
        assertNull(iterator.getNodePointer().getNode());
    }

    @Test
    public void testDOMAttributeIterator_MultipleAttributes() {
        Element element = createElement("root", "xmlns:p prefix1", "xmlns:q prefix2");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("*"));

        assertEquals(iterator.getNodePointer().getNode(), element.getAttributeNode("attr1"));
        assertTrue(iterator.setPosition(1));
        assertEquals(iterator.getNodePointer().getNode(), element.getAttributeNode("attr2"));
        assertTrue(iterator.setPosition(2));
        assertNull(iterator.getNodePointer().getNode());
    }

    @Test
    public void testDOMAttributeIterator_NamespaceAttribute() {
        Element element = createElement("root", "xmlns:p prefix1", "xmlns:q prefix2");
        element.setAttributeNS("xmlns:x", "x:attr1", "value1");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("xmlns:x:attr1"));

        assertEquals(iterator.getNodePointer().getNode(), element.getAttributeNodeNS("xmlns:x", "attr1"));
        assertTrue(iterator.setPosition(1));
        assertNull(iterator.getNodePointer().getNode());
    }

    @Test
    public void testDOMAttributeIterator_NoMatchingAttribute() {
        Element element = createElement("root", "xmlns:p prefix1", "xmlns:q prefix2");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("nonExistentAttr"));

        assertNull(iterator.getNodePointer().getNode());
        assertTrue(iterator.setPosition(1));
        assertNull(iterator.getNodePointer().getNode());
    }

    private Element createElement(String tagName, String... prefixAttributes) {
        Element element = document.createElement(tagName);
        for (String attribute : prefixAttributes) {
            String[] parts = attribute.split(":");
            String prefix = parts[0];
            String localName = parts[1];
            String value = parts.length > 2 ? parts[2] : "";
            element.setAttributeNS(prefix, localName, value);
        }
        return element;
    }

}