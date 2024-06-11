import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {
  
  @Test
  public void testParse() {
    String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    
    assertNotNull(doc);
    assertEquals("http://example.com", doc.baseUri());
    assertEquals("Test", doc.title());
    assertTrue(doc.hasText());
    assertEquals("Hello World!", doc.text());
  }
  
  @Test
  public void testParseBodyFragment() {
    String fragment = "<div><p>This is a fragment</p></div>";
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragment(fragment, baseUri);
    
    assertNotNull(doc);
    assertEquals("http://example.com", doc.baseUri());
    assertFalse(doc.hasTitle());
    assertTrue(doc.hasText());
    assertEquals("This is a fragment", doc.text());
  }
  
  @Test
  public void testParseComment() {
    String html = "<!-- This is a comment -->";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    
    assertNotNull(doc);
    assertTrue(doc.hasText());
    assertEquals(" This is a comment ", doc.text());
  }
  
  @Test
  public void testParseCdata() {
    String html = "<![CDATA[This is CDATA]]>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    
    assertNotNull(doc);
    assertTrue(doc.hasText());
    assertEquals("This is CDATA", doc.text());
  }
  
  @Test
  public void testParseXmlDecl() {
    String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    
    assertNotNull(doc);
    assertFalse(doc.hasText());
    assertEquals("http://example.com", doc.baseUri());
  }
  
  @Test
  public void testParseEndTag() {
    String html = "<div>Hello World!</div>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    
    assertNotNull(doc);
    assertTrue(doc.hasText());
    assertEquals("Hello World!", doc.text());
  }
  
  @Test
  public void testParseStartTag() {
    String html = "<h1>Hello World!</h1>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    
    assertNotNull(doc);
    assertTrue(doc.hasText());
    assertEquals("Hello World!", doc.text());
  }
  
  @Test
  public void testParseAttribute() {
    String html = "<a href=\"http://example.com\">Link</a>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    
    assertNotNull(doc);
    assertFalse(doc.hasText());
    
    Element link = doc.select("a").first();
    assertNotNull(link);
    assertTrue(link.hasText());
    assertEquals("Link", link.text());
    assertEquals("http://example.com", link.attr("href"));
  }
  
  @Test
  public void testParseTextNode() {
    String html = "<p>This is a <em>test</em> paragraph.</p>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    
    assertNotNull(doc);
    assertFalse(doc.hasTitle());
    assertTrue(doc.hasText());
    assertEquals("This is a test paragraph.", doc.text());
  }
}