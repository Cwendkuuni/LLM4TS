package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 private static final String XML_NS = "xml";
 private static final String NS_PREFIX = "test";
 private static final String ATTRIBUTE_NAME = "testAttribute";
 private static final String ELEMENT_NAME = "testElement";
 private static final String ELEMENT_XMLNS = "http://test.com/namespace";

 @Test
 public void testEmptyAttributeIterator() throws JDOMException {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, new QName(XML_NS, "*"));
 assertEquals(Collections.EMPTY_LIST, iterator.getAttributes());
 }

 @Test
 public void testSingleAttributeIterator() throws JDOMException {
 Document document = new Document();
 Element root = new Element(ELEMENT_NAME);
 document.setRootElement(root);
 Attribute attribute = new Attribute(new QName(NS_PREFIX, ATTRIBUTE_NAME), "testValue");
 root.setAttribute(attribute);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(document, root), new QName(NS_PREFIX, ATTRIBUTE_NAME));
 List<NodePointer> nodePointers = new ArrayList<>();
 nodePointers.add(iterator.getNodePointer());
 assertEquals(1, nodePointers.size());
 }

 @Test
 public void testMultipleAttributesIterator() throws JDOMException {
 Document document = new Document();
 Element root = new Element(ELEMENT_NAME);
 document.setRootElement(root);
 root.addNamespaceDeclaration(NS_PREFIX, ELEMENT_XMLNS);
 Attribute attribute1 = new Attribute(new QName(NS_PREFIX, ATTRIBUTE_NAME, ELEMENT_XMLNS), "testValue1");
 Attribute attribute2 = new Attribute(new QName(NS_PREFIX, ATTRIBUTE_NAME, ELEMENT_XMLNS), "testValue2");
 root.setAttribute(attribute1);
 root.setAttribute(attribute2);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(document, root), new QName(NS_PREFIX, ATTRIBUTE_NAME, ELEMENT_XMLNS));
 List<NodePointer> nodePointers = new ArrayList<>();
 int position = 0;
 while (iterator.hasNext()) {
 nodePointers.add(iterator.next());
 position++;
 }
 assertEquals(2, nodePointers.size());
 }

 @Test(expected = JDOMException.class)
 public void testAttributeIteratorWithNullName() throws JDOMException {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(null, null), null);
 }

 @Test(expected = JDOMException.class)
 public void testAttributeIteratorWithNullParent() throws JDOMException {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, new QName(XML_NS, ATTRIBUTE_NAME));
 }

 @Test
 public void testAttributeIteratorWithWildcardName() throws JDOMException {
 Document document = new Document();
 Element root = new Element(ELEMENT_NAME);
 document.setRootElement(root);
 root.addNamespaceDeclaration(NS_PREFIX, ELEMENT_XMLNS);
 root.addNamespaceDeclaration(XML_NS, "http://www.w3.org/2000/xmlns/");
 Attribute attribute1 = new Attribute(new QName(NS_PREFIX, ATTRIBUTE_NAME, ELEMENT_XMLNS), "testValue1");
 Attribute attribute2 = new Attribute(new QName(XML_NS, "xml:id", XML_NS), "testId");
 root.setAttribute(attribute1);
 root.setAttribute(attribute2);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(document, root), new QName(XML_NS, "*"));
 List<NodePointer> nodePointers = new ArrayList<>();
 int position = 0;
 while (iterator.hasNext()) {
 nodePointers.add(iterator.next());
 position++;
 }
 assertEquals(2, nodePointers.size());
 }
}