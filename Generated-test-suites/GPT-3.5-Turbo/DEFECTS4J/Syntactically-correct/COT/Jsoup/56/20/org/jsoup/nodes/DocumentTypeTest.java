import org.junit.Test;

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
        Document.OutputSettings out = new Document.OutputSettings();
        out.setSyntax(Syntax.html);
        documentType.outerHtmlHead(accum, 0, out);
        assertEquals("<!doctype", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlTail(accum, 0, out);
        assertEquals("", accum.toString());
    }
    
    @Test
    public void testHas() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertTrue(documentType.has("name"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeNullParameters() {
        new DocumentType(null, null, null, "baseUri");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeEmptyParameters() {
        new DocumentType("", "", "", "baseUri");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeNullBaseUri() {
        new DocumentType("name", "publicId", "systemId", null);
    }
    
    @Test(expected = IOException.class)
    public void testOuterHtmlHeadIOException() throws IOException {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.setSyntax(Syntax.html);
        documentType.outerHtmlHead(accum, 0, out);
        throw new IOException();
    }
}