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
    public void testDOMAttributeIterator_SingleAttribute() {
        Element element = createMockElement("element", "namespaceURI", "prefix");
        QName name = new QName("namespaceURI", "prefix", "*");
        NodePointer parent = new DOMPointer(createMockDocument(), element);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator.getNodePointer());
        assertEquals(name, iterator.getNodePointer().getName());
        assertEquals("element", iterator.getNodePointer().getNodeName());
        assertEquals("namespaceURI:prefix", iterator.getNodePointer().toString());
    }

    @Test
    public void testDOMAttributeIterator_MultipleAttributes() {
        Element element = createMockElement("element", "namespaceURI", "prefix");
        QName name = new QName("namespaceURI", "*", "*");
        NodePointer parent = new DOMPointer(createMockDocument(), element);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator.getNodePointer());
        assertEquals(name, iterator.getNodePointer().getName());
        assertEquals("element", iterator.getNodePointer().getNodeName());
        assertEquals("namespaceURI:attribute1 namespaceURI:attribute2", iterator.getNodePointer().toString());

        for (int i = 0; i < 5; i++) {
            iterator.next();
            assertNotNull(iterator.getNodePointer());
        }
    }

    @Test
    public void testDOMAttributeIterator_NoMatchingAttributes() {
        Element element = createMockElement("element", "namespaceURI", "prefix");
        QName name = new QName("namespaceURI", "nonmatchingName", "*");
        NodePointer parent = new DOMPointer(createMockDocument(), element);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        for (int i = 0; i < 5; i++) {
            iterator.next();
            assertNull(iterator.getNodePointer());
        }
    }

    @Test
    public void testDOMAttributeIterator_AttributeWithDifferentPrefix() {
        Element parentElement = createMockElement("parent", "namespaceURI1", "prefix1");
        Element childElement = createMockElement("child", "namespaceURI2", "prefix2");
        childElement.setAttributeNodeNS(childElement.createAttribute("data1"), "namespaceURI1", "data");
        parentElement.appendChild(childElement);
        NodePointer parent = new DOMPointer(createMockDocument(), parentElement);
        QName name = new QName("namespaceURI1", "*", "*");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator.getNodePointer());
        assertEquals("data", iterator.getNodePointer().getName().getNodeValue());
        assertEquals("child", iterator.getNodePointer().getNodeName());
        assertEquals("namespaceURI1:data", iterator.getNodePointer().toString());
    }

    @Test
    public void testDOMAttributeIterator_AttributeWithSameNameButDifferentNS() {
        Element parentElement = createMockElement("parent", "namespaceURI1", "prefix1");
        Element childElement1 = createMockElement("child1", "namespaceURI2", "prefix2");
        childElement1.setAttributeNodeNS(childElement1.createAttribute("data1"), "namespaceURI1", "data1");
        Element childElement2 = createMockElement("child2", "namespaceURI2", "prefix2");
        childElement2.setAttributeNodeNS(childElement2.createAttribute("data2"), "namespaceURI2", "data2");
        parentElement.appendChild(childElement1);
        parentElement.appendChild(childElement2);
        NodePointer parent = new DOMPointer(createMockDocument(), parentElement);
        QName name = new QName("namespaceURI2", "*", "*");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator.getNodePointer());
        assertEquals("data2", iterator.getNodePointer().getName().getNodeValue());
        assertEquals("child2", iterator.getNodePointer().getNodeName());
        assertEquals("namespaceURI2:data2", iterator.getNodePointer().toString());
    }

    private Element createMockElement(String tagName, String namespaceURI, String prefix) {
        Element element = mock(Element.class);
        when(element.getTagName()).thenReturn(tagName);
        when(element.getNamespaceURI()).thenReturn(namespaceURI);
        when(element.getPrefix()).thenReturn(prefix);
        when(element.hasAttributes()).thenReturn(true);
        return element;
    }

    private Document createMockDocument() {
        Document document = mock(Document.class);
        when(document.getDocumentElement()).thenReturn(mock(Element.class));
        return document;
    }
}