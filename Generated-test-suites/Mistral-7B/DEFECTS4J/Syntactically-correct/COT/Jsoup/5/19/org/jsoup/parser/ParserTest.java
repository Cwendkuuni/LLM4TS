package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test class for Parser class in org.jsoup.parser package
 */
public class ParserTest {

 @Test
 public void testParse() {
 Parser parser = new Parser("", "", false);
 Document doc = parser.parse();
 assertEquals("html", doc.tagName());
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<html><body><div>Test</div></body></html>", "", true);
 Document doc = parser.parse();
 Element body = doc.body();
 assertEquals(1, body.childNodeSize());
 assertEquals("div", body.child(0).tagName());
 assertEquals("Test", body.child(0).text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Parser parser = new Parser("<html><body><div><div>Test</div></div></body></html>", "", true);
 Document doc = parser.parse();
 Element body = doc.body();
 assertEquals(1, body.childNodeSize());
 Element div = body.child(0);
 assertEquals(1, div.childNodeSize());
 assertEquals("div", div.tagName());
 assertEquals("Test", div.child(0).text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullHtml() {
 Parser parser = new Parser(null, "", false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 Parser parser = new Parser("<html></html>", null, false);
 }

 @Test
 public void testParseWithEmptyHtml() {
 Parser parser = new Parser("", "", false);
 Document doc = parser.parse();
 assertEquals(0, doc.childNodeSize());
 }

 @Test
 public void testParseWithEmptyBodyFragment() {
 Parser parser = new Parser("", "", true);
 Document doc = parser.parse();
 assertEquals(1, doc.childNodeSize());
 assertNotNull(doc.body());
 assertEquals(0, doc.body().childNodeSize());
 }

 @Test
 public void testParseWithHtmlStringContainingCommentAndCdata() {
 Parser parser = new Parser("<html><!-- test comment --><![CDATA[test cdata]]></html>", "", false);
 Document doc = parser.parse();
 assertEquals(1, doc.childNodeSize());
 Element html = doc.child(0);
 assertEquals(2, html.childNodeSize());
 Element comment = (Comment) html.child(0);
 assertEquals("test comment", comment.data());
 Element cdata = (DataNode) html.child(1);
 assertEquals("test cdata", cdata.text());
 }

 @Test
 public void testParseWithXmlDeclaration() {
 Parser parser = new Parser("<html xmlns:my=\"http://example.com\"><head></head><body></body></html>", "", false);
 Document doc = parser.parse();
 XmlDeclaration xmlDeclaration = (XmlDeclaration) doc.child(0);
 assertTrue(xmlDeclaration.isStandalone());
 assertTrue(xmlDeclaration.isVersionSpecified());
 assertEquals("1.0", xmlDeclaration.getVersion());
 }

 @Test
 public void testParseWithTagAttributes() {
 Parser parser = new Parser("<html><body><a href=\"test.html\" title=\"Test Title\" id=\"test-id\"><img src=\"test-image.jpg\" alt=\"Test Image\"></a></body></html>", "", false);
 Document doc = parser.parse();
 Element a = doc.body().child(0);
 assertEquals(4, a.attributeSize());
 Attribute href = a.attr("href");
 assertEquals("test.html", href.getValue());
 Attribute title = a.attr("title");
 assertEquals("Test Title", title.getValue());
 Attribute id = a.attr("id");
 assertEquals("test-id", id.getValue());
 Attribute imgSrc = a.getElementsByTag("img").get(0).attr("src");
 assertEquals("test-image.jpg", imgSrc.getValue());
 Attribute imgAlt = a.getElementsByTag("img").get(0).attr("alt");
 assertEquals("Test Image", imgAlt.getValue());
 }

 @Test
 public void testParseWithEmptyTag() {
 Parser parser = new Parser("<html><body><img/></body></html>", "", false);
 Document doc = parser.parse();
 Element img = doc.body().child(0);
 assertTrue(img.isEmpty());
 }

 @Test
 public void testParseWithSelfClosingTag() {
 Parser parser = new Parser("<html><body><img src=\"test.jpg\" /></body></html>", "", false);
 Document doc = parser.parse();
 Element img = doc.body().child(0);
 assertTrue(img.isSelfClosing());
 }

 @Test
 public void testParseWithImplicitParentTag() {
 Parser parser = new Parser("<html><body><input type=\"text\"></body></html>", "", false);
 Document doc = parser.parse();
 Element body = doc.body();
 Element input = body.child(0);
 assertEquals("input", input.tagName());
 Element form = body.getParent();
 assertEquals("form", form.tagName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidParentTag() {
 Parser parser = new Parser("<html><body><header></header></body></html>", "", false);
 }

 @Test
 public void testParseWithInvalidTagInBody() {
 Parser parser = new Parser("<html><body><header></header></body></html>", "", false);
 }

 @Test
 public void testParseWithInvalidTagInHead() {
 Parser parser = new Parser("<html><head><body></body></head></html>", "", false);
 }

 @Test
 public void testParseWithInvalidTagInBodyWithRelaxedParsing() {
 Parser parser = new Parser("<html><body><header></header></body></html>", "", true);
 Document doc = parser.parse();
 Document normalizedDoc = doc.normalise();
 assertEquals(1, normalizedDoc.childNodeSize());
 assertEquals("html", normalizedDoc.tagName());
 }
}