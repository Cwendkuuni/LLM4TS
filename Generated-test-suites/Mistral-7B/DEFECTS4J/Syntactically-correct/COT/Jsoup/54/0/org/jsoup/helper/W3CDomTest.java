package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.StringWriter;
import java.util.HashMap;

public class W3CDomTest {

 private W3CDom w3CDom;
 private Document jsoupDocument;
 private Document w3cDocument;

 @Before
 public void setUp() {
 w3CDom = new W3CDom();
 jsoupDocument = org.jsoup.helper.HttpConnection.responseToDocument(
 "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
 "<html>\r\n" +
 "<head>\r\n" +
 "<title>Test</title>\r\n" +
 "</head>\r\n" +
 "<body>\r\n" +
 "<h1 id=\"test\">Hello World</h1>\r\n" +
 "</body>\r\n" +
 "</html>");
 }

 @Test
 public void testFromJsoup() {
 w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 Assert.assertNotNull(w3cDocument);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFromJsoupNullDocument() {
 w3CDom.fromJsoup(null);
 }

 @Test
 public void testConvert() {
 w3cDocument = w3cDocument.getOwnerDocument(); // reset for testing
 w3CDom.convert(jsoupDocument, w3cDocument);
 Assert.assertEquals(jsoupDocument.childNodes().getLength(), w3cDocument.getChildNodes().getLength());
 }

 @Test
 public void testAsString() {
 String jsoupString = w3CDom.asString(jsoupDocument);
 String w3cString = w3CDom.asString(w3cDocument);
 Assert.assertEquals(jsoupString, w3cString);
 }

 @Test
 public void testFromJsoupWithNamespace() {
 org.jsoup.nodes.Document namespaceJsoupDocument = org.jsoup.helper.HttpConnection.responseToDocument(
 "<html xmlns:test=\"http://example.com/test\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.w3.org/1999/xhtml\">\r\n" +
 "<head>\r\n" +
 "<title>Test</title>\r\n" +
 "</head>\r\n" +
 "<body>\r\n" +
 "<h1 id=\"test\" test:attribute=\"value\">Hello World</h1>\r\n" +
 "</body>\r\n" +
 "</html>");
 Document namespaceW3cDocument = w3CDom.fromJsoup(namespaceJsoupDocument);
 Assert.assertEquals(namespaceJsoupDocument.getElementsByTagName("h1").item(0).getAttribute("test:attribute"), namespaceW3cDocument.getElementsByTagName("h1").item(0).getAttribute("test:attribute"));
 }

 @Test(expected = IllegalStateException.class)
 public void testFromJsoupWithInvalidParserConfigurationException() {
 factory.setNamespaceAware(true); // set it before the method call to simulate an exception
 Document invalidDocument = null; // should throw an exception
 try {
 w3cDocument = w3CDom.fromJsoup(invalidDocument);
 } catch (ParserConfigurationException e) {
 // expected
 }
 }
}