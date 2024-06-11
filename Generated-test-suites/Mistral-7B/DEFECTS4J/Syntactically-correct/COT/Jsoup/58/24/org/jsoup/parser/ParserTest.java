import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;

public class ParserTest {

 private Parser parser;
 private Document document;

 @Test
 public void testParserConstructor() {
 Parser expectedParser = new Parser(new HtmlTreeBuilder());
 Parser actualParser = new Parser(new HtmlTreeBuilder());

 Assert.assertEquals("TreeBuilder should be the same", expectedParser.getTreeBuilder(), actualParser.getTreeBuilder());
 }

 @Test
 public void testParseInputEmptyHtml() {
 parser = new Parser(new HtmlTreeBuilder());
 document = parser.parseInput("", "baseUri");

 Assert.assertNull("Document should be null", document);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullHtml() {
 parser = new Parser(new HtmlTreeBuilder());
 parser.parseInput(null, "baseUri");
 }

 @Test
 public void testParseInputValidHtml() {
 parser = new Parser(new HtmlTreeBuilder());
 document = parser.parseInput("<html><head></head><body><p>Hello World</p></body></html>", "baseUri");

 Assert.assertNotNull("Document should not be null", document);
 Assert.assertEquals("Document title should be empty", "", document.title());
 }

 @Test(expected = ParseError.class)
 public void testParseInputInvalidHtml() {
 parser = new Parser(new HtmlTreeBuilder());
 document = parser.parseInput("<html><body><p>Hello <script>World</script>", "baseUri");

 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals("Number of errors should be 1", 1, errors.size());
 Assert.assertEquals("Error message should be 'Unsupported start tag: script'", "Unsupported start tag: script", errors.get(0).getMessage());
 }

 @Test
 public void testParseInputWithTrackErrors() {
 parser = new Parser(new HtmlTreeBuilder());
 parser.setTrackErrors(5);
 document = parser.parseInput("<html><body><p>Hello <script>World</script>", "baseUri");

 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals("Number of errors should be 1", 1, errors.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetTrackErrorsNegativeNumber() {
 parser = new Parser(new HtmlTreeBuilder());
 parser.setTrackErrors(-5);
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder expectedTreeBuilder = new HtmlTreeBuilder();
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();

 Assert.assertEquals("TreeBuilder should be the same", expectedTreeBuilder, actualTreeBuilder);
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);

 TreeBuilder expectedTreeBuilder = newTreeBuilder;
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();

 Assert.assertEquals("TreeBuilder should be the same", expectedTreeBuilder, actualTreeBuilder);
 }

 @Test
 public void testIsTrackErrors() {
 parser = new Parser(new HtmlTreeBuilder());
 Assert.assertFalse("Should not be tracking errors by default", parser.isTrackErrors());

 parser.setTrackErrors(1);
 Assert.assertTrue("Should be tracking errors", parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsZero() {
 parser = new Parser(new HtmlTreeBuilder());
 parser.setTrackErrors(0);

 Assert.assertFalse("Should not be tracking errors", parser.isTrackErrors());
 }

 @Test
 public void testGetErrorsEmpty() {
 Assert.assertNull("Errors should be null when no errors are tracked", parser.getErrors());
 }

 @Test
 public void testGetErrorsNonEmpty() {
 parser = new Parser(new HtmlTreeBuilder());
 parser.setTrackErrors(5);
 document = parser.parseInput("<html><body><p>Hello <script>World</script>", "baseUri");
 List<ParseError> errors = parser.getErrors();

 Assert.assertEquals("Number of errors should be 1", 1, errors.size());
 }

 @Test
 public void testParseInputWithXml() {
 parser = new Parser(new XmlTreeBuilder());
 document = parser.parseInput("<xml></xml>", "baseUri");

 Assert.assertNotNull("Document should not be null", document);
 Assert.assertEquals("Document element tag name should be xml", "xml", document.tagName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputInvalidXml() {
 parser = new Parser(new XmlTreeBuilder());
 document = parser.parseInput("<xml<script></script>", "baseUri");
 }

 @Test
 public void testParseFragmentValidHtml() {
 String fragmentHtml = "<p>Hello World</p>";
 List<Node> expectedNodes = Jsoup.parse(fragmentHtml).children();
 List<Node> actualNodes = Parser.parseFragment(fragmentHtml, null, "baseUri");

 Assert.assertEquals("Number of nodes should be the same", expectedNodes.size(), actualNodes.size());
 for (int i = 0; i < expectedNodes.size(); i++) {
 Node expectedNode = expectedNodes.get(i);
 Node actualNode = actualNodes.get(i);

 Assert.assertEquals("Node tag names should be the same", expectedNode.tagName(), actualNode.tagName());
 Assert.assertEquals("Node text should be the same", expectedNode.text(), actualNode.text());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullHtml() {
 Parser.parseFragment(null, null, "baseUri");
 }

 @Test
 public void testParseFragmentEmptyHtml() {
 String fragmentHtml = "";
 List<Node> expectedNodes = new Element("").children();
 List<Node> actualNodes = Parser.parseFragment(fragmentHtml, null, "baseUri");

 Assert.assertEquals("Number of nodes should be the same", expectedNodes.size(), actualNodes.size());
 }

 @Test
 public void testParseFragmentWithContext() {
 String fragmentHtml = "<div><p>Hello World</p></div>";
 Element context = Jsoup.parse("<div></div>").first();
 List<Node> expectedNodes = Jsoup.parse(fragmentHtml).children();
 List<Node> actualNodes = Parser.parseFragment(fragmentHtml, context, "baseUri");

 Assert.assertEquals("Number of nodes should be the same", expectedNodes.size(), actualNodes.size());
 for (int i = 0; i < expectedNodes.size(); i++) {
 Node expectedNode = expectedNodes.get(i);
 Node actualNode = actualNodes.get(i);

 Assert.assertEquals("Node tag names should be the same", expectedNode.tagName(), actualNode.tagName());
 Assert.assertEquals("Node text should be the same", expectedNode.text(), actualNode.text());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullContext() {
 String fragmentHtml = "<div><p>Hello World</p></div>";
 Parser.parseFragment(fragmentHtml, null, "baseUri");
 }

 @Test
 public void testParseFragmentWithBaseUri() {
 String fragmentHtml = "<img src=\"image.png\" alt=\"Image\">";
 String baseUri = "http://example.com";
 List<Node> expectedNodes = Jsoup.parse(fragmentHtml).children();
 List<Node> actualNodes = Parser.parseFragment(fragmentHtml, null, baseUri);

 Element imgElement = (Element) actualNodes.get(0);
 Assert.assertEquals("Image source URL should match base URI", baseUri + "/image.png", imgElement.attr("src"));
 }

 @Test
 public void testParseXmlFragmentValidXml() {
 String fragmentXml = "<xml><name>John Doe</name></xml>";
 List<Node> expectedNodes = Jsoup.parse(fragmentXml).children();
 List<Node> actualNodes = Parser.parseXmlFragment(fragmentXml, "baseUri");

 Assert.assertEquals("Number of nodes should be the same", expectedNodes.size(), actualNodes.size());
 for (int i = 0; i < expectedNodes.size(); i++) {
 Node expectedNode = expectedNodes.get(i);
 Node actualNode = actualNodes.get(i);

 Assert.assertEquals("Node tag names should be the same", expectedNode.tagName(), actualNode.tagName());
 Assert.assertEquals("Node text should be the same", expectedNode.text(), actualNode.text());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXmlFragmentNullXml() {
 Parser.parseXmlFragment(null, "baseUri");
 }

 @Test
 public void testParseXmlFragmentEmptyXml() {
 String fragmentXml = "";
 List<Node> expectedNodes = new Element("").children();
 List<Node> actualNodes = Parser.parseXmlFragment(fragmentXml, "baseUri");

 Assert.assertEquals("Number of nodes should be the same", expectedNodes.size(), actualNodes.size());
 }

 @Test
 public void testParseBodyFragmentValidHtml() {
 String bodyHtml = "<body><p>Hello World</p></body>";
 Document document = Parser.parseBodyFragment(bodyHtml, "baseUri");

 Assert.assertNotNull("Document should not be null", document);
 Assert.assertEquals("Document title should be empty", "", document.title());
 Assert.assertEquals("Document body text should be Hello World", "Hello World", document.body().text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentNullHtml() {
 Parser.parseBodyFragment(null, "baseUri");
 }

 @Test
 public void testParseBodyFragmentEmptyHtml() {
 String bodyHtml = "";
 Document document = Parser.parseBodyFragment(bodyHtml, "baseUri");

 Assert.assertNotNull("Document should not be null", document);
 Assert.assertEquals("Document title should be empty", "", document.title());
 Assert.assertEquals("Document body text should be empty", "", document.body().text());
 }

 @Test
 public void testParseBodyFragmentWithBaseUri() {
 String bodyHtml = "<body><img src=\"image.png\" alt=\"Image\"></body>";
 String baseUri = "http://example.com";
 Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

 Element imgElement = (Element) document.getElementsByTag("img").first();
 Assert.assertEquals("Image source URL should match base URI", baseUri + "/image.png", imgElement.src());
 }

 @Test
 public void testUnescapeEntitiesValidEntities() {
 String input = "&amp;amp; &lt; &gt; &apos; &quot;";
 String output = Parser.unescapeEntities(input, false);

 Assert.assertEquals("Unescaped string should be &amp; &lt; &gt; ' &\"", input, output);
 }

 @Test
 public void testUnescapeEntitiesInAttribute() {
 String input = "<div data-foo=\"&amp;amp; &lt; &gt; &apos; &quot;\"></div>";
 String output = Parser.unescapeEntities(input, true);

 String expectedOutput = "<div data-foo=\"&amp;amp; &lt; &gt; ' &\"</div>";
 Assert.assertEquals("Unescaped attribute string should be &amp; &lt; &gt; ' &\"", expectedOutput, output);
 }

 @Test
 public void testUnescapeEntitiesInvalidEntities() {
 String input = "&invalid;";
 String output = Parser.unescapeEntities(input, false);

 Assert.assertEquals("Unescaped string should be &invalid;", input, output);
 }

 @Test
 public void testParseBodyFragmentRelaxedDeprecated() {
 Document document = Parser.parseBodyFragmentRelaxed("<body><p>Hello World</p></body>", "baseUri");

 Assert.assertNotNull("Document should not be null", document);
 Assert.assertEquals("Document title should be empty", "", document.title());
 Assert.assertEquals("Document body text should be Hello World", "Hello World", document.body().text());
 }
}