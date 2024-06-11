import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType documentType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
        assertEquals("HTML", documentType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.attr("systemId"));
        assertEquals("http://example.com", documentType.baseUri());
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testConstructorWithEmptyValues() {
        DocumentType documentType = new DocumentType("", "", "", "");
        assertTrue(documentType.attr("name").isEmpty());
        assertTrue(documentType.attr("publicId").isEmpty());
        assertTrue(documentType.attr("systemId").isEmpty());
        assertTrue(documentType.baseUri().isEmpty());
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHeadWithHtmlSyntaxAndNoPublicIdAndNoSystemId() {
        DocumentType documentType = new DocumentType("HTML", "", "", "");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            documentType.outerHtmlHead(stringBuilder, 0, outputSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("<!doctype HTML>", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlHeadWithHtmlSyntaxAndPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            documentType.outerHtmlHead(stringBuilder, 0, outputSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlHeadWithNonHtmlSyntaxAndPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.xml); // Assuming XML syntax
        StringBuilder stringBuilder = new StringBuilder();
        try {
            documentType.outerHtmlHead(stringBuilder, 0, outputSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", stringBuilder.toString()); // No difference as of now, may change in the future
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "");
        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlTail(stringBuilder, 0, new Document.OutputSettings());
        assertTrue(stringBuilder.toString().isEmpty()); // No HTML to display after the closing '>' of the doctype declaration
    }

    @Test
    public void testHasWithNonBlankAttribute() {
        DocumentType documentType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "");
        assertTrue(documentType.has("name"));
        assertTrue(documentType.has("publicId"));
        assertTrue(documentType.has("systemId"));
    }

    @Test
    public void testHasWithBlankAttribute() {
        DocumentType documentType = new DocumentType("", "", "", "");
        assertFalse(documentType.has("name"));
        assertFalse(documentType.has("publicId"));
        assertFalse(documentType.has("systemId"));
    }
}