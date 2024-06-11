package org.jsoup.nodes;

import org.junit.Test;
import org.hamcrest.CoreMatchers;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class DocumentTypeTest {

  @Test
  public void testNodeName() {
    DocumentType document = new DocumentType("name", "publicId", "systemId", "baseUri");
    assertThat(document.nodeName(), is("#doctype"));
  }

  @Test
  public void testOuterHtmlHead() {
    DocumentType document = new DocumentType("name", "publicId", "systemId", "baseUri");
    StringBuilder accum = new StringBuilder();
    document.outerHtmlHead(accum, 0, null);
    assertThat(accum.toString(), is("<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">"));
  }

  @Test
  public void testOuterHtmlTail() {
    DocumentType document = new DocumentType("name", "publicId", "systemId", "baseUri");
    StringBuilder accum = new StringBuilder();
    document.outerHtmlTail(accum, 0, null);
    assertThat(accum.toString(), is(""));
  }

}