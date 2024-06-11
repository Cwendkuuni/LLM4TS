import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMAttributeIteratorTest {

  private Document doc;
  private Element element;

  @Before
  public void setUp() {
    doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    element = doc.createElement("TestElement");
    doc.appendChild(element);
  }

  @Test
  public void testDOMAttributeIterator() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(element, new QName("*"));
    assertNotNull(iterator);
  }

  @Test
  public void testGetNodePointer() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(element, new QName("*"));
    NodePointer nodePointer = iterator.getNodePointer();
    assertNotNull(nodePointer);
  }

  @Test
  public void testGetPosition() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(element, new QName("*"));
    int position = iterator.getPosition();
    assertEquals(0, position);
  }

  @Test
  public void testSetPosition() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(element, new QName("*"));
    boolean setPosition = iterator.setPosition(1);
    assertTrue(setPosition);
  }

  @Test
  public void testSetPositionInvalid() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(element, new QName("*"));
    boolean setPosition = iterator.setPosition(-1);
    assertFalse(setPosition);
  }

  @Test
  public void testSetPositionOutOfRange() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(element, new QName("*"));
    boolean setPosition = iterator.setPosition(10);
    assertFalse(setPosition);
  }

  @Test
  public void testGetAttributeValid() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(element, new QName("*"));
    Attr attribute = iterator.getAttribute(element, new QName("attributeName"));
    assertNull(attribute);
    element.setAttribute("attributeName", "attributeValue");
    attribute = iterator.getAttribute(element, new QName("attributeName"));
    assertNotNull(attribute);
    assertEquals("attributeValue", attribute.getValue());
  }

  @Test
  public void testGetAttributeWithNamespace() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(element, new QName("*", "namespace"));
    Attr attribute = iterator.getAttribute(element, new QName("attributeName", "namespace"));
    assertNull(attribute);
    element.setAttributeNS("namespace", "attributeName", "attributeValue");
    attribute = iterator.getAttribute(element, new QName("attributeName", "namespace"));
    assertNotNull(attribute);
    assertEquals("attributeValue", attribute.getValue());
  }

  @Test
  public void testGetAttributeWithNamespaceFallback() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(element, new QName("*", "namespace"));
    Attr attribute = iterator.getAttribute(element, new QName("attributeName", "fallbackNamespace"));
    assertNull(attribute);
    element.setAttribute("attributeName", "attributeValue");
    attribute = iterator.getAttribute(element, new QName("attributeName", "fallbackNamespace"));
    assertNotNull(attribute);
    assertEquals("attributeValue", attribute.getValue());
  }

  @Test
  public void testTestAttrWithMatchingAttr() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(element, new QName("*"));
    Attr attribute = doc.createAttribute("attributeName");
    boolean testAttr = iterator.testAttr(attribute, new QName("*"));
    assertTrue(testAttr);
  }

  @Test
  public void testTestAttrWithMismatchingAttr() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(element, new QName("*"));
    Attr attribute = doc.createAttribute("attributeName");
    boolean testAttr = iterator.testAttr(attribute, new QName("differentName"));
    assertFalse(testAttr);
  }

  @Test
  public void testTestAttrWithMatchingPrefixAndLocalName() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(element, new QName("prefix:localName"));
    Attr attribute = doc.createAttribute("prefix:localName");
    boolean testAttr = iterator.testAttr(attribute, new QName("prefix:localName"));
    assertTrue(testAttr);
  }

  @Test
  public void testTestAttrWithMatchingPrefixAndWildcardLocalName() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(element, new QName("prefix:*"));
    Attr attribute = doc.createAttribute("prefix:localName");
    boolean testAttr = iterator.testAttr(attribute, new QName("prefix:*"));
    assertTrue(testAttr);
  }

  @Test
  public void testTestAttrWithMatchingWildcardPrefixAndLocalName() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(element, new QName("*:localName"));
    Attr attribute = doc.createAttribute("prefix:localName");
    boolean testAttr = iterator.testAttr(attribute, new QName("*:localName"));
    assertTrue(testAttr);
  }

  @Test
  public void testTestAttrWithMatchingNamespaceAndWildcardLocalName() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(element, new QName("*", "namespace"));
    Attr attribute = doc.createAttributeNS("namespace", "localName");
    boolean testAttr = iterator.testAttr(attribute, new QName("*", "namespace"));
    assertTrue(testAttr);
  }

  @Test
  public void testTestAttrWithMatchingNamespaceAndWildcardLocalNameFallback() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(element, new QName("*", "namespace"));
    Attr attribute = doc.createAttributeNS("namespace", "localName");
    boolean testAttr = iterator.testAttr(attribute, new QName("*", "fallbackNamespace"));
    assertTrue(testAttr);
  }

  @Test
  public void testTestAttrWithMatchingNamespaceAndWildcardLocalNameFallbackPrefix() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(element, new QName("*", "namespace"));
    Attr attribute = doc.createAttributeNS("namespace", "prefix:localName");
    boolean testAttr = iterator.testAttr(attribute, new QName("*", "fallbackNamespace"));
    assertFalse(testAttr);
  }

  @Test
  public void testTestAttrWithMatchingNamespaceAndWildcardLocalNameFallbackNoPrefix() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(element, new QName("*", "namespace"));
    Attr attribute = doc.createAttributeNS("namespace", "localName");
    boolean testAttr = iterator.testAttr(attribute, new QName("*", "namespace"));
    assertTrue(testAttr);
  }

  @Test
  public void testEqualStringsEqual() {
    boolean equalStrings = DOMAttributeIterator.equalStrings("test", "test");
    assertTrue(equalStrings);
  }

  @Test
  public void testEqualStringsNotEqual() {
    boolean equalStrings = DOMAttributeIterator.equalStrings("test", "different");
    assertFalse(equalStrings);
  }

  @Test
  public void testEqualStringsNull() {
    boolean equalStrings = DOMAttributeIterator.equalStrings(null, null);
    assertTrue(equalStrings);
  }

  @Test
  public void testEqualStringsOneNull() {
    boolean equalStrings = DOMAttributeIterator.equalStrings("test", null);
    assertFalse(equalStrings);
  }

  @Test
  public void testEqualStringsOneNullReverse() {
    boolean equalStrings = DOMAttributeIterator.equalStrings(null, "test");
    assertFalse(equalStrings);
  }

  @Test
  public void testEqualStringsBothNull() {
    boolean equalStrings = DOMAttributeIterator.equalStrings(null, null);
    assertTrue(equalStrings);
  }

  @Test
  public void testSetPositionFirst() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(element, new QName("*"));
    boolean setPosition = iterator.setPosition(1);
    assertTrue(setPosition);
    int position = iterator.getPosition();
    assertEquals(1, position);
  }

  @Test
  public void testSetPositionLast() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(element, new QName("*"));
    boolean setPosition = iterator.setPosition(2);
    assertFalse(setPosition);
    int position = iterator.getPosition();
    assertEquals(0, position);
  }

  @Test
  public void testSetPositionZero() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(element, new QName("*"));
    boolean setPosition = iterator.setPosition(0);
    assertFalse(setPosition);
    int position = iterator.getPosition();
    assertEquals(0, position);
  }

  @Test
  public void testSetPositionNegative() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(element, new QName("*"));
    boolean setPosition = iterator.setPosition(-1);
    assertFalse(setPosition);
    int position = iterator.getPosition();
    assertEquals(0, position);
  }

  @Test
  public void testSetPositionMax() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(element, new QName("*"));
    boolean setPosition = iterator.setPosition(Integer.MAX_VALUE);
    assertFalse(setPosition);
    int position = iterator.getPosition();
    assertEquals(0, position);
  }

  @Test
  public void testSetPositionMin() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(element, new QName("*"));
    boolean setPosition = iterator.setPosition(Integer.MIN_VALUE);
    assertFalse(setPosition);
    int position = iterator.getPosition();
    assertEquals(0, position);
  }

  @Test
  public void testConstructorWithNonElementNode() {
    Node textNode = doc.createTextNode("text");
    DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(textNode), new QName("*"));
    assertNotNull(iterator);
    int position = iterator.getPosition();
    assertEquals(0, position);
  }

  @Test
  public void testConstructorWithNamespaceNode() {
    Element namespaceElement = doc.createElementNS("namespace", "namespaceElement");
    DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNamespaceNodePointer(namespaceElement), new QName("*"));
    assertNotNull(iterator);
    int position = iterator.getPosition();
    assertEquals(0, position);
  }

}