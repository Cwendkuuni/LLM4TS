
import org.jsoup.nodes.DocumentType;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType docType = new DocumentType("html", "", "", "http://www.example.com");
        
        assertEquals("html", docType.attr("name"));
        assertEquals("", docType.attr("publicId"));
        assertEquals("", docType.attr("systemId"));
        assertEquals("http://www.example.com", docType.baseUri());
    }
    
    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", "", "", "http://www.example.com");
        
        assertEquals("#doctype", docType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://www.example.com");
        
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0//EN\" http://www.w3.org/TR/REC-html40/strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "", "", "http://www.example.com");
        
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, new Document.OutputSettings());
        
        assertEquals("", accum.toString());
    }
    
}
