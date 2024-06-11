import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHeadHtmlDoctype() throws IOException {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, outputSettings);
        assertEquals("<!doctype html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadNonHtmlDoctype() throws IOException {
        DocumentType documentType = new DocumentType("rss", null, null, "http://www.example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.xhtml);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, outputSettings);
        assertEquals("<!DOCTYPE rss>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadMissingPublicId() throws IOException {
        DocumentType documentType = new DocumentType("html", null, "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, outputSettings);
        assertEquals("<!doctype html \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadMissingSystemId() throws IOException {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", null, "http://www.example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, outputSettings);
        assertEquals("<!doctype html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">", accum.toString());
    }

    @Test
    public void testHasAttributeWithAttribute() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        assertTrue(documentType.has(DocumentType.NAME));
    }

    @Test
    public void testHasAttributeWithoutAttribute() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        assertFalse(documentType.has(DocumentType.PUBLIC_ID));
    }
}