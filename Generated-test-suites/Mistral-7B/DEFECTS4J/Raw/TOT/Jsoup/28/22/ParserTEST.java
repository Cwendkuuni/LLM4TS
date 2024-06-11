import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;

 @Before
 public void setup() {
 parser = new Parser(new HtmlTreeBuilder());
 }

 @Test
 public void testParseValidHtml() {
 String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
 Document doc = parser.parseInput(html, null);
 assertEquals("Test", doc.title().text());
 assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testParseInvalidHtmlWithTracking() {
 String html = "<html><head><title>Test</title></head><body><p>Hello <script>alert('XSS')</script> World</p></body></html>";
 parser.setTrackErrors(1);
 Document doc = parseInput(html, null);
 List<ParseError> errors = parser.getErrors();
 assertEquals(1, errors.size());
 assertEquals("Error parsing start tag \'script'\n at line 4 col 11", errors.get(0).getMessage());
 }

 @Test(expected = org.jsoup.exceptions.MalformedHtmlException.class)
 public void testParseInvalidHtmlWithoutTracking() {
 String html = "<html><head><title>Test</title></head><body><p>Hello <script>alert('XSS')</script> World</p></body></html>";
 parser.setTrackErrors(0);
 parser.parseInput(html, null);
 }

 @Test
 public void testSetTreeBuilder() {
 Parser parserWithNewTreeBuilder = parser.setTreeBuilder(new XmlTreeBuilder());
 Document doc = parserWithNewTreeBuilder.parseInput("<xml><root/></xml>", null);
 assertTrue(doc.tagName().equals("document")); // XML parser creates a document, not an HTML document
 }
}