
import org.junit.Test;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.DocumentType;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHeadHtmlSyntaxNoPublicIdNoSystemId() {
        DocumentType documentType = new DocumentType("name", "", "", "baseUri");
        StringBuilder accum = new StringBuilder();
        OutputSettings outputSettings = new OutputSettings().syntax(OutputSettings.Syntax.html);
        try {
            documentType.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        assertEquals("<!doctype", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadHtmlSyntaxWithPublicId() {
        DocumentType documentType = new DocumentType("name", "publicId", "", "baseUri");
        StringBuilder accum = new StringBuilder();
        OutputSettings outputSettings = new OutputSettings().syntax(OutputSettings.Syntax.html);
        try {
            documentType.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        assertEquals("<!DOCTYPE name PUBLIC \"publicId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadHtmlSyntaxWithSystemId() {
        DocumentType documentType = new DocumentType("name", "", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        OutputSettings outputSettings = new OutputSettings().syntax(OutputSettings.Syntax.html);
        try {
            documentType.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        assertEquals("<!DOCTYPE name \"systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadHtmlSyntaxWithPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        OutputSettings outputSettings = new OutputSettings().syntax(OutputSettings.Syntax.html);
        try {
            documentType.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        assertEquals("<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadNonHtmlSyntax() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        OutputSettings outputSettings = new OutputSettings().syntax(OutputSettings.Syntax.xml);
        try {
            documentType.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        assertEquals("<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, new OutputSettings());
        assertEquals("", accum.toString());
    }

    @Test
    public void testHasAttributeWithAttribute() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertTrue(documentType.has("name"));
    }

    @Test
    public void testHasAttributeWithoutAttribute() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertFalse(documentType.has("randomAttr"));
    }

}
