package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator_Empty() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(), null);
        assertNull(iterator.getNodePointer());
        assertTrue(iterator.getPosition() == 0);
        assertFalse(iterator.setPosition(1));
    }

    @Test
    public void testDOMAttributeIterator_SingleAttribute() {
        Element element = createMockElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("test"));

        Attr testAttr = createMockAttribute("test", "value");
        element.setAttributeNode(testAttr);

        assertSame(testAttr, iterator.getNodePointer().getNode());
        assertTrue(iterator.getPosition() == 1);
        assertTrue(iterator.setPosition(0));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_MultipleAttributes() {
        Element element = createMockElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("test"));

        Attr test1Attr = createMockAttribute("test1", "value1");
        Attr test2Attr = createMockAttribute("test2", "value2");
        Attr test3Attr = createMockAttribute("test3", "value3");
        element.setAttributeNode(test1Attr);
        element.setAttributeNode(test2Attr);
        element.setAttributeNode(test3Attr);

        for (int i = 0; i < 3; i++) {
            Attr attr = (Attr) iterator.getNodePointer().getNode();
            assertTrue(equalStrings(attr.getName().getName(), "test" + (i + 1)));
            iterator.next();
        }

        assertNull(iterator.getNodePointer());
        assertTrue(iterator.getPosition() == 0);
    }

    @Test
    public void testDOMAttributeIterator_MultipleNamespaces() {
        Element element = createMockElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("{http://test.com}test"));

        Attr attr1 = createMockAttribute("{http://test.com}test1", "value1");
        Attr attr2 = createMockAttribute("test2", "value2");
        element.setAttributeNodeNS(attr1, "http://test.com", "test1");
        element.setAttributeNode(attr2);

        for (int i = 0; i < 2; i++) {
            Attr attr = (Attr) iterator.getNodePointer().getNode();
            if (i == 0) {
                assertTrue(equalStrings(attr.getNodeValue(), "value1"));
                assertTrue(equalStrings(attr.getNamespaceURI(), "http://test.com"));
            } else {
                assertSame(attr2, iterator.getNodePointer().getNode());
            }
            iterator.next();
        }

        assertNull(iterator.getNodePointer());
        assertTrue(iterator.getPosition() == 0);
    }

    @Test
    public void testDOMAttributeIterator_NullElement() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(null), new QName("test"));
        assertNull(iterator.getNodePointer());
        assertTrue(iterator.getPosition() == 0);
    }

    private Element createMockElement() {
        Element element = mock(Element.class);
        when(element.getAttributeNodes()).thenReturn(mock(NamedNodeMap.class));
        when(element.getAttributeNodeNS(anyString(), anyString())).thenReturn((Attr) anyObject());
        when(element.getAttributeNode(anyString())).thenReturn((Attr) anyObject());
        return element;
    }

    private Attr createMockAttribute(String localName, String value) {
        Attr attr = mock(Attr.class);
        when(attr.getName()).thenReturn(new QName(null, localName));
        when(attr.getNodeValue()).thenReturn(value);
        return attr;
    }

    private static boolean equalStrings(String s1, String s2) {
        return s1 == s2 || s1 != null && s1.equals(s2);
    }
}