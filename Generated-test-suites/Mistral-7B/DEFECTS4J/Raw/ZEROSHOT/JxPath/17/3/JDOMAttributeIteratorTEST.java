import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.JDOMException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JDOMAttributeIteratorTest {

 @Test
 public void testEmptyAttributeIterator() throws JDOMException {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(new Element("root")), new QName("", ""));
 assertEquals(Collections.EMPTY_LIST, iterator.getAttributeNames());
 assertNull(iterator.getNodePointer());
 }

 @Test
 public void testSingleAttributeIterator() throws JDOMException {
 Element root = new Element("root");
 root.addAttribute("attribute1", "value1");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(root), new QName("", "attribute1"));
 List<String> attributeNames = new ArrayList<>();
 attributeNames.add("attribute1");
 assertEquals(attributeNames, iterator.getAttributeNames());
 NodePointer attributePointer = iterator.getNodePointer();
 assertNotNull(attributePointer);
 assertEquals("attribute1", attributePointer.getName().toString());
 assertEquals("value1", attributePointer.getValue().getValue());
 }

 @Test
 public void testMultipleAttributesIterator() throws JDOMException {
 Element root = new Element("root");
 root.addAttribute("attribute1", "value1");
 root.addAttribute("attribute2", "value2");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(root), new QName("", ""));
 List<String> attributeNames = new ArrayList<>();
 attributeNames.add("attribute1");
 attributeNames.add("attribute2");
 assertEquals(attributeNames, iterator.getAttributeNames());
 NodePointer attributePointer1 = iterator.getNodePointer();
 assertEquals("attribute1", attributePointer1.getName().toString());
 assertEquals("value1", attributePointer1.getValue().getValue());
 NodePointer attributePointer2 = iterator.getNodePointer();
 assertEquals("attribute2", attributePointer2.getName().toString());
 assertEquals("value2", attributePointer2.getValue().getValue());
 }

 @Test
 public void testMultipleAttributesIteratorWithNameSpace() throws JDOMException {
 Element root = new Element("root");
 root.addNamespaceDeclaration("ns", "namespace-uri");
 root.addAttribute("attribute1", "value1", "ns:attr-name");
 root.addAttribute("attribute2", "value2", "ns:attr-name");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(root), new QName("ns:attr-name"));
 List<String> attributeNames = new ArrayList<>();
 attributeNames.add("attribute1");
 attributeNames.add("attribute2");
 assertEquals(attributeNames, iterator.getAttributeNames());
 NodePointer attributePointer1 = iterator.getNodePointer();
 assertEquals("attribute1", attributePointer1.getName().toString());
 assertEquals("value1", attributePointer1.getValue().getValue());
 NodePointer attributePointer2 = iterator.getNodePointer();
 assertEquals("attribute2", attributePointer2.getName().toString());
 assertEquals("value2", attributePointer2.getValue().getValue());
 }

 @Test
 public void testInvalidAttributeNameIterator() throws JDOMException {
 Element root = new Element("root");
 root.addAttribute("attribute1", "value1");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(root), new QName("", "attribute2"));
 List<String> attributeNames = new ArrayList<>();
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testInvalidAttributeNameIteratorWithNameSpace() throws JDOMException {
 Element root = new Element("root");
 root.addNamespaceDeclaration("ns", "namespace-uri");
 root.addAttribute("attribute1", "value1", "ns:attr-name");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(root), new QName("ns:attribute2"));
 List<String> attributeNames = new ArrayList<>();
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testOutOfBoundsAttributeIterator() throws JDOMException {
 Element root = new Element("root");
 root.addAttribute("attribute1", "value1");
 root.addAttribute("attribute2", "value2");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(root), new QName("", ""));
 iterator.next();
 assertFalse(iterator.hasPrevious());
 }

 @Test
 public void testOutOfBoundsAttributeIteratorWithNameSpace() throws JDOMException {
 Element root = new Element("root");
 root.addNamespaceDeclaration("ns", "namespace-uri");
 root.addAttribute("attribute1", "value1", "ns:attr-name");
 root.addAttribute("attribute2", "value2", "ns:attr-name");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(root), new QName("ns:attr-name"));
 iterator.next();
 assertFalse(iterator.hasPrevious());
 }
}