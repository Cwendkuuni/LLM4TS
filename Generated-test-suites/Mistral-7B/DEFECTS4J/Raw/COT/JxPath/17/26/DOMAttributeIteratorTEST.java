import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class DOMAttributeIteratorTest {

 private NodePointer nodePointer;
 private DOMAttributeIterator iterator;

 @org.junit.Before
 public void setUp() {
 nodePointer = new NodePointer(new Element() {{
 // setup the element with some attributes
 setAttribute("id", "testId");
 setAttributeNS("xmlns:test", "test:test");
 setAttributeNS("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
 setAttributeNS("xmlns", "http://www.w3.org/2000/xmlns/");
 }});
 }

 @org.junit.Test
 public void testConstructor() {
 iterator = new DOMAttributeIterator(nodePointer, new QName(""));
 assertNotNull(iterator);
 }

 @org.junit.Test
 public void testConstructorWithName() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("id"));
 assertNotNull(iterator);
 }

 @org.junit.Test
 public void testGetNodePointer() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("id"));
 Attr firstAttribute = (Attr) iterator.getNodePointer().getNode();
 assertNotNull(firstAttribute);
 assertEquals("testId", firstAttribute.getValue());
 }

 @org.junit.Test
 public void testGetNodePointerWithInvalidPosition() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("id"));
 assertFalse(iterator.setPosition(-1));
 assertNull(iterator.getNodePointer());
 }

 @org.junit.Test
 public void testGetNodePointerWithOutOfBoundsPosition() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("id"));
 iterator.next();
 assertFalse(iterator.setPosition(attributesSize() + 1));
 assertNull(iterator.getNodePointer());
 }

 @org.junit.Test
 public void testGetPosition() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("id"));
 assertEquals(0, iterator.getPosition());
 }

 @org.junit.Test
 public void testSetPositionWithValidPosition() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("id"));
 iterator.next();
 assertEquals(1, iterator.getPosition());
 }

 @org.junit.Test
 public void testSetPositionWithInvalidPosition() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("id"));
 assertFalse(iterator.setPosition(-1));
 }

 @org.junit.Test
 public void testSetPositionWithOutOfBoundsPosition() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("id"));
 iterator.next();
 iterator.next();
 assertFalse(iterator.setPosition(2));
 }

 @org.junit.Test
 public void testNext() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("id"));
 Attr firstAttribute = (Attr) iterator.getNodePointer().getNode();
 iterator.next();
 Attr secondAttribute = (Attr) iterator.getNodePointer().getNode();
 assertNotNull(secondAttribute);
 assertFalse(equalStrings(firstAttribute.getValue(), secondAttribute.getValue()));
 }

 @org.junit.Test
 public void testHasNext() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("id"));
 assertTrue(iterator.hasNext());
 }

 @org.junit.Test
 public void testHasNextWithEndOfIterator() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("id"));
 iterator.next();
 iterator.next();
 assertFalse(iterator.hasNext());
 }

 private int attributesSize() {
 NamedNodeMap namedNodeMap = nodePointer.getNode().getAttributes();
 return namedNodeMap.getLength();
 }

}