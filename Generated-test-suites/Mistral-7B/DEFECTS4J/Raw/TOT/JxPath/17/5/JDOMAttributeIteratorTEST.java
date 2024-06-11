import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 private NodePointer rootNode;
 private Element testElement;

 @Before
 public void setUp() {
 Element root = new Element("root");
 testElement = new Element("test");
 testElement.addAttribute("id", "1");
 testElement.addAttribute("name", "testName", "testNS");
 root.addContent(testElement);
 rootNode = new NodePointer(root);
 }

 @Test
 public void testAttributeIteratorEmpty() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(rootNode, null);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testAttributeIteratorSingleAttribute() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(testElement.getParentElement().getParent(), new QName("*", null));
 Attribute attribute = (Attribute) iterator.next();
 assertEquals("1", attribute.getValue());
 }

 @Test
 public void testAttributeIteratorMultipleAttributes() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(testElement, new QName("*", "testNS"));
 Attribute attribute1 = (Attribute) iterator.next();
 assertEquals("testName", attribute1.getName());
 Attribute attribute2 = (Attribute) iterator.next();
 assertNull(attribute2);
 }

 @Test
 public void testAttributeIteratorWithNamespace() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(testElement, new QName("name", "testNS"));
 Attribute attribute = (Attribute) iterator.next();
 assertEquals("testName", attribute.getName());
 }

 @Test
 public void testAttributeIteratorWithPrefix() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(testElement.getParentElement().getParent(), new QName("test:name", null));
 Attribute attribute = (Attribute) iterator.next();
 assertEquals("testName", attribute.getName());
 }

 @Test
 public void testAttributeIteratorWithEmptyNamespace() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(testElement, new QName("name", ""));
 Attribute attribute = (Attribute) iterator.next();
 assertEquals("testName", attribute.getName());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAttributeIteratorOutOfBounds() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(testElement, new QName("name", "testNS"));
 iterator.next();
 iterator.next();
 iterator.next(); // No such attribute, should throw an exception
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeIteratorNullParent() {
 new JDOMAttributeIterator(null, new QName("name", "testNS"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeIteratorNullName() {
 new JDOMAttributeIterator(rootNode, null);
 }
}