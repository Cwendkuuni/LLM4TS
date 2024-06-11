import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
  
  private Document document;
  
  @Before
  public void setUp() {
    document = new Document("https://example.com");
  }
  
  @Test
  public void testCreateShell() {
    Document shell = Document.createShell("https://example.com");
    
    assertNotNull(shell);
    assertNotNull(shell.head());
    assertNotNull(shell.body());
    
    assertEquals("https://example.com", shell.baseUri());
    assertEquals("html", shell.head().nodeName());
    assertEquals("head", shell.head().parent().nodeName());
    assertEquals("body", shell.body().nodeName());
    assertEquals("body", shell.body().parent().nodeName());
  }
  
  @Test
  public void testHead() {
    assertNull(document.head());
    
    document.head().appendElement("title").text("Test Title");
    
    assertNotNull(document.head());
    assertEquals("head", document.head().nodeName());
    assertEquals("title", document.head().child(0).nodeName());
    assertEquals("Test Title", document.head().child(0).text());
  }
  
  @Test
  public void testBody() {
    assertNull(document.body());
    
    document.body().appendElement("div");
    
    assertNotNull(document.body());
    assertEquals("body", document.body().nodeName());
    assertEquals("div", document.body().child(0).nodeName());
  }
  
  @Test
  public void testTitle() {
    assertEquals("", document.title());
    
    document.appendElement("head");
    
    assertEquals("", document.title());
    
    document.title("Test Title");
    
    assertEquals("Test Title", document.title());
    
    document.appendElement("title").text("H2G2");
    
    document.title("Test Title");
    
    assertEquals("Test Title", document.getElementsByTag("title").first().text());
  }
  
  @Test
  public void testNormalise() {
    assertNull(document.getElementsByTag("html").first());
    assertNull(document.getElementsByTag("head").first());
    assertNull(document.getElementsByTag("body").first());
    
    document.normalise();
    
    assertNotNull(document.getElementsByTag("html").first());
    assertNotNull(document.getElementsByTag("head").first());
    assertNotNull(document.getElementsByTag("body").first());
    
    assertEquals("html", document.child(0).nodeName());
    assertEquals("head", document.head().nodeName());
    assertEquals("body", document.body().nodeName());
  }
  
  @Test
  public void testcreateElement() {
    assertEquals("div", document.createElement("div").tagName());
    assertEquals("p", document.createElement("p").tagName());
    assertEquals("a", document.createElement("a").tagName());
  }
  
  @Test
  public void testouterHtml() {
    document.appendElement("h1").text("Test Heading");
    document.appendElement("p").text("Test Paragraph");
    
    assertEquals("<h1>Test Heading</h1>\n<p>Test Paragraph</p>", document.outerHtml());
  }
  
  @Test
  public void testText() {
    assertNull(document.body());
    
    document.appendElement("body");
    
    assertNull(document.body().text());
    
    document.text("Test Text");
    
    assertEquals("Test Text", document.body().text());
  }
  
  @Test
  public void testOutputSettings() {
    assertNotNull(document.outputSettings());
    
    assertEquals(Entities.EscapeMode.base, document.outputSettings().escapeMode());
    assertEquals(Charset.forName("UTF-8"), document.outputSettings().charset());
    assertTrue(document.outputSettings().prettyPrint());
    assertEquals(1, document.outputSettings().indentAmount());
    
    document.outputSettings().escapeMode(Entities.EscapeMode.extended);
    document.outputSettings().charset(Charset.forName("ISO-8859-1"));
    document.outputSettings().prettyPrint(false);
    document.outputSettings().indentAmount(2);
    
    assertEquals(Entities.EscapeMode.extended, document.outputSettings().escapeMode());
    assertEquals(Charset.forName("ISO-8859-1"), document.outputSettings().charset());
    assertFalse(document.outputSettings().prettyPrint());
    assertEquals(2, document.outputSettings().indentAmount());
  }

}