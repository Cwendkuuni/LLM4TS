import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.JDOMException;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 private NodePointer root;
 private NodeIterator iterator;

 @Before
 public void setUp() throws JDOMException {
 String xml = "<root xmlns:x=\"http://test.com\">\n" +
 "  <element x:attr1=\"value1\">Content 1</element>\n" +
 "  <element x:attr2=\"value2\">Content 2</element>\n" +
 "  <element attr3=\"value3\">Content 3</element>\n" +
 "</root>";
 Document doc = Document.parse(new StringReader(xml));
 root = new NodePointer(doc.getRootElement());
 }

 @Test
 public void testEmptyAttributeIterator() {
 iterator = new JDOMAttributeIterator(root, null);
 assertEquals(0, iterator.getPosition());
 assertNull(iterator.getNodePointer());
 }

 @Test
 public void testAttributeIteratorWithWildcardName() {
 iterator = new JDOMAttributeIterator(root, null);
 assertEquals(3, iterator.getPosition());
 NodePointer attrPointer = iterator.getNodePointer();
 assertEquals("attr3", attrPointer.getName().getLocalPart());
 assertEquals("value3", attrPointer.getValue());
 assertNull(iterator.getNodePointer());
 assertEquals(3, iterator.getPosition());
 }

 @Test
 public void testAttributeIteratorWithSpecificName() {
 iterator = new JDOMAttributeIterator(root, new QName("x:attr1"));
 assertEquals(1, iterator.getPosition());
 NodePointer attrPointer = iterator.getNodePointer();
 assertEquals("attr1", attrPointer.getName().getLocalPart());
 assertEquals("value1", attrPointer.getValue());
 assertNull(iterator.getNodePointer());
 assertEquals(1, iterator.getPosition());
 }

 @Test
 public void testAttributeIteratorWithSpecificNameAndNamespace() {
 Namespace namespace = new Namespace("x", "http://test.com");
 iterator = new JDOMAttributeIterator(root, new QName(namespace, "attr1"));
 assertEquals(1, iterator.getPosition());
 NodePointer attrPointer = iterator.getNodePointer();
 assertEquals("attr1", attrPointer.getName().getLocalPart());
 assertEquals("value1", attrPointer.getValue());
 assertNull(iterator.getNodePointer());
 assertEquals(1, iterator.getPosition());
 }

 @Test
 public void testAttributeIteratorWithNullName() {
 iterator = new JDOMAttributeIterator(root, null);
 assertEquals(3, iterator.getPosition());
 NodePointer attrPointer = iterator.getNodePointer();
 assertNull(attrPointer);
 assertEquals(3, iterator.getPosition());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeIteratorWithInvalidName() {
 iterator = new JDOMAttributeIterator(root, new QName("", ""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeIteratorWithInvalidNameAndNamespace() {
 Namespace namespace = new Namespace("", "");
 iterator = new JDOMAttributeIterator(root, new QName(namespace, ""));
 }
}