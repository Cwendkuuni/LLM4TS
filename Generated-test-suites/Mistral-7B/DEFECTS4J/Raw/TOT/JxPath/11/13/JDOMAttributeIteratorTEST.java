import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 private static final String XML_NS = "xmlns";
 private static final String PREFIX = "prefix";
 private static final String LOCAL_NAME = "localName";
 private static final String NAMESPACE_URI = "namespaceURI";
 private static final String VALUE = "value";

 @Test
 public void testEmptyAttributeIterator() {
 Element emptyElement = new Element("emptyElement");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(emptyElement), new QName(PREFIX, LOCAL_NAME));
 assertEquals(Collections.EMPTY_LIST, iterator.getAttributes());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testSingleAttributeIterator() {
 Element singleElement = new Element("singleElement");
 singleElement.addAttribute(new Attribute(XML_NS, "xmlns:x", "http://example.com/"));
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(singleElement), new QName("", "xmlns"));
 assertEquals(Collections.singletonList(singleElement.getAttribute(XML_NS)), iterator.getAttributes());
 assertTrue(iterator.hasNext());
 }

 @Test
 public void testMultipleAttributesIterator() {
 Element multipleElement = new Element("multipleElement");
 multipleElement.addAttribute(new Attribute(XML_NS, "xmlns:x", "http://example.com/"));
 multipleElement.addAttribute(new Attribute("prefix1", "attr1", "namespace1", "value1"));
 multipleElement.addAttribute(new Attribute("prefix2", "attr2", "namespace2", "value2"));
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(multipleElement), new QName("", "*"));
 List<Attribute> attributes = iterator.getAttributes();
 assertEquals(3, attributes.size());
 assertTrue(iterator.hasNext());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIteratorOutOfBoundsException() {
 Element multipleElement = new Element("multipleElement");
 multipleElement.addAttribute(new Attribute(XML_NS, "xmlns:x", "http://example.com/"));
 multipleElement.addAttribute(new Attribute("prefix1", "attr1", "namespace1", "value1"));
 multipleElement.addAttribute(new Attribute("prefix2", "attr2", "namespace2", "value2"));
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(multipleElement), new JDOMAttributePointer(multipleElement, new Attribute("prefix2", "attr2", "namespace2", "value2")));
 assertEquals(3, iterator.getPosition());
 iterator.next();
 assertEquals(3, iterator.getPosition());
 iterator.next();
 iterator.next();
 iterator.next(); // IndexOutOfBoundsException should be thrown here
 }

 @Test
 public void testIteratorToFirstPosition() {
 Element multipleElement = new Element("multipleElement");
 multipleElement.addAttribute(new Attribute(XML_NS, "xmlns:x", "http://example.com/"));
 multipleElement.addAttribute(new Attribute("prefix1", "attr1", "namespace1", "value1"));
 multipleElement.addAttribute(new Attribute("prefix2", "attr2", "namespace2", "value2"));
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(multipleElement), new JDOMAttributePointer(multipleElement, new Attribute("prefix2", "attr2", "namespace2", "value2")));
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.previous();
 assertEquals(iterator.getPosition(), 2);
 }
}