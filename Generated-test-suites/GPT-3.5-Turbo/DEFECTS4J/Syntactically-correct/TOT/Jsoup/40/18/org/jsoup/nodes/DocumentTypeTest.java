import org.jsoup.nodes.DocumentType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
    }

    @Test
    public void testConstructor() {
        assertNotNull(documentType);
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, 0, outputSettings);

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHead_noPublicId() {
        DocumentType documentTypeNoPublicId = new DocumentType("html", "", "http://www.w3.org/TR/html4/loose.dtd", "http://example.com");

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        documentTypeNoPublicId.outerHtmlHead(accum, 0, outputSettings);

        assertEquals("<!DOCTYPE html \"http://www.w3.org/TR/html4/loose.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHead_noSystemId() {
        DocumentType documentTypeNoSystemId = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "", "http://example.com");

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        documentTypeNoSystemId.outerHtmlHead(accum, 0, outputSettings);

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHead_noPublicIdAndSystemId() {
        DocumentType documentTypeNoPublicIdAndSystemId = new DocumentType("html", "", "", "http://example.com");

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        documentTypeNoPublicIdAndSystemId.outerHtmlHead(accum, 0, outputSettings);

        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        documentType.outerHtmlTail(accum, 0, outputSettings);

        assertEquals("", accum.toString());
    }
}