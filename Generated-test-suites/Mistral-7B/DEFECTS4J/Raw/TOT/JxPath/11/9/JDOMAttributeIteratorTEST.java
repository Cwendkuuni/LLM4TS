import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class JDOMAttributeIteratorTest {

 private NodePointer mockParentNode;
 private Element mockElement;
 private QName mockQName;
 private JDOMAttributeIterator iterator;

 @Before
 public void setup() {
 mockParentNode = new NodePointer(new Object(), new QName("root"));
 mockElement = new Element("root");
 mockQName = new QName("xml", "test:attribute");
 }

 @Test
 public void testEmptyIterator() {
 iterator = new JDOMAttributeIterator(mockParentNode, mockQName);
 assertSame(Collections.EMPTY_LIST, iterator.attributes);
 }

 @Test
 public void testSingleAttributeIterator() {
 mockElement.addAttribute("single", "singleValue");
 iterator = new JDOMAttributeIterator(mockParentNode, mockQName);
 iterator.attributes = new ArrayList<Attribute>() {{
 add(new Attribute("single", "singleValue", "xml"));
 }};

 Attribute attribute = (Attribute) iterator.getNodePointer().getNode();
 assertSame("single", attribute.getName());
 assertSame("singleValue", attribute.getValue());
 }

 @Test
 public void testMultipleAttributesIterator() {
 mockElement.addAttribute("attribute1", "value1");
 mockElement.addAttribute("attribute2", "value2", "xml:namespace");
 mockElement.addAttribute("attribute3", "value3");
 iterator = new JDOMAttributeIterator(mockParentNode, mockQName);
 iterator.attributes = new ArrayList<Attribute>() {{
 add(new Attribute("attribute1", "value1", null));
 add(new Attribute("attribute2", "value2", new Namespace("xml", "xml")));
 add(new Attribute("attribute3", "value3", null));
 }};

 Attribute attribute1 = (Attribute) iterator.getNodePointer().getNode();
 assertSame("attribute1", attribute1.getName());
 assertNull(attribute1.getNamespace());

 Attribute attribute2 = (Attribute) iterator.getNodePointer().getNode();
 assertSame("attribute2", attribute2.getName());
 assertSame(new Namespace("xml", "xml"), attribute2.getNamespace());
 }

 @Test
 public void testOutOfBoundsIterator() {
 iterator = new JDOMAttributeIterator(mockParentNode, mockQName);
 iterator.attributes = new ArrayList<Attribute>() {{
 add(new Attribute("single", "singleValue", "xml"));
 }};

 iterator.setPosition(2);
 assertFalse(iterator.hasNext());
 assertNull(iterator.next());
 }

 @Test
 public void testSetPositionToFirst() {
 iterator = new JDOMAttributeIterator(mockParentNode, mockQName);
 iterator.attributes = new ArrayList<Attribute>() {{
 add(new Attribute("single", "singleValue", "xml"));
 add(new Attribute("multiple", "multipleValue", null));
 }};

 iterator.setPosition(0);
 assertSame(iterator.getNodePointer(), iterator.next());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionToNegative() {
 iterator = new JDOMAttributeIterator(mockParentNode, mockQName);
 iterator.attributes = new ArrayList<Attribute>() {{
 add(new Attribute("single", "singleValue", "xml"));
 add(new Attribute("multiple", "multipleValue", null));
 }};

 iterator.setPosition(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionToLastPlusOne() {
 iterator = new JDOMAttributeIterator(mockParentNode, mockQName);
 iterator.attributes = new ArrayList<Attribute>() {{
 add(new Attribute("single", "singleValue", "xml"));
 add(new Attribute("multiple", "multipleValue", null));
 }};

 iterator.setPosition(3);
 }
}