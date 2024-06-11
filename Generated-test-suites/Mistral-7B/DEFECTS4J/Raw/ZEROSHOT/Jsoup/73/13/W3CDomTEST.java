package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class W3CDomTest {

 @Test
 public void testFromJsoup() throws ParserConfigurationException, TransformerException {
 Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
 Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

 assertEquals("html", w3cDoc.getDocumentElement().getTagName());
 assertEquals("http://www.w3.org/1999/xhtml", w3cDoc.getDocumentURI());

 }

 @Test
 public void testConvert() throws ParserConfigurationException, TransformerException {
 Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><p id='test'>Hello World</p></body></html>");
 Document w3cDoc = new Document();
 new W3CDom().convert(jsoupDoc, w3cDoc);

 assertEquals("html", w3cDoc.getDocumentElement().getTagName());
 assertEquals("http://www.w3.org/1999/xhtml", w3cDoc.getDocumentURI());

 Element pElement = (Element) w3cDoc.getElementsByTagName("p").item(0);
 assertEquals("p", pElement.getTagName());
 assertEquals("Hello World", pElement.getTextContent());
 assertEquals("test", pElement.getAttribute("id"));

 }

 @Test
 public void testAsString() throws ParserConfigurationException, TransformerException {
 Document w3cDoc = new DocumentBuilderFactory().newDocumentBuilder().newDocument();
 w3cDoc.setDocumentURI("http://example.com");
 Element root = w3cDoc.createElement("root");
 w3cDoc.appendChild(root);
 Element child = w3cDoc.createElement("child");
 root.appendChild(child);
 child.setTextContent("Hello World");

 Document jsoupDoc = new W3CDom().fromJsoup(w3cDoc);
 String w3cString = new W3CDom().asString(jsoupDoc);
 String expected = "<html><head></head><body><div xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:w=\"http://example.com/\">" +
         "<root>" +
         "<child>Hello World</child>" +
         "</root></body></html>";

 assertEquals(expected, w3cString);
 }

 @Test
 public void testNamespaces() throws ParserConfigurationException, TransformerException {
 Document w3cDoc = new DocumentBuilderFactory().newDocumentBuilder().newDocument();
 w3cDoc.setDocumentURI("http://example.com");
 w3cDoc.getDocumentElement().setAttribute("xmlns:x", "http://example.com/x");
 w3cDoc.getDocumentElement().setAttribute("xmlns:y", "http://example.com/y");
 Element root = w3cDoc.createElement("root");
 root.setAttributeNS("http://example.com/x", "xml:id", "one");
 w3cDoc.appendChild(root);
 Element child = w3cDoc.createElement("child");
 child.setTextContent("Hello World");
 root.appendChild(child);

 Document jsoupDoc = new W3CDom().fromJsoup(w3cDoc);
 NodeIterator nodeIterator = jsoupDoc.selectNode("//*:one").iterator();
 Element element = nodeIterator.next();
 assertEquals("one", element.id());
 }

 @Test
 public void testComment() throws ParserConfigurationException, TransformerException {
 Document w3cDoc = new DocumentBuilderFactory().newDocumentBuilder().newDocument();
 Comment comment = w3cDoc.createComment("Test Comment");
 w3cDoc.appendChild(comment);
 Document jsoupDoc = new W3CDom().fromJsoup(w3cDoc);
 assertEquals("Test Comment", jsoupDoc.body().text());
 }
}