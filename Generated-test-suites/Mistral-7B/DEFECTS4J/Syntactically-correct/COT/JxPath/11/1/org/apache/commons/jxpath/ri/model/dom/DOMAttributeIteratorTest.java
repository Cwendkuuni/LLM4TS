import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class DOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        NodePointer parentMock = mock(NodePointer.class);
        QName nameMock = mock(QName.class);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);
        Assert.assertSame(parentMock, iterator.getParent());
        Assert.assertSame(nameMock, iterator.getName());
        Assert.assertNotNull(iterator.getAttributes());
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testConstructorWithNullParent() {
        Assert.assertNull(new DOMAttributeIterator(null, new QName("test", "test")));
    }

    @Test
    public void testConstructorWithNullName() {
        NodePointer parentMock = mock(NodePointer.class);
        Assert.assertNull(new DOMAttributeIterator(parentMock, null));
    }

    @Test
    public void testConstructorWithEmptyList() {
        NodePointer parentMock = mock(NodePointer.class);
        QName nameMock = new QName("test", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertEquals(0, iterator.size());
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testConstructorWithSingleAttribute() {
        NodePointer parentMock = mock(NodePointer.class);
        QName nameMock = new QName("test", "test");
        Attr attributeMock = mock(Attr.class);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);
        when(parentMock.getNode()).thenReturn(mock(Element.class));
        when(parentMock.getNamespaceURI("test")).thenReturn("test-namespace");
        when(parentMock.getNamespaceURI(null)).thenReturn("default-namespace");
        when(parentMock.getNamespaceURI(attr.getNamespaceURI())).thenReturn("attr-namespace");
        when(parentMock.getNamespaceURI(attr.getPrefix())).thenReturn("attr-prefix");
        when(parentMock.getLocalName(attr)).thenReturn("attr-local-name");
        when(attr.getValue()).thenReturn("attr-value");
        iterator.attributes.add(attributeMock);

        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertSame(iterator, nodePointer.getIterator());
        Assert.assertSame(parentMock, nodePointer.getParent());
        Assert.assertSame(attributeMock, nodePointer.getNode());
        Assert.assertEquals("test-namespace:test", nodePointer.getQualifiedName().toString());

        iterator.setPosition(1);
        nodePointer = iterator.getNodePointer();
        Assert.assertNull(nodePointer);
    }

    @Test
    public void testConstructorWithMultipleAttributes() {
        NodePointer parentMock = mock(NodePointer.class);
        QName nameMock = new QName("test", "test");
        Attr attribute1Mock = mock(Attr.class);
        Attr attribute2Mock = mock(Attr.class);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);
        when(parentMock.getNode()).thenReturn(mock(Element.class));
        when(parentMock.getNamespaceURI("test")).thenReturn("test-namespace");
        when(parentMock.getNamespaceURI(null)).thenReturn("default-namespace");
        when(parentMock.getNamespaceURI(attr1.getNamespaceURI())).thenReturn("attr1-namespace");
        when(parentMock.getNamespaceURI(attr1.getPrefix())).thenReturn("attr1-prefix");
        when(parentMock.getLocalName(attr1)).thenReturn("attr1-local-name");
        when(attr1.getValue()).thenReturn("attr1-value");
        when(parentMock.getNamespaceURI(attr2.getNamespaceURI())).thenReturn("attr2-namespace");
        when(parentMock.getNamespaceURI(attr2.getPrefix())).thenReturn("attr2-prefix");
        when(parentMock.getLocalName(attr2)).thenReturn("attr2-local-name");
        when(attr2.getValue()).thenReturn("attr2-value");
        iterator.attributes.add(attribute1Mock);
        iterator.attributes.add(attribute2Mock);

        NodePointer nodePointer1 = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer1);
        Assert.assertSame(iterator, nodePointer1.getIterator());
        Assert.assertSame(parentMock, nodePointer1.getParent());
        Assert.assertSame(attribute1Mock, nodePointer1.getNode());
        Assert.assertEquals("test-namespace:test", nodePointer1.getQualifiedName().toString());

        iterator.setPosition(2);
        NodePointer nodePointer2 = iterator.getNodePointer();
        Assert.assertNull(nodePointer2);

        nodePointer1 = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer1);
        Assert.assertEquals(1, iterator.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionWithInvalidIndex() {
        NodePointer parentMock = mock(NodePointer.class);
        QName nameMock = new QName("test", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);
        iterator.setPosition(10);
    }

    @Test
    public void testSize() {
        NodePointer parentMock = mock(NodePointer.class);
        QName nameMock = new QName("test", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);
        when(parentMock.getNode()).thenReturn(mock(Element.class));
        when(parentMock.getNamespaceURI("test")).thenReturn("test-namespace");
        when(parentMock.getNamespaceURI(null)).thenReturn("default-namespace");
        when(parentMock.getNamespaceURI(attr.getNamespaceURI())).thenReturn("attr-namespace");
        when(parentMock.getNamespaceURI(attr.getPrefix())).thenReturn("attr-prefix");
        when(parentMock.getLocalName(attr)).thenReturn("attr-local-name");
        when(attr.getValue()).thenReturn("attr-value");
        iterator.attributes.add(mock(Attr.class));
        iterator.attributes.add(mock(Attr.class));

        Assert.assertEquals(2, iterator.size());

        iterator.setPosition(1);
        Assert.assertEquals(1, iterator.size());
    }

    @Test
    public void testGetNodePointer() {
        NodePointer parentMock = mock(NodePointer.class);
        QName nameMock = new QName("test", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);
        when(parentMock.getNode()).thenReturn(mock(Element.class));
        when(parentMock.getNamespaceURI("test")).thenReturn("test-namespace");
        when(parentMock.getNamespaceURI(null)).thenReturn("default-namespace");
        when(parentMock.getNamespaceURI(attr.getNamespaceURI())).thenReturn("attr-namespace");
        when(parentMock.getNamespaceURI(attr.getPrefix())).thenReturn("attr-prefix");
        when(parentMock.getLocalName(attr)).thenReturn("attr-local-name");
        when(attr.getValue()).thenReturn("attr-value");
        iterator.attributes.add(mock(Attr.class));
        iterator.attributes.add(mock(Attr.class));

        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertSame(iterator, nodePointer.getIterator());
        Assert.assertSame(parentMock, nodePointer.getParent());
        Assert.assertEquals("test-namespace:test", nodePointer.getQualifiedName().toString());

        iterator.setPosition(2);
        nodePointer = iterator.getNodePointer();
        Assert.assertNull(nodePointer);

        nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(1, iterator.size());
    }

    @Test
    public void testGetPosition() {
        NodePointer parentMock = mock(NodePointer.class);
        QName nameMock = new QName("test", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);
        when(parentMock.getNode()).thenReturn(mock(Element.class));
        when(parentMock.getNamespaceURI("test")).thenReturn("test-namespace");
        when(parentMock.getNamespaceURI(null)).thenReturn("default-namespace");
        when(parentMock.getNamespaceURI(attr.getNamespaceURI())).thenReturn("attr-namespace");
        when(parentMock.getNamespaceURI(attr.getPrefix())).thenReturn("attr-prefix");
        when(parentMock.getLocalName(attr)).thenReturn("attr-local-name");
        when(attr.getValue()).thenReturn("attr-value");
        iterator.attributes.add(mock(Attr.class));
        iterator.attributes.add(mock(Attr.class));

        Assert.assertEquals(0, iterator.getPosition());

        iterator.setPosition(1);
        Assert.assertEquals(1, iterator.getPosition());

        iterator.setPosition(2);
        Assert.assertEquals(2, iterator.getPosition());

        iterator.setPosition(0);
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPositionWithInvalidIndex() {
        NodePointer parentMock = mock(NodePointer.class);
        QName nameMock = new QName("test", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);
        iterator.setPosition(10);
        Assert.assertEquals(0, iterator.getPosition());
    }

}