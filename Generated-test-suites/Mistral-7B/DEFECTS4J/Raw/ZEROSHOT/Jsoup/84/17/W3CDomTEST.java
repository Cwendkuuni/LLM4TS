package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextDocument;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.ParserConfigurationException;

public class W3CDomTest {

 @Test
 public void testFromJsoup() throws ParserConfigurationException {
 Document jsoupDocument = new TextDocument("");
 W3CDom w3cdom = new W3CDom();
 Document w3cDocument = w3cdom.fromJsoup(jsoupDocument);
 assertNotNull(w3cDocument);
 }

 @Test
 public void testConvert() {
 Document jsoupDocument = new TextDocument("");
 W3CDom w3cdom = new W3CDom();
 Document w3cDocument = w3cdom.newDocument();
 Element root = w3cdom.newElement("root");
 w3cdom.convert(jsoupDocument, w3cDocument);
 w3cdom.convert(jsoupDocument.body(), root, w3cDocument);

 assertEquals(1, w3cDocument.getDocumentElement().getChildNodes().getLength());
 Element rootElement = (Element) w3cDocument.getDocumentElement().getChildNodes().item(0);
 assertEquals("root", rootElement.getTagName());
 }

 @Test
 public void testConvertWithAttributes() {
 Document jsoupDocument = new TextDocument("<html lang='en'><head><title>Test</title></head><body><p class='foo' id='bar'>Hello world</p></body></html>");
 W3CDom w3cdom = new W3CDom();
 Document w3cDocument = w3cdom.newDocument();
 Element root = w3cdom.newElement("html");
 w3cdom.convert(jsoupDocument, w3cDocument);
 w3cdom.convert(jsoupDocument.body(), root, w3cDocument);

 assertEquals("html", root.getTagName());
 Element head = (Element) root.getElementsByTagName("head").item(0);
 assertEquals(1, head.getChildNodes().getLength());
 Element title = (Element) head.getChildNodes().item(0);
 assertEquals("Test", title.getTextContent());
 assertEquals("en", title.getAttribute("lang"));

 Element body = (Element) root.getElementsByTagName("body").item(0);
 assertEquals(1, body.getChildNodes().getLength());
 Element p = (Element) body.getChildNodes().item(0);
 assertEquals("p", p.getTagName());
 assertEquals("Hello world", p.getTextContent());
 assertEquals("foo", p.getAttribute("class"));
 assertEquals("bar", p.getAttribute("id"));
 }

 @Test
 public void testConvertWithNamespaces() {
 Document jsoupDocument = new TextDocument("<html xmlns:xhtml='http://www.w3.org/1999/xhtml'><xhtml:head><xhtml:title>Test</xhtml:title></xhtml:head><xhtml:body><xhtml:p class='foo' id='bar'>Hello world</xhtml:p></xhtml:body></html>");
 W3CDom w3cdom = new W3CDom();
 Document w3cDocument = w3cdom.newDocument();
 Element root = w3cdom.newElement("html");
 w3cdom.convert(jsoupDocument, w3cDocument);
 w3cdom.convert(jsoupDocument.body(), root, w3cDocument);

 assertEquals("html", root.getTagName());
 Element head = (Element) root.getElementsByTagNameNS("http://www.w3.org/1999/xhtml", "head").item(0);
 assertEquals(1, head.getChildNodes().getLength());
 Element title = (Element) head.getChildNodes().item(0);
 assertEquals("Test", title.getTextContent());

 Element body = (Element) root.getElementsByTagNameNS("http://www.w3.org/1999/xhtml", "body").item(0);
 assertEquals(1, body.getChildNodes().getLength());
 Element p = (Element) body.getChildNodes().item(0);
 assertEquals("p", p.getTagName());
 assertEquals("Hello world", p.getTextContent());
 assertEquals("foo", p.getAttributeNS("http://www.w3.org/2000/xmlns/", "class"));
 assertEquals("bar", p.getAttributeNS("http://www.w3.org/2000/xmlns/", "id"));
 }

 @Test
 public void testAsString() {
 Document w3cDocument = w3cdom.newDocument();
 Element root = w3cdom.newElement("root");
 root.appendChild(w3cdom.newTextNode("Hello world"));
 w3cDocument.appendChild(root);

 assertEquals("<root>Hello world</root>", w3cdom.asString(w3cDocument));
 }
}