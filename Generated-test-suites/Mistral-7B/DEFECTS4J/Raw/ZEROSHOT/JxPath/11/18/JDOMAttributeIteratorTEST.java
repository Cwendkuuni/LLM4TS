import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 @Test
 public void testEmptyIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(null), null);
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testClassicAttributeIterator() {
 String xmlns = "xml";
 String prefix = "xml";
 String localName = "xml:lang";
 String expectedValue = "en";

 Element element = new Element("root");
 element.addAttribute(new Attribute(xmlns, "xml:lang", expectedValue));

 NodePointer parent = new NodePointer(element);

 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, localName));
 NodePointer attributePointer = iterator.next();
 assertEquals(expectedValue, attributePointer.getNode().getValue());
 assertEquals(1, iterator.getPosition());

 assertTrue(iterator.hasNext());
 iterator.next();
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testWildcardAttributeIterator() {
 String xmlns = "xml";
 String prefix = "xml";
 String localName = "*";
 String expectedValue1 = "lang";
 String expectedValue2 = "version";
 String expectedValue3 = "xmlns:xsi";

 Element element = new Element("root");
 element.addAttribute(new Attribute(xmlns, "xml:lang", "en"));
 element.addAttribute(new Attribute("xml:version", "1.0"));
 element.addAttribute(new Attribute(xmlns, "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance"));

 NodePointer parent = new NodePointer(element);

 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, localName));

 NodePointer attribute1 = iterator.next();
 assertEquals(expectedValue1, attribute1.getNode().getName().getLocalPart());
 assertEquals(expectedValue1, attribute1.getNode().getValue());

 NodePointer attribute2 = iterator.next();
 assertEquals(expectedValue2, attribute2.getNode().getName().getLocalPart());

 NodePointer attribute3 = iterator.next();
 assertEquals(expectedValue3, attribute3.getNode().getName().getLocalPart());

 assertFalse(iterator.hasNext());
 }

 @Test
 public void testNullParent() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(null), new QName("xml", "*"));
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test(expected = IllegalStateException.class)
 public void testNullName() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(new Element("root")), null);
 }
}