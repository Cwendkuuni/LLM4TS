import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.jdom.ElementTest;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JDOMAttributeIteratorTest {

 private NodePointer parent;
 private JDOMAttributeIterator iterator;
 private Element element;

 @Before
 public void setUp() {
 parent = new NodePointer(new ElementTest(), new QName(""));
 element = new ElementTest("test");
 }

 @Test
 public void testJDOMAttributeIteratorConstructorNull() {
 iterator = new JDOMAttributeIterator(null, new QName(""));
 assertEquals(Collections.EMPTY_LIST, iterator.getAttributes());
 }

 @Test
 public void testJDOMAttributeIteratorConstructorEmpty() {
 iterator = new JDOMAttributeIterator(parent, new QName(""));
 assertEquals(Collections.EMPTY_LIST, iterator.getAttributes());
 }

 @Test
 public void testJDOMAttributeIteratorConstructorOneAttribute() {
 Attribute attribute = new Attribute("test", "testValue");
 iterator = new JDOMAttributeIterator(parent, new QName("test"));
 iterator.setPosition(1);
 assertEquals(attribute, iterator.getNodePointer().getNode());
 }

 @Test
 public void testJDOMAttributeIteratorConstructorMultipleAttributes() {
 Attribute attribute1 = new Attribute("test1", "testValue1");
 Attribute attribute2 = new Attribute("test2", "testValue2");
 element.addContent(attribute1);
 element.addContent(attribute2);
 iterator = new JDOMAttributeIterator(parent, new QName("test"));
 iterator.setPosition(2);
 assertEquals(attribute2, iterator.getNodePointer().getNode());
 }

 @Test
 public void testJDOMAttributeIteratorConstructorMultipleAttributesDifferentNS() {
 Attribute attribute1 = new Attribute("test1:test1", "testValue1");
 Attribute attribute2 = new Attribute("test2:test2", "testValue2");
 element.addNamespaceDeclaration("test1", "http://test1.com");
 element.addNamespaceDeclaration("test2", "http://test2.com");
 element.addContent(attribute1);
 element.addContent(attribute2);
 iterator = new JDOMAttributeIterator(parent, new QName("test1:test1"));
 iterator.setPosition(1);
 assertEquals(attribute1, iterator.getNodePointer().getNode());
 }

 @Test
 public void testJDOMAttributeIteratorConstructorEmptyName() {
 iterator = new JDOMAttributeIterator(parent, new QName(""));
 assertEquals(Collections.EMPTY_LIST, iterator.getAttributes());
 }

 @Test
 public void testJDOMAttributeIteratorConstructorWildcardName() {
 iterator = new JDOMAttributeIterator(parent, new QName("*"));
 List attributes = iterator.getAttributes();
 assertEquals(2, attributes.size());
 Attribute firstAttribute = (Attribute) attributes.get(0);
 Attribute secondAttribute = (Attribute) attributes.get(1);
 assertTrue(firstAttribute.getName().equals(""));
 assertTrue(secondAttribute.getName().equals(""));
 }

 @Test
 public void testJDOMAttributeIteratorGetNodePointerNullIterator() {
 iterator = new JDOMAttributeIterator(parent, new QName(""));
 assertNull(iterator.getNodePointer());
 }

 @Test
 public void testJDOMAttributeIteratorGetNodePointerInvalidPosition() {
 iterator = new JDOMAttributeIterator(parent, new QName("test"));
 iterator.setPosition(10);
 assertNull(iterator.getNodePointer());
 }

 @Test
 public void testJDOMAttributeIteratorGetNodePointerValidPosition() {
 iterator = new JDOMAttributeIterator(parent, new QName("test"));
 iterator.setPosition(1);
 assertTrue(iterator.getNodePointer() instanceof JDOMAttributePointer);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testJDOMAttributeIteratorGetNodePointerInvalidPositionException() {
 iterator = new JDOMAttributeIterator(parent, new QName("test"));
 iterator.setPosition(-1);
 }

 @Test
 public void testJDOMAttributeIteratorGetPositionNullIterator() {
 iterator = new JDOMAttributeIterator(parent, new QName(""));
 assertEquals(0, iterator.getPosition());
 }

 @Test
 public void testJDOMAttributeIteratorSetPositionNullIterator() {
 iterator = new JDOMAttributeIterator(parent, new QName(""));
 assertFalse(iterator.setPosition(1));
 }

 @Test
 public void testJDOMAttributeIteratorSetPositionValidPosition() {
 iterator = new JDOMAttributeIterator(parent, new QName("test"));
 assertTrue(iterator.setPosition(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testJDOMAttributeIteratorSetPositionInvalidPositionException() {
 iterator = new JDOMAttributeIterator(parent, new QName("test"));
 assertFalse(iterator.setPosition(3));
 }
}