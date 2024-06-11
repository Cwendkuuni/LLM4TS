import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.junit.Test;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

 private NodePointer parent;
 private QName name;
 private DOMAttributeIterator iterator;

 @Test
 public void testEmptyIterator() {
 iterator = new DOMAttributeIterator(null, null);
 assertTrue(iterator.isEmpty());
 }

 @Test
 public void testSimpleIterator() {
 parent = new DOMPointer("root", null, null);
 name = new QName("test", "http://test.com", "test");
 iterator = new DOMAttributeIterator(parent, name);
 assertFalse(iterator.isEmpty());
 assertEquals(0, iterator.getPosition());
 }

 @Test
 public void testIteratorWithSingleAttribute() {
 Element element = new Element("root");
 element.setAttribute("test", "value");
 parent = new DOMPointer("root", null, null);
 name = new QName("test", null, "test");
 iterator = new DOMAttributeIterator(parent, name);
 assertFalse(iterator.isEmpty());
 assertEquals(0, iterator.getPosition());
 assertEquals("value", iterator.getNodePointer().getNodeValue());
 }

 @Test
 public void testIteratorWithMultipleAttributes() {
 Element element = new Element("root");
 element.setAttribute("test1", "value1");
 element.setAttribute("test2", "value2");
 parent = new DOMPointer("root", null, null);
 name = new QName("test", null, "test");
 iterator = new DOMAttributeIterator(parent, name);
 assertFalse(iterator.isEmpty());
 assertEquals(0, iterator.getPosition());
 assertEquals("value1", iterator.getNodePointer().getNodeValue());
 iterator.next();
 assertEquals("value2", iterator.getNodePointer().getNodeValue());
 }

 @Test
 public void testIteratorWithNamespaceAttribute() {
 Element element = new Element("root");
 element.setAttributeNS("xmlns:prefix", "prefix:test", "value");
 parent = new DOMPointer("root", null, "prefix");
 name = new QName("test", null, "prefix");
 iterator = new DOMAttributeIterator(parent, name);
 assertFalse(iterator.isEmpty());
 assertEquals(0, iterator.getPosition());
 assertEquals("value", iterator.getNodePointer().getNodeValue());
 }

 @Test
 public void testIteratorWithMixedAttributes() {
 Element element = new Element("root");
 element.setAttribute("test1", "value1");
 element.setAttributeNS("xmlns:prefix", "prefix:test2", "value2");
 parent = new DOMPointer("root", null, null);
 name = new QName("test", null, "*");
 iterator = new DOMAttributeIterator(parent, name);
 assertFalse(iterator.isEmpty());
 assertEquals(0, iterator.getPosition());
 assertEquals("value1", iterator.getNodePointer().getNodeValue());
 iterator.next();
 assertEquals("value2", iterator.getNodePointer().getNodeValue());
 }

 @Test
 public void testIteratorWithNoMatchingAttribute() {
 parent = new DOMPointer("root", null, null);
 name = new QName("test", null, "test");
 iterator = new DOMAttributeIterator(parent, name);
 assertFalse(iterator.isEmpty());
 assertEquals(0, iterator.getPosition());
 assertTrue(iterator.next());
 assertTrue(iterator.isEmpty());
 }

 @Test
 public void testIteratorWithParentChange() {
 Element rootElement = new Element("root");
 rootElement.setAttribute("test1", "value1");
 Element childElement = rootElement.createElement("child");
 childElement.setAttribute("test2", "value2");
 rootElement.appendChild(childElement);
 parent = new DOMPointer("root", null, null);
 name = new QName("test", null, "*");
 iterator = new DOMAttributeIterator(parent, name);
 assertFalse(iterator.isEmpty());
 assertEquals(0, iterator.getPosition());
 assertEquals("value1", iterator.getNodePointer().getNodeValue());
 iterator.next();
 assertEquals("value2", iterator.getNodePointer().getNodeValue());
 parent = new DOMPointer("child", null, null);
 iterator = new DOMAttributeIterator(parent, name);
 assertFalse(iterator.isEmpty());
 assertEquals(0, iterator.getPosition());
 assertEquals("test2", iterator.getNodePointer().getNodeName().getName());
 }
}