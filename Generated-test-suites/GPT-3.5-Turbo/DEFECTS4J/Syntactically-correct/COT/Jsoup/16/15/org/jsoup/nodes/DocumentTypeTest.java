
import org.junit.Test;
import org.jsoup.nodes.DocumentType;

import static org.junit.Assert.assertEquals;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() {
        DocumentType documentType = new DocumentType("name", "-//W3C//DTD XHTML 1.0 Transitional//EN", null, "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemId() {
        DocumentType documentType = new DocumentType("name", null, "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("name", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, new Document.OutputSettings());
        assertEquals("", accum.toString());
    }
}
