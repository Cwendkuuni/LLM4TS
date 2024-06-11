import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DocumentTypeTest {

    private DocumentType docType;

    @Before
    public void setUp() {
        docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            docType.outerHtmlHead(sb, 0, outputSettings);
        } catch(Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", sb.toString());
    }
    
    @Test
    public void testOuterHtmlHeadHtml5() {
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        try {
            docType.outerHtmlHead(sb, 0, outputSettings);
        } catch(Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("<!doctype html>", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        docType.outerHtmlTail(sb, 0, outputSettings);
        assertEquals("", sb.toString());
    }

    @Test
    public void testHas() {
        assertFalse(docType.has("publicId"));
        assertTrue(docType.has("name"));
    }
}