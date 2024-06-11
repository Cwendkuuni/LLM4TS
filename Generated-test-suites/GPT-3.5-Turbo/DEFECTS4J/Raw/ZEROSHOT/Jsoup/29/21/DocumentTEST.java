import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DocumentTest {
  
  private Document document;
  
  @Before
  public void setUp() {
    document = new Document("http://www.example.com");
  }
  
  @Test
  public void testCreateShell() {
    Document shell = Document.createShell("http://www.example.com");
    assertNotNull(shell);
    assertTrue(shell.html().contains("<html>"));
    assertTrue(shell.html().contains("<head>"));
    assertTrue(shell.html().contains("<body>"));
  }
  
  @Test
  public void testHead() {
    Element head = document.head();
    assertNull(head);
    
    document.appendElement("head");
    head = document.head();
    assertNotNull(head);
  }
  
  @Test
  public void testBody() {
    Element body = document.body();
    assertNull(body);
    
    document.appendElement("body");
    body = document.body();
    assertNotNull(body);
  }
  
  @Test
  public void testTitle() {
    String title = document.title();
    assertEquals("", title);
    
    document.head().appendElement("title").text("Test Title");
    title = document.title();
    assertEquals("Test Title", title);
  }
  
  @Test
  public void testSetTitle() {
    document.setTitle("Test Title");
    Element titleElement = document.head().getElementsByTag("title").first();
    assertNotNull(titleElement);
    assertEquals("Test Title", titleElement.text());
    
    document.setTitle("Another Title");
    assertEquals("Another Title", titleElement.text());
  }
  
  @Test
  public void testText() {
    document.body().text("Test Body");
    String text = document.body().text();
    assertEquals("Test Body", text);
  }
  
  @Test
  public void testCreateElement() {
    Element element = document.createElement("div");
    assertNotNull(element);
    assertEquals("div", element.tagName());
    assertEquals("http://www.example.com", element.baseUri());
  }
  
  @Test
  public void testNormalise() {
    document.appendElement("html");
    document.appendElement("head");
    document.appendElement("body");
    
    document.normalise();
    assertTrue(document.html().contains("<html>"));
    assertTrue(document.html().contains("<head>"));
    assertTrue(document.html().contains("<body>"));
  }
  
  @Test
  public void testOuterHtml() {
    document.appendElement("div").text("Test Div");
    String html = document.outerHtml();
    assertTrue(html.contains("<div>Test Div</div>"));
  }
  
  @Test
  public void testClone() {
    Document clone = document.clone();
    assertNotNull(clone);
    assertNotSame(document, clone);
    assertNotSame(document.outputSettings(), clone.outputSettings());
  }
  
  @Test
  public void testOutputSettings() {
    Document.OutputSettings outputSettings = document.outputSettings();
    assertNotNull(outputSettings);
    
    document.outputSettings(outputSettings);
    assertSame(outputSettings, document.outputSettings());
  }
  
  @Test
  public void testQuirksMode() {
    Document.QuirksMode quirksMode = document.quirksMode();
    assertNotNull(quirksMode);
    
    document.quirksMode(quirksMode);
    assertSame(quirksMode, document.quirksMode());
  }
}