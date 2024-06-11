import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.StringUtil;

public class DocumentTypeTest {

    @Test
    void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        Assertions.assertEquals("#doctype", documentType.nodeName());
        Assertions.assertEquals(name, documentType.attr("name"));
        Assertions.assertEquals(publicId, documentType.attr("publicId"));
        Assertions.assertEquals(systemId, documentType.attr("systemId"));
        Assertions.assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    void testEmptyDocumentType() {
        DocumentType documentType = new DocumentType("html", "", "", "");

        Assertions.assertEquals("#doctype", documentType.nodeName());
        Assertions.assertNull(documentType.attr("publicId"));
        Assertions.assertNull(documentType.attr("systemId"));
        Assertions.assertNull(documentType.baseUri());

        StringBuilder accumulator = new StringBuilder();
        documentType.outerHtmlHead(accumulator, 0, new Document.OutputSettings());
        String expected = "<!DOCTYPE html>\n";
        Assertions.assertEquals(expected, accumulator.toString());
    }

    @Test
    void testDocumentTypeOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "http://example.com");
        StringBuilder accumulator = new StringBuilder();

        documentType.outerHtmlHead(accumulator, 0, new Document.OutputSettings());

        String expected = "<!DOCTYPE html PUBLIC \"publicId\" systemId>\n";
        Assertions.assertEquals(expected, accumulator.toString());
    }

    @Test
    void testInvalidDocumentType() {
        DocumentType documentType = new DocumentType("html", null, null, "");
        Assertions.assertThrows(NullPointerException.class, () -> documentType.outerHtmlHead(new StringBuilder(), 0, new Document.OutputSettings()));
    }
}