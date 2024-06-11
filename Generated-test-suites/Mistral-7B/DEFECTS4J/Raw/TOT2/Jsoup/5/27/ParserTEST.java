import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParserTest {

 @Test
 public void testParseDocument() {
 String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(html, "http://example.com");

 assertEquals("Test Page", doc.title());
 Element h1 = doc.select("h1").first();
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Hello World</h1></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

 assertEquals("", doc.head().outerHtml());
 Element h1 = doc.select("h1").first();
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<body><h1>Hello World</h1></body>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");

 assertEquals("", doc.head().outerHtml());
 Element h1 = doc.select("h1").first();
 assertEquals("Hello World", h1.text());
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullHtml() {
 String html = null;
 Parser parser = new Parser(html, "http://example.com", false);
 parser.parse(); // should throw a NullPointerException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptyHtml() {
 String html = "";
 Document doc = Parser.parse(html, "http://example.com");
 assertNotNull(doc); // document should not be null
 assertEquals("", doc.outerHtml()); // empty string
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidStartTag() {
 String html = "<html<title>"; // missing '>'
 Document doc = Parser.parse(html, "http://example.com");
 assertNotNull(doc); // document should not be null
 assertEquals("", doc.outerHtml()); // empty string
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidEndTag() {
 String html = "<html><body></htmlhead>"; // 'head' tag not closed
 Document doc = Parser.parse(html, "http://example.com");
 assertNotNull(doc); // document should not be null
 assertEquals("", doc.outerHtml()); // empty string
 }

 @Test
 public void testParseComment() {
 String html = "<html><!-- This is a comment --><body></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 Element comment = doc.select("html comment").first();
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseXmlDeclaration() {
 String html = "<html xmlns:foo=\"bar\">";
 Document doc = Parser.parse(html, "http://example.com");
 XmlDeclaration decl = doc.select("xml-declaration").first();
 assertEquals("http://example.com", decl.baseURI());
 assertEquals("xmlns:foo=\"bar\"", decl.xmlns());
 }

 @Test
 public void testParseEndTag() {
 String html = "<html><body><p>Hello<br/>World</p></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 Element p = doc.select("p").first();
 assertEquals(2, p.childNodeSize());
 }

 @Test
 public void testParseAttribute() {
 String html = "<img src=\"image.jpg\" alt=\"An image\">";
 Document doc = Parser.parse(html, "http://example.com");
 Element img = doc.select("img").first();
 assertEquals("image.jpg", img.attr("src"));
 assertEquals("An image", img.attr("alt"));
 }

 @Test
 public void testParseTextNode() {
 String html = "<p>Hello <there> World</p>";
 Document doc = Parser.parse(html, "http://example.com");
 Element p = doc.select("p").first();
 assertEquals("Hello <there> World", p.text());
 }

 @Test
 public void testParseCdata() {
 String html = "<script><![CDATA[var x = 1;]]></script>";
 Document doc = Parser.parse(html, "http://example.com");
 Element script = doc.select("script").first();
 assertEquals("<![CDATA[var x = 1;]]>", script.outerHtml());
 }
}