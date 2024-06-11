import org.junit.Test;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Tag;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParseEmptyHtml() {
 Parser parser = new Parser("", "http://www.example.com", false);
 Document doc = parser.parse();

 assertEquals("http://www.example.com", doc.baseUri());
 assertEquals(htmlTag, doc.tagName());
 assertNotNull(doc.body());
 assertFalse(doc.head().elements().hasSize());
 }

 @Test
 public void testParseHtmlWithTextNode() {
 Parser parser = new Parser("<html><body><p>Hello World</p></body></html>", "http://www.example.com", false);
 Document doc = parser.parse();

 assertEquals("http://www.example.com", doc.baseUri());
 assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testParseHtmlWithElement() {
 Parser parser = new Parser("<html><body><p id='test'><b>Hello World</b></p></body></html>", "http://www.example.com", false);
 Document doc = parser.parse();

 assertEquals("http://www.example.com", doc.baseUri());
 Element p = doc.body().elementSibling(0);
 assertEquals("test", p.attr("id"));
 assertEquals("Hello World", p.text());
 assertEquals(2, p.children().size());
 assertEquals("b", p.child(0).tagName());
 }

 @Test
 public void testParseHtmlWithAttributes() {
 Parser parser = new Parser("<html><body><p id='test' class='testClass' title='Test Title'><a href='http://example.com'>Link</a></p></body></html>", "http://www.example.com", false);
 Document doc = parser.parse();

 assertEquals("http://www.example.com", doc.baseUri());
 Element p = doc.body().elementSibling(0);
 assertEquals("test", p.attr("id"));
 assertEquals("testClass", p.classAttr());
 assertEquals("Test Title", p.attr("title"));
 assertEquals("Link", p.child(1).text());
 assertEquals("http://example.com", p.child(1).attr("href"));
 }

 @Test
 public void testParseHtmlWithComments() {
 Parser parser = new Parser("<html><!-- Comment 1 --><body><p>Hello World</p><!-- Comment 2 --></body></html>", "http://www.example.com", false);
 Document doc = parser.parse();

 assertEquals("http://www.example.com", doc.baseUri());
 assertEquals(2, doc.body().children().size());
 assertEquals(1, ((Comment) doc.body().child(0)).text().split("\n").length);
 assertEquals("Hello World", doc.body().child(1).text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidHtml() {
 Parser parser = new Parser("<html><body><p<tag", "http://www.example.com", false);
 try {
 parser.parse();
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseHtmlWithMissingTag() {
 Parser parser = new Parser("<html><body><div><p</body></html>", "http://www.example.com", false);
 try {
 parser.parse();
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}