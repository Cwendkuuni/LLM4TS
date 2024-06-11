import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.ParseErrorList;
import java.util.List;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setUp() {
 parser = new Parser(new HtmlTreeBuilder());
 }

 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 doc = parser.parseInput(html, null);
 assertNotNull(doc);
 assertNotNull(doc.title());
 assertEquals("Hello World", doc.title().text());
 }

 @Test
 public void testParseInputWithErrorTracking() {
 Parser parserWithTrackErrors = new Parser(new HtmlTreeBuilder());
 parserWithTrackErrors.setTrackErrors(5);
 String htmlWithError = "<html><body><script>alert('Hello')</script><h1>Hello World</h1></body></html>";
 List<ParseError> errors = parserWithTrackErrors.parseInput(htmlWithError, null).errors();
 assertNotNull(errors);
 assertEquals(1, errors.size());
 assertEquals("Unexpected '<', expecting '<!', or '<?'", errors.get(0).message());
 }

 @Test
 public void testParseInputWithNullBaseUri() {
 String html = "<img src='/image.jpg'>";
 doc = parser.parseInput(html, null);
 Element img = doc.select("img").first();
 assertEquals("image.jpg", img.attr("src"));
 }

 @Test
 public void testParseInputWithBaseUri() {
 String baseUri = "http://example.com/";
 String html = "<img src='image.html'>";
 doc = parser.parseInput(html, baseUri);
 Element img = doc.select("img").first();
 assertEquals("image.html", img.attr("src"));
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder treeBuilder = parser.getTreeBuilder();
 assertNotNull(treeBuilder);
 }

 @Test
 public void testSetTreeBuilder() {
 Parser parserWithCustomTreeBuilder = new Parser(new XmlTreeBuilder());
 TreeBuilder customTreeBuilder = new XmlTreeBuilder();
 parserWithCustomTreeBuilder.setTreeBuilder(customTreeBuilder);
 TreeBuilder newTreeBuilder = parserWithCustomTreeBuilder.getTreeBuilder();
 assertEquals(customTreeBuilder, newTreeBuilder);
 }

 @Test
 public void testIsTrackErrors() {
 Parser parserWithTrackErrors = new Parser(new HtmlTreeBuilder());
 parserWithTrackErrors.setTrackErrors(5);
 assertTrue(parserWithTrackErrors.isTrackErrors());
 Parser parserWithoutTrackErrors = new Parser(new HtmlTreeBuilder());
 assertFalse(parserWithoutTrackErrors.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 parser.setTrackErrors(5);
 List<ParseError> errors = parser.parseInput("<script><h1>Hello</script>", null).errors();
 assertEquals(1, errors.size());
 List<ParseError> errorsBeforeTracking = parser.parseInput("<script><h1>Hello</script>", null).errors();
 assertEquals(0, errorsBeforeTrackking.size());
 }

 @Test
 public void testGetErrors() {
 Parser parserWithTrackErrors = new Parser(new HtmlTreeBuilder());
 parserWithTrackErrors.setTrackErrors(5);
 List<ParseError> errors = parserWithTrackErrors.parseInput("<script><h1>Hello</script>", null).errors();
 assertEquals(1, errors.size());
 assertEquals("Unexpected '<', expecting '<!', or '<?'", errors.get(0).message());
 List<ParseError> getErrors = parserWithTrackErrors.getErrors();
 assertEquals(1, getErrors.size());
 assertEquals("Unexpected '<', expecting '<!', or '<?'", getErrors.get(0).message());
 }

 @Test
 public void testParse() {
 Document doc = Parser.parse("<html><body><h1>Hello World</h1></body></html>", null);
 assertNotNull(doc);
 assertNotNull(doc.title());
 assertEquals("Hello World", doc.title().text());
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<ul><li>Item 1</li><li>Item 2</li></ul>";
 List<Node> nodes = Parser.parseFragment(fragmentHtml, null, null);
 assertNotNull(nodes);
 assertFalse(nodes.isEmpty());
 Element list = (Element) nodes.get(0);
 assertEquals("ul", list.tagName());
 List<Element> listItems = list.children();
 assertEquals(2, listItems.size());
 Element item1 = listItems.get(0);
 assertEquals("li", item1.tagName());
 assertEquals("Item 1", item1.text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyFragmentHtml = "<h1>Hello World</h1>";
 Document doc = Parser.parseBodyFragment(bodyFragmentHtml, null);
 assertNotNull(doc);
 assertNotNull(doc.body());
 Element h1 = doc.select("h1").first();
 assertEquals("Hello World", h1.text());
 }
}