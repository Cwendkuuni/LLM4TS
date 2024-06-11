package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.junit.Test;
import static org.junit.Assert.*;

public class W3CDomTest {

 private Document createJsoupDocument() {
 return new org.jsoup.helper.W3CDom().fromJsoup(new org.jsoup.nodes.Document());
 }

 @Test
 public void testFromJsoup() {
 Document jsoupDocument = createJsoupDocument();
 Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
 assertNotNull(w3cDocument);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFromJsoupNullInput() {
 new W3CDom().fromJsoup(null);
 }

 @Test
 public void testConvert() {
 Document jsoupDocument = createJsoupDocument();
 Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
 new W3CDom().convert(jsoupDocument, w3cDocument);
 assertSame(jsoupDocument.childNodes().item(0), w3cDocument.getDocumentElement());
 }

 @Test
 public void testW3CBuilder() {
 Document jsoupDocument = createJsoupDocument();
 Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
 NodeTraversor traversal = new NodeTraversor();
 traversal.traverse(new NodeVisitor() {
 @Override
 public void head(Node node, int depth) {
 if (node instanceof Element) {
 String tagName = node.tagName();
 assertEquals("tagName", tagName, ((Element) node).getTagName());
 }
 }

 @Override
 public void tail(Node node, int depth) {
 // empty
 }
 }, w3cDocument.getDocumentElement());
 }

 @Test
 public void testAsString() {
 Document w3cDocument = createJsoupDocument();
 Document w3cDocumentWithChild = new DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
 w3cDocumentWithChild.appendChild(w3cDocument.getDocumentElement());
 String w3cString = new W3CDom().asString(w3cDocumentWithChild);
 String jsoupString = new org.jsoup.nodes.Document(w3cDocument).toString();
 assertEquals("asString", w3cString, jsoupString);
 }

 @Test(expected = IllegalStateException.class)
 public void testAsStringNullInput() {
 new W3CDom().asString(null);
 }

 @Test
 public void testEmptyDocument() {
 Document w3cDocument = new DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
 String w3cString = new W3CDom().asString(w3cDocument);
 String emptyString = "";
 assertEquals("asString - empty document", emptyString, w3cString);
 }

 @Test
 public void testComment() {
 Document jsoupDocument = new org.jsoup.nodes.Document();
 jsoupDocument.appendElement("p").appendText("This is a comment").appendElement("--!").attr("test", "test");
 Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
 Element comment = (Element) w3cDocument.getDocumentElement().getElementsByTagName("!--").item(0);
 assertEquals("comment text", "This is a comment", comment.getTextContent());
 assertEquals("comment attribute", "test", comment.getAttribute("test"));
 }

 @Test
 public void testAttributes() {
 Document jsoupDocument = new org.jsoup.nodes.Document();
 Element p = jsoupDocument.createElement("p");
 p.setAttribute("class", "test");
 p.setText("Test");
 Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
 Element pElement = (Element) w3cDocument.getDocumentElement().getElementsByTagName("p").item(0);
 assertEquals("attribute value", "test", pElement.getAttribute("class"));
 assertEquals("attribute value", "Test", pElement.getTextContent());
 }

 @Test
 public void testEmptyAttribute() {
 Document jsoupDocument = new org.jsoup.nodes.Document();
 Element p = jsoupDocument.createElement("p");
 p.setAttribute("class", "");
 Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
 Element pElement = (Element) w3cDocument.getDocumentElement().getElementsByTagName("p").item(0);
 assertTrue("empty attribute present", pElement.hasAttribute("class"));
 assertNull("empty attribute value", pElement.getAttribute("class"));
 }

 @Test
 public void testNamespace() {
 Document jsoupDocument = new org.jsoup.nodes.Document();
 jsoupDocument.appendChild(jsoupDocument.createElementNS("http://www.example.com/namespace", "ns:element"));
 Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
 Element nsElement = (Element) w3cDocument.getDocumentElement().getElementsByTagNameNS("http://www.example.com/namespace", "ns:element").item(0);
 assertEquals("namespace prefix", "ns", nsElement.getPrefix());
 assertEquals("namespace local name", "element", nsElement.getLocalName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFromJsoupEmptyDocument() {
 new W3CDom().fromJsoup(new DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
 }

}