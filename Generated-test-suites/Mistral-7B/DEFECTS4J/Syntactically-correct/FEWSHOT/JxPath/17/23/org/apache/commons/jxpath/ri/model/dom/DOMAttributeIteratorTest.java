package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator_whenIteratorIsCreated_thenAttributesAreAddedToList() {
        // Given
        Element element = mock(Element.class);
        NodePointer parent = mock(NodePointer.class);
        QName name = new QName("testName", "testPrefix");
        when(element.getNodeName()).thenReturn("testElement");
        when(element.getTagName()).thenReturn("testElement");
        when(element.getNamespaceURI()).thenReturn("testNamespaceURI");
        when(element.getAttributes()).thenReturn(mock(NamedNodeMap.class));

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        // Then
        List<Attr> attributes = iterator.attributes;
        assertSame(name, attributes.get(0).getName());
        assertNull(attributes.get(0).getValue());
        assertSame("testNamespaceURI", attributes.get(0).getNamespaceURI());
    }

    @Test
    public void testDOMAttributeIterator_whenIteratorIsCreatedWithWildcardName_thenAllAttributesAreAddedToList() {
        // Given
        Element element = mock(Element.class);
        NodePointer parent = mock(NodePointer.class);
        QName name = new QName("*");
        when(element.getNodeName()).thenReturn("testElement");
        when(element.getTagName()).thenReturn("testElement");
        when(element.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        NamedNodeMap attributesMap = mock(NamedNodeMap.class);
        when(element.getAttributes()).thenReturn(attributesMap);
        Attr attribute1 = mock(Attr.class);
        Attr attribute2 = mock(Attr.class);
        when(attributesMap.getLength()).thenReturn(2);
        when(attributesMap.item(0)).thenReturn(attribute1);
        when(attributesMap.item(1)).thenReturn(attribute2);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        // Then
        List<Attr> attributes = iterator.attributes;
        assertSame(attribute1, attributes.get(0));
        assertSame(attribute2, attributes.get(1));
    }

    @Test
    public void testDOMAttributeIterator_whenIteratorIsCreatedWithQualifiedName_thenAttributeIsAddedToListIfItMatches() {
        // Given
        Element element = mock(Element.class);
        NodePointer parent = mock(NodePointer.class);
        QName name = new QName("testNamespaceURI:testName");;
        when(element.getNodeName()).thenReturn("testElement");
        when(element.getTagName()).thenReturn("testElement");
        when(element.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        Attr attribute = mock(Attr.class);
        when(attribute.getNamespaceURI()).thenReturn("testNamespaceURI");
        when(attribute.getNodeName()).thenReturn("testName");
        when(attribute.getValue()).thenReturn("testValue");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        // Then
        List<Attr> attributes = iterator.attributes;
        assertSame(attribute, attributes.get(0));
    }

    @Test
    public void testDOMAttributeIterator_whenGetNodePointerIsCalled_thenCorrespondingAttributeIsReturned() {
        // Given
        Element element = mock(Element.class);
        NodePointer parent = mock(NodePointer.class);
        QName name = new QName("testName");
        when(element.getNodeName()).thenReturn("testElement");
        when(element.getTagName()).thenReturn("testElement");
        when(element.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        Attr attribute = mock(Attr.class);
        when(attributes.get(0)).thenReturn(attribute);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        // When & Then
        NodePointer nodePointer = iterator.getNodePointer();
        assertSame(attribute, nodePointer.getNode());
    }

    @Test
    public void testDOMAttributeIterator_whenGetPositionIsCalled_thenCorrespondingAttributeIsReturned() {
        // Given
        Element element = mock(Element.class);
        NodePointer parent = mock(NodePointer.class);
        QName name = new QName("testName");
        when(element.getNodeName()).thenReturn("testElement");
        when(element.getTagName()).thenReturn("testElement");
        when(element.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        Attr attribute = mock(Attr.class);
        when(attributes.get(0)).thenReturn(attribute);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        // When & Then
        int position = iterator.getPosition();
        int expectedPosition = 1;
        assertSame(expectedPosition, position);

        // Given: setPosition to 2
        iterator.setPosition(2);

        // When & Then
        position = iterator.getPosition();
        assertSame(expectedPosition, position);

        // Given: setPosition to 3 (out of bounds)
        iterator.setPosition(3);

        // When & Then
        position = iterator.getPosition();
        assertSame(expectedPosition, position);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDOMAttributeIterator_whenGetNodePointerIsCalledWithInvalidPosition() {
        // Given
        Element element = mock(Element.class);
        NodePointer parent = mock(NodePointer.class);
        QName name = new QName("testName");
        when(element.getNodeName()).thenReturn("testElement");
        when(element.getTagName()).thenReturn("testElement");
        when(element.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        Attr attribute1 = mock(Attr.class);
        Attr attribute2 = mock(Attr.class);
        when(attributes.get(0)).thenReturn(attribute1);
        when(attributes.get(1)).thenReturn(attribute2);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        // When
        iterator.setPosition(3);

        // Then: exception is thrown
        iterator.getNodePointer();
    }
}