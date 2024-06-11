package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.w3c.dom.Attribute;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator_ElementWithSingleAttribute() {
        Element element = createTestElement("testElement", "testNamespaceURI", "testPrefix");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("test:attribute"));

        assertNotNull(iterator.getNodePointer());
        assertEquals("testAttribute", iterator.getNodePointer().getName().getName());
        assertNotNull(iterator.getNodePointer().getNode());
        assertEquals("testvalue", ((Attribute) iterator.getNodePointer().getNode()).getNodeValue());
    }

    @Test
    public void testDOMAttributeIterator_ElementWithMultipleAttributes() {
        Element element = createTestElement("testElement", "testNamespaceURI", "testPrefix");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("*"));

        assertNotNull(iterator.getNodePointer());
        for (int i = 0; i < iterator.getSize(); i++) {
            Attribute attribute = (Attribute) iterator.getNodePointer().getNode();
            assertEquals(null, attribute.getNodeValue() == null ? "" : attribute.getNodeValue(), "Attribute value " + i);
        }
    }

    @Test
    public void testDOMAttributeIterator_ElementWithNoMatchingAttributes() {
        Element element = createTestElement("testElement", "testNamespaceURI", "testPrefix");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("nonMatchingAttribute"));

        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_ElementWithNamespaces() {
        Element element = createTestElementWithNamespaces("testElement", "testNamespaceURI1", "testPrefix1", "testNamespaceURI2", "testPrefix2");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("testPrefix1:attribute"));

        assertNotNull(iterator.getNodePointer());
        assertEquals("attribute", iterator.getNodePointer().getName().getName());
        assertNotNull(iterator.getNodePointer().getNode());
        assertEquals("testvalue1", ((Attribute) iterator.getNodePointer().getNode()).getNodeValue());

        iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("testPrefix2:attribute"));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_ElementWithFiltering() {
        Element element = createTestElementWithFiltering("testElement", "testNamespaceURI", "testPrefix");
        QName name = new QName("test:attribute");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), name);

        assertNull(iterator.getNodePointer());

        iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("*"));

        int position = 0;
        for (NodePointer nodePointer = iterator.getNext(); nodePointer != null; nodePointer = iterator.getNext()) {
            Attribute attribute = (Attribute) nodePointer.getNode();
            if (testAttr(attribute)) {
                assertEquals("attribute" + position++, nodePointer.getName().getName(), attribute.getNodeName());
                assertEquals("value" + position++, attribute.getNodeValue(), nodePointer.getNode().getNodeValue());
            }
        }
    }

    private Element createTestElement(String tagName, String namespaceURI, String prefix) {
        Element element = createTestElementWithAttributes(tagName, namespaceURI, prefix, "attribute1", "value1");
        return element;
    }

    private Element createTestElementWithNamespaces(String tagName, String namespaceURI1, String prefix1, String namespaceURI2, String prefix2) {
        Element element = createTestElementWithAttributes(tagName, namespaceURI1, prefix1, "attribute1", "value1");
        element.setNamespaceURI(namespaceURI2);
        element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + prefix2, namespaceURI2);
        element.setAttributeNS("xmlns:" + prefix1, "attribute2", "value2");
        return element;
    }

    private Element createTestElementWithFiltering(String tagName, String namespaceURI, String prefix) {
        Element element = createTestElementWithAttributes(tagName, namespaceURI, prefix, "attribute1", "value1", "attribute2", "value2", "attribute3", "value3");
        return element;
    }

    private Element createTestElementWithAttributes(String tagName, String namespaceURI, String prefix, String... attributeNamesAndValues) {
        Element element = new Element(tagName);
        element.setNamespaceURI(namespaceURI);
        if (prefix != null) {
            element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + prefix, namespaceURI);
        }
        for (String nameAndValue : attributeNamesAndValues) {
            String[] parts = nameAndValue.split(" ", 2);
            String name = parts[0];
            String value = parts[1];
            if (prefix != null) {
                element.setAttributeNS("xmlns:" + prefix, name, value);
            } else {
                element.setAttribute(name, value);
            }
        }
        return element;
    }

    private boolean testAttr(Attribute attribute) {
        String nodePrefix = attribute.getNamespaceURI().equals("") ? null : DOMNodePointer.getPrefix(attribute);
        String nodeLocalName = attribute.getNodeName().substring(nodePrefix != null ? nodePrefix.length() + 1 : 0);

        if (nodePrefix != null && nodePrefix.equals("xmlns")) {
            return false;
        }

        if (nodePrefix == null && nodeLocalName.equals("xmlns")) {
            return false;
        }

        String testLocalName = name.getName();
        if (testLocalName.equals("*") || testLocalName.equals(nodeLocalName)) {
            String testPrefix = name.getPrefix();

            if (equalStrings(testPrefix, nodePrefix)) {
                return true;
            }
            String testNS = null;
            if (testPrefix != null) {
                testNS = parent.getNamespaceURI(testPrefix);
            }
            String nodeNS = null;
            if (nodePrefix != null) {
                nodeNS = parent.getNamespaceURI(nodePrefix);
            }
            return equalStrings(testNS, nodeNS);
        }
        return false;
    }
}