import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

  @Test
  public void testConstructor_ValidInputs_ShouldCreateInstance() {
    DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com/");
    assertNotNull(dt);
    assertEquals("html", dt.attr("name"));
    assertEquals("-//W3C//DTD HTML 4.01//EN", dt.attr("publicId"));
    assertEquals("http://www.w3.org/TR/html4/strict.dtd", dt.attr("systemId"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructor_EmptyName_ShouldThrowException() {
    new DocumentType("", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com/");
  }

  @Test
  public void testConstructor_NullPublicId_ShouldSetEmptyString() {
    DocumentType dt = new DocumentType("html", null, "http://www.w3.org/TR/html4/strict.dtd", "http://example.com/");
    assertEquals("", dt.attr("publicId"));
  }

  @Test
  public void testConstructor_NullSystemId_ShouldSetEmptyString() {
    DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", null, "http://example.com/");
    assertEquals("", dt.attr("systemId"));
  }

  @Test
  public void testNodeName_ReturnsDoctype() {
    DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com/");
    assertEquals("#doctype", dt.nodeName());
  }

  @Test
  public void testOuterHtmlHead_ReturnsCorrectString() {
    DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com/");
    StringBuilder accum = new StringBuilder();
    dt.outerHtmlHead(accum, 0, new Document.OutputSettings());
    assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
  }

  @Test
  public void testOuterHtmlTail_DoesNothing() {
    DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com/");
    StringBuilder accum = new StringBuilder();
    dt.outerHtmlTail(accum, 0, new Document.OutputSettings());
    assertEquals("", accum.toString());
  }

}