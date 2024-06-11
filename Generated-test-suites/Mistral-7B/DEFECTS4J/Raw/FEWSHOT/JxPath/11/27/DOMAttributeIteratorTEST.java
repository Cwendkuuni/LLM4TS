package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator() {
        Element element = createTestElement();
        QName name = new QName("test", null, null);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointers.RootNodePointer(element), name);
        assertNotNull(iterator);
        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());

        Attr attribute1 = element.setAttributeNode(createTestAttribute("attr1", "value1", null));
        DOMAttributeIterator iterator1 = new DOMAttributeIterator(new DOMPointers.RootNodePointer(element), name);
        assertNotNull(iterator1);
        assertEquals(0, iterator1.getPosition());
        assertNull(iterator1.getNodePointer());

        iterator1.next();
        assertEquals(1, iterator1.getPosition());
        assertNotNull(iterator1.getNodePointer());
        assertEquals("attr1", iterator1.getNodePointer().getAttributes().getName().getName());
        assertEquals("value1", iterator1.getNodePointer().getAttributes().getValue());

        iterator.next();
        assertEquals(1, iterator.getPosition());
        assertNotNull(iterator.getNodePointer());
        assertEquals("attr1", iterator.getNodePointer().getAttributes().getName().getName());
        assertEquals("value1", iterator.getNodePointer().getAttributes().getValue());

        Attr attribute2 = element.setAttributeNode(createTestAttribute("attr2", "value2", null));
        iterator1.next();
        assertEquals(2, iterator1.getPosition());
        assertNotNull(iterator1.getNodePointer());
        assertEquals("attr2", iterator1.getNodePointer().getAttributes().getName().getName());
        assertEquals("value2", iterator1.getNodePointer().getAttributes().getValue());

        iterator.next();
        assertEquals(2, iterator.getPosition());
        assertNotNull(iterator.getNodePointer());
        assertEquals("attr2", iterator.getNodePointer().getAttributes().getName().getName());
        assertEquals("value2", iterator.getNodePointer().getAttributes().getValue());

        iterator1.next();
        assertEquals(3, iterator1.getPosition());
        assertNull(iterator1.getNodePointer());

        iterator.next();
        assertEquals(3, iterator.getPosition());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorWithNamespace() {
        Element element = createTestElementWithNamespace();
        QName name = new QName("test:test", null, "http://example.com");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointers.RootNodePointer(element), name);
        assertNotNull(iterator);
        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());

        Attr attribute1 = element.setAttributeNodeNS(createTestAttributeNS("attr1", "value1", null, "http://example.com"));
        DOMAttributeIterator iterator1 = new DOMAttributeIterator(new DOMPointers.RootNodePointer(element), name);
        assertNotNull(iterator1);
        assertEquals(0, iterator1.getPosition());
        assertNull(iterator1.getNodePointer());

        iterator1.next();
        assertEquals(1, iterator1.getPosition());
        assertNotNull(iterator1.getNodePointer());
        assertEquals("attr1", iterator1.getNodePointer().getAttributes().getName().getName());
        assertEquals("value1", iterator1.getNodePointer().getAttributes().getValue());
        assertEquals("http://example.com", iterator1.getNodePointer().getAttributes().getNamespaceURI());

        iterator.next();
        assertEquals(1, iterator.getPosition());
        assertNotNull(iterator.getNodePointer());
        assertEquals("attr1", iterator.getNodePointer().getAttributes().getName().getName());
        assertEquals("value1", iterator.getNodePointer().getAttributes().getValue());
        assertEquals("http://example.com", iterator.getNodePointer().getAttributes().getNamespaceURI());

        Attr attribute2 = element.setAttributeNodeNS(createTestAttributeNS("attr2", "value2", null, "http://example.com"));
        iterator1.next();
        assertEquals(2, iterator1.getPosition());
        assertNotNull(iterator1.getNodePointer());
        assertEquals("attr2", iterator1.getNodePointer().getAttributes().getName().getName());
        assertEquals("value2", iterator1.getNodePointer().getAttributes().getValue());
        assertEquals("http://example.com", iterator1.getNodePointer().getAttributes().getNamespaceURI());

        iterator.next();
        assertEquals(2, iterator.getPosition());
        assertNotNull(iterator.getNodePointer());
        assertEquals("attr2", iterator.getNodePointer().getAttributes().getName().getName());
        assertEquals("value2", iterator.getNodePointer().getAttributes().getValue());
        assertEquals("http://example.com", iterator.getNodePointer().getAttributes().getNamespaceURI());

        iterator1.next();
        assertEquals(3, iterator1.getPosition());
        assertNull(iterator1.getNodePointer());

        iterator.next();
        assertEquals(3, iterator.getPosition());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorWithWildcardName() {
        Element element = createTestElement();
        QName name = new QName("*", null, null);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointers.RootNodePointer(element), name);
        assertNotNull(iterator);
        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());

        Attr attribute1 = element.setAttributeNode(createTestAttribute("attr1", "value1", null));
        iterator.next();
        assertEquals(1, iterator.getPosition());
        assertNotNull(iterator.getNodePointer());
        assertEquals("attr1", iterator.getNodePointer().getAttributes().getName().getName());
        assertEquals("value1", iterator.getNodePointer().getAttributes().getValue());

        Attr attribute2 = element.setAttributeNode(createTestAttribute("attr2", "value2", null));
        iterator.next();
        assertEquals(2, iterator.getPosition());
        assertNotNull(iterator.getNodePointer());
        assertEquals("attr2", iterator.getNodePointer().getAttributes().getName().getName());
        assertEquals("value2", iterator.getNodePointer().getAttributes().getValue());

        iterator.next();
        assertEquals(3, iterator.getPosition());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorWithInvalidName() {
        Element element = createTestElement();
        QName name = new QName("xmlns:test", null, null);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointers.RootNodePointer(element), name);
        assertNull(iterator);

        Attr attribute1 = element.setAttributeNode(createTestAttribute("attr1", "value1", null));
        iterator = new DOMAttributeIterator(new DOMPointers.RootNodePointer(element), name);
        assertNull(iterator);
    }

    @Test
    public void testDOMAttributeIteratorWithNullName() {
        Element element = createTestElement();
        QName name = null;

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointers.RootNodePointer(element), name);
        assertNull(iterator);
    }

    @Test
    public void testDOMAttributeIteratorWithEmptyName() {
        Element element = createTestElement();
        QName name = new QName("", null, null);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointers.RootNodePointer(element), name);
        assertNull(iterator);
    }

    private Element createTestElement() {
        Element element = DOM.createElement("root");
        element.setAttributeNode(DOM.createAttribute("attr1"));
        element.setAttributeNode(DOM.createAttribute("attr2"));
        return element;
    }

    private Element createTestElementWithNamespace() {
        Element element = DOM.createElementNS("http://example.com", "root");
        element.setAttributeNodeNS(DOM.createAttributeNS("http://example.com", "attr1"));
        element.setAttributeNodeNS(DOM.createAttributeNS("http://example.com", "attr2"));
        return element;
    }

    private Attr createTestAttribute(String localName, String value, String prefix) {
        Attr attr = DOM.createAttribute(localName);
        attr.setValue(value);
        if (prefix != null) {
            DOM.setNamespaceURI(attr, prefix);
        }
        return attr;
    }

    private Attr createTestAttributeNS(String localName, String value, String prefix, String namespaceURI) {
        Attr attr = DOM.createAttributeNS(namespaceURI, localName);
        attr.setValue(value);
        if (prefix != null) {
            DOM.setPrefix(attr, prefix);
        }
        return attr;
    }
}