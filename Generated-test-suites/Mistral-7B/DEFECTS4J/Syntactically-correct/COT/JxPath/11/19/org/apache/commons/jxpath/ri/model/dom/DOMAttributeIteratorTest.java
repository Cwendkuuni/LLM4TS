import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
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
 private DOMAttributeIterator iterator;

 @Test
 public void testConstructor() {
 parent = new DOMPointNodePointer(null, null);
 name = new QName("", "test");
 iterator = new DOMAttributeIterator(parent, name);
 Assert.assertNotNull(iterator);
 }

 @Test
 public void testConstructorWithNullParent() {
 name = new QName("", "test");
 Assert.assertNull(new DOMAttributeIterator(null, name));
 }

 @Test
 public void testConstructorWithNullName() {
 parent = new DOMPointNodePointer(null, null);
 Assert.assertNull(new DOMAttributeIterator(parent, null));
 }

 @Test
 public void testHasNext() {
 parent = new DOMPointNodePointer(null, new Element() {});
 name = new QName("", "test");
 iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 Assert.assertTrue(iterator.hasNext());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNextWithoutAttributes() {
 parent = new DOMPointNodePointer(null, new Element() {});
 name = new QName("", "test");
 iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 iterator.next();
 }

 @Test
 public void testNextWithAttributes() {
 parent = new DOMPointNodePointer(null, new Element() {
 @AttributeWithNamespace(namespaceURI = "testNamespace", localName = "testAttribute")
 private String testAttribute;
 });
 name = new QName("testNamespace", "testAttribute");
 iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 Assert.assertEquals(iterator.getNodePointer().getNode(), parent.getNode().getAttributeNodeNS(name.getNamespaceURI(), name.getName()));
 }

 @Test
 public void testNextWithDifferentAttributes() {
 parent = new DOMPointNodePointer(null, new Element() {
 @AttributeWithNamespace(namespaceURI = "testNamespace1", localName = "testAttribute1")
 private String testAttribute1;
 @AttributeWithNamespace(namespaceURI = "testNamespace2", localName = "testAttribute2")
 private String testAttribute2;
 });
 name = new QName("testNamespace1", "testAttribute1");
 iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 name = new QName("testNamespace2", "testAttribute2");
 iterator.next();
 Assert.assertEquals(iterator.getNodePointer().getNode(), parent.getNode().getAttributeNodeNS(name.getNamespaceURI(), name.getName()));
 }

 @Test
 public void testGetPosition() {
 parent = new DOMPointNodePointer(null, new Element() {});
 name = new QName("", "test");
 iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 iterator.next();
 Assert.assertEquals(iterator.getPosition(), 2);
 }

 @Test
 public void testSetPosition() {
 parent = new DOMPointNodePointer(null, new Element() {
 @AttributeWithNamespace(namespaceURI = "testNamespace", localName = "testAttribute1")
 private String testAttribute1;
 @AttributeWithNamespace(namespaceURI = "testNamespace", localName = "testAttribute2")
 private String testAttribute2;
 });
 name = new QName("testNamespace", "testAttribute1");
 iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 Assert.assertEquals(iterator.getNodePointer().getNode(), parent.getNode().getAttributeNodeNS(name.getNamespaceURI(), name.getName()));
 iterator.setPosition(2);
 Assert.assertEquals(iterator.getNodePointer().getNode(), parent.getNode().getAttributeNodeNS(name.getNamespaceURI(), name.getName()));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionOutOfBounds() {
 parent = new DOMPointNodePointer(null, new Element() {
 @AttributeWithNamespace(namespaceURI = "testNamespace", localName = "testAttribute1")
 private String testAttribute1;
 @AttributeWithNamespace(namespaceURI = "testNamespace", localName = "testAttribute2")
 private String testAttribute2;
 });
 name = new QName("testNamespace", "testAttribute1");
 iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 iterator.setPosition(3);
 }

 @Test
 public void testGetNodePointer() {
 parent = new DOMPointNodePointer(null, new Element() {});
 name = new QName("", "test");
 iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 Assert.assertNotNull(iterator.getNodePointer());
 Assert.assertEquals(iterator.getNodePointer().getNode(), parent.getNode().getAttributeNodeNS("", "test"));
 }
}