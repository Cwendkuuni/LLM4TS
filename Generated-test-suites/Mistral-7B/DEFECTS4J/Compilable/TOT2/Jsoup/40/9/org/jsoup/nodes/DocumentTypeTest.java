package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.jsoup.nodes.DocumentType;

public class DocumentTypeTest {
   @Test
   public void testDocumentTypeConstructor() {
      String name = "html";
      String publicId = "publicID";
      String systemId = "systemID";
      String baseUri = "baseURI";
      DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);
      assertEquals("#doctype", docType.nodeName());
      assertEquals(name, docType.attr("name"));
      assertEquals(publicId, docType.attr("publicId"));
      assertEquals(systemId, docType.attr("systemId"));
      assertEquals(baseUri, docType.baseUri());
   }
}