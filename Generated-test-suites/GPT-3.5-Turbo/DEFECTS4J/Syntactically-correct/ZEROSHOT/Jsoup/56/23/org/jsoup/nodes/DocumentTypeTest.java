import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            documentType.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertTrue(accum.toString().startsWith("<!DOCTYPE"));
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            documentType.outerHtmlTail(accum, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("", accum.toString());
    }

    @Test
    public void testHas() {
        assertFalse(documentType.has("attribute"));
        assertTrue(documentType.has("name"));
        assertTrue(documentType.has("publicId"));
        assertTrue(documentType.has("systemId"));
    }
}