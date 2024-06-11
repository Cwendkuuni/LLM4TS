package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.parser.ParserConfigurationException;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class W3CDomTest {

 private W3CDom w3CDom;
 private Document jsoupDocument;
 private Document w3cDocument;

 @Before
 public void setUp() {
 w3CDom = new W3CDom();

 jsoupDocument = DocumentHelper.parseText("<html><body><h1>Hello World</h1></body></html>");
 }

 @Test
 public void testFromJsoup() {
 w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 assertNotNull(w3cDocument);
 }

 @Test(expected = IllegalStateException.class)
 public void testFromJsoupWithNullInput() {
 w3CDom.fromJsoup(null);
 }

 @Test
 public void testConvert() {
 w3CDom.convert(jsoupDocument, w3cDocument);

 assertEquals(1, w3cDocument.getDocumentElement().getChildNodes().getLength());
 Element root = w3cDocument.getDocumentElement();
 assertEquals("html", root.getNodeName());
 assertEquals(1, root.getChildNodes().getLength());

 Element body = (Element) root.getChildNodes().item(0);
 assertEquals("body", body.getNodeName());
 assertEquals(1, body.getChildNodes().getLength());

 Element h1 = (Element) body.getChildNodes().item(0);
 assertEquals("h1", h1.getNodeName());
 assertEquals("Hello World", h1.getTextContent());
 }

 @Test
 public void testAsString() {
 String w3cString = w3CDom.asString(w3cDocument);
 assertNotNull(w3cString);
 }

 @Test
 public void testFromJsoupWithNamespaces() {
 Attributes jsoupAttributes = jsoupDocument.head().attributes();
 jsoupAttributes.put("xmlns:html", "http://www.w3.org/1999/xhtml");
 jsoupAttributes.put("xmlns:foo", "http://example.com/foo");

 w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 String xmlnsPrefix_html = w3CDom.W3CBuilder.xmlnsKey;
 String xmlnsPrefix_foo = "foo";

 String htmlNamespace = w3CDom.W3CBuilder.namespaces.get(xmlnsPrefix_html);
 String fooNamespace = w3CDom.W3CBuilder.namespaces.get(xmlnsPrefix_foo);

 Element htmlElement = w3cDocument.getDocumentElement();
 assertNotNull(htmlElement);
 assertEquals(htmlNamespace, htmlElement.getNamespaceURI());
 assertEquals("html", htmlElement.getTagName());

 Element fooElement = (Element) htmlElement.getElementsByTagNameNS(fooNamespace, "test").item(0);
 assertNull(fooElement);

 htmlElement.setAttributeNS(htmlNamespace, "html:test", "hello");
 fooElement = (Element) htmlElement.getElementsByTagNameNS(fooNamespace, "test").item(0);
 assertNull(fooElement);

 htmlElement.setAttributeNS(htmlNamespace, "html:test", "hello");
 htmlElement.setAttributeNS(xmlnsPrefix_foo, "foo:test", "world");

 fooElement = (Element) htmlElement.getElementsByTagNameNS(fooNamespace, "test").item(0);
 assertNotNull(fooElement);
 assertEquals("world", fooElement.getTextContent());
 }

}