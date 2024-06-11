import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DOMAttributeIteratorTest {

 private NodePointer parentNode;
 private DOMAttributeIterator iterator;

 @Before
 public void setUp() {
 String xml = "<root xmlns:x='http://test.com'><child xmlns:y='http://test.com' attr1='value1' attr2='value2' attr3='value3'></child></root>";
 Document doc = javax.xml.parsers.DocumentBuilderFactory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
 parentNode = new NodePointer(doc.getDocumentElement());
 }

 @Test
 public void testDOMAttributeIteratorCreation() {
 QName attrName = new QName("attr1", null);
 iterator = new DOMAttributeIterator(parentNode, attrName);
 }

 @Test
 public void testEmptyIterator() {
 QName attrName = new QName("nonExistentAttribute", null);
 iterator = new DOMAttributeIterator(parentNode, attrName);
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 assertNull(iterator.next());
 }

 @Test
 public void testIteratorWithSingleAttribute() {
 QName attrName = new QName("attr1", null);
 iterator = new DOMAttributeIterator(parentNode, attrName);
 assertTrue(iterator.hasNext());
 NodePointer attrPointer = iterator.next();
 assertEquals("attr1", attrPointer.getNodeName());
 assertEquals("value1", attrPointer.getNodeValue());
 assertEquals(1, iterator.getPosition());
 assertFalse(iterator.hasPrevious());
 }

 @Test
 public void testIteratorWithMultipleAttributes() {
 QName attrName = new QName("*", null);
 iterator = new DOMAttributeIterator(parentNode, attrName);
 assertTrue(iterator.hasNext());
 NodePointer attrPointer = iterator.next();
 assertEquals("xmlns:x", attrPointer.getNodeName());
 assertEquals("http://test.com", attrPointer.getNodeValue());
 assertEquals(1, iterator.getPosition());

 assertTrue(iterator.hasNext());
 attrPointer = iterator.next();
 assertEquals("attr1", attrPointer.getNodeName());
 assertEquals("value1", attrPointer.getNodeValue());
 assertEquals(2, iterator.getPosition());

 assertTrue(iterator.hasNext());
 attrPointer = iterator.next();
 assertEquals("attr2", attrPointer.getNodeName());
 assertEquals("value2", attrPointer.getNodeValue());
 assertEquals(3, iterator.getPosition());

 assertTrue(iterator.hasNext());
 attrPointer = iterator.next();
 assertEquals("attr3", attrPointer.getNodeName());
 assertEquals("value3", attrPointer.getNodeValue());
 assertEquals(4, iterator.getPosition());

 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithNamespaceAwareAttribute() {
 QName attrName = new QName("xmlns:x:attr1", "http://test.com");
 iterator = new DOMAttributeIterator(parentNode, attrName);
 assertTrue(iterator.hasNext());
 NodePointer attrPointer = iterator.next();
 assertEquals("xmlns:x:attr1", attrPointer.getNodeName().toString());
 assertEquals("http://test.com", attrPointer.getNodeValue());
 assertEquals(1, iterator.getPosition());

 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithNoMatchingAttribute() {
 QName attrName = new QName("nonExistentAttr", null);
 iterator = new DOMAttributeIterator(parentNode, attrName);
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 assertNull(iterator.next());
 }
}