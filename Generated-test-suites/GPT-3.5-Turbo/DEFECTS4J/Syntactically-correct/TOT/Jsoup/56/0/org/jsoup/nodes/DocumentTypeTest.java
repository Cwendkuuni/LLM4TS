import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {
    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://www.example.com");
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
    public void testOuterHtmlHeadForHtml5Doctype() {
        documentType = new DocumentType("html", "", "", "http://www.example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        StringBuilder accum = new StringBuilder();
        try {
            documentType.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown during outerHtmlHead method call");
        }
        assertEquals("<!doctype", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadForNonHtml5Doctype() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            documentType.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown during outerHtmlHead method call");
        }
        assertEquals("<!DOCTYPE", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithName() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            documentType.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown during outerHtmlHead method call");
        }
        assertTrue(accum.toString().contains(" html"));
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            documentType.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown during outerHtmlHead method call");
        }
        assertTrue(accum.toString().contains(" PUBLIC \"-//W3C//DTD HTML 4.0//EN\""));
    }

    @Test
    public void testOuterHtmlHeadWithSystemId() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            documentType.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown during outerHtmlHead method call");
        }
        assertTrue(accum.toString().contains(" \"http://www.w3.org/TR/REC-html40/strict.dtd\""));
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        documentType.outerHtmlTail(accum, 0, outputSettings);
        assertEquals("", accum.toString());
    }

    @Test
    public void testHasForExistingAttribute() {
        assertTrue(documentType.has(DocumentType.NAME));
    }

    @Test
    public void testHasForNonExistingAttribute() {
        assertFalse(documentType.has("nonExistingAttribute"));
    }
}