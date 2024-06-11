import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {
  
  private Document document;

  @Before
  public void setUp() {
    document = new Document("http://example.com");
  }

  @Test
  public void testConstructor() {
    assertEquals("#root", document.tagName());
    assertEquals("http://example.com", document.baseUri());
    assertEquals("", document.text());
  }

  @Test
  public void testCreateShell() {
    Document shellDoc = Document.createShell("http://example.com");
    assertTrue(shellDoc.hasElementById("html"));
    assertTrue(shellDoc.hasElementById("head"));
    assertTrue(shellDoc.hasElementById("body"));
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
  public void testTitle() {
    assertEquals("", document.title());
    
    document.head().appendElement("title").text("Test Title");
    assertEquals("Test Title", document.title());
  }

  @Test
  public void testSetTitle() {
    document.title("Test Title");
    assertEquals("Test Title", document.title());
  }

  @Test
  public void testCreateElement() {
    Element div = document.createElement("div");
    assertNotNull(div);
    assertEquals("div", div.tagName());
    assertEquals("http://example.com", div.baseUri());
  }

  @Test
  public void testNormalise() {
    document.createElement("head");
    document.createElement("body");
    document.appendElement("title").text("Test Title");
    
    Document normalisedDoc = document.normalise();
    
    assertEquals(1, normalisedDoc.getElementsByTag("head").size());
    assertEquals(1, normalisedDoc.getElementsByTag("body").size());
    assertEquals(1, normalisedDoc.getElementsByTag("title").size());
  }

  @Test
  public void testOuterHtml() {
    assertEquals("", document.outerHtml());
    
    document.appendElement("div");
    
    assertEquals("<div></div>", document.outerHtml());
  }

  @Test
  public void testText() {
    document.text("Test Text");
    assertEquals("Test Text", document.text());
  }

  @Test
  public void testClone() {
    Document cloneDoc = document.clone();
    
    assertNotSame(document, cloneDoc);
    assertEquals(document.baseUri(), cloneDoc.baseUri());
    assertEquals(document.head().tagName(), cloneDoc.head().tagName());
    assertEquals(document.body().tagName(), cloneDoc.body().tagName());
  }

  @Test
  public void testOutputSettings() {
    Document.OutputSettings outputSettings = new Document.OutputSettings();
    outputSettings.indentAmount(2);
    document.outputSettings(outputSettings);
    
    assertEquals(2, document.outputSettings().indentAmount());
  }

  @Test
  public void testQuirksMode() {
    assertEquals(Document.QuirksMode.noQuirks, document.quirksMode());
    
    document.quirksMode(Document.QuirksMode.quirks);
    assertEquals(Document.QuirksMode.quirks, document.quirksMode());
  }

}