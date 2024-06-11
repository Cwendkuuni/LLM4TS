import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;


public class DocumentTest {
  
  private Document doc;
  
  @Before
  public void setUp() {
    doc = new Document("http://example.com");
  }
  
  @Test
  public void testConstructor() {
    assertNotNull(doc);
    assertEquals("#root", doc.nodeName());
  }
  
  @Test
  public void testCreateShell() {
    Document shell = Document.createShell("http://example.com");
    assertNotNull(shell);
    assertEquals("html", shell.nodeName());
    assertEquals("head", shell.head().nodeName());
    assertEquals("body", shell.body().nodeName());
  }
  
  @Test
  public void testHead() {
    assertNotNull(doc.head());
    assertEquals("head", doc.head().nodeName());
  }
  
  @Test
  public void testBody() {
    assertNotNull(doc.body());
    assertEquals("body", doc.body().nodeName());
  }
  
  @Test
  public void testTitle() {
    assertEquals("", doc.title());
    
    doc.title("Test Title");
    assertEquals("Test Title", doc.title());
    
    doc.getElementsByTag("title").remove();
    assertEquals("", doc.title());
  }
  
  @Test
  public void testcreateElement() {
    String tagName = "div";
    assertEquals("div", doc.createElement(tagName).nodeName());
  }
  
  @Test
  public void testNormalise() {
    Document normalisedDoc = doc.normalise();
    assertEquals("html", normalisedDoc.getElementsByTag("html").first().nodeName());
    assertEquals("head", normalisedDoc.head().nodeName());
    assertEquals("body", normalisedDoc.body().nodeName());
    
    assertEquals(0, normalisedDoc.select("html").get(0).childNodes.size());
  }
  
  @Test
  public void testOuterHtml() {
    assertEquals("", doc.outerHtml());
    
    doc.appendElement("div").text("Test");
    assertEquals("<div>Test</div>", doc.outerHtml());
  }
  
  @Test
  public void testText() {
    assertEquals("", doc.text());
    
    doc.text("Test");
    assertEquals("Test", doc.text());
    
    doc.text("");
    assertEquals("", doc.text());
  }
  
  @Test
  public void testNodeName() {
    assertEquals("#document", doc.nodeName());
  }
  
}