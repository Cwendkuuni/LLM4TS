package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DocumentTypeTest {
  
  @Test
  public void testConstructor() {
    DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://www.example.com");

    assertEquals("html", docType.attr("name"));
    assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", docType.attr("publicId"));
    assertEquals("http://www.w3.org/TR/html4/loose.dtd", docType.attr("systemId"));
    assertEquals("http://www.example.com", docType.baseUri());
  }

  @Test
  public void testNodeName() {
    DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://www.example.com");

    assertEquals("#doctype", docType.nodeName());
  }

  @Test
  public void testOuterHtmlHead() {
    DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://www.example.com");

    StringBuilder accum = new StringBuilder();
    Document.OutputSettings out = new Document.OutputSettings();

    docType.outerHtmlHead(accum, 0, out);

    assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" http://www.w3.org/TR/html4/loose.dtd>", accum.toString());
  }

  @Test
  public void testOuterHtmlTail() {
    DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://www.example.com");

    StringBuilder accum = new StringBuilder();
    Document.OutputSettings out = new Document.OutputSettings();

    docType.outerHtmlTail(accum, 0, out);

    assertEquals("", accum.toString());
  }

}