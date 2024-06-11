import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JDOMAttributeIteratorTest {

 @Test
 public void testEmptyIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(null), new QName("", ""));
 Assertions.assertNull(iterator.getNodePointer());
 Assertions.assertEquals(-1, iterator.getPosition());
 }

 @Test
 public void testSingleAttributeIterator() {
 Element element = new Element("rootElement");
 Attribute attribute = new Attribute("attributeName", "attributeValue", "http://test.com/namespace");
 element.addAttribute(attribute);

 NodePointer parent = new NodePointer(element);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("http://test.com/namespace", "attributeName"));
 NodePointer attributePointer = iterator.getNodePointer();

 Assertions.assertNotNull(attributePointer);
 Assertions.assertEquals("attributeName", attributePointer.getNodeName().getName());
 Assertions.assertEquals("http://test.com/namespace", attributePointer.getNodeName().getNamespaceURI());
 Assertions.assertEquals("attributeValue", attributePointer.getNodeValue().getValue());
 Assertions.assertEquals(1, iterator.getPosition());

 // test setPosition
 iterator.setPosition(0);
 Assertions.assertEquals(-1, iterator.getPosition());
 Assertions.assertNull(iterator.getNodePointer());

 iterator.setPosition(1);
 Attribute nextAttribute = (Attribute) iterator.getNodePointer().getNode();
 Assertions.assertEquals("attributeName", nextAttribute.getName());
 Assertions.assertEquals("attributeValue", nextAttribute.getValue());
 Assertions.assertEquals(1, iterator.getPosition());
 }

 @Test
 public void testMultipleAttributesIterator() {
 Element element = new Element("rootElement");
 Attribute attribute1 = new Attribute("attributeName1", "attributeValue1", "http://test.com/namespace1");
 Attribute attribute2 = new Attribute("attributeName2", "attributeValue2", "http://test.com/namespace1");
 Attribute attribute3 = new Attribute("attributeName3", "attributeValue3", "http://test.com/namespace2");
 Attribute attribute4 = new Attribute("attributeName4", "attributeValue4", "http://test.com/namespace2");
 element.addAttribute(attribute1);
 element.addAttribute(attribute2);
 element.addAttribute(attribute3);
 element.addAttribute(attribute4);

 NodePointer parent = new NodePointer(element);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));

 // test first attribute
 NodePointer attributePointer = iterator.getNodePointer();
 Assertions.assertNotNull(attributePointer);
 Assertions.assertEquals("attributeName1", attributePointer.getNodeName().getName());
 Assertions.assertEquals("", attributePointer.getNodeName().getNamespaceURI());
 Assertions.assertEquals("attributeValue1", attributePointer.getNodeValue().getValue());
 Assertions.assertEquals(1, iterator.getPosition());

 // test next attributes
 for (int i = 0; i < 3; i++) {
 attributePointer = iterator.getNodePointer();
 Assertions.assertNotNull(attributePointer);
 Assertions.assertEquals("attributeName" + (i + 2), attributePointer.getNodeName().getName());
 Assertions.assertEquals("http://test.com/namespace" + (i / 2 + 1), attributePointer.getNodeName().getNamespaceURI());
 Assertions.assertEquals("attributeValue" + (i + 2), attributePointer.getNodeValue().getValue());
 Assertions.assertEquals(i + 2, iterator.getPosition());
 iterator.setPosition(iterator.getPosition() + 1);
 }

 // test last attribute
 attributePointer = iterator.getNodePointer();
 Assertions.assertNotNull(attributePointer);
 Assertions.assertEquals("attributeName4", attributePointer.getNodeName().getName());
 Assertions.assertEquals("http://test.com/namespace2", attributePointer.getNodeName().getNamespaceURI());
 Assertions.assertEquals("attributeValue4", attributePointer.getNodeValue().getValue());
 Assertions.assertEquals(5, iterator.getPosition());

 // test setPosition with invalid position
 iterator.setPosition(6);
 Assertions.assertEquals(-1, iterator.getPosition());
 Assertions.assertNull(iterator.getNodePointer());
 }

 @Test
 public void testSingleNamespaceIterator() {
 Element element = new Element("rootElement");
 Attribute attribute = new Attribute("attributeName", "attributeValue", "http://test.com/namespace");
 element.addAttribute(attribute);

 NodePointer parent = new NodePointer(element);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("http://test.com/namespace", "attributeName"));
 NodePointer attributePointer = iterator.getNodePointer();

 Assertions.assertNotNull(attributePointer);
 Assertions.assertEquals("attributeName", attributePointer.getNodeName().getName());
 Assertions.assertEquals("http://test.com/namespace", attributePointer.getNodeName().getNamespaceURI());
 Assertions.assertEquals("attributeValue", attributePointer.getNodeValue().getValue());
 Assertions.assertEquals(1, iterator.getPosition());
 }

 @Test
 public void testMultipleNamespacesIterator() {
 Element element = new Element("rootElement");
 Attribute attribute1 = new Attribute("attributeName1", "attributeValue1", "http://test.com/namespace1");
 Attribute attribute2 = new Attribute("attributeName2", "attributeValue2", "http://test.com/namespace2");
 Attribute attribute3 = new Attribute("attributeName3", "attributeValue3", "http://test.com/namespace3");
 Attribute attribute4 = new Attribute("attributeName4", "attributeValue4", "http://test.com/namespace3");
 element.addAttribute(attribute1);
 element.addAttribute(attribute2);
 element.addAttribute(attribute3);
 element.addAttribute(attribute4);

 NodePointer parent = new NodePointer(element);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));

 // test first attribute with namespace 1
 NodePointer attributePointer = iterator.getNodePointer();
 Assertions.assertNotNull(attributePointer);
 Assertions.assertEquals("attributeName1", attributePointer.getNodeName().getName());
 Assertions.assertEquals("http://test.com/namespace1", attributePointer.getNodeName().getNamespaceURI());
 Assertions.assertEquals("attributeValue1", attributePointer.getNodeValue().getValue());
 Assertions.assertEquals(1, iterator.getPosition());

 // test next attribute with namespace 1
 iterator.setPosition(1);
 attributePointer = iterator.getNodePointer();
 Assertions.assertNotNull(attributePointer);
 Assertions.assertEquals("attributeName2", attributePointer.getNodeName().getName());
 Assertions.assertEquals("http://test.com/namespace1", attributePointer.getNodeName().getNamespaceURI());
 Assertions.assertEquals("attributeValue2", attributePointer.getNodeValue().getValue());
 Assertions.assertEquals(2, iterator.getPosition());

 // test next attribute with namespace 2
 iterator.setPosition(3);
 attributePointer = iterator.getNodePointer();
 Assertions.assertNotNull(attributePointer);
 Assertions.assertEquals("attributeName3", attributePointer.getNodeName().getName());
 Assertions.assertEquals("http://test.com/namespace3", attributePointer.getNodeName().getNamespaceURI());
 Assertions.assertEquals("attributeValue3", attributePointer.getNodeValue().getValue());
 Assertions.assertEquals(4, iterator.getPosition());

 // test next attribute with namespace 3
 iterator.setPosition(5);
 attributePointer = iterator.getNodePointer();
 Assertions.assertNotNull(attributePointer);
 Assertions.assertEquals("attributeName4", attributePointer.getNodeName().getName());
 Assertions.assertEquals("http://test.com/namespace3", attributePointer.getNodeName().getNamespaceURI());
 Assertions.assertEquals("attributeValue4", attributePointer.getNodeValue().getValue());
 Assertions.assertEquals(5, iterator.getPosition());
 }

 @Test
 public void testEmptyNamespaceIterator() {
 Element element = new Element("rootElement");
 Attribute attribute1 = new Attribute("attributeName1", "attributeValue1");
 Attribute attribute2 = new Attribute("attributeName2", "attributeValue2");
 element.addAttribute(attribute1);
 element.addAttribute(attribute2);

 NodePointer parent = new NodePointer(element);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
 NodePointer attributePointer = iterator.getNodePointer();

 // test first attribute with empty namespace
 Assertions.assertNotNull(attributePointer);
 Assertions.assertEquals("attributeName1", attributePointer.getNodeName().getName());
 Assertions.assertEquals("", attributePointer.getNodeName().getNamespaceURI());
 Assertions.assertEquals("attributeValue1", attributePointer.getNodeValue().getValue());
 Assertions.assertEquals(1, iterator.getPosition());

 // test next attribute with empty namespace
 iterator.setPosition(1);
 attributePointer = iterator.getNodePointer();
 Assertions.assertNotNull(attributePointer);
 Assertions.assertEquals("attributeName2", attributePointer.getNodeName().getName());
 Assertions.assertEquals("", attributePointer.getNodeName().getNamespaceURI());
 Assertions.assertEquals("attributeValue2", attributePointer.getNodeValue().getValue());
 Assertions.assertEquals(2, iterator.getPosition());
 }
}