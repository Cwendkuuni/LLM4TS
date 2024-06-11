import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 @Test
 public void testEmptyIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(null), new QName("", "*"));
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithSingleAttribute() {
 Element element = new Element("root", "root");
 Attribute attribute = new Attribute("test", "testValue", "testNs");
 element.addAttribute(attribute);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("testNs:test"));
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 assertEquals(next.getNode(), attribute);
}

 @Test
 public void testIteratorWithMultipleAttributes() {
 Element element = new Element("root", "root");
 Attribute attribute1 = new Attribute("test1", "testValue1", "testNs1");
 Attribute attribute2 = new Attribute("test2", "testValue2", "testNs2");
 element.addAttribute(attribute1);
 element.addAttribute(attribute2);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("testNs1:test1"));
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 assertEquals(next.getNode(), attribute1);
 iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("testNs2:test2"));
 assertEquals(2, iterator.getPosition());
 assertTrue(iterator.hasNext());
 next = iterator.next();
 assertEquals(next.getNode(), attribute2);
}

 @Test
 public void testIteratorWithNoMatchingAttribute() {
 Element element = new Element("root", "root");
 Attribute attribute1 = new Attribute("test1", "testValue1", "testNs1");
 Attribute attribute2 = new Attribute("test2", "testValue2", "testNs2");
 element.addAttribute(attribute1);
 element.addAttribute(attribute2);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("testNs3:test3"));
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithWildcardName() {
 Element element = new Element("root", "root");
 Attribute attribute1 = new Attribute("test1", "testValue1", "testNs1");
 Attribute attribute2 = new Attribute("test2", "testValue2", "testNs2");
 element.addAttribute(attribute1);
 element.addAttribute(attribute2);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", "*"));
 assertEquals(2, iterator.getPosition());
 assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 assertNotNull(next.getNode());
 next = iterator.next();
 assertNotNull(next.getNode());
 assertFalse(iterator.hasNext());
 }
}