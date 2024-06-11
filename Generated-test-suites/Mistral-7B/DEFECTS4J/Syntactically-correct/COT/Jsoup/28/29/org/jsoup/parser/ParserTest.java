import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import java.util.List;

public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;

 @Before
 public void setup() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = parser.parseInput(html, "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals("Hello World", doc.select("h1").text());
 }

 @Test
 public void testGetTreeBuilder() {
 Assert.assertSame(treeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 Assert.assertSame(newTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 Assert.assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsTrue() {
 parser.setTrackErrors(5);
 Assert.assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsZero() {
 parser.setTrackErrors(0);
 Assert.assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testGetErrorsEmpty() {
 Assert.assertNotNull(parser.getErrors());
 Assert.assertEquals(0, parser.getErrors().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullHtml() {
 parser.parseInput(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullBaseUri() {
 parser.parseInput("<html><body><h1>Hello World</h1></body></html>", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputEmptyHtml() {
 parser.parseInput("", "http://example.com");
 }

 @Test(expected = ParseError.class)
 public void testParseInputInvalidHtml() {
 parser.setTrackErrors(5);
 String invalidHtml = "<html<body>";
 Document doc = parser.parseInput(invalidHtml, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("<html<body>", errors.get(0).getMessage());
 }

 @Test(expected = NullPointerException.class)
 public void testParseInputNullTreeBuilder() {
 ParseErrorList errors = ParseErrorList.tracking(5);
 Parser parser = new Parser(null);
 parser.parseInput("<html><body><h1>Hello World</h1></body></html>", "http://example.com", errors);
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<p>Fragment</p>";
 List<Node> nodes = Parser.parseFragment(fragmentHtml, null, "http://example.com");
 Assert.assertFalse(nodes.isEmpty());
 Element p = (Element) nodes.get(0);
 Assert.assertEquals("Fragment", p.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullFragmentHtml() {
 Parser.parseFragment(null, null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullContext() {
 Parser.parseFragment("<p>Fragment</p>", null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullBaseUri() {
 Parser.parseFragment("<p>Fragment</p>", null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testParseFragmentNullTreeBuilder() {
 List<Node> nodes = Parser.parseFragment("<p>Fragment</p>", null, "http://example.com", ParseErrorList.noTracking());
 Parser parser = new Parser(null);
 parser.parseFragment("<p>Fragment</p>", null, "http://example.com", ParseErrorList.noTracking());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<p>Body Fragment</p>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 Element body = doc.body();
 List<Node> nodes = body.childNodes();
 Assert.assertFalse(nodes.isEmpty());
 Element p = (Element) nodes.get(0);
 Assert.assertEquals("Body Fragment", p.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentNullBodyHtml() {
 Parser.parseBodyFragment(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentNullBaseUri() {
 Parser.parseBodyFragment("<p>Body Fragment</p>", null);
 }

 @Test(expected = NullPointerException.class)
 public void testParseBodyFragmentNullTreeBuilder() {
 Document doc = Parser.parseBodyFragment("<p>Body Fragment</p>", "http://example.com", ParseErrorList.noTracking());
 Parser parser = new Parser(null);
 parser.parseBodyFragment("<p>Body Fragment</p>", "http://example.com", ParseErrorList.noTracking());
 }

 @Test
 public void testParseInvalidBodyFragment() {
 Parser.setTrackErrors(5);
 String invalidBodyHtml = "<html<body>";
 Document doc = Parser.parseBodyFragment(invalidBodyHtml, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("<html<body>", errors.get(0).getMessage());
 }
}