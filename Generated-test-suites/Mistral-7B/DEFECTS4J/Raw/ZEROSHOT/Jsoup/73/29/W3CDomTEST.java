package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextDocument;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.ParserConfigurationException;

public class W3CDomTest {

 private W3CDom w3CDom = new W3CDom();

 @Test
 public void testFromJsoup() throws ParserConfigurationException {
 // Given a jsoup Document
 Document jsoupDocument = new TextDocument("").body();
 jsoupDocument.appendText("Hello World!");

 // When converting the jsoup Document to a W3C Document
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);

 // Then the W3C Document should have the same content as the jsoup Document
 Assert.assertEquals("Hello World!", w3CDom.asString(w3cDocument));

 // And the W3C Document should have the same structure as the jsoup Document
 Assert.assertEquals(2, w3cDocument.getDocumentElement().getChildNodes().getLength());
 Assert.assertEquals("body", w3cDocument.getDocumentElement().getNodeName());
 Element bodyElement = (Element) w3cDocument.getDocumentElement().getChildNodes().item(0);
 Assert.assertEquals("Hello World!", bodyElement.getTextContent());
 }

 @Test
 public void testConvert() throws ParserConfigurationException {
 // Given a jsoup Document
 Document jsoupDocument = new TextDocument("").body();
 jsoupDocument.appendText("Hello <b>World!</b>");

 // When converting the jsoup Document to a W3C Document and then back to a jsoup Document
 Document jsoupDocumentConverted = w3CDom.fromJsoup(w3CDom.fromJsoup(jsoupDocument));

 // Then the jsoup Document should be equal to the original jsoup Document
 Assert.assertEquals(jsoupDocument.html(), jsoupDocumentConverted.html());

 // And the W3C Document should have the same structure and content as the original jsoup Document
 Element bodyElement = (Element) jsoupDocument.childNodes().item(0);
 Element bodyElementW3C = (Element) jsoupDocumentConverted.getDocumentElement().getElementsByTagName("body").item(0);
 Assert.assertEquals(bodyElement, bodyElementW3C);
 Assert.assertEquals("Hello <b>World!</b>", bodyElement.html());
 Assert.assertEquals("Hello <b>World!</b>", bodyElementW3C.getTextContent());
 }

 @Test
 public void testNamespaces() throws ParserConfigurationException {
 // Given a jsoup Document with namespaces
 Document jsoupDocument = new TextDocument("").body();
 jsoupDocument.appendElement("html").html("");
 jsoupDocument.body().appendElement("html:body").appendText("Hello World!");

 // When converting the jsoup Document to a W3C Document
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);

 // Then the W3C Document should have the same structure and content as the jsoup Document
 Assert.assertEquals("Hello World!", w3CDom.asString(w3cDocument));

 // And the W3C Document should have the correct namespaces
 Assert.assertEquals("http://www.w3.org/1999/xhtml", w3cDocument.getDocumentElement().getNamespaceURI());
 Assert.assertEquals("html", w3cDocument.getDocumentElement().getLocalName());
 Element bodyElement = (Element) w3cDocument.getDocumentElement().getElementsByTagName("body").item(0);
 Assert.assertEquals("http://www.w3.org/1999/xhtml", bodyElement.getNamespaceURI());
 Assert.assertEquals("body", bodyElement.getLocalName());
 }

 @Test
 public void testComment() throws ParserConfigurationException {
 // Given a jsoup Document with a comment
 Document jsoupDocument = new TextDocument("").body();
 jsoupDocument.appendComment("This is a comment");

 // When converting the jsoup Document to a W3C Document
 Document w3cDocument = w3cDocument = w3CDom.fromJsoup(jsoupDocument);

 // Then the W3C Document should have the same structure and content as the jsoup Document, including the comment
 Assert.assertEquals("", w3cDocument.getDocumentElement().getTextContent());
 Comment comment = (Comment) w3cDocument.getDocumentElement().getChildNodes().item(0);
 Assert.assertEquals("This is a comment", comment.getData());
 }
}