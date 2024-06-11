import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertNotNull(docType);
        assertEquals("name", docType.attr("name"));
        assertEquals("publicId", docType.attr("publicId"));
        assertEquals("systemId", docType.attr("systemId"));
        assertEquals("baseUri", docType.getBaseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType1 = new DocumentType("name", "publicId", "systemId", "baseUri");
        DocumentType docType2 = new DocumentType("name", null, null, "baseUri");
        DocumentType docType3 = new DocumentType("name", "publicId", null, "baseUri");
        DocumentType docType4 = new DocumentType("name", null, "systemId", "baseUri");

        Document.OutputSettings htmlSyntaxSettings = new Document.OutputSettings();
        htmlSyntaxSettings.syntax(Document.OutputSettings.Syntax.html);

        StringBuilder accum1 = new StringBuilder();
        StringBuilder accum2 = new StringBuilder();
        StringBuilder accum3 = new StringBuilder();
        StringBuilder accum4 = new StringBuilder();

        try {
            docType1.outerHtmlHead(accum1, 0, htmlSyntaxSettings);
            docType2.outerHtmlHead(accum2, 0, htmlSyntaxSettings);
            docType3.outerHtmlHead(accum3, 0, htmlSyntaxSettings);
            docType4.outerHtmlHead(accum4, 0, htmlSyntaxSettings);
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }

        assertEquals("<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">", accum1.toString());
        assertEquals("<!doctype name>", accum2.toString());
        assertEquals("<!DOCTYPE name PUBLIC \"publicId\">", accum3.toString());
        assertEquals("<!DOCTYPE name \"systemId\">", accum4.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();

        docType.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }

    @Test
    public void testHas() {
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertTrue(docType.has("name"));
        assertTrue(docType.has("publicId"));
        assertTrue(docType.has("systemId"));

        DocumentType docType2 = new DocumentType(null, "publicId", "systemId", "baseUri");
        assertFalse(docType2.has("name"));
        assertTrue(docType2.has("publicId"));
        assertTrue(docType2.has("systemId"));
    }

}