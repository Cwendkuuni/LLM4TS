import org.junit.Test;
import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import java.util.List;

public class ParserTest {

 private Parser parserHtml;
 private Parser parserXml;

 @Before
 public void setup() {
 parserHtml = Parser.htmlParser();
 parserXml = Parser.xmlParser();
 }

 // Testing the parseInput method of Parser class
 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://example.com";
 Document docHtml = parserHtml.parseInput(html, baseUri);
 String xhtml = "<html xmlns='http://www.w3.org/1999/xhtml' version='1.0'><body><h1>Hello World</h1></body></html>";
 Document docXhtml = parserXml.parseInput(xhtml, baseUri);

 Assert.assertNotNull(docHtml);
 Assert.assertNotNull(docXhtml);
 Assert.assertEquals(1, docHtml.body().childNodes().size());
 Assert.assertEquals(1, docXhtml.body().childNodes().size());
 Assert.assertEquals("Hello World", docHtml.select("h1").text());
 Assert.assertEquals("Hello World", docXhtml.select("h1").text());
 }

 // Testing the getTreeBuilder method of Parser class
 @Test
 public void testGetTreeBuilder() {
 TreeBuilder treeBuilderHtml = parserHtml.getTreeBuilder();
 TreeBuilder treeBuilderXml = parserXml.getTreeBuilder();

 Assert.assertEquals(HtmlTreeBuilder.class, treeBuilderHtml.getClass());
 Assert.assertEquals(XmlTreeBuilder.class, treeBuilderXml.getClass());
 }

 // Testing the isTrackErrors method of Parser class
 @Test
 public void testIsTrackErrors() {
 Assert.assertFalse(parserHtml.isTrackErrors());
 Assert.assertFalse(parserXml.isTrackErrors());

 parserHtml.setTrackErrors(1);
 Assert.assertTrue(parserHtml.isTrackErrors());

 parserXml.setTrackErrors(1);
 Assert.assertTrue(parserXml.isTrackErrors());
 }

 // Testing the setTrackErrors method of Parser class
 @Test
 public void testSetTrackErrors() {
 parserHtml.setTrackErrors(0);
 Assert.assertFalse(parserHtml.isTrackErrors());

 parserXml.setTrackErrors(0);
 Assert.assertFalse(parserXml.isTrackErrors());

 parserHtml.setTrackErrors(10);
 Assert.assertTrue(parserHtml.isTrackErrors());

 parserXml.setTrackErrors(10);
 Assert.assertTrue(parserXml.isTrackErrors());
 }

 // Testing the getErrors method of Parser class
 @Test
 public void testGetErrors() {
 String invalidHtml = "<html><head><script>alert('Hello')</script></head><body>\n<h1>Hello World</h1>\n</body></html>";
 List<ParseError> errorsHtml = parserHtml.parseInput(invalidHtml, "").getErrors();
 List<ParseError> errorsXml = parserXml.parseInput(invalidHtml, "").getErrors();

 Assert.assertNotNull(errorsHtml);
 Assert.assertNotNull(errorsXml);
 Assert.assertTrue(errorsHtml.size() > 0);
 Assert.assertTrue(errorsXml.size() > 0);
 }

 // Testing the parse method of Parser class (HTML)
 @Test
 public void testParse() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(html);

 Assert.assertNotNull(doc);
 Assert.assertEquals(1, doc.body().childNodes().size());
 Assert.assertEquals("Hello World", doc.select("h1").text());
 }

 // Testing the parseFragment method of Parser class (HTML)
 @Test
 public void testParseFragment() {
 String fragmentHtml = "<div><p>Hello</p></div>";
 Element context = Jsoup.parse("<html><body></body></html>").body();
 Document doc = Parser.parseFragment(fragmentHtml, context, "");

 Assert.assertNotNull(doc);
 Assert.assertEquals(1, doc.body().childNodes().size());
 Assert.assertEquals("Hello", doc.select("p").text());
 }

 // Testing the parseBodyFragment method of Parser class (HTML)
 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<html><body>\n<p>Hello World</p>\n</body></html>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "");

 Assert.assertNotNull(doc);
 Assert.assertEquals(1, doc.body().childNodes().size());
 Assert.assertEquals("Hello World", doc.body().text());
 }

 // Testing the parseBodyFragmentRelaxed method of Parser class (HTML)
 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<html><body>\n<p>Hello World</p>\n</body></html>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "");
 Assert.assertNotNull(doc);
 Assert.assertEquals(1, doc.body().childNodes().size());
 Assert.assertEquals("Hello World", doc.body().text());
 }
}