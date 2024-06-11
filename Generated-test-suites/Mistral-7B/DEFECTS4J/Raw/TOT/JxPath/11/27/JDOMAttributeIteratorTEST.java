import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 private NodePointer nodePointer;
 private NodeIterator iterator;
 private Element testElement;

 @Before
 public void setUp() {
 Element root = new Element("root");
 root.addNamespaceDeclaration("xml", "http://www.w3.org/XML/1998/namespace");
 testElement = new Element("test");
 testElement.addNamespaceDeclaration("test", "http://test.com/namespace");
 testElement.addAttribute("attr1", "value1", "xml");
 testElement.addAttribute("attr2", "value2", "test");
 testElement.addAttribute("attr3", "value3", "test");
 root.addContent(testElement);

 nodePointer = new NodePointer(root);
 iterator = new JDOMAttributeIterator(nodePointer, new QName("test", "test"));
 }

 @Test
 public void testIteration() {
 List<Attribute> attributes = (List<Attribute>) iterator.getIterator().toJavaList();
 assertEquals(3, attributes.size());
 for (int i = 0; i < 3; i++) {
 NodePointer attributePointer = iterator.getNodePointer();
 assertEquals("attr" + (i + 1), attributePointer.getName().getLocalPart());
 assertEquals("test", attributePointer.getName().getPrefix());
 assertEquals("value" + (i + 1), attributePointer.getValue().getValue());
 iterator.next();
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testNoAttributes() {
 NodePointer attributePointer = iterator.getNodePointer();
 iterator = new JDOMAttributeIterator(nodePointer, new QName("xml", "*"));
 assertNull(iterator.getNodePointer());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testOutOfBounds() {
 iterator = new JDOMAttributeIterator(nodePointer, new QName("test", "*"));
 iterator.next();
 iterator.next();
 NodePointer attributePointer = iterator.getNodePointer();
 assertNull(attributePointer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidQName() {
 iterator = new JDOMAttributeIterator(nodePointer, new QName("test", "test", "http://test.com/wrong"));
 assertNull(iterator.getNodePointer());
 }
}