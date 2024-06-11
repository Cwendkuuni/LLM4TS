package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 @Test
 public void testDefaultSettings() {
 Document document = xmlTreeBuilder.defaultSettings().parse(new StringReader("<xml></xml>"), "http://test.com");
 Assert.assertNotNull(document);
 }

 @Test
 public void testInitialiseParse() {
 Document document = xmlTreeBuilder.parse(new StringReader("<xml></xml>"), "http://test.com");
 Element rootElement = document.body();
 Assert.assertNotNull(rootElement);
 Assert.assertEquals("xml", rootElement.nodeName());
 }

 @Test
 public void testProcessStartTag() {
 Document document = xmlTreeBuilder.parse(new StringReader("<root><child></child></root>"), "http://test.com");
 List<Node> children = document.body().childNodes();
 Assert.assertTrue(children.size() > 0);
 Element rootElement = (Element) children.get(0);
 Element childElement = (Element) children.get(1);
 Assert.assertEquals("root", rootElement.nodeName());
 Assert.assertEquals("child", childElement.nodeName());
 }

 @Test
 public void testProcessComment() {
 Document document = xmlTreeBuilder.parse(new StringReader("<!-- this is a comment -->"), "http://test.com");
 List<Node> comments = document.body().getElementsByTag("comment");
 Assert.assertTrue(comments.size() > 0);
 Comment comment = (Comment) comments.get(0);
 Assert.assertEquals(" this is a comment ", comment.text());
 }

 @Test
 public void testProcessCharacter() {
 Document document = xmlTreeBuilder.parse(new StringReader("<xml>abc</xml>"), "http://test.com");
 List<Node> textNodes = document.body().getElementsByTag("text");
 Assert.assertTrue(textNodes.size() > 0);
 TextNode textNode = (TextNode) textNodes.get(0);
 Assert.assertEquals("abc", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 Document document = xmlTreeBuilder.parse(new StringReader("<!DOCTYPE xml SYSTEM \"http://www.w3.org/TR/xml11/DTD/xml11.dtd\">"), "http://test.com");
 List<Node> doctypeNodes = document.body().getElementsByTag("DocumentType");
 Assert.assertTrue(doctypeNodes.size() > 0);
 DocumentType doctype = (DocumentType) doctypeNodes.get(0);
 Assert.assertEquals("xml", doctype.nodeName());
 }

 @Test
 public void testParseFragment() {
 Document document = xmlTreeBuilder.parseFragment("<root><child></child></root>", "http://test.com");
 Element rootElement = document.body();
 Element childElement = (Element) rootElement.getFirstChild();
 Assert.assertEquals("root", rootElement.nodeName());
 Assert.assertEquals("child", childElement.nodeName());
 }

 @Test (expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 xmlTreeBuilder.parse(new StringReader("{invalid xml}"), "http://test.com");
 }

 @Test (expected = NullPointerException.class)
 public void testNullInput() {
 xmlTreeBuilder.parse(null, "http://test.com");
 }

 @Test (expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 xmlTreeBuilder.parse(new StringReader("<xml></xml>"), null);
 }
}