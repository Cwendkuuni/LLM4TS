package org.jsoup.helper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class W3CDomTest {

 private W3CDom w3cdom;
 private Document jsoupDocument;
 private Document w3cDocument;
 private String xmlString;

 @Before
 public void setUp() {
 w3cdom = new W3CDom();
 jsoupDocument = new org.jsoup.nodes.Document();
 w3cDocument = w3cdom.fromJsoup(jsoupDocument);
 }

 @After
 public void tearDown() {
 // Free resources
 jsoupDocument.close();
 w3cDocument.getDocumentElement().getParentNode().getParentNode().removeChild(w3cDocument.getDocumentElement().getParentNode());
 w3cDocument = null;
 jsoupDocument = null;
 }

 @Test
 public void testFromJsoup() {
 assertNotNull(w3cDocument);
 }

 @Test
 public void testConvert() {
 Element root = w3cDocument.getDocumentElement();
 root.setTagName("html");
 root.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");
 Element head = w3cdom.newElement("head");
 Element title = w3cdom.newElement("title");
 title.setTextNode("Test Title");
 head.appendChild(title);
 root.appendChild(head);

 Element body = w3cdom.newElement("body");
 Text text = w3cdom.newTextNode("Test Content");
 body.appendChild(text);
 root.appendChild(body);

 w3cdom.convert(jsoupDocument, w3cDocument);

 assertEquals("html", w3cDocument.getDocumentElement().getTagName());
 assertEquals(1, w3cDocument.getDocumentElement().getChildNodes().getLength());
 assertEquals("head", w3cDocument.getDocumentElement().getChildNodes().item(0).getTagName());
 assertEquals("title", w3cDocument.getDocumentElement().getChildNodes().item(0).getChildNodes().getLength());
 assertEquals("Test Title", w3cDocument.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0).getNodeValue());
 assertEquals("body", w3cDocument.getDocumentElement().getChildNodes().item(1).getTagName());
 assertEquals("Test Content", w3cDocument.getDocumentElement().getChildNodes().item(1).getChildNodes().item(0).getNodeValue());
 }

 @Test
 public void testAsString() {
 xmlString = w3cdom.asString(w3cDocument);
 assertTrue(xmlString.contains("<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>Test Title</title></head><body>Test Content</body></html>"));
 }
}