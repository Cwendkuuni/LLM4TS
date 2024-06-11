import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Document.QuirksMode;
import org.jsoup.parser.Tag;
import org.jsoup.helper.Validate;
import org.jsoup.select.Elements;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {
  
  @Test
  public void testDocumentConstructor() {
    Document doc = new Document("https://example.com");
    assertNotNull(doc);
    assertEquals("#root", doc.nodeName());
    assertEquals("https://example.com", doc.baseUri());
  }
  
  @Test
  public void testCreateShell() {
    Document doc = Document.createShell("https://example.com");
    assertNotNull(doc);
    assertEquals("html", doc.children().get(0).nodeName());
    assertEquals("head", doc.head().nodeName());
    assertEquals("body", doc.body().nodeName());
  }

  @Test
  public void testTitle() {
    Document doc = new Document("https://example.com");
    doc.title("Test Title");
    assertEquals("Test Title", doc.title());
  }

  @Test
  public void testNormalise() {
    Document doc = new Document("https://example.com");
    doc.title("Test Title");
    doc.normalise();
    assertEquals(1, doc.getElementsByTag("html").size());
    assertEquals(1, doc.getElementsByTag("head").size());
    assertEquals(1, doc.getElementsByTag("body").size());
  }

  @Test
  public void testClone() {
    Document doc = new Document("https://example.com");
    Document clonedDoc = doc.clone();
    assertNotEquals(doc, clonedDoc);
    assertEquals(doc.outputSettings().charset(), clonedDoc.outputSettings().charset());
  }

  @Test
  public void testOutputSettings() {
    Document doc = new Document("https://example.com");
    OutputSettings outputSettings = new OutputSettings();
    outputSettings.prettyPrint(false);
    doc.outputSettings(outputSettings);
    assertEquals(outputSettings, doc.outputSettings());
  }

  @Test
  public void testQuirksMode() {
    Document doc = new Document("https://example.com");
    doc.quirksMode(QuirksMode.limitedQuirks);
    assertEquals(QuirksMode.limitedQuirks, doc.quirksMode());
  }
}