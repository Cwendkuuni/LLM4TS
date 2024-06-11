package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIteratorEmpty() {
        NodePointer parentMock = mock(NodePointer.class);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentMock, null);
        assertTrue(iterator.attributes == Collections.EMPTY_LIST);
    }

    @Test
    public void testJDOMAttributeIteratorSingleAttribute() {
        NodePointer parentMock = mock(NodePointer.class);
        Element elementMock = mock(Element.class);
        when(parentMock.getNode()).thenReturn(elementMock);
        String attributeName = "attributeName";
        Attribute attributeMock = mock(Attribute.class);
        when(elementMock.getAttribute(attributeName, Namespace.XML_NAMESPACE)).thenReturn(attributeMock);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentMock, new QName("", attributeName));
        Attribute attribute = iterator.getNodePointer().getNode();
        assertEquals(attributeMock, attribute);
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testJDOMAttributeIteratorMultipleAttributes() {
        NodePointer parentMock = mock(NodePointer.class);
        Element elementMock = mock(Element.class);
        when(parentMock.getNode()).thenReturn(elementMock);
        String attributeName1 = "attributeName1";
        Attribute attribute1Mock = mock(Attribute.class);
        when(elementMock.getAttribute(attributeName1, Namespace.XML_NAMESPACE)).thenReturn(attribute1Mock);
        String attributeName2 = "attributeName2";
        Attribute attribute2Mock = mock(Attribute.class);
        when(elementMock.getAttribute(attributeName2, Namespace.XML_NAMESPACE)).thenReturn(attribute2Mock);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentMock, new QName("", "*"));
        Attribute attribute1 = iterator.getNodePointer().getNode();
        assertEquals(attribute1Mock, attribute1);
        assertEquals(1, iterator.getPosition());

        Attribute attribute2 = iterator.getNodePointer().getNode();
        assertEquals(attribute2Mock, attribute2);
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testJDOMAttributeIteratorNextPosition() {
        NodePointer parentMock = mock(NodePointer.class);
        Element elementMock = mock(Element.class);
        when(parentMock.getNode()).thenReturn(elementMock);
        String attributeName = "attributeName";
        Attribute attributeMock = mock(Attribute.class);
        when(elementMock.getAttribute(attributeName, Namespace.XML_NAMESPACE)).thenReturn(attributeMock);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentMock, new QName("", attributeName));
        iterator.setPosition(1);
        Attribute nextAttribute = iterator.getNodePointer().getNode();
        assertEquals(attributeMock, nextAttribute);
        assertEquals(2, iterator.getPosition());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testJDOMAttributeIteratorOutOfBounds() {
        NodePointer parentMock = mock(NodePointer.class);
        Element elementMock = mock(Element.class);
        when(parentMock.getNode()).thenReturn(elementMock);
        String attributeName = "attributeName";
        Attribute attributeMock = mock(Attribute.class);
        when(elementMock.getAttribute(attributeName, Namespace.XML_NAMESPACE)).thenReturn(attributeMock);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentMock, new QName("", attributeName));
        iterator.setPosition(2);
    }
}