import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMock.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(JDOMAttributeIterator.class)
public class JDOMAttribeIteratorTest {

 private Element testElement;
 private NodePointer testParentNodePointer;

 @Before
 public void setUp() {
 testElement = new Element("testElement");
 testParentNodePointer = new NodePointer(new Element("testParent"));
 testParentNodePointer.setNode(testElement);
 }

 @Test
 public void testEmptyAttributeIterator() {
 given(testElement.getNamespace("xml")).willReturn(Namespace.XML_NAMESPACE);
 JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(testParentNodePointer, new QName("", "*"));
 assertTrue(attributeIterator.hasNext()); // check if hasNext() returns true for empty list
 assertFalse(attributeIterator.hasNext()); // check if hasNext() returns false after first call
 }

 @Test
 public void testAttributeIteratorWithSingleAttribute() {
 given(testElement.getNamespace("testPrefix")).willReturn(new Namespace("testPrefix", ""));
 testElement.addAttribute("testAttribute", "testValue", "testPrefix");
 JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(testParentNodePointer, new QName("testPrefix", "testAttribute"));
 Attribute expectedAttribute = testElement.getAttribute("testAttribute");
 NodePointer expectedNodePointer = new JDOMAttributePointer(testParentNodePointer, expectedAttribute);
 assertEquals(expectedNodePointer, attributeIterator.getNodePointer());
 }

 @Test
 public void testAttributeIteratorWithMultipleAttributes() {
 given(testElement.getNamespace("testPrefix")).willReturn(new Namespace("testPrefix", ""));
 testElement.addAttribute("testAttribute1", "testValue1", "testPrefix");
 testElement.addAttribute("testAttribute2", "testValue2", "testPrefix");
 JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(testParentNodePointer, new QName("testPrefix", "*"));
 NodePointer expectedNodePointer1 = new JDOMAttributePointer(testParentNodePointer, testElement.getAttribute("testAttribute1"));
 NodePointer expectedNodePointer2 = new JDOMAttributePointer(testParentNodePointer, testElement.getAttribute("testAttribute2"));
 List<NodePointer> expectedNodePointers = new ArrayList<>();
 expectedNodePointers.add(expectedNodePointer1);
 expectedNodePointers.add(expectedNodePointer2);
 List<NodePointer> nodePointers = new ArrayList<>();
 for (int i = 0; i < 2; i++) {
 NodePointer nodePointer = attributeIterator.getNodePointer();
 nodePointers.add(nodePointer);
 assertEquals(expectedNodePointers.get(i), nodePointer);
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAttributeIteratorWithNullName() {
 JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(testParentNodePointer, null);
 attributeIterator.getNodePointer();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeIteratorWithEmptyNamespace() {
 JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(testParentNodePointer, new QName("", "*"));
 }
}