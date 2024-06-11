import org.junit.Assert;
import org.junit.Test;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;

public class JDOMAttributeIteratorTest {

 private NodePointer parentNode;
 private QName attributeName;
 private NodeIterator attributeIterator;

 @Test
 public void testJDOMAttributeIterator_ValidName() {
 parentNode = new JDOMPointer(createRootElement("root", "ns1:prefix1", "http://test.com/ns1"));
 attributeName = new QName("ns1:prefix1:attributeName");
 attributeIterator = new JDOMAttributeIterator(parentNode, attributeName);

 Attribute expectedAttribute = createAttribute("attributeName", "attributeValue");
 Attribute actualAttribute = (Attribute) attributeIterator.getNodePointer().getNode();
 Assert.assertNotNull(actualAttribute);
 Assert.assertEquals(expectedAttribute.getText(), actualAttribute.getText());
 }

 @Test
 public void testJDOMAttributeIterator_EmptyList() {
 parentNode = new JDOMPointer(createRootElement("root", null, null));
 attributeName = new QName("*");
 attributeIterator = new JDOMAttributeIterator(parentNode, attributeName);
 Assert.assertFalse(attributeIterator.hasNext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJDOMAttributeIterator_NullParent() {
 attributeName = new QName("*");
 AttributeIterator iterator = null;
 try {
 iterator = new JDOMAttributeIterator(null, attributeName);
 }
 catch (IllegalArgumentException e) {
 // expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJDOMAttributeIterator_NullName() {
 parentNode = new JDOMPointer(createRootElement("root", null, null));
 attributeIterator = new JDOMAttributeIterator(parentNode, null);
 Assert.assertFalse(attributeIterator.hasNext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJDOMAttributeIterator_EmptyName() {
 parentNode = new JDOMPointer(createRootElement("root", null, null));
 attributeName = new QName("");
 attributeIterator = new JDOMAttributeIterator(parentNode, attributeName);
 Assert.assertFalse(attributeIterator.hasNext());
 }

 @Test
 public void testJDOMAttributeIterator_WildcardName() {
 parentNode = new JDOMPointer(createRootElement("root", null, null));
 attributeName = new QName("*");
 attributeIterator = new JDOMAttributeIterator(parentNode, attributeName);
 while (attributeIterator.hasNext()) {
 Attribute expectedAttribute = (Attribute) attributeIterator.getNodePointer().getNode();
 Attribute actualAttribute = (Attribute) attributeIterator.getNext().getNode();
 Assert.assertEquals(expectedAttribute.getText(), actualAttribute.getText());
 }
 }

 @Test
 public void testJDOMAttributeIterator_NonExistentPosition() {
 parentNode = new JDOMPointer(createRootElement("root", null, null));
 attributeName = new QName("*");
 attributeIterator = new JDOMAttributeIterator(parentNode, attributeName);
 Attribute expectedAttribute = new Attribute("nonExistentAttribute", "nonExistentValue");
 Assert.assertFalse(attributeIterator.setPosition(1));
 Assert.assertNull(attributeIterator.getNodePointer().getNode());
 }

 @Test
 public void testJDOMAttributeIterator_InvalidPosition() {
 parentNode = new JDOMPointer(createRootElement("root", null, null));
 attributeName = new QName("*");
 attributeIterator = new JDOMAttributeIterator(parentNode, attributeName);
 Attribute expectedAttribute = (Attribute) attributeIterator.getNodePointer().getNode();
 attributeIterator.setPosition(0);

 int position = attributeIterator.getPosition();
 Assert.assertEquals(0, position);

 Attribute actualAttribute = (Attribute) attributeIterator.getNodePointer().getNode();
 Assert.assertEquals(expectedAttribute.getText(), actualAttribute.getText());

 attributeIterator.setPosition(attributeIterator.getSize() + 1);
 Assert.assertNull(attributeIterator.getNodePointer().getNode());
 }

 @Test
 public void testJDOMAttributeIterator_ValidPosition() {
 parentNode = new JDOMPointer(createRootElement("root", null, null));
 attributeName = new QName("*");
 attributeIterator = new JDOMAttributeIterator(parentNode, attributeName);
 Attribute expectedAttribute1 = (Attribute) attributeIterator.getNodePointer().getNode();
 attributeIterator.setPosition(1);
 Attribute expectedAttribute2 = (Attribute) attributeIterator.getNodePointer().getNode();
 Attribute actualAttribute1 = (Attribute) attributeIterator.getNodePointer().getNode();
 Attribute actualAttribute2 = (Attribute) attributeIterator.getNodePointer().getNode();

 int position1 = attributeIterator.getPosition();
 Assert.assertEquals(1, position1);
 Assert.assertNotEquals(expectedAttribute1, expectedAttribute2);
 Assert.assertEquals(expectedAttribute1.getText(), actualAttribute1.getText());
 Assert.assertEquals(expectedAttribute2.getText(), actualAttribute2.getText());
 }

 private Element createRootElement(String name, String prefix, String uri) {
 Element element = new Element(name);
 if (prefix != null && uri != null) {
 element.setNamespacePrefix(prefix);
 element.setNamespaceURI(uri);
 }
 return element;
 }

 private Attribute createAttribute(String name, String text) {
 Attribute attribute = new Attribute(name);
 attribute.setText(text);
 return attribute;
 }
}