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

public class DOMAttributeIteratorTest {

 private NodePointer parent;
 private QName name;
 private DOMAttributeIterator iterator;

 @Test
 public void testConstructor() {
 parent = new NodePointer(null, null, null, 0, null);
 name = new QName("ns:test", "test");
 iterator = new DOMAttributeIterator(parent, name);
 Assert.assertNotNull(iterator);
 }

 @Test
 public void testTestAttr() {
 Attr attr = createAttr("test1", "ns1:test1", "ns1", "value1");
 Attr attr2 = createAttr("test2", "ns2:test2", "ns2", "value2");
 Attr attr3 = createAttr("test3", "test3", null, "value3");

 Assert.assertTrue(DOMAttributeIterator.testAttr(attr, new QName("ns1:test1")));
 Assert.assertTrue(DOMAttributeIterator.testAttr(attr, new QName("test1")));
 Assert.assertTrue(DOMAttributeIterator.testAttr(attr, new QName("", "test1")));
 Assert.assertTrue(DOMAttributeIterator.testAttr(attr, new QName("ns1", "test1")));
 Assert.assertFalse(DOMAttributeIterator.testAttr(attr, new QName("ns1:test2")));
 Assert.assertFalse(DOMAttributeIterator.testAttr(attr, new QName("ns2:test2")));
 Assert.assertFalse(DOMAttributeIterator.testAttr(attr, new QName("test2")));
 Assert.assertFalse(DOMAttributeIterator.testAttr(attr, new QName("", "test2")));
 Assert.assertFalse(DOMAttributeIterator.testAttr(attr, new QName("ns1", "test2")));
 Assert.assertFalse(DOMAttributeIterator.testAttr(attr, new QName("ns1:test3")));
 Assert.assertFalse(DOMAttributeIterator.testAttr(attr, new QName("test3")));
 Assert.assertFalse(DOMAttributeIterator.testAttr(attr, new QName("", "test3")));

 Assert.assertFalse(DOMAttributeIterator.testAttr(attr2, new QName("ns1:test1")));
 Assert.assertFalse(DOMAttributeIterator.testAttr(attr2, new QName("test1")));
 Assert.assertFalse(DOMAttributeIterator.testAttr(attr2, new QName("", "test1")));
 Assert.assertFalse(DOMAttributeIterator.testAttr(attr2, new QName("ns1", "test1")));
 Assert.assertTrue(DOMAttributeIterator.testAttr(attr2, new QName("ns2:test2")));
 Assert.assertTrue(DOMAttributeIterator.testAttr(attr2, new QName("test2")));
 Assert.assertFalse(DOMAttributeIterator.testAttr(attr2, new QName("", "test2")));
 Assert.assertFalse(DOMAttributeIterator.testAttr(attr2, new QName("ns1", "test2")));
 Assert.assertFalse(DOMAttributeIterator.testAttr(attr2, new QName("ns1:test3")));
 Assert.assertFalse(DOMAttributeIterator.testAttr(attr2, new QName("test3")));
 Assert.assertFalse(DOMAttributeIterator.testAttr(attr2, new QName("", "test3")));

 Assert.assertTrue(DOMAttributeIterator.testAttr(attr3, new QName("test3")));
 Assert.assertTrue(DOMAttributeIterator.testAttr(attr3, new QName("", "test3")));
 Assert.assertFalse(DOMAttributeIterator.testAttr(attr3, new QName("ns1:test1")));
 Assert.assertFalse(DOMAttributeIterator.testAttr(attr3, new QName("test1")));
 Assert.assertFalse(DOMAttributeIterator.testAttr(attr3, new QName("ns1", "test1")));
 Assert.assertFalse(DOMAttributeIterator.testAttr(attr3, new QName("ns2:test2")));
 Assert.assertFalse(DOMAttributeIterator.testAttr(attr3, new QName("test2")));
 Assert.assertFalse(DOMAttributeIterator.testAttr(attr3, new QName("ns1:test3")));
 }

 @Test
 public void testConstructorWithElement() {
 Element element = createElement("root");
 Attr attr = createAttr("test1", "ns1:test1", "ns1", "value1");
 element.setAttributeNode(attr);

 parent = new NodePointer(null, null, element, 0, null);
 name = new QName("ns1:test1");
 iterator = new DOMAttributeIterator(parent, name);

 NodePointer nodePointer = iterator.getNodePointer();
 Assert.assertNotNull(nodePointer);
 Assert.assertEquals(attr, nodePointer.getNode());
 }

 @Test
 public void testConstructorWithWildcard() {
 parent = new NodePointer(null, null, null, 0, null);
 name = new QName("*");
 iterator = new DOMAttributeIterator(parent, name);
 Assert.assertNotNull(iterator);
 }

 @Test
 public void testGetPosition() {
 parent = new NodePointer(null, null, null, 0, null);
 name = new QName("test");
 iterator = new DOMAttributeIterator(parent, name);
 iterator.setPosition(2);
 Assert.assertEquals(2, iterator.getPosition());
 }

 @Test
 public void testSetPosition() {
 parent = new NodePointer(null, null, null, 0, null);
 name = new QName("test");
 iterator = new DOMAttributeIterator(parent, name);
 Attr attr1 = createAttr("test1", null, null, "value1");
 Attr attr2 = createAttr("test2", null, null, "value2");
 Attr attr3 = createAttr("test3", null, null, "value3");
 iterator.addAttribute(attr1);
 iterator.addAttribute(attr2);
 iterator.addAttribute(attr3);

 iterator.setPosition(1);
 Assert.assertEquals(attr2, iterator.getNodePointer().getNode());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionOutOfBounds() {
 parent = new NodePointer(null, null, null, 0, null);
 name = new QName("test");
 iterator = new DOMAttributeIterator(parent, name);
 Attr attr1 = createAttr("test1", null, null, "value1");
 Attr attr2 = createAttr("test2", null, null, "value2");
 Attr attr3 = createAttr("test3", null, null, "value3");
 iterator.addAttribute(attr1);
 iterator.addAttribute(attr2);
 iterator.addAttribute(attr3);

 iterator.setPosition(4);
 }

 @Test
 public void testHasNext() {
 parent = new NodePointer(null, null, null, 0, null);
 name = new QName("test");
 iterator = new DOMAttributeIterator(parent, name);
 Attr attr1 = createAttr("test1", null, null, "value1");
 Attr attr2 = createAttr("test2", null, null, "value2");
 Attr attr3 = createAttr("test3", null, null, "value3");
 iterator.addAttribute(attr1);
 iterator.addAttribute(attr2);
 iterator.addAttribute(attr3);

 Assert.assertTrue(iterator.hasNext());
 Assert.assertTrue(iterator.hasNext());
 Assert.assertTrue(iterator.hasNext());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testNext() {
 parent = new NodePointer(null, null, null, 0, null);
 name = new QName("test");
 iterator = new DOMAttributeIterator(parent, name);
 Attr attr1 = createAttr("test1", null, null, "value1");
 Attr attr2 = createAttr("test2", null, null, "value2");
 Attr attr3 = createAttr("test3", null, null, "value3");
 iterator.addAttribute(attr1);
 iterator.addAttribute(attr2);
 iterator.addAttribute(attr3);

 NodePointer nodePointer1 = iterator.next();
 Assert.assertEquals(attr1, nodePointer1.getNode());

 NodePointer nodePointer2 = iterator.next();
 Assert.assertEquals(attr2, nodePointer2.getNode());

 NodePointer nodePointer3 = iterator.next();
 Assert.assertEquals(attr3, nodePointer3.getNode());

 Assert.assertNull(iterator.next());
 }

 @Test
 public void testHasNextAfterLast() {
 parent = new NodePointer(null, null, null, 0, null);
 name = new QName("test");
 iterator = new DOMAttributeIterator(parent, name);
 Attr attr1 = createAttr("test1", null, null, "value1");
 Attr attr2 = createAttr("test2", null, null, "value2");
 Attr attr3 = createAttr("test3", null, null, "value3");
 iterator.addAttribute(attr1);
 iterator.addAttribute(attr2);
 iterator.addAttribute(attr3);

 iterator.next();
 iterator.next();
 iterator.next();

 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testGetNodeIterator() {
 parent = new NodePointer(null, null, null, 0, null);
 name = new QName("test");
 iterator = new DOMAttributeIterator(parent, name);
 Attr attr1 = createAttr("test1", null, null, "value1");
 Attr attr2 = createAttr("test2", null, null, "value2");
 Attr attr3 = createAttr("test3", null, null, "value3");
 iterator.addAttribute(attr1);
 iterator.addAttribute(attr2);
 iterator.addAttribute(attr3);

 NodeIterator nodeIterator = iterator.getNextSiblingIterator();
 NodePointer nodePointer1 = nodeIterator.next();
 Assert.assertEquals(attr2, nodePointer1.getNode());

 NodePointer nodePointer2 = nodeIterator.next();
 Assert.assertEquals(attr3, nodePointer2.getNode());

 Assert.assertNull(nodeIterator.next());
 }

 private Node createElement(String tagName) {
 Element element = document.createElement(tagName);
 return element;
 }

 private Attr createAttr(String localName, String prefix, String namespaceURI, String value) {
 Attr attr = document.createAttribute(localName);
 attr.setNodeValue(value);
 if (prefix != null && namespaceURI != null) {
 attr.setNamespaceURI(namespaceURI);
 attr.setPrefix(prefix);
 }
 return attr;
 }
}