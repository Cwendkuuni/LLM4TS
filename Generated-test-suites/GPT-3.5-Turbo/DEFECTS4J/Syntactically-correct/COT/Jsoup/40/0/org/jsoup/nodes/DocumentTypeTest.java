
import org.junit.Test;
import org.jsoup.nodes.DocumentType;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType documentType = new DocumentType("doctype-name", "public-id", "system-id", "base-uri");

        assertEquals("doctype-name", documentType.attr("name"));
        assertEquals("public-id", documentType.attr("publicId"));
        assertEquals("system-id", documentType.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("doctype-name", "public-id", "system-id", "base-uri");

        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("doctype-name", "public-id", "system-id", "base-uri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, 0, out);

        assertEquals("<!DOCTYPE doctype-name PUBLIC \"public-id\" \"system-id\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("doctype-name", "public-id", "system-id", "base-uri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlTail(accum, 0, out);

        assertEquals("", accum.toString());
    }
}
