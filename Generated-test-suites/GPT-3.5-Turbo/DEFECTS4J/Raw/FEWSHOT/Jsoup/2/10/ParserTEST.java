import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {
  
  @Test
  public void testParse() {
    // Test parse method with valid HTML
    String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertEquals("<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>", doc.toString());
  }
  
  @Test
  public void testParseBodyFragment() {
    // Test parseBodyFragment method with valid HTML fragment
    String bodyHtml = "<h1>Hello</h1>";
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
    assertEquals("<html><head></head><body><h1>Hello</h1></body></html>", doc.toString());
  }
  
  @Test
  public void testParseComment() {
    // Test parsing HTML comment
    String html = "<!-- This is a comment -->";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertEquals("<html><head></head><body><!-- This is a comment --></body></html>", doc.toString());
  }
  
  @Test
  public void testParseCdata() {
    // Test parsing CDATA section
    String html = "<![CDATA[<h1>Hello</h1>]]>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertEquals("<html><head></head><body>&lt;h1&gt;Hello&lt;/h1&gt;</body></html>", doc.toString());
  }
  
  @Test
  public void testParseXmlDecl() {
    // Test parsing XML declaration
    String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertEquals("<html><head></head><body><?xml version=\"1.0\" encoding=\"UTF-8\"?></body></html>", doc.toString());
  }
  
  @Test
  public void testParseEndTag() {
    // Test parsing end tag
    String html = "<h1>Hello</h1>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertEquals("<html><head></head><body><h1>Hello</h1></body></html>", doc.toString());
  }
  
  @Test
  public void testParseStartTag() {
    // Test parsing start tag
    String html = "<h1>Hello</h1>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertEquals("<html><head></head><body><h1>Hello</h1></body></html>", doc.toString());
  }
  
  @Test
  public void testParseTextNode() {
    // Test parsing text node
    String html = "Hello";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertEquals("<html><head></head><body>Hello</body></html>", doc.toString());
  }
  
  @Test
  public void testParseAttribute() {
    // Test parsing attribute
    String html = "<p class=\"foo\">Hello</p>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertEquals("<html><head></head><body><p class=\"foo\">Hello</p></body></html>", doc.toString());
  }
  
  @Test
  public void testParseEmptyElement() {
    // Test parsing empty element
    String html = "<img src=\"image.jpg\" alt=\"Image\">";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertEquals("<html><head></head><body><img src=\"image.jpg\" alt=\"Image\"></body></html>", doc.toString());
  }
  
  @Test
  public void testParseImplicitParent() {
    // Test parsing element with implicit parent
    String html = "<title>Test</title>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertEquals("<html><head><title>Test</title></head><body></body></html>", doc.toString());
  }
  
  @Test
  public void testParseBaseHref() {
    // Test parsing <base href> tag
    String html = "<base href=\"http://example.com\">";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertEquals("<html><head><base href=\"http://example.com\"></head><body></body></html>", doc.toString());
  }
  
  @Test
  public void testParseUnknownChar() {
    // Test parsing HTML with unknown character
    String html = "<p>Unknown char: â</p>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertEquals("<html><head></head><body><p>Unknown char: â</p></body></html>", doc.toString());
  }
  
  @Test
  public void testPopStackToClose() {
    // Test popStackToClose method
    String html = "<h1>Hello</h1><div><p>Test</p></div>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    assertEquals("<html><head></head><body><h1>Hello</h1><div><p>Test</p></div></body></html>", doc.toString());
    
    Element div = doc.select("div").first();
    div.remove();
    assertEquals("<html><head></head><body><h1>Hello</h1></body></html>", doc.toString());
  }
  
  @Test
  public void testLast() {
    // Test last method
    String html = "<h1>Hello</h1>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    Element last = doc.body().lastElementSibling();
    assertNotNull(last);
    assertEquals("<h1>Hello</h1>", last.toString());
  }
  
}