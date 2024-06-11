package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

  @Test
  public void testConstructor() {
    // Typical Use Case
    DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/loose.dtd", "https://www.example.com");
    assertNotNull(docType);
    
    // Edge Case
    docType = new DocumentType("", "", "", "");
    assertNotNull(docType);
    
    // Error Scenario
    docType = new DocumentType(null, null, null, null);
    assertNotNull(docType);
  }
  
  @Test
  public void testNodeName() {
    DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/loose.dtd", "https://www.example.com");
    assertEquals("#doctype", docType.nodeName());
  }
  
  @Test
  public void testOuterHtmlHead() {
    DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/loose.dtd", "https://www.example.com");
    
    // Typical Use Case
    StringBuilder accum = new StringBuilder();
    docType.outerHtmlHead(accum, 0, new Document.OutputSettings(Syntax.html));
    assertEquals("<!doctype html PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/loose.dtd\">", accum.toString());
    
    // Edge Case
    accum = new StringBuilder();
    docType.outerHtmlHead(accum, 0, new Document.OutputSettings(Syntax.html));
    assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/loose.dtd\">", accum.toString());
    
    // Error Scenario
    accum = new StringBuilder();
    docType.outerHtmlHead(accum, 0, new Document.OutputSettings(Syntax.xml));
    assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/loose.dtd\">", accum.toString());
  }
  
  @Test
  public void testOuterHtmlTail() {
    DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/loose.dtd", "https://www.example.com");
    StringBuilder accum = new StringBuilder();
    docType.outerHtmlTail(accum, 0, new Document.OutputSettings(Syntax.html));
    assertEquals("", accum.toString());
  }

  @Test
  public void testHas() {
    DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/loose.dtd", "https://www.example.com");
    
    // Typical Use Case
    assertTrue(docType.has(DocumentType.NAME));
    assertTrue(docType.has(DocumentType.PUBLIC_ID));
    assertTrue(docType.has(DocumentType.SYSTEM_ID));
    
    // Edge Case
    DocumentType emptyDocType = new DocumentType("", "", "", "");
    assertFalse(emptyDocType.has(DocumentType.NAME));
    assertFalse(emptyDocType.has(DocumentType.PUBLIC_ID));
    assertFalse(emptyDocType.has(DocumentType.SYSTEM_ID));
    
    // Error Scenario
    DocumentType nullDocType = new DocumentType(null, null, null, null);
    assertFalse(nullDocType.has(DocumentType.NAME));
    assertFalse(nullDocType.has(DocumentType.PUBLIC_ID));
    assertFalse(nullDocType.has(DocumentType.SYSTEM_ID));
  }
}