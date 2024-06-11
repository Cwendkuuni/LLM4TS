import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class DOMAttributeIteratorTest {

 private NodePointer nodePointer;
 private DOMAttributeIterator iterator;
 private Element element;

 @Before
 public void setUp() {
 // Create a simple XML document for testing
 String xml = "<root xmlns:x='http://test'><child x:foo='bar'><subchild x:baz='qux'/></child></root>";
 Document document = JaxbDocument.fromString(xml);
 element = (Element) document.getDocumentElement().getElementsByTagName("child").item(0);
 nodePointer = new DOMPoint(document, element.getParentNode());
 }

 @Test
 public void testEmptyIterator() {
 iterator = new DOMAttributeIterator(nodePointer, null);
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithWildcardName() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("*"));
 assertEquals(2, iterator.getPosition());
 assertSame(element.getAttributeNode("xmlns:x"), iterator.getNodePointer().getNode());
 assertSame(element.getAttributeNode("foo"), iterator.next().getNodePointer().getNode());
 assertSame(element.getAttributeNode("baz"), iterator.next().getNodePointer().getNode());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithSpecificName() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("foo"));
 assertEquals(1, iterator.getPosition());
 assertSame(element.getAttributeNode("foo"), iterator.getNodePointer().getNode());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithSpecificPrefix() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("{http://test}foo"));
 assertEquals(1, iterator.getPosition());
 assertSame(element.getAttributeNodeNS(DOMConstants.XMLNS_URI, "x:foo"), iterator.getNodePointer().getNode());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithSpecificPrefixAndLocalName() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("{http://test}foo", "bar"));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithNamespaceURIAndLocalName() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("http://test", "foo"));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithMissingAttribute() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("baz"));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithNamespaceAttribute() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("{http://test}", "xmlns:x"));
 assertNull(iterator.getNodePointer().getNode());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithNamespaceAttributeAndLocalName() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("{http://test}", "xmlns:x", "foo"));
 assertNull(iterator.getNodePointer().getNode());
 assertFalse(iterator.hasNext());
 }
}