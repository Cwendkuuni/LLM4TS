import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

 private NodePointer nodePointer;
 private JDOMAttributeIterator iterator;

 @Before
 public void setup() {
 String xml = "<root xmlns:x='http://test.com'>" +
 "  <element x:attr1='value1'/>" +
 "  <element x:attr2='value2'/>" +
 "</root>";
 JDomJXPathContext context = new JDomJXPathContext(new StringReader(xml));
 nodePointer = context.getDocument().getRootElement().createChild("newElement");
 nodePointer.getDocument().setRootElement(nodePointer.getDocument().getRootElement().detach());
 nodePointer.getDocument().addContent(new Element("root").addNamespace("x", "http://test.com"));

 Element root = nodePointer.getDocument().getRootElement();
 root.addContent(new Element("element1").addAttribute("attr1", "value1"));
 root.addContent(new Element("element2").addAttribute("attr2", "value2"));
 }

 @Test
 public void testEmptyAttributeIterator() {
 iterator = new JDOMAttributeIterator(nodePointer, null);
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testAttributeIteratorWithOneAttribute() {
 iterator = new JDOMAttributeIterator(nodePointer.createChild("element"), new QName("attr1"));
 Attribute attribute = (Attribute) iterator.next().getNode();
 Assert.assertEquals("attr1", attribute.getName());
 Assert.assertEquals("value1", attribute.getValue());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testAttributeIteratorWithMultipleAttributes() {
 iterator = new JDOMAttributeIterator(nodePointer.createChild("element"), new QName("*"));
 Attribute attribute1 = (Attribute) iterator.next().getNode();
 Attribute attribute2 = (Attribute) iterator.next().getNode();
 Assert.assertEquals("attr1", attribute1.getName());
 Assert.assertEquals("value1", attribute1.getValue());
 Assert.assertEquals("attr2", attribute2.getName());
 Assert.assertEquals("value2", attribute2.getValue());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test(expected = IllegalStateException.class)
 public void testAttributeIteratorCalledAfterEndOfIteration() {
 iterator = new JDOMAttributeIterator(nodePointer.createChild("element"), new QName("*"));
 iterator.next();
 iterator.next();
 iterator.next();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeIteratorWithInvalidNamespace() {
 iterator = new JDOMAttributeIterator(nodePointer.createChild("element"), new QName("xmlns", "http://test.com"));
 }

}