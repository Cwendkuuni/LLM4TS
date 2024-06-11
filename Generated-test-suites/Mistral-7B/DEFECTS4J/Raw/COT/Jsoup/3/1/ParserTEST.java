package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.LinkedList;

public class ParserTest {

 private Parser createParser(String html, String baseUri) {
 Validate.notNull(html);
 Validate.notNull(baseUri);
 return new Parser(html, baseUri, false);
 }

 @Test
 public void testParse() throws Exception {
 Document doc = Parser.parse("<html><head><title>Test Title</title></head><body><h1>Test Title</h1></body></html>", "http://test.com");
 Equals verifier = Equals.equalTo(doc);
 Equals headVerifier = Equals.equalTo(doc.head());
 Equals titleVerifier = Equals.equalTo(doc.title());
 Equals bodyVerifier = Equals.equalTo(doc.body());
 Equals h1Verifier = Equals.equalTo(doc.select("h1").first());

 verifier.isTrue();
 headVerifier.isTrue();
 titleVerifier.isTrue();
 titleVerifier.getString().isEqualTo("Test Title");
 bodyVerifier.isTrue();
 h1Verifier.isTrue();
 h1Verifier.getString().isEqualTo("Test Title");
 }

 @Test(expected = NullPointerException.class)
 public void testNullHtml() throws Exception {
 Parser parser = new Parser(null, "http://test.com", false);
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() throws Exception {
 Parser parser = new Parser("<html></html>", null, false);
 }

 @Test
 public void testParseBodyFragment() throws Exception {
 Document doc = Parser.parseBodyFragment("<body><h1>Test Title</h1></body>", "http://test.com");
 Equals bodyVerifier = Equals.equalTo(doc);
 Equals h1Verifier = Equals.equalTo(doc.select("h1").first());

 bodyVerifier.isTrue();
 h1Verifier.isTrue();
 h1Verifier.getString().isEqualTo("Test Title");
 }

 @Test
 public void testEmptyInput() throws Exception {
 Document doc = Parser.parse("", "http://test.com");
 assertNull(doc);
 }

 @Test
 public void testParseComment() throws Exception {
 Parser parser = createParser("<!DOCTYPE html><html><!-- This is a comment --><body></body></html>", "http://test.com");
 Document doc = parser.parse();
 Element comment = doc.select("html() comment()").first();
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseCdata() throws Exception {
 Parser parser = createParser("<html><![CDATA[This is some CDATA]]><body></body></html>", "http://test.com");
 Document doc = parser.parse();
 Element cdata = doc.select("html() cdata()").first();
 assertEquals("This is some CDATA", cdata.text());
 }

 @Test
 public void testParseXmlDecl() throws Exception {
 Parser parser = createParser("<html xmlns:foo=\"bar\"><xml xml:version=\"1.0\"></xml>", "http://test.com");
 Document doc = parser.parse();
 XmlDeclaration xmlDecl = doc.select("html() xmlDeclaration()").first();
 assertEquals("1.0", xmlDecl.xmlVersion());
 }

 @Test
 public void testParseEndTag() throws Exception {
 Parser parser = createParser("<html><body><p><div></div></p></body></html>", "http://test.com");
 Document doc = parser.parse();
 Element div = doc.select("div").first();
 parser.parseEndTag();
 assertEquals(0, doc.body().childSize());
 assertNull(div);
 }

 @Test
 public void testParseStartTag() throws Exception {
 Parser parser = createParser("<html><body><p><a href=\"http://test.com\">Link</a></p></body></html>", "http://test.com");
 Document doc = parser.parse();
 Element link = doc.select("a").first();
 assertEquals("Link", link.text());
 assertEquals("http://test.com", link.absUrl("href"));
 }

 @Test
 public void testParseStartTagWithData() throws Exception {
 Parser parser = createParser("<html><body><textarea>Hello World</textarea></body></html>", "http://test.com");
 Document doc = parser.parse();
 Element textarea = doc.select("textarea").first();
 assertEquals("Hello World", textarea.text());
 }

 @Test
 public void testParseStartTagWithEmptyTag() throws Exception {
 Parser parser = createParser("<html><body><img src=\"image.png\"/></body></html>", "http://test.com");
 Document doc = parser.parse();
 Element img = doc.select("img").first();
 assertEquals(0, img.childSize());
 }

 @Test
 public void testParseStartTagWithImplicitParent() throws Exception {
 Parser parser = createParser("<html><body><script src=\"script.js\"></script></body></html>", "http://test.com");
 Document doc = parser.parse();
 Element script = doc.select("script").first();
 assertEquals(0, script.childSize());
 }

 @Test
 public void testParseStartTagWithInvalidParent() throws Exception {
 Parser parser = createParser("<html><body><div><p><div></div></p></body></html>", "http://test.com");
 Document doc = parser.parse();
 Element div1 = doc.select("div").first();
 parser.parseStartTag("li");
 Element div2 = doc.select("div").first();
 assertEquals(div1, div2.parent());
 }

 @Test
 public void testParseStartTagWithMultipleAttributes() throws Exception {
 Parser parser = createParser("<html><body><a href=\"http://test.com\" class=\"test\" title=\"Test Title\"></a></body></html>", "http://test.com");
 Document doc = parser.parse();
 Element link = doc.select("a").first();
 assertEquals("http://test.com", link.absUrl("href"));
 assertEquals("Test Title", link.attr("title"));
 assertEquals("test", link.attr("class"));
 }

 @Test
 public void testParseTextNode() throws Exception {
 Parser parser = createParser("<html><body><p>Hello World</p></body></html>", "http://test.com");
 Document doc = parser.parse();
 Element p = doc.select("p").first();
 assertEquals("Hello World", p.text());
 }
}