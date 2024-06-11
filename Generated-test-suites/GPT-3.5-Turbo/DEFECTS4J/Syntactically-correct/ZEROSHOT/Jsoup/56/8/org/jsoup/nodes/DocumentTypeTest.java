import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("doctype", "-//W3C//DTD HTML 4.01//EN",
                "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
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
        StringBuilder stringBuilder = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setSyntax(Document.OutputSettings.Syntax.html);

        try {
            documentType.outerHtmlHead(stringBuilder, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }

        assertEquals("<!doctype DOCTYPE PUBLIC \"-//W3C//DTD HTML 4.01//EN\""
                + " \"http://www.w3.org/TR/html4/strict.dtd\">", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder stringBuilder = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        documentType.outerHtmlTail(stringBuilder, 0, outputSettings);

        assertEquals("", stringBuilder.toString());
    }

    @Test
    public void testHas() {
        assertTrue(documentType.has("name"));
        assertTrue(documentType.has("publicId"));
        assertTrue(documentType.has("systemId"));
        assertFalse(documentType.has("invalidAttribute"));
    }
}