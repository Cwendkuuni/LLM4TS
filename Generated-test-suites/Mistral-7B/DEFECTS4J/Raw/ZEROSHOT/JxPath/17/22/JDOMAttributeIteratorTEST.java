import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class JDOMAttributeIteratorTest {

 @Test
 public void testEmptyIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(null), new QName("", ""));
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testSingleAttributeIterator() {
 Element element = new Element("root");
 Attribute attribute = new Attribute("name", "value", new Namespace("prefix", "http://test.com"));
 element.addAttribute(attribute);
 NodePointer parent = new NodePointer(element);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("prefix:name"));
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 assertSame(next.getNode(), attribute);
 assertSame(next.getNodePointer().getParent(), parent);
 }

 @Test
 public void testMultipleAttributesIterator() {
 Element element = new Element("root");
 Attribute attribute1 = new Attribute("name1", "value1", new Namespace("prefix1", "http://test1.com"));
 Attribute attribute2 = new Attribute("name2", "value2", new Namespace("prefix2", "http://test2.com"));
 Attribute attribute3 = new Attribute("name3", "value3", new Namespace("prefix3", "http://test3.com"));
 element.addAttribute(attribute1);
 element.addAttribute(attribute2);
 element.addAttribute(attribute3);
 NodePointer parent = new NodePointer(element);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));
 assertEquals(3, iterator.getPosition());
 assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 assertSame(next.getNode(), attribute1);
 assertSame(next.getNodePointer().getParent(), parent);
 next = iterator.next();
 assertSame(next.getNode(), attribute2);
 assertSame(next.getNodePointer().getParent(), parent);
 next = iterator.next();
 assertSame(next.getNode(), attribute3);
 assertSame(next.getNodePointer().getParent(), parent);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testNamespaceIterator() {
 Element element = new Element("root");
 Attribute attribute1 = new Attribute("name1", "value1", new Namespace("prefix1", "http://test1.com"));
 Attribute attribute2 = new Attribute("name2", "value2", new Namespace("prefix2", "http://test2.com"));
 Attribute attribute3 = new Attribute("name3", "value3", new Namespace("prefix3", "http://test3.com"));
 element.addAttribute(attribute1);
 element.addAttribute(attribute2);
 element.addAttribute(attribute3);
 NodePointer parent = new NodePointer(element);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("prefix3:name3"));
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 assertSame(next.getNode(), attribute3);
 assertSame(next.getNodePointer().getParent(), parent);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testEmptyNamespaceIterator() {
 Element element = new Element("root");
 Attribute attribute1 = new Attribute("name1", "value1", new Namespace("prefix1", "http://test1.com"));
 Attribute attribute2 = new Attribute("name2", "value2", new Namespace("prefix2", "http://test2.com"));
 Attribute attribute3 = new Attribute("name3", "value3", new Namespace("prefix3", "http://test3.com"));
 element.addAttribute(attribute1);
 element.addAttribute(attribute2);
 NodePointer parent = new NodePointer(element);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));
 assertEquals(3, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testInvalidIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(null), new QName(""));
 assertFalse(iterator.hasNext());
 assertNull(iterator.next());
 }
}