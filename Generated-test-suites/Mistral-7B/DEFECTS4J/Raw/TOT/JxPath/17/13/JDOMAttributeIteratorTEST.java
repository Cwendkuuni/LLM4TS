import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.JDOMException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JDOMAttributeIteratorTest {

 @Test
 public void testJDOMAttributeIterator_EmptyElement() throws Exception {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(new Element("")), null);
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testJDOMAttributeIterator_SingleAttribute() throws Exception {
 Element element = new Element("root", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
 element.addAttribute(new Attribute("id", "test"));
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(element), null);
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 assertEquals("id", next.getNodeName().getName());
 assertEquals("test", next.getNodeName().getValue());
 assertNull(next.getNodeValue());
 }

 @Test
 public void testJDOMAttributeIterator_MultipleAttributes() throws Exception {
 Element element = new Element("root", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
 element.addAttribute(new Attribute("id", "test1"));
 element.addAttribute(new Attribute("class", "test2"));
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(element), null);
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 assertEquals("id", next.getNodeName().getName());
 assertEquals("test1", next.getNodeName().getValue());
 assertNull(next.getNodeValue());

 next = iterator.next();
 assertEquals("class", next.getNodeName().getName());
 assertEquals("test2", next.getNodeName().getValue());
 assertNull(next.getNodeValue());

 assertFalse(iterator.hasNext());
 }

 @Test
 public void testJDOMAttributeIterator_EmptyList() throws Exception {
 Element element = new Element("root", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(element), new QName("*", "*"));
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testJDOMAttributeIterator_EmptyListWithNamespace() throws Exception {
 Element element = new Element("root", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(element), new QName("xsi", "*"));
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test(expected = JDOMException.class)
 public void testJDOMAttributeIterator_NullParent() throws Exception {
 new JDOMAttributeIterator(null, null);
 }

 @Test(expected = JDOMException.class)
 public void testJDOMAttributeIterator_NullElement() throws Exception {
 new JDOMAttributeIterator(new JDOMPointer(null), null);
 }
}