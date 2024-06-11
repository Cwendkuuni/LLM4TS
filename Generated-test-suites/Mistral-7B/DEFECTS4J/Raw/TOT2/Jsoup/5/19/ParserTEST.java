import org.junit.Test;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attribute;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParse() {
 Parser parser = new Parser("<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>", "http://example.com");
 Document doc = parser.parse();
 Element html = doc;

 // Check if the parsed Document has the expected HTML structure
 assertEquals("html", html.tagName());
 assertEquals("http://example.com", html.baseUri());

 Element head = html.child(0);
 assertEquals("head", head.tagName());

 Element title = head.child(0);
 assertEquals("title", title.tagName());
 assertEquals("Test Title", title.text());

 Element body = html.child(1);
 assertEquals("body", body.tagName());

 Element h1 = body.child(0);
 assertEquals("h1", h1.tagName());
 assertEquals("Test Heading", h1.text());
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<p>Test Paragraph</p>", "http://example.com");
 Document doc = parser.parseBodyFragment(parser.tq.html(), "http://example.com");
 Element body = doc.body();

 // Check if the parsed Document has the expected HTML structure
 assertEquals("body", body.tagName());
 assertEquals("http://example.com", body.baseUri().toString());

 Element p = body.child(0);
 assertEquals("p", p.tagName());
 assertEquals("Test Paragraph", p.text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Parser parser = new Parser("<div class=\"test\">Test Div</div>", "http://example.com");
 parser.relaxed = true;
 Document doc = parser.parseBodyFragment(parser.tq.html(), "http://example.com");
 Element body = doc.body();

 // Check if the parsed Document has the expected HTML structure
 assertEquals("body", body.tagName());
 assertEquals("http://example.com", body.baseUri().toString());

 Element div = body.child(0);
 assertEquals("div", div.tagName());
 assertEquals("Test Div", div.text());

 // Check if the implicit parent tag is created
 assertEquals("div", div.parent().tagName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullHtml() {
 Parser parser = new Parser(null, "http://example.com", false);
 // This test should throw an IllegalArgumentException
 parser.parse();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 Parser parser = new Parser("<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>", null, false);
 // This test should throw an IllegalArgumentException
 parser.parse();
 }
}