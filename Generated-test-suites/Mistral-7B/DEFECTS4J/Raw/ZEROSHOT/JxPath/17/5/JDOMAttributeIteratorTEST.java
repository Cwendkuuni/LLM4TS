import org.apache.commons.jxpath.ri.NodeIterator;
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
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(null), new QName("", ""));
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithSingleAttribute() {
 Element root = new Element("root");
 root.addAttribute("attr", "value");
 NodePointer nodePointer = new NodePointer(root);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "attr"));
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 assertEquals(next.getNode(), root.getAttribute("attr"));
 }

 @Test
 public void testIteratorWithMultipleAttributes() {
 Element root = new Element("root");
 root.addAttribute("attr1", "value1");
 root.addAttribute("attr2", "value2");
 root.addAttribute("attr3", "value3");
 NodePointer nodePointer = new NodePointer(root);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "*"));
 assertEquals(3, iterator.getPosition());
 assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 assertEquals(next.getNode(), root.getAttribute("attr1"));
 next = iterator.next();
 assertEquals(next.getNode(), root.getAttribute("attr2"));
 next = iterator.next();
 assertEquals(next.getNode(), root.getAttribute("attr3"));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithNamespace() {
 Element root = new Element("root");
 root.addNamespaceDeclaration("xml", "http://www.w3.org/XML/1998/namespace");
 root.addAttribute("xml:attr", "value");
 NodePointer nodePointer = new NodePointer(root);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("xml", "attr"));
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 assertEquals(next.getNode(), root.getAttribute("xml:attr"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIteratorWithNullParent() {
 new JDOMAttributeIterator(null, new QName("", ""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIteratorWithNullName() {
 new JDOMAttributeIterator(new NodePointer(null), null);
 }
}