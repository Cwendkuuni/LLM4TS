import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.ParseError;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
 public void testParseInputWithValidHtml() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 Document doc = parser.parseInput(html, "http://test.com");
 assertNotNull(doc);
 assertEquals("Test", doc.title().text());
 assertEquals("Hello World", doc.body().getElementsByTag("h1").text());
 }

 @Test
 public void testParseInputWithInvalidHtml() {
 String html = "<html><head><title>Test</title></head></html>"; // missing body
 Document doc = parser.parseInput(html, "http://test.com");
 assertNotNull(doc);
 assertNull(doc.body());
 }

 @Test
 public void testParseInputWithErrors() {
 String html = "<html><head><title>Test</title></head><script>alert('Hello')</script><body><h1>Hello World</h1></body></html>";
 parser.setTrackErrors(3);
 Document doc = parser.parseInput(html, "http://test.com");
 List<ParseError> errors = parser.getErrors();
 assertEquals(3, errors.size());
 assertTrue(errors.get(0).getLine() > 0); // the first error should be in the script tag
 assertEquals("Unexpected '<', expecting '<!', 'script'", errors.get(0).getMessage());
 assertEquals("Unexpected '<', expecting '>', 'script'", errors.get(1).getMessage());
 assertEquals("Expecting end tag for tag \"script\"", errors.get(2).getMessage());
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
 }
}