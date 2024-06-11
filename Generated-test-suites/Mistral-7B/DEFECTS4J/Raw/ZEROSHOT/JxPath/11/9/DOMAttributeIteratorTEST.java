import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.junit.Assert;
import org.junit.Test;

public class DOMAttributeIteratorTest {

 private NodePointer rootNodePointer;
 private NodePointer elementNodePointer;

 @SuppressWarnings("deprecation")
 @Test
 public void testDOMAttributeIterator_whenIteratorIsCreatedWithNullName_thenReturnEmptyIterator() {
  rootNodePointer = new DOMPointNodePointer(createDocument());
  DOMAttributeIterator iterator = new DOMAttributeIterator(rootNodePointer, null);
  Assert.assertEquals(0, iterator.getPosition());
  Assert.assertFalse(iterator.hasNext());
  Assert.assertNull(iterator.next());
 }

 @SuppressWarnings("deprecation")
 @Test
 public void testDOMAttributeIterator_whenIteratorIsCreatedWithEmptyElement_thenReturnEmptyIterator() {
  rootNodePointer = new DOMPointNodePointer(createDocument());
  Element emptyElement = createEmptyElement("emptyElement");
  elementNodePointer = new DOMElementNodePointer(emptyElement);
  DOMAttributeIterator iterator = new DOMAttributeIterator(rootNodePointer, new QName("*"));
  iterator.setIteratorNodePointer(elementNodePointer);
  Assert.assertEquals(0, iterator.getPosition());
  Assert.assertFalse(iterator.hasNext());
  Assert.assertNull(iterator.next());
 }

 @SuppressWarnings("deprecation")
 @Test
 public void testDOMAttributeIterator_whenIteratorIsCreatedWithElementContainingSingleAttribute_thenReturnSingleAttribute() {
  rootNodePointer = new DOMPointNodePointer(createDocument());
  Element element = createElement("element", "attributeName", "attributeValue");
  elementNodePointer = new DOMElementNodePointer(element);
  DOMAttributeIterator iterator = new DOMAttributeIterator(rootNodePointer, new QName("attributeName"));
  iterator.setIteratorNodePointer(elementNodePointer);
  NodePointer attributePointer = iterator.next();
  Assert.assertNotNull(attributePointer);
  Assert.assertTrue(attributePointer instanceof DOMAttributePointer);
  Assert.assertEquals("attributeValue", ((DOMAttributePointer) attributePointer).getAttributeValue());
  Assert.assertEquals(1, iterator.getPosition());
  Assert.assertTrue(iterator.hasNext());
  Assert.assertNull(iterator.next());
 }

 @SuppressWarnings("deprecation")
 @Test
 public void testDOMAttributeIterator_whenIteratorIsCreatedWithElementContainingMultipleAttributes_thenReturnAttributesInCorrectOrder() {
  rootNodePointer = new DOMPointNodePointer(createDocument());
  Element element = createElement("element", "attribute1", "value1");
  elementNodePointer = new DOMElementNodePointer(element);
  element.setAttributeNodeNS(createAttributeNS("ns1", "attribute2", "value2"));
  DOMAttributeIterator iterator = new DOMAttributeIterator(rootNodePointer, new QName("*"));
  iterator.setIteratorNodePointer(elementNodePointer);
  NodePointer firstAttributePointer = iterator.next();
  NodePointer secondAttributePointer = iterator.next();
  Assert.assertNotNull(firstAttributePointer);
  Assert.assertNotNull(secondAttributePointer);
  Assert.assertTrue(firstAttributePointer instanceof DOMAttributePointer);
  Assert.assertTrue(secondAttributePointer instanceof DOMAttributePointer);
  Assert.assertEquals("value1", ((DOMAttributePointer) firstAttributePointer).getAttributeValue());
  Assert.assertEquals("value2", ((DOMAttributePointer) secondAttributePointer).getAttributeValue());
  Assert.assertEquals(2, iterator.getPosition());
  Assert.assertTrue(iterator.hasNext());
  Assert.assertNull(iterator.next());
 }

 @SuppressWarnings("deprecation")
 @Test
 public void testDOMAttributeIterator_whenIteratorIsCreatedWithElementContainingAttributesWithSameLocalNameButDifferentPrefixes_thenReturnAttributesInCorrectOrder() {
  rootNodePointer = new DOMPointNodePointer(createDocument());
  Element element = createElement("element", null);
  element.setAttributeNodeNS(createAttributeNS("ns1", "attribute1", "value1"));
  element.setAttributeNodeNS(createAttributeNS("ns2", "attribute1", "value2"));
  DOMAttributeIterator iterator = new DOMAttributeIterator(rootNodePointer, new QName("attribute1"));
  iterator.setIteratorNodePointer(elementNodePointer);
  NodePointer firstAttributePointer = iterator.next();
  NodePointer secondAttributePointer = iterator.next();
  Assert.assertNotNull(firstAttributePointer);
  Assert.assertNotNull(secondAttributePointer);
  Assert.assertTrue(firstAttributePointer instanceof DOMAttributePointer);
  Assert.assertTrue(secondAttributePointer instanceof DOMAttributePointer);
  Assert.assertEquals("value1", ((DOMAttributePointer) firstAttributePointer).getAttributeValue());
  Assert.assertEquals("value2", ((DOMAttributePointer) secondAttributePointer).getAttributeValue());
  Assert.assertEquals(2, iterator.getPosition());
  Assert.assertTrue(iterator.hasNext());
  Assert.assertNull(iterator.next());
 }

 @SuppressWarnings("deprecation")
 @Test
 public void testDOMAttributeIterator_whenIteratorIsCreatedWithElementContainingAttributesWithSameLocalNameButDifferentPrefixesAndDefaultNamespace_thenReturnAttributesInCorrectOrder() {
  rootNodePointer = new DOMPointNodePointer(createDocument());
  Element element = createElement("element", null);
  element.setAttributeNodeNS(createAttributeNS("", "attribute1", "value1"));
  element.setAttributeNodeNS(createAttributeNS("ns1", "attribute1", "value2"));
  DOMAttributeIterator iterator = new DOMAttributeIterator(rootNodePointer, new QName("attribute1"));
  iterator.setIteratorNodePointer(elementNodePointer);
  NodePointer firstAttributePointer = iterator.next();
  NodePointer secondAttributePointer = iterator.next();
  Assert.assertNotNull(firstAttributePointer);
  Assert.assertNotNull(secondAttributePointer);
  Assert.assertTrue(firstAttributePointer instanceof DOMAttributePointer);
  Assert.assertTrue(secondAttributePointer instanceof DOMAttributePointer);
  Assert.assertEquals("value1", ((DOMAttributePointer) firstAttributePointer).getAttributeValue());
  Assert.assertEquals("value2", ((DOMAttributePointer) secondAttributePointer).getAttributeValue());
  Assert.assertEquals(2, iterator.getPosition());
  Assert.assertTrue(iterator.hasNext());
  Assert.assertNull(iterator.next());
 }

 @SuppressWarnings("deprecation")
 @Test
 public void testDOMAttributeIterator_whenIteratorIsCreatedWithElementContainingAttributesWithSameLocalNameButDifferentPrefixesAndDifferentNamespaces_thenReturnAttributesInCorrectOrder() {
  rootNodePointer = new DOMPointNodePointer(createDocument());
  Element element = createElement("element", null);
  element.setAttributeNodeNS(createAttributeNS("ns1", "attribute1", "value1"));
  element.setAttributeNodeNS(createAttributeNS("ns2", "attribute1", "value2"));
  DOMAttributeIterator iterator = new DOMAttributeIterator(rootNodePointer, new QName("attribute2"));
  iterator.setIteratorNodePointer(elementNodePointer);
  Assert.assertEquals(0, iterator.getPosition());
  Assert.assertFalse(iterator.hasNext());
  Assert.assertNull(iterator.next());
 }

 @SuppressWarnings("deprecation")
 @Test(expected = UnsupportedOperationException.class)
 public void testDOMAttributeIterator_whenSetPositionToInvalidPosition_thenThrowUnsupportedOperationException() {
  rootNodePointer = new DOMPointNodePointer(createDocument());
  Element element = createElement("element", null);
  DOMAttributeIterator iterator = new DOMAttributeIterator(rootNodePointer, new QName("*"));
  iterator.setIteratorNodePointer(element);
  iterator.setPosition(-1);
 }

 @SuppressWarnings("deprecation")
 @Test
 public void testDOMAttributeIterator_whenSetPositionToInvalidLastPosition_thenReturnNull() {
  rootNodePointer = new DOMPointNodePointer(createDocument());
  Element element = createElement("element", null);
  DOMAttributeIterator iterator = new DOMAttributeIterator(rootNodePointer, new QName("*"));
  iterator.setIteratorNodePointer(element);
  iterator.setPosition(1);
  Assert.assertEquals(1, iterator.getPosition());
  Assert.assertFalse(iterator.hasNext());
  Assert.assertNull(iterator.next());
  iterator.setPosition(2);
  Assert.assertEquals(2, iterator.getPosition());
  Assert.assertFalse(iterator.hasNext());
  Assert.assertNull(iterator.next());
  iterator.setPosition(3);
  Assert.assertEquals(3, iterator.getPosition());
  Assert.assertFalse(iterator.hasNext());
  Assert.assertNull(iterator.next());
  iterator.setPosition(4);
  Assert.assertEquals(4, iterator.getPosition());
  Assert.assertFalse(iterator.hasNext());
  Assert.assertNull(iterator.next());
 }

 @SuppressWarnings("deprecation")
 @Test
 public void testDOMAttributeIterator_whenSetPositionToFirstPosition_thenReturnFirstAttribute() {
  rootNodePointer = new DOMPointNodePointer(createDocument());
  Element element = createElement("element", null);
  element.setAttributeNodeNS(createAttributeNS("", "attribute1", "value1"));
  DOMAttributeIterator iterator = new DOMAttributeIterator(rootNodePointer, new QName("attribute1"));
  iterator.setIteratorNodePointer(element);
  iterator.setPosition(0);
  NodePointer attributePointer = iterator.next();
  Assert.assertEquals("value1", ((DOMAttributePointer) attributePointer).getAttributeValue());
  Assert.assertEquals(1, iterator.getPosition());
  Assert.assertTrue(iterator.hasNext());
  Assert.assertNull(iterator.next());
 }

 @SuppressWarnings("deprecation")
 @Test
 public void testDOMAttributeIterator_whenSetPositionToLastPosition_thenReturnLastAttribute() {
  rootNodePointer = new DOMPointNodePointer(createDocument());
  Element element = createElement("element", null);
  element.setAttributeNodeNS(createAttributeNS("", "attribute1", "value1"));
  element.setAttributeNodeNS(createAttributeNS("", "attribute2", "value2"));
  DOMAttributeIterator iterator = new DOMAttributeIterator(rootNodePointer, new QName("*"));
  iterator.setIteratorNodePointer(element);
  iterator.setPosition(1);
  NodePointer firstAttributePointer = iterator.next();
  NodePointer lastAttributePointer = iterator.next();
  Assert.assertEquals("value1", ((DOMAttributePointer) firstAttributePointer).getAttributeValue());
  Assert.assertEquals("value2", ((DOMAttributePointer) lastAttributePointer).getAttributeValue());
  Assert.assertEquals(2, iterator.getPosition());
  Assert.assertFalse(iterator.hasNext());
  Assert.assertNull(iterator.next());
 }

 private Document createDocument() {
  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
  Document document;
  try {
       document = factory.newDocumentBuilder().newDocument();
  } catch (ParserConfigurationException e) {
       throw new RuntimeException(e);
  }
  return document;
 }

 private Element createElement(String tagName, String attributeName, String attributeValue) {
  Document document = createDocument();
  Element element = document.createElement(tagName);
  if (attributeName != null && attributeValue != null) {
       Attr attr = document.createAttribute(attributeName);
       attr.setValue(attributeValue);
       element.setAttributeNode(attr);
  }
  return element;
 }

 private Attribute createAttributeNS(String namespaceURI, String localName, String value) {
  Document document = createDocument();
  Attribute attr = document.createAttributeNS(namespaceURI, localName);
  attr.setValue(value);
  return attr;
 }

}