import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DocumentTest {
  
  private Document document;
  
  @Before
  public void setUp() {
    document = new Document("https://www.example.com");
  }
  
  @Test
  public void testCreateShell() {
    Document shell = Document.createShell("https://www.example.com");
    
    assertNotNull(shell.head());
    assertNotNull(shell.body());
    assertEquals("html", shell.tagName());
    assertEquals("head", shell.head().tagName());
    assertEquals("body", shell.body().tagName());
  }
  
  @Test
  public void testHead() {
    assertNotNull(document.head());
    assertEquals("head", document.head().tagName());
  }
  
  @Test
  public void testBody() {
    assertNotNull(document.body());
    assertEquals("body", document.body().tagName());
  }
  
  @Test
  public void testTitle_noTitleSet() {
    String title = document.title();
    
    assertEquals("", title);
  }
  
  @Test
  public void testTitle_titleSet() {
    document.head().appendElement("title").text("Example Title");
    
    String title = document.title();
    
    assertEquals("Example Title", title);
  }
  
  @Test
  public void testTitle_updateExistingTitle() {
    document.head().appendElement("title").text("Old Title");
    document.title("New Title");
    
    String title = document.title();
    
    assertEquals("New Title", title);
  }
  
  @Test
  public void testTitle_addToHead() {
    document.title("New Title");
    
    Element titleElement = document.head().getElementsByTag("title").first();
    String title = document.title();
    
    assertNotNull(titleElement);
    assertEquals("New Title", title);
  }
  
  @Test
  public void testTitle_notAddToHead() {
    document.title("New Title");
    
    Element titleElement = document.getElementsByTag("title").first();
    String title = document.title();
    
    assertNull(titleElement);
    assertEquals("New Title", title);
  }
  
  @Test
  public void testCreateElement() {
    Element element = document.createElement("div");
    
    assertNotNull(element);
    assertEquals("div", element.tagName());
    assertEquals("https://www.example.com", element.baseUri());
  }
  
  @Test
  public void testNormalise() {
    document.createElement("p").text("Text 1");
    document.appendElement("div").text("Text 2");
    document.normalise();
    
    Element body = document.body();
    assertEquals("Text 1 Text 2", body.text());
    assertTrue(body.childNodes().isEmpty());
  }
  
  @Test
  public void testOuterHtml() {
    document.createElement("div").text("Example Text");
    
    String outerHtml = document.outerHtml();
    assertEquals("<div>Example Text</div>", outerHtml);
  }
  
  @Test
  public void testText() {
    document.text("Example Text");
    
    Element body = document.body();
    assertEquals("Example Text", body.text());
  }
  
  @Test
  public void testNodeName() {
    String nodeName = document.nodeName();
    
    assertEquals("#document", nodeName);
  }
}