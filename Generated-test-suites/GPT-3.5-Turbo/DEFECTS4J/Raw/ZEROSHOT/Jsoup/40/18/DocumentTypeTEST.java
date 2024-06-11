import org.jsoup.nodes.DocumentType;
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
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, 0, out);

        assertEquals("<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlTail(accum, 0, out);

        assertEquals("", accum.toString());
    }
}