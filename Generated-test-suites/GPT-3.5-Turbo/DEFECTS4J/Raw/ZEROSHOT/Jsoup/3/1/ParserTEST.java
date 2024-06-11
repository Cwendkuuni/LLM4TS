import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
  
  @Test
  public void testParse() {
    String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
    String baseUri = "http://www.example.com";
  
    Document doc = Parser.parse(html, baseUri);
    
    assertNotNull(doc);
    assertEquals("<html>", doc.childNodes().get(0).outerHtml());
    assertEquals("<head>", doc.childNodes().get(0).childNodes().get(0).outerHtml());
    assertEquals("<title>", doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).outerHtml());
    assertEquals("Test", doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).text());
    assertEquals("<body>", doc.childNodes().get(0).childNodes().get(1).outerHtml());
    assertEquals("<h1>", doc.childNodes().get(0).childNodes().get(1).childNodes().get(0).outerHtml());
    assertEquals("Hello World", doc.childNodes().get(0).childNodes().get(1).childNodes().get(0).text());
  }
  
  @Test
  public void testParseBodyFragment() {
    String bodyHtml = "<h1>Hello World</h1>";
    String baseUri = "http://www.example.com";
    
    Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
    
    assertNotNull(doc);
    assertFalse(doc.hasHead());
    assertTrue(doc.hasBody());
    assertEquals("<body>", doc.childNodes().get(0).outerHtml());
    assertEquals("<h1>", doc.childNodes().get(0).childNodes().get(0).outerHtml());
    assertEquals("Hello World", doc.childNodes().get(0).childNodes().get(0).text());
  }
}