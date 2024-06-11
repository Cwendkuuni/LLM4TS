package org.apache.commons.jxpath.ri.model.jdom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.JDOMException;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JDOMAttributeIteratorTest {

 @Test
 public void testJDOMAttributeIterator_EmptyElement() {
 Element emptyElement = new Element("root");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(emptyElement), null);
 assertTrue(iterator.hasNext());
 assertNull(iterator.next());
 }

 @Test
 public void testJDOMAttributeIterator_SingleAttribute() throws JDOMException {
 Element element = new Element("root");
 element.addAttribute(new Attribute("attr", "value", "ns"));
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("ns:attr"));
 assertNotNull(iterator.next());
 assertEquals("value", iterator.next().getValue());
 assertNull(iterator.next());
 }

 @Test
 public void testJDOMAttributeIterator_MultipleAttributes() throws JDOMException {
 Element element = new Element("root");
 element.addAttribute(new Attribute("attr1", "value1", "ns1"));
 element.addAttribute(new Attribute("attr2", "value2", "ns2"));
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), null);
 assertEquals(2, iterator.size());
 assertNotNull(iterator.next());
 assertEquals("value1", iterator.next().getValue());
 assertNotNull(iterator.next());
 assertEquals("value2", iterator.next().getValue());
 assertNull(iterator.next());
 }

 @Test
 public void testJDOMAttributeIterator_NamespaceURINotFound() throws JDOMException {
 Element element = new Element("root");
 element.addAttribute(new Attribute("attr", "value", "ns:notFound"));
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("ns:notFound", "*"));
 assertNull(iterator.next());
 }

 @Test
 public void testJDOMAttributeIterator_NameNotPresent() throws JDOMException {
 Element element = new Element("root");
 element.addAttribute(new Attribute("attr1", "value1", "ns"));
 element.addAttribute(new Attribute("attr2", "value2", "ns"));
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("ns:attr3"));
 assertNull(iterator.next());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testJDOMAttributeIterator_NoAttributes() throws JDOMException {
 Element element = new Element("root");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), null);
 iterator.remove();
 }
}