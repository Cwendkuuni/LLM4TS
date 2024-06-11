import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMParentNodePointer;

public class DOMAttributeIteratorTest {

 private NodeIterator createDOMAttributeIterator(Node node, QName name) {
  return new DOMAttributeIterator(new DOMParentNodePointer(null, node), name);
 }

 @Test
 public void testConstructorWithElementAndQName() {
  Element element = new org.w3c.dom.ElementImpl();
  QName name = new QName("test", "testPrefix", "testNamespace");
  DOMAttributeIterator iterator = createDOMAttributeIterator(element, name);
  assertNull(iterator.getNodePointer());
  assertFalse(iterator.setPosition(0));
  assertFalse(iterator.setPosition(-1));
 }

 @Test
 public void testConstructorWithElementAndWildQName() {
  Element element = new org.w3c.dom.ElementImpl();
  QName name = new QName("*");
  DOMAttributeIterator iterator = createDOMAttributeIterator(element, name);
  assertNotNull(iterator.getNodePointer());
  assertTrue(iterator.setPosition(1));
  assertIteratorPosition(iterator, 0);
  iterator.next();
  assertIteratorPosition(iterator, 1);
 }

 @Test
 public void testConstructorWithNodeAndQName() {
  Element element = new org.w3c.dom.ElementImpl();
  Attr attr = element.setAttributeNode(new Attr("test", "testValue"));
  Node node = attr;
  QName name = new QName("test", "testPrefix", "testNamespace");
  DOMAttributeIterator iterator = createDOMAttributeIterator(node, name);
  assertNull(iterator.getNodePointer());
  assertFalse(iterator.setPosition(0));
  assertFalse(iterator.setPosition(-1));
 }

 @Test
 public void testConstructorWithNodeAndWildQName() {
  Element element = new org.w3c.dom.ElementImpl();
  Attr attr1 = element.setAttributeNode(new Attr("test1", "testValue1"));
  Attr attr2 = element.setAttributeNode(new Attr("test2", "testValue2"));
  Node node = attr1;
  QName name = new QName("*");
  DOMAttributeIterator iterator = createDOMAttributeIterator(node, name);
  assertNotNull(iterator.getNodePointer());
  assertTrue(iterator.setPosition(1));
  assertIteratorPosition(iterator, 0);
  iterator.next();
  assertIteratorPosition(iterator, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullNode() {
  QName name = new QName("test");
  DOMAttributeIterator iterator = new DOMAttributeIterator(null, name);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullQName() {
  Node node = new org.w3c.dom.ElementImpl();
  DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentNodePointer(null, node), null);
 }

 @Test
 public void testSetPositionWithInvalidPosition() {
  Element element = new org.w3c.dom.ElementImpl();
  QName name = new QName("test");
  DOMAttributeIterator iterator = createDOMAttributeIterator(element, name);
  assertFalse(iterator.setPosition(0));
  assertFalse(iterator.setPosition(-1));
  assertFalse(iterator.setPosition(10));
 }

 @Test
 public void testSetPositionWithValidPosition() {
  Element element = new org.w3c.dom.ElementImpl();
  QName name = new QName("test");
  DOMAttributeIterator iterator = createDOMAttributeIterator(element, name);
  assertTrue(iterator.setPosition(1));
  assertIteratorPosition(iterator, 0);
  iterator.next();
  assertTrue(iterator.setPosition(1));
  assertIteratorPosition(iterator, 1);
 }

 @Test
 public void testGetPosition() {
  Element element = new org.w3c.dom.ElementImpl();
  QName name = new QName("test");
  DOMAttributeIterator iterator = createDOMAttributeIterator(element, name);
  assertFalse(iterator.setPosition(0));
  assertFalse(iterator.setPosition(-1));
  assertFalse(iterator.setPosition(10));
  assertPosition(iterator, 0);
  iterator.next();
  assertPosition(iterator, 1);
 }

 @Test
 public void testGetNodePointer() {
  Element element = new org.w3c.dom.ElementImpl();
  QName name = new QName("test");
  DOMAttributeIterator iterator = createDOMAttributeIterator(element, name);
  assertNull(iterator.getNodePointer());
  iterator.next();
  DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
  assertEquals("test", attributePointer.getAttribute().getName());
  assertEquals("testValue", attributePointer.getAttribute().getValue());
  assertEquals("testNamespace", attributePointer.getNamespaceURI());
  assertEquals("testPrefix", attributePointer.getPrefix());
 }

 @Test
 public void testHasNextAttribute() {
  Element element = new org.w3c.dom.ElementImpl();
  QName name = new QName("test");
  DOMAttributeIterator iterator = createDOMAttributeIterator(element, name);
  assertFalse(iterator.hasNext());
  element.setAttributeNode(new Attr("test1", "testValue1"));
  assertTrue(iterator.hasNext());
  iterator.next();
  assertFalse(iterator.hasNext());
 }

 @Test
 public void testNextAttribute() {
  Element element = new org.w3c.dom.ElementImpl();
  QName name = new QName("test");
  DOMAttributeIterator iterator = createDOMAttributeIterator(element, name);
  assertFalse(iterator.hasNext());
  element.setAttributeNode(new Attr("test1", "testValue1"));
  iterator.next();
  DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
  assertEquals("test1", attributePointer.getAttribute().getName());
  assertEquals("testValue1", attributePointer.getAttribute().getValue());
  assertEquals("", attributePointer.getNamespaceURI());
  assertEquals("", attributePointer.getPrefix());
  assertFalse(iterator.hasNext());
 }

 @Test
 public void testGetPositionWithInvalidPosition() {
  Element element = new org.w3c.dom.ElementImpl();
  QName name = new QName("test");
  DOMAttributeIterator iterator = createDOMAttributeIterator(element, name);
  assertFalse(iterator.setPosition(0));
  assertFalse(iterator.setPosition(-1));
  assertFalse(iterator.setPosition(10));
  int position = iterator.getPosition();
  Assert.assertEquals(-1, position);
 }

 @Test
 public void testGetPositionWithValidPosition() {
  Element element = new org.w3c.dom.ElementImpl();
  QName name = new QName("test");
  DOMAttributeIterator iterator = createDOMAttributeIterator(element, name);
  assertFalse(iterator.setPosition(0));
  assertFalse(iterator.setPosition(-1));
  assertFalse(iterator.setPosition(10));
  iterator.next();
  int position = iterator.getPosition();
  Assert.assertEquals(1, position);
 }

 private void assertIteratorPosition(DOMAttributeIterator iterator, int expectedPosition) {
  int actualPosition = iterator.getPosition();
  Assert.assertEquals(expectedPosition, actualPosition);
 }

 private void assertPosition(DOMAttributeIterator iterator, int expectedPosition) {
  int actualPosition = iterator.getPosition();
  Assert.assertEquals(expectedPosition, actualPosition);
 }
}