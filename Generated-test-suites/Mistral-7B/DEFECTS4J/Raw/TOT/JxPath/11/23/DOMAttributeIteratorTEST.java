package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

 private DOMAttributeIterator iterator;
 private Element rootElement;

 @Before
 public void setUp() {
 Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
 rootElement = document.createElement("root");
 rootElement.setAttribute("attr1", "value1");
 rootElement.setAttribute("xmlns:x", "http://example.com");
 rootElement.setAttributeNS("http://example.com", "attr2", "value2");
 document.appendChild(rootElement);
 iterator = new DOMAttributeIterator(new DOMPointer(document), new QName("*"));
 }

 /**
 * Test empty iterator
 */
 @Test
 public void testEmptyIterator() {
 iterator = new DOMAttributeIterator(new DOMPointer(rootElement), new QName("nonexistent"));
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 assertNull(iterator.next());
 }

 /**
 * Test iterator for unmatched name
 */
 @Test
 public void testUnmatchedName() {
 iterator = new DOMAttributeIterator(new DOMPointer(rootElement), new QName("nonexistent:attr"));
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 assertNull(iterator.next());
 }

 /**
 * Test iterator for matched name with prefix and local name
 */
 @Test
 public void testMatchedNameWithPrefixAndLocalName() {
 iterator = new DOMAttributeIterator(new DOMPointer(rootElement), new QName("x:attr2"));
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 NodePointer nodePointer = iterator.next();
 assertTrue(nodePointer instanceof DOMAttributePointer);
 assertEquals("value2", ((DOMAttributePointer) nodePointer).getNodeValue());
 }

 /**
 * Test iterator for matched name without prefix and local name
 */
 @Test
 public void testMatchedNameWithoutPrefixAndLocalName() {
 iterator = new DOMAttributeIterator(new DOMPointer(rootElement), new QName("attr1"));
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 NodePointer nodePointer = iterator.next();
 assertTrue(nodePointer instanceof DOMAttributePointer);
 assertEquals("value1", ((DOMAttributePointer) nodePointer).getNodeValue());
 }

 /**
 * Test iterator for matched name with prefix and wildcard local name
 */
 @Test
 public void testMatchedNameWithPrefixAndWildcardLocalName() {
 iterator = new DOMAttributeIterator(new DOMPointer(rootElement), new QName("x:*"));
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 NodePointer nodePointer = iterator.next();
 assertTrue(nodePointer instanceof DOMAttributePointer);
 assertEquals("attr1", ((DOMAttributePointer) nodePointer).getNodeName().getName());
 assertEquals("value1", ((DOMAttributePointer) nodePointer).getNodeValue());
 nodePointer = iterator.next();
 assertTrue(nodePointer instanceof DOMAttributePointer);
 assertEquals("xmlns:x", ((DOMAttributePointer) nodePointer).getNodeName().getName());
 assertEquals("http://example.com", ((DOMAttributePointer) nodePointer).getNodeName().getNamespaceURI());
 assertEquals("attr2", ((DOMAttributePointer) nodePointer).getNodeName().getLocalPart());
 assertEquals("value2", ((DOMAttributePointer) nodePointer).getNodeValue());
 }

 /**
 * Test iterator for matched name with wildcard prefix and local name
 */
 @Test
 public void testMatchedNameWithWildcardPrefixAndLocalName() {
 iterator = new DOMAttributeIterator(new DOMPointer(rootElement), new QName("*:attr2"));
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 NodePointer nodePointer = iterator.next();
 assertTrue(nodePointer instanceof DOMAttributePointer);
 assertEquals("attr2", ((DOMAttributePointer) nodePointer).getNodeName().getName());
 assertEquals("value2", ((DOMAttributePointer) nodePointer).getNodeValue());
 }

 /**
 * Test iterator for unmatched name with wildcard prefix and local name
 */
 @Test
 public void testUnmatchedNameWithWildcardPrefixAndLocalName() {
 iterator = new DOMAttributeIterator(new DOMPointer(rootElement), new QName("*:attr3"));
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 assertNull(iterator.next());
 }

 /**
 * Test iteration to the end of the list
 */
 @Test
 public void testIterationToTheEndOfTheList() {
 iterator = new DOMAttributeIterator(new DOMPointer(rootElement), new QName("*"));
 NodePointer nodePointer1 = iterator.next();
 NodePointer nodePointer2 = iterator.next();
 assertTrue(nodePointer1 != null);
 assertTrue(nodePointer2 != null);
 assertEquals(3, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }
}