import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class DOMAttributeIteratorTest {

 private NodePointer parentMock;
 private QName name;
 private DOMAttributeIterator iterator;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 parentMock = mock(NodePointer.class);
 name = new QName("test", "test:attribute");
 }

 @Test
 public void testConstructor_ElementNodeWithSpecificAttribute_ShouldInitializeIteratorWithSingleAttribute() {
 Element element = mock(Element.class);
 when(parentMock.getNode()).thenReturn(element);
 Attr attribute = mock(Attr.class);
 when(element.getAttributeNodeNS(anyString(), anyString())).thenReturn(attribute);

 iterator = new DOMAttributeIterator(parentMock, name);
 List<Attr> attributes = iterator.getAttributes();
 Assert.assertEquals(1, attributes.size());
 Assert.assertEquals(attribute, attributes.get(0));
 }

 @Test
 public void testConstructor_ElementNodeWithMultipleAttributes_ShouldInitializeIteratorWithAllMatchingAttributes() {
 Element element = mock(Element.class);
 when(parentMock.getNode()).thenReturn(element);
 Attr attribute1 = mock(Attr.class);
 Attr attribute2 = mock(Attr.class);
 when(element.getAttributeNodeNS(anyString(), eq("test:attribute"))).thenReturn(attribute1);
 when(element.getAttributes()).thenReturn(mock(NamedNodeMap.class));
 doReturn(attribute2).when(element).getAttributes().item(1);

 iterator = new DOMAttributeIterator(parentMock, name);
 List<Attr> attributes = iterator.getAttributes();
 Assert.assertEquals(2, attributes.size());
 Assert.assertEquals(attribute1, attributes.get(0));
 Assert.assertEquals(attribute2, attributes.get(1));
 }

 @Test
 public void testConstructor_ElementNodeWithNoMatchingAttributes_ShouldInitializeIteratorWithEmptyList() {
 Element element = mock(Element.class);
 when(parentMock.getNode()).thenReturn(element);
 when(element.getAttributeNodeNS(anyString(), anyString())).thenReturn(null);

 iterator = new DOMAttributeIterator(parentMock, name);
 List<Attr> attributes = iterator.getAttributes();
 Assert.assertEquals(0, attributes.size());
 }

 @Test
 public void testConstructor_ElementNodeWithMixedAttributes_ShouldInitializeIteratorWithAllMatchingAttributes() {
 Element element = mock(Element.class);
 when(parentMock.getNode()).thenReturn(element);
 Attr attribute1 = mock(Attr.class);
 Attr attribute2 = mock(Attr.class);
 when(element.getAttributeNodeNS(anyString(), eq("test:attribute1"))).thenReturn(attribute1);
 when(element.getAttributes()).thenReturn(mock(NamedNodeMap.class));
 doReturn(attribute2).when(element).getAttributes().item(1);

 iterator = new DOMAttributeIterator(parentMock, new QName("test", "test:attribute1"));
 List<Attr> attributes = iterator.getAttributes();
 Assert.assertEquals(2, attributes.size());
 Assert.assertEquals(attribute1, attributes.get(0));
 Assert.assertEquals(attribute2, attributes.get(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructor_NullParentPointer_ShouldThrowIllegalArgumentException() {
 iterator = new DOMAttributeIterator(null, name);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructor_NullName_ShouldThrowIllegalArgumentException() {
 iterator = new DOMAttributeIterator(parentMock, null);
 }

 @Test
 public void testNodePointer_ShouldReturnCorrectNodePointer() {
 Attr attribute = mock(Attr.class);
 iterator = new DOMAttributeIterator(parentMock, name);
 iterator.getAttributes().add(attribute);
 NodePointer nodePointer = iterator.getNodePointer();
 Assert.assertEquals(parentMock, nodePointer.getParent());
 Assert.assertEquals(attribute, nodePointer.getNode());
 }

 @Test
 public void testGetPosition_ShouldReturnCorrectPosition() {
 iterator = new DOMAttributeIterator(parentMock, name);
 iterator.getAttributes().add(mock(Attr.class));
 iterator.getAttributes().add(mock(Attr.class));
 Assert.assertEquals(0, iterator.getPosition());
 }

 @Test
 public void testSetPosition_ShouldReturnTrue_WhenPositionIsValid() {
 iterator = new DOMAttributeIterator(parentMock, name);
 iterator.getAttributes().add(mock(Attr.class));
 iterator.getAttributes().add(mock(Attr.class));
 Assert.assertTrue(iterator.setPosition(1));
 }

 @Test
 public void testSetPosition_ShouldReturnFalse_WhenPositionIsInvalid() {
 iterator = new DOMAttributeIterator(parentMock, name);
 iterator.getAttributes().add(mock(Attr.class));
 iterator.getAttributes().add(mock(Attr.class));
 Assert.assertFalse(iterator.setPosition(4));
 }

 @Test
 public void testGetAttributes_ShouldReturnListOfAttributes() {
 iterator = new DOMAttributeIterator(parentMock, name);
 iterator.getAttributes().add(mock(Attr.class));
 iterator.getAttributes().add(mock(Attr.class));
 List<Attr> attributes = iterator.getAttributes();
 Assert.assertEquals(2, attributes.size());
 }

}