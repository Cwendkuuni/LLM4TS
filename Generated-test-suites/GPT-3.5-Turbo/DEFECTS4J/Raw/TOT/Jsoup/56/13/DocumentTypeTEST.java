import org.jsoup.nodes.DocumentType;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    
    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHeadHtml5() {
        DocumentType documentType = new DocumentType("name", null, null, "baseUri");
        StringBuilder expected = new StringBuilder();
        expected.append("<!doctype");
        
        assertEquals(expected.toString(), documentType.outerHtmlHead(new StringBuilder(), 0, Document.OutputSettings.html()));
    }
    
    @Test
    public void testOuterHtmlHeadNonHtml5() {
        DocumentType documentType = new DocumentType("name", "publicId", null, "baseUri");
        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE").append(" ").append(documentType.attr(DocumentType.NAME)).append(" PUBLIC \"")
                .append(documentType.attr(DocumentType.PUBLIC_ID)).append('"');
        
        assertEquals(expected.toString(), documentType.outerHtmlHead(new StringBuilder(), 0, Document.OutputSettings.html()));
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder expected = new StringBuilder();
        // No action required in outerHtmlTail method, therefore expected output is an empty StringBuilder
        assertEquals(expected.toString(), documentType.outerHtmlTail(new StringBuilder(), 0, Document.OutputSettings.html()));
    }
    
    @Test
    public void testHasWithExistingAttribute() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertTrue(documentType.has(DocumentType.NAME));
    }
    
    @Test
    public void testHasWithNonExistingAttribute() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertFalse(documentType.has("nonExistingAttribute"));
    }
}