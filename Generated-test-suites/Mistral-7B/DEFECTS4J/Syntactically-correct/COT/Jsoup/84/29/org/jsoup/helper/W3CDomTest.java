package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Traversal;
import org.w3c.dom.traversal.DocumentTraversal;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class W3CDomTest {

 // Test for fromJsoup method
 @Test
 public void testFromJsoup() {
 Document inJsoupDoc = W3CDom.newJsoupDocument();
 Document outW3CDoc = W3CDom.newInstance().fromJsoup(inJsoupDoc);

 Assert.assertNotNull(outW3CDoc);
 }

 // Test for convert method with an empty document
 @Test
 public void testConvertEmpty() {
 Document inJsoupDoc = W3CDom.newJsoupDocument();
 Document outW3CDoc = W3CDom.newInstance().convert(inJsoupDoc, W3CDom.newDocument());

 Assert.assertNotNull(outW3CDoc);
 Assert.assertEquals(0, outW3CDoc.getChildNodes().getLength());
 }

 // Test for convert method with a document having root element
 @Test
 public void testConvertWithRootElement() {
 Document inJsoupDoc = W3CDom.newJsoupDocumentWithRootElement();
 Document outW3CDoc = W3CDom.newInstance().convert(inJsoupDoc, W3CDom.newDocument());

 Assert.assertNotNull(outW3CDoc);
 Assert.assertEquals(1, outW3CDoc.getChildNodes().getLength());

 Element rootElement = (Element) outW3CDoc.getChildNodes().item(0);
 Assert.assertNotNull(rootElement);
 Assert.assertEquals("root", rootElement.getTagName());
 }

 // Test for convert method with a document having child elements
 @Test
 public void testConvertWithChildElements() {
 Document inJsoupDoc = W3CDom.newJsoupDocumentWithChildElements();
 Document outW3CDoc = W3CDom.newInstance().convert(inJsoupDoc, W3CDom.newDocument());

 Assert.assertNotNull(outW3CDoc);
 Assert.assertEquals(3, outW3CDoc.getChildNodes().getLength());

 Element rootElement = (Element) outW3CDoc.getChildNodes().item(0);
 Assert.assertNotNull(rootElement);
 Assert.assertEquals("root", rootElement.getTagName());

 Element childElement1 = (Element) rootElement.getChildNodes().item(0);
 Assert.assertNotNull(childElement1);
 Assert.assertEquals("child1", childElement1.getTagName());

 Element childElement2 = (Element) rootElement.getChildNodes().item(1);
 Assert.assertNotNull(childElement2);
 Assert.assertEquals("child2", childElement2.getTagName());

 Element childElement3 = (Element) rootElement.getChildNodes().item(2);
 Assert.assertNotNull(childElement3);
 Assert.assertEquals("child3", childElement3.getTagName());
 }

 // Test for asString method with an empty document
 @Test
 public void testAsStringEmpty() {
 Document inW3CDoc = W3CDom.newDocument();
 String outString = W3CDom.newInstance().asString(inW3CDoc);

 Assert.assertEquals("", outString);
 }

 // Test for asString method with a document having root element
 @Test
 public void testAsStringWithRootElement() {
 Document inW3CDoc = W3CDom.newInstance().fromJsoup(W3CDom.newJsoupDocumentWithRootElement());
 String outString = W3CDom.newInstance().asString(inW3CDoc);

 Assert.assertNotNull(outString);
 Assert.assertEquals("<root></root>", outString);
 }

 // Test for asString method with a document having root element and child elements
 @Test
 public void testAsStringWithChildElements() {
 Document inW3CDoc = W3CDom.newInstance().fromJsoup(W3CDom.newJsoupDocumentWithChildElements());
 String outString = W3CDom.newInstance().asString(inW3CDoc);

 Assert.assertNotNull(outString);
 Assert.assertEquals("<root><child1>child1</child1><child2>child2</child2><child3>child3</child3></root>", outString);
 }

 // Test for fromJsoup method with null input
 @Test(expected = NullPointerException.class)
 public void testFromJsoupNullInput() {
 W3CDom w3CDom = new W3CDom();
 w3CDom.fromJsoup(null);
 }

 // Test for convert method with null input
 @Test(expected = NullPointerException.class)
 public void testConvertNullInput() {
 W3CDom w3CDom = new W3CDom();
 w3CDom.convert(null, W3CDom.newDocument());
 }

 // Test for convert method with null output
 @Test(expected = NullPointerException.class)
 public void testConvertNullOutput() {
 Document inJsoupDoc = W3CDom.newJsoupDocument();
 W3CDom w3CDom = new W3CDom();
 w3CDom.convert(inJsoupDoc, null);
 }

 // Test for asString method with null input
 @Test(expected = NullPointerException.class)
 public void testAsStringNullInput() {
 W3CDom w3CDom = new W3CDom();
 w3CDom.asString(null);
 }

 // Test for asString method with empty output
 @Test
 public void testAsStringEmptyOutput() {
 Document inW3CDoc = W3CDom.newDocument();
 W3CDom w3CDom = new W3CDom();
 String outString = w3CDom.asString(inW3CDoc);

 Assert.assertEquals("", outString);
 }

 // Test for asString method with empty document
 @Test
 public void testAsStringEmptyDocument() {
 Document inW3CDoc = W3CDom.newDocument();
 Document inJsoupDoc = W3CDom.newJsoupDocumentFromW3CDoc(inW3CDoc);
 String outString = W3CDom.newInstance().asString(inW3CDoc);

 Assert.assertEquals("", outString);
 }

 // Test for asString method with document having root element and empty child nodes
 @Test
 public void testAsStringWithEmptyChildNodes() {
 Document inW3CDoc = W3CDom.newDocument();
 Element rootElement = inW3CDoc.createElement("root");
 inW3CDoc.appendChild(rootElement);
 String outString = W3CDom.newInstance().asString(inW3CDoc);

 Assert.assertEquals("<root></root>", outString);
 }

 // Test for asString method with document having root element and child elements
 @Test
 public void testAsStringWithChildElementsAndNamespaces() {
 Document inW3CDoc = W3CDom.newDocumentWithNamespaces();
 Element rootElement = inW3CDoc.createElementNS("http://example.com", "root");
 Element childElement1 = inW3CDoc.createElementNS("http://example.com", "child1");
 childElement1.setTextContent("child1");
 rootElement.appendChild(childElement1);

 Element childElement2 = inW3CDoc.createElementNS("http://example.com", "child2");
 childElement2.setTextContent("child2");
 rootElement.appendChild(childElement2);

 String outString = W3CDom.newInstance().asString(inW3CDoc);

 Assert.assertEquals("<root xmlns=\"http://example.com\"><child1>child1</child1><child2>child2</child2></root>", outString);
 }

 // Test for asString method with document having root element and child elements with namespaces
 @Test
 public void testAsStringWithChildElementsNamespacesAndPrefixes() {
 Document inW3CDoc = W3CDom.newDocumentWithNamespaces();
 Element rootElement = inW3CDoc.createElementNS("http://example.com", "root");
 Element childElement1 = inW3CDoc.createElementNS("http://example.com", "child1");
 childElement1.setTextContent("child1");
 rootElement.appendChild(childElement1);
 rootElement.setAttributeNS("http://example.com", "xmlns:x", "http://example.com/x");
 rootElement.setAttributeNS("http://example.com", "xmlns:y", "http://example.com/y");

 Element childElement2 = inW3CDoc.createElementNS("{http://example.com}x", "child2");
 childElement2.setTextContent("child2");
 rootElement.appendChild(childElement2);

 Element childElement3 = inW3CDoc.createElementNS("{http://example.com}y", "child3");
 childElement3.setTextContent("child3");
 rootElement.appendChild(childElement3);

 String outString = W3CDom.newInstance().asString(inW3CDoc);

 Assert.assertEquals("<root xmlns:x=\"http://example.com/x\" xmlns:y=\"http://example.com/y\"><child1>child1</child1><x:child2>child2</x:child2><y:child3>child3</y:child3></root>", outString);
 }

 // Test for asString method with document having root element and child elements with namespaces and comments
 @Test
 public void testAsStringWithChildElementsNamespacesCommentsAndPrefixes() {
 Document inW3CDoc = W3CDom.newDocumentWithNamespaces();
 Element rootElement = inW3CDoc.createElementNS("http://example.com", "root");
 Element childElement1 = inW3CDoc.createElementNS("http://example.com", "child1");
 childElement1.setTextContent("child1");
 rootElement.appendChild(childElement1);
 rootElement.setAttributeNS("http://example.com", "xmlns:x", "http://example.com/x");
 rootElement.setAttributeNS("http://example.com", "xmlns:y", "http://example.com/y");

 Element childElement2 = inW3CDoc.createElementNS("{http://example.com}x", "child2");
 childElement2.setTextContent("child2");
 rootElement.appendChild(childElement2);

 Element childElement3 = inW3CDoc.createElementNS("{http://example.com}y", "child3");
 childElement3.setTextContent("child3");
 rootElement.appendChild(childElement3);

 Comment comment = inW3CDoc.createComment("This is a comment");
 rootElement.appendChild(comment);

 String outString = W3CDom.newInstance().asString(inW3CDoc);

 Assert.assertEquals("<root xmlns:x=\"http://example.com/x\" xmlns:y=\"http://example.com/y\"><child1>child1</child1><x:child2>child2</x:child2><y:child3>child3</y:child3><!--This is a comment-->", outString);
 }

 private static Document newDocument() {
 Document document = null;
 try {
 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 DocumentBuilder builder = factory.newDocumentBuilder();
 document = builder.newDocument();
 } catch (ParserConfigurationException e) {
 e.printStackTrace();
 }
 return document;
 }

 private static Document newDocumentWithNamespaces() {
 Document document = null;
 try {
 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 factory.setNamespaceAware(true);
 DocumentBuilder builder = factory.newDocumentBuilder();
 document = builder.newDocument();
 } catch (ParserConfigurationException e) {
 e.printStackTrace();
 }
 return document;
 }

 private static org.jsoup.nodes.Document newJsoupDocument() {
 return org.jsoup.Jsoup.parse("<root></root>");
 }

 private static org.jsoup.nodes.Document newJsoupDocumentWithRootElement() {
 return org.jsoup.Jsoup.parse("<root></root>");
 }

 private static org.jsoup.nodes.Document newJsoupDocumentWithChildElements() {
 return org.jsoup.Jsoup.parse("<root><child1></child1><child2></child2><child3></child3></root>");
 }

 private static org.jsoup.nodes.Document newJsoupDocumentFromW3CDoc(Document inW3CDoc) {
 Document outJsoupDoc = null;
 try {
 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 DocumentBuilder builder = factory.newDocumentBuilder();
 outJsoupDoc = builder.newDocument();
 Traversal traversal = ((DocumentTraversal) inW3CDoc);
 NodeVisitor visitor = new NodeVisitor() {
 @Override
 public void visit(Node node) {
 if (node instanceof Element) {
 Element element = (Element) node;
 outJsoupDoc.importNode(node, true);
 } else if (node instanceof Text) {
 Text text = (Text) node;
 outJsoupDoc.appendChild(org.jsoup.Jsoup.parse(text.getData()));
 } else if (node instanceof Comment) {
 Comment comment = (Comment) node;
 outJsoupDoc.appendChild(org.jsoup.Jsoup.parse(comment.getData()));
 }
 }
 };
 traversal.traverse(visitor);
 } catch (ParserConfigurationException | TransformerException e) {
 e.printStackTrace();
 }
 return outJsoupDoc;
 }
}