import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.HtmlTreeBuilder;
import java.util.List;

public class ParserTest {

 private Parser parserHtml;
 private Parser parserXml;

 @Before
 public void setUp() {
 parserHtml = Parser.htmlParser();
 parserXml = Parser.xmlParser();
 }

 // Testing parseInput method for HTML parser
 @Test
 public void testParseInputHtml() {
 String html = "<html><body><h1>Test</h1></body></html>";
 Document doc = parserHtml.parseInput(html, "http://example.com");
 Assert.assertEquals("Test", doc.title());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullHtml() {
 parserHtml.parseInput(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullBaseUri() {
 parserHtml.parseInput("<html></html>", null);
 }

 @Test
 public void testParseInputWithErrors() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 parser.setTrackErrors(1);
 String html = "<html><body><h1>Test<script>alert('Hello, World!');</script></body></html>";
 List<ParseError> errors = parser.parseInput(html, "http://example.com").getErrors();
 Assert.assertEquals(1, errors.size());
 }

 @Test
 public void testParseInputEmpty() {
 Document doc = parserHtml.parseInput("", "http://example.com");
 Assert.assertNull(doc);
 }

 // Testing parseInput method for XML parser
 @Test
 public void testParseInputXml() {
 String xml = "<xml><tag>Test</tag></xml>";
 Document doc = parserXml.parseInput(xml, "http://example.com");
 Assert.assertEquals("Test", doc.child(0).text());
 }

 // Testing static parse method
 @Test
 public void testParseStaticHtml() {
 Document doc = Parser.parse("<html><body><h1>Test</h1></body></html>", "http://example.com");
 Assert.assertEquals("Test", doc.title());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseStaticNullHtml() {
 Parser.parse(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseStaticNullBaseUri() {
 Parser.parse("<html></html>", null);
 }

 // Testing parseFragment method
 @Test
 public void testParseFragment() {
 String fragmentHtml = "<div><p>Test</p></div>";
 List<Node> nodes = Parser.parseFragment(fragmentHtml, null, "http://example.com");
 Assert.assertEquals(1, nodes.size());
 Assert.assertTrue(nodes.get(0) instanceof Element);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullFragmentHtml() {
 Parser.parseFragment(null, null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullBaseUri() {
 Parser.parseFragment("<div></div>", null);
 }

 // Testing parseBodyFragment method
 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Test</h1></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 Assert.assertEquals("Test", doc.title());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentNullBodyHtml() {
 Parser.parseBodyFragment(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentNullBaseUri() {
 Parser.parseBodyFragment("<body></body>", null);
 }

 // Testing unescapeHtmlEntities method
 @Test
 public void testUnescapeHtmlEntities() {
 String html = "&lt;p&gt;Test&lt;/p&gt;";
 String unescaped = Parser.unescapeHtmlEntities(html);
 Assert.assertEquals("<p>Test</p>", unescaped);
 }

 // Testing parseBodyFragmentRelaxed method (deprecated)
 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<body><h1>Test</h1></body>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
 Assert.assertEquals("Test", doc.title());
 }

 @Test(expected = DeprecationException.class)
 public void testParseBodyFragmentRelaxedDeprecated() {
 Parser.parseBodyFragmentRelaxed("<body></body>", "http://example.com");
 }
}