
import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");
        Appendable accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings(Syntax.html);
        try {
            docType.outerHtmlHead(accum, 0, out);
        } catch (IOException e) {
            fail("IOException occurred");
        }
        assertEquals("<!doctype", accum.toString());

        docType.removeAttr(DocumentType.PUBLIC_ID);
        accum = new StringBuilder();
        try {
            docType.outerHtmlHead(accum, 0, out);
        } catch (IOException e) {
            fail("IOException occurred");
        }
        assertEquals("<!doctype", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");
        Appendable accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        docType.outerHtmlTail(accum, 0, out);
        assertEquals("", accum.toString());
    }
}
