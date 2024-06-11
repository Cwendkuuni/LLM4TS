import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document.OutputSettings.*;
import org.junit.Test;

public class DocumentTypeTest {
    @Test
    public void testConstructor() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("name", documentType.attr(NAME));
        assertEquals("publicId", documentType.attr(PUBLIC_ID));
        assertEquals("systemId", documentType.attr(SYSTEM_ID));
        assertEquals("baseUri", documentType.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        Appendable accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings outputSettings = new Document.OutputSettings(Syntax.html, false);
        try {
            documentType.outerHtmlHead(accum, depth, outputSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        Appendable accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings outputSettings = new Document.OutputSettings(Syntax.html, false);
        documentType.outerHtmlTail(accum, depth, outputSettings);
        assertEquals("", accum.toString());
    }

    @Test
    public void testHas() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals(true, documentType.has(NAME));
        assertEquals(true, documentType.has(PUBLIC_ID));
        assertEquals(true, documentType.has(SYSTEM_ID));
    }
}