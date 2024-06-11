package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {
  
  private DocumentType documentType;
  
  @Before
  public void setUp() {
    documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
  }
  
  @Test
  public void testNodeName() {
    assertEquals("#doctype", documentType.nodeName());
  }
  
  @Test
  public void testOuterHtmlHead() {
    StringBuilder accum = new StringBuilder();
    int depth = 0;
    Document.OutputSettings out = new Document.OutputSettings();
    documentType.outerHtmlHead(accum, depth, out);
    assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
  }
  
  @Test
  public void testOuterHtmlTail() {
    StringBuilder accum = new StringBuilder();
    int depth = 0;
    Document.OutputSettings out = new Document.OutputSettings();
    documentType.outerHtmlTail(accum, depth, out);
    assertEquals("", accum.toString());
  }
}