import org.junit.Test;
import org.mockito.Mock;
import org.jsoup.nodes.DocumentType;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Mock
    DocumentType documentType;

    @Test
    public void testDocumentTypeConstructor() {
        documentType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        assertNotNull(documentType);
        assertEquals("doctype", documentType.attr("name"));
        assertEquals("publicId", documentType.attr("publicId"));
        assertEquals("systemId", documentType.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        documentType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        documentType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        StringBuilder stringBuilder = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        documentType.outerHtmlHead(stringBuilder, 0, outputSettings);
        assertEquals("<!DOCTYPE doctype PUBLIC \"publicId\" \"systemId\">", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        documentType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        StringBuilder stringBuilder = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        documentType.outerHtmlTail(stringBuilder, 0, outputSettings);
        assertEquals("", stringBuilder.toString());
    }
}