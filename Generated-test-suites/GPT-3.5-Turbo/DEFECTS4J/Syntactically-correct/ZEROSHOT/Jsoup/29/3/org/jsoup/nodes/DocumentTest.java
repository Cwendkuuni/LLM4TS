import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {
  
  private Document document;
  
  @Before
  public void setUp() {
    document = new Document("baseUri");
  }
  
  @Test
  public void testCreateShell() {
    Document doc = Document.createShell("baseUri");
    assertNotNull(doc);
    assertEquals("html", doc.nodeName());
    assertNotNull(doc.head());
    assertNotNull(doc.body());
  }
  
  @Test
  public void testHead() {
    assertNull(document.head());
  }
  
  @Test
  public void testBody() {
    assertNull(document.body());
  }
  
  @Test
  public void testTitle() {
    assertEquals("", document.title());
    
    document.head().appendElement("title").text("Test Title");
    assertEquals("Test Title", document.title());
    
    document.title("New Title");
    assertEquals("New Title", document.title());
  }
  
  @Test
  public void testcreateElement() {
    Element element = document.createElement("div");
    assertNotNull(element);
    assertEquals("div", element.nodeName());
  }
  
  @Test
  public void testNormalise() {
    document.appendElement("html");
    assertNull(document.getElementsByTag("head").first());
    assertNull(document.getElementsByTag("body").first());
    
    document.normalise();
    
    assertNotNull(document.head());
    assertNotNull(document.body());
  }
  
  @Test
  public void testOuterHtml() {
    assertEquals("", document.outerHtml());
  }
  
  @Test
  public void testText() {
    assertEquals(document, document.text("text"));
    assertEquals("text", document.body().ownText());
  }
  
  @Test
  public void testNodeName() {
    assertEquals("#document", document.nodeName());
  }
  
  @Test
  public void testClone() {
    Document clone = document.clone();
    assertNotNull(clone);
    assertNotSame(clone, document);
    assertEquals(clone.outputSettings().charset(), document.outputSettings().charset());
  }
  
  @Test
  public void testOutputSettings() {
    Document.OutputSettings outputSettings = new Document.OutputSettings();
    outputSettings.prettyPrint(false);
    document.outputSettings(outputSettings);
    assertEquals(outputSettings, document.outputSettings());
  }
  
  @Test
  public void testQuirksMode() {
    assertEquals(Document.QuirksMode.noQuirks, document.quirksMode());
    document.quirksMode(Document.QuirksMode.limitedQuirks);
    assertEquals(Document.QuirksMode.limitedQuirks, document.quirksMode());
  }
}