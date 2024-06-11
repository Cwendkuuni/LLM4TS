import org.apache.commons.jxpath.ri.model.DOMNodePointer;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

 private NodePointer parentNode;
 private DOMAttributeIterator iterator;

 @Before
 public void setUp() {
 Node root = new Element.createElement("root");
 root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:x", "http://example.com/x");
 root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:y", "http://example.com/y");
 root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:z", "http://example.com/z");
 root.setAttributeNode(new Attr("id", "rootId"));
 root.setAttributeNode(new Attr("class", "rootClass"));
 Element child = new Element.createElement("child");
 child.setAttributeNode(new Attr("id", "childId"));
 child.setAttributeNode(new Attr("class", "childClass"));
 child.setAttributeNode(new Attr("a:test", "testValue"));
 root.appendChild(child);
 parentNode = new DOMNodePointer(root);
 }

 @Test
 public void testEmptyIterator() {
 iterator = new DOMAttributeIterator(parentNode, new QName("", "*"));
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testSingleAttributeIterator() {
 iterator = new DOMAttributeIterator(parentNode, new QName("", "*"));
 iterator.next();
 Attr singleAttribute = (Attr) iterator.getNodePointer().getNode();
 assertEquals("id", singleAttribute.getNodeName());
 assertEquals("rootId", singleAttribute.getNodeValue());
 }

 @Test
 public void testSingleAttributeIteratorWithPrefix() {
 iterator = new DOMAttributeIterator(parentNode, new QName("x", "*"));
 iterator.next();
 Attr singleAttribute = (Attr) iterator.getNodePointer().getNode();
 assertEquals("xmlns:x", singleAttribute.getNodeName());
 assertEquals("http://example.com/x", singleAttribute.getNodeValue());
 }

 @Test
 public void testSingleAttributeIteratorWithLocalName() {
 iterator = new DOMAttributeIterator(parentNode, new QName("", "class"));
 iterator.next();
 Attr singleAttribute = (Attr) iterator.getNodePointer().getNode();
 assertEquals("class", singleAttribute.getNodeName());
 assertEquals("rootClass", singleAttribute.getNodeValue());
 }

 @Test
 public void testMultipleAttributesIterator() {
 iterator = new DOMAttributeIterator(parentNode, new QName("", "*"));
 iterator.next(); // id
 iterator.next(); // class
 iterator.next(); // xmlns:x
 iterator.next(); // xmlns:y
 iterator.next(); // xmlns:z
 Attr multipleAttribute = (Attr) iterator.getNodePointer().getNode();
 assertEquals("a:test", multipleAttribute.getNodeName());
 assertEquals("testValue", multipleAttribute.getNodeValue());
 }

 @Test
 public void testMultipleAttributesIteratorWithPrefix() {
 iterator = new DOMAttributeIterator(parentNode, new QName("x", "*"));
 iterator.next(); // xmlns:x
 iterator.next(); // nothing with the prefix "x" and no local name
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testPastEndOfIterator() {
 iterator = new DOMAttributeIterator(parentNode, new QName("", "*"));
 iterator.next(); // id
 iterator.next(); // class
 iterator.next(); // xmlns:x
 iterator.next(); // xmlns:y
 iterator.next(); // xmlns:z
 iterator.next(); // testAttribute (out of bounds)
 }

 @Test
 public void testPastEndOfIteratorWithPrefix() {
 iterator = new DOMAttributeIterator(parentNode, new QName("x", "*"));
 iterator.next(); // xmlns:x
 iterator.next(); // nothing with the prefix "x" and no local name
 iterator.next(); // nothing with the prefix "x" and local name "xmlns"
 iterator.next(); // xmlns:y
 iterator.next(); // xmlns:z
 iterator.next(); // testAttribute (out of bounds with prefix)
 }

 @Test
 public void testIteratorReset() {
 iterator = new DOMAttributeIterator(parentNode, new QName("", "*"));
 iterator.next(); // id
 iterator.next(); // class
 iterator.next(); // xmlns:x
 iterator.next(); // xmlns:y
 iterator.next(); // xmlns:z
 iterator.next(); // testAttribute
 iterator.next(); // testAttribute (out of bounds)
 iterator.previous(); // testAttribute
 assertEquals("testValue", ((Attr) iterator.getNodePointer().getNode()).getNodeValue());
 }
}