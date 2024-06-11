package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

 private static final String TEST_XML = ""
         + "<root xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:test='http://test.com/'>"
         + "<element1 attr1='value1' attr2='value2' attr3='value3'/>"
         + "<element2 attr1='value4' attr2='value5' attr3='value6' attr4='value7'/>"
         + "</root>";

 @Test
 public void testJDOMAttributeIterator() {
  Element root = new org.jdom.JDOMParser().parse(new StringReader(TEST_XML)).getDocument().getRootElement();
  JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(root), null);
  List<Attribute> attributes = iterator.attributes;
  assertEquals(6, attributes.size());
  assertEquals("value1", attributes.get(0).getValue());
  assertEquals("value2", attributes.get(1).getValue());
  assertEquals("value3", attributes.get(2).getValue());
  assertEquals("value4", attributes.get(3).getValue());
  assertEquals("value5", attributes.get(4).getValue());
  assertEquals("value6", attributes.get(5).getValue());
 }

 @Test
 public void testJDOMAttributeIteratorWithWildcardQName() {
  Element root = new org.jdom.JDOMParser().parse(new StringReader(TEST_XML)).getDocument().getRootElement();
  JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(root), new QName("*"));
  List<Attribute> attributes = iterator.attributes;
  assertEquals(9, attributes.size());
  for (Attribute attribute : attributes) {
       System.out.println(attribute.getQualifiedName() + " = " + attribute.getValue());
  }
 }

 @Test
 public void testJDOMAttributeIteratorWithNamespace() {
  Element root = new org.jdom.JDOMParser().parse(new StringReader(TEST_XML)).getDocument().getRootElement();
  JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(root), new QName("test", "attr1"));
  List<Attribute> attributes = iterator.attributes;
  assertEquals(3, attributes.size());
  assertEquals("value1", attributes.get(0).getValue());
  assertEquals("value4", attributes.get(1).getValue());
  assertEquals("value4", attributes.get(2).getValue()); // second 'attr1' is from second element
 }

 @Test
 public void testJDOMAttributeIteratorWithEmptyList() {
  Element root = new Element("root");
  JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(root), new QName("xsi", "xmlns:xsi"));
  List<Attribute> attributes = iterator.attributes;
  assertEquals(0, attributes.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJDOMAttributeIteratorWithNullParent() {
  JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, new QName("xmlns", "xmlns"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJDOMAttributeIteratorWithNullQName() {
  JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(new Element("root")), null);
 }

}