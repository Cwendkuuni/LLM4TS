package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator_ElementWithAttribute() {
        Element element = createTestElement("test", "data", "attr1", "value1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("attr1"));

        Attr expectedAttribute = iterator.getNodePointer().getNode();
        assertEquals("value1", expectedAttribute.getNodeValue());
        assertEquals(1, iterator.getPosition());

        assertEquals(expectedAttribute, iterator.getNodePointer().getNode());
        assertFalse(iterator.setPosition(2));
        assertEquals(1, iterator.getPosition());

        Attr nextAttribute = iterator.getNodePointer().getNode();
        assertNull(nextAttribute);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_ElementWithMultipleAttributes() {
        Element element = createTestElement("test", null, "attr1", "value1", "attr2", "value2");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("*"));

        List<Attr> expectedAttributes = new ArrayList<>();
        expectedAttributes.add((Attr) iterator.getNodePointer().getNode());
        expectedAttributes.add((Attr) iterator.next().getNodePointer().getNode());

        assertEquals(expectedAttributes.size(), iterator.size());
        for (Attr attribute : expectedAttributes) {
            assertEquals(attribute.getValue(), iterator.next().getNode().getNodeValue());
        }
    }

    @Test
    public void testDOMAttributeIterator_ElementWithNoAttributes() {
        Element element = createTestElement("test", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("*"));

        assertFalse(iterator.hasNext());
        assertNull(iterator.next().getNodePointer().getNode());
    }

    @Test
    public void testDOMAttributeIterator_ElementWithNoMatchingAttribute() {
        Element element = createTestElement("test", null, "attr1", "value1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("attr3"));

        assertFalse(iterator.hasNext());
        assertNull(iterator.next().getNodePointer().getNode());
    }

    private Node createTestElement(String tagName, String namespaceURI, String... attributeNamesAndValues) {
        Element element = new Element(tagName);
        if (namespaceURI != null) {
            element.setNamespaceURI(namespaceURI);
        }
        for (String attributeNameAndValue : attributeNamesAndValues) {
            String[] parts = attributeNameAndValue.split(" ", 2);
            if (parts.length == 1) {
                element.setAttribute(parts[0], parts[0]);
            } else {
                String prefix = parts[0];
                String localName = parts[1];
                element.setAttributeNS(prefix, localName, parts[1]);
            }
        }
        return element;
    }
}