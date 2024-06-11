package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class DOMAttributeIteratorTest {

 private Element testElement;
 private Node testParent;
 private DOMAttributeIterator iterator;

 @Before
 public void setUp() {
 testParent = DOMUtil.createTestDocument().getDocumentElement();
 testElement = testParent.createElement("testElement");
 testParent.appendChild(testElement);
 testElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
 testElement.setAttributeNS("", "testattr1", "value1");
 testElement.setAttributeNS("", "testattr2", "value2");
 testElement.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xml:id", "testId");
 }

 @Test
 public void testIterationWithWildcardName() {
 iterator = new DOMAttributeIterator(new DOMPointer(testParent), null);
 NodePointer nodePointer;
 for (int i = 0; i < 3; i++) {
 nodePointer = iterator.next();
 assertNotNull(nodePointer);
 assertInstanceOf(Attr.class, nodePointer.getNode());
 }
 }

 @Test
 public void testIterationWithSpecificName() {
 iterator = new DOMAttributeIterator(new DOMPointer(testParent), new QName("testattr1", ""));
 NodePointer nodePointer = iterator.next();
 assertNotNull(nodePointer);
 assertInstanceOf(Attr.class, nodePointer.getNode());
 assertEquals("value1", nodePointer.getNode().getValue());
 }

 @Test
 public void testIterationWithSpecificPrefixAndName() {
 iterator = new DOMAttributeIterator(new DOMPointer(testParent), new QName("xmlns:xsi", "xml:id"));
 NodePointer nodePointer = iterator.next();
 assertNotNull(nodePointer);
 assertInstanceOf(Attr.class, nodePointer.getNode());
 assertEquals("testId", nodePointer.getNode().getNodeValue());
 }

 @Test
 public void testIterationWithEmptyList() {
 iterator = new DOMAttributeIterator(new DOMPointer(testParent), new QName("nonexistent", ""));
 assertNull(iterator.next());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIterationWithNullParent() {
 iterator = new DOMAttributeIterator(null, new QName("testattr1", ""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIterationWithNullName() {
 iterator = new DOMAttributeIterator(new DOMPointer(testParent), null);
 }

 @Test
 public void testGetPosition() {
 iterator = new DOMAttributeIterator(new DOMPointer(testParent), new QName("testattr1", ""));
 iterator.next();
 iterator.next();
 int position = iterator.getPosition();
 assertEquals(2, position);
 }
}