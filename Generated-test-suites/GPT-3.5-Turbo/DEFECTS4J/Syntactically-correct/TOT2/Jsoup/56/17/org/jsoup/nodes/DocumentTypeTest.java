import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");

        assertEquals("html", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.attr("systemId"));
        assertEquals("http://example.com", docType.baseUri());
    }

    @Test
    public void testHtml5DoctypeOutput() {
        DocumentType docType = new DocumentType("html", "", "", "http://example.com");

        assertEquals("<!doctype html>", docType.outerHtml());
    }

    @Test
    public void testHtmlDoctypeOutput() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", docType.outerHtml());
    }

    @Test
    public void testHasAttribute() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");

        assertTrue(docType.has("name"));
        assertTrue(docType.has("publicId"));
        assertTrue(docType.has("systemId"));
        assertFalse(docType.has("class"));
    }
}