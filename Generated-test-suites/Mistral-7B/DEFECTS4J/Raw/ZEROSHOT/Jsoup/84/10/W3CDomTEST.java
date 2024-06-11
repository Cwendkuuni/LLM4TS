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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class W3CDomTest {

 @Test
 public void testFromJsoup() throws Exception {
 Document jsoupDocument = new TextDocument("");
 W3CDom w3CDom = new W3CDom();
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);

 Assert.assertNotNull(w3cDocument);
 }

 @Test
 public void testConvert() throws Exception {
 Document jsoupDocument = new TextDocument("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>");
 W3CDom w3CDom = new W3CDom();
 Document w3cDocument = w3CDom.newDocument();
 w3CDom.convert(jsoupDocument, w3cDocument);

 Assert.assertEquals("<html xmlns:xmlns=\"http://www.w3.org/1999/xhtml\"><head xmlns:xmlns=\"http://www.w3.org/1999/xhtml\"><title>Test</title></head><body xmlns:xmlns=\"http://www.w3.org/1999/xhtml\"><p>Hello World!</p></body></html>", w3CDom.asString(w3cDocument));
 }

 @Test
 public void testConvertWithNamespaces() throws Exception {
 Document jsoupDocument = new TextDocument("<html xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xmlns:test=\"http://test.com\"><head><title>Test</title></head><body xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"><xhtml:p xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xmlns:test=\"http://test.com\"><test:span test:attr=\"test\">Test</test:span></xhtml:p></body></html>");
 W3CDom w3CDom = new W3CDom();
 Document w3cDocument = w3CDom.newDocument();
 w3CDom.convert(jsoupDocument, w3cDocument);

 Assert.assertEquals("<html xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xmlns:test=\"http://test.com\"><head><title>Test</title></head><body xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"><p xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"><span xmlns:test=\"http://test.com\" test:attr=\"test\">Test</span></p></body></html>", w3CDom.asString(w3cDocument));
 }

 @Test
 public void testConvertWithAttributes() throws Exception {
 Document jsoupDocument = new TextDocument("<html><head><title attr=\"test\">Test</title></head><body><p attr=\"test\" class=\"test\">Hello World!</p></body></html>");
 W3CDom w3CDom = new W3CDom();
 Document w3cDocument = w3CDom.newDocument();
 w3CDom.convert(jsoupDocument, w3cDocument);

 Element title = (Element) w3cDocument.getElementsByTagName("title").item(0);
 Attribute titleAttr = title.getAttributeNode("attr");
 Assert.assertNotNull(titleAttr);
 Assert.assertEquals("test", titleAttr.getValue());

 Element p = (Element) w3cDocument.getElementsByTagName("p").item(0);
 Attribute pAttr = p.getAttributeNode("attr");
 Assert.assertNotNull(pAttr);
 Assert.assertEquals("test", pAttr.getValue());

 Attribute pClass = p.getAttributeNode("class");
 Assert.assertNotNull(pClass);
 Assert.assertEquals("test", pClass.getValue());
 }

 @Test
 public void testConvertWithNestedElements() throws Exception {
 Document jsoupDocument = new TextDocument("<html><head><title>Test</title></head><body><p><span>Hello World!</span></p></body></html>");
 W3CDom w3CDom = new W3CDom();
 Document w3cDocument = w3CDom.newDocument();
 w3CDom.convert(jsoupDocument, w3cDocument);

 Element p = (Element) w3cDocument.getElementsByTagName("p").item(0);
 Element span = (Element) p.getElementsByTagName("span").item(0);

 Assert.assertEquals("Hello World!", span.getTextContent());
 }

 @Test
 public void testConvertWithTextNodes() throws Exception {
 Document jsoupDocument = new TextDocument("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>");
 W3CDom w3CDom = new W3CDom();
 Document w3cDocument = w3CDom.newDocument();
 w3CDom.convert(jsoupDocument, w3cDocument);

 Element p = (Element) w3cDocument.getElementsByTagName("p").item(0);
 Text textNode = (Text) p.getFirstChild();

 Assert.assertEquals("Hello World!", textNode.getData());
 }

 @Test
 public void testConvertWithComments() throws Exception {
 Document jsoupDocument = new TextDocument("<html><!-- This is a comment --><head><title>Test</title></head><body><p>Hello World!</p></body></html>");
 W3CDom w3CDom = new W3CDom();
 Document w3cDocument = w3CDom.newDocument();
 w3CDom.convert(jsoupDocument, w3cDocument);

 Comment comment = (Comment) w3cDocument.getElementsByTagName("comment").item(0);

 Assert.assertEquals("This is a comment", comment.getData());
 }
}