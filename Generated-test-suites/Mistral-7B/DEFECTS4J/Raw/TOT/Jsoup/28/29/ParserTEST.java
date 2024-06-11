package org.jsoup.parser;

import org.junit.Assert; import org.junit.Test; import org.jsoup.Jsoup; import org.jsoup.nodes.Document; import org.jsoup.nodes.Element; import org.jsoup.nodes.ParseError; import org.jsoup.parser.Parser;

public class ParserTest {

 private Parser parser;

 @Test
 public void testParseInput() {
 // Test parsing with default settings
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = parser.parseInput(html, "http://example.com");
 Element h1 = doc.select("h1").first();
 Assert.assertEquals("Hello World", h1.text());

 // Test parsing with error tracking enabled
 maxErrors = 3;
 parser = new Parser(new TreeBuilder()); // create a new parser instance with error tracking enabled
 List<ParseError> errors = parser.getErrors();
 doc = parser.parseInput(html + "<invalid>", "http://example.com");
 Assert.assertEquals(1, errors.size()); // one error expected
 Assert.assertEquals("Error parsing start tag <invalid>", errors.get(0).getMessage()); // error message should match

 // Test parsing with error tracking disabled
 maxErrors = 0;
 parser = new Parser(new TreeBuilder()); // create a new parser instance with error tracking disabled
 errors = parser.getErrors();
 doc = parser.parseInput(html + "<invalid>", "http://example.com");
 Assert.assertNull(errors); // no errors expected
 }

 @Test
 public void testSetTreeBuilder() {
 // Test setting TreeBuilder
 parser = new Parser(new HtmlTreeBuilder());
 Parser newParser = parser.setTreeBuilder(new XmlTreeBuilder());
 Assert.assertNotSame(parser.getTreeBuilder(), newParser.getTreeBuilder()); // new parser instance should have different TreeBuilder
 }

 @Test
 public void testIsTrackErrors() {
 // Test getTrackErrors state
 parser = new Parser(new TreeBuilder());
 Assert.assertFalse(parser.isTrackErrors());
 parser.setTrackErrors(2);
 Assert.assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 // Test setTrackErrors state
 parser = new Parser(new TreeBuilder());
 Assert.assertEquals(Parser.DEFAULT_MAX_ERRORS, parser.getMaxErrors());
 parser.setTrackErrors(0);
 Assert.assertEquals(0, parser.getMaxErrors());
 parser.setTrackErrors(3);
 Assert.assertEquals(3, parser.getMaxErrors());
 }

 @Test
 public void testGetErrors() {
 // Test getErrors with no errors
 parser = new Parser(new TreeBuilder());
 List<ParseError> errors = parser.getErrors();
 Assert.assertNull(errors);

 // Test getErrors with errors
 parser = new Parser(new TreeBuilder());
 parser.parseInput("<html><body><script></script><div></div></body></html>", "http://example.com");
 errors = parser.getErrors();
 Assert.assertNotNull(errors);
 Assert.assertEquals(1, errors.size()); // one error expected
 }

 @Test
 public void testParse() {
 // Test static parse method
 Document doc = Parser.parse("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
 Element h1 = doc.select("h1").first();
 Assert.assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseFragment() {
 // Test static parseFragment method
 Element context = Jsoup.parse("<div id='container'></div>").getElementById("container");
 List<Node> nodes = Parser.parseFragment("<p>Test fragment</p>", context, "http://example.com");
 Assert.assertEquals(1, nodes.size()); // one node expected
 Assert.assertTrue(nodes.get(0) instanceof Element); // node should be an element
 }

 @Test
 public void testParseBodyFragment() {
 // Test static parseBodyFragment method
 Document doc = Parser.parseBodyFragment("<p>Test body fragment</p>", "http://example.com");
 Element p = doc.body().select("p").first();
 Assert.assertEquals("Test body fragment", p.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentNullBaseUri() {
 // Test static parseBodyFragment with null baseUri
 Parser.parseBodyFragment("<p>Test body fragment</p>", null);
 }
}