import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

  @Test
  public void testParse() {
    // Typical use case
    String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1><p>Test Paragraph</p></body></html>";
    String baseUri = "http://www.example.com";
    String expected = "<html>\n" +
                      " <head>\n" +
                      "  <title>Test Title</title>\n" +
                      " </head>\n" +
                      " <body>\n" +
                      "  <h1>Test Heading</h1>\n" +
                      "  <p>Test Paragraph</p>\n" +
                      " </body>\n" +
                      "</html>\n";

    Document doc = Parser.parse(html, baseUri);

    assertEquals(expected, doc.outerHtml());
  }

  @Test
  public void testParseBodyFragment() {
    // Typical use case
    String bodyHtml = "<h1>Test Heading</h1><p>Test Paragraph</p>";
    String baseUri = "http://www.example.com";
    String expected = "<html>\n" +
                      " <head></head>\n" +
                      " <body>\n" +
                      "  <h1>Test Heading</h1>\n" +
                      "  <p>Test Paragraph</p>\n" +
                      " </body>\n" +
                      "</html>\n";

    Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

    assertEquals(expected, doc.outerHtml());
  }

  @Test
  public void testParse_EmptyHTML() {
    // Edge case - empty HTML
    String html = "";
    String baseUri = "http://www.example.com";
    String expected = "<html>\n" +
                      " <head></head>\n" +
                      " <body></body>\n" +
                      "</html>\n";

    Document doc = Parser.parse(html, baseUri);

    assertEquals(expected, doc.outerHtml());
  }

  @Test
  public void testParseBodyFragment_EmptyBodyFragment() {
    // Edge case - empty body fragment
    String bodyHtml = "";
    String baseUri = "http://www.example.com";
    String expected = "<html>\n" +
                      " <head></head>\n" +
                      " <body></body>\n" +
                      "</html>\n";

    Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

    assertEquals(expected, doc.outerHtml());
  }

  @Test
  public void testParse_MissingBaseUri() {
    // Error scenario - missing baseUri
    String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1><p>Test Paragraph</p></body></html>";
    String baseUri = null;

    try {
      Document doc = Parser.parse(html, baseUri);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Base URI must not be null", e.getMessage());
    }
  }

  @Test
  public void testParseBodyFragment_MissingBaseUri() {
    // Error scenario - missing baseUri
    String bodyHtml = "<h1>Test Heading</h1><p>Test Paragraph</p>";
    String baseUri = null;

    try {
      Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Base URI must not be null", e.getMessage());
    }
  }

  @Test
  public void testParse_InvalidHTML() {
    // Error scenario - invalid HTML
    String html = "<html><head><title>Test Title</head><body><h1>Test Heading</h1><p>Test Paragraph</p></body></html>";
    String baseUri = "http://www.example.com";

    try {
      Document doc = Parser.parse(html, baseUri);
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      assertEquals("Unexpected end tag (head).", e.getMessage());
    }
  }

  @Test
  public void testParseBodyFragment_InvalidBodyFragment() {
    // Error scenario - invalid body fragment
    String bodyHtml = "<h1>Test Heading<p>Test Paragraph</p>";
    String baseUri = "http://www.example.com";

    try {
      Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      assertEquals("Unexpected end tag (h1). Expected (p)", e.getMessage());
    }
  }
}