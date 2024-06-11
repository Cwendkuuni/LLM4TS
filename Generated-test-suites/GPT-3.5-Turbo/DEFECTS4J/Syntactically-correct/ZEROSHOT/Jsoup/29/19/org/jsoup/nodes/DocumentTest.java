import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTest {

  @Test
  public void testConstructor() {
    Document doc = new Document("http://example.com");
    assertNotNull(doc);
    assertEquals("#root", doc.nodeName());
    assertEquals("http://example.com", doc.baseUri());
  }

  @Test
  public void testCreateShell() {
    Document doc = Document.createShell("http://example.com");
    assertNotNull(doc);
    assertEquals("html", doc.nodeName());
    assertEquals("http://example.com", doc.baseUri());
    assertNotNull(doc.head());
    assertNotNull(doc.body());
  }

  @Test
  public void testHead() {
    Document doc = Document.createShell("http://example.com");
    assertNotNull(doc);
    assertNotNull(doc.head());
    assertEquals("head", doc.head().nodeName());
  }

  @Test
  public void testBody() {
    Document doc = Document.createShell("http://example.com");
    assertNotNull(doc);
    assertNotNull(doc.body());
    assertEquals("body", doc.body().nodeName());
  }

  @Test
  public void testTitle() {
    Document doc = Document.createShell("http://example.com");
    assertNotNull(doc);
    assertEquals("", doc.title());

    doc.title("Test Title");
    assertEquals("Test Title", doc.title());

    doc.title("");
    assertEquals("", doc.title());
  }

  @Test
  public void testText() {
    Document doc = Document.createShell("http://example.com");
    assertNotNull(doc);
    assertEquals("", doc.text());

    doc.text("Test text");
    assertEquals("Test text", doc.text());

    doc.text("");
    assertEquals("", doc.text());
  }

  @Test
  public void testNormalise() {
    Document doc = Document.createShell("http://example.com");
    assertNotNull(doc);
    assertEquals(doc, doc.normalise());
  }

  @Test
  public void testNormaliseStructure() {
    Document doc = Document.createShell("http://example.com");
    assertNotNull(doc);
    doc.normaliseStructure("head", doc);
    doc.normaliseStructure("body", doc);
  }

  @Test
  public void testCreateElement() {
    Document doc = Document.createShell("http://example.com");
    assertNotNull(doc);
    Element elem = doc.createElement("div");
    assertNotNull(elem);
    assertEquals("div", elem.nodeName());
    assertEquals("http://example.com", elem.baseUri());
  }

  @Test
  public void testOuterHtml() {
    Document doc = Document.createShell("http://example.com");
    assertNotNull(doc);
    assertEquals("<html><head></head><body></body></html>", doc.outerHtml());
  }

  @Test
  public void testNodeName() {
    Document doc = Document.createShell("http://example.com");
    assertNotNull(doc);
    assertEquals("#document", doc.nodeName());
  }

  @Test
  public void testClone() {
    Document doc = Document.createShell("http://example.com");
    assertNotNull(doc);
    Document clone = doc.clone();
    assertNotNull(clone);
    assertNotSame(doc, clone);
    assertEquals(doc.outerHtml(), clone.outerHtml());
  }

  @Test
  public void testOutputSettings() {
    Document doc = Document.createShell("http://example.com");
    assertNotNull(doc);
    Document.OutputSettings outputSettings = doc.outputSettings();
    assertNotNull(outputSettings);

    Document.OutputSettings clone = outputSettings.clone();
    assertNotNull(clone);
    assertNotSame(outputSettings, clone);
    assertEquals(outputSettings.escapeMode(), clone.escapeMode());
    assertEquals(outputSettings.charset(), clone.charset());
    assertEquals(outputSettings.prettyPrint(), clone.prettyPrint());
    assertEquals(outputSettings.indentAmount(), clone.indentAmount());

    Document.OutputSettings newOutputSettings = new Document.OutputSettings();
    assertNotEquals(outputSettings.escapeMode(), newOutputSettings.escapeMode());
    assertNotEquals(outputSettings.charset(), newOutputSettings.charset());
    assertNotEquals(outputSettings.prettyPrint(), newOutputSettings.prettyPrint());
    assertNotEquals(outputSettings.indentAmount(), newOutputSettings.indentAmount());

    doc.outputSettings(newOutputSettings);
    assertSame(newOutputSettings, doc.outputSettings());
  }

  @Test
  public void testQuirksMode() {
    Document doc = Document.createShell("http://example.com");
    assertNotNull(doc);
    Document.QuirksMode quirksMode = doc.quirksMode();
    assertNotNull(quirksMode);

    Document.QuirksMode newQuirksMode = Document.QuirksMode.quirks;
    assertNotEquals(quirksMode, newQuirksMode);

    doc.quirksMode(newQuirksMode);
    assertSame(newQuirksMode, doc.quirksMode());
  }
}