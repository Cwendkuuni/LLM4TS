import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

 private NodePointer parentNode;
 private DOMAttributeIterator attributeIterator;

 @Before
 public void setUp() {
 // Replace this with the actual initialization of parentNode.
 parentNode = new NodePointer(null, null, null, 0, 0, null);
 }

 @After
 public void tearDown() {
 // Replace this with the actual cleanup code.
 }

 @Test
 public void testConstructor() {
 QName qname = new QName("test", "testLocalName", "testNamespaceURI");
 attributeIterator = new DOMAttributeIterator(parentNode, qname);
 assertNotNull(attributeIterator);
 }

 @Test
 public void testConstructorWithWildcard() {
 QName qname = new QName("*", null, null);
 attributeIterator = new DOMAttributeIterator(parentNode, qname);
 assertNotNull(attributeIterator);
 }

 @Test
 public void testGetNodePointer_ValidPosition() {
 QName qname = new QName("test", "testLocalName", "testNamespaceURI");
 parentNode.setNode(new Element("testElement"));
 Attr attr1 = new Attr("test1", "testValue1");
 Attr attr2 = new Attr("test2", "testValue2");
 Node attrNode1 = parentNode.getNode().setAttributeNode(attr1);
 Node attrNode2 = parentNode.getNode().setAttributeNode(attr2);
 parentNode.getNode().setNodeName("testParentElement");
 attributeIterator = new DOMAttributeIterator(parentNode, qname);
 assertEquals(attr1, attributeIterator.getNodePointer().getNode());
 }

 @Test
 public void testGetNodePointer_InvalidPosition() {
 QName qname = new QName("test", "testLocalName", "testNamespaceURI");
 parentNode.setNode(new Element("testElement"));
 Attr attr1 = new Attr("test1", "testValue1");
 Attr attr2 = new Attr("test2", "testValue2");
 Node attrNode1 = parentNode.getNode().setAttributeNode(attr1);
 Node attrNode2 = parentNode.getNode().setAttributeNode(attr2);
 parentNode.getNode().setNodeName("testParentElement");
 attributeIterator = new DOMAttributeIterator(parentNode, qname);
 attributeIterator.setPosition(2);
 assertNull(attributeIterator.getNodePointer().getNode());
 }

 @Test
 public void testGetPosition_ValidPosition() {
 QName qname = new QName("test", "testLocalName", "testNamespaceURI");
 parentNode.setNode(new Element("testElement"));
 Attr attr1 = new Attr("test1", "testValue1");
 Attr attr2 = new Attr("test2", "testValue2");
 Node attrNode1 = parentNode.getNode().setAttributeNode(attr1);
 Node attrNode2 = parentNode.getNode().setAttributeNode(attr2);
 parentNode.getNode().setNodeName("testParentElement");
 attributeIterator = new DOMAttributeIterator(parentNode, qname);
 attributeIterator.next();
 attributeIterator.next();
 assertEquals(2, attributeIterator.getPosition());
 }

 @Test
 public void testGetPosition_InvalidPosition() {
 QName qname = new QName("test", "testLocalName", "testNamespaceURI");
 parentNode.setNode(new Element("testElement"));
 Attr attr1 = new Attr("test1", "testValue1");
 Attr attr2 = new Attr("test2", "testValue2");
 Node attrNode1 = parentNode.getNode().setAttributeNode(attr1);
 Node attrNode2 = parentNode.getNode().setAttributeNode(attr2);
 parentNode.getNode().setNodeName("testParentElement");
 attributeIterator = new DOMAttributeIterator(parentNode, qname);
 assertEquals(0, attributeIterator.getPosition());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetNodePointer_ClosedIterator() {
 QName qname = new QName("test", "testLocalName", "testNamespaceURI");
 parentNode.setNode(new Element("testElement"));
 Attr attr1 = new Attr("test1", "testValue1");
 Attr attr2 = new Attr("test2", "testValue2");
 Node attrNode1 = parentNode.getNode().setAttributeNode(attr1);
 Node attrNode2 = parentNode.getNode().setAttributeNode(attr2);
 parentNode.getNode().setNodeName("testParentElement");
 attributeIterator = new DOMAttributeIterator(parentNode, qname);
 attributeIterator.next();
 attributeIterator.close();
 assertEquals(null, attributeIterator.getNodePointer().getNode());
 }

 @Test
 public void testTestAttr_ValidAttribute() {
 QName name = new QName("testFS", "", "testNS");
 Attr attr = new Attr("testLocalName", "testValue");
 assertTrue(DOMAttributeIterator.testAttr(attr, name));
 }

 @Test
 public void testTestAttr_InvalidAttribute() {
 QName name = new QName("testFS", "", "testNS");
 Attr attr = new Attr("testLocalName", "testValue");
 assertFalse(DOMAttributeIterator.testAttr(new Attr("xmlns:test", "testValue"), name));
 }

 @Test
 public void testTestAttr_NullName() {
 Attr attr = new Attr("testLocalName", "testValue");
 assertFalse(DOMAttributeIterator.testAttr(attr, null));
 }

 @Test
 public void testTestAttr_NullAttr() {
 QName name = new QName("testFS", "", "testNS");
 assertFalse(DOMAttributeIterator.testAttr(null, name));
 }

 @Test(expected = NullPointerException.class)
 public void testGetAttribute_NullElement() {
 Element element = null;
 QName qname = new QName("test", "testLocalName", "testNamespaceURI");
 attributeIterator = new DOMAttributeIterator(new NodePointer(element, null, null, 0, 0, null), qname);
 assertEquals(null, attributeIterator.getNodePointer().getNode());
 }

 @Test(expected = NullPointerException.class)
 public void testGetAttribute_NullName() {
 Element element = new Element("testElement");
 QName qname = null;
 attributeIterator = new DOMAttributeIterator(new NodePointer(element, null, null, 0, 0, null), qname);
 assertEquals(null, attributeIterator.getNodePointer().getNode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAttribute_InvalidPrefix() {
 Element element = new Element("testElement");
 QName qname = new QName("test:test", null, null);
 attributeIterator = new DOMAttributeIterator(new NodePointer(element, null, null, 0, 0, null), qname);
 assertEquals(null, attributeIterator.getNodePointer().getNode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAttribute_EmptyLocalName() {
 Element element = new Element("testElement");
 QName qname = new QName("", null, null);
 attributeIterator = new DOMAttributeIterator(new NodePointer(element, null, null, 0, 0, null), qname);
 assertEquals(null, attributeIterator.getNodePointer().getNode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAttribute_NullPrefixAndEmptyLocalName() {
 Element element = new Element("testElement");
 QName qname = new QName(null, "", null);
 attributeIterator = new DOMAttributeIterator(new NodePointer(element, null, null, 0, 0, null), qname);
 assertEquals(null, attributeIterator.getNodePointer().getNode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAttribute_NullElementAndNullName() {
 QName qname = new QName("test", "testLocalName", "testNamespaceURI");
 attributeIterator = new DOMAttributeIterator(null, qname);
 assertEquals(null, attributeIterator.getNodePointer().getNode());
 }

 @Test
 public void testGetAttribute_ValidAttributeName() {
 Element element = new Element("testElement");
 QName qname = new QName("test", "testLocalName", "testNamespaceURI");
 Attr attribute = new Attr("testLocalName", "testValue");
 element.setAttributeNode(attribute);
 attributeIterator = new DOMAttributeIterator(new NodePointer(element, null, null, 0, 0, null), qname);
 assertEquals(attribute, attributeIterator.getNodePointer().getNode());
 }

 @Test
 public void testGetAttribute_AttributeNotFound() {
 Element element = new Element("testElement");
 QName qname = new QName("test", "testLocalName", "testNamespaceURI");
 attributeIterator = new DOMAttributeIterator(new NodePointer(element, null, null, 0, 0, null), qname);
 assertEquals(null, attributeIterator.getNodePointer().getNode());
 }

 @Test(expected = NullPointerException.class)
 public void testGetAttribute_NullElementAndAttribute() {
 Element element = null;
 Attr attr = new Attr("testLocalName", "testValue");
 attributeIterator = new DOMAttributeIterator(new NodePointer(element, null, null, 0, 0, null), attr);
 assertEquals(null, attributeIterator.getNodePointer().getNode());
 }

 @Test
 public void testGetAttribute_ValidAttribute() {
 Element element = new Element("testElement");
 QName qname = new QName("test", "testLocalName", "testNamespaceURI");
 Attr attribute = new Attr("testLocalName", "testValue");
 element.setAttributeNode(attribute);
 attributeIterator = new DOMAttributeIterator(new NodePointer(element, null, null, 0, 0, null), qname);
 assertEquals(attribute, attributeIterator.getNodePointer().getNode());
 }

}