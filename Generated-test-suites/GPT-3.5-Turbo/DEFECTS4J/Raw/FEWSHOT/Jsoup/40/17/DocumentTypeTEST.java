import org.jsoup.nodes.DocumentType;
import org.jsoup.helper.Validate;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Node;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    @org.junit.Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }

    @org.junit.Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, depth, out);
        assertEquals("<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">", accum.toString());
    }

    @org.junit.Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlTail(accum, depth, out);
        assertEquals("", accum.toString());
    }
}