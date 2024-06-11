import org.junit.*;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

public class ParserTest {

   @Test
   public void testParse() {
      String html = "<html><head><title>Test HTML</title></head><body><h1>Hello World</h1></body></html>";
      String baseUri = "http://example.com";
      Document doc = Parser.parse(html, baseUri);
      
      assertNotNull(doc);
      assertEquals("http://example.com", doc.baseUri());
      assertEquals("Test HTML", doc.title());
      assertEquals("Hello World", doc.body().select("h1").text());
   }

   @Test
   public void testParseBodyFragment() {
      String bodyHtml = "<h1>Hello World</h1>";
      String baseUri = "http://example.com";
      Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
      
      assertNotNull(doc);
      assertEquals("http://example.com", doc.baseUri());
      assertEquals("Hello World", doc.body().select("h1").text());
   }

   @Test
   public void testParseComment() {
      String html = "<!-- Test Comment -->";
      String baseUri = "http://example.com";
      Document doc = Parser.parse(html, baseUri);
      
      assertNotNull(doc);
      assertEquals("http://example.com", doc.baseUri());
      assertEquals("<!-- Test Comment -->", doc.body().html());
   }

   @Test
   public void testParseCdata() {
      String html = "<![CDATA[Test CDATA]]>";
      String baseUri = "http://example.com";
      Document doc = Parser.parse(html, baseUri);
      
      assertNotNull(doc);
      assertEquals("http://example.com", doc.baseUri());
      assertEquals("<![CDATA[Test CDATA]]>", doc.body().html());
   }

   @Test
   public void testParseXmlDecl() {
      String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
      String baseUri = "http://example.com";
      Document doc = Parser.parse(html, baseUri);
      
      assertNotNull(doc);
      assertEquals("http://example.com", doc.baseUri());
      assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", doc.body().html());
   }

   @Test
   public void testParseEndTag() {
      String html = "<html><body><h1>Hello World</h1></body></html>";
      String baseUri = "http://example.com";
      Document doc = Parser.parse(html, baseUri);
      
      assertNotNull(doc);
      assertEquals("http://example.com", doc.baseUri());
      assertEquals("Hello World", doc.body().select("h1").text());
   }

   @Test
   public void testParseStartTag() {
      String html = "<html><body><h1>Hello World</h1></body></html>";
      String baseUri = "http://example.com";
      Document doc = Parser.parse(html, baseUri);
      
      assertNotNull(doc);
      assertEquals("http://example.com", doc.baseUri());
      assertEquals("Hello World", doc.body().select("h1").text());
   }

   @Test
   public void testParseTextNode() {
      String html = "<html><body><h1>Hello World</h1></body></html>";
      String baseUri = "http://example.com";
      Document doc = Parser.parse(html, baseUri);
      
      assertNotNull(doc);
      assertEquals("http://example.com", doc.baseUri());
      assertEquals("Hello World", doc.body().select("h1").text());
   }

   @Test
   public void testParseAttribute() {
      String html = "<a href=\"http://example.com\">Link</a>";
      String baseUri = "http://example.com";
      Document doc = Parser.parse(html, baseUri);
      
      assertNotNull(doc);
      assertEquals("http://example.com", doc.baseUri());
      assertEquals("<a href=\"http://example.com\">Link</a>", doc.body().html());
   }

   @Test
   public void testStackHasValidParent() {
      Parser parser = new Parser(html, baseUri, false);
      boolean result = parser.stackHasValidParent(Tag.valueOf("h1"));
      
      assertTrue(result);
   }

   @Test
   public void testPopStackToSuitableContainer() {
      Parser parser = new Parser(html, baseUri, false);
      Element child = new Element(Tag.valueOf("h1"), baseUri);
      Element result = parser.popStackToSuitableContainer(child.tag());
      
      assertNull(result);
   }

   @Test
   public void testPopStackToClose() {
      Parser parser = new Parser(html, baseUri, false);
      Element result = parser.popStackToClose(Tag.valueOf("h1"));
      
      assertNull(result);
   }

   @Test
   public void testLast() {
      Parser parser = new Parser(html, baseUri, false);
      Element result = parser.last();
      
      assertNotNull(result);
   }
}