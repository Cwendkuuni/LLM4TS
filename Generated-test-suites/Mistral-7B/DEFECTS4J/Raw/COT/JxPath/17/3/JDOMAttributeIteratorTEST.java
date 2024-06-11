package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.JDOMException;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 private static Element createElement(String name, String prefix, String uri, String... attributes) {
 Element element = new Element(name);
 for (String attribute : attributes) {
 String[] attrParts = attribute.split(":");
 String name = attrParts[0];
 String value = attrParts[1];
 Attribute attribute = new Attribute(name, value, new Namespace(prefix, uri));
 element.addContent(attribute);
 }
 return element;
 }

 @Test
 public void testJDOMAttributeIterator_singleAttribute() {
 NodePointer parent = new JDOMPointer(createElement("root", null, null), null);
 QName name = new QName("test:attr");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
 NodePointer attributePointer = iterator.getNodePointer();
 assertEquals(parent, attributePointer.getParent());
 Attribute attribute = (Attribute) attributePointer.getNode();
 assertEquals("test:attr", attribute.getName());
 }

 @Test
 public void testJDOMAttributeIterator_emptyList() {
 NodePointer parent = new JDOMPointer(createElement("root", null, null), null);
 QName name = new QName("*");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
 assertNull(iterator.getNodePointer());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJDOMAttributeIterator_nullParent() {
 NodePointer parent = null;
 QName name = new QName("test:attr");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJDOMAttributeIterator_nullName() {
 NodePointer parent = new JDOMPointer(createElement("root", null, null), null);
 QName name = null;
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
 }

 @Test
 public void testJDOMAttributeIterator_multipleAttributes() {
 NodePointer parent = new JDOMPointer(createElement("root", null, null), null);
 QName name = new QName("*");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
 NodePointer firstAttribute = iterator.getNodePointer();
 NodePointer secondAttribute = iterator.getNodePointer();
 assertEquals(parent, firstAttribute.getParent());
 assertEquals(parent, secondAttribute.getParent());
 Attribute firstAttr = (Attribute) firstAttribute.getNode();
 Attribute secondAttr = (Attribute) secondAttribute.getNode();
 assertEquals(firstAttr.getValue(), secondAttr.getValue());
 // check that all attributes have been returned
 int attrCount = 0;
 while (iterator.getNodePointer() != null) {
 attrCount++;
 iterator.next();
 }
 assertEquals(3, attrCount);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testJDOMAttributeIterator_negativePosition() {
 NodePointer parent = new JDOMPointer(createElement("root", null, null), null);
 QName name = new QName("test:attr");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
 iterator.setPosition(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testJDOMAttributeIterator_positionBeyondLastAttribute() {
 NodePointer parent = new JDOMPointer(createElement("root", null, null), null);
 QName name = new QName("test:attr");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.setPosition(4);
 }

 @Test(expected = JDOMException.class)
 public void testJDOMAttributeIterator_namespaceResolutionFailure() throws JDOMException {
 NodePointer parent = new JDOMPointer(createElement("root", "xmlns:x", "http://test.com"), null);
 QName name = new QName("x:test");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
 }

}