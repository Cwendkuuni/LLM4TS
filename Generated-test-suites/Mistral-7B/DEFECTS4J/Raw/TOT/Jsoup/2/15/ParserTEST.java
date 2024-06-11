import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setup() {
 parser = new Parser("<html><head></head><body><h1>Hello World</h1></body></html>", "http://example.com");
 doc = parser.parse();
 }

 @Test
 public void testParseHtml() {
 Element html = doc.getRootElement();
 assertEquals("html", html.tagName());
 assertNull(html.parent());

 Element head = html.child(0);
 assertEquals("head", head.tagName());
 assertEquals(0, head.childrenSize());

 Element body = html.child(1);
 assertEquals("body", body.tagName());

 Element h1 = body.child(0);
 assertEquals("h1", h1.tagName());
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseBodyFragment() {
 parser = new Parser("<p>Hello</p>", "http://example.com");
 doc = parser.parse();

 Element body = doc.getRootElement();
 assertEquals("body", body.tagName());

 Element p = body.child(0);
 assertEquals("p", p.tagName());
 assertEquals("Hello", p.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullHtml() {
 parser = new Parser(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 parser = new Parser("<html></html>", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyBaseUri() {
 parser = new Parser("<html></html>", "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 parser = new Parser("<foo></foo>", "http://example.com");
 }

 @Test
 public void testParseComment() {
 parser = new Parser("<!-- This is a comment -->", "http://example.com");
 doc = parser.parse();

 Element comment = doc.getRootElement().child(0);
 assertEquals("comment", comment.tagName());
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseXmlDeclaration() {
 parser = new Parser("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "http://example.com");
 doc = parser.parse();

 Element xmlDeclaration = doc.getRootElement().child(0);
 assertEquals("xml-declaration", xmlDeclaration.tagName());
 }
}