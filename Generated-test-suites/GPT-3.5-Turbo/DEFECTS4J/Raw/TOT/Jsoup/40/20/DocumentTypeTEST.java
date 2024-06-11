import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTypeTest {
  
  // Expert 1 test cases
  @Test
  public void testConstructor() {
    DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
    assertEquals("html", docType.nodeName());
    assertEquals("-//W3C//DTD HTML 4.01//EN", docType.attr("publicId"));
    assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.attr("systemId"));
    assertEquals("http://example.com", docType.baseUri());
  }
  
  // Expert 2 test cases
  @Test
  public void testOuterHtmlHead() {
    DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
    StringBuilder accum = new StringBuilder();
    docType.outerHtmlHead(accum, 0, new Document.OutputSettings());
    assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
  }
  
  // Expert 3 test cases
  @Test
  public void testOuterHtmlTail() {
    DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
    StringBuilder accum = new StringBuilder();
    docType.outerHtmlTail(accum, 0, new Document.OutputSettings());
    assertEquals("", accum.toString());
  }
  
}