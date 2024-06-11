import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.junit.Assert;
import org.junit.Test;

public class DOMAttributeIteratorTest {

 private NodePointer parent;
 private QName name;

 @Test
 public void testConstructorNullParent() {
     Assert.assertNull(new DOMAttributeIterator(null, new QName("test")));
 }

 @Test
 public void testConstructorNullName() {
     Element element = new Element("test");
     Assert.assertNotNull(new DOMAttributeIterator(new NodePointer(element), null));
 }

 @Test
 public void testConstructorEmptyList() {
     Element element = new Element("test");
     DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName(""));
     Assert.assertEquals(0, iterator.getPosition());
     Assert.assertNull(iterator.getNodePointer());
 }

 @Test
 public void testConstructorSingleAttribute() {
     Element element = new Element("test");
     Attr attribute = element.setAttributeNode(element.createAttribute("testAttribute"));
     DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("testAttribute"));
     Assert.assertEquals(1, iterator.getPosition());
     Assert.assertNotNull(iterator.getNodePointer());
     DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
     Assert.assertEquals("test", attributePointer.getNode().getNodeName());
     Assert.assertEquals("testAttribute", attributePointer.getAttribute().getName());
 }

 @Test
 public void testConstructorMultipleAttributes() {
     Element element = new Element("test");
     Attr attribute1 = element.setAttributeNode(element.createAttribute("testAttribute1"));
     Attr attribute2 = element.setAttributeNode(element.createAttribute("testAttribute2"));
     DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("testAttribute*"));
     Assert.assertEquals(2, iterator.getPosition());
     Assert.assertNotNull(iterator.getNodePointer());
     DOMAttributePointer attributePointer1 = (DOMAttributePointer) iterator.getNodePointer();
     Assert.assertEquals("test", attributePointer1.getNode().getNodeName());
     Assert.assertEquals("testAttribute1", attributePointer1.getAttribute().getName());
     Assert.assertEquals(1, iterator.next().getPosition());
     Assert.assertNotNull(iterator.getNodePointer());
     DOMAttributePointer attributePointer2 = (DOMAttributePointer) iterator.getNodePointer();
     Assert.assertEquals("test", attributePointer2.getNode().getNodeName());
     Assert.assertEquals("testAttribute2", attributePointer2.getAttribute().getName());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionNegative() {
     Element element = new Element("test");
     Attr attribute = element.setAttributeNode(element.createAttribute("testAttribute"));
     DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("testAttribute"));
     iterator.next();
     iterator.setPosition(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionGreaterThanSize() {
     Element element = new Element("test");
     Attr attribute1 = element.setAttributeNode(element.createAttribute("testAttribute1"));
     Attr attribute2 = element.setAttributeNode(element.createAttribute("testAttribute2"));
     DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("testAttribute*"));
     iterator.next();
     iterator.setPosition(3);
 }

 @Test
 public void testNext() {
     Element element = new Element("test");
     Attr attribute1 = element.setAttributeNode(element.createAttribute("testAttribute1"));
     Attr attribute2 = element.setAttributeNode(element.createAttribute("testAttribute2"));
     DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("testAttribute*"));
     Assert.assertEquals(1, iterator.getPosition());
     Assert.assertNotNull(iterator.getNodePointer());
     iterator.next();
     Assert.assertEquals(2, iterator.getPosition());
     Assert.assertNotNull(iterator.getNodePointer());
     iterator.next();
     Assert.assertEquals(0, iterator.getPosition());
     Assert.assertNull(iterator.getNodePointer());
 }

 @Test
 public void testHasNext() {
     Element element = new Element("test");
     Attr attribute1 = element.setAttributeNode(element.createAttribute("testAttribute1"));
     Attr attribute2 = element.setAttributeNode(element.createAttribute("testAttribute2"));
     DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("testAttribute*"));
     Assert.assertTrue(iterator.hasNext());
     Assert.assertTrue(iterator.hasNext());
     Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testGetPosition() {
     Element element = new Element("test");
     Attr attribute1 = element.setAttributeNode(element.createAttribute("testAttribute1"));
     Attr attribute2 = element.setAttributeNode(element.createAttribute("testAttribute2"));
     DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("testAttribute*"));
     Assert.assertEquals(1, iterator.getPosition());
     iterator.next();
     Assert.assertEquals(2, iterator.getPosition());
     iterator.next();
     Assert.assertEquals(0, iterator.getPosition());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetPositionWhenNoMoreAttributes() {
     Element element = new Element("test");
     Attr attribute1 = element.setAttributeNode(element.createAttribute("testAttribute1"));
     DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("testAttribute*"));
     iterator.next();
     iterator.setPosition(2);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetPositionWhenNoAttributes() {
     Element element = new Element("test");
     DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("testAttribute*"));
     iterator.setPosition(1);
 }

 @Test
 public void testGetNodePointerWithNoMoreAttributes() {
     Element element = new Element("test");
     Attr attribute1 = element.setAttributeNode(element.createAttribute("testAttribute1"));
     DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("testAttribute*"));
     iterator.next();
     iterator.next();
     Assert.assertNull(iterator.getNodePointer());
 }

 @Test
 public void testGetNodePointerWithFirstAttribute() {
     Element element = new Element("test");
     Attr attribute = element.setAttributeNode(element.createAttribute("testAttribute"));
     DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("testAttribute"));
     Assert.assertNotNull(iterator.getNodePointer());
 }

 @Test
 public void testGetNodePointerWithLastAttribute() {
     Element element = new Element("test");
     Attr attribute1 = element.setAttributeNode(element.createAttribute("testAttribute1"));
     Attr attribute2 = element.setAttributeNode(element.createAttribute("testAttribute2"));
     DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("testAttribute*"));
     Assert.assertNotNull(iterator.getNodePointer());
     iterator.next();
     Assert.assertNotNull(iterator.getNodePointer());
     Assert.assertEquals(2, iterator.getPosition());
     Assert.assertNotNull(iterator.getNodePointer());
 }
}