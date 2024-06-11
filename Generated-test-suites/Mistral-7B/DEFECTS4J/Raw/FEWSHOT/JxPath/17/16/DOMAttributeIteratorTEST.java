package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator_NullParent() {
        assertNull(new DOMAttributeIterator(null, new QName("")));
    }

    @Test
    public void testDOMAttributeIterator_EmptyElement() {
        Node dummyNode = new Element("dummy") {
            {
            }
        };
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(dummyNode), new QName(""));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_EmptyAttributeList() {
        Node dummyElement = new Element("dummy") {
            {
            }
        };
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(dummyElement), new QName(""));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_SimpleAttribute() {
        Node dummyElement = new Element("dummy") {
            {
                setAttribute("test", "testValue");
            }
        };
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(dummyElement), new QName("test"));
        assertTrue(iterator.hasNext());
        DOMAttributePointer next = (DOMAttributePointer) iterator.next();
        assertEquals("testValue", next.getValue().getValue());
    }

    @Test
    public void testDOMAttributeIterator_EmptyNamespaceAttribute() {
        Node dummyElement = new Element("dummy") {
            {
                setAttribute("xml:test", "testValue");
            }
        };
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(dummyElement), new QName("xml:test"));
        assertTrue(iterator.hasNext());
        DOMAttributePointer next = (DOMAttributePointer) iterator.next();
        assertEquals("testValue", next.getValue().getValue());
    }

    @Test
    public void testDOMAttributeIterator_NamespaceAttribute() {
        String testNamespaceURI = "http://example.com/test";
        Node dummyElement = new Element("dummy") {
            {
                setAttributeNS("http://example.com/test", "test", "testValue");
            }
        };
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(dummyElement), new QName("test", testNamespaceURI));
        assertTrue(iterator.hasNext());
        DOMAttributePointer next = (DOMAttributePointer) iterator.next();
        assertEquals("testValue", next.getValue().getValue());
    }

    @Test
    public void testDOMAttributeIterator_MismatchedNamespaceAttribute() {
        String testNamespaceURI = "http://example.com/test";
        Node dummyElement = new Element("dummy") {
            {
                setAttributeNS("http://example2.com/test", "test", "testValue");
            }
        };
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(dummyElement), new QName("test", testNamespaceURI));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_WildcardNamespaceAttribute() {
        String testNamespaceURI = "http://example.com/*";
        Node dummyElement = new Element("dummy") {
            {
                setAttributeNS("http://example.com/test", "test", "testValue");
            }
        };
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(dummyElement), new QName(null, testNamespaceURI));
        assertTrue(iterator.hasNext());
        DOMAttributePointer next = (DOMAttributePointer) iterator.next();
        assertEquals("testValue", next.getValue().getValue());
    }

    @Test
    public void testDOMAttributeIterator_MismatchedLocalNameAttribute() {
        Node dummyElement = new Element("dummy") {
            {
                setAttribute("test2", "testValue");
            }
        };
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(dummyElement), new QName("test", null));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_WildcardLocalNameAttribute() {
        Node dummyElement = new Element("dummy") {
            {
                setAttribute("test", "testValue");
                setAttribute("test2", "testValue2");
            }
        };
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(dummyElement), new QName(null, null));
        assertTrue(iterator.hasNext());
        DOMAttributePointer next = (DOMAttributePointer) iterator.next();
        assertEquals("testValue", next.getValue().getValue());
    }

    @Test
    public void testDOMAttributeIterator_IteratorOrder() {
        Node dummyElement = new Element("dummy") {
            {
                setAttribute("test3", "testValue3");
                setAttribute("test2", "testValue2");
                setAttribute("test", "testValue");
            }
        };
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(dummyElement), new QName(null, null));
        List<String> attributeValues = new ArrayList<>();
        while (iterator.hasNext()) {
            DOMAttributePointer next = (DOMAttributePointer) iterator.next();
            attributeValues.add(next.getValue().getValue());
        }
        assertEquals("testValue", attributeValues.get(0));
        assertEquals("testValue2", attributeValues.get(1));
        assertEquals("testValue3", attributeValues.get(2));
    }

    @Test
    public void testDOMAttributeIterator_EmptyNamespaceAndLocalNameAttribute() {
        Node dummyElement = new Element("dummy") {
            {
                setAttribute("xml:test", "testValue");
            }
        };
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(dummyElement), new QName(""));
        assertTrue(iterator.hasNext());
        DOMAttributePointer next = (DOMAttributePointer) iterator.next();
        assertEquals("testValue", next.getValue().getValue());
    }
}