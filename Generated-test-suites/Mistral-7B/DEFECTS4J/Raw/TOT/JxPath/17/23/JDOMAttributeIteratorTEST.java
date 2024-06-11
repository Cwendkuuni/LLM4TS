import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 private NodePointer rootNodePointer;
 private NodeIterator iterator;

 @Before
 public void setUp() {
 Element rootElement = new Element("root");
 rootElement.setAttribute("attr1", "value1");
 rootElement.setAttribute("attr2", "value2");
 rootElement.setAttribute("xml:namespace", "http://example.com/namespace");
 Element childElement = new Element("child");
 childElement.setAttribute("attr3", "value3");
 childElement.setAttribute("attr4", "value4");
 rootElement.addContent(childElement);
 rootNodePointer = new NodePointer(rootElement);
 }

 @Test
 public void testIterateThroughAttributes() {
 iterator = new JDOMAttributeIterator(rootNodePointer, null);
 assertNotNull(iterator);
 List<Attribute> expectedAttributes = new ArrayList<>();
 expectedAttributes.add(rootElement.getAttribute("attr1"));
 expectedAttributes.add(rootElement.getAttribute("attr2"));
 expectedAttributes.addAll(childElement.getAttributes());
 for (int i = 0; i < 5; i++) {
 Attribute attribute = (Attribute) iterator.getNodePointer().getNode();
 assertEquals(expectedAttributes.get(i).getName(), attribute.getName());
 assertEquals(expectedAttributes.get(i).getValue(), attribute.getValue());
 iterator.next();
 }
 }

 @Test
 public void testIterateThroughEmptyList() {
 iterator = new JDOMAttributeIterator(new NodePointer(new Element("empty")), null);
 assertTrue(iterator.next() == null);
 }

 @Test(expected = NoSuchElementException.class)
 public void testIteratePastEndOfList() {
 iterator = new JDOMAttributeIterator(rootNodePointer, null);
 for (int i = 0; i < iterator.size(); i++) {
 iterator.next();
 }
 iterator.next();
 }

 @Test
 public void testIterateThroughAttributesWithPrefix() {
 iterator = new JDOMAttributeIterator(rootNodePointer, new QName("xmlns", "http://example.com/namespace"));
 List<Attribute> expectedAttributes = new ArrayList<>();
 expectedAttributes.add(rootElement.getAttribute("xml:namespace"));
 for (int i = 0; i < childElement.getAttributes().size(); i++) {
 Attribute attribute = (Attribute) childElement.getAttributes().get(i);
 if (attribute.getNamespace().equals(Namespace.XML_NAMESPACE)) {
 expectedAttributes.add(attribute);
 }
 }
 for (int i = 0; i < 5; i++) {
 Attribute attribute = (Attribute) iterator.getNodePointer().getNode();
 assertEquals(expectedAttributes.get(i).getName(), attribute.getName());
 assertEquals(expectedAttributes.get(i).getValue(), attribute.getValue());
 iterator.next();
 }
 }
}