
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

  @Test
  public void testCreateShell() {
    Document doc = Document.createShell("http://example.com");
    assertNotNull(doc);
    assertEquals("http://example.com", doc.baseUri());
    assertEquals("html", doc.nodeName());
    assertNotNull(doc.head());
    assertNotNull(doc.body());
    assertEquals("head", doc.head().nodeName());
    assertEquals("body", doc.body().nodeName());
  }

  @Test
  public void testHead() {
    Document doc = Document.createShell("http://example.com");
    Element head = doc.head();
    assertNotNull(head);
    assertEquals("head", head.nodeName());
  }

  @Test
  public void testBody() {
    Document doc = Document.createShell("http://example.com");
    Element body = doc.body();
    assertNotNull(body);
    assertEquals("body", body.nodeName());
  }

  @Test
  public void testTitle() {
    Document doc = Document.createShell("http://example.com");
    String title = doc.title();
    assertEquals("", title);
    doc.title("Page Title");
    assertEquals("Page Title", doc.title());
  }

  @Test
  public void testTitleWithExistingTitleElement() {
    Document doc = Document.createShell("http://example.com");
    doc.title("Page Title");
    String title = doc.title();
    assertEquals("Page Title", title);
    doc.title("Updated Title");
    assertEquals("Updated Title", doc.title());
  }

  @Test
  public void testNormalise() {
    Document doc = Document.createShell("http://example.com");
    doc.normalise();
    Element head = doc.head();
    assertNotNull(head);
    Element body = doc.body();
    assertNotNull(body);
  }

  @Test
  public void testNormaliseWithExistingHtmlElement() {
    Document doc = Document.createShell("http://example.com");
    Element html = doc.appendElement("html");
    Element head = doc.head();
    html.appendChild(head);
    doc.normalise();
    assertNotEquals(html, doc.getElementsByTag("html").first());
    Element newHtml = doc.getElementsByTag("html").first();
    assertEquals(newHtml, doc.body().parent());
  }

  @Test
  public void testNormaliseTextNodes() {
    Document doc = Document.createShell("http://example.com");
    Element body = doc.body();
    body.appendChild(new TextNode("Test", ""));
    doc.normaliseTextNodes(doc);
    Node firstChild = doc.body().childNode(0);
    assertTrue(firstChild instanceof TextNode);
    assertEquals(" ", firstChild.outerHtml());
    Node secondChild = doc.body().childNode(1);
    assertTrue(secondChild instanceof TextNode);
    assertEquals("Test", secondChild.outerHtml());
  }

  @Test
  public void testClone() {
    Document doc1 = Document.createShell("http://example.com");
    Document doc2 = doc1.clone();
    assertNotSame(doc1, doc2);
    assertEquals(doc1.baseUri(), doc2.baseUri());
    assertEquals(doc1.nodeName(), doc2.nodeName());
    assertEquals(doc1.outputSettings(), doc2.outputSettings());
  }

  @Test
  public void testOutputSettings() {
    Document doc = Document.createShell("http://example.com");
    Document.OutputSettings settings = doc.outputSettings();
    assertNotNull(settings);
    assertEquals("UTF-8", settings.charset().name());
    assertEquals(true, settings.prettyPrint());
    assertEquals(1, settings.indentAmount());

    settings.charset(Charset.forName("ISO-8859-1"));
    doc.outputSettings(settings);
    assertEquals("ISO-8859-1", doc.outputSettings().charset().name());

    settings.prettyPrint(false);
    doc.outputSettings(settings);
    assertEquals(false, doc.outputSettings().prettyPrint());

    settings.indentAmount(2);
    doc.outputSettings(settings);
    assertEquals(2, doc.outputSettings().indentAmount());
  }

  @Test
  public void testQuirksMode() {
    Document doc = Document.createShell("http://example.com");
    assertEquals(Document.QuirksMode.noQuirks, doc.quirksMode());
    doc.quirksMode(Document.QuirksMode.quirks);
    assertEquals(Document.QuirksMode.quirks, doc.quirksMode());
  }

  @Test
  public void testCreateElement() {
    Document doc = Document.createShell("http://example.com");
    Element element = doc.createElement("div");
    assertNotNull(element);
    assertEquals("div", element.nodeName());
  }

  @Test
  public void testText() {
    Document doc = Document.createShell("http://example.com");
    doc.text("Hello, World!");
    assertEquals("Hello, World!", doc.body().text());
  }

  @Test
  public void testOuterHtml() {
    Document doc = Document.createShell("http://example.com");
    doc.text("Hello, World!");
    assertEquals("Hello, World!", doc.outerHtml());
  }

}
