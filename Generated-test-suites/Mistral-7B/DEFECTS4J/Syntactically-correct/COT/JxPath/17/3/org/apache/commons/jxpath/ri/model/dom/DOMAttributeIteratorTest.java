import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

 private NodePointer nodePointer;
 private DOMAttributeIterator attributeIterator;

 @Test
 public void testConstructor() {
 NodePointer parent = new NodePointer(null, null, null, null, 0);
 QName name = new QName("test", null, null, null, "*", null);
 attributeIterator = new DOMAttributeIterator(parent, name);
 Assert.assertNotNull(attributeIterator);
 }

 @Test
 public void testConstructorWithElement() {
 NodePointer parent = new NodePointer(null, null, null, null, 0);
 Element element = new Element.createElement("test");
 QName name = new QName("test", null, null, null, "*", null);
 attributeIterator = new DOMAttributeIterator(parent, name);
 attributeIterator.setParent(element);
 Assert.assertNotNull(attributeIterator);
 }

 @Test
 public void testConstructorWithAttribute() {
 NodePointer parent = new NodePointer(null, null, null, null, 0);
 Element element = new Element.createElement("test");
 Attr attribute = element.setAttributeNode(element.createAttribute("testAttr"));
 QName name = new QName("test", null, null, null, "*", null);
 attributeIterator = new DOMAttributeIterator(parent, name);
 attributeIterator.setParent(element);
 Assert.assertNotNull(attributeIterator);
 }

 @Test
 public void testConstructorWithAttributeList() {
 NodePointer parent = new NodePointer(null, null, null, null, 0);
 Element element = new Element.createElement("test");
 NamedNodeMap map = element.getAttributes();
 Attr attribute = (Attr) map.item(0);
 QName name = new QName("test", null, null, null, "*", null);
 attributeIterator = new DOMAttributeIterator(parent, name);
 attributeIterator.setParent(element);
 Assert.assertNotNull(attributeIterator);
 }

 @Test
 public void testConstructionWithEmptyAttributeList() {
 NodePointer parent = new NodePointer(null, null, null, null, 0);
 Element element = new Element.createElement("test");
 NamedNodeMap map = element.getAttributes();
 attributeIterator = new DOMAttributeIterator(parent, new QName("test", null, null, null, "*", null));
 attributeIterator.setParent(element);
 Assert.assertEquals(0, attributeIterator.size());
 }

 @Test
 public void testHasNext() {
 NodePointer parent = new NodePointer(null, null, null, null, 0);
 Element element = new Element.createElement("test");
 QName name = new QName("test", null, null, null, "*", null);
 attributeIterator = new DOMAttributeIterator(parent, name);
 attributeIterator.setParent(element);
 Attr attribute = element.setAttributeNode(element.createAttribute("testAttr"));
 attributeIterator.next();
 Assert.assertTrue(attributeIterator.hasNext());
 }

 @Test
 public void testNoNext() {
 NodePointer parent = new NodePointer(null, null, null, null, 0);
 Element element = new Element.createElement("test");
 QName name = new QName("test", null, null, null, "*", null);
 attributeIterator = new DOMAttributeIterator(parent, name);
 attributeIterator.setParent(element);
 Attr attribute1 = element.setAttributeNode(element.createAttribute("testAttr1"));
 Attr attribute2 = element.setAttributeNode(element.createAttribute("testAttr2"));
 attributeIterator.next();
 attributeIterator.next();
 Assert.assertFalse(attributeIterator.hasNext());
 }

 @Test
 public void testGetNodePointer() {
 NodePointer parent = new NodePointer(null, null, null, null, 0);
 Element element = new Element.createElement("test");
 QName name = new QName("test", null, null, null, "*", null);
 attributeIterator = new DOMAttributeIterator(parent, name);
 attributeIterator.setParent(element);
 Attr attribute = element.setAttributeNode(element.createAttribute("testAttr"));
 NodePointer nodePointer = attributeIterator.next();
 Assert.assertNotNull(nodePointer);
 Assert.assertTrue(nodePointer instanceof DOMAttributePointer);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetNodePointerWhenNoNext() {
 NodePointer parent = new NodePointer(null, null, null, null, 0);
 Element element = new Element.createElement("test");
 QName name = new QName("test", null, null, null, "*", null);
 attributeIterator = new DOMAttributeIterator(parent, name);
 attributeIterator.setParent(element);
 Attr attribute1 = element.setAttributeNode(element.createAttribute("testAttr1"));
 Attr attribute2 = element.setAttributeNode(element.createAttribute("testAttr2"));
 attributeIterator.next();
 attributeIterator.next();
 nodePointer = attributeIterator.next();
 }

 @Test
 public void testGetPosition() {
 NodePointer parent = new NodePointer(null, null, null, null, 0);
 Element element = new Element.createElement("test");
 QName name = new QName("test", null, null, null, "*", null);
 attributeIterator = new DOMAttributeIterator(parent, name);
 attributeIterator.setParent(element);
 Attr attribute1 = element.setAttributeNode(element.createAttribute("testAttr1"));
 Attr attribute2 = element.setAttributeNode(element.createAttribute("testAttr2"));
 attributeIterator.next();
 attributeIterator.next();
 Assert.assertEquals(2, attributeIterator.getPosition());
 }

 @Test
 public void testSetPosition() {
 NodePointer parent = new NodePointer(null, null, null, null, 0);
 Element element = new Element.createElement("test");
 QName name = new QName("test", null, null, null, "*", null);
 attributeIterator = new DOMAttributeIterator(parent, name);
 attributeIterator.setParent(element);
 Attr attribute1 = element.setAttributeNode(element.createAttribute("testAttr1"));
 Attr attribute2 = element.setAttributeNode(element.createAttribute("testAttr2"));
 attributeIterator.next();
 attributeIterator.next();
 attributeIterator.setPosition(1);
 Assert.assertEquals(attribute1, attributeIterator.getNodePointer().getNode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPositionWhenInvalidPosition() {
 NodePointer parent = new NodePointer(null, null, null, null, 0);
 Element element = new Element.createElement("test");
 QName name = new QName("test", null, null, null, "*", null);
 attributeIterator = new DOMAttributeIterator(parent, name);
 attributeIterator.setParent(element);
 Attr attribute1 = element.setAttributeNode(element.createAttribute("testAttr1"));
 Attr attribute2 = element.setAttributeNode(element.createAttribute("testAttr2"));
 attributeIterator.next();
 attributeIterator.next();
 attributeIterator.setPosition(3);
 }

 @Test
 public void testSize() {
 NodePointer parent = new NodePointer(null, null, null, null, 0);
 Element element = new Element.createElement("test");
 QName name = new QName("test", null, null, null, "*", null);
 attributeIterator = new DOMAttributeIterator(parent, name);
 attributeIterator.setParent(element);
 Attr attribute1 = element.setAttributeNode(element.createAttribute("testAttr1"));
 Attr attribute2 = element.setAttributeNode(element.createAttribute("testAttr2"));
 Assert.assertEquals(2, attributeIterator.size());
 }
}