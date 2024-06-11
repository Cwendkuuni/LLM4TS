package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

 private Node testNode;
 private Element testElement;
 private DOMAttributeIterator iterator;

 @Before
 public void setup() {
 testNode = new org.apache.commons.jxpath.ri.model.DOMTestHelper().newElement("root", null);
 testElement = (Element) testNode;
 testElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:x", "http://www.w3.org/2000/xmlns/xhtml");
 testElement.setAttributeNS("x", "testAttr1", "value1");
 testElement.setAttributeNode((Attr) testElement.getAttributes().getNamedItem("id"));
 testElement.setAttributeNode((Attr) testElement.getAttributes().getNamedItem("class"));
 }

 @Test
 public void testDOMAttributeIterator_Empty_Case() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(null), null);
 assertEquals(false, iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIterator_Single_Attribute() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testNode), null);
 assertEquals("id", iterator.getNodePointer().getNodeName());
 }

 @Test
 public void testDOMAttributeIterator_Multiple_Attributes() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), new QName("x", "testAttr1"));
 assertEquals("value1", iterator.getNodePointer().getNodeValue());

 iterator.setPosition(1);
 assertEquals("class", iterator.getNodePointer().getNodeName());
 }

 @Test
 public void testDOMAttributeIterator_Namespace_Case() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), new QName("http://www.w3.org/2000/xmlns/", "xmlns:x"));
 assertEquals("http://www.w3.org/2000/xmlns/xhtml", iterator.getNodePointer().getNamespaceURI());
 }

 @Test
 public void testDOMAttributeIterator_Null_Name_Case() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), null);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIterator_No_Matching_Attribute() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), new QName("x", "nonexistent"));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIterator_Empty_Element() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testNode), null);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIterator_Mixed_Namespaces() {
 NodeList attributes = testElement.getAttributes();
 Attr xmlnsX = (Attr) attributes.item(1);
 NodeList xmlnsXAttrs = xmlnsX.getOwnerDocument().getElementsByTagNameNS("http://www.w3.org/2000/xmlns/xhtml", "xml:base");
 Attr xmlBase = (Attr) xmlnsXAttrs.item(0);
 String xmlBaseValue = xmlBase.getValue();

 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), new QName("http://www.w3.org/2000/xmlns/xhtml", "xml:base"));
 assertEquals(xmlBaseValue, iterator.getNodePointer().getNodeValue());
 }
}