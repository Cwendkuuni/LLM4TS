import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 private Parser htmlParser;
 private Parser xmlParser;

 @Before
 public void setup() {
 htmlParser = Parser.htmlParser();
 xmlParser = Parser.xmlParser();
 }

 @Test
 public void testParseValidHtml() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = htmlParser.parseInput(html, "http://example.com");
 assertNotNull(doc);
 assertEquals("Hello World", doc.select("h1").text());
 }

 @Test
 public void testParseValidXml() {
 String xml = "<root><element>Hello World</element></root>";
 Document doc = xmlParser.parseInput(xml, "http://example.com");
 assertNotNull(doc);
 assertEquals("Hello World", doc.select("root > element").text());
 }

 @Test
 public void testTrackErrors() {
 String html = "<html><body><h1>Hello World</h1><script>alert('xss')</script></body></html>";
 Parser parser = new Parser(new HtmlTreeBuilder()) {
 @Override
 public ParseErrorList errors() {
 return ParseErrorList.tracking(1);
 }
 };
 List<ParseError> errors = parser.parseInput(html, "http://example.com").getErrors();
 assertNotNull(errors);
 assertFalse(errors.isEmpty());
 assertEquals(1, errors.size());
 assertTrue(errors.get(0) instanceof org.jsoup.parser.ParseError.Script);
 }

 @Test(expected = org.jsoup.parser.ParseError.class)
 public void testParseError() {
 String html = "<html><body><script>alert('xss')</script></body></html>";
 Parser parser = htmlParser; // use default Parser
 parser.parseInput(html, "http://example.com");
 }

 @Test(expected = org.jsoup.parser.ParseError.class)
 public void testParseInvalidXml() {
 String xml = "<root>";
 Parser parser = xmlParser; // use default Parser
 parser.parseInput(xml, "http://example.com");
 }

 @Test
 public void testSetTreeBuilder() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
 parser.setTreeBuilder(new XmlTreeBuilder());
 assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
 }
}