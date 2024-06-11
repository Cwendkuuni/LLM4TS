import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static org.junit.jupiter.api.Assertions.*;

public class DOMAttributeIteratorTest {

 private Node createTestNode(String name, String prefix, String nsUri, String value) throws Exception {
 Element element = DocumentBuilderFactory.newInstance().newDocumentBuilder().newElement(name);
 element.setPrefix(prefix);
 element.setAttributeNS(nsUri, name + ":test", value);
 return element;
 }

 @Test
 public void testEmptyIterator() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(""), new QName("", "*"));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithNoMatchingAttribute() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint("root"), new QName("testPrefix", "test:ns", "http://test.com"));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithSingleMatchingAttribute() throws Exception {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint("root"), new QName("testPrefix", "test:localName", "http://test.com"));
 Node testNode = createTestNode("testElement", "testPrefix", "http://test.com", "testValue");
 ((Element) testNode).appendChild(createTestNode("childElement", null, null, "childValue"));
 Node parentNode = createTestNode("parentElement", null, null, null);
 ((Document) parentNode).appendChild(testNode);
 DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.next();
 assertEquals(testNode, attributePointer.getNode());
 assertEquals("testValue", attributePointer.getNodeValue());
 }

 @Test
 public void testIteratorWithMultipleMatchingAttributes() throws Exception {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint("root"), new QName("testPrefix", "*", "http://test.com"));
 Node testNode = createTestNode("testElement", "testPrefix", "http://test.com", "testValue1");
 Node testNode2 = createTestNode("testElement", "testPrefix", "http://test.com", "testValue2");
 ((Element) testNode).appendChild(createTestNode("childElement", null, null, "childValue1"));
 ((Element) testNode2).appendChild(createTestNode("childElement", null, null, "childValue2"));
 Node parentNode = createTestNode("parentElement", null, null, null);
 ((Document) parentNode).appendChild(testNode);
 ((Document) parentNode).appendChild(testNode2);
 DOMAttributePointer attributePointer1 = (DOMAttributePointer) iterator.next();
 assertEquals(testNode, attributePointer1.getNode());
 assertEquals("testValue1", attributePointer1.getNodeValue());
 DOMAttributePointer attributePointer2 = (DOMAttributePointer) iterator.next();
 assertEquals(testNode2, attributePointer2.getNode());
 assertEquals("testValue2", attributePointer2.getNodeValue());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithErrorAttribute() throws Exception {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint("root"), new QName("testPrefix", "test:localName", null));
 Node testNode = createTestNode("testElement", "testPrefix", null, "testValue");
 ((Element) testNode).setAttributeNodeNS(testNode.getAttributes().getNamedItem("test:localName"), "http://test.com", "testValueError");
 Node parentNode = createTestNameSpace("root");
 ((Document) parentNode).appendChild(testNode);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithNullParent() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(null, new QName("testPrefix", "test:localName", "http://test.com"));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithNullQName() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint("root"), null);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithNullParentNamespace() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint("root"), new QName("testPrefix", "test:localName", null));
 assertFalse(iterator.hasNext());
 }

 private Node createTestNameSpace(String name) throws Exception {
 Element rootElement = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().createElement("rootElement");
 Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
 document.appendChild(rootElement);
 document.setDocumentURI("http://test.com");
 document.setXmlStandalone(true);
 rootElement.setPrefix("testPrefix");
 rootElement.setAttributeNS("xmlns:test", "xmlns:test", "http://test.com");
 return document;
 }
}