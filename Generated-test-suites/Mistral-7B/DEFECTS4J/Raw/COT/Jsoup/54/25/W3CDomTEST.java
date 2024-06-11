package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.rpc.encoding.ModeledSerializable;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class W3CDomTest {

 private W3CDom w3CDom = new W3CDom();

 @Test
 public void testFromJsoup() {
  org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
  Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
  Assert.assertNotNull(w3cDoc);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFromJsoupNullInput() {
  W3CDom w3CDom = new W3CDom();
  Assert.assertThrows(IllegalArgumentException.class, () -> w3CDom.fromJsoup(null));
 }

 @Test
 public void testConvert() {
  org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><h1 xmlns:test=\"http://test.com\">Hello World</h1></body></html>");
  Document w3cDoc = w33CDom.fromJsoup(jsoupDoc);
  Document result = new Document();
  w3CDom.convert(jsoupDoc, result);
  Assert.assertEquals(w3cDoc.getDocumentElement(), result.getDocumentElement());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConvertNullJsoupDoc() {
  W3CDom w3CDom = new W3CDom();
  Assert.assertThrows(IllegalArgumentException.class, () -> w3CDom.convert(null, new Document()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConvertNullW3cDoc() {
  W3CDom w3CDom = new W3CDom();
  Document w3cDoc = new Document();
  Assert.assertThrows(IllegalArgumentException.class, () -> w3CDom.convert(new Document(), null));
 }

 @Test
 public void testAsString() {
  String xml = "<html><body><h1>Hello World</h1></body></html>";
  Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new org.xml.XMLInputFactory().createXMLEventReader(new StringReader(xml)));
  Document result = w3CDom.fromJsoup(org.jsoup.Jsoup.parse(xml));
  String string = w3CDom.asString(result);
  Assert.assertEquals(xml, string);
 }

 @Test(expected = IllegalStateException.class)
 public void testAsStringFailedTransform() {
  Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
  String string = w3CDom.asString(w3cDoc);
  Assert.assertThrows(IllegalStateException.class, () -> w3CDom.asString(w3cDoc));
 }

 @Test
 public void testWithNamespaces() {
  String xml = "<html xmlns:test=\"http://test.com\"><body><h1 xmlns:test=\"http://test.com\">Hello World</h1></body></html>";
  Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new org.xml.XMLInputFactory().createXMLEventReader(new StringReader(xml)));
  Document result = w3CDom.fromJsoup(org.jsoup.Jsoup.parse(xml));
  String string = w3CDom.asString(result);
  Assert.assertEquals(xml, string);
 }

 @Test
 public void testWithEmptyElement() {
  String xml = "<html><body><h1></h1></body></html>";
  Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new org.xml.XMLInputFactory().createXMLEventReader(new StringReader(xml)));
  Document result = w3CDom.fromJsoup(org.jsoup.Jsoup.parse(xml));
  String string = w3CDom.asString(result);
  Assert.assertEquals(xml, string);
 }

 @Test
 public void testWithAttributes() {
  String xml = "<html><body><h1 id=\"test\" class=\"test\" title=\"Test Title\"></h1></body></html>";
  Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new org.xml.XMLInputFactory().createXMLEventReader(new StringReader(xml)));
  Document result = w3CDom.fromJsoup(org.jsoup.Jsoup.parse(xml));
  String string = w3CDom.asString(result);
  Assert.assertEquals(xml, string);
 }

 @Test
 public void testWithComment() {
  String xml = "<html><!-- This is a comment --><body><h1>Hello World</h1></body></html>";
  Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new org.xml.XMLInputFactory().createXMLEventReader(new StringReader(xml)));
  Document result = w3CDom.fromJsoup(org.jsoup.Jsoup.parse(xml));
  String string = w3CDom.asString(result);
  Assert.assertEquals(xml, string);
 }

 @Test
 public void testWithTextNode() {
  String xml = "<html><body><p>This is some text</p></body></html>";
  Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new org.xml.XMLInputFactory().createXMLEventReader(new StringReader(xml)));
  Document result = w3CDom.fromJsoup(org.jsoup.Jsoup.parse(xml));
  String string = w3CDom.asString(result);
  Assert.assertEquals(xml, string);
 }

 @Test
 public void testWithDataNode() {
  String xml = "<html><body><p data-test=\"test\">This is some data</p></body></html>";
  Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new org.xml.XMLInputFactory().createXMLEventReader(new StringReader(xml)));
  Document result = w3CDom.fromJsoup(org.jsoup.Jsoup.parse(xml));
  String string = w3CDom.asString(result);
  Assert.assertEquals(xml, string);
 }

 @Test
 public void testWithEmptyDocument() {
  Document result = new Document();
  String string = w3CDom.asString(result);
  Assert.assertEquals("", string);
 }

}