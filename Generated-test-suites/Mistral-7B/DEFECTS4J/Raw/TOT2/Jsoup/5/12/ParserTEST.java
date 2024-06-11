import org.junit.Test;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParse() {
 Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "http://test.com");
 Document doc = parser.parse();
 Element root = doc.body();
 assertEquals("Hello World", root.getElementsByTag("h1").text());
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<body><h1>Hello World</h1></body>", "http://test.com");
 Document doc = parser.parse();
 Element root = doc.body();
 assertEquals("Hello World", root.getElementsByTag("h1").text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Parser parser = new Parser("<body><h1>Hello World</h1></body>", "http://test.com");
 parser.relaxed = true;
 Document doc = parser.parse();
 Element root = doc.body();
 assertEquals("Hello World", root.getElementsByTag("h1").text());
 }

 @Test
 public void testParseEmptyHtml() {
 Parser parser = new Parser("<html></html>", "http://test.com");
 Document doc = parser.parse();
 assertEquals(0, doc.body().childElementCount());
 }

 @Test
 public void testParseInvalidHtml() {
 Parser parser = new Parser("<html><h1>Hello<script>alert('Test');</script>World</h1>", "http://test.com");
 Document doc = parser.parse();
 Element root = doc.body();
 assertEquals(0, root.childElementCount());
 }

 @Test
 public void testParseInvalidHtmlWithRelaxed() {
 Parser parser = new Parser("<html><h1>Hello<script>alert('Test');</script>World</h1>", "http://test.com");
 parser.relaxed = true;
 Document doc = parser.parse();
 Element root = doc.body();
 assertEquals(0, root.childElementCount());
 }

 @Test
 public void testParseWithBaseTag() {
 Parser parser = new Parser("<html><base href=\"/test\"><body><h1>Hello World</h1><a href=\"test.html\"></a></body></html>", "http://test.com");
 Document doc = parser.parse();
 assertEquals("test.html", doc.baseUri());
 }

 @Test
 public void testParseWithImplicitParent() {
 Parser parser = new Parser("<body><img src=\"image.jpg\"></body>", "http://test.com");
 Document doc = parser.parse();
 Element img = doc.getElementsByTag("img").first();
 assertEquals("image.jpg", img.absUrl("src"));
 }
}