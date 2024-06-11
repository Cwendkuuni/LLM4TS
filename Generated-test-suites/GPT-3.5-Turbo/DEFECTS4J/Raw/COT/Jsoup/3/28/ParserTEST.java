
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

  @Test
  public void testParse() {
    String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertEquals("Test", doc.title());
    assertEquals("Hello World", doc.body().child(0).text());
  }

  @Test
  public void testParseBodyFragment() {
    String bodyHtml = "<h1>Hello World</h1>";
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
    assertEquals("Hello World", doc.body().child(0).text());
  }

  @Test
  public void testParseBodyFragmentWithRelaxedParsing() {
    String bodyHtml = "<p>Test</p>";
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
    assertEquals("Test", doc.body().child(0).text());
  }

  @Test
  public void testParseComment() {
    String html = "<!-- This is a comment -->";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertEquals(" This is a comment ", doc.body().ownText());
  }

  @Test
  public void testParseCdata() {
    String html = "<![CDATA[<p>Test</p>]]>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertEquals("<p>Test</p>", doc.body().ownText());
  }

  @Test
  public void testParseXmlDecl() {
    String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertNull(doc.body());
  }

  @Test
  public void testParseEndTag() {
    String html = "<div><p>Test</p></div>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertEquals("Test", doc.body().child(0).text());
  }

  @Test
  public void testParseStartTag() {
    String html = "<div><p>Test</p></div>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertEquals("Test", doc.body().child(0).text());
  }

  @Test
  public void testParseTextNode() {
    String html = "This is a text node";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertEquals("This is a text node", doc.body().ownText());
  }

  @Test
  public void testParseAttribute() {
    String html = "<div class=\"test\">Test</div>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertEquals("test", doc.body().getElementsByClass("test").get(0).className());
  }
}

