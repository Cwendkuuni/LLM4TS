import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 private Element createTestElement(String name, Namespace ns) {
 String xmlns = "http://example.com/xmlns";
 Element root = new Element(name);
 root.addNamespaceDeclaration(xmlns, ns);
 root.addAttribute("attr1", "value1", ns);
 root.addAttribute("attr2", "value2", ns);
 return root;
 }

 @Test
 public void testEmptyIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(null), new QName("", "*"));
 assertEquals(0, iterator.getPosition());
 assertNull(iterator.getNodePointer());
 }

 @Test
 public void testSingleAttributeIterator() {
 Element element = createTestElement("root", Namespace.NO_NAMESPACE);
 NodePointer pointer = new NodePointer(element);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(pointer, new QName("root", "*"));

 assertEquals(2, iterator.getPosition());
 NodePointer attributePointer = iterator.getNodePointer();
 assertEquals("value1", attributePointer.getNode().getValue());
 assertEquals("attr1", attributePointer.getNode().getName());
 assertEquals("", attributePointer.getNode().getNamespaceURI());

 iterator.setPosition(1);
 attributePointer = iterator.getNodePointer();
 assertEquals("value2", attributePointer.getNode().getValue());
 assertEquals("attr2", attributePointer.getNode().getName());
 assertEquals("", attributePointer.getNode().getNamespaceURI());
 }

 @Test
 public void testMultipleAttributesIterator() {
 Element element = createTestElement("root", Namespace.NO_NAMESPACE);
 NodePointer pointer = new NodePointer(element);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(pointer, new QName("root", "*"));

 assertEquals(2, iterator.getPosition());
 NodePointer attributePointer1 = iterator.getNodePointer();
 assertEquals("value1", attributePointer1.getNode().getValue());
 assertEquals("attr1", attributePointer1.getNode().getName());
 assertEquals("", attributePointer1.getNode().getNamespaceURI());

 iterator.setPosition(1);
 NodePointer attributePointer2 = iterator.getNodePointer();
 assertEquals("value2", attributePointer2.getNode().getValue());
 assertEquals("attr2", attributePointer2.getNode().getName());
 assertEquals("", attributePointer2.getNode().getNamespaceURI());
 }

 @Test
 public void testSingleAttributeWithNamespaceIterator() {
 Element element = createTestElement("root", new Namespace("xmlns", "http://example.com/xmlns"));
 NodePointer pointer = new NodePointer(element);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(pointer, new QName("root", "attr1", new Namespace("xmlns", "http://example.com/xmlns")));

 assertEquals(1, iterator.getPosition());
 NodePointer attributePointer = iterator.getNodePointer();
 assertEquals("value1", attributePointer.getNode().getValue());
 assertEquals("attr1", attributePointer.getNode().getName());
 assertEquals("http://example.com/xmlns", attributePointer.getNode().getNamespaceURI());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testNextAttributeOutOfBound() {
 Element element = createTestElement("root", Namespace.NO_NAMESPACE);
 NodePointer pointer = new NodePointer(element);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(pointer, new QName("root", "*"));

 iterator.setPosition(3);
 iterator.getNodePointer();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWrongNamespaceIterator() {
 Element element = createTestElement("root", Namespace.NO_NAMESPACE);
 NodePointer pointer = new NodePointer(element);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(pointer, new QName("root", "attr", new Namespace("http://example.com/xmlns2", "http://example.com/xmlns2")));
 }

 @Test
 public void testEmptyAttributeListIterator() {
 Element element = new Element("root");
 NodePointer pointer = new NodePointer(element);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(pointer, new QName("root", "*"));

 assertEquals(0, iterator.getPosition());
 assertNull(iterator.getNodePointer());
 }
}