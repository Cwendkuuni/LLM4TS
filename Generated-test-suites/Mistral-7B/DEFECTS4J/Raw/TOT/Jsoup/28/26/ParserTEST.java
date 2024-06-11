import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseError;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
 String html = "<html><body><p>Test</p></body></html>";
 String baseUri = "http://example.com";
 Document doc = parser.parseInput(html, baseUri);
 Element body = doc.body();
 Element p = body.child(0);
 assertEquals("Test", p.text());
 }

 @Test
 public void testParseInputWithTrackingErrors() {
 String html = "<html><body><script>alert('Hello World')</script></body></html>";
 String baseUri = "http://example.com";
 Parser parserWithTrackingErrors = new Parser(treeBuilder).setTrackErrors(1);
 List<ParseError> errors = parserWithTrackingErrors.parseInput(html, baseUri).getErrors();
 assertEquals(1, errors.size());
 assertNotNull(errors.get(0).getLine());
 assertEquals("Error parsing start tag : script", errors.get(0).getMessage());
 }

 @Test
 public void testParseInputEmptyString() {
 String baseUri = "http://example.com";
 Document doc = parser.parseInput("", baseUri);
 assertNull(doc);
 }

 @Test
 public void testParseInputNullHtml() {
 String baseUri = "http://example.com";
 try {
 parser.parseInput(null, baseUri);
 fail("ParseInput with null HTML should throw an exception.");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testParseInputWithSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 Parser parserWithNewTreeBuilder = new Parser(newTreeBuilder);
 assertEquals(newTreeBuilder, parserWithNewTreeBuilder.getTreeBuilder());
 }

 @Test
 public void testParseInputWithSetMaxErrors() {
 Parser parserWithMaxErrors = new Parser(treeBuilder).setTrackErrors(10);
 assertEquals(10, parserWithMaxErrors.getMaxErrors());
 }

 @Test
 public void testParseInputWithMaxErrorsReached() {
 String html = "<html><body><script>alert('Hello World')</script><script>alert('Hello Again')</script></body></html>";
 String baseUri = "http://example.com";
 Parser parserWithMaxErrors = new Parser(treeBuilder).setTrackErrors(1);
 List<ParseError> errors = parserWithMaxErrors.parseInput(html, baseUri).getErrors();
 assertEquals(1, errors.size());
 assertNotNull(errors.get(0).getLine());
 assertEquals("Error parsing start tag : script", errors.get(0).getMessage());
 }

 @Test
 public void testParseInputWithMaxErrorsExceeded() {
 String html = "<html><body><script>alert('Hello World')</script><script>alert('Hello Again')</script></body></html>";
 String baseUri = "http://example.com";
 Parser parserWithMaxErrors = new Parser(treeBuilder).setTrackErrors(-1);
 List<ParseError> errors = parserWithMaxErrors.parseInput(html, baseUri).getErrors();
 assertFalse(errors.isEmpty());
 }

 @Test
 public void testParseStaticFunction() {
 String html = "<html><body><p>Test</p></body></html>";
 Document doc = Parser.parse(html);
 Element body = doc.body();
 Element p = body.child(0);
 assertEquals("Test", p.text());
 }

 @Test
 public void testParseStaticFunctionWithTrackingErrors() {
 String html = "<html><body><script>alert('Hello World')</script></body></html>";
 Parser parserWithTrackingErrors = new Parser(treeBuilder).setTrackErrors(1);
 List<ParseError> errors = Parser.parse(html, null).getErrors();
 assertEquals(1, errors.size());
 assertNotNull(errors.get(0).getLine());
 assertEquals("Error parsing start tag : script", errors.get(0).getMessage());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<p>Test</p>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 Element body = doc.body();
 Element p = body.child(0);
 assertEquals("Test", p.text());
 }

 @Test
 public void testParseBodyFragmentWithTrackingErrors() {
 String bodyHtml = "<script>alert('Hello World')</script>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 List<ParseError> errors = doc.getErrors();
 assertEquals(1, errors.size());
 assertNotNull(errors.get(0).getLine());
 assertEquals("Error parsing start tag : script", errors.get(0).getMessage());
 }

 @Test
 public void testParseBodyFragmentWithEmptyString() {
 Document doc = Parser.parseBodyFragment("", "http://example.com");
 assertNull(doc);
 }

 @Test
 public void testParseBodyFragmentWithNullHtml() {
 try {
 Parser.parseBodyFragment(null, "http://example.com");
 fail("ParseBodyFragment with null HTML should throw an exception.");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<p>Test</p>";
 Element context = Jsoup.parse("<div><p>Context</p></div>").body();
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
 assertEquals(1, nodes.size());
 Element p = (Element) nodes.get(0);
 assertEquals("Test", p.text());
 }

 @Test
 public void testParseFragmentWithTrackingErrors() {
 String fragmentHtml = "<script>alert('Hello World')</script>";
 Element context = Jsoup.parse("<div><p>Context</p></div>").body();
 List<ParseError> errors = Parser.parseFragment(fragmentHtml, context, "http://example.com").getErrors();
 assertEquals(1, errors.size());
 assertNotNull(errors.get(0).getLine());
 assertEquals("Error parsing start tag : script", errors.get(0).getMessage());
 }

 @Test
 public void testParseFragmentWithEmptyString() {
 List<Node> nodes = Parser.parseFragment("", context, "http://example.com");
 assertEquals(0, nodes.size());
 }

 @Test
 public void testParseFragmentWithNullHtml() {
 try {
 List<Node> nodes = Parser.parseFragment(null, context, "http://example.com");
 fail("ParseFragment with null HTML should throw an exception.");
 } catch (NullPointerException e) {
 // expected
 }
 }
}