package org.jsoup.nodes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTest {
  
  private Document document;

  @Before
  public void setUp() {
    document = new Document("https://www.example.com");
  }

  @Test
  public void testCreateShell() {
    Document shell = Document.createShell("https://www.example.com");
    assertNotNull(shell);
    assertEquals("html", shell.nodeName());
    assertEquals("head", shell.head().nodeName());
    assertEquals("body", shell.body().nodeName());
  }
  
  @Test
  public void testHead() {
    Element head = document.head();
    assertNotNull(head);
    assertEquals("head", head.nodeName());
  }

  @Test
  public void testBody() {
    Element body = document.body();
    assertNotNull(body);
    assertEquals("body", body.nodeName());
  }
  
  @Test
  public void testTitle() {
    String title = document.title();
    assertNotNull(title);
    assertEquals("", title);
    
    document.title("Test Title");
    assertEquals("Test Title", document.title());
  }
  
  @Test
  public void testNormalise() {
    Document normalizedDoc = document.normalise();
    assertNotNull(normalizedDoc);
    assertEquals("html", normalizedDoc.nodeName());
    assertEquals("head", normalizedDoc.head().nodeName());
    assertEquals("body", normalizedDoc.body().nodeName());
  }
  
  @Test
  public void testCreateElement() {
    Element element = document.createElement("div");
    assertNotNull(element);
    assertEquals("div", element.nodeName());
    assertEquals("https://www.example.com", element.baseUri());
  }
  
  @Test
  public void testText() {
    document.text("Test");
    assertEquals("Test", document.body().text());
  }
  
  @Test
  public void testOuterHtml() {
    String outerHtml = document.outerHtml();
    assertNotNull(outerHtml);
    assertEquals("", outerHtml);
  }
  
  @Test
  public void testNodeName() {
    String nodeName = document.nodeName();
    assertNotNull(nodeName);
    assertEquals("#document", nodeName);
  }
  
  @Test
  public void testOutputSettings() {
    Document.OutputSettings outputSettings = document.outputSettings();
    assertNotNull(outputSettings);
    assertEquals(Document.OutputSettings.class, outputSettings.getClass());
  }
}