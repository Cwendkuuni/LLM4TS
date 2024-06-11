import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DOMAttributeIteratorTest {

 private NodePointer parent;
 private QName name;

 @Before
 public void setUp() {
 parent = mock(NodePointer.class);
 name = new QName("test", null, "http://test.com");
 }

 @Test
 public void testConstructorWithElementAndWildcardName() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("*", null, null));
 List<Attr> expectedAttributes = new ArrayList<>();
 Assert.assertNotNull(iterator);
 Assert.assertEquals(0, iterator.getPosition());
 }

 @Test
 public void testConstructorWithElementAndSpecificName() {
 Element element = mock(Element.class);
 when(element.getAttributeNodeNS(Mockito.eq("http://test.com"), Mockito.eq("test"))).thenReturn(mock(Attr.class));
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test", "http://test.com", null));
 List<Attr> expectedAttributes = new ArrayList<>();
 expectedAttributes.add(mock(Attr.class));
 Assert.assertNotNull(iterator);
 Assert.assertEquals(0, iterator.getPosition());
 Assert.assertEquals(expectedAttributes, iterator.getAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullParent() {
 new DOMAttributeIterator(null, new QName("test", null, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullName() {
 new DOMAttributeIterator(parent, null);
 }

 @Test
 public void testNext() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test", null, null));
 iterator.next();
 iterator.next();
 DOMAttributePointer attributePointer = iterator.getNodePointer();
 Assert.assertNotNull(attributePointer);
 Assert.assertEquals(2, iterator.getPosition());
 }

 @Test
 public void testPrevious() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test", null, null));
 iterator.next();
 iterator.next();
 iterator.previous();
 DOMAttributePointer attributePointer = iterator.getNodePointer();
 Assert.assertNotNull(attributePointer);
 Assert.assertEquals(1, iterator.getPosition());
 }

 @Test
 public void testGetPosition() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test", null, null));
 iterator.next();
 iterator.next();
 Assert.assertEquals(2, iterator.getPosition());
 }

 @Test
 public void testSetPositionWithValidIndex() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test", null, null));
 iterator.setPosition(2);
 DOMAttributePointer attributePointer = iterator.getNodePointer();
 Assert.assertNotNull(attributePointer);
 Assert.assertEquals(2, iterator.getPosition());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionWithInvalidIndex() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test", null, null));
 iterator.setPosition(5);
 }

 @Test
 public void testGetAttributes() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test", null, null));
 List<Attr> expectedAttributes = new ArrayList<>();
 expectedAttributes.add(mock(Attr.class));
 expectedAttributes.add(mock(Attr.class));
 when(parent.getNodes(Mockito.eq(NodeIterator.CHILD_NODES))).thenReturn(expectedAttributes);
 Assert.assertEquals(expectedAttributes, iterator.getAttributes());
 }

 @Test
 public void testGetAttributesWithEmptyList() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test", null, null));
 when(parent.getNodes(Mockito.eq(NodeIterator.CHILD_NODES))).thenReturn(new ArrayList<>());
 Assert.assertNotNull(iterator.getAttributes());
 Assert.assertTrue(iterator.getAttributes().isEmpty());
 }

 @Test
 public void testGetAttributesWithNullParent() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(null, new QName("test", null, null));
 Assert.assertNull(iterator.getAttributes());
 }

}