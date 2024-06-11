import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.junit.Assert.*;

public class DocumentTypeTest {

  @Test
  public void testDocumentTypeCreation() {
    Document document = new Document();
    DocumentType documentType = document.createElement("DOCTYPE", "#doctype");

    assertEquals("#doctype", documentType.nodeName());
    assertNull(documentType.attr("publicId"));
    assertNull(documentType.attr("systemId"));
    assertEquals("", documentType.baseUri());
  }

  @Test
  public void testDocumentTypeWithPublicId() {
    Document document = new Document();
    DocumentType documentType = document.createElement("DOCTYPE", "#doctype", "publicIdTest", "");

    assertEquals("#doctype", documentType.nodeName());
    assertEquals("publicIdTest", documentType.attr("publicId"));
    assertNull(documentType.attr("systemId"));
    assertEquals("", documentType.baseUri());
  }

  @Test
  public void testDocumentTypeWithSystemId() {
    Document document = new Document();
    DocumentType documentType = document.createElement("DOCTYPE", "#doctype", "", "systemIdTest");

    assertEquals("#doctype", documentType.nodeName());
    assertNull(documentType.attr("publicId"));
    assertNull(documentType.attr("systemId"));
    assertEquals("systemIdTest", documentType.baseUri());
  }

  @Test
  public void testDocumentTypeWithPublicIdAndSystemId() {
    Document document = new Document();
    DocumentType documentType = document.createElement("DOCTYPE", "#doctype", "publicIdTest", "systemIdTest");

    assertEquals("#doctype", documentType.nodeName());
    assertEquals("publicIdTest", documentType.attr("publicId"));
    assertEquals("systemIdTest", documentType.attr("systemId"));
    assertEquals("", documentType.baseUri());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDocumentTypeWithNullName() {
    Document document = new Document();
    documentType = document.createElement("DOCTYPE", null, "", "");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDocumentTypeWithEmptyName() {
    Document document = new Document();
    documentType = document.createElement("DOCTYPE", "", "", "");
  }
}