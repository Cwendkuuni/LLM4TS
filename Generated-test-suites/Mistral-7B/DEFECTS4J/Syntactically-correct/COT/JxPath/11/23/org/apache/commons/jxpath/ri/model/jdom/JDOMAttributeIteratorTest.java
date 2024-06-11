import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 private NodePointer parentNode;
 private JDOMAttributeIterator iterator;

 @Before
 public void setup() {
 Element element = new Element("root");
 Namespace xmlNS = Namespace.getXmlNamespace();
 Namespace ns = Namespace.getNamespace("test");
 element.addNamespaceDeclaration(xmlNS.getNamespaceURI(), xmlNS.getPrefix());
 element.addNamespaceDeclaration(ns.getNamespaceURI(), ns.getPrefix());

 Attribute attr1 = new Attribute("attr1", "value1", ns);
 Attribute attr2 = new Attribute("attr2", "value2", xmlNS);
 Attribute attr3 = new Attribute("attr3", "value3", ns);
 Attribute attr4 = new Attribute("attr4", "value4", xmlNS);
 element.addAttribute(attr1);
 element.addAttribute(attr2);
 element.addAttribute(attr3);
 element.addAttribute(attr4);

 parentNode = new NodePointer(element);
 }

 @Test
 public void testEmptyIterator() {
     JDOMAttributeIterator emptyIterator = new JDOMAttributeIterator(parentNode, new QName("", "*"));
     assertFalse(emptyIterator.hasNext());
 }

 @Test
 public void testIteratorWithOneAttribute() {
     JDOMAttributeIterator oneAttributeIterator = new JDOMAttributeIterator(parentNode, new QName("test:attr1"));
     assertTrue(oneAttributeIterator.hasNext());
     NodePointer next = oneAttributeIterator.next();
     assertEquals(next.getNode(), attr1);
     assertFalse(oneAttributeIterator.hasNext());
 }

 @Test
 public void testIteratorWithMultipleAttributes() {
     JDOMAttributeIterator multipleAttributesIterator = new JDOMAttributeIterator(parentNode, new QName("", "*"));
     assertTrue(multipleAttributesIterator.hasNext());
     NodePointer next1 = multipleAttributesIterator.next();
     assertEquals(next1.getNode(), attr1);
     assertTrue(multipleAttributesIterator.hasNext());
     NodePointer next2 = multipleAttributesIterator.next();
     assertEquals(next2.getNode(), attr2);
     assertTrue(multipleAttributesIterator.hasNext());
     NodePointer next3 = multipleAttributesIterator.next();
     assertEquals(next3.getNode(), attr3);
     assertTrue(multipleAttributesIterator.hasNext());
     NodePointer next4 = multipleAttributesIterator.next();
     assertEquals(next4.getNode(), attr4);
     assertFalse(multipleAttributesIterator.hasNext());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIteratorWithNegativePosition() {
     JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("test:attr1"));
     iterator.setPosition(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIteratorWithPositionGreaterThanSize() {
     JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("test:attr1"));
     iterator.setPosition(5);
 }

 @Test
 public void testIteratorWithNullParentNode() {
     JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, new QName("test:attr1"));
     assertFalse(iterator.hasNext());
 }
}