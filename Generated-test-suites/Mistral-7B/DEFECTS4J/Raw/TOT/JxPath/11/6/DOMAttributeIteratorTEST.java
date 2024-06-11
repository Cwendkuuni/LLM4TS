import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

 private static final String XML_STRING = "<root xmlns:x='http://example.com'><element attr1='value1' attr2='value2' xmlns:y='http://example.org' /></root>";

 @Test
 public void testDOMAttributeIterator() {
  Element rootElement = getDocumentFromString(XML_STRING).getDocumentElement();
  QName testName = new QName("x:attr1");
  DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointers.DOMPointer(rootElement), testName);

  NodePointer nodePointer = iterator.getNodePointer();
  assertEquals(NodePointer.class, nodePointer.getClass());
  assertEquals(iterator.getPosition(), nodePointer.getPosition());
  assertTrue(nodePointer instanceof DOMAttributePointer);

  Attr attribute = (Attr) nodePointer.getNode();
  assertEquals("value1", attribute.getValue());
  assertEquals("attr1", attribute.getNodeName());
  assertEquals("http://example.com", attribute.getNamespaceURI());

  iterator.setPosition(1);
  nodePointer = iterator.getNodePointer();
  assertEquals("attr2", nodePointer.getNode().getNodeName());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testDOMAttributeIteratorOutOfBounds() {
  Element rootElement = getDocumentFromString(XML_STRING).getDocumentElement();
  QName testName = new QName("y:nonexistentAttr");
  DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointers.DOMPointer(rootElement), testName);
  iterator.setPosition(1);
 }

 @Test
 public void testDOMAttributeIteratorWildcard() {
  Element rootElement = getDocumentFromString(XML_STRING).getDocumentElement();
  QName testName = new QName("*");
  DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointers.DOMPointer(rootElement), testName);

  List<NodePointer> nodes = new ArrayList<>();
  NodePointer nodePointer;
  int position = 0;

  do {
   nodePointer = iterator.getNodePointer();
   nodes.add(nodePointer);
   position++;
  } while (iterator.setPosition(iterator.getPosition() + 1));

  assertEquals(7, nodes.size());
  for (int i = 0; i < nodes.size(); i++) {
   Attr attribute = (Attr) nodes.get(i).getNode();
   if (i == 0) {
    assertEquals("xmlns", attribute.getNodeName());
    assertEquals("http://www.w3.org/2000/xmlns/", attribute.getNamespaceURI());
   } else if (i == 1) {
    assertEquals("xmlns:x", attribute.getNodeName());
    assertEquals("http://example.com", attribute.getNamespaceURI());
   } else if (i == 2) {
    assertEquals("xmlns:y", attribute.getNodeName());
    assertEquals("http://example.org", attribute.getNamespaceURI());
   } else {
    assertTrue(attribute.getNodeName().startsWith("attr"));
    assertEquals("root", attribute.getOwnerElement().getNodeName());
   }
  }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDOMAttributeIteratorInvalidQName() {
  Element rootElement = getDocumentFromString(XML_STRING).getDocumentElement();
  QName testName = new QName(":invalid:attr");
  DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointers.DOMPointer(rootElement), testName);
 }

 private Document getDocumentFromString(String xmlString) {
  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
  DocumentBuilder builder;
  try {
   builder = factory.newDocumentBuilder();
   return builder.parse(new InputSource(new StringReader(xmlString)));
  } catch (ParserConfigurationException | IOException e) {
   throw new RuntimeException("Error creating Document from String", e);
  }
 }
}