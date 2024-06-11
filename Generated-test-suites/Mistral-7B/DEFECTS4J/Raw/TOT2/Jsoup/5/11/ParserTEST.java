import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

 @Test
 public void testParse() {
 Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "http://example.com");
 Document doc = parser.parse();

 Element title = doc.title();
 Assert.assertEquals("Test", title.text());
 Element h1 = doc.getElementsByTag("h1").first();
 Assert.assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<p>Hello</p>", "http://example.com");
 Document doc = parser.parseBodyFragment(parser.tq.toString(), "http://example.com");

 Element p = doc.getElementsByTag("p").first();
 Assert.assertEquals("Hello", p.text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Parser parser = new Parser("<p>Hello</p>", "http://example.com");
 parser.relaxed = true;
 Document doc = parser.parse();

 Element p = doc.getElementsByTag("p").first();
 Assert.assertEquals("Hello", p.text());
 }

 @Test
 public void testParseUnknownTagSelfClosed() {
 Parser parser = new Parser("<div><mytag /></div>", "http://example.com");
 Document doc = parser.parse();

 Element div = doc.getElementsByTag("div").first();
 Element mytag = div.getElementsByTag("mytag").first();
 Assert.assertEquals("", mytag.text());
 }

 @Test
 public void testParseUnknownTagNotSelfClosed() {
 Parser parser = new Parser("<div><mytag></mytag></div>", "http://example.com");
 Document doc = parser.parse();

 Element div = doc.getElementsByTag("div").first();
 Element mytag = div.getElementsByTag("mytag").first();
 Assert.assertEquals("", mytag.text());
 }

 @Test
 public void testParseXMLDecl() {
 Parser parser = new Parser("<xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\">", "http://example.com");
 Document doc = parser.parse();

 XmlDeclaration decl = doc.getXmlDeclaration();
 Assert.assertTrue(decl.getVersion() != null);
 Assert.assertEquals("UTF-8", decl.getEncoding());
 Assert.assertTrue(decl.isStandalone());
 }

 @Test
 public void testParseComment() {
 Parser parser = new Parser("<html><!-- This is a comment --></html>", "http://example.com");
 Document doc = parser.parse();

 Comment comment = doc.getElementsByTag("!--").first();
 Assert.assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseEndTagMismatch() {
 Parser parser = new Parser("<html><body><html></body></html>", "http://example.com");
 Document doc = parser.parse();

 // This test should fail
 }

 @Test
 public void testParseInvalidTagName() {
 Parser parser = new Parser("<html><foo></foo></html>", "http://example.com");
 Document doc = parser.parse();

 // This test should fail
 }

 @Test
 public void testParseAttribute() {
 Parser parser = new Parser("<p id=\"test\" class=\"test\" title=\"test\"></p>", "http://example.com");
 Document doc = parser.parse();

 Element p = doc.getElementsByTag("p").first();
 Map<String, Attribute> attributes = p.attributes();

 Assert.assertEquals("test", attributes.get("id").getAttributeValue());
 Assert.assertEquals("test", attributes.get("class").getAttributeValue());
 Assert.assertEquals("test", attributes.get("title").getAttributeValue());
 }

 @Test
 public void testParseAttributeEmptyValue() {
 Parser parser = new Parser("<p id=\"test\" class></p>", "http://example.com");
 Document doc = parser.parse();

 Element p = doc.getElementsByTag("p").first();
 Map<String, Attribute> attributes = p.attributes();

 Assert.assertEquals("", attributes.get("class").getAttributeValue());
 }

 @Test
 public void testParseAttributeQuotedValue() {
 Parser parser = new Parser("<p id=\"test\" class=\"test\" title=\"Some title\"></p>", "http://example.com");
 Document doc = parser.parse();

 Element p = doc.getElementsByTag("p").first();
 Map<String, Attribute> attributes = p.attributes();

 Assert.assertEquals("Some title", attributes.get("title").getAttributeValue());
 }

 @Test
 public void testParseAttributeUnquotedValue() {
 Parser parser = new Parser("<p id=\"test\" class=test title=Some title></p>", "http://example.com");
 Document doc = parser.parse();

 Element p = doc.getElementsByTag("p").first();
 Map<String, Attribute> attributes = p.attributes();

 Assert.assertEquals("test", attributes.get("class").getAttributeValue());
 Assert.assertEquals("Some title", attributes.get("title").getAttributeValue());
 }

 @Test
 public void testParseDataTag() {
 Parser parser = new Parser("<html><body><script src=\"script.js\"></script></body></html>", "http://example.com");
 Document doc = parser.parse();

 Element script = doc.getElementsByTag("script").first();
 TextNode scriptText = (TextNode) script.childNodes().get(0);

 Assert.assertEquals("script.js", scriptText.text());
 }

 @Test
 public void testParseDataTagWithBaseUri() {
 Parser parser = new Parser("<html><body><script src=\"script.js\"></script></body></html>", "http://example.com/index.html");
 Document doc = parser.parse();

 Element script = doc.getElementsByTag("script").first();
 TextNode scriptText = (TextNode) script.childNodes().get(0);

 Assert.assertEquals("script.js", scriptText.text());
 Assert.assertEquals("http://example.com/script.js", script.absUrl("src"));
 }

 @Test
 public void testParseRelativeUrl() {
 Parser parser = new Parser("<html><body><script src=\"scripts/script.js\"></script></body></html>", "http://example.com/index.html");
 Document doc = parser.parse();

 Element script = doc.getElementsByTag("script").first();
 TextNode scriptText = (TextNode) script.childNodes().get(0);

 Assert.assertEquals("scripts/script.js", scriptText.text());
 Assert.assertEquals("http://example.com/scripts/script.js", script.absUrl("src"));
 }

 @Test
 public void testParseBaseTag() {
 Parser parser = new Parser("<html><head><base href=\"/subdir\"></base></head><body><a href=\"../index.html\"></a></body></html>", "http://example.com");
 Document doc = parser.parse();

 Element base = doc.getElementsByTag("base").first();
 String baseUri = doc.baseUri();

 Assert.assertEquals("/subdir", base.absUrl("href"));
 Assert.assertEquals("http://example.com/subdir/index.html", baseUri);
 }
}