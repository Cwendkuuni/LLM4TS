package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.ElementImpl;
import org.w3c.dom.AttrImpl;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator_Empty() {
        NodePointer parentMock = mock(NodePointer.class);
        QName nameMock = mock(QName.class);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);

        assertTrue(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testDOMAttributeIterator_OneAttribute() {
        NodePointer parentMock = mock(NodePointer.class);
        QName nameMock = new QName("test", "prefix", "http://test.com");
        Element elementMock = mock(Element.class);
        Attr attributeMock = mock(Attr.class);

        when(parentMock.getNode()).thenReturn(new Document());
        when(parentMock.getNode()).thenReturn(elementMock);
        when(elementMock.getAttributeNodeNS(Mockito.anyString(), Mockito.anyString())).thenReturn(attributeMock);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);

        assertTrue(iterator.hasNext());
        assertTrue(iterator.next() instanceof DOMAttributePointer);
        assertNotNull(((DOMAttributePointer) iterator.next()).getAttribute());
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIterator_MultipleAttributes() {
        NodePointer parentMock = mock(NodePointer.class);
        QName nameMock = new QName("test", "prefix", "http://test.com");
        Element elementMock = mock(Element.class);
        Attr attribute1Mock = mock(Attr.class);
        Attr attribute2Mock = mock(Attr.class);

        when(parentMock.getNode()).thenReturn(new Document());
        when(parentMock.getNode()).thenReturn(elementMock);
        when(elementMock.getAttributeNodeNS(Mockito.anyString(), Mockito.anyString())).thenReturn(attribute1Mock);
        when(elementMock.getAttributes()).thenReturn(new AttributesImpl());
        when(elementMock.getAttributes().getLength()).thenReturn(2);
        when(((AttributesImpl) elementMock.getAttributes()).item(1)).thenReturn(attribute2Mock);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);

        assertTrue(iterator.hasNext());
        assertTrue(iterator.next() instanceof DOMAttributePointer);
        assertNotNull(((DOMAttributePointer) iterator.next()).getAttribute());
        assertEquals(1, iterator.getPosition());

        assertTrue(iterator.hasNext());
        assertTrue(iterator.next() instanceof DOMAttributePointer);
        assertNotNull(((DOMAttributePointer) iterator.next()).getAttribute());
        assertEquals(2, iterator.getPosition());

        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testDOMAttributeIterator_Namespace() {
        NodePointer parentMock = mock(NodePointer.class);
        QName nameMock = new QName("test", "prefix1", "http://test1.com");
        Element elementMock = mock(Element.class);
        Attr attribute1Mock = new AttrImpl();
        attribute1Mock.setNamespaceURI("http://test2.com");
        attribute1Mock.setPrefix("prefix2");
        attribute1Mock.setLocalName("test1");

        Element element2Mock = mock(Element.class);
        Attr attribute2Mock = new AttrImpl();
        attribute2Mock.setLocalName("test2");

        when(parentMock.getNode()).thenReturn(new Document());
        when(parentMock.getNode()).thenReturn(elementMock);
        when(parentMock.getNode()).thenReturn(element2Mock);
        when(elementMock.getAttributeNodeNS(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        when(elementMock.getAttributes()).thenReturn(new NamedNodeMapImpl());
        when(((NamedNodeMapImpl) elementMock.getAttributes()).getNamedItemNS(Mockito.anyString(), Mockito.anyString())).thenReturn(attribute1Mock);
        when(element2Mock.getAttributes()).thenReturn(new AttributesImpl());
        when(((AttributesImpl) element2Mock.getAttributes()).getLength()).thenReturn(1);
        when(((AttributesImpl) element2Mock.getAttributes()).item(0)).thenReturn(attribute2Mock);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);

        assertFalse(iterator.hasNext());

        parentMock.getNode().setNodeValue("testElement");
        when(parentMock.getNode()).thenReturn(elementMock);
        when(elementMock.getNamespaceURI()).thenReturn("http://test1.com");

        iterator = new DOMAttributeIterator(parentMock, nameMock);

        assertTrue(iterator.hasNext());
        assertTrue(iterator.next() instanceof DOMAttributePointer);
        assertNotNull(((DOMAttributePointer) iterator.next()).getAttribute());
        assertEquals("test1", ((DOMAttributePointer) iterator.next()).getAttribute().getNodeName());
        assertEquals("prefix2", ((DOMAttributePointer) iterator.next()).getAttribute().getPrefix());
        assertEquals("http://test2.com", ((DOMAttributePointer) iterator.next()).getAttribute().getNamespaceURI());
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIterator_EmptyNamespace() {
        NodePointer parentMock = mock(NodePointer.class);
        QName nameMock = new QName("test", null, "");
        Element elementMock = mock(Element.class);
        Attr attributeMock = mock(Attr.class);

        when(parentMock.getNode()).thenReturn(new Document());
        when(parentMock.getNode()).thenReturn(elementMock);
        when(elementMock.getAttributeNode(Mockito.anyString())).thenReturn(attributeMock);
        when(attributeMock.getNamespaceURI()).thenReturn("");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);

        assertTrue(iterator.hasNext());
        assertTrue(iterator.next() instanceof DOMAttributePointer);
        assertNotNull(((DOMAttributePointer) iterator.next()).getAttribute());
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIterator_MultipleNamespaces() {
        NodePointer parentMock = mock(NodePointer.class);
        QName name1Mock = new QName("test1", "prefix1", "http://test1.com");
        QName name2Mock = new QName("test2", null, "");
        Element elementMock = mock(Element.class);
        Attr attribute1Mock = new AttrImpl();
        attribute1Mock.setNamespaceURI("http://test1.com");
        attribute1Mock.setPrefix("prefix1");
        attribute1Mock.setLocalName("test1");
        Attr attribute2Mock = new AttrImpl();
        attribute2Mock.setLocalName("test2");

        when(parentMock.getNode()).thenReturn(new Document());
        when(parentMock.getNode()).thenReturn(elementMock);
        when(elementMock.getAttributeNodeNS(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        when(elementMock.getAttributes()).thenReturn(new NamedNodeMapImpl());
        when(((NamedNodeMapImpl) elementMock.getAttributes()).getNamedItemNS(Mockito.anyString(), Mockito.anyString())).thenReturn(attribute1Mock);
        when(((NamedNodeMapImpl) elementMock.getAttributes()).getNamedItem(Mockito.anyString())).thenReturn(attribute2Mock);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, name1Mock);

        assertTrue(iterator.hasNext());
        assertTrue(iterator.next() instanceof DOMAttributePointer);
        assertNotNull(((DOMAttributePointer) iterator.next()).getAttribute());
        assertEquals("test1", ((DOMAttributePointer) iterator.next()).getAttribute().getNodeName());
        assertEquals("prefix1", ((DOMAttributePointer) iterator.next()).getAttribute().getPrefix());
        assertEquals("http://test1.com", ((DOMAttributePointer) iterator.next()).getAttribute().getNamespaceURI());
        assertEquals(1, iterator.getPosition());

        iterator = new DOMAttributeIterator(parentMock, name2Mock);

        assertTrue(iterator.hasNext());
        assertTrue(iterator.next() instanceof DOMAttributePointer);
        assertNotNull(((DOMAttributePointer) iterator.next()).getAttribute());
        assertEquals(2, iterator.getPosition());

        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testDOMAttributeIterator_EmptyNamespaceAndName() {
        NodePointer parentMock = mock(NodePointer.class);
        QName nameMock = new QName(null, null, "");
        Element elementMock = mock(Element.class);
        Attr attributeMock = mock(Attr.class);

        when(parentMock.getNode()).thenReturn(new Document());
        when(parentMock.getNode()).thenReturn(elementMock);
        when(elementMock.getAttributeNode(Mockito.anyString())).thenReturn(attributeMock);
        when(attributeMock.getNamespaceURI()).thenReturn("");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);

        assertTrue(iterator.hasNext());
        assertTrue(iterator.next() instanceof DOMAttributePointer);
        assertNotNull(((DOMAttributePointer) iterator.next()).getAttribute());
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIterator_MultipleAttributesWithSameNameButDifferentNamespaces() {
        NodePointer parentMock = mock(NodePointer.class);
        QName nameMock = new QName("test", null, "");
        Element elementMock = mock(Element.class);
        Attr attribute1Mock = new AttrImpl();
        attribute1Mock.setLocalName("test");
        attribute1Mock.setNamespaceURI("http://test1.com");
        Attr attribute2Mock = new AttrImpl();
        attribute2Mock.setLocalName("test");
        attribute2Mock.setNamespaceURI("http://test2.com");

        when(parentMock.getNode()).thenReturn(new Document());
        when(parentMock.getNode()).thenReturn(elementMock);
        when(elementMock.getAttributes()).thenReturn(new NamedNodeMapImpl());
        when(((NamedNodeMapImpl) elementMock.getAttributes()).getLength()).thenReturn(2);
        when(((NamedNodeMapImpl) elementMock.getAttributes()).item(0)).thenReturn(attribute1Mock);
        when(((NamedNodeMapImpl) elementMock.getAttributes()).item(1)).thenReturn(attribute2Mock);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);

        assertTrue(iterator.hasNext());
        assertTrue(iterator.next() instanceof DOMAttributePointer);
        assertNotNull(((DOMAttributePointer) iterator.next()).getAttribute());
        assertEquals("test", ((DOMAttributePointer) iterator.next()).getAttribute().getNodeName());
        assertEquals(null, ((DOMAttributePointer) iterator.next()).getAttribute().getPrefix());
        assertEquals("", ((DOMAttributePointer) iterator.next()).getAttribute().getNamespaceURI());
        assertEquals(1, iterator.getPosition());

        assertFalse(iterator.hasNext());
        assertNull(iterator.next());

        iterator = new DOMAttributeIterator(parentMock, nameMock);

        assertTrue(iterator.hasNext());
        assertTrue(iterator.next() instanceof DOMAttributePointer);
        assertNotNull(((DOMAttributePointer) iterator.next()).getAttribute());
        assertEquals("test", ((DOMAttributePointer) iterator.next()).getAttribute().getNodeName());
        assertEquals(null, ((DOMAttributePointer) iterator.next()).getAttribute().getPrefix());
        assertEquals("http://test2.com", ((DOMAttributePointer) iterator.next()).getAttribute().getNamespaceURI());
        assertEquals(2, iterator.getPosition());

        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    class NamedNodeMapImpl extends NamedNodeMap {
        private NodeList nodes = new NodeListImpl();

        @Override
        public int getLength() {
            return nodes.getLength();
        }

        @Override
        public Node item(int index) {
            return nodes.item(index);
        }
    }

    class NodeListImpl implements NodeList {
        private List<Node> nodes = new ArrayList<>();

        @Override
        public int getLength() {
            return nodes.size();
        }

        @Override
        public Node item(int index) {
            return nodes.get(index);
        }
    }
}