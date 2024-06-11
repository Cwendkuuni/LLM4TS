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
 private Element testElement;
 private JDOMAttributeIterator attributeIterator;

 @Before
 public void setup() {
 testElement = new Element("root", "xmlns:test", "http://test.com");
 testElement.addAttribute("attr1", "value1");
 testElement.addAttribute("attr2", "value2");
 testElement.addAttribute("attr3", "value3");
 nodePointer = new NodePointer(testElement);
 }

 @Test
 public void testGetNodePointer_WithValidPosition_ReturnsExpectedAttribute() {
 Attribute expectedAttribute = testElement.getAttribute("attr1");
 NodePointer actualNodePointer = attributeIterator.getNodePointer();
 JDOMAttributePointer actualAttributePointer = (JDOMAttributePointer) actualNodePointer;
 assertEquals(expectedAttribute, actualAttributePointer.getAttribute());
 }

 @Test
 public void testGetNodePointer_WithInvalidPosition_ReturnsNull() {
 attributeIterator = new JDOMAttributeIterator(nodePointer, new QName("test:attr4"));
 NodePointer actualNodePointer = attributeIterator.getNodePointer();
 assertNull(actualNodePointer);
 }

 @Test
 public void testGetNodePointer_WithLastPosition_ReturnsLastAttribute() {
 Attribute expectedAttribute = testElement.getAttribute("attr3");
 attributeIterator = new JDOMAttributeIterator(nodePointer, new QName("*"));
 attributeIterator.setPosition(attributeIterator.getPosition() + attributeIterator.getSize() - 1);
 NodePointer actualNodePointer = attributeIterator.getNodePointer();
 JDOMAttributePointer actualAttributePointer = (JDOMAttributePointer) actualNodePointer;
 assertEquals(expectedAttribute, actualAttributePointer.getAttribute());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetNodePointer_WithInvalidPosition_ThrowsIndexOutOfBoundsException() {
 attributeIterator = new JDOMAttributeIterator(nodePointer, new QName("*"));
 attributeIterator.setPosition(-1);
 attributeIterator.getNodePointer();
 }

 @Test
 public void testGetNodePointer_WithValidPositionAndDifferentNamespace_ReturnsExpectedAttribute() {
 Namespace testNamespace = new Namespace("test", "http://test.com");
 Attribute expectedAttribute = testElement.getAttribute("test:attr2", testNamespace);
 attributeIterator = new JDOMAttributeIterator(nodePointer, new QName("test:attr2", testNamespace));
 NodePointer actualNodePointer = attributeIterator.getNodePointer();
 JDOMAttributePointer actualAttributePointer = (JDOMAttributePointer) actualNodePointer;
 assertEquals(expectedAttribute, actualAttributePointer.getAttribute());
 }

 @Test
 public void testGetNodePointer_WithEmptyAttributeList_ReturnsNull() {
 testElement.removeAttribute("attr1");
 nodePointer = new NodePointer(testElement);
 attributeIterator = new JDOMAttributeIterator(nodePointer, new QName("*"));
 NodePointer actualNodePointer = attributeIterator.getNodePointer();
 assertNull(actualNodePointer);
 }
}