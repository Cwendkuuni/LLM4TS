import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");

        assertEquals("html", documentType.nodeName());
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", documentType.attr(DocumentType.PUBLIC_ID));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", documentType.attr(DocumentType.SYSTEM_ID));
        assertEquals("http://www.example.com", documentType.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");

        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        StringBuilder accumulator = new StringBuilder();
        try {
            documentType.outerHtmlHead(accumulator, 0, outputSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">", accumulator.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        StringBuilder accumulator = new StringBuilder();

        documentType.outerHtmlTail(accumulator, 0, outputSettings);

        assertEquals("", accumulator.toString());
    }

    @Test
    public void testHas() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");

        assertTrue(documentType.has(DocumentType.PUBLIC_ID));
        assertTrue(documentType.has(DocumentType.SYSTEM_ID));
        assertFalse(documentType.has(DocumentType.NAME));
    }
}