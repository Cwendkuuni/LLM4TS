import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.TreeBuilderFactory;
import org.jsoup.parser.XmlTreeBuilder;

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
 String html = "<html><body><h1>Test</h1></body></html>";
 String baseUri = "http://example.com";
 List<ParseError> errors = parser.getErrors();

 Document doc = parser.parseInput(html, baseUri);
 Assert.assertEquals(0, errors.size());
 Assert.assertNotNull(doc);
 Assert.assertNotNull(doc.head());
 Assert.assertNotNull(doc.body());
 Assert.assertEquals("Test", doc.title().text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullHtml() {
 parser.parseInput(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullBaseUri() {
 parser.parseInput("<html></html>", null);
 }

 @Test
 public void testParseInputWithTrackErrors() {
 String html = "<html><body><h1>Test</h1><script>alert('XSS')</script></body></html>";
 String baseUri = "http://example.com";
 List<ParseError> errors = parser.getErrors();

 parser.setTrackErrors(5);
 Document doc = parser.parseInput(html, baseUri);
 Assert.assertEquals(1, errors.size());
 Assert.assertNotNull(doc);
 Assert.assertNotNull(doc.head());
 Assert.assertNotNull(doc.body());
 Assert.assertEquals("Test", doc.title().text());
 Assert.assertEquals("Malformed HTML: Unexpected element script at line 3 column 1", errors.get(0).getMessage());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetErrorsEmptyList() {
 parser.setTrackErrors(0);
 List<ParseError> errors = parser.getErrors();
 errors.remove(0);
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder expectedTreeBuilder = treeBuilder;
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
 Assert.assertSame(expectedTreeBuilder, actualTreeBuilder);
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 Parser parser = new Parser(treeBuilder);
 parser.setTreeBuilder(newTreeBuilder);
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
 Assert.assertSame(newTreeBuilder, actualTreeBuilder);
 }

 @Test
 public void testIsTrackErrors() {
 Parser parser = new Parser(treeBuilder);
 Assert.assertFalse(parser.isTrackErrors());
 parser.setTrackErrors(1);
 Assert.assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsZero() {
 Parser parser = new Parser(treeBuilder);
 parser.setTrackErrors(0);
 Assert.assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsPositive() {
 Parser parser = new Parser(treeBuilder);
 parser.setTrackErrors(5);
 Assert.assertTrue(parser.isTrackErrors());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetTrackErrorsNegative() {
 Parser parser = new Parser(treeBuilder);
 parser.setTrackErrors(-1);
 }

 @Test
 public void testParseStaticParse() {
 String html = "<html><body><h1>Test</h1></body></html>";
 Document doc = Parser.parse(html);
 Assert.assertNotNull(doc);
 Assert.assertNotNull(doc.head());
 Assert.assertNotNull(doc.body());
 Assert.assertEquals("Test", doc.title().text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseStaticParseNullHtml() {
 Parser.parse(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseStaticParseNullBaseUri() {
 Parser.parse("<html></html>", null);
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<div>Test</div>";
 Element context = Jsoup.parse("<html><body></body></html>").body();
 String baseUri = "http://example.com";
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
 Assert.assertFalse(context.children().isEmpty());
 Assert.assertEquals(1, nodes.size());
 Element actualDiv = (Element) nodes.get(0);
 Assert.assertEquals("div", actualDiv.tagName());
 Assert.assertEquals("Test", actualDiv.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullFragmentHtml() {
 Parser.parseFragment(null, null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullContext() {
 Parser.parseFragment("<div>Test</div>", null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullBaseUri() {
 Parser.parseFragment("<div>Test</div>", null, null);
 }

 @Test
 public void testParseXmlFragment() {
 String fragmentXml = "<xml><div>Test</div></xml>";
 String baseUri = "http://example.com";
 List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
 Assert.assertFalse(nodes.isEmpty());
 Assert.assertEquals(1, nodes.size());
 Element actualDiv = (Element) nodes.get(0);
 Assert.assertEquals("div", actualDiv.tagName());
 Assert.assertEquals("Test", actualDiv.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXmlFragmentNullFragmentXml() {
 Parser.parseXmlFragment(null, "http://example.com");
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><p>Test</p></body>";
 String baseUri = "http://example.com";
 Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
 Assert.assertNotNull(doc);
 Assert.assertNotNull(doc.head());
 Assert.assertNotNull(doc.body());
 Assert.assertEquals(1, doc.numberOfChildren());
 Element p = (Element) doc.body().get(0);
 Assert.assertEquals("p", p.tagName());
 Assert.assertEquals("Test", p.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentNullBodyHtml() {
 Parser.parseBodyFragment(null, "http://example.com");
 }

 @Test
 public void testParseBodyFragmentNullBaseUri() {
 Parser.parseBodyFragment("<body></body>", null);
 }

 @Test
 public void testUnescapeEntities() {
 String html = "&amp; &lt; &gt; &quot; &apos; &nbsp; &iexcl; &cent; &pound; &curren; &yen; &euro; &reg; &deg; &sup2; &sup3; &copy; &trade; &agrave; &aacute; &acirc; &atilde; &amacr; &aelig; &ccedil; &egrave; &eacute; &ecirc; &euml; &igrave; &iacute; &icirc; &itilde; &iuml; &eth; &ntild; &oelig; &scedil; &ouml; &divide; &oslash; &ugrave; &uacute; &ucirc; &utilde; &ouml; &times; &plus; &iquest;";
 String unescaped = Parser.unescapeEntities(html, false);
 String expected = "&amp; &lt; &gt; &quot; &apos; &nbsp; &#191; &#160; &#169; &#164; &#8364; &#8365; &#224; &#225; &#226; &#228; &#231; &#246; &#247; &#249; &#252; &#253; &#254; &#255; &#256; &#257; &#194; &#195; &#196; &#198; &#201; &#202; &#203; &#205; &#206; &#209; &#210; &#211; &#212; &#214; &#215; &#216; &#217; &#218; &#220; &#221;";
 Assert.assertEquals(unescaped, expected);
 }

 @Test
 public void testUnescapeEntitiesNullString() {
 Parser.unescapeEntities(null, false);
 }
}