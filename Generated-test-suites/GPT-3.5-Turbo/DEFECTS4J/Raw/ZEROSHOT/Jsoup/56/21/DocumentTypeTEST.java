package org.jsoup.nodes;

import org.jsoup.helper.StringUtil;
import org.junit.*;
import static org.junit.Assert.*;

public class DocumentTypeTest {

  @Test
  public void testNodeName() {
    DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");
    assertEquals("#doctype", docType.nodeName());
  }

  @Test
  public void testOuterHtmlHead() {
    DocumentType docType1 = new DocumentType("name", "publicId", "systemId", "baseUri");
    Document.OutputSettings out1 = new Document.OutputSettings();
    out1.setSyntax(Document.OutputSettings.Syntax.html);
    StringBuilder accum1 = new StringBuilder();
    try {
      docType1.outerHtmlHead(accum1, 1, out1);
      assertEquals("<!doctype", accum1.toString());
    } catch (Exception e) {
      fail("Exception thrown: " + e);
    }
    DocumentType docType2 = new DocumentType("name", null, null, "baseUri");
    Document.OutputSettings out2 = new Document.OutputSettings();
    out2.setSyntax(Document.OutputSettings.Syntax.html);
    StringBuilder accum2 = new StringBuilder();
    try {
      docType2.outerHtmlHead(accum2, 1, out2);
      assertEquals("<!DOCTYPE", accum2.toString());
    } catch (Exception e) {
      fail("Exception thrown: " + e);
    }
  }

  @Test
  public void testOuterHtmlTail() {
    DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");
    StringBuilder accum = new StringBuilder();
    docType.outerHtmlTail(accum, 1, new Document.OutputSettings());
    assertEquals("", accum.toString());
  }

  @Test
  public void testHas() {
    DocumentType docType1 = new DocumentType("name", "publicId", "systemId", "baseUri");
    assertFalse(docType1.has("dummyAttribute"));
    assertTrue(docType1.has("name"));
    assertTrue(docType1.has("publicId"));
    assertTrue(docType1.has("systemId"));

    DocumentType docType2 = new DocumentType("", "", "", "baseUri");
    assertFalse(docType2.has("name"));
    assertFalse(docType2.has("publicId"));
    assertFalse(docType2.has("systemId"));
  }

}