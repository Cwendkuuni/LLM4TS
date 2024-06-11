import static org.junit.Assert.*;

import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;

public class DocumentTypeTest {

    // Test case for DocumentType constructor
    @Test
    public void testDocumentTypeConstructor() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.0 Transitional//EN", "http://www.w3.org/TR/REC-html40/loose.dtd", "https://example.com");
        
        assertEquals("html", dt.nodeName());
        assertEquals("-//W3C//DTD HTML 4.0 Transitional//EN", dt.attr(DocumentType.PUBLIC_ID));
        assertEquals("http://www.w3.org/TR/REC-html40/loose.dtd", dt.attr(DocumentType.SYSTEM_ID));
        assertEquals("https://example.com", dt.baseUri());
    }

    // Test case for outerHtmlHead method with HTML syntax and no publicId and systemId
    @Test
    public void testOuterHtmlHeadHtmlSyntaxNoPublicAndSystemId() {
        DocumentType dt = new DocumentType("html", "", "", "");
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.syntax(Document.OutputSettings.Syntax.html);

        StringBuilder sb = new StringBuilder();
        try {
            dt.outerHtmlHead(sb, 0, settings);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }

        String expectedOutput = "<!doctype";
        assertEquals(expectedOutput, sb.toString());
    }

    // Test case for outerHtmlHead method with non-HTML syntax
    @Test
    public void testOuterHtmlHeadNonHtmlSyntax() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.0 Transitional//EN", "http://www.w3.org/TR/REC-html40/loose.dtd", "");
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.syntax(Document.OutputSettings.Syntax.xml);

        StringBuilder sb = new StringBuilder();
        try {
            dt.outerHtmlHead(sb, 0, settings);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }

        String expectedOutput = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\" \"http://www.w3.org/TR/REC-html40/loose.dtd\">";
        assertEquals(expectedOutput, sb.toString());
    }

    // Test case for outerHtmlHead method with publicId and systemId
    @Test
    public void testOuterHtmlHeadWithPublicAndSystemId() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.0 Transitional//EN", "http://www.w3.org/TR/REC-html40/loose.dtd", "");
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.syntax(Document.OutputSettings.Syntax.html);

        StringBuilder sb = new StringBuilder();
        try {
            dt.outerHtmlHead(sb, 0, settings);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }

        String expectedOutput = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\" \"http://www.w3.org/TR/REC-html40/loose.dtd\">";
        assertEquals(expectedOutput, sb.toString());
    }

    // Test case for outerHtmlTail method
    @Test
    public void testOuterHtmlTail() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.0 Transitional//EN", "http://www.w3.org/TR/REC-html40/loose.dtd", "");
        StringBuilder sb = new StringBuilder();
        
        dt.outerHtmlTail(sb, 0, null);

        String expectedOutput = "";
        assertEquals(expectedOutput, sb.toString());
    }

    // Test case for has method with valid attribute
    @Test
    public void testHasValidAttribute() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.0 Transitional//EN", "http://www.w3.org/TR/REC-html40/loose.dtd", "");
        
        boolean hasAttribute = dt.has(DocumentType.NAME);
        assertTrue(hasAttribute);
    }

    // Test case for has method with blank attribute
    @Test
    public void testHasBlankAttribute() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.0 Transitional//EN", "http://www.w3.org/TR/REC-html40/loose.dtd", "");

        boolean hasAttribute = dt.has(DocumentType.PUBLIC_ID);
        assertFalse(hasAttribute);
    }

}