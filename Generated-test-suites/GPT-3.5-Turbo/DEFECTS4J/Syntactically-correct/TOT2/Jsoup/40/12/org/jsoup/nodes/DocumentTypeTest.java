import org.jsoup.nodes.DocumentType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
        assertNotNull(documentType);
        assertEquals("html", documentType.nodeName());
        assertEquals("//W3C//DTD HTML 4.01//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
        StringBuilder sb = new StringBuilder();
        documentType.outerHtmlHead(sb, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
        StringBuilder sb = new StringBuilder();
        documentType.outerHtmlTail(sb, 0, new Document.OutputSettings());
        assertEquals("", sb.toString());
    }

    // Additional test cases for edge cases and error scenarios can be added here

}