package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

/**
 * Test class for Parser class.
 */
public class ParserTest {

 private static final String HTML_FRAGMENT = "<html><head><title>Test</title></head><body>" +
         "<p><textarea name='test'>test</textarea></p>" +
         "<script src=\"script.js\"></script>" +
         "<base href=\"/test\">" +
         "<img src=\"image.png\" alt=\"image\"/>" +
         "</body></html>";

 @Test
 public void testParse() {
     Document doc = Parser.parse(HTML_FRAGMENT, "http://test.com");
     assertNotNull(doc);
     assertEquals("http://test.com/test/", doc.baseUri());
     assertEquals(HtmlParser.Tag.HTML, doc.tagName());

     Element html = doc.body();
     assertEquals(HtmlParser.Tag.HTML, html.tagName());
     assertEquals(3, html.childSize());

     Element head = html.child(0);
     assertEquals(HtmlParser.Tag.HEAD, head.tagName());
     assertEquals(1, head.childSize());

     Element title = head.child(0);
     assertEquals(HtmlParser.Tag.TITLE, title.tagName());
     assertEquals("Test", title.text());

     Element body = html.child(1);
     assertEquals(HtmlParser.Tag.BODY, body.tagName());
     assertEquals(4, body.childSize());

     Element p = body.child(0);
     assertEquals(HtmlParser.Tag.P, p.tagName());
     assertEquals(1, p.childSize());

     Element textarea = p.child(0);
     assertEquals(HtmlParser.Tag.TEXTAREA, textarea.tagName());
     assertEquals("test", textarea.attr("name"));
     assertEquals("test", textarea.text());

     Element script = body.child(2);
     assertEquals(HtmlParser.Tag.SCRIPT, script.tagName());
     assertNotNull(script.attr("src"));

     Element base = body.child(3);
     assertEquals(HtmlParser.Tag.BASE, base.tagName());
     assertEquals("http://test.com/test/", base.absUrl("href"));

     Element img = body.child(4);
     assertEquals(HtmlParser.Tag.IMG, img.tagName());
     assertEquals("image.png", img.attr("src"));
     assertEquals("image", img.attr("alt"));
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullHtml() {
     Parser.parse(null, "http://test.com");
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullBaseUri() {
     Parser.parse("<html></html>", null);
 }

 @Test
 public void testParseBodyFragment() {
     Document doc = Parser.parseBodyFragment("<p><textarea name='test'>test</textarea></p>", "http://test.com");
     assertEquals(HtmlParser.Tag.BODY, doc.tagName());
     assertEquals(1, doc.childSize());

     Element html = doc.body();
     assertEquals(HtmlParser.Tag.BODY, html.tagName());
     assertEquals(1, html.childSize());

     Element p = html.child(0);
     assertEquals(HtmlParser.Tag.P, p.tagName());
     assertEquals(1, p.childSize());

     Element textarea = p.child(0);
     assertEquals(HtmlParser.Tag.TEXTAREA, textarea.tagName());
     assertEquals("test", textarea.text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
     Document doc = Parser.parseBodyFragmentRelaxed("<p><textarea name='test'>test</textarea></p>", "http://test.com");
     assertEquals(HtmlParser.Tag.BODY, doc.tagName());
     assertEquals(1, doc.childSize());

     Element html = doc.body();
     assertEquals(HtmlParser.Tag.BODY, html.tagName());
     assertEquals(1, html.childSize());

     Element p = html.child(0);
     assertEquals(HtmlParser.Tag.P, p.tagName());
     assertEquals(1, p.childSize());

     Element textarea = p.child(0);
     assertEquals(HtmlParser.Tag.TEXTAREA, textarea.tagName());
     assertEquals("test", textarea.text());
 }

 @Test
 public void testParseComment() {
     Document doc = Parser.parse("<html><!-- test comment --></html>", "http://test.com");
     Comment comment = (Comment) doc.body().child(0);
     assertEquals(" test comment ", comment.text());
 }

 @Test
 public void testParseXmlDecl() {
     Document doc = Parser.parse("<html xmlns=\"test\" xml:lang=\"en-US\"><head><title>Test</title></head><body></body></html>", "http://test.com");
     XmlDeclaration xmlDecl = (XmlDeclaration) doc.body().child(0);
     assertEquals("test", xmlDecl.xmlNs());
     assertEquals("en-US", xmlDecl.xmlLang());
 }

 @Test
 public void testParseEndTag() {
     Document doc = Parser.parse("<html><body><p><textarea name='test'>test</textarea></p></body></html>", "http://test.com");
     Element textarea = doc.body().child(1).child(0);
     Element parent = textarea.parent();
     parent.removeChild(textarea);

     Element expectedTag = doc.body().child(1);
     Element actualTag = textarea.parent();
     assertEquals(expectedTag.tagName(), actualTag.tagName());
 }

 @Test
 public void testParseStartTag() {
     Document doc = Parser.parse("<html><body><p><textarea name='test'>test</textarea></p></body></html>", "http://test.com");
     Element p = doc.body().child(1);
     Element textarea = new Element(HtmlParser.Tag.TEXTAREA, "http://test.com");
     textarea.putAttrs(p.attributes());
     textarea.text("test");

     assertEquals(p.childSize(), textarea.childSize());
     assertEquals(p.child(0), textarea);
 }

 @Test
 public void testParseAttribute() {
     Document doc = Parser.parse("<html><body><p><textarea name='test'>test</textarea></p></body></html>", "http://test.com");
     Element textarea = doc.body().child(1).child(0);
     Attribute attr = textarea.attr("name");
     assertEquals("test", attr.getValue());
     assertEquals("name", attr.getKey());
 }

 @Test
 public void testParseTextNode() {
     Document doc = Parser.parse("<html><body><p><textarea name='test'>test<img src=\"image.png\"></textarea></p></body></html>", "http://test.com");
     Element textarea = doc.body().child(1).child(0);
     String expectedText = "test<img src=\"image.png\"";
     String actualText = textarea.text();
     assertEquals(expectedText, actualText);
 }

 @Test
 public void testParseCdata() {
     Document doc = Parser.parse("<html><body><![CDATA[<script>alert(\"test\");</script>]]></body></html>", "http://test.com");
     DataNode cdata = (DataNode) doc.body().child(0);
     assertEquals("[CDATA[<script>alert(\"test\");</script>]", cdata.data());
 }
}