import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

  @Test
  public void testNodeName() {
    DocumentType docType = new DocumentType("docType", "publicId", "systemId", "baseUri");
    assertEquals("#doctype", docType.nodeName());
  }

  @Test
  public void testOuterHtmlHead() {
    DocumentType docType = new DocumentType("docType", "publicId", "systemId", "baseUri");
    StringBuilder accum = new StringBuilder();
    Document.OutputSettings out = new Document.OutputSettings();
    try {
      docType.outerHtmlHead(accum, 0, out);
      String expected = "<!DOCTYPE docType PUBLIC \"publicId\" \"systemId\">";
      assertEquals(expected, accum.toString());
    } catch (Exception e) {
      fail("An exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testOuterHtmlTail() {
    DocumentType docType = new DocumentType("docType", "publicId", "systemId", "baseUri");
    StringBuilder accum = new StringBuilder();
    Document.OutputSettings out = new Document.OutputSettings();
    docType.outerHtmlTail(accum, 0, out);
    assertEquals("", accum.toString());
  }

  @Test
  public void testHas() {
    DocumentType docType = new DocumentType("docType", "publicId", "systemId", "baseUri");
    assertFalse(docType.has("missingAttribute"));
    assertTrue(docType.has("name"));
  }
}