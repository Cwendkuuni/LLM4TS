package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

 private NodePointer parentNode;
 private JDOMAttributeIterator attributeIterator;

 @Before
 public void setUp() {
 JDOMContext jdomContext = new JDOMContext();
 Element root = new Element("root");
 root.addNamespaceDeclaration("xml", "http://www.w3.org/XML/1998/namespace");
 root.addNamespaceDeclaration("test", "http://test.com/namespace");
 root.addAttribute("id", "1", "test");
 root.addAttribute("class", "test-class");
 root.addAttribute("xml:version", "1.0");
 root.addAttribute("xml:lang", "en");

 Element element = new Element("element");
 element.addNamespaceDeclaration("xml", "http://www.w3.org/XML/1998/namespace");
 element.addNamespaceDeclaration("test", "http://test.com/namespace");
 element.addAttribute("name1", "value1", "test");
 element.addAttribute("name2", "value2", null);
 element.addAttribute("name3", "value3", "test");
 element.addAttribute("name4", "value4", "test");
 root.addContent(element);

 parentNode = new JDOMAttrributePointer(jdomContext.getDocument().getRootElement(), root);
 }

 @Test
 public void testJDOMAttributeIteratorConstructor() {
 QName name = new QName("test:name1");
 attributeIterator = new JDOMAttributeIterator(parentNode, name);
 }

 @Test
 public void testGetNodePointer_NormalCase() {
 QName name = new QName("test:name1");
 Attribute expectedAttribute = (Attribute) parentNode.getDocument().getRootElement().getChild("element").getChild("attribute1");
 NodePointer actualNodePointer = attributeIterator.getNodePointer();

 Assert.assertNotNull(actualNodePointer);
 Assert.assertEquals(expectedAttribute, actualNodePointer.getNode());
 }

 @Test
 public void testGetNodePointer_EndOfListCase() {
 QName name = new QName("test:name5");
 attributeIterator = new JDOMAttributeIterator(parentNode, name);

 NodePointer actualNodePointer = attributeIterator.getNodePointer();

 Assert.assertNull(actualNodePointer);
 }

 @Test
 public void testGetNodePointer_InvalidPositionCase() {
 QName name = new QName("test:name1");
 attributeIterator = new JDOMAttributeIterator(parentNode, name);

 Assert.assertFalse(attributeIterator.setPosition(5));
 NodePointer actualNodePointer = attributeIterator.getNodePointer();

 Assert.assertNull(actualNodePointer);
 }

 @Test
 public void testGetPosition_NormalCase() {
 QName name = new QName("test:name1");
 attributeIterator = new JDOMAttributeIterator(parentNode, name);

 int expectedPosition = 1;
 int actualPosition = attributeIterator.getPosition();

 Assert.assertEquals(expectedPosition, actualPosition);
 }

 @Test
 public void testSetPosition_NormalCase() {
 QName name = new QName("test:name1");
 attributeIterator = new JDOMAttributeIterator(parentNode, name);

 int expectedPosition = 2;
 boolean actualResult = attributeIterator.setPosition(expectedPosition);

 Assert.assertTrue(actualResult);
 int actualPosition = attributeIterator.getPosition();

 Assert.assertEquals(expectedPosition, actualPosition);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPosition_InvalidPositionCase() {
 QName name = new QName("test:name1");
 attributeIterator = new JDOMAttributeIterator(parentNode, name);

 int expectedPosition = 5;
 boolean actualResult = attributeIterator.setPosition(expectedPosition);
 }

 @Test
 public void testSetPosition_EndOfListCase() {
 QName name = new QName("test:name1");
 attributeIterator = new JDOMAttributeIterator(parentNode, name);

 int expectedPosition = attributes.size() + 1;
 boolean actualResult = attributeIterator.setPosition(expectedPosition);
 }
}