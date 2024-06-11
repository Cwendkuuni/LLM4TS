import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

 private NodePointer parentNode;
 private DOMAttributeIterator iterator;
 private Element testElement;

 @Before
 public void setUp() {
 testElement = new DOMElementFactory().createElement("test");
 testElement.setAttributeNode(new DOMAttributeFactory().createAttribute("test1", "value1"));
 testElement.setAttributeNode(new DOMAttributeFactory().createAttribute("test2", "value2"));
 testElement.setAttributeNode(new DOMAttributeFactory().createAttribute("xmlns:x", "http://test.com/x"));
 Node parentNodeNode = new DOMElementFactory().createElement("parent");
 parentNodeNode.appendChild(testElement);
 parentNode = new DOMPointerFactory().createNodePointer(parentNodeNode, 0);
 }

 @Test
 public void testConstructor() {
 iterator = new DOMAttributeIterator(parentNode, new QName("test", "test1"));
 assertEquals(2, iterator.getPosition());
 }

 @Test
 public void testConstructorWithWildcard() {
 iterator = new DOMAttributeIterator(parentNode, new QName("*", "*"));
 assertEquals(3, iterator.getPosition());
 }

 @Test
 public void testConstructorWithNamespace() {
 iterator = new DOMAttributeIterator(parentNode, new QName("http://test.com/x", "test1"));
 assertEquals(1, iterator.getPosition());
 }

 @Test
 public void testNext() {
 iterator = new DOMAttributeIterator(parentNode, new QName("test", "test1"));
 iterator.next();
 iterator.next();
 iterator.next();
 assertEquals(3, iterator.getPosition());
 }

 @Test
 public void testPrevious() {
 iterator = new DOMAttributeIterator(parentNode, new QName("test", "test1"));
 iterator.next();
 iterator.next();
 iterator.previous();
 assertEquals(2, iterator.getPosition());
 }

 @Test
 public void testGetNodePointer() {
 NodePointer nodePointer = iterator.getNodePointer();
 assertTrue(nodePointer instanceof DOMAttributePointer);
 }

 @Test
 public void testGetPosition() {
 iterator = new DOMAttributeIterator(parentNode, new QName("test", "test1"));
 iterator.next();
 iterator.next();
 assertEquals(3, iterator.getPosition());
 }

 @Test
 public void testSetPositionToFirst() {
 iterator = new DOMAttributeIterator(parentNode, new QName("test", "test1"));
 iterator.setPosition(1);
 assertTrue(iterator.getNodePointer() instanceof DOMAttributePointer);
 iterator.next();
 assertEquals(3, iterator.getPosition());
 }

 @Test
 public void testSetPositionToLast() {
 iterator = new DOMAttributeIterator(parentNode, new QName("test", "test1"));
 iterator.setPosition(3);
 assertTrue(iterator.getNodePointer() instanceof DOMAttributePointer);
 iterator.previous();
 assertEquals(2, iterator.getPosition());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionToInvalidPosition() {
 iterator = new DOMAttributeIterator(parentNode, new QName("test", "test1"));
 iterator.setPosition(4);
 }
}