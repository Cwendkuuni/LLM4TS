import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 private static final QName ATTRIBUTE_NAME = new QName("test", "namespace", "attrName");
 private static final String XML_NAMESPACE_PREFIX = "xml";
 private static final String NO_NAMESPACE_PREFIX = "";

 @Test
 public void testEmptyIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, ATTRIBUTE_NAME);
 assertNotNull(iterator);
 assertTrue(iterator.hasNext());
 assertNull(iterator.next());
 }

 @Test
 public void testSingleAttributeIterator() {
 Element element = new Element("testElement");
 Attribute attribute = new Attribute("attrName", "attrValue", "namespace");
 element.addAttribute(attribute);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), ATTRIBUTE_NAME);
 assertNotNull(iterator);
 assertTrue(iterator.hasNext());
 JDOMAttributePointer next = (JDOMAttributePointer) iterator.next();
 assertEquals(attribute, next.getNode());
 assertTrue(iterator.hasNext());
 next = (JDOMAttributePointer) iterator.next();
 assertNull(next.getNode());
 }

 @Test
 public void testMultiAttributeIterator() {
 Element element = new Element("testElement");
 Attribute attr1 = new Attribute("attrName1", "attrValue1", "namespace1");
 Attribute attr2 = new Attribute("attrName2", "attrValue2", "namespace1");
 Attribute attr3 = new Attribute("attrName3", "attrValue3", "namespace2");
 element.addAttribute(attr1);
 element.addAttribute(attr2);
 element.addAttribute(attr3);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), ATTRIBUTE_NAME);
 assertNotNull(iterator);
 assertTrue(iterator.hasNext());
 JDOMAttributePointer next = (JDOMAttributePointer) iterator.next();
 assertEquals(attr1, next.getNode());
 assertTrue(iterator.hasNext());
 next = (JDOMAttributePointer) iterator.next();
 assertEquals(attr2, next.getNode());
 assertTrue(iterator.hasNext());
 next = (JDOMAttributePointer) iterator.next();
 assertEquals(attr3, next.getNode());
 assertTrue(iterator.hasNext());
 assertNull(iterator.next());
 }

 @Test
 public void testIteratorWithXmlNamespace() {
 Element element = new Element("testElement");
 Attribute attr1 = new Attribute("attrName", "attrValue", XML_NAMESPACE_PREFIX);
 element.addAttribute(attr1);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), ATTRIBUTE_NAME);
 assertNotNull(iterator);
 assertTrue(iterator.hasNext());
 JDOMAttributePointer next = (JDOMAttributePointer) iterator.next();
 assertEquals(attr1, next.getNode());
 assertTrue(iterator.hasNext());
 assertNull(iterator.next());
 }

 @Test
 public void testIteratorWithNoNamespace() {
 Element element = new Element("testElement");
 Attribute attr1 = new Attribute("attrName", "attrValue");
 element.addAttribute(attr1);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), ATTRIBUTE_NAME);
 assertNotNull(iterator);
 assertTrue(iterator.hasNext());
 JDOMAttributePointer next = (JDOMAttributePointer) iterator.next();
 assertEquals(attr1, next.getNode());
 assertTrue(iterator.hasNext());
 assertNull(iterator.next());
 }

 @Test
 public void testIteratorWithNoMatchingAttributes() {
 Element element = new Element("testElement");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), ATTRIBUTE_NAME);
 assertNotNull(iterator);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithMismatchedNamespace() {
 Element element = new Element("testElement");
 Attribute attr1 = new Attribute("attrName", "attrValue", "namespace1");
 element.addAttribute(attr1);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("test", "namespace2", "attrName"));
 assertNotNull(iterator);
 assertFalse(iterator.hasNext());
 }

}