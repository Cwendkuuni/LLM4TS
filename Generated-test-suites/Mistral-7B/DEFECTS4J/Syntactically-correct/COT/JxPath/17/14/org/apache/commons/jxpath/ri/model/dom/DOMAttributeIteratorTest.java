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

 private NodePointer parentMock;
 private QName nameMock;
 private List attributesMock;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 parentMock = mock(NodePointer.class);
 nameMock = new QName("namespace:localName");
 attributesMock = new ArrayList();
 }

 @Test
 public void testConstructor_WithElementAndQName_ShouldInitializeIteratorWithAttributes() {
 Node nodeMock = mock(Node.class);
 when(parentMock.getNode()).thenReturn(nodeMock);
 when(nodeMock.getNodeType()).thenReturn(Node.ELEMENT_NODE);
 when(nodeMock.getAttributes()).thenReturn(createMock(NamedNodeMap.class));

 DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);

 verify(parentMock, times(1)).getNode();
 verify(nodeMock, times(1)).getNodeType();

 assertFalse(iterator.hasNext());
 Assert.assertEquals(0, iterator.getPosition());
 }

 @Test
 public void testConstructor_WithElementAndWildcardQName_ShouldInitializeIteratorWithAllAttributes() {
 Node nodeMock = mock(Node.class);
 when(parentMock.getNode()).thenReturn(nodeMock);
 when(nodeMock.getNodeType()).thenReturn(Node.ELEMENT_NODE);
 when(nodeMock.getAttributes()).thenReturn(createMock(NamedNodeMap.class));

 int length = 5;
 for (int i = 0; i < length; i++) {
 Attr attrMock = mock(Attr.class);
 when(attrMock.getNodeName()).thenReturn(createQName("namespace:" + i, "localName" + i));
 when(attrMock.getNodeValue()).thenReturn("value" + i);
 attributesMock.add(attrMock);
 }

 when(nodeMock.getAttributes()).thenReturn(attributesMock);

 DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, new QName("*"));

 verify(parentMock, times(1)).getNode();
 verify(nodeMock, times(1)).getNodeType();

 assertTrue(iterator.hasNext());
 Assert.assertEquals(0, iterator.getPosition());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructor_WithNullParent_ShouldThrowIllegalArgumentException() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(null, nameMock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructor_WithNullName_ShouldThrowIllegalArgumentException() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, null);
 }

 @Test
 public void testNext_ShouldMoveIteratorToNextAttribute() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);
 Attr firstAttributeMock = mock(Attr.class);
 Attr secondAttributeMock = mock(Attr.class);

 when(parentMock.getNode()).thenReturn(createMock(Element.class));
 when(createMock(NamedNodeMap.class).getLength()).thenReturn(2);
 when(createMock(NamedNodeMap.class).item(0)).thenReturn(firstAttributeMock);
 when(createMock(NamedNodeMap.class).item(1)).thenReturn(secondAttributeMock);

 iterator.next();
 iterator.next();

 verify(createMock(NamedNodeMap.class), times(1)).getLength();
 verify(createMock(NamedNodeMap.class), times(1)).item(0);
 verify(createMock(NamedNodeMap.class), times(1)).item(1);

 Assert.assertEquals(1, iterator.getPosition());
 }

 @Test
 public void testHasNext_ShouldReturnTrueIfThereIsAnotherAttribute() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);
 Attr firstAttributeMock = mock(Attr.class);
 Attr secondAttributeMock = mock(Attr.class);

 when(parentMock.getNode()).thenReturn(createMock(Element.class));
 when(createMock(NamedNodeMap.class).getLength()).thenReturn(2);
 when(createMock(NamedNodeMap.class).item(0)).thenReturn(firstAttributeMock);
 when(createMock(NamedNodeMap.class).item(1)).thenReturn(secondAttributeMock);

 assertTrue(iterator.hasNext());
 }

 @Test
 public void testHasNext_ShouldReturnFalseIfThereIsNoAnotherAttribute() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);
 Attr firstAttributeMock = mock(Attr.class);

 when(parentMock.getNode()).thenReturn(createMock(Element.class));
 when(createMock(NamedNodeMap.class).getLength()).thenReturn(1);
 when(createMock(NamedNodeMap.class).item(0)).thenReturn(firstAttributeMock);

 assertFalse(iterator.hasNext());
 }

 @Test
 public void testGetNodePointer_ShouldReturnAttributeNodePointer() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);
 Attr attributeMock = mock(Attr.class);

 when(parentMock.getNode()).thenReturn(createMock(Element.class));
 when(createMock(NamedNodeMap.class).getLength()).thenReturn(1);
 when(createMock(NamedNodeMap.class).item(0)).thenReturn(attributeMock);

 NodePointer nodePointer = iterator.getNodePointer();

 verify(createMock(NamedNodeMap.class), times(1)).getLength();
 verify(createMock(NamedNodeMap.class), times(1)).item(0);
 Assert.assertEquals(attributeMock, nodePointer.getNode());
 }

 @Test
 public void testGetNodePointer_ShouldReturnNullIfThereIsNoNextAttribute() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);

 when(parentMock.getNode()).thenReturn(createMock(Element.class));
 when(createMock(NamedNodeMap.class).getLength()).thenReturn(0);

 NodePointer nodePointer = iterator.getNodePointer();

 assertNull(nodePointer);
 }

 @Test
 public void testGetPosition_ShouldReturnCurrentPositionOfIterator() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);
 Attr firstAttributeMock = mock(Attr.class);
 Attr secondAttributeMock = mock(Attr.class);

 when(parentMock.getNode()).thenReturn(createMock(Element.class));
 when(createMock(NamedNodeMap.class).getLength()).thenReturn(2);
 when(createMock(NamedNodeMap.class).item(0)).thenReturn(firstAttributeMock);
 when(createMock(NamedNodeMap.class).item(1)).thenReturn(secondAttributeMock);

 iterator.next();

 Assert.assertEquals(1, iterator.getPosition());
 }

 @Test
 public void testSetPosition_ShouldMoveIteratorToSpecifiedPosition() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);
 Attr firstAttributeMock = mock(Attr.class);
 Attr secondAttributeMock = mock(Attr.class);
 Attr thirdAttributeMock = mock(Attr.class);

 when(parentMock.getNode()).thenReturn(createMock(Element.class));
 when(createMock(NamedNodeMap.class).getLength()).thenReturn(3);
 when(createMock(NamedNodeMap.class).item(0)).thenReturn(firstAttributeMock);
 when(createMock(NamedNodeMap.class).item(1)).thenReturn(secondAttributeMock);
 when(createMock(NamedNodeMap.class).item(2)).thenReturn(thirdAttributeMock);

 iterator.next();
 iterator.next();

 Assert.assertEquals(2, iterator.getPosition());
 assertTrue(iterator.setPosition(2));
 assertFalse(iterator.setPosition(5));
 }
}