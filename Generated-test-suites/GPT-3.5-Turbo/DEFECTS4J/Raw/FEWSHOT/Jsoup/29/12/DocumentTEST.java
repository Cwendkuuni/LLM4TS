import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

  @Test
  public void testConstructor() {
    Document doc = new Document("http://www.example.com");
    assertEquals("#root", doc.nodeName());
    assertEquals("http://www.example.com", doc.baseUri());
  }

  @Test
  public void testCreateShell() {
    Document doc = Document.createShell("http://www.example.com");
    assertEquals("http://www.example.com", doc.baseUri());
    assertEquals("html", doc.getElementsByTag("html").get(0).nodeName());
    assertEquals("head", doc.getElementsByTag("head").get(0).nodeName());
    assertEquals("body", doc.getElementsByTag("body").get(0).nodeName());
  }

  @Test
  public void testHead() {
    Document doc = new Document("http://www.example.com");
    Element headEl = doc.appendElement("head");
    assertEquals(headEl, doc.head());
  }

  @Test
  public void testBody() {
    Document doc = new Document("http://www.example.com");
    Element bodyEl = doc.appendElement("body");
    assertEquals(bodyEl, doc.body());
  }

  @Test
  public void testTitle() {
    Document doc = new Document("http://www.example.com");
    doc.head().appendElement("title").text("Test Title");
    assertEquals("Test Title", doc.title());
    
    doc.title("Updated Title");
    assertEquals("Updated Title", doc.title());
  }

  @Test
  public void testCreateElement() {
    Document doc = new Document("http://www.example.com");
    Element elem = doc.createElement("div");
    assertEquals("div", elem.nodeName());
    assertEquals("http://www.example.com", elem.baseUri());
  }

  @Test
  public void testNormalise() {
    Document doc = Document.createShell("http://www.example.com");
    Element htmlEl = doc.getElementsByTag("html").get(0);
    Element headEl = doc.getElementsByTag("head").get(0);
    Element bodyEl = doc.getElementsByTag("body").get(0);
    
    // Create text nodes outside of <body>
    TextNode tn1 = new TextNode("Test1", "");
    TextNode tn2 = new TextNode("Test2", "");
    
    htmlEl.appendChild(tn1);
    headEl.appendChild(tn2);
    
    doc.normalise();
    
    // Check if text nodes are moved to <body>
    assertEquals(2, bodyEl.childNodes().size());
    assertEquals(tn1, bodyEl.childNodes().get(0));
    assertEquals(tn2, bodyEl.childNodes().get(1));
  }

  @Test
  public void testOuterHtml() {
    Document doc = new Document("http://www.example.com");
    doc.appendElement("div").text("Test");
    assertEquals("<div>Test</div>", doc.outerHtml());
  }

  @Test
  public void testText() {
    Document doc = new Document("http://www.example.com");
    doc.body().text("Test");
    assertEquals("Test", doc.text());
  }

  @Test
  public void testOutputSettings() {
    Document doc = new Document("http://www.example.com");
    Document.OutputSettings settings = new Document.OutputSettings();
    settings.escapeMode(Document.Entities.EscapeMode.extended);
    settings.charset("UTF-16");
    settings.prettyPrint(false);
    settings.indentAmount(2);
    
    doc.outputSettings(settings);
    
    assertEquals(settings, doc.outputSettings());
  }

  @Test
  public void testQuirksMode() {
    Document doc = new Document("http://www.example.com");
    Document.QuirksMode quirksMode = Document.QuirksMode.quirks;
    doc.quirksMode(quirksMode);
    
    assertEquals(quirksMode, doc.quirksMode());
  }
}