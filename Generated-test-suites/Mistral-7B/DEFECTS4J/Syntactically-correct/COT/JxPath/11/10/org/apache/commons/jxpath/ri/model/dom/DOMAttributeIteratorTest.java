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
import org.junit.Test;

public class DOMAttributeIteratorTest {

 private NodePointer parent;
 private QName name;
 private DOMAttributeIterator iterator;

 @Test
 public void testConstructor_NullParent_ThrowsIllegalArgumentException() {
  Assert.assertThrows(IllegalArgumentException.class, () -> new DOMAttributeIterator(null, new QName("test")));
 }

 @Test
 public void testConstructor_NullName_ThrowsIllegalArgumentException() {
  NodePointer nodePointer = new NodePointer(null, 0, null);
  Assert.assertThrows(IllegalArgumentException.class, () -> new DOMAttributeIterator(nodePointer, null));
 }

 @Test
 public void testConstructor_ValidArguments() {
  Node root = new Element("root");
  parent = new NodePointer(root, 0, null);
  name = new QName("test");
  DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
 }

 @Test
 public void testGetNodePointer_InitialPosition_ReturnsExpectedAttribute() {
  Node root = new Element("root");
  NodePointer nodePointer = new NodePointer(root, 0, null);
  QName name = new QName("test");
  DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);
  Attr expectedAttribute = new Attr("test", "testValue");
  root.setAttributeNode(expectedAttribute);

  NodePointer actualNodePointer = iterator.getNodePointer();
  Assert.assertEquals(expectedAttribute, actualNodePointer.getNode());
 }

 @Test
 public void testGetNodePointer_PositionBeyondEnd_ReturnsNull() {
  Node root = new Element("root");
  NodePointer nodePointer = new NodePointer(root, 0, null);
  QName name = new QName("test");
  DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);
  iterator.setPosition(2);

  NodePointer actualNodePointer = iterator.getNodePointer();
  Assert.assertNull(actualNodePointer);
 }

 @Test
 public void testGetNodePointer_PositionOutofBounds_ReturnsNull() {
  Node root = new Element("root");
  NodePointer nodePointer = new NodePointer(root, 0, null);
  QName name = new QName("test");
  DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);
  iterator.setPosition(-1);

  NodePointer actualNodePointer = iterator.getNodePointer();
  Assert.assertNull(actualNodePointer);
 }

 @Test
 public void testGetNodePointer_PositionValid_ReturnsExpectedAttribute() {
  Node root = new Element("root");
  NodePointer nodePointer = new NodePointer(root, 0, null);
  QName name = new QName("test");
  Attr attr1 = new Attr("test1", "testValue1");
  Attr attr2 = new Attr("test2", "testValue2");
  Attr attr3 = new Attr("test3", "testValue3");
  root.setAttributeNode(attr1);
  root.setAttributeNode(attr2);
  root.setAttributeNode(attr3);
  DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);

  iterator.setPosition(1);
  NodePointer actualNodePointer = iterator.getNodePointer();
  Assert.assertEquals(attr2, actualNodePointer.getNode());
 }

 @Test
 public void testGetPosition_InitialPosition_ReturnsZero() {
  NodePointer nodePointer = new NodePointer(null, 0, null);
  QName name = new QName("test");
  DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);

  int actualPosition = iterator.getPosition();
  Assert.assertEquals(0, actualPosition);
 }

 @Test
 public void testGetPosition_PositionBeyondEnd_ReturnsSizeofList() {
  Node root = new Element("root");
  NodePointer nodePointer = new NodePointer(root, 0, null);
  QName name = new QName("test");
  Attr attr1 = new Attr("test1", "testValue1");
  Attr attr2 = new Attr("test2", "testValue2");
  Attr attr3 = new Attr("test3", "testValue3");
  root.setAttributeNode(attr1);
  root.setAttributeNode(attr2);
  root.setAttributeNode(attr3);
  DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);

  int actualPosition = iterator.getPosition();
  int expectedPosition = iterator.size();
  Assert.assertEquals(expectedPosition, actualPosition);
 }

 @Test
 public void testGetPosition_PositionGreaterThanSize_ThrowsIndexOutOfBoundsException() {
  Node root = new Element("root");
  NodePointer nodePointer = new NodePointer(root, 0, null);
  QName name = new QName("test");
  Attr attr1 = new Attr("test1", "testValue1");
  Attr attr2 = new Attr("test2", "testValue2");
  Attr attr3 = new Attr("test3", "testValue3");
  root.setAttributeNode(attr1);
  root.setAttributeNode(attr2);
  root.setAttributeNode(attr3);
  DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);

  Assert.assertThrows(IndexOutOfBoundsException.class, () -> iterator.setPosition(4));
 }

 @Test
 public void testSetPosition_PositionValid_SetsPositionCorrectly() {
  Node root = new Element("root");
  NodePointer nodePointer = new NodePointer(root, 0, null);
  QName name = new QName("test");
  Attr attr1 = new Attr("test1", "testValue1");
  Attr attr2 = new Attr("test2", "testValue2");
  Attr attr3 = new Attr("test3", "testValue3");
  root.setAttributeNode(attr1);
  root.setAttributeNode(attr2);
  root.setAttributeNode(attr3);
  DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);

  iterator.setPosition(2);
  Assert.assertEquals(2, iterator.getPosition());
 }

 @Test
 public void testSetPosition_PositionInvalid_ThrowsIndexOutOfBoundsException() {
  Node root = new Element("root");
  NodePointer nodePointer = new NodePointer(root, 0, null);
  QName name = new QName("test");
  Attr attr1 = new Attr("test1", "testValue1");
  Attr attr2 = new Attr("test2", "testValue2");
  Attr attr3 = new Attr("test3", "testValue3");
  root.setAttributeNode(attr1);
  root.setAttributeNode(attr2);
  root.setAttributeNode(attr3);
  DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);

  Assert.assertThrows(IndexOutOfBoundsException.class, () -> iterator.setPosition(4));
 }
}