
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    
    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", doctype.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() throws IOException {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        doctype.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() throws IOException {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        doctype.outerHtmlTail(accum, 0, out);
        assertEquals("", accum.toString());
    }
    
    @Test
    public void testHas() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertTrue(doctype.has("name"));
        assertTrue(doctype.has("publicId"));
        assertTrue(doctype.has("systemId"));
    }
    
    @Test
    public void testHas_blankAttribute() {
        DocumentType doctype = new DocumentType("", "", "", "baseUri");
        assertFalse(doctype.has("name"));
        assertFalse(doctype.has("publicId"));
        assertFalse(doctype.has("systemId"));
    }
}
