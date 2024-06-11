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

 @Test
 public void testConstructor() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(null, null), null);
 Assert.assertNull(iterator.getNodePointer());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testConstructorWithElementAndName() {
 Element element = new Element("test");
 parent = new NodePointer(null, element);
 name = new QName("test");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
 Assert.assertNotNull(iterator.getNodePointer());
 Assert.assertTrue(iterator.hasNext());
 }

 @Test
 public void testConstructorWithElementAndWildcardName() {
 Element element = new Element("test");
 parent = new NodePointer(null, element);
 name = new QName("*");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
 Assert.assertNotNull(iterator.getNodePointer());
 Assert.assertTrue(iterator.hasNext());
 }

 @Test
 public void testConstructorWithElementAndSpecificName() {
 Element element = new Element("test");
 parent = new NodePointer(null, element);
 name = new QName("test:specificName");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testConstructorWithNullParent() {
 name = new QName("test");
 DOMAttributeIterator iterator = new DOMAttributeIterator(null, name);
 Assert.assertNull(iterator.getNodePointer());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testConstructorWithNullName() {
 parent = new NodePointer(null, new Element("test"));
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, null);
 Assert.assertNull(iterator.getNodePointer());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testTestAttr() {
 Attr attr = new Attr("test", "test");
 Attr attr2 = new Attr("test2", "test2");
 Attr attr3 = new Attr("xmlns:test", "test");
 Attr attr4 = new Attr("xmlns", "test");

 Assert.assertTrue(DOMAttributeIterator.testAttr(attr));
 Assert.assertTrue(DOMAttributeIterator.testAttr(attr2));
 Assert.assertFalse(DOMAttributeIterator.testAttr(attr3));
 Assert.assertFalse(DOMAttributeIterator.testAttr(attr4));
 }

 @Test
 public void testGetAttribute() {
 Element element = new Element("test");
 element.setAttributeNode(new Attr("test1", "test1"));
 element.setAttributeNode(new Attr("test2", "test2"));
 parent = new NodePointer(null, element);
 name = new QName("test");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

 Attr attribute = iterator.getNodePointer().getNode();
 Assert.assertNotNull(attribute);
 Assert.assertEquals("test1", attribute.getValue());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetNodePointerWithInvalidPosition() {
 parent = new NodePointer(null, new Element("test"));
 name = new QName("test");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
 iterator.setPosition(-1);
 iterator.getNodePointer();
 }

 @Test
 public void testGetPosition() {
 parent = new NodePointer(null, new Element("test"));
 name = new QName("test");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
 iterator.setPosition(3);
 Assert.assertEquals(3, iterator.getPosition());
 }

 @Test
 public void testSetPositionWithInvalidPosition() {
 parent = new NodePointer(null, new Element("test"));
 name = new QName("test");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
 iterator.setPosition(0);
 Assert.assertEquals(0, iterator.getPosition());
 iterator.setPosition(5);
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testSetPositionWithValidPosition() {
 parent = new NodePointer(null, new Element("test"));
 name = new QName("test");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
 iterator.setPosition(1);
 Assert.assertTrue(iterator.hasNext());
 }

 @Test
 public void testHasNext() {
 parent = new NodePointer(null, new Element("test"));
 name = new QName("test");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
 iterator.setPosition(0);
 Assert.assertTrue(iterator.hasNext());
 iterator.next();
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testNext() {
 parent = new NodePointer(null, new Element("test"));
 name = new QName("test");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
 DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.next();
 Assert.assertNotNull(attributePointer.getNode());
 iterator.next();
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testNextAndSetPosition() {
 parent = new NodePointer(null, new Element("test"));
 name = new QName("test");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
 DOMAttributePointer attributePointer1 = (DOMAttributePointer) iterator.next();
 iterator.next();
 DOMAttributePointer attributePointer2 = (DOMAttributePointer) iterator.next();
 iterator.setPosition(1);
 DOMAttributePointer attributePointer3 = (DOMAttributePointer) iterator.next();
 Assert.assertSame(attributePointer1.getNode(), attributePointer3.getNode());
 }

 @Test
 public void testGetPositionAfterSetPosition() {
 parent = new NodePointer(null, new Element("test"));
 name = new QName("test");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
 iterator.setPosition(1);
 Assert.assertEquals(1, iterator.getPosition());
 }
}