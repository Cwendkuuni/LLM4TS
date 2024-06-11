package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.JDOMException;
import org.jdom.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 @Test
 public void testEmptyIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(), null);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testSingleAttributeIterator() throws JDOMException {
 Document doc = new Document();
 Element root = new Element("root", "xml");
 root.addAttribute("name", "value");
 doc.setRootElement(root);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(doc, root), null);
 NodePointer next = iterator.next();
 Attribute expectedAttribute = root.getAttribute("name");
 assertTrue(next instanceof JDOMAttributePointer);
 assertEquals(expectedAttribute, next.getNode());
 }

 @Test
 public void testMultipleAttributesIterator() throws JDOMException {
 Document doc = new Document();
 Element root = new Element("root", "xml");
 root.addAttribute("attr1", "value1");
 root.addAttribute("attr2", "value2");
 doc.setRootElement(root);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(doc, root), null);
 NodePointer next = null;
 while (iterator.hasNext()) {
 next = iterator.next();
 }
 assertEquals(2, iterator.getPosition());
 assertEquals("attr2", next.getNode().getName().getLocalPart());
 }

 @Test
 public void testFilteredIterator() throws JDOMException {
 Document doc = new Document();
 Element root = new Element("root", "xml");
 root.addAttribute("xml:attr1", "value1");
 root.addAttribute("attr2", "value2");
 doc.setRootElement(root);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(doc, root), new QName("xml", "*"));
 NodePointer next = null;
 while (iterator.hasNext()) {
 next = iterator.next();
 }
 assertEquals(1, iterator.getPosition());
 assertEquals("xml:attr1", next.getNode().getName().getQualifiedName());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testPreviousOutOfBounds() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(), null);
 iterator.previous();
 }

 @Test(expected = IllegalStateException.class)
 public void testSetPositionOutOfBounds() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(), null);
 iterator.setPosition(2);
 }
}