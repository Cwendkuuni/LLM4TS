package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.parser.ParserConfigurationException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Stack;

import static org.junit.Assert.*;

public class W3CDomTest {

 private DocumentBuilderFactory factory;
 private W3CDom w3CDom;
 private org.jsoup.nodes.Document jsoupDocument;

 @Before
 public void setUp() {
 factory = DocumentBuilderFactory.newInstance();
 w3CDom = new W3CDom();
 jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p id='p1'>Hello World</p></body></html>");
 }

 @Test
 public void testFromJsoup() throws ParserConfigurationException {
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 assertNotNull(w3cDocument);
 assertTrue(w3cDocument instanceof Document);
 }

 @Test
 public void testConvert() throws ParserConfigurationException {
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 w3CDom.convert(jsoupDocument, w3cDocument);
 assertNotNull(w3cDocument.getDocumentElement());
 assertEquals("html", w3cDocument.getDocumentElement().getTagName());
 }

 @Test
 public void testAsString() throws ParserConfigurationException, TransformerException {
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 String w3cString = w3CDom.asString(w3cDocument);
 String jsoupString = jsoupDocument.body().html();
 assertEquals(jsoupString, w3cString);
 }

 @Test
 public void testConvertWithNamespaces() throws ParserConfigurationException {
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 w3CDom.convert(jsoupDocument, w3cDocument);
 Element body = w3cDocument.getDocumentElement().getElementsByTagName("body").item(0);
 body.setAttribute("xmlns:jsoup", "http://jsoup.org");
 Element p = (Element) body.getElementsByTagName("p").item(0);
 p.setAttribute("class", "test");
 p.setAttribute("xml:id", "p1");
 w3CDom.convert(jsoupDocument, w3cDocument);
 assertEquals("http://jsoup.org", w3cDocument.getDocumentElement().getNamespaceURI());
 assertEquals("test", p.getAttribute("class"));
 assertEquals("p1", p.getAttribute("xml:id"));
 }

 @Test
 public void testTextNodes() throws ParserConfigurationException, TransformerException {
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 w3CDom.convert(jsoupDocument, w3cDocument);
 Element root = w3cDocument.getDocumentElement();
 Element p = (Element) root.getElementsByTagName("p").item(0);
 Text textNode = p.getTextContent();
 assertEquals("Hello World", textNode.getData());
 }

 @Test
 public void testCommentNodes() throws ParserConfigurationException, TransformerException {
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 w3CDom.convert(jsoupDocument, w3cDocument);
 Element root = w3cDocument.getDocumentElement();
 Comment commentNode = (Comment) root.getElementsByTagName("comment").item(0);
 assertEquals("This is a comment", commentNode.getData());
 }
}