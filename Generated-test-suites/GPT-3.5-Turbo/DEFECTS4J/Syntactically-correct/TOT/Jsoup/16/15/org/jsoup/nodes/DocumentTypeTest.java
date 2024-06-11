import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertNotNull(doctype);
        assertEquals("name", doctype.attr("name"));
        assertEquals("publicId", doctype.attr("publicId"));
        assertEquals("systemId", doctype.attr("systemId"));
        assertEquals("baseUri", doctype.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHead_withPublicId() {
        DocumentType doctype = new DocumentType("name", "publicId", "", "baseUri");
        StringBuilder sb = new StringBuilder();
        doctype.outerHtmlHead(sb, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\">", sb.toString());
    }

    @Test
    public void testOuterHtmlHead_withSystemId() {
        DocumentType doctype = new DocumentType("name", "", "systemId", "baseUri");
        StringBuilder sb = new StringBuilder();
        doctype.outerHtmlHead(sb, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html systemId\">", sb.toString());
    }

    @Test
    public void testOuterHtmlHead_withoutIds() {
        DocumentType doctype = new DocumentType("name", "", "", "baseUri");
        StringBuilder sb = new StringBuilder();
        doctype.outerHtmlHead(sb, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html>", sb.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder sb = new StringBuilder();
        doctype.outerHtmlTail(sb, 0, new Document.OutputSettings());
        assertEquals("", sb.toString());
    }
}