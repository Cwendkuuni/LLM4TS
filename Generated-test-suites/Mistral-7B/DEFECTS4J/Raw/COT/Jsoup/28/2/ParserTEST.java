import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;

 @Before
 public void setUp() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://example.com";
 Document doc = parser.parseInput(html, baseUri);
 Assert.assertNotNull(doc);
 Assert.assertEquals(1, doc.numHeadElements());
 Assert.assertEquals(1, doc.numBodyElements());
 Assert.assertEquals("Hello World", doc.select("h1").text());
 }

 @Test
 public void testParseInputWithTrackErrors() {
 String html = "<html><body><h1>Hello World</h1><script>alert('Error');</script></body></html>";
 parser.setTrackErrors(1);
 Document doc = parser.parseInput(html, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Encountered a script tag in <html>, but scripts are not allowed.", errors.get(0).getMessage());
 }

 @Test
 public void testParseInputWithMaxErrorsReached() {
 String html = "<html><body>";
 for (int i = 0; i < Parser.DEFAULT_MAX_ERRORS + 1; i++) {
 html += "<script>alert('Error');</script>";
 }
 String baseUri = "http://example.com";
 parser.setTrackErrors(Parser.DEFAULT_MAX_ERRORS);
 Document doc = null;
 try {
 doc = parser.parseInput(html, baseUri);
 } catch (Exception e) {
 // expected
 }
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(Parser.DEFAULT_MAX_ERRORS, errors.size());
 }

 @Test
 public void testParseInputWithNullHtml() {
 String html = null;
 String baseUri = "http://example.com";
 try {
 parser.parseInput(html, baseUri);
 Assert.fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testParseInputWithNullBaseUri() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = null;
 try {
 parser.parseInput(html, baseUri);
 Assert.fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testParseInputWithEmptyHtml() {
 String html = "";
 String baseUri = "http://example.com";
 Document doc = parser.parseInput(html, baseUri);
 Assert.assertNotNull(doc);
 Assert.assertEquals(0, doc.numHeadElements());
 Assert.assertEquals(0, doc.numBodyElements());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<h1>Hello World</h1>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals(1, doc.numHeadElements());
 Assert.assertEquals(1, doc.numBodyElements());
 Assert.assertEquals("Hello World", doc.select("h1").text());
 }

 @Test
 public void testParseBodyFragmentWithNullBodyHtml() {
 String bodyHtml = null;
 String baseUri = "http://example.com";
 try {
 Parser.parseBodyFragment(bodyHtml, baseUri);
 Assert.fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testParseBodyFragmentWithEmptyBodyHtml() {
 String bodyHtml = "";
 String baseUri = "http://example.com";
 Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
 Assert.assertNotNull(doc);
 Assert.assertEquals(0, doc.numHeadElements());
 Assert.assertEquals(0, doc.numBodyElements());
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<h1>Hello World</h1>";
 Element context = Jsoup.parse("<html><body></body></html>").body();
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
 Assert.assertNotNull(nodes);
 Assert.assertEquals(1, nodes.size());
 Node node = nodes.get(0);
 Assert.assertEquals("h1", node.tagName());
 Assert.assertEquals("Hello World", node.text());
 }

 @Test
 public void testParseFragmentWithNullFragmentHtml() {
 String fragmentHtml = null;
 Element context = Jsoup.parse("<html><body></body></html>").body();
 String baseUri = "http://example.com";
 try {
 Parser.parseFragment(fragmentHtml, context, baseUri);
 Assert.fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testParseFragmentWithEmptyFragmentHtml() {
 String fragmentHtml = "";
 Element context = Jsoup.parse("<html><body></body></html>").body();
 String baseUri = "http://example.com";
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
 Assert.assertNotNull(nodes);
 Assert.assertEquals(0, nodes.size());
 }

 @Test
 public void testParseFragmentWithNullContext() {
 String fragmentHtml = "<h1>Hello World</h1>";
 Element context = null;
 String baseUri = "http://example.com";
 try {
 Parser.parseFragment(fragmentHtml, context, baseUri);
 Assert.fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testParseFragmentWithNullBaseUri() {
 String fragmentHtml = "<h1>Hello World</h1>";
 Element context = Jsoup.parse("<html><body></body></html>").body();
 String baseUri = null;
 try {
 Parser.parseFragment(fragmentHtml, context, baseUri);
 Assert.fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testHtmlParser() {
 Parser parser = Parser.htmlParser();
 Document doc = parser.parseInput("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
 Assert.assertNotNull(doc);
 }

 @Test
 public void testXmlParser() {
 Parser parser = Parser.xmlParser();
 Document doc = parser.parseInput("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
 Assert.assertNotNull(doc);
 }

 @Rule
 public ExpectedException exception = ExpectedException.none();

 @Test
 public void testParseInputWithScriptTagWithoutTrackErrors() {
 String html = "<html><body><h1>Hello World</h1><script>alert('Error');</script></body></html>";
 parser = new Parser(new HtmlTreeBuilder());
 try {
 parser.parseInput(html, "http://example.com");
 } catch (Exception e) {
 exception.expect(Exception.class);
 exception.expectMessage("Encountered a script tag in <html>, but scripts are not allowed.");
 parser.parseInput(html, "http://example.com");
 }
 }
}