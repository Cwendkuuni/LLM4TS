import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
  
  private Document document;
  
  @Before
  public void setup() {
    document = new Document("http://example.com");
  }
  
  @Test
  public void testCreateShell() {
    Document shell = Document.createShell("http://example.com");
    assertNotNull(shell);
    assertEquals("html", shell.child(0).tagName());
    assertEquals("head", shell.head().tagName());
    assertEquals("body", shell.body().tagName());
  }
  
  @Test
  public void testTitle() {
    assertEquals("", document.title());
    document.title("Test Title");
    assertEquals("Test Title", document.title());
  }
  
  @Test
  public void testNormalise() {
    document.body().text("Some text");
    document.normalise();
    assertEquals("Some text", document.body().text());
  }
  
  @Test
  public void testCreateElement() {
    Element element = document.createElement("div");
    assertNotNull(element);
    assertEquals("div", element.tagName());
    assertEquals("http://example.com", element.baseUri());
  }
  
  @Test
  public void testOuterHtml() {
    String expectedHtml = "<head></head><body></body>";
    assertEquals(expectedHtml, document.outerHtml());
  }
  
  @Test
  public void testText() {
    document.text("Some text content");
    assertEquals("Some text content", document.body().text());
  }
  
  @Test
  public void testOutputSettings() {
    Document.OutputSettings outputSettings = document.outputSettings();
    assertNotNull(outputSettings);
    assertEquals("base", outputSettings.escapeMode().name());
    assertEquals("UTF-8", outputSettings.charset().name());
    assertTrue(outputSettings.prettyPrint());
    assertEquals(1, outputSettings.indentAmount());
  }
}