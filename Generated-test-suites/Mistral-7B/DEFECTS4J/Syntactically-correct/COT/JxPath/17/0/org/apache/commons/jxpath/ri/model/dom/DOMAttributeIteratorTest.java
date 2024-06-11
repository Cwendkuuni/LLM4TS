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

 private NodePointer nodePointer;
 private DOMAttributeIterator iterator;

 @Test
 public void testConstructor() {
 nodePointer = new NodePointer(null, null, null);
 QName name = new QName("test", null, null);
 iterator = new DOMAttributeIterator(nodePointer, name);
 Assert.assertNotNull(iterator);
 }

 @Test
 public void testTestAttr() {
 Attr attr = new Attr("test", "test");
 String prefix = "test";
 String localName = "test";
 String ns = "test";

 boolean result = DOMAttributeIterator.testAttr(attr);
 Assert.assertFalse(result);

 result = DOMAttributeIterator.testAttr(attr);
 result = DOMAttributeIterator.testAttr(attr, prefix, localName, ns);
 Assert.assertTrue(result);
 }

 @Test
 public void testGetAttribute() {
 Element element = new Element("test");
 Attr attr = new Attr("test", "test");
 element.setAttributeNode(attr);
 QName name = new QName("test", null, null);
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element, null, null), name);
 Attr attribute = iterator.getAttribute(element, name);
 Assert.assertSame(attr, attribute);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAttributeNullElement() {
 Element element = null;
 QName name = new QName("test", null, null);
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element, null, null), name);
 iterator.getAttribute(element, name);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAttributeNullName() {
 Element element = new Element("test");
 QName name = null;
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element, null, null), name);
 iterator.getAttribute(element, name);
 }

 @Test
 public void testSetPosition() {
 NodePointer nodePointer = new NodePointer(null, null, null);
 QName name = new QName("test", null, null);
 DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);
 iterator.setPosition(0);
 iterator.setPosition(1);
 iterator.setPosition(2);
 iterator.setPosition(3);
 iterator.setPosition(-1);
 iterator.setPosition(0);
 }

 @Test
 public void testGetPosition() {
 NodePointer nodePointer = new NodePointer(null, null, null);
 QName name = new QName("test", null, null);
 DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);
 iterator.setPosition(3);
 int position = iterator.getPosition();
 Assert.assertEquals(3, position);
 }

 @Test
 public void testgetNodePointer() {
 NodePointer nodePointer = new NodePointer(null, null, null);
 QName name = new QName("test", null, null);
 DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);
 iterator.setPosition(1);
 NodePointer nodePointer = iterator.getNodePointer();
 Assert.assertNotNull(nodePointer);
 Assert.assertSame(nodePointer.getNode(), iterator.getNodePointer().getNode());
 Assert.assertEquals(1, iterator.getPosition());
 }

 @Test
 public void testIteratorNext() {
 NodePointer nodePointer = new NodePointer(null, null, null);
 QName name = new QName("test", null, null);
 DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);
 iterator.setPosition(1);
 NodePointer nodePointerNext = iterator.getNodePointer();
 iterator.next();
 NodePointer nodePointerNextNext = iterator.getNodePointer();
 Assert.assertNotNull(nodePointerNext);
 Assert.assertNotNull(nodePointerNextNext);
 Assert.assertFalse(nodePointerNext.equals(nodePointerNextNext));
 }

 @Test
 public void testIteratorHasNext() {
 NodePointer nodePointer = new NodePointer(null, null, null);
 QName name = new QName("test", null, null);
 DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);
 Assert.assertFalse(iterator.hasNext());
 NodePointer nodePointerNext = new DOMAttributePointer(nodePointer, new Attr("test", "test"));
 iterator.setPosition(1);
 iterator.add(nodePointerNext);
 Assert.assertTrue(iterator.hasNext());
 }

 @Test
 public void testHasNextAfterLastElement() {
 NodePointer nodePointer = new NodePointer(null, null, null);
 QName name = new QName("test", null, null);
 DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);
 iterator.setPosition(1);
 iterator.next();
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testHasNextWithEmptyIterator() {
 NodePointer nodePointer = new NodePointer(null, null, null);
 QName name = new QName("test", null, null);
 DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorToString() {
 NodePointer nodePointer = new NodePointer(null, null, null);
 QName name = new QName("test", null, null);
 DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);
 iterator.setPosition(1);
 String toString = iterator.toString();
 Assert.assertEquals("DOMAttributeIterator[position=1]", toString);
 }

 @Test
 public void testIteratorEquals() {
 NodePointer nodePointer1 = new NodePointer(null, null, null);
 NodePointer nodePointer2 = new NodePointer(null, null, null);
 QName name1 = new QName("test", null, null);
 QName name2 = new QName("test", null, null);
 DOMAttributeIterator iterator1 = new DOMAttributeIterator(nodePointer1, name1);
 DOMAttributeIterator iterator2 = new DOMAttributeIterator(nodePointer2, name2);
 iterator1.setPosition(1);
 iterator2.setPosition(1);
 Assert.assertEquals(iterator1, iterator2);
 }

 @Test
 public void testIteratorNotEquals() {
 NodePointer nodePointer1 = new NodePointer(null, null, null);
 NodePointer nodePointer2 = new NodePointer(null, null, null);
 QName name1 = new QName("test", null, null);
 QName name2 = new QName("test", null, null);
 DOMAttributeIterator iterator1 = new DOMAttributeIterator(nodePointer1, name1);
 DOMAttributeIterator iterator2 = new DOMAttributeIterator(nodePointer2, name2);
 iterator1.setPosition(1);
 iterator2.setPosition(2);
 Assert.assertNotEquals(iterator1, iterator2);
 }

 @Test
 public void testIteratorHashCode() {
 NodePointer nodePointer = new NodePointer(null, null, null);
 QName name = new QName("test", null, null);
 DOMAttributeIterator iterator1 = new DOMAttributeIterator(nodePointer, name);
 iterator1.setPosition(1);
 DOMAttributeIterator iterator2 = new DOMAttributeIterator(nodePointer, name);
 iterator2.setPosition(2);
 Assert.assertNotEquals(iterator1.hashCode(), iterator2.hashCode());
 }

 @Test
 public void testIteratorClone() {
 NodePointer nodePointer = new NodePointer(null, null, null);
 QName name = new QName("test", null, null);
 DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);
 iterator.setPosition(1);
 DOMAttributeIterator iteratorClone = (DOMAttributeIterator) iterator.clone();
 Assert.assertNotSame(iterator, iteratorClone);
 Assert.assertEquals(iterator, iteratorClone);
 }

 @Test
 public void testConstructorWithNullParent() {
 NodePointer nodePointer = null;
 QName name = new QName("test", null, null);
 try {
 DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);
 Assert.fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testConstructorWithNullName() {
 NodePointer nodePointer = new NodePointer(null, null, null);
 try {
 DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, null);
 Assert.fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetAttributesNullElement() {
 Element element = null;
 QName name = new QName("test", null, null);
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element, null, null), name);
 List attributes = iterator.getAttributes();
 Assert.assertNull(attributes);
 }

 @Test
 public void testGetAttributesEmptyElement() {
 Element element = new Element("test");
 QName name = new QName("test", null, null);
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element, null, null), name);
 List attributes = iterator.getAttributes();
 Assert.assertEquals(0, attributes.size());
 }

 @Test
 public void testGetAttributesSingleAttribute() {
 Element element = new Element("test");
 Attr attr = new Attr("test", "test");
 element.setAttributeNode(attr);
 QName name = new QName("test", null, null);
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element, null, null), name);
 List attributes = iterator.getAttributes();
 Assert.assertEquals(1, attributes.size());
 Assert.assertSame(attr, attributes.get(0));
 }

 @Test
 public void testGetAttributesMultipleAttributes() {
 Element element = new Element("test");
 Attr attr1 = new Attr("test1", "test1");
 Attr attr2 = new Attr("test2", "test2");
 element.setAttributeNode(attr1);
 element.setAttributeNode(attr2);
 QName name = new QName("test", null, null);
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element, null, null), name);
 List attributes = iterator.getAttributes();
 Assert.assertEquals(2, attributes.size());
 Assert.assertTrue(attributes.contains(attr1));
 Assert.assertTrue(attributes.contains(attr2));
 }

 @Test
 public void testGetAttributesNullName() {
 Element element = new Element("test");
 Attr attr = new Attr("test", "test");
 element.setAttributeNode(attr);
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element, null, null), null);
 List attributes = iterator.getAttributes();
 Assert.assertNull(attributes);
 }

 @Test
 public void testGetAttributesWithWildcardName() {
 Element element = new Element("test");
 Attr attr1 = new Attr("test1", "test1");
 Attr attr2 = new Attr("test2", "test2");
 element.setAttributeNode(attr1);
 element.setAttributeNode(attr2);
 QName name = new QName("*", null, null);
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element, null, null), name);
 List attributes = iterator.getAttributes();
 Assert.assertEquals(2, attributes.size());
 Assert.assertTrue(attributes.contains(attr1));
 Assert.assertTrue(attributes.contains(attr2));
 }

 @Test
 public void testGetAttributesWithPrefixAndLocalName() {
 Element element = new Element("test");
 Attr attr = new Attr("test:test", "test");
 element.setAttributeNode(attr);
 QName name = new QName("test", null, null);
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element, null, null), name);
 List attributes = iterator.getAttributes();
 Assert.assertEquals(1, attributes.size());
 Assert.assertSame(attr, attributes.get(0));
 }

 @Test
 public void testGetAttributesWithNamespaceUri() {
 Element element = new Element("test");
 Attr attr = new Attr("test:test", "test");
 element.setAttributeNode(attr);
 String namespaceUri = "test";
 QName name = new QName("test", namespaceUri, null);
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element, null, null), name);
 List attributes = iterator.getAttributes();
 Assert.assertEquals(1, attributes.size());
 Assert.assertSame(attr, attributes.get(0));
 }
}