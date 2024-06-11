package org.jsoup.helper;

import org.junit.Test;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class W3CDomTest {

 @Test
 public void testFromJsoup() {
 Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
 Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
 assertEquals("HTML", w3cDocument.getDocumentElement().getTagName());
 assertNotNull(w3cDocument.getDocumentElement().getElementsByTagName("body").item(0));
 assertNotNull(w3cDocument.getDocumentElement().getElementsByTagName("h1").item(0));
 assertEquals("Hello World", w3cDocument.getDocumentElement().getElementsByTagName("h1").item(0).getTextContent());
 }

 @Test
 public void testConvert() {
 Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1 xmlns:test='http://example.com'>Hello World</h1></body></html>");
 Document w3cDocument = new Document();
 new W3CDom().convert(jsoupDocument, w3cDocument);
 Element h1 = (Element) w3cDocument.getElementsByTagName("h1").item(0);
 assertEquals("http://example.com", h1.getNamespaceURI());
 assertEquals("test", h1.getPrefix());
 assertEquals("Hello World", h1.getTextContent());
 }

 @Test
 public void testConvertWithEmptyDocument() {
 Document w3cDocument = new Document();
 new W3CDom().convert(null, w3cDocument);
 assertNull(w3cDocument.getDocumentElement());
 }

 @Test
 public void testAsString() {
 Document w3cDocument = org.newanno.threadpoolfield.util.xml.XmlUtil.parseString("<html><body><h1>Hello World</h1></body></html>");
 String w3cString = new W3CDom().asString(w3cDocument);
 Document jsoupDocument = org.jsoup.Jsoup.parse(w3cString);
 assertEquals("<html><body><h1>Hello World</h1></body></html>", w3cString);
 assertEquals("HTML", jsoupDocument.documentElement().nodeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAsStringNullDocument() {
 String result = new W3CDom().asString(null);
 }
}