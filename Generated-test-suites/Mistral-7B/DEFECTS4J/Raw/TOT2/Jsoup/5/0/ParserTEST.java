import org.junit.Test;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 @Test
 public void testParseDocument() {
 String html = "<html><head><title>Test Title</title></head><body><h1>Test Header</h1></body></html>";
 Document doc = Parser.parse(html, "http://test.com");
 assertEquals("Test Title", doc.title().text());
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 assertNotNull(doc.title());
 assertNotNull(doc.head().title());
 assertEquals("Test Header", doc.body().text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Test Header</h1></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
 assertNotNull(doc);
 assertNotNull(doc.head());
 assertNull(doc.title());
 assertEquals("Test Header", doc.body().text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<body><h1>Test Header</h1>"; // missing closing tag
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://test.com");
 assertNotNull(doc);
 assertNotNull(doc.head());
 assertNull(doc.title());
 assertEquals("Test Header", doc.body().text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullHtml() {
 Parser parser = new Parser(null, "http://test.com", false);
 parser.parse(); // should throw exception
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 Parser parser = new Parser("<html></html>", null, false);
 parser.parse(); // should throw exception
 }

 @Test
 public void testParseWithEmptyHtml() {
 Parser parser = new Parser("", "http://test.com", false);
 Document doc = parser.parse();
 assertNotNull(doc);
 assertNotNull(doc.head());
 assertNull(doc.title());
 assertNotNull(doc.body());
 assertEquals("", doc.body().text());
 }

 @Test
 public void testParseWithInvalidTagName() {
 Parser parser = new Parser("<invalidTag></invalidTag>", "http://test.com", false);
 parser.parse(); // should not throw exception, but should not parse validly
 Document doc = parser.parse();
 assertNotNull(doc);
 assertNotNull(doc.head());
 assertNull(doc.title());
 assertNotNull(doc.body());
 assertTrue(doc.body().children().isEmpty());
 }

 @Test
 public void testParseWithMismatchedTags() {
 Parser parser = new Parser("<html><body><h1>Test Header</h1></body></html>", "http://test.com");
 parser.parse(); // should throw exception
 }
}