import org.junit.*;
import static org.junit.Assert.*;

public class DocumentTypeTest {

  private DocumentType docType;

  // test case for constructor 1
  @Test
  public void testConstructor1() {
    docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/TR/html4/");
    assertEquals("html", docType.nodeName());
    assertEquals("-//W3C//DTD HTML 4.01//EN", docType.attr(DocumentType.PUBLIC_ID));
    assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.attr(DocumentType.SYSTEM_ID));
    assertEquals("http://www.w3.org/TR/html4/", docType.baseUri());
  }

  // test case for constructor 2
  @Test
  public void testConstructor2() {
    docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/TR/html4/");
    assertEquals("html", docType.nodeName());
    assertEquals("-//W3C//DTD HTML 4.01//EN", docType.attr(DocumentType.PUBLIC_ID));
    assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.attr(DocumentType.SYSTEM_ID));
    assertEquals("http://www.w3.org/TR/html4/", docType.baseUri());
  }

  // test case for nodeName()
  @Test
  public void testNodeName() {
    docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/TR/html4/");
    assertEquals("#doctype", docType.nodeName());
  }

  // test case for outerHtmlHead() - html syntax
  @Test
  public void testOuterHtmlHeadHTMLSyntax() {
      Document.OutputSettings out = new Document.OutputSettings();
      out.syntax(Syntax.html);
      docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/TR/html4/");

      StringBuilder sb = new StringBuilder();
      try {
        docType.outerHtmlHead(sb, 0, out);
      } catch (Exception e) {
        fail("Exception thrown: " + e.getMessage());
      }

      assertEquals("<!doctype html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", sb.toString());
  }

  // test case for outerHtmlHead() - other than html syntax
  @Test
  public void testOuterHtmlHeadOtherSyntax() {
      Document.OutputSettings out = new Document.OutputSettings();
      out.syntax(Syntax.xhtml);
      docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/TR/html4/");

      StringBuilder sb = new StringBuilder();
      try {
        docType.outerHtmlHead(sb, 0, out);
      } catch (Exception e) {
        fail("Exception thrown: " + e.getMessage());
      }

      assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", sb.toString());
  }

  // test case for outerHtmlTail()
  @Test
  public void testOuterHtmlTail() {
      docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/TR/html4/");

      StringBuilder sb = new StringBuilder();
      docType.outerHtmlTail(sb, 0, new Document.OutputSettings());

      assertEquals("", sb.toString());
  }

  // test case for has() - when attribute is present
  @Test
  public void testHasAttributePresent() {
      docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/TR/html4/");
      assertTrue(docType.has(DocumentType.NAME));
  }

  // test case for has() - when attribute is not present
  @Test
  public void testHasAttributeNotPresent() {
      docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/TR/html4/");
      assertFalse(docType.has("version"));
  }

  @After
  public void tearDown() {
    docType = null;
  }
}