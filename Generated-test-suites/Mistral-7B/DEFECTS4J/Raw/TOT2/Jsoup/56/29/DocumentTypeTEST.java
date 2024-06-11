import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.helper.StringUtil;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() throws Exception {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseUri");
        Appendable accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!doctype html>\n", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() throws Exception {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseUri");
        Appendable accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, new Document.OutputSettings());
        assertEquals("", accum.toString());
    }

    @Test
    public void testHasAttribute() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseUri");
        documentType.attr("name", "html");
        assertTrue(documentType.has("name"));
    }

    @Test
    public void testEmptyStringConstructor() {
        DocumentType documentType = new DocumentType("", "", "", "");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testNullConstructor() {
        DocumentType documentType = new DocumentType(null, null, null, null);
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testWhitespaceAndSpecialCharsConstructor() {
        DocumentType documentType = new DocumentType("html   ", "publicID  ", "systemID  ", "baseUri  ");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testNullAttributes() {
        DocumentType documentType = new DocumentType("html", "", "", "");
        assertFalse(documentType.has("name"));
        assertFalse(documentType.has("publicId"));
        assertFalse(documentType.has("systemId"));
    }

    @Test
    public void testOuterHtmlHeadIOException() throws Exception {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseUri");
        Appendable accum = new StringBuilder();
        assertThrows(IOException.class, () -> documentType.outerHtmlHead(accum, 0, new Document.OutputSettings()));
    }

    @Test
    public void testEdgeCasesOuterHtmlHead() throws Exception {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseUri");
        Appendable accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.xml));
        assertEquals("<!DOCTYPE html>\n", accum.toString());
    }
}