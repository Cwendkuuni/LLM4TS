import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.helper.StringUtil;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, depth, out);
        String expected = "<!DOCTYPE html PUBLIC \"publicId\" systemId\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlTail(accum, depth, out);
        String expected = ""; // No implementation for outerHtmlTail, so the expected result is empty string
        assertEquals(expected, accum.toString());
    }
}