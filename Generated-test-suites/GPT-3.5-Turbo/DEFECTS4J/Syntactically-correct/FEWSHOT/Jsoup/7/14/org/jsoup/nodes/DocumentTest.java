
package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTest {

  @Test
  public void testDocument() {
    Document document = new Document("http://example.com");
    assertNotNull(document.baseUri());
    assertEquals("#root", document.nodeName());
  }

  @Test
  public void testCreateShell() {
    Document document = Document.createShell("http://example.com");
    assertNotNull(document.baseUri());
    assertEquals("#root", document.nodeName());
    assertNotNull(document.head());
    assertEquals("head", document.head().nodeName());
    assertNotNull(document.body());
    assertEquals("body", document.body().nodeName());
  }

  @Test
  public void testHead() {
    Document document = new Document("http://example.com");
    assertNull(document.head());
  }

  @Test
  public void testBody() {
    Document document = new Document("http://example.com");
    assertNull(document.body());
  }

  @Test
  public void testTitle() {
    Document document = new Document("http://example.com");
    assertEquals("", document.title());
    document.title("Test Title");
    assertEquals("Test Title", document.title());
  }

  @Test
  public void testTitleNull() {
    Document document = new Document("http://example.com");
    assertEquals("", document.title());
    document.title(null);
    assertEquals("", document.title());
  }

  @Test
  public void testTitleEmpty() {
    Document document = new Document("http://example.com");
    assertEquals("", document.title());
    document.title("");
    assertEquals("", document.title());
  }

  @Test
  public void testCreateElement() {
    Document document = new Document("http://example.com");
    Element element = document.createElement("div");
    assertNotNull(element);
    assertEquals("div", element.nodeName());
  }

  @Test
  public void testNormalise() {
    Document document = new Document("http://example.com");
    assertEquals(document, document.normalise());
  }

  @Test
  public void testOuterHtml() {
    Document document = new Document("http://example.com");
    assertEquals("", document.outerHtml());
  }

  @Test
  public void testText() {
    Document document = new Document("http://example.com");
    String text = "Some text";
    assertSame(document, document.text(text));
    assertEquals(text, document.body().text());
  }

  @Test
  public void testNodeName() {
    Document document = new Document("http://example.com");
    assertEquals("#document", document.nodeName());
  }

  @Test
  public void testOutputSettings() {
    Document document = new Document("http://example.com");
    assertNotNull(document.outputSettings());
    assertNotNull(document.outputSettings().escapeMode());
    assertNotNull(document.outputSettings().charset());
    assertNotNull(document.outputSettings().encoder());
    assertTrue(document.outputSettings().prettyPrint());
    assertEquals(1, document.outputSettings().indentAmount());
  }
}
