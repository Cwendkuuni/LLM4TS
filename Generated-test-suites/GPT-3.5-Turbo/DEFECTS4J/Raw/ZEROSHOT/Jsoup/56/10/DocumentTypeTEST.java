import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        String name = "doctype";
        String publicId = "-//W3C//DTD HTML 4.01//EN";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertNotNull(documentType);
        assertEquals(name, documentType.attr(DocumentType.NAME));
        assertEquals(publicId, documentType.attr(DocumentType.PUBLIC_ID));
        assertEquals(systemId, documentType.attr(DocumentType.SYSTEM_ID));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("doctype", null, null, null);
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("doctype", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", null);
        String expectedHtml = "<!DOCTYPE doctype PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">";

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals(expectedHtml, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("doctype", null, null, null);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);

        assertEquals("", accum.toString());
    }

    @Test
    public void testHas() {
        DocumentType documentType = new DocumentType("doctype", "-//W3C//DTD HTML 4.01//EN", null, null);

        assertTrue(documentType.has(DocumentType.NAME));
        assertTrue(documentType.has(DocumentType.PUBLIC_ID));
        assertFalse(documentType.has(DocumentType.SYSTEM_ID));
    }
}