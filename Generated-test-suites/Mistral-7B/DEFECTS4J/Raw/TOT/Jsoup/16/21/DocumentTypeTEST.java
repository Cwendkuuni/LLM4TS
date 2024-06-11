import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    @Test
    public void testDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseURI");
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals("publicID", documentType.attr("publicId"));
        assertEquals("systemID", documentType.attr("systemId"));
        assertEquals("baseURI", documentType.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "", "", "");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        Document document = Jsoup.parse("<!DOCTYPE html>");
        DocumentType documentType = document.documentType();

        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE html>");

        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlHead(actual, 0, null);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Document document = Jsoup.parse("<!DOCTYPE html>");
        DocumentType documentType = document.documentType();

        StringBuilder expected = new StringBuilder();
        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlTail(actual, 0, null);

        assertTrue(actual.toString().isEmpty());
    }

    @Test
    public void testNullAttributes() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        assertEquals("#doctype", documentType.nodeName());
        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
    }

    @Test
    public void testEmptyAttributes() {
        DocumentType documentType = new DocumentType("html", "", "", "");
        assertEquals("#doctype", documentType.nodeName());
        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
    }

    @Test
    public void testAttributesWithValues() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseURI");
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals("publicID", documentType.attr("publicId"));
        assertEquals("systemID", documentType.attr("systemId"));
        assertEquals("baseURI", documentType.baseUri());
    }

    @Test(expected = NullPointerException.class)
    public void testNullDocumentType() {
        DocumentType documentType = null;
        documentType.outerHtmlHead(null, 0, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullAccumulator() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseURI");
        documentType.outerHtmlHead(null, 0, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullOutSettings() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseURI");
        documentType.outerHtmlHead(new StringBuilder(), 0, null);
    }
}