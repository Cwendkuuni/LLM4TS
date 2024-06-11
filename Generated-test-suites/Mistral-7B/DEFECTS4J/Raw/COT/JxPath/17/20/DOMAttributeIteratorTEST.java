import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class DOMAttributeIteratorTest {

    private static final String TEST_NAMESPACE = "test";
    private static final String TEST_LOCAL_NAME = "testLocalName";

    private NodePointer nodePointerMock;
    private Element elementMock;
    private DOMAttributeIterator iterator;

    @Before
    public void setUp() {
        nodePointerMock = mock(NodePointer.class);
        elementMock = mock(Element.class);
        Node nodeMock = mock(Node.class);

        when(nodePointerMock.getNode()).thenReturn(nodeMock);
        when(nodeMock.getNodeType()).thenReturn(Node.ELEMENT_NODE);

        iterator = new DOMAttributeIterator(nodePointerMock, new QName(TEST_NS, TEST_LOCAL_NAME));
    }

    @After
    public void tearDown() {
        ClassImposteriser.install();
        ClassImposteriser.unregister(Element.class);
        ClassImposteriser.unregister(Node.class);
        ClassImposteriser.unregister(NodeIterator.class);
        ClassImposteriser.unregister(DOMAttributeIterator.class);
        ClassImposteriser.unregister(DOMAttributePointer.class);
        ClassImposteriser.unregister(Attr.class);
        ClassImposteriser.unregister(QName.class);
        ClassImposteriser.unregister(NodeList.class);
        ClassImposteriser.deinstall();
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(iterator);
    }

    @Test
    public void testConstructorWithNullParent() {
        NodePointer nullParent = null;
        Assert.assertNull(new DOMAttributeIterator(nullParent, new QName("*", "*")));
    }

    @Test
    public void testConstructorWithNullName() {
        NodePointer parent = new DOMAttributePointer(nodePointerMock, null);
        Assert.assertNull(new DOMAttributeIterator(parent, null));
    }

    @Test
    public void testConstructorWithEmptyList() {
        NodePointer parent = new DOMAttributePointer(nodePointerMock, new QName("*", "*"));
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", ""));
        Assert.assertFalse(iterator.hasNext());
        Assert.assertNull(iterator.next());
    }

    @Test
    public void testConstructorWithSingleAttribute() {
        Attr singleAttrMock = mock(Attr.class);
        NodeList singleAttrNodeListMock = mock(NodeList.class);
        when(singleAttrMock.getNodeName()).thenReturn(TEST_LOCAL_NAME);
        when(singleAttrNodeListMock.item(0)).thenReturn(singleAttrMock);
        NamedNodeMap mockNamedNodeMap = mock(NamedNodeMap.class);
        when(elementMock.getAttributes()).thenReturn(mockNamedNodeMap);
        when(mockNamedNodeMap.getNamedItem(TEST_LOCAL_NAME)).thenReturn(singleAttrMock);

        NodePointer parent = new DOMAttributePointer(nodePointerMock, elementMock);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName(TEST_NS, TEST_LOCAL_NAME));

        assertThat(iterator, instanceOf(DOMAttributeIterator.class));
        assertThat(iterator.hasNext(), is(true));
        NodePointer nextNode = iterator.next();
        assertThat(nextNode, instanceOf(DOMAttributePointer.class));
        Assert.assertEquals(singleAttrMock, nextNode.getNode());
    }

    @Test
    public void testConstructorWithMultipleAttributes() {
        Attr firstAttrMock = mock(Attr.class);
        Attr secondAttrMock = mock(Attr.class);
        NodeList firstAttrNodeListMock = mock(NodeList.class);
        NodeList secondAttrNodeListMock = mock(NodeList.class);
        when(firstAttrMock.getNodeName()).thenReturn(TEST_LOCAL_NAME);
        when(firstAttrNodeListMock.item(0)).thenReturn(firstAttrMock);
        when(secondAttrMock.getNodeName()).thenReturn(TEST_LOCAL_NAME);
        when(secondAttrNodeListMock.item(0)).thenReturn(secondAttrMock);
        NamedNodeMap mockNamedNodeMap = mock(NamedNodeMap.class);
        when(elementMock.getAttributes()).thenReturn(mockNamedNodeMap);
        when(mockNamedNodeMap.getLength()).thenReturn(2);
        when(mockNamedNodeMap.getNamedItem(TEST_LOCAL_NAME, 0)).thenReturn(firstAttrMock);
        when(mockNamedNodeMap.getNamedItem(TEST_LOCAL_NAME, 1)).thenReturn(secondAttrMock);

        NodePointer parent = new DOMAttributePointer(nodePointerMock, elementMock);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName(TEST_NS, TEST_LOCAL_NAME));

        assertThat(iterator, instanceOf(DOMAttributeIterator.class));
        assertThat(iterator.hasNext(), is(true));
        NodePointer firstNextNode = iterator.next();
        assertThat(firstNextNode, instanceOf(DOMAttributePointer.class));
        Assert.assertEquals(firstAttrMock, firstNextNode.getNode());

        assertThat(iterator.hasNext(), is(true));
        NodePointer secondNextNode = iterator.next();
        assertThat(secondNextNode, instanceOf(DOMAttributePointer.class));
        Assert.assertEquals(secondAttrMock, secondNextNode.getNode());

        assertThat(iterator.hasNext(), is(false));
        Assert.assertNull(iterator.next());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidName() {
        NodePointer parent = new DOMAttributePointer(nodePointerMock, null);
        new DOMAttributeIterator(parent, new QName(TEST_NS, null));
    }

    @Test
    public void testHasNext() {
        Attr firstAttrMock = mock(Attr.class);
        Attr secondAttrMock = mock(Attr.class);
        NodeList firstAttrNodeListMock = mock(NodeList.class);
        NodeList secondAttrNodeListMock = mock(NodeList.class);
        when(firstAttrMock.getNodeName()).thenReturn(TEST_LOCAL_NAME);
        when(firstAttrNodeListMock.item(0)).thenReturn(firstAttrMock);
        when(secondAttrMock.getNodeName()).thenReturn(TEST_LOCAL_NAME);
        when(secondAttrNodeListMock.item(0)).thenReturn(secondAttrMock);
        NamedNodeMap mockNamedNodeMap = mock(NamedNodeMap.class);
        when(elementMock.getAttributes()).thenReturn(mockNamedNodeMap);
        when(mockNamedNodeMap.getLength()).thenReturn(2);
        when(mockNamedNodeMap.getNamedItem(TEST_LOCAL_NAME, 0)).thenReturn(firstAttrMock);
        when(mockNamedNodeMap.getNamedItem(TEST_LOCAL_NAME, 1)).thenReturn(secondAttrMock);

        NodePointer parent = new DOMAttributePointer(nodePointerMock, elementMock);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName(TEST_NS, TEST_LOCAL_NAME));

        iterator.next();
        iterator.next();

        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    public void testNext() {
        Attr firstAttrMock = mock(Attr.class);
        Attr secondAttrMock = mock(Attr.class);
        NodeList firstAttrNodeListMock = mock(NodeList.class);
        NodeList secondAttrNodeListMock = mock(NodeList.class);
        when(firstAttrMock.getNodeName()).thenReturn(TEST_LOCAL_NAME);
        when(firstAttrNodeListMock.item(0)).thenReturn(firstAttrMock);
        when(secondAttrMock.getNodeName()).thenReturn(TEST_LOCAL_NAME);
        when(secondAttrNodeListMock.item(0)).thenReturn(secondAttrMock);
        NamedNodeMap mockNamedNodeMap = mock(NamedNodeMap.class);
        when(elementMock.getAttributes()).thenReturn(mockNamedNodeMap);
        when(mockNamedNodeMap.getLength()).thenReturn(2);
        when(mockNamedNodeMap.getNamedItem(TEST_LOCAL_NAME, 0)).thenReturn(firstAttrMock);
        when(mockNamedNodeMap.getNamedItem(TEST_LOCAL_NAME, 1)).thenReturn(secondAttrMock);

        NodePointer parent = new DOMAttributePointer(nodePointerMock, elementMock);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName(TEST_NS, TEST_LOCAL_NAME));

        NodePointer firstNextNode = iterator.next();
        NodePointer secondNextNode = iterator.next();

        Assert.assertEquals(firstNextNode.getNode(), firstAttrMock);
        Assert.assertEquals(secondNextNode.getNode(), secondAttrMock);
    }
}