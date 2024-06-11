import org.junit.Test;
import org.jsoup.nodes.DocumentType;

public class DocumentTypeTest {
   @Test
   public void testConstructor() {
      DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
      assert(docType.nodeName().equals("#doctype"));
      assert(docType.attr("name").equals("html"));
      assert(docType.attr("publicId").equals("publicId"));
      assert(docType.attr("systemId").equals("systemId"));
      assert(docType.attr("baseUri").equals("baseUri"));
   }
}