package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.*;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setUp() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello <world></h1></body></html>";
 parser = new Parser(html, "http://test.com", false);
 doc = parser.parse();
 }

 @Test
 public void testParse() {
 assertEquals("Test", doc.title());
 assertEquals("Hello <world>", doc.body().text());
 }

 @Test
 public void testParseBodyFragment() {
 "<html><body><h1>Test</h1></body>".endsWith("html>body>");
 String bodyHtml = "<html><body><h1>Test</h1></body>";
 Document docBody = Parser.parseBodyFragment(bodyHtml, "http://test.com");
 assertEquals("Test", docBody.body().text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullHtml() {
 parser = new Parser(null, "http://test.com", false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullBaseUri() {
 parser = new Parser("<html></html>", null, false);
 }

 @Test
 public void testParseComment() {
 String html = "<html><!-- This is a comment --></html>";
 parser = new Parser(html, "http://test.com", false);
 doc = parser.parse();
 assertEquals("This is a comment", doc.body().child(0).child(0).text());
 }

 @Test
 public void testParseXmlDecl() {
 String html = "<html xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' version='1.0' encoding='UTF-8'>\n";
 parser = new Parser(html, "http://test.com", false);
 doc = parser.parse();
 assertEquals("UTF-8", doc.xmlDeclaration().encoding());
 }

 @Test
 public void testParseEndTag() {
 String html = "<html><body><p><div></div></p></body></html>";
 parser = new Parser(html, "http://test.com", false);
 doc = parser.parse();
 assertEquals(3, doc.body().child(0).childCount());
 }

 @Test
 public void testParseStartTag() {
 String html = "<html><body><p><a href='test.html' title='Link'>Link</a></p></body></html>";
 parser = new Parser(html, "http://test.com", false);
 doc = parser.parse();
 assertEquals(4, doc.body().child(0).childCount());
 assertEquals("Link", doc.body().child(0).child(1).text());
 assertEquals("test.html", doc.body().child(0).child(1).attr("href"));
 }

 @Test
 public void testParseAttribute() {
 String html = "<html><body><p><input type='text' name='username' value='test'></p></body></html>";
 parser = new Parser(html, "http://test.com", false);
 doc = parser.parse();
 assertEquals("test", doc.body().child(0).child(0).attr("value"));
 }

 @Test
 public void testParseTextNode() {
 String html = "<html><body><p>Test</p></body></html>";
 parser = new Parser(html, "http://test.com", false);
 doc = parser.parse();
 assertEquals("Test", doc.body().child(0).text());
 }

 @Test
 public void testParseCdata() {
 String html = "<html><body><p><![CDATA[ Test ]]></p></body></html>";
 parser = new Parser(html, "http://test.com", false);
 doc = parser.parse();
 assertEquals("Test", doc.body().child(0).child(0).text());
 }

 @Test(expected = IllegalStateException.class)
 public void testParseInvalidTagOrder() {
 String html = "<html><body><div><p></p></div></body></html>";
 parser = new Parser(html, "http://test.com", false);
 doc = parser.parse();
 }

 @Test
 public void testParseEmptyElement() {
 String html = "<html><body><img src='test.png'></body></html>";
 parser = new Parser(html, "http://test.com", false);
 doc = parser.parse();
 assertTrue(doc.body().child(0) instanceof Element);
 assertTrue(doc.body().child(0).tag().isData());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseUnknownTag() {
 String html = "<html><body><x></x></body></html>";
 parser = new Parser(html, "http://test.com", false);
 doc = parser.parse();
 }

 @Test
 public void testParseInvalidBaseUri() {
 String html = "<html><base href='/test'></html>";
 parser = new Parser("<html></html>", "http://test.com", false);
 doc = parser.parse();
 doc.setBaseUri("/test");
 assertEquals("test", doc.baseUri());
 }

 @Test
 public void testParseImplicitParent() {
 String html = "<html><body><div></div><world></world></body></html>";
 parser = new Parser(html, "http://test.com", false);
 doc = parser.parse();
 assertEquals(3, doc.childCount());
 assertEquals("world", doc.body().child(2).tagName());
 }

 @Test
 public void testParseInvalidParent() {
 String html = "<html><body><p><ul></ul></p></body></html>";
 parser = new Parser(html, "http://test.com", false);
 doc = parser.parse();
 assertEquals(1, doc.body().childCount());
 }

 @After
 public void tearDown() {
 parser = null;
 doc = null;
 }

}