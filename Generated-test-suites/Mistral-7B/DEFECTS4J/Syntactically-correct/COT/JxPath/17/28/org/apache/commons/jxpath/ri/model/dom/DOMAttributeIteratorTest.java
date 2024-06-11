import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DOMAttributeIteratorTest {

 private NodePointer parent;
 private QName name;
 private DOMAttributeIterator iterator;
 private Element element;

 @Before
 public void setUp() {
 parent = mock(NodePointer.class);
 name = new QName("testName", "testPrefix", "testNamespace");
 element = mock(Element.class);
 }

 @Test
 public void testConstructor() {
 iterator = new DOMAttributeIterator(parent, name);
 Assert.assertNotNull(iterator);
 }

 @Test
 public void testGetNodePointer_whenPositionIsValid_returnsDOMAttributePointer() {
 when(parent.getNode()).thenReturn(mock(Node.class));
 when(element.getAttributeNodeNS(name.getNamespaceURI(), name.getName())).thenReturn(mock(Attr.class));
 iterator = new DOMAttributeIterator(parent, name);
 NodePointer nodePointer = iterator.getNodePointer();
 Assert.assertInstanceOf(DOMAttributePointer.class, nodePointer);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetNodePointer_whenPositionIsInvalid_throwsException() {
 when(parent.getNode()).thenReturn(mock(Node.class));
 when(element.getAttributeNodeNS(name.getNamespaceURI(), name.getName())).thenReturn(mock(Attr.class));
 iterator = new DOMAttributeIterator(parent, name);
 iterator.setPosition(2);
 iterator.getNodePointer();
 }

 @Test
 public void testGetPosition_whenValidPosition_returnsPosition() {
 when(parent.getNode()).thenReturn(mock(Node.class));
 when(element.getAttributeNodeNS(name.getNamespaceURI(), name.getName())).thenReturn(mock(Attr.class));
 iterator = new DOMAttributeIterator(parent, name);
 iterator.setPosition(1);
 int position = iterator.getPosition();
 Assert.assertEquals(1, position);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPosition_whenInvalidPosition_throwsException() {
 when(parent.getNode()).thenReturn(mock(Node.class));
 when(element.getAttributeNodeNS(name.getNamespaceURI(), name.getName())).thenReturn(mock(Attr.class));
 iterator = new DOMAttributeIterator(parent, name);
 iterator.setPosition(0);
 iterator.setPosition(5);
 }

 @Test
 public void testTestAttr_whenAttrHasNoPrefix_returnsTrue() {
 Attr attr = mock(Attr.class);
 when(attr.getNodeName()).thenReturn(name.getName());
 boolean result = DOMAttributeIterator.testAttr(attr);
 Assert.assertTrue(result);
 }

 @Test
 public void testTestAttr_whenAttrHasPrefix_returnsFalse() {
 Attr attr = mock(Attr.class);
 when(attr.getNodeName()).thenReturn(new QName("testName", "testPrefix", "testNamespace").getNodeName());
 boolean result = DOMAttributeIterator.testAttr(attr);
 Assert.assertFalse(result);
 }

 @Test
 public void testTestAttr_whenAttrNSMatches_returnsTrue() {
 Attr attr = mock(Attr.class);
 when(attr.getNodeName()).thenReturn(name.getNodeName());
 when(parent.getNamespaceURI(name.getPrefix())).thenReturn("testNamespace");
 when(parent.getNamespaceResolver()).thenReturn(mock(NamespaceResolver.class));
 boolean result = DOMAttributeIterator.testAttr(attr);
 Assert.assertTrue(result);
 }

 @Test
 public void testTestAttr_whenAttrNSDoesNotMatch_returnsFalse() {
 Attr attr = mock(Attr.class);
 when(attr.getNodeName()).thenReturn(name.getNodeName());
 when(parent.getNamespaceURI(name.getPrefix())).thenReturn("testOtherNamespace");
 boolean result = DOMAttributeIterator.testAttr(attr);
 Assert.assertFalse(result);
 }

 @Test
 public void testGetAttribute_whenElementHasAttrWithMatchingName_returnsAttr() {
 when(parent.getNode()).thenReturn(element);
 when(element.getAttributeNodeNS(name.getNamespaceURI(), name.getName())).thenReturn(mock(Attr.class));
 iterator = new DOMAttributeIterator(parent, name);
 iterator.setPosition(1);
 Attr result = iterator.getNodePointer().getNode();
 Assert.assertNotNull(result);
 Assert.assertEquals(name.getNodeName(), result.getNodeName());
 }

 @Test
 public void testGetAttribute_whenElementHasNoAttrWithMatchingName_returnsNull() {
 when(parent.getNode()).thenReturn(element);
 iterator = new DOMAttributeIterator(parent, name);
 iterator.setPosition(1);
 Attr result = iterator.getNodePointer().getNode();
 Assert.assertNull(result);
 }
}