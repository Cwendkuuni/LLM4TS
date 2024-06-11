package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedList;

/**
 * Test class for Parser.java
 *
 * @author YourName
 */
public class ParserTest {

 private static final String HTML_TEMPLATE = "<html><head><title>Test Title</title></head>" +
         "<body><h1>Test Heading</h1><p>Test Paragraph</p>" +
         "<textarea>Test TextArea</textarea>" +
         "<img alt=\"Test Image\" src=\"test.jpg\"/>" +
         "<base href=\"/test/\"/>" +
         "</body></html>";

 @Test
 public void testParse() {
     String html = HTML_TEMPLATE;
     String baseUri = "http://test.com/";
     Document doc = Parser.parse(html, baseUri);
     assertEquals("Test Title", doc.title());
     Element body = doc.body();
     assertEquals("Test Heading", body.select("h1").text());
     assertEquals("Test Paragraph", body.select("p").text());
     Element textarea = body.select("textarea").first();
     assertEquals("Test TextArea", textarea.text());
     assertEquals("/test/test.jpg", textarea.select("img").attr("src"));
     assertEquals("http://test.com/", doc.baseUri());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullHtml() {
     String html = null;
     String baseUri = "http://test.com/";
     try {
         Parser.parse(html, baseUri);
         fail("Expected IllegalArgumentException");
     } catch (IllegalArgumentException e) {
         // expected
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullBaseUri() {
     String html = HTML_TEMPLATE;
     String baseUri = null;
     try {
         Parser.parse(html, baseUri);
         fail("Expected IllegalArgumentException");
     } catch (IllegalArgumentException e) {
         // expected
     }
 }

 @Test
 public void testParseBodyFragment() {
     String bodyHtml = "<p>Test Paragraph</p>";
     Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com/");
     assertEquals("", doc.head().text());
     Element body = doc.body();
     assertEquals("Test Paragraph", body.text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
     String bodyHtml = "<p>Test Paragraph</p>";
     Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://test.com/");
     assertEquals("", doc.head().text());
     Element body = doc.body();
     assertEquals("Test Paragraph", body.text());
 }

 @Test
 public void testParseEmptyString() {
     String html = "";
     String baseUri = "http://test.com/";
     Document doc = Parser.parse(html, baseUri);
     assertNotNull(doc);
     assertTrue(doc.isEmpty());
 }

 @Test
 public void testParseInvalidHtml() {
     String html = "<html><body><h1<script>alert('Test')</script>";
     String baseUri = "http://test.com/";
     try {
         Document doc = Parser.parse(html, baseUri);
         fail("Expected ParserError");
     } catch (Parser.ParserError e) {
         // expected
     }
 }

 @Test
 public void testParseTagWithoutEnd() {
     String html = "<html><body><h1<img src=\"test.jpg\"/>";
     String baseUri = "http://test.com/";
     try {
         Document doc = Parser.parse(html, baseUri);
         fail("Expected ParserError");
     } catch (Parser.ParserError e) {
         // expected
     }
 }

 @Test
 public void testParseXmlDecl() {
     String html = "<html xmlns=\"http://www.w3.org/1999/xhtml\">";
     String baseUri = "http://test.com/";
     Document doc = Parser.parse(html, baseUri);
     XmlDeclaration xmlDecl = doc.select("xml-declaration").first();
     assertEquals("http://www.w3.org/1999/xhtml", xmlDecl.xmlns());
     assertEquals("http://test.com/", xmlDecl.documentURI());
 }

 @Test
 public void testParseComment() {
     String html = "<html><!-- Test Comment --><body></body></html>";
     String baseUri = "http://test.com/";
     Document doc = Parser.parse(html, baseUri);
     Comment comment = doc.select("html-->").first();
     assertEquals("Test Comment", comment.text());
 }

 @Test
 public void testParseEndTag() {
     String html = "<html><body><h1></h1></body></html>";
     String baseUri = "http://test.com/";
     Document doc = Parser.parse(html, baseUri);
     Element body = doc.body();
     assertEquals(1, body.childNodeSize());
     Element h1 = body.child(0);
     assertEquals(0, h1.childNodeSize());
 }

 @Test
 public void testParseStartTag() {
     String html = "<html><head><title>Test Title</title><body>" +
             "<h1 id=\"test-id\">Test Heading</h1>" +
             "<p class=\"test-class\">Test Paragraph</p>" +
             "<textarea name=\"test-name\">Test TextArea</textarea>" +
             "<img alt=\"Test Image\" src=\"test.jpg\" width=\"100\" height=\"100\"/>" +
             "</body></html>";
     String baseUri = "http://test.com/";
     Document doc = Parser.parse(html, baseUri);
     Element title = doc.select("title").first();
     assertEquals("Test Title", title.text());
     Element body = doc.body();
     assertEquals(5, body.childNodeSize());
     Element h1 = (Element) body.child(0);
     assertEquals("h1", h1.tagName());
     assertEquals("Test Heading", h1.text());
     Element p = (Element) body.child(1);
     assertEquals("p", p.tagName());
     assertEquals("Test Paragraph", p.text());
     Element textarea = (Element) body.child(3);
     assertEquals("textarea", textarea.tagName());
     Attributes attrs = textarea.attributes();
     assertEquals("Test TextArea", attrs.get("value"));
     assertEquals("test-name", attrs.get("name"));
     assertEquals(2, attrs.size());
     Element img = (Element) body.child(4);
     assertEquals("img", img.tagName());
     assertEquals("Test Image", img.attr("alt"));
     assertEquals("test.jpg", img.attr("src"));
     assertEquals("100", img.attr("width"));
     assertEquals("100", img.attr("height"));
 }

 @Test
 public void testParseAttribute() {
     String html = "<div class=\"test-class\" id=\"test-id\" title=\"Test Title\"/>";
     String baseUri = "http://test.com/";
     Document doc = Parser.parse(html, baseUri);
     Element div = doc.select("div").first();
     assertEquals("div", div.tagName());
     Attributes attrs = div.attributes();
     assertEquals("test-class", attrs.get("class"));
     assertEquals("test-id", attrs.get("id"));
     assertEquals("Test Title", attrs.get("title"));
     assertEquals(3, attrs.size());
 }

 @Test
 public void testParseTextNode() {
     String html = "<html><head><title>Test Title</title><body><p>Test Paragraph</p></body></html>";
     String baseUri = "http://test.com/";
     Document doc = Parser.parse(html, baseUri);
     Element body = doc.body();
     TextNode textNode = (TextNode) body.child(0);
     assertEquals("Test Paragraph", textNode.text());
 }

 @Test
 public void testParseCdata() {
     String html = "<html><body><![CDATA[Test CDATA]]></body></html>";
     String baseUri = "http://test.com/";
     Document doc = Parser.parse(html, baseUri);
     TextNode textNode = (TextNode) doc.body().child(0);
     assertEquals("Test CDATA", textNode.text());
 }

 @Test
 public void testParseBaseTag() {
     String html = "<html><head><title>Test Title</title><body>" +
             "<base href=\"/test/\"/>" +
             "</body></html>";
     String baseUri = "http://test.com/";
     Document doc = Parser.parse(html, baseUri);
     assertEquals("/test/", doc.baseUri());
 }
}