import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.example.com");
        assertEquals("html", documentType.attr("name"));
        assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", documentType.attr("systemId"));
        assertEquals("http://www.example.com", documentType.baseUri());
    }
    
    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.example.com");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead_noPublicAndNoSystem() {
        DocumentType documentType = new DocumentType("html", null, null, "http://www.example.com");
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        StringBuilder accum = new StringBuilder();
        try {
            documentType.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("<!doctype", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHead_hasPublicAndNoSystem() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", null, "http://www.example.com");
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        StringBuilder accum = new StringBuilder();
        try {
            documentType.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHead_hasSystemAndNoPublic() {
        DocumentType documentType = new DocumentType("html", null, "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.example.com");
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        StringBuilder accum = new StringBuilder();
        try {
            documentType.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("<!DOCTYPE html \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHead_hasPublicAndSystem() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.example.com");
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        StringBuilder accum = new StringBuilder();
        try {
            documentType. outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }
    
    @Test
    public void testHas_true() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.example.com");
        assertTrue(documentType.has("name"));
    }
    
    @Test
    public void testHas_false() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.example.com");
        assertFalse(documentType.has("version"));
    }
}