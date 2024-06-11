import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 private NodePointer parentNode;
 private QName attributeName;
 private JDOMAttributeIterator iterator;

 @Test
 public void testEmptyIterator() {
 parentNode = new NodePointer(new Element("root"));
 iterator = new JDOMAttributeIterator(parentNode, new QName("", "*"));
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithOneAttribute() {
 Element root = new Element("root");
 root.addAttribute("attr1", "value1");
 parentNode = new NodePointer(root);
 attributeName = new QName("", "attr1");
 iterator = new JDOMAttributeIterator(parentNode, attributeName);
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 assertEquals(next.getNode().getValue(), "value1");
 }

 @Test
 public void testIteratorWithMultipleAttributes() {
 Element root = new Element("root");
 root.addAttribute("attr1", "value1");
 root.addAttribute("attr2", "value2");
 parentNode = new NodePointer(root);
 attributeName = new QName("", "*");
 iterator = new JDOMAttributeIterator(parentNode, attributeName);
 assertEquals(2, iterator.getPosition());
 assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 assertEquals(next.getNode().getValue(), "value1");
 next = iterator.next();
 assertEquals(next.getNode().getValue(), "value2");
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithNamespace() {
 Namespace ns = Namespace.getXmlNamespace();
 Element root = new Element("root", ns);
 root.addAttribute("xml:attr1", "value1");
 parentNode = new NodePointer(root);
 attributeName = new QName("", "xml:attr1");
 iterator = new JDOMAttributeIterator(parentNode, attributeName);
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 assertEquals(next.getNode().getValue(), "value1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIteratorWithInvalidAttributeName() {
 Element root = new Element("root");
 root.addAttribute("attr1", "value1");
 parentNode = new NodePointer(root);
 attributeName = new QName("", "attrX");
 iterator = new JDOMAttributeIterator(parentNode, attributeName);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIteratorWithNullParentNode() {
 attributeName = new QName("", "");
 iterator = new JDOMAttributeIterator(null, attributeName);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testIteratorWithEmptyParentNode() {
 Element root = new Element("root");
 parentNode = new NodePointer(root);
 attributeName = new QName("", "*");
 iterator = new JDOMAttributeIterator(parentNode, attributeName);
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }
}