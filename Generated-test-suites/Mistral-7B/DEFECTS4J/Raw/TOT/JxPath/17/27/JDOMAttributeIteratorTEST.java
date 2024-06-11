package org.apache.commons.jxpath.ri.model.jdom;

import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class JDOMAttributeIteratorTest {

 private NodePointer root;
 private NodeIterator iterator;

 @Before
 public void setUp() throws Exception {
 String xml = "<root xmlns:x='http://test'><child attr1='value1' attr2='value2' /></root>";
 Document doc = new SAXBuilder().build(new StringReader(xml));
 root = new NodePointer(doc.getRootElement());
 }

 @Test
 public void testEmptyIterator() {
 iterator = new JDOMAttributeIterator(root, new QName("", "*"));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testSingleAttributeIterator() {
 QName name = new QName("x", "attr1");
 iterator = new JDOMAttributeIterator(root, name);
 NodePointer attribute = iterator.next();
 assertEquals(root, attribute.getParent());
 assertSame(name, attribute.getNodeName());
 assertNull(iterator.next());
 }

 @Test
 public void testMultipleAttributesIterator() {
 QName name = new QName("x", "attr2");
 iterator = new JDOMAttributeIterator(root, name);
 NodePointer attribute1 = iterator.next();
 assertEquals(root, attribute1.getParent());
 assertSame(name, attribute1.getNodeName());
 NodePointer attribute2 = iterator.next();
 assertEquals(root, attribute2.getParent());
 assertSame(new QName("", "xmlns:x"), attribute1.getNodeName().getNamespace());
 assertSame(new QName("x", "*"), attribute2.getNodeName());
 assertNull(iterator.next());
 }

 @Test
 public void testMixedNamespaceIterator() throws JDOMException {
 String xml = "<root xmlns:x='http://test' xmlns:y='http://other'><child attr1='value1' attr2='value2' attr3='value3' /></root>";
 Document doc = new SAXBuilder().build(new StringReader(xml));
 root = new NodePointer(doc.getRootElement());

 QName name1 = new QName("x", "attr1");
 NodePointer attribute1 = new JDOMAttributeIterator(root, name1).next().getNodePointer();

 QName name2 = new QName("", "xmlns:x");
 NodePointer namespace1 = new JDOMAttributeIterator(root, name2).next().getNodePointer();

 QName name3 = new QName("y", "attr3");
 NodePointer attribute3 = new JDOMAttributeIterator(root, name3).next().getNodePointer();

 assertSame(root, attribute1.getParent());
 assertSame(name1, attribute1.getNodeName());
 assertSame(root, namespace1.getParent());
 assertSame(name2, namespace1.getNodeName());
 assertSame(root, attribute3.getParent());
 assertSame(name3, attribute3.getNodeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullParent() {
 iterator = new JDOMAttributeIterator(null, new QName("", "*"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullName() {
 iterator = new JDOMAttributeIterator(root, null);
 }
}