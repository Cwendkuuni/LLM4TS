import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;

 @BeforeClass
 public static void setupClass() {
 treeBuilder = new HtmlTreeBuilder();
 }

 @Before
 public void setup() {
 parser = new Parser(treeBuilder);
 }

 /**
 * Test constructor
 */
 @Test
 public void testParserConstructor() {
 // no checks
 }

 /**
 * Test parseInput method with valid input
 */
 @Test
 public void testParseInputValid() {
 String html = "<html><head></head><body><p>Test</p></body></html>";
 Document document = parser.parseInput(html, "http://test.com");
 Element body = document.body();
 Element p = body.child(0);
 Assert.assertEquals("Test", p.text());
 }

 /**
 * Test parseInput method with empty input
 */
 @Test
 public void testParseInputEmpty() {
 Document document = parser.parseInput("", "http://test.com");
 Assert.assertNull(document);
 }

 /**
 * Test parseInput method with null input
 */
 @Test(expected = NullPointerException.class)
 public void testParseInputNull() {
 parser.parseInput(null, "http://test.com");
 }

 /**
 * Test parseInput method with invalid input (malformed HTML)
 */
 @Test(expected = IllegalArgumentException.class)
 public void testParseInputInvalid() {
 String html = "<html<p>Test</p>";
 Assert.assertThrows(IllegalArgumentException.class, () -> parser.parseInput(html, "http://test.com"));
 }

 /**
 * Test getTreeBuilder method
 */
 @Test
 public void testGetTreeBuilder() {
 Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
 }

 /**
 * Test setTreeBuilder method
 */
 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 Assert.assertEquals(newTreeBuilder, parser.getTreeBuilder());
 }

 /**
 * Test isTrackErrors method
 */
 @Test
 public void testIsTrackErrors() {
 Assert.assertFalse(parser.isTrackErrors());
 }

 /**
 * Test setTrackErrors method with valid value
 */
 @Test
 public void testSetTrackErrorsValid() {
 parser.setTrackErrors(5);
 Assert.assertTrue(parser.isTrackErrors());
 }

 /**
 * Test setTrackErrors method with invalid value (negative number)
 */
 @Test(expected = IllegalArgumentException.class)
 public void testSetTrackErrorsInvalid() {
 parser.setTrackErrors(-1);
 }

 /**
 * Test getErrors method
 */
 @Test
 public void testGetErrorsEmpty() {
 List<ParseError> errors = parser.getErrors();
 Assert.assertTrue(errors.isEmpty());
 }

 /**
 * Test getErrors method with errors
 */
 @Test
 public void testGetErrorsWithErrors() {
 parser.parseInput("<html><body><script></script>", "http://test.com");
 List<ParseError> errors = parser.getErrors();
 Assert.assertFalse(errors.isEmpty());
 }

 /**
 * Test settings method
 */
 @Test
 public void testSettings() {
 ParseSettings settings = parser.settings();
 Assert.assertNotNull(settings);
 }

 /**
 * Test parse method
 */
 @Test
 public void testParse() {
 Document document = Parser.parse("<html><head></head><body><p>Test</p></body></html>", "http://test.com");
 Element body = document.body();
 Element p = body.child(0);
 Assert.assertEquals("Test", p.text());
 }

 /**
 * Test parseFragment method with valid input
 */
 @Test
 public void testParseFragmentValid() {
 String fragmentHtml = "<p>Test</p>";
 List<Node> nodes = Parser.parseFragment(fragmentHtml, null, "http://test.com");
 Node node = nodes.get(0);
 Assert.assertEquals("p", node.tagName());
 }

 /**
 * Test parseFragment method with empty input
 */
 @Test
 public void testParseFragmentEmpty() {
 List<Node> nodes = Parser.parseFragment("", null, "http://test.com");
 Assert.assertTrue(nodes.isEmpty());
 }

 /**
 * Test parseFragment method with null input
 */
 @Test(expected = NullPointerException.class)
 public void testParseFragmentNull() {
 Parser.parseFragment(null, null, "http://test.com");
 }

 /**
 * Test parseFragment method with invalid input (malformed HTML)
 */
 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentInvalid() {
 String fragmentHtml = "<html<p>Test</p>";
 List<Node> nodes = Parser.parseFragment(fragmentHtml, null, "http://test.com");
 Assert.assertThrows(IllegalArgumentException.class, () -> Parser.parseFragment(fragmentHtml, null, "http://test.com"));
 }

 /**
 * Test parseXmlFragment method with valid input
 */
 @Test
 public void testParseXmlFragmentValid() {
 String fragmentXml = "<xml><test>Test</test></xml>";
 List<Node> nodes = Parser.parseXmlFragment(fragmentXml, "http://test.com");
 Node node = nodes.get(0);
 Assert.assertEquals("test", node.tagName());
 }

 /**
 * Test parseBodyFragment method with valid input
 */
 @Test
 public void testParseBodyFragmentValid() {
 String bodyHtml = "<body><p>Test</p></body>";
 Document document = Parser.parseBodyFragment(bodyHtml, "http://test.com");
 Element p = document.body().child(0);
 Assert.assertEquals("Test", p.text());
 }

 /**
 * Test parseBodyFragment method with empty input
 */
 @Test
 public void testParseBodyFragmentEmpty() {
 Document document = Parser.parseBodyFragment("", "http://test.com");
 Assert.assertNull(document);
 }

 /**
 * Test parseBodyFragment method with null input
 */
 @Test(expected = NullPointerException.class)
 public void testParseBodyFragmentNull() {
 Parser.parseBodyFragment(null, "http://test.com");
 }

 /**
 * Test unescapeEntities method with valid input
 */
 @Test
 public void testUnescapeEntitiesValid() {
 String string = "&lt;p&gt;Test&amp; &apos; &quot; &gt;";
 String unescapedString = Parser.unescapeEntities(string, false);
 Assert.assertEquals("<p>Test & ' & \" >", unescapedString);
 }

 /**
 * Test unescapeEntities method with invalid input (null)
 */
 @Test(expected = NullPointerException.class)
 public void testUnescapeEntitiesNull() {
 Parser.unescapeEntities(null, false);
 }

 /**
 * Test parseBodyFragmentRelaxed method (deprecated)
 */
 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<body><p>Test</p></body>";
 Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://test.com");
 Element p = document.body().child(0);
 Assert.assertEquals("Test", p.text());
 }

 /**
 * Test htmlParser method
 */
 @Test
 public void testHtmlParser() {
 Parser parser = Parser.htmlParser();
 }

 /**
 * Test xmlParser method
 */
 @Test
 public void testXmlParser() {
 Parser parser = Parser.xmlParser();
 }
}