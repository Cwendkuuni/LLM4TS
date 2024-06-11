package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class W3CDomTest {

 private W3CDom w3CDom = new W3CDom();

 @Test
 public void testFromJsoup() {
  org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
  Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
  Assert.assertNotNull(w3cDocument);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFromJsoupNullInput() {
  W3CDom w3CDom = new W3CDom();
  Assert.assertThrows(IllegalArgumentException.class, () -> w3CDom.fromJsoup(null));
 }

 @Test
 public void testConvert() {
  org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
  Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
  w3CDom.convert(jsoupDocument, w3cDocument);

  // Basic check of the structure
  Assert.assertEquals(jsoupDocument.nodeName(), w3cDocument.getDocumentElement().getNodeName());
  Assert.assertEquals(jsoupDocument.nodeName(), w3cDocument.getDocumentElement().getNodeName());
  Assert.assertEquals(jsoupDocument.childNodes().getLength(), w3cDocument.getDocumentElement().getChildNodes().getLength());

  // Check specific nodes
  Element h1 = (Element) jsoupDocument.selectFirst("h1");
  Element w3CH1 = (Element) w3cDocument.getElementsByTagName("h1").item(0);
  Assert.assertEquals(h1.text(), w3CH1.getTextContent());

  Text textNode = (Text) jsoupDocument.selectFirst("text()");
  Text w3CTextNode = (Text) w3cDocument.getElementsByTagName("text").item(0);
  Assert.assertEquals(textNode.text(), w3CTextNode.getNodeValue());

  Comment comment = (Comment) jsoupDocument.selectFirst("comment()");
  Comment w3CComment = (Comment) w3cDocument.getElementsByTagName("comment").item(0);
  Assert.assertEquals(comment.data(), w3CComment.getNodeValue());
 }

 @Test
 public void testAsString() {
  Document doc = w3CDom.fromJsoup(org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>"));
  String jsoupOutput = w3CDom.asString(doc);
  Assert.assertNotNull(jsoupOutput);
  Assert.assertEquals("<html><body><h1>Hello World</h1></body></html>", jsoupOutput);
 }

 @Test
 public void testHandleEmptyInput() {
  Document emptyDoc = w3CDom.fromJsoup(org.jsoup.nodes.Document.emptyDocument());
  String asString = w3CDom.asString(emptyDoc);
  Assert.assertEquals("", asString);
 }

 @Test(expected = TransformerException.class)
 public void testTransformerException() {
  Document doc = w3CDom.fromJsoup(org.jsoup.nodes.Document.emptyDocument());
  Assert.assertThrows(TransformerException.class, () -> w3CDom.asString(null));
 }

 // Edge Cases
 @Test
 public void testConvertWithSingleNode() {
  org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><p></p></body></html>");
  Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
  w3CDom.convert(jsoupDocument, w3cDocument);

  // Basic check of the structure
  Assert.assertEquals(jsoupDocument.nodeName(), w3cDocument.getDocumentElement().getNodeName());
  Assert.assertEquals(jsoupDocument.nodeName(), w3cDocument.getDocumentElement().getNodeName());
  Assert.assertEquals(jsoupDocument.childNodes().getLength(), w3cDocument.getDocumentElement().getChildNodes().getLength());

  Element p = (Element) jsoupDocument.selectFirst("p");
  Element w3CP = (Element) w3cDocument.getElementsByTagName("p").item(0);
  Assert.assertEquals(p.text(), w3CP.getTextContent());
 }

 @Test
 public void testConvertWithEmptyTextNode() {
  org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><p><Text></Text></p></body></html>");
  Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
  w3CDom.convert(jsoupDocument, w3cDocument);

  Element p = (Element) jsoupDocument.selectFirst("p");
  Element w3CP = (Element) w3cDocument.getElementsByTagName("p").item(0);
  Assert.assertEquals(p.childNodes().getLength(), w3CP.getChildNodes().getLength());
  Assert.assertNull(w3CP.getChildNodes().item(0).getNodeValue());
 }

 @Test
 public void testConvertWithMultipleChildren() {
  org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><p><h1>Test</h1><span>Test</span></p></body></html>");
  Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
  w3CDom.convert(jsoupDocument, w3cDocument);

  Element p = (Element) jsoupDocument.selectFirst("p");
  Element w3CP = (Element) w3cDocument.getElementsByTagName("p").item(0);

  Assert.assertEquals(p.childNodes().getLength(), w3CP.getChildNodes().getLength());
  Assert.assertEquals(p.childNodes().get(0).nodeName(), w3CP.getChildNodes().item(0).getNodeName());
  Assert.assertEquals(p.childNodes().get(1).nodeName(), w3CP.getChildNodes().item(1).getNodeName());
 }

 @Test
 public void testConvertWithNestedComments() {
  org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><p><span><!-- Test -->Hello World<!-- Test --></span></p></body></html>");
  Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
  w3CDom.convert(jsoupDocument, w3cDocument);

  Element p = (Element) jsoupDocument.selectFirst("p");
  Element w3CP = (Element) w3cDocument.getElementsByTagName("p").item(0);

  Element span = (Element) p.childNodes().get(0);
  Comment firstComment = (Comment) span.childNodes().get(0);
  Comment secondComment = (Comment) span.childNodes().get(1);

  Assert.assertEquals(firstComment.getData(), w3CP.getElementsByTagName("comment").item(0).getNodeValue());
  Assert.assertEquals(secondComment.getData(), w3CP.getElementsByTagName("comment").item(1).getNodeValue());
 }

 @Test
 public void testConvertWithAttributes() {
  org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><p id='test' class='test'><h1>Hello World</h1></p></body></html>");
  Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
  w3CDom.convert(jsoupDocument, w3cDocument);

  Element p = (Element) jsoupDocument.selectFirst("p");
  Element w3CP = (Element) w3cDocument.getElementsByTagName("p").item(0);

  Map<String, String> jsoupAttributes = Attributes.toMap(p.attributes());
  Map<String, String> w3cAttributes = new HashMap<>();
  for (int i = 0; i < w3CP.getAttributes().getLength(); i++) {
      Node attribute = w3CP.getAttributes().item(i);
      w3cAttributes.put(attribute.getNodeName(), attribute.getNodeValue());
  }

  Assert.assertEquals(jsoupAttributes, w3cAttributes);
 }

 @Test
 public void testConvertWithNamespaces() {
  org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:xhtml='http://www.w3.org/1999/xhtml'><body><xhtml:p xmlns:html='http://www.w3.org/1999/xhtml'><xhtml:span xmlns:my='http://www.example.com/my' xmlns:my2='http://www.example.com/my2'><xhtml:h1 xmlns:other='http://www.example.com/other'>Test</xhtml:h1></xhtml:span></xhtml:p></body></html>");
  Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
  w3CDom.convert(jsoupDocument, w3cDocument);

  Element p = (Element) jsoupDocument.selectFirst("xhtml:p");
  Element w3CP = (Element) w3cDocument.getElementsByTagNameNS("http://www.w3.org/1999/xhtml", "p").item(0);

  Assert.assertEquals(p.namespaceURI(), w3CP.getNamespaceURI());
  Assert.assertEquals(p.getNamespacePrefix(), w3CP.getTagName().substring(0, w3CP.getTagName().indexOf(":")));

  Element span = (Element) p.childNodes().item(0);
  Assert.assertEquals(span.getNamespaceURI(), w3CP.getNamespaceURI());
  Assert.assertEquals(span.getNamespacePrefix(), w3CP.getTagName().substring(0, w3CP.getTagName().indexOf(":")));

  Element h1 = (Element) span.childNodes().item(0);
  Assert.assertEquals(h1.getNamespaceURI(), "http://www.example.com/other");
  Assert.assertEquals(h1.getNamespacePrefix(), "other");
 }
}