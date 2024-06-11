import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.example.com");
        assertEquals("html", doctype.attr("name"));
        assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", doctype.attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", doctype.attr("systemId"));
        assertEquals("http://www.example.com", doctype.baseUri());
    }

    @Test
    public void testOuterHtmlHead() {
        // Edge case: testing for HTML5 doctype
        DocumentType doctype = new DocumentType("html", "", "", "");
        StringBuilder sb = new StringBuilder();
        doctype.outerHtmlHead(sb, 0, new Document.OutputSettings());
        assertEquals("<!doctype html>", sb.toString());
        
        // Error scenario: public ID without system ID
        doctype = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "", "");
        sb = new StringBuilder();
        doctype.outerHtmlHead(sb, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\">", sb.toString());
    }

    @Test
    public void testHasAttribute() {
        DocumentType doctype = new DocumentType("html", "", "", "");
        assertFalse(doctype.has("name"));
        assertFalse(doctype.has("publicId"));
        assertFalse(doctype.has("systemId"));

        doctype.attr("name", "html");
        assertTrue(doctype.has("name"));
    }
}