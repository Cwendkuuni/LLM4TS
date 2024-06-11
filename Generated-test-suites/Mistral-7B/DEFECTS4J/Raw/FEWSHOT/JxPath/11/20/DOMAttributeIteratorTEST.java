package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeIterator;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator() {
        Element element = createElement("root", "namespace1");
        Attr attribute1 = createAttribute("attr1", "namespace1", "value1");
        Attr attribute2 = createAttribute("attr2", null, "value2");
        element.setAttributeNode(attribute1);
        element.setAttributeNode(attribute2);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("*"));
        assertNotNull(iterator);

        List<Attr> attributes = iterator.getAttributes();
        assertEquals(2, attributes.size());
        assertNotNull(attributes.get(0));
        assertEquals("attr1", attributes.get(0).getNodeName());
        assertEquals("value1", attributes.get(0).getValue());

        assertNotNull(attributes.get(1));
        assertEquals("attr2", attributes.get(1).getNodeName());
        assertEquals("value2", attributes.get(1).getValue());

        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals(attribute1, nodePointer.getNode());

        iterator.setPosition(1);
        nodePointer = iterator.getNodePointer();
        assertEquals(attribute2, nodePointer.getNode());
    }

    @Test
    public void testDOMAttributeIteratorWithPrefix() {
        Element element = createElement("root", "namespace1");
        Attr attribute1 = createAttribute("xmlns:p1", null, "prefix1");
        Attr attribute2 = createAttribute("attr1", "namespace1", "value1");
        element.setAttributeNode(attribute1);
        element.setAttributeNode(attribute2);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("p1:attr1"));
        assertNotNull(iterator);

        List<Attr> attributes = iterator.getAttributes();
        assertEquals(1, attributes.size());
        assertNotNull(attributes.get(0));
        assertEquals("attr1", attributes.get(0).getNodeName());
        assertEquals("value1", attributes.get(0).getValue());

        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals(attribute2, nodePointer.getNode());
    }

    @Test
    public void testDOMAttributeIteratorWithNamespace() {
        Element element = createElement("root", "namespace1");
        Attr attribute1 = createAttribute("attr1", null, "value1");
        Attr attribute2 = createAttribute("attr2", "namespace2", "value2");
        element.setAttributeNode(attribute1);
        element.setAttributeNode(attribute2);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("*", "namespace2"));
        assertNotNull(iterator);

        List<Attr> attributes = iterator.getAttributes();
        assertEquals(1, attributes.size());
        assertNotNull(attributes.get(0));
        assertEquals("attr2", attributes.get(0).getNodeName());
        assertEquals("value2", attributes.get(0).getValue());

        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals(attribute2, nodePointer.getNode());
    }

    @Test
    public void testDOMAttributeIteratorEmpty() {
        Element element = createElement("root", "namespace1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("*"));
        assertNotNull(iterator);
        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer().getNode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDOMAttributeIteratorNullParent() {
        new DOMAttributeIterator(null, new QName("*"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDOMAttributeIteratorNullName() {
        new DOMAttributeIterator(new DOMPointer(createElement("root", "namespace1")), null);
    }

    private static Element createElement(String tag, String namespace) {
        Element element = document.createElement(tag);
        element.setNamespaceURI(namespace);
        return element;
    }

    private static Attr createAttribute(String localName, String namespace, String value) {
        Attr attribute = document.createAttribute(localName);
        if (namespace != null) {
            attribute.setNamespaceURI(namespace);
        }
        attribute.setValue(value);
        return attribute;
    }

    private static Document document = new DocumentImpl();

}