import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTypeTest {
  
  @Test
  public void testDocumentTypeConstructor() {
    DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", null, "http://example.com");
    assertNotNull(doctype);
    assertEquals("html", doctype.attr(DocumentType.NAME));
    assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.attr(DocumentType.PUBLIC_ID));
    assertNull(doctype.attr(DocumentType.SYSTEM_ID));
  }
  
  @Test
  public void testNodeName() {
    DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", null, "http://example.com");
    assertEquals("#doctype", doctype.nodeName());
  }
  
  @Test
  public void testOuterHtmlHeadHtml5Doctype() {
    DocumentType doctype = new DocumentType("html", null, null, "http://example.com");
    StringBuilder sb = new StringBuilder();
    doctype.outerHtmlHead(sb, 0, new Document.OutputSettings(Syntax.html));
    assertEquals("<!doctype html>", sb.toString());
  }
  
  @Test
  public void testOuterHtmlHeadNormalDoctype() {
    DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.example.org", "http://example.com");
    StringBuilder sb = new StringBuilder();
    doctype.outerHtmlHead(sb, 0, new Document.OutputSettings(Syntax.html));
    assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.example.org\">", sb.toString());
  }
  
  @Test
  public void testHasMethod() {
    DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", null, "http://example.com");
    assertTrue(doctype.has(DocumentType.NAME));
    assertTrue(doctype.has(DocumentType.PUBLIC_ID));
    assertFalse(doctype.has(DocumentType.SYSTEM_ID));
  }

}