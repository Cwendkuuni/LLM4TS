import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

  @Test
  public void testNodeName() {
    DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");
    String nodeName = docType.nodeName();
    assertEquals("#doctype", nodeName);
  }

  @Test
  public void testOuterHtmlHead() {
    DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");
    StringBuilder accum = new StringBuilder();
    docType.outerHtmlHead(accum, 0, new Document.OutputSettings());
    assertEquals("<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">", accum.toString());
  }

  @Test
  public void testOuterHtmlTail() {
    DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");
    StringBuilder accum = new StringBuilder();
    docType.outerHtmlTail(accum, 0, new Document.OutputSettings());
    assertEquals("", accum.toString());
  }
  
}