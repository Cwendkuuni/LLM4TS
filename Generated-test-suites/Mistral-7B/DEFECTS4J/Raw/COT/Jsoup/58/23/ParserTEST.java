import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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
 String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
 Document doc = parser.parseInput(html, "http://example.com");
 assertNotNull(doc);
 assertEquals("Test", doc.title().text());
 assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testParseInputWithTrackingErrors() {
 String html = "<html><head><title>Test</title></head><body><p>Hello World</p><script>throw new Error('Test')</script></body></html>";
 parser = new Parser(treeBuilder).setTrackErrors(1);
 Document doc = parser.parseInput(html, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 assertNotNull(doc);
 assertEquals("Test", doc.title().text());
 assertEquals("Hello World", doc.body().text());
 assertTrue(errors.size() > 0);
 assertEquals("Error: Test", errors.get(0).message());
 }

 @Test(expected = RuntimeException.class)
 public void testParseInputWithTrackingErrorsAndMaxErrorsReached() {
 String html = "<html><head><title>Test</title></head><body><p>Hello World</p><script>throw new Error('Test')</script></body></html>";
 parser = new Parser(treeBuilder).setTrackErrors(1);
 Document doc1 = parser.parseInput(html, "http://example.com");
 List<ParseError> errors1 = parser.getErrors();
 assertEquals("Test", doc1.title().text());
 assertEquals("Hello World", doc1.body().text());
 assertTrue(errors1.size() > 0);
 assertEquals("Error: Test", errors1.get(0).message());

 String html2 = "<html><head><title>Test</title></head><body><p>Hello World</p><script>throw new Error('Test')</script></body></html>";
 Document doc2 = parser.parseInput(html2, "http://example.com");
 List<ParseError> errors2 = parser.getErrors();
 assertNotNull(doc2);
 assertNull(doc2.title()); // parse error occurred, title should be null
 assertFalse(doc2.body().text().isEmpty()); // parse error occurred, body text should not be empty
 assertTrue(errors2.size() > 0);
 assertEquals("Error: Test", errors2.get(0).message());
 }

 @Test
 public void testGetTreeBuilder() {
 assertEquals(treeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 assertEquals(newTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 assertFalse(parser.isTrackErrors());
 parser = new Parser(treeBuilder).setTrackErrors(1);
 assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsAndGetErrors() {
 parser = new Parser(treeBuilder).setTrackErrors(1);
 String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
 Document doc = parser.parseInput(html, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 assertNotNull(doc);
 assertEquals("Test", doc.title().text());
 assertEquals("Hello World", doc.body().text());
 assertTrue(errors.size() > 0);
 assertEquals("", errors.get(0).message()); // default message when no error is thrown
 }

 @Test
 public void testParseStaticMethods() {
 String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 assertNotNull(doc);
 assertEquals("Test", doc.title().text());
 assertEquals("Hello World", doc.body().text());
 }

 @Test(expected = RuntimeException.class)
 public void testParseFragmentWithEmptyFragment() {
 String fragmentHtml = "";
 Element context = Jsoup.parse("<html><body></body></html>").body();
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
 assertNotNull(nodes);
 assertTrue(nodes.isEmpty());
 }

 @Test(expected = RuntimeException.class)
 public void testParseFragmentWithMalformedHtml() {
 String fragmentHtml = "<html><head><title>Test</title>";
 Element context = Jsoup.parse("<html><body></body></html>").body();
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
 assertNotNull(nodes);
 assertTrue(nodes.isEmpty());
 }

 @Test
 public void testParseXmlFragment() {
 String fragmentXml = "<xml><item><name>Apple</name><quantity>1</quantity></item></xml>";
 List<Node> nodes = Parser.parseXmlFragment(fragmentXml, "http://example.com");
 assertNotNull(nodes);
 assertFalse(nodes.isEmpty());
 Node appleItem = nodes.get(0);
 assertEquals("item", appleItem.tagName());
 Element nameElement = (Element) appleItem.child(0);
 assertEquals("name", nameElement.tagName());
 assertEquals("Apple", nameElement.text());
 Element quantityElement = (Element) appleItem.child(1);
 assertEquals("quantity", quantityElement.tagName());
 assertEquals("1", quantityElement.text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><p>Hello World</p></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 assertNotNull(doc);
 assertNotNull(doc.body());
 assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testUnescapeEntities() {
 String input = "&lt;p&gt;Hello World&amp;&#39;s Test&amp;lt;/p&gt;";
 String output = Parser.unescapeEntities(input, false);
 assertEquals("<p>Hello World&amp;&rsquo;s Test</p>", output);
 String input2 = "&amp;lt;p&gt;Hello World&amp;&#39;s Test&amp;lt;/p&gt;";
 String output2 = Parser.unescapeEntities(input2, true);
 assertEquals("<p>Hello World&apos;s Test</p>", output2);
 }

}