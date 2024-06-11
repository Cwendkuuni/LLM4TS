
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testNoArgsConstructor() {
        DocumentType documentType = new DocumentType("", "", "", "");
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("", documentType.attr("name"));
        assertEquals("", documentType.attr("publicId"));
        assertEquals("", documentType.attr("systemId"));
    }

    @Test
    public void testParameterizedConstructor() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("name", documentType.attr("name"));
        assertEquals("publicId", documentType.attr("publicId"));
        assertEquals("systemId", documentType.attr("systemId"));
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Syntax.html);
        try {
            documentType.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("An exception occurred.");
        }
        assertEquals("<!doctype name PUBLIC \"publicId\" \"systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithBlankPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("name", "", "", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Syntax.html);
        try {
            documentType.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("An exception occurred.");
        }
        assertEquals("<!doctype name>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            documentType.outerHtmlTail(accum, 0, out);
        } catch (Exception e) {
            fail("An exception occurred.");
        }
        assertEquals("", accum.toString());
    }

    @Test
    public void testHasWithAttribute() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertTrue(documentType.has("name"));
    }

    @Test
    public void testHasWithoutAttribute() {
        DocumentType documentType = new DocumentType("", "", "", "");
        assertFalse(documentType.has("name"));
    }
}

