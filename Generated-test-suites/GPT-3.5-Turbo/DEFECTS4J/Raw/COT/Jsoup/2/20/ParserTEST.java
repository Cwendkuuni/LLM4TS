import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {
  @Test
  public void testParse() {
    // Arrange
    String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
    String baseUri = "https://www.example.com";
    Document expected = Document.createShell(baseUri);
    expected.head().append("<title>Test</title>");
    expected.body().append("<h1>Hello World</h1>");

    // Act
    Document actual = Parser.parse(html, baseUri);

    // Assert
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testParseBodyFragment() {
    // Arrange
    String bodyHtml = "<h1>Hello World</h1>";
    String baseUri = "https://www.example.com";
    Document expected = Document.createShell(baseUri);
    expected.body().append("<h1>Hello World</h1>");

    // Act
    Document actual = Parser.parseBodyFragment(bodyHtml, baseUri);

    // Assert
    Assert.assertEquals(expected, actual);
  }
}