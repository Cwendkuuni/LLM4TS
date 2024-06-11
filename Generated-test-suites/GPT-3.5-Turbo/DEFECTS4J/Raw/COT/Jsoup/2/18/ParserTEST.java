import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

  @Test
  public void testParse() {
    String html = "<html><body><h1>Hello World</h1></body></html>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);

    assertNotNull(doc);
    assertEquals("Hello World", doc.body().select("h1").text());
  }

  @Test
  public void testParseBodyFragment() {
    String bodyHtml = "<h1>Hello World</h1>";
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

    assertNotNull(doc);
    assertEquals("Hello World", doc.body().select("h1").text());
  }

  @Test
  public void testParseComment() {
    String html = "<!-- Testing comment -->";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);

    assertNotNull(doc);
    assertEquals("<!-- Testing comment -->", doc.body().html());
  }

  @Test
  public void testParseCdata() {
    String html = "<![CDATA[<h1>Hello World</h1>]]>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);

    assertNotNull(doc);
    assertEquals("<h1>Hello World</h1>", doc.body().html());
  }

  @Test
  public void testParseXmlDecl() {
    String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);

    assertNotNull(doc);
    assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", doc.html());
  }

  @Test
  public void testParseEndTag() {
    String html = "<h1>Hello World</h1>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);

    assertNotNull(doc);
    assertEquals("Hello World", doc.body().select("h1").text());
  }

  // Additional test cases for edge cases and exception handling scenarios
  @Test
  public void testParseEmptyHtml() {
    String html = "";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);

    assertNotNull(doc);
    assertEquals("", doc.html());
  }

  @Test
  public void testParseEmptyBodyFragment() {
    String bodyHtml = "";
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

    assertNotNull(doc);
    assertEquals("", doc.body().html());
  }

  @Test(expected = NullPointerException.class)
  public void testParseNullHtml() {
    String html = null;
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
  }

  @Test(expected = NullPointerException.class)
  public void testParseBodyFragmentNullHtml() {
    String bodyHtml = null;
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
  }

  @Test(expected = NullPointerException.class)
  public void testParseNullBaseUri() {
    String html = "<h1>Hello World</h1>";
    String baseUri = null;
    Document doc = Parser.parse(html, baseUri);
  }

  @Test(expected = NullPointerException.class)
  public void testParseBodyFragmentNullBaseUri() {
    String bodyHtml = "<h1>Hello World</h1>";
    String baseUri = null;
    Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParseInvalidHtml() {
    String html = "<h1>Hello World";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParseBodyFragmentInvalidHtml() {
    String bodyHtml = "<h1>Hello World";
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
  }

  @Test
  public void testParseNestedTags() {
    String html = "<div><p><span>Hello World</span></p></div>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);

    assertNotNull(doc);
    assertEquals("Hello World", doc.select("span").text());
  }

  @Test
  public void testParseAttribute() {
    String html = "<div class=\"example\">Hello World</div>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);

    assertNotNull(doc);
    assertEquals("example", doc.select("div").attr("class"));
  }

}