
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    
    @Test
    public void nodeNameTest() {
        DocumentType documentType = new DocumentType("name", "publicID", "systemID", "baseURI");
        
        assertEquals("#doctype", documentType.nodeName());
    }
    
    @Test
    public void outerHtmlHeadTest() {
        DocumentType documentType = new DocumentType("name", "publicID", "systemID", "baseURI");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        
        documentType.outerHtmlHead(accum, 0, out);
        
        assertEquals("<!DOCTYPE html PUBLIC \"publicID\" systemID\">", accum.toString());
    }
    
    @Test
    public void outerHtmlTailTest() {
        DocumentType documentType = new DocumentType("name", "publicID", "systemID", "baseURI");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        
        documentType.outerHtmlTail(accum, 0, out);
        
        assertEquals("", accum.toString());
    }
}
