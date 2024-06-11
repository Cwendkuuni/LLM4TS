package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.names.QName;

public class DOMAttributeIteratorTest {
    @Test
    public void testDOMAttributeIterator() {
        Element element = createTestElement();

        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(new DOMNodePointer(element), new QName("*"));
        assertNull(attributeIterator.getNodePointer());
        assertTrue(attributeIterator.hasNext());

        DOMAttributePointer attributePointer = (DOMAttributePointer) attributeIterator.next();
        assertNotNull(attributePointer.getNode());
        assertNotNull(attributePointer.getNodeName());
        assertEquals("id", attributePointer.getNodeName().getLocalPart());
        assertEquals("xmlns:xsi", attributePointer.getNodeName().getPrefix());
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", attributePointer.getNodeName().getNamespaceURI());

        attributeIterator = new DOMAttributeIterator(new DOMNodePointer(element), new QName("xmlns:x"));
        assertNull(attributeIterator.getNodePointer());
        assertFalse(attributeIterator.hasNext());

        attributeIterator = new DOMAttributeIterator(new DOMNodePointer(element), new QName("xmlns", "http://www.w3.org/2000/xmlns/"));
        assertNull(attributeIterator.getNodePointer());
        assertTrue(attributeIterator.hasNext());

        attributePointer = (DOMAttributePointer) attributeIterator.next();
        assertNotNull(attributePointer.getNode());
        assertNotNull(attributePointer.getNodeName());
        assertEquals("xmlns:xsi", attributePointer.getNodeName().getLocalPart());
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", attributePointer.getNodeName().getNamespaceURI());

        attributeIterator = new DOMAttributeIterator(new DOMNodePointer(element), new QName("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance"));
        assertNull(attributeIterator.getNodePointer());
        assertFalse(attributeIterator.hasNext());

        attributeIterator = new DOMAttributeIterator(new DOMNodePointer(element), new QName("foo"));
        assertNull(attributeIterator.getNodePointer());
        assertFalse(attributeIterator.hasNext());
    }

    private Element createTestElement() {
        Element element = (Element) DOM.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root xmlns:x=\"http://www.example.com/xsi\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><child id=\"1\"/></root>").getDocumentElement();
        return element;
    }
}

class AssertNotNull {
    public static void assertNotNull(Object object) {
        if (object == null) {
            throw new AssertionError();
        }
    }
}

class AssertFalse {
    public static void assertFalse(boolean condition) {
        if (condition) {
            throw new AssertionError();
        }
    }
}

class AssertTrue {
    public static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError();
        }
    }
}