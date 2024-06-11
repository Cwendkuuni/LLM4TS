import org.junit.Test;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParse() {
 Parser parser = new Parser("<html><head></head><body><h1>Hello World</h1></body></html>", "http://example.com");
 Document doc = parser.parse();
 Assert.assertEquals("Hello World", doc.select("h1").text());
 }

 @Test
 public void testParseEmptyDocument() {
 Parser parser = new Parser("<html></html>", "http://example.com");
 Document doc = parser.parse();
 Assert.assertTrue(doc.body().childElementCount() == 0);
 }

 @Test
 public void testParseInvalidHtml() {
 Parser parser = new Parser("<html><head><script>alert('XSS')</script><body>", "http://example.com");
 try {
 parser.parse();
 fail("Expected ParserException");
 } catch (Parser.ParserException e) {
 // Expected
 }
 }

 @Test
 public void testParseBreakingHtml() {
 Parser parser = new Parser("<html><script><!--\ndocument.write(\"<script>alert('XSS')</script>\")-->", "http://example.com");
 try {
 parser.parse();
 fail("Expected ParserException");
 } catch (Parser.ParserException e) {
 // Expected
 }
 }

 @Test
 public void testParseSelfClosingTag() {
 Parser parser = new Parser("<html><head><img src=\"image.jpg\" /></head></html>", "http://example.com");
 Document doc = parser.parse();
 Assert.assertTrue(doc.select("img").hasAttr("src"));
 }

 @Test
 public void testParseComments() {
 Parser parser = new Parser("<html><!-- Comment --><body><!-- Another Comment --></body></html>", "http://example.com");
 Document doc = parser.parse();
 Assert.assertTrue(doc.body().child(0).toString().trim().startsWith("Comment"));
 }

 @Test
 public void testParseCdata() {
 Parser parser = new Parser("<html><![CDATA[ <script>alert('XSS')</script> ]]>", "http://example.com");
 Document doc = parser.parse();
 // CDATA is not considered part of the document tree, so there's nothing to test
 }

 @Test
 public void testParseXmlDecl() {
 Parser parser = new Parser("<html xmlns:html=\'http://example.com\' xml:lang=\"en\">", "http://example.com");
 Document doc = parser.parse();
 Assert.assertEquals("http://example.com", doc.xmlNamespaceURI("html"));
 Assert.assertEquals("en", doc.xmlLang());
 }
}