import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.helper.StringUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
        
        assertEquals("html", docType.nodeName());
        assertEquals("//W3C//DTD HTML 4.01//EN", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.attr("systemId"));
    }

    @Test
    public void testHtml5DoctypeOutput() {
        DocumentType docType = new DocumentType("html", null, null, "http://example.com");

        // Test output for HTML5 doctype without public and system ID
        assertEquals("<!doctype html>", docType.outerHtml());

        // Test output for HTML5 doctype with public ID
        docType.attr("publicId", "-//W3C//DTD HTML 4.01//EN");
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">", docType.outerHtml());

        // Test output for HTML5 doctype with system ID
        docType.attr("systemId", "http://www.w3.org/TR/html4/strict.dtd");
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", docType.outerHtml());
    }

    @Test
    public void testHasMethod() {
        DocumentType docType = new DocumentType("html", null, null, "http://example.com");

        assertFalse(docType.has("name"));
        assertFalse(docType.has("publicId"));
        assertFalse(docType.has("systemId"));

        docType.attr("publicId", "-//W3C//DTD HTML 4.01//EN");

        assertFalse(docType.has("name"));
        assertTrue(docType.has("publicId"));
        assertFalse(docType.has("systemId"));
    }
}