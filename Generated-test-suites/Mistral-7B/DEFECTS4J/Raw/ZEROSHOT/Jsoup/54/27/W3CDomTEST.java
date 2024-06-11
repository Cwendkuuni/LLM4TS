package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.parser.ParserConfigurationException;

import java.io.StringWriter;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {

 private W3CDom w3CDom;
 private org.jsoup.nodes.Document jsoupDocument;

 @Before
 public void setup() {
 w3CDom = new W3CDom();
 jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test W3CDom</title></head><body><p>Test</p></body></html>");
 }

 @Test
 public void testFromJsoup() {
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 assertNotNull(w3cDocument);
 }

 @Test
 public void testConvert() {
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 w3CDom.convert(jsoupDocument, w3cDocument);

 Element rootElement = (Element) w3cDocument.getDocumentElement();
 assertEquals("html", rootElement.getTagName());

 Element titleElement = (Element) rootElement.getElementsByTagName("head").item(0);
 assertEquals("title", titleElement.getTagName());

 Element bodyElement = (Element) rootElement.getElementsByTagName("body").item(0);
 assertEquals("p", bodyElement.getElementsByTagName("p").item(0).getTagName());
 }

 @Test
 public void testAsString() {
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 String w3cDocumentString = w3CDom.asString(w3cDocument);
 assertEquals("<html>\n<head>\n<title>Test W3CDom</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>", w3cDocumentString);
 }

 @Test
 public void testComment() {
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 Element commentElement = jsoupDocument.body().insertBefore(jsoupDocument.body().child(0), new org.jsoup.nodes.TextNode("Test Comment")); asElement();
 w3CDom.convert(jsoupDocument, w3cDocument);
 Comment w3cComment = (Comment) w3cDocument.getDocumentElement().getElementsByTagName("comment").item(0);
 assertEquals("Test Comment", w3cComment.getData());
 }

 @Test
 public void testTextNode() {
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 Element textElement = jsoupDocument.body().textNodes().get(0).splitText(3);
 w3CDom.convert(jsoupDocument, w3cDocument);
 Text w3cTextNode = (Text) w3cDocument.getDocumentElement().getElementsByTagName("text").item(0);
 assertEquals("Test ", w3cTextNode.getData());
 }

}