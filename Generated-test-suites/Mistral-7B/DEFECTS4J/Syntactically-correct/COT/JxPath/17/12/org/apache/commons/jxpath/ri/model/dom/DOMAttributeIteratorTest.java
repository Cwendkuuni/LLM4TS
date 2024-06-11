package org.apache.commons.jxpath.ri.model.dom.test;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.DOMParser;

import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

 private NodeIterator iterator;
 private Document document;
 private Element root;

 @Before
 public void setup() throws Exception {
 String xml = "<root xmlns:x='http://test'><child xmlns:x='http://test' attr1='value1' attr2='value2' xmlns:y='http://test'><subChild attr3='value3'/></child></root>";
 document = newDocument(xml);
 root = (Element) document.getDocumentElement();
 }

 @Test
 public void testConstructor_WhenParentIsElementAndNameIsSpecified_ThenAttributesAreAddedToIterator() {
 iterator = new DOMAttributeIterator(new DOMPointer(document, root), new QName("*"));
 assertEquals(2, iterator.size());
 }

 @Test
 public void testConstructor_WhenParentIsElementAndNameIsSpecifiedWithPrefix_ThenAttributesWithMatchingPrefixAreAddedToIterator() {
 iterator = new DOMAttributeIterator(new DOMPointer(document, root), new QName("x:attr1"));
 assertEquals(1, iterator.size());
 }

 @Test
 public void testConstructor_WhenParentIsElementAndNameIsWildcard_ThenAllAttributesAreAddedToIterator() {
 iterator = new DOMAttributeIterator(new DOMPointer(document, root), new QName("*"));
 assertEquals(4, iterator.size());
 }

 @Test
 public void testConstructor_WhenParentIsElementAndNameIsNull_ThenNoAttributesAreAddedToIterator() {
 iterator = new DOMAttributeIterator(new DOMPointer(document, root), null);
 assertEquals(0, iterator.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructor_WhenParentIsNotElement_ThenExceptionIsThrown() {
 iterator = new DOMAttributeIterator(new DOMPointer(document, document), new QName("*"));
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testNext_WhenPositionIsAtStart_ThenFirstAttributeIsReturned() {
 NodePointer nodePointer = iterator.next();
 assertNotNull(nodePointer);
 assertInstanceOf(DOMAttributePointer.class, nodePointer);
 assertEquals("value1", ((DOMAttributePointer) nodePointer).getValue().getValue());
 }

 @Test
 public void testNext_WhenPositionIsAtEnd_ThenNullIsReturned() {
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 NodePointer nodePointer = iterator.next();
 assertNull(nodePointer);
 }

 @Test
 public void testNext_WhenPositionIsInMiddle_ThenCorrectAttributeIsReturned() {
 iterator.next();
 iterator.next();
 NodePointer nodePointer = iterator.next();
 assertEquals("value3", ((DOMAttributePointer) nodePointer).getValue().getValue());
 }

 @Test(expected = IllegalStateException.class)
 public void testNext_WhenPositionIsBeyondEnd_ThenExceptionIsThrown() {
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
}

 @Test
 public void testPrevious_WhenPositionIsAtEnd_ThenLastAttributeIsReturned() {
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 NodePointer nodePointer = iterator.previous();
 assertNotNull(nodePointer);
 assertInstanceOf(DOMAttributePointer.class, nodePointer);
 assertEquals("value2", ((DOMAttributePointer) nodePointer).getValue().getValue());
 }

 @Test
 public void testPrevious_WhenPositionIsAtStart_ThenNullIsReturned() {
 NodePointer nodePointer = iterator.previous();
 assertNull(nodePointer);
 }

 @Test
 public void testPrevious_WhenPositionIsInMiddle_ThenCorrectAttributeIsReturned() {
 iterator.next();
 iterator.next();
 NodePointer nodePointer = iterator.previous();
 assertEquals("value1", ((DOMAttributePointer) nodePointer).getValue().getValue());
 }

 @Test(expected = IllegalStateException.class)
 public void testPrevious_WhenPositionIsBeyondStart_ThenExceptionIsThrown() {
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 NodePointer nodePointer = iterator.previous();
 fail("IllegalStateException expected");
 }

 @Test
 public void testSize_WhenAllAttributesAreIterated_ThenSizeIsEqualToNumberOfAttributes() {
 int size = iterator.size();
 int index = 0;
 while (iterator.hasNext()) {
 NodePointer nodePointer = iterator.next();
 assertEquals("value" + (index + 1), ((DOMAttributePointer) nodePointer).getValue().getValue());
 index++;
 }
 assertEquals(4, size);
 }

 private Document newDocument(String xml) throws Exception {
 DOMParser parser = new DOMParser();
 parser.parseFromString(xml, null);
 return parser.getDocument();
 }
}