import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParserTest {

 @Test
 public void testParse() {
 Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>", "http://test.com");
 Document doc = parser.parse();
 Element title = doc.title();
 Element body = doc.body();
 Element h1 = body.getElementsByTag("h1").first();

 assertEquals("Test", title.text());
 assertEquals("Hello", h1.text());
 assertNotNull(title);
 assertNotNull(body);
 assertNotNull(h1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullHtml() {
 Parser parser = new Parser(null, "http://test.com", false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 Parser parser = new Parser("<html></html>", null, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyHtml() {
 Parser parser = new Parser("", "http://test.com", false);
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<body><h1>Hello</h1></body>", "http://test.com");
 Document doc = parser.parse();
 Element body = doc;
 Element h1 = body.getElementsByTag("h1").first();

 assertEquals("Hello", h1.text());
 assertNotNull(h1);
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Parser parser = new Parser("<body><h1>Hello</h1></body>", "http://test.com");
 parser.relaxed = true;
 Document doc = parser.parse();
 Element body = doc;
 Element h1 = body.getElementsByTag("h1").first();

 assertEquals("Hello", h1.text());
 assertNotNull(h1);
 }

 @Test
 public void testParseEmptyBodyFragment() {
 Parser parser = new Parser("<body></body>", "http://test.com");
 Document doc = parser.parse();
 Element body = doc;

 assertNotNull(body);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMalformedHtml() {
 Parser parser = new Parser("<html</body></html>", "http://test.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSelfClosingTagMissingEndTag() {
 Parser parser = new Parser("<img />", "http://test.com");
 Document doc = parser.parse();
 }

 @Test
 public void testParseComment() {
 Parser parser = new Parser("<html><!-- Test Comment --></html>", "http://test.com");
 Document doc = parser.parse();
 Element comment = doc.getElementsByTag("comment").first();

 assertEquals("Test Comment", comment.text());
 assertNotNull(comment);
 }

 @Test
 public void testParseXmlDecl() {
 Parser parser = new Parser("<html xmlns=\"http://test.com/namespace\" xml:lang=\"en\"></html>", "http://test.com");
 Document doc = parser.parse();
 XmlDeclaration decl = doc.getElementsByTag("xml-declaration").first();

 assertEquals("http://test.com/namespace", decl.getNamespace());
 assertEquals("en", decl.getText());
 assertNotNull(decl);
 }

 @Test
 public void testParseStartTagWithAttributes() {
 Parser parser = new Parser("<div id=\"test\" class=\"test\" style=\"color:red;\" >Hello</div>", "http://test.com");
 Document doc = parser.parse();
 Element div = doc.getElementsByTag("div").first();

 assertEquals("test", div.attr("id"));
 assertEquals("test", div.attr("class"));
 assertEquals("color:red;", div.attr("style"));
 assertEquals("Hello", div.text());
 assertNotNull(div);
 }

 @Test
 public void testParseTextNode() {
 Parser parser = new Parser("<p>This is a test text node</p>", "http://test.com");
 Document doc = parser.parse();
 TextNode textNode = (TextNode) doc.body().text();

 assertEquals("This is a test text node", textNode.text());
 assertNotNull(textNode);
 }

 @Test
 public void testParseCdata() {
 Parser parser = new Parser("<p><![CDATA[This is a test CDATA]]></p>", "http://test.com");
 Document doc = parser.parse();
 TextNode textNode = (TextNode) doc.body().text();

 assertEquals("This is a test CDATA", textNode.text());
 assertNotNull(textNode);
 }

 @Test
 public void testParseScriptTag() {
 Parser parser = new Parser("<script type=\"text/javascript\">function testFunction() { return 1; }</script>", "http://test.com");
 Document doc = parser.parse();
 Element script = doc.getElementsByTag("script").first();

 assertEquals("function testFunction() { return 1; }", script.text());
 assertNotNull(script);
 }
}