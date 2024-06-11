import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTypeTest {

   @Test
   public void testConstructor() {
       DocumentType dt = new DocumentType("html", "", "", "http://example.com");
       assertNotNull(dt);
       assertEquals("html", dt.attr("name"));
       assertEquals("", dt.attr("publicId"));
       assertEquals("", dt.attr("systemId"));
   }

   @Test
   public void testNodeName() {
       DocumentType dt = new DocumentType("html", "", "", "http://example.com");
       assertEquals("#doctype", dt.nodeName());
   }

   @Test
   public void testOuterHtmlHead() {
       DocumentType dt = new DocumentType("html", "", "", "http://example.com");
       StringBuilder sb = new StringBuilder();
       dt.outerHtmlHead(sb, 0, new Document.OutputSettings());
       assertTrue(sb.toString().contains("<!doctype"));
   }

   @Test
   public void testOuterHtmlTail() {
       DocumentType dt = new DocumentType("html", "", "", "http://example.com");
       StringBuilder sb = new StringBuilder();
       dt.outerHtmlTail(sb, 0, new Document.OutputSettings());
       assertEquals("", sb.toString());
   }

   @Test
   public void testHas() {
       DocumentType dt = new DocumentType("html", "", "", "http://example.com");
       assertTrue(dt.has("name"));
       assertFalse(dt.has("nonexistent"));
   }
}