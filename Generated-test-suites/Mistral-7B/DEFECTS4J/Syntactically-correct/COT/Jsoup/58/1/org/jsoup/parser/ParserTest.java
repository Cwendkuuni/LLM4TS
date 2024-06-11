import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.xml.XmlTreeBuilder;

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
 Document doc = parser.parseInput("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals("Hello World", doc.select("h1").text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullHtml() {
 parser.parseInput(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputEmptyHtml() {
 parser.parseInput("", "http://example.com");
 }

 @Test
 public void testParseInputWithTrackingErrors() {
 errors: for (ParseError error : parser.getErrors()) {
 if (error != null) {
 throw new AssertionError("Unexpected error: " + error.toString());
 }
 }
 Document doc = parser.parseInput("<html><body><script>throw new Error('Test Error');</script></body></html>", "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testParseInputWithNoTrackingErrors() {
 Document doc = parser.parseInput("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals("Hello World", doc.select("h1").text());
 Assert.assertNull(parser.getErrors());
 }

 @Test
 public void testParseInputWithXml() {
 Document doc = Parser.parseXmlFragment("<xml><tag>Test</tag></xml>", "http://example.com").get(0).asDocument();
 Assert.assertNotNull(doc);
 Assert.assertEquals("Test", doc.select("tag").text());
 }

 @Test
 public void testParseFragment() {
 List<Node> nodes = Parser.parseFragment("<fragment><a href='http://example.com'>Link</a></fragment>", null, "http://example.com");
 Assert.assertFalse(nodes.isEmpty());
 Assert.assertEquals(1, nodes.size());
 Element link = (Element) nodes.get(0);
 Assert.assertEquals("Link", link.text());
 Assert.assertEquals("http://example.com", link.attr("href"));
 }

 @Test
 public void testParseFragmentWithTrackingErrors() {
 errors: for (ParseError error : parser.getErrors()) {
 if (error != null) {
 throw new AssertionError("Unexpected error: " + error.toString());
 }
 }
 List<Node> nodes = Parser.parseFragment("<fragment><script>throw new Error('Test Error');</script></fragment>", null, "http://example.com");
 Assert.assertFalse(nodes.isEmpty());
 Assert.assertEquals(1, nodes.size());
 Assert.assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testParseFragmentWithNoTrackingErrors() {
 List<Node> nodes = Parser.parseFragment("<fragment><a href='http://example.com'>Link</a></fragment>", null, "http://example.com");
 Assert.assertFalse(nodes.isEmpty());
 Assert.assertEquals(1, nodes.size());
 Element link = (Element) nodes.get(0);
 Assert.assertEquals("Link", link.text());
 Assert.assertEquals("http://example.com", link.attr("href"));
 Assert.assertNull(parser.getErrors());
 }

 @Test
 public void testParseBodyFragment() {
 Document doc = Parser.parseBodyFragment("<body><h1>Hello World</h1></body>", "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals("Hello World", doc.select("h1").text());
 }

 @Test
 public void testParseBodyFragmentWithTrackingErrors() {
 errors: for (ParseError error : parser.getErrors()) {
 if (error != null) {
 throw new AssertionError("Unexpected error: " + error.toString());
 }
 }
 Document doc = Parser.parseBodyFragment("<body><script>throw new Error('Test Error');</script></body>", "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testParseBodyFragmentWithNoTrackingErrors() {
 Document doc = Parser.parseBodyFragment("<body><h1>Hello World</h1></body>", "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals("Hello World", doc.select("h1").text());
 Assert.assertNull(parser.getErrors());
 }

 @Test
 public void testUnescapeEntities() {
 String html = "<div>&lt;p&gt;This is a &amp; test.&lt;/p&gt;</div>";
 String unescaped = Parser.unescapeEntities(html, false);
 Assert.assertEquals("<div><p>This is a test.</p></div>", unescaped);
 }

 @Test
 public void testParseInputWithMinimalErrors() {
 int maxErrors = 1;
 Parser parser = new Parser(treeBuilder).setTrackErrors(maxErrors);
 Document doc = parser.parseInput("<html><body><h1>Hello World</h1><script>throw new Error('Test Error');</script></body></html>", "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals("Hello World", doc.select("h1").text());
 List<ParseError> errors = parser.getErrors();
 Assert.assertFalse(errors.isEmpty());
 Assert.assertTrue(errors.size() <= maxErrors);
 }

 @Test
 public void testParseInputWithMaxErrors() {
 int maxErrors = Integer.MAX_VALUE;
 Parser parser = new Parser(treeBuilder).setTrackErrors(maxErrors);
 Document doc = parser.parseInput("<html><body><script>throw new Error('Test Error 1');</script><script>throw new Error('Test Error 2');</script></body></html>", "http://example.com");
 Assert.assertNotNull(doc);
 List<ParseError> errors = parser.getErrors();
 Assert.assertFalse(errors.isEmpty());
 Assert.assertTrue(errors.size() > Integer.MAX_VALUE / 2);
 }
}