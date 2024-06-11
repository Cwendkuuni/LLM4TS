import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.JDOMElement;
import org.apache.commons.jxpath.ri.model.JDOMNamespaceContext;
import org.apache.commons.jxpath.ri.model.JDOMNode;
import org.apache.commons.jxpath.ri.model.JDOMQName;
import org.apache.commons.jxpath.ri.model.JDOMAttribute;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

 private JDOMElement rootElement;
 private JDOMNamespaceContext namespaceContext;

 @Before
 public void setup() {
 // Create root element
 Element rootElementXml = new Element("root");
 rootElementXml.addNamespaceDeclaration("xml", "http://www.w3.org/XML/1998/namespace");
 rootElement = new JDOMElement(rootElementXml);

 // Create attribute with default namespace
 Attribute attr1 = new Attribute("attr1", "value1", null);
 rootElement.getContent().add(attr1);

 // Create attribute with xml namespace
 Attribute attr2 = new Attribute("xml:attr2", "value2", "xml");
 rootElement.getContent().add(attr2);

 // Create attribute with custom namespace
 Element c1 = new Element("c", "http://test.com/ns");
 Attribute attr3 = new Attribute("name", "value3", "http://test.com/ns");
 c1.setAttribute(attr3);
 rootElement.getContent().add(c1);

 // Create iterator
 NodePointer root = new JDOMNodePointer(JDOMNode.TYPE_ELEMENT, rootElement);
 QName qname = new JDOMQName("*", null, null);
 NodeIterator iterator = new JDOMAttributeIterator(root, qname);
 this.namespaceContext = new JDOMNamespaceContext(rootElement.getNamespace());
 }

 @Test
 public void testEmptyIterator() {
 NodePointer nodePointer = iterator.next();
 Assert.assertNull(nodePointer);
 }

 @Test
 public void testIteratorWithDefaultNamespace() {
 NodePointer nodePointer = iterator.next();
 JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;
 JDOMAttribute attribute = attributePointer.getAttribute();
 Assert.assertEquals("attr1", attribute.getName().getLocalPart());
 Assert.assertEquals("value1", attribute.getValue());
 }

 @Test
 public void testIteratorWithXmlNamespace() {
 NodePointer nodePointer = iterator.next();
 JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;
 JDOMAttribute attribute = attributePointer.getAttribute();
 Assert.assertEquals("xml:attr2", attribute.getName().getQualifiedName());
 Assert.assertEquals("value2", attribute.getValue());
 }

 @Test
 public void testIteratorWithCustomNamespace() {
 NodePointer nodePointer = iterator.next();
 NodePointer nodePointerNext = iterator.next();
 JDOMAttributePointer attributePointer1 = (JDOMAttributePointer) nodePointer;
 JDOMAttributePointer attributePointer2 = (JDOMAttributePointer) nodePointerNext;
 JDOMAttribute attribute1 = attributePointer1.getAttribute();
 JDOMAttribute attribute2 = attributePointer2.getAttribute();
 Assert.assertEquals("http://test.com/ns:name", attribute1.getName().getQualifiedName());
 Assert.assertEquals("value3", attribute1.getValue());
 Assert.assertEquals("http://test.com/ns:name", attribute2.getName().getQualifiedName());
 Assert.assertNotSame(attribute1, attribute2);
 }

 @Test
 public void testIteratorNextAfterLastElement() {
 NodePointer nodePointer = iterator.next();
 // Move to the last position
 iterator.setPosition(iterator.getPosition() + iterator.getSize() - 1);
 NodePointer nodePointerLast = iterator.next();
 // Move to the next position, which should be null
 NodePointer nodePointerNext = iterator.next();
 Assert.assertNull(nodePointerNext);
 }

 @Test
 public void testSetPositionWithInvalidPosition() {
 iterator.setPosition(10);
 // Move back to the first position
 iterator.setPosition(0);
 NodePointer nodePointer = iterator.next();
 JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;
 JDOMAttribute attribute = attributePointer.getAttribute();
 // Check that the attribute is the same as the first one
 Assert.assertEquals("attr1", attribute.getName().getLocalPart());
 }
}