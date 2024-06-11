import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.xml.XmlTreeBuilder;
import java.util.ArrayList;
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
 String baseUri = "http://example.com/";
 Document doc = parser.parseInput(html, baseUri);
 Assert.assertNotNull(doc);
 Assert.assertNotNull(doc.title());
 Assert.assertEquals("Hello World", doc.title().text());
 }

 @Test
 public void testParseInputWithErrors() {
 String html = "<html><body><h1>Hello <script> World </script></h1></body></html>";
 String baseUri = "http://example.com/";
 parser.setTrackErrors(1);
 Document doc = parser.parseInput(html, baseUri);
 List<ParseError> errors = parser.getErrors();
 Assert.assertNotNull(doc);
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Error parsing start tag : script", errors.get(0).getMessage());
 }

 @Test
 public void testParseInputWithMaxErrorsReached() {
 String html = "<html><body>";
 String baseUri = "http://example.com/";
 parser.setTrackErrors(1);
 for (int i = 0; i < 2; i++) {
 parser.parseInput(html, baseUri);
 }
 List<ParseError> errors = parser.getErrors();
 Assert.assertNotNull(errors);
 Assert.assertEquals(2, errors.size());
 }

 @Test
 public void testParseInputWithNullHtml() {
 String baseUri = "http://example.com/";
 parser.parseInput(null, baseUri);
 Assert.assertNull(parser.getTreeBuilder());
 }

 @Test(expected = NullPointerException.class)
 public void testParseInputWithNullBaseUri() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 parser.parseInput(html, null);
 }

 @Test
 public void testParseInputWithEmptyString() {
 String baseUri = "http://example.com/";
 Document doc = parser.parseInput("", baseUri);
 Assert.assertNotNull(doc);
 Assert.assertNull(doc.body());
 }

 @Test
 public void testParseInputWithWhitespaceOnlyString() {
 String baseUri = "http://example.com/";
 Document doc = parser.parseInput("   ", baseUri);
 Assert.assertNotNull(doc);
 Assert.assertNull(doc.body());
 }

 @Test
 public void testParseInputWithLargeString() {
 String html = "<html lang=\"en\">\n" +
 " <head>\n" +
 " <meta charset=\"UTF-8\">\n" +
 " <title>The Dormouse's story</title>\n" +
 " </head>\n" +
 " <body>\n" +
 " <p class=\"title\"><b>The Dormouse's story</b></p>\n" +
 " <p class=\"story\">Once upon a time there were three little sisters...</p>\n" +
 " </body>\n" +
 " </html>";
 Document doc = parser.parseInput(html, "http://example.com/");
 Assert.assertNotNull(doc);
 Assert.assertEquals(2, doc.numberOfElements());
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 Assert.assertEquals(newTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 parser.setTrackErrors(0);
 Assert.assertFalse(parser.isTrackErrors());
 parser.setTrackErrors(1);
 Assert.assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 String html = "<html><body><h1>Hello <script> World </script></h1></body></html>";
 String baseUri = "http://example.com/";
 parser.setTrackErrors(1);
 Document doc = parser.parseInput(html, baseUri);
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Error parsing start tag : script", errors.get(0).getMessage());
 }

 @Test
 public void testGetErrorsWhenNoErrors() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://example.com/";
 parser.parseInput(html, baseUri);
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(0, errors.size());
 }

 @Test
 public void testParseFragment() {
 String html = "<div><p>Hello World</p></div>";
 Element context = Jsoup.parse("<html><body></body></html>").body();
 List<Node> nodes = Parser.parseFragment(html, context, "http://example.com/");
 Assert.assertEquals(1, nodes.size());
 Assert.assertTrue(nodes.get(0) instanceof Element);
 Assert.assertEquals("p", nodes.get(0).tagName());
 Assert.assertEquals("Hello World", nodes.get(0).text());
 }

 @Test
 public void testParseFragmentWithErrors() {
 String html = "<div><p>Hello <script> World </script></p></div>";
 Element context = Jsoup.parse("<html><body></body></html>").body();
 parser.setTrackErrors(1);
 List<Node> nodes = Parser.parseFragment(html, context, "http://example.com/");
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Error parsing start tag : script", errors.get(0).getMessage());
 Assert.assertEquals(0, nodes.size());
 }

 @Test
 public void testParseFragmentWithMaxErrorsReached() {
 Element context = Jsoup.parse("<html><body></body></html>").body();
 List<Node> nodes = new ArrayList<>();
 for (int i = 0; i < 2; i++) {
 String html = "<div><p>Hello World</p></div>";
 nodes.addAll(Parser.parseFragment(html, context, "http://example.com/"));
 }
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(2, errors.size());
 }

 @Test
 public void testParseFragmentWithNullHtml() {
 Element context = Jsoup.parse("<html><body></body></html>").body();
 Parser.parseFragment(null, context, "http://example.com/");
 Assert.assertEquals(0, context.childSize());
 }

 @Test(expected = NullPointerException.class)
 public void testParseFragmentWithNullContext() {
 String html = "<div><p>Hello World</p></div>";
 Parser.parseFragment(html, null, "http://example.com/");
 }

 @Test
 public void testParseFragmentWithEmptyString() {
 Element context = Jsoup.parse("<html><body></body></html>").body();
 List<Node> nodes = Parser.parseFragment("", context, "http://example.com/");
 Assert.assertEquals(0, nodes.size());
 }

 @Test
 public void testParseFragmentWithLargeString() {
 String html = "<html lang=\"en\">\n" +
 " <head>\n" +
 " <meta charset=\"UTF-8\">\n" +
 " <title>The Dormouse's story</title>\n" +
 " </head>\n" +
 " <body>\n" +
 " <p class=\"title\"><b>The Dormouse's story</b></p>\n" +
 " <p class=\"story\">Once upon a time there were three little sisters...</p>\n" +
 " </body>\n" +
 " </html>";
 Element context = Jsoup.parse("<html><body></body></html>").body();
 List<Node> nodes = Parser.parseFragment(html, context, "http://example.com/");
 Assert.assertEquals(2, nodes.size());
 }

 @Test
 public void testParseXmlFragment() {
 String xml = "<root><element>Hello World</element></root>";
 List<Node> nodes = Parser.parseXmlFragment(xml, "http://example.com/");
 Assert.assertEquals(1, nodes.size());
 Assert.assertTrue(nodes.get(0) instanceof Element);
 Assert.assertEquals("element", nodes.get(0).tagName());
 Assert.assertEquals("Hello World", nodes.get(0).text());
 }

 @Test
 public void testParseXmlFragmentWithErrors() {
 String xml = "<root><element><script>Hello World</script></element></root>";
 parser.setTrackErrors(1);
 List<Node> nodes = Parser.parseXmlFragment(xml, "http://example.com/");
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Error parsing start tag : script", errors.get(0).getMessage());
 Assert.assertEquals(0, nodes.size());
 }

 @Test
 public void testParseXmlFragmentWithMaxErrorsReached() {
 List<Node> nodes = new ArrayList<>();
 for (int i = 0; i < 2; i++) {
 String xml = "<root><element>Hello World</element></root>";
 nodes.addAll(Parser.parseXmlFragment(xml, "http://example.com/"));
 }
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(2, errors.size());
 }

 @Test
 public void testParseXmlFragmentWithNullXml() {
 List<Node> nodes = Parser.parseXmlFragment(null, "http://example.com/");
 Assert.assertEquals(0, nodes.size());
 }

 @Test(expected = NullPointerException.class)
 public void testParseXmlFragmentWithNullBaseUri() {
 String xml = "<root><element>Hello World</element></root>";
 Parser.parseXmlFragment(xml, null);
 }

 @Test
 public void testParseBodyFragment() {
 String html = "<html><body><p>Hello World</p></body></html>";
 String baseUri = "http://example.com/";
 Document doc = Parser.parseBodyFragment(html, baseUri);
 Assert.assertEquals(1, doc.numberOfElements());
 Assert.assertTrue(doc.body().child(0) instanceof Element);
 Assert.assertEquals("p", doc.body().child(0).tagName());
 Assert.assertEquals("Hello World", doc.body().child(0).text());
 }

 @Test
 public void testParseBodyFragmentWithErrors() {
 String html = "<html><body><p>Hello <script> World </script></p></body></html>";
 String baseUri = "http://example.com/";
 parser.setTrackErrors(1);
 Document doc = Parser.parseBodyFragment(html, baseUri);
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Error parsing start tag : script", errors.get(0).getMessage());
 Assert.assertEquals(0, doc.numberOfElements());
 }

 @Test
 public void testParseBodyFragmentWithMaxErrorsReached() {
 String html = "<html><body><p>Hello World</p></body></html>";
 String baseUri = "http://example.com/";
 for (int i = 0; i < 2; i++) {
 Document doc = Parser.parseBodyFragment(html, baseUri);
 }
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(2, errors.size());
 }

 @Test
 public void testParseBodyFragmentWithNullHtml() {
 String baseUri = "http://example.com/";
 Document doc = Parser.parseBodyFragment(null, baseUri);
 Assert.assertEquals(0, doc.numberOfElements());
 }

 @Test(expected = NullPointerException.class)
 public void testParseBodyFragmentWithNullBaseUri() {
 String html = "<html><body><p>Hello World</p></body></html>";
 Parser.parseBodyFragment(html, null);
 }

 @Test
 public void testParseBodyFragmentWithEmptyString() {
 String baseUri = "http://example.com/";
 Document doc = Parser.parseBodyFragment("", baseUri);
 Assert.assertEquals(0, doc.numberOfElements());
 }

 @Test
 public void testParseBodyFragmentWithLargeString() {
 String html = "<html lang=\"en\">\n" +
 " <head>\n" +
 " <meta charset=\"UTF-8\">\n" +
 " <title>The Dormouse's story</title>\n" +
 " </head>\n" +
 " <body>\n" +
 " <p class=\"title\"><b>The Dormouse's story</b></p>\n" +
 " <p class=\"story\">Once upon a time there were three little sisters...</p>\n" +
 " </body>\n" +
 " </html>";
 Document doc = Parser.parseBodyFragment(html, "http://example.com/");
 Assert.assertEquals(2, doc.numberOfElements());
 }

 @Test
 public void testUnescapeEntities() {
 String html = "&nbsp; Hello &quot; World &amp; Me &lt;p&gt;";
 String result = Parser.unescapeEntities(html, false);
 Assert.assertEquals(" Hello \" World & Me <p>", result);
 String resultAttribute = Parser.unescapeEntities(html, true);
 Assert.assertEquals("&nbsp; Hello &quot; World &amp; Me &lt;p&gt;", resultAttribute);
 }

 @Test
 public void testParseInputWithDeprecatedMethodCall() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://example.com/";
 Document doc = Parser.parseBodyFragmentRelaxed(html, baseUri);
 Assert.assertEquals(1, doc.numberOfElements());
 Assert.assertTrue(doc.body().child(0) instanceof Element);
 Assert.assertEquals("h1", doc.body().child(0).tagName());
 Assert.assertEquals("Hello World", doc.body().child(0).text());
 }
}