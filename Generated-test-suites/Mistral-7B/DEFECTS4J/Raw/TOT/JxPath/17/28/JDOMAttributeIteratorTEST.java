import org.apache.commons.jxpath.ri.model.JDOM;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom. Namespace;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 @Test
 public void testJDOMAttributeIterator_withValidElementAndAttributeName_returnsAttribute() {
 JDOM jdom = new JDOM();
 String xml = "<root xmlns:x='http://test.com'><child x:attr='value'/></root>";
 Document doc = jdom.parse(xml);
 Element root = doc.getRootElement();
 NodePointer nodePointer = new NodePointer(root);
 QName name = new QName("x:attr");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, name);
 NodePointer attributePointer = iterator.next();
 Attribute expectedAttribute = root.getAttribute("attr");
 Attribute actualAttribute = (Attribute) attributePointer.getNode();
 assertEquals(expectedAttribute.getValue(), actualAttribute.getValue());
 }

 @Test
 public void testJDOMAttributeIterator_withValidElementAndWildcardAttributeName_returnsAllAttributes() {
 JDOM jdom = new JDOM();
 String xml = "<root xmlns:x='http://test.com'><child x:attr1='value1'/><child x:attr2='value2'/></root>";
 Document doc = jdom.parse(xml);
 Element root = doc.getRootElement();
 NodePointer nodePointer = new NodePointer(root);
 QName name = new QName("*");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, name);
 List<NodePointer> attributes = new ArrayList<>();
 while (iterator.hasNext()) {
 attributes.add(iterator.next());
 }
 assertEquals(2, attributes.size());
 }

 @Test
 public void testJDOMAttributeIterator_withNullElement_returnsEmptyIterator() {
 JDOM jdom = new JDOM();
 String xml = "<root></root>";
 Document doc = jdom.parse(xml);
 Element root = doc.getRootElement();
 NodePointer nodePointer = new NodePointer(root);
 QName name = new QName("x:attr");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, name);
 assertTrue(iterator.isEmpty());
 }

 @Test
 public void testJDOMAttributeIterator_withEmptyElement_returnsEmptyIterator() {
 JDOM jdom = new JDOM();
 String xml = "<root xmlns:x='http://test.com'/>";
 Document doc = jdom.parse(xml);
 Element root = doc.getRootElement();
 NodePointer nodePointer = new NodePointer(root);
 QName name = new QName("x:attr");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, name);
 assertTrue(iterator.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJDOMAttributeIterator_withNullParentNodePointer_throwsIllegalArgumentException() {
 NodePointer nodePointer = null;
 QName name = new QName("x:attr");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, name);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJDOMAttributeIterator_withNullQName_throwsIllegalArgumentException() {
 NodePointer nodePointer = new NodePointer(new Element("root"));
 QName name = null;
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, name);
 }

 @Test
 public void testJDOMAttributeIterator_withNullAttributeName_returnsEmptyIterator() {
 JDOM jdom = new JDOM();
 String xml = "<root xmlns:x='http://test.com'><child x:attr='value'/></root>";
 Document doc = jdom.parse(xml);
 Element root = doc.getRootElement();
 NodePointer nodePointer = new NodePointer(root);
 QName name = new QName(null);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, name);
 assertTrue(iterator.isEmpty());
 }

 @Test
 public void testJDOMAttributeIterator_withNonExistentAttributeName_returnsEmptyIterator() {
 JDOM jdom = new JDOM();
 String xml = "<root xmlns:x='http://test.com'><child x:attr='value'/></root>";
 Document doc = jdom.parse(xml);
 Element root = doc.getRootElement();
 NodePointer nodePointer = new NodePointer(root);
 QName name = new QName("x:nonExistentAttr");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, name);
 assertTrue(iterator.isEmpty());
 }

 @Test
 public void testJDOMAttributeIterator_withElementAndNamespaceprefix_returnsAttributeWithCorrectNamespace() {
 JDOM jdom = new JDOM();
 String xml = "<root xmlns:x='http://test.com' xmlns:ns='http://test2.com'><child x:attr='value'/></root>";
 Document doc = jdom.parse(xml);
 Element root = doc.getRootElement();
 NodePointer nodePointer = new NodePointer(root);
 QName name = new QName("{http://test.com}attr");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, name);
 NodePointer attributePointer = iterator.next();
 Attribute expectedAttribute = root.getAttribute("attr");
 Attribute actualAttribute = (Attribute) attributePointer.getNode();
 assertEquals(expectedAttribute.getValue(), actualAttribute.getValue());
 }

 @Test
 public void testJDOMAttributeIterator_withElementAndWildcardNamespaceprefix_returnsAllAttributesWithCorrectNamespaces() {
 JDOM jdom = new JDOM();
 String xml = "<root xmlns:x='http://test.com' xmlns:ns='http://test2.com'><child x:attr1='value1' xmlns:y='http://test3.com' x:attr2='value2'/></root>";
 Document doc = jdom.parse(xml);
 Element root = doc.getRootElement();
 NodePointer nodePointer = new NodePointer(root);
 QName name = new QName("*");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, name);
 List<NodePointer> attributes = new ArrayList<>();
 while (iterator.hasNext()) {
 attributes.add(iterator.next());
 }
 List<String> namespacePrefixes = new ArrayList<>();
 for (NodePointer attribute : attributes) {
 String namespaceURI = attribute.getNode().getNamespaceURI();
 String namespacePrefix = (namespaceURI != null) ? namespaceURI.substring(0, namespaceURI.lastIndexOf('/') + 1) : "";
 namespacePrefixes.add(namespacePrefix);
 }
 assertEquals(2, namespacePrefixes.size());
 assertEquals("x", namespacePrefixes.get(0));
 assertEquals("http://test3.com", namespacePrefixes.get(1));
 }
}