import static org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.helper.StringUtil;

public class DocumentTypeTest {
    @Test
    public void testDocumentTypeConstructor() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("publicId", docType.attr("publicId"));
        assertEquals("systemId", docType.attr("systemId"));
        assertEquals("baseUri", docType.baseUri());
    }

    @Test
    public void testDocumentTypeNodeName() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        Document doc = new Document();
        StringBuilder accum = new StringBuilder();
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        docType.outerHtmlHead(accum, 0, doc.outputSettings());
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" systemId>\n", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        Document doc = new Document();
        StringBuilder accum = new StringBuilder();
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        docType.outerHtmlTail(accum, 0, doc.outputSettings());
        assertEquals("", accum.toString());
    }
}