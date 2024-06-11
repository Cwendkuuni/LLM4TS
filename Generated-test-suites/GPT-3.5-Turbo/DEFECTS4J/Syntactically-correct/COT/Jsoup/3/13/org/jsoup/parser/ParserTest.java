import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

  @Test
  public void testParse() {
    String html = "<html><body><h1>Hello World!</h1></body></html>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);

    assertNotNull(doc);
    assertEquals("http://example.com", doc.baseUri());
    assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World!</h1>\n </body>\n</html>",
        doc.html());
  }

  @Test
  public void testParseBodyFragment() {
    String bodyHtml = "<h1>Hello World!</h1>";
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

    assertNotNull(doc);
    assertEquals("http://example.com", doc.baseUri());
    assertEquals("<body>\n <h1>Hello World!</h1>\n</body>", doc.html());
  }

  @Test
  public void testCommentParsing() {
    String html = "<!-- This is a comment -->";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);

    assertNotNull(doc);
    assertEquals("http://example.com", doc.baseUri());
    assertEquals("<!-- This is a comment -->", doc.html());
  }

  @Test
  public void testCDATAParsing() {
    String html = "<![CDATA[This is CDATA]]>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);

    assertNotNull(doc);
    assertEquals("http://example.com", doc.baseUri());
    assertEquals("<![CDATA[This is CDATA]]>", doc.html());
  }

  @Test
  public void testXmlDeclParsing() {
    String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);

    assertNotNull(doc);
    assertEquals("http://example.com", doc.baseUri());
    assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", doc.html());
  }

  @Test
  public void testEndTagParsing() {
    String html = "<html><body><h1>Hello World!</h1></body></html>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);

    assertNotNull(doc);
    assertEquals("http://example.com", doc.baseUri());
    assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World!</h1>\n </body>\n</html>",
        doc.html());
  }

  @Test
  public void testStartTagParsing() {
    String html = "<html><body><h1>Hello World!</h1></body></html>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);

    assertNotNull(doc);
    assertEquals("http://example.com", doc.baseUri());
    assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World!</h1>\n </body>\n</html>",
        doc.html());
  }

  @Test
  public void testTextNodeParsing() {
    String html = "<h1>Hello World!</h1>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);

    assertNotNull(doc);
    assertEquals("http://example.com", doc.baseUri());
    assertEquals("<h1>Hello World!</h1>", doc.html());
  }

  @Test
  public void testAttributeParsing() {
    String html = "<h1 class=\"title\" id=\"heading\">Hello World!</h1>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);

    assertNotNull(doc);
    assertEquals("http://example.com", doc.baseUri());
    assertEquals("<h1 class=\"title\" id=\"heading\">Hello World!</h1>", doc.html());
  }

  @Test
  public void testEmptyElementParsing() {
    String html = "<img src=\"image.png\"/>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);

    assertNotNull(doc);
    assertEquals("http://example.com", doc.baseUri());
    assertEquals("<img src=\"image.png\">", doc.html());
  }

  @Test
  public void testDataTagParsing() {
    String html = "<script>alert('Hello World!');</script>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);

    assertNotNull(doc);
    assertEquals("http://example.com", doc.baseUri());
    assertEquals("<script>alert('Hello World!');</script>", doc.html());
  }

  @Test
  public void testBaseTagParsing() {
    String html = "<base href=\"http://example.com\">";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);

    assertNotNull(doc);
    assertEquals("http://example.com", doc.baseUri());
    assertEquals("<base href=\"http://example.com\">", doc.html());
  }
}