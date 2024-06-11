import org.jsoup.nodes.*;
import org.junit.Test;

public class ParserTest {

  @Test
  public void testParse() {
    // Typical use case
    String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
    String baseUri = "https://www.example.com";
    Document document = Parser.parse(html, baseUri);
    // Perform assertions on the document
    
    // Edge case: Empty HTML
    html = "";
    baseUri = "https://www.example.com";
    document = Parser.parse(html, baseUri);
    // Perform assertions on the document
    
    // Error scenario: Missing closing tag
    html = "<html><head><title>Test</title><body><h1>Hello World</h1></body>";
    baseUri = "https://www.example.com";
    document = Parser.parse(html, baseUri);
    // Perform assertions on the document
  }

  @Test
  public void testParseBodyFragment() {
    // Typical use case
    String bodyHtml = "<h1>Hello World</h1>";
    String baseUri = "https://www.example.com";
    Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
    // Perform assertions on the document
    
    // Edge case: Empty body HTML
    bodyHtml = "";
    baseUri = "https://www.example.com";
    document = Parser.parseBodyFragment(bodyHtml, baseUri);
    // Perform assertions on the document
    
    // Error scenario: Missing closing tag
    bodyHtml = "<h1>Hello World";
    baseUri = "https://www.example.com";
    document = Parser.parseBodyFragment(bodyHtml, baseUri);
    // Perform assertions on the document
  }

  @Test
  public void testParseRelaxed() {
    // Typical use case
    String bodyHtml = "<h1>Hello World</h1>";
    String baseUri = "https://www.example.com";
    Document document = Parser.parseRelaxed(bodyHtml, baseUri);
    // Perform assertions on the document
    
    // Edge case: Empty body HTML
    bodyHtml = "";
    baseUri = "https://www.example.com";
    document = Parser.parseRelaxed(bodyHtml, baseUri);
    // Perform assertions on the document
    
    // Error scenario: Missing closing tag
    bodyHtml = "<h1>Hello World";
    baseUri = "https://www.example.com";
    document = Parser.parseRelaxed(bodyHtml, baseUri);
    // Perform assertions on the document
  }
}