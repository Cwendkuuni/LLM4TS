import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

 private Parser parser;

 @Test
 public void testParseHtml() {
 parser = new Parser(new HtmlTreeBuilder());
 Document doc = parser.parseInput("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals("Test", doc.title().text());
 Assert.assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testParseHtmlWithErrors() {
 parser = new Parser(new HtmlTreeBuilder());
 parser.setTrackErrors(3);
 Document doc = null;
 try {
 doc = parser.parseInput("<html><head><title>Test</title></head><body><h1>Hello<script>World</script></h1></body></html>", "http://example.com");
 } catch (ParseException e) {
 List<ParseError> errors = e.getParseErrors();
 Assert.assertEquals(3, errors.size());
 Assert.assertEquals("Expecting ';' but was '{'", errors.get(0).getMessage());
 Assert.assertEquals("Expecting '>' but was 's', <script>", errors.get(1).getMessage());
 Assert.assertEquals("Expecting '>' but was '^'", errors.get(2).getMessage());
 }
 Assert.assertNull(doc);
 }

 @Test
 public void testParseXml() {
 parser = new Parser(new XmlTreeBuilder());
 Document doc = parser.parseInput("<root><element>Hello World</element></root>", "http://example.com");
 Assert.assertNotNull(doc);
 Element element = doc.element(0);
 Assert.assertEquals("Hello World", element.text());
 }

 @Test
 public void testParseFragment() {
 parser = new Parser(new HtmlTreeBuilder());
 Element context = Jsoup.parse("<html><body><div id='container'></div></body></html>").body();
 List<Node> nodes = Parser.parseFragment("<p id='test'>Test</p>", context, "http://example.com");
 Assert.assertNotNull(nodes);
 Assert.assertEquals(1, nodes.size());
 Node node = nodes.get(0);
 Assert.assertEquals("p", node.tagName());
 Assert.assertEquals("Test", node.text());
 context.remove(); // clean up
 }

 @Test
 public void testParseBodyFragment() {
 Document doc = Parser.parseBodyFragment("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testUnescapeEntities() {
 String input = "<p>&#160;Hello &amp; World!</p>";
 String output = Parser.unescapeEntities(input, false);
 Assert.assertEquals("<p> Hello &amp; World!</p>", output);
 }
}