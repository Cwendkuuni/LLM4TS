import org.jsoup.nodes.DocumentType;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTypeTest {

  @Test
  public void testDocumentTypeConstructor() {
    DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.example.com");

    Assert.assertEquals("html", documentType.nodeName());
    Assert.assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", documentType.attr("publicId"));
    Assert.assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", documentType.attr("systemId"));
    Assert.assertEquals("http://www.example.com", documentType.baseUri());
  }

  @Test
  public void testHas_PublicId_True() {
    DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.example.com");

    Assert.assertTrue(documentType.has("publicId"));
  }

  @Test
  public void testHas_PublicId_False() {
    DocumentType documentType = new DocumentType("html", "", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.example.com");

    Assert.assertFalse(documentType.has("publicId"));
  }

  @Test
  public void testOuterHtmlHead_HTML5() {
    DocumentType documentType = new DocumentType("html", "", "", "http://www.example.com");

    StringBuilder builder = new StringBuilder();
    documentType.outerHtmlHead(builder, 0, Document.OutputSettings.html());

    Assert.assertEquals("<!doctype", builder.toString());
  }

  @Test
  public void testOuterHtmlHead_NotHTML5() {
    DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.example.com");

    StringBuilder builder = new StringBuilder();
    documentType.outerHtmlHead(builder, 0, Document.OutputSettings.html());

    Assert.assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", builder.toString());
  }

  @Test
  public void testOuterHtmlTail() {
    DocumentType documentType = new DocumentType("html", "", "", "http://www.example.com");

    StringBuilder builder = new StringBuilder();
    documentType.outerHtmlTail(builder, 0, Document.OutputSettings.html());

    Assert.assertEquals("", builder.toString());
  }
}