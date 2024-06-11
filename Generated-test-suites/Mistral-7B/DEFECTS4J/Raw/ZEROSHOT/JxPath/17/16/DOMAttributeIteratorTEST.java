import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

 @Test
 public void testNewInstance() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(null, null), null);
 assertNull(iterator.getNodePointer());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testNewInstanceWithElementAndWildcardName() {
 Element element = new Element("root");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("*"));
 assertNotNull(iterator.getNodePointer());
 assertEquals(element, iterator.getNodePointer().getNode().getParentNode());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testNewInstanceWithElementAndSpecificName() {
 Element element = new Element("root");
 Attr attr = element.setAttributeNode(new Attr("id", "1"));
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("id"));
 assertNotNull(iterator.getNodePointer());
 assertEquals(element, iterator.getNodePointer().getNode().getParentNode());
 assertTrue(iterator.hasNext());
 }

 @Test
 public void testNewInstanceWithElementAndSpecificNamespace() {
 Element element = new Element("root");
 element.setAttributeNS("http://example.com", "xml:id", "1");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("{http://example.com}xml:id"));
 assertNotNull(iterator.getNodePointer());
 assertEquals(element, iterator.getNodePointer().getNode().getParentNode());
 assertTrue(iterator.hasNext());
 }

 @Test
 public void testNewInstanceWithElementAndMultipleAttributes() {
 Element element = new Element("root");
 element.setAttributeNode(new Attr("id", "1"));
 element.setAttributeNode(new Attr("class", "test"));
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("*"));
 assertNotNull(iterator.getNodePointer());
 assertEquals(element, iterator.getNodePointer().getNode().getParentNode());
 assertTrue(iterator.hasNext());
 }

 @Test
 public void testHasNext() {
 Element element = new Element("root");
 Attr attr1 = element.setAttributeNode(new Attr("id", "1"));
 Attr attr2 = element.setAttributeNode(new Attr("class", "test"));
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("*"));
 iterator.next();
 iterator.next();
 assertTrue(iterator.hasNext());
 }

 @Test(expected = IllegalStateException.class)
 public void testHasNextAfterLastAttribute() {
 Element element = new Element("root");
 Attr attr1 = element.setAttributeNode(new Attr("id", "1"));
 Attr attr2 = element.setAttributeNode(new Attr("class", "test"));
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("*"));
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 }

 @Test
 public void testNext() {
 Element element = new Element("root");
 Attr attr1 = element.setAttributeNode(new Attr("id", "1"));
 Attr attr2 = element.setAttributeNode(new Attr("class", "test"));
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("*"));
 NodePointer nodePointer1 = iterator.next();
 NodePointer nodePointer2 = iterator.next();
 assertEquals(attr1, nodePointer1.getNode().getNodeValue());
 assertEquals(attr2, nodePointer2.getNode().getNodeValue());
 }

 @Test
 public void testNextWithSpecificName() {
 Element element = new Element("root");
 Attr attr1 = element.setAttributeNode(new Attr("id", "1"));
 Attr attr2 = element.setAttributeNode(new Attr("class", "test"));
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("id"));
 NodePointer nodePointer = iterator.next();
 assertEquals(attr1, nodePointer.getNode().getNodeValue());
 }

 @Test
 public void testNextWithSpecificNamespace() {
 Element element = new Element("root");
 element.setAttributeNS("http://example.com", "xml:id", "1");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("{http://example.com}xml:id"));
 NodePointer nodePointer = iterator.next();
 assertEquals(element.getAttributeNS("http://example.com", "xml:id"), nodePointer.getNode().getNodeValue());
 }
}