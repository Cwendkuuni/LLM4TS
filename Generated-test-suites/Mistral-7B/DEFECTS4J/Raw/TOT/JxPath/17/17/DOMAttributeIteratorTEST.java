package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DOMAttributeIteratorTest {

 private NodePointer nodePointer;
 private DOMAttributeIterator iterator;
 private Document document;

 @Before
 public void setup() {
 document = Mockito.mock(Document.class);
 nodePointer = new DOMPointer("", document);
 }

 @Test
 public void testEmptyIterator() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("", "*"));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testSingleAttribute() {
 Element element = Mockito.mock(Element.class);
 Attr attribute = Mockito.mock(Attr.class);
 Mockito.when(element.getAttributeNode(Mockito.anyString())).thenReturn(attribute);
 Mockito.when(document.createElementNS(Mockito.anyString(), Mockito.anyString())).thenReturn(element);
 Node pointer = Mockito.mock(Node.class);
 Mockito.when(pointer.getParentNode()).thenReturn(document);
 nodePointer = new DOMPointer("/", document);
 nodePointer.setNode(pointer);
 iterator = new DOMAttributeIterator(nodePointer, new QName("", "test"));
 Mockito.when(attribute.getNodeValue()).thenReturn("testValue");
 assertEquals("testValue", iterator.next().getValue());
 }

 @Test
 public void testMultipleAttributes() {
 Element element = Mockito.mock(Element.class);
 Attr attribute1 = Mockito.mock(Attr.class);
 Attr attribute2 = Mockito.mock(Attr.class);
 Mockito.when(element.getAttributeNode(Mockito.anyString())).thenReturn(Mockito.any(Attr.class));
 Mockito.when(attribute1.getNodeValue()).thenReturn("attr1Value");
 Mockito.when(attribute2.getNodeValue()).thenReturn("attr2Value");
 Mockito.when(document.createElementNS(Mockito.anyString(), Mockito.anyString())).thenReturn(element);
 NodeList nodeList = Mockito.mock(NodeList.class);
 Mockito.when(document.getDocumentElement().getAttributes()).thenReturn(nodeList);
 Mockito.when(nodeList.item(0)).thenReturn(attribute1);
 Mockito.when(nodeList.item(1)).thenReturn(attribute2);
 nodePointer = new DOMPointer("/", document);
 iterator = new DOMAttributeIterator(nodePointer, new QName("", "*"));
 assertEquals("attr1Value", iterator.next().getValue());
 assertEquals("attr2Value", iterator.next().getValue());
 assertNull(iterator.next());
 }

 @Test
 public void testNamespaceAttribute() {
 DocumentType documentType = Mockito.mock(DocumentType.class);
 Element root = Mockito.mock(Element.class);
 Mockito.when(documentType.getDocumentElement()).thenReturn(root);
 Mockito.when(root.getTagName()).thenReturn("html");
 Mockito.when(root.getNamespaceURI()).thenReturn("http://www.w3.org/1999/xhtml");
 Attr namespaceAttr = Mockito.mock(Attr.class);
 Mockito.when(namespaceAttr.getLocalName()).thenReturn("xmlns:html");
 Mockito.when(namespaceAttr.getValue()).thenReturn("http://www.w3.org/1999/xhtml");
 Mockito.when(root.getAttributeNodeNS(Mockito.anyString(), Mockito.anyString())).thenReturn(namespaceAttr);
 nodePointer = new DOMPointer("/", document);
 iterator = new DOMAttributeIterator(nodePointer, new QName("", "html"));
 assertEquals("http://www.w3.org/1999/xhtml", iterator.next().getValue());
 }

 @Test
 public void testFailingIterator() {
 Document document = Mockito.mock(Document.class);
 NodePointer nodePointer = new DOMPointer("/", document);
 iterator = new DOMAttributeIterator(nodePointer, new QName("", "*"));
 assertFalse(iterator.hasNext());
 }
}