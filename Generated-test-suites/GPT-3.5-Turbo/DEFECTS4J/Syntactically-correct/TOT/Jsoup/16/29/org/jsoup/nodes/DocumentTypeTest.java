import org.jsoup.nodes.DocumentType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DocumentTypeTest {
    
    private DocumentType documentType;
    
    @Before
    public void setup() {
        documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
    }
    
    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead_blankPublicId() {
        DocumentType docType = new DocumentType("html", "", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html SYSTEM \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHead_blankSystemId() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }
    
    // Additional test cases for other methods in the class
    
    @Test
    public void testName() {
        assertEquals("html", documentType.attr("name"));
    }
    
    @Test
    public void testAttr_publicId() {
        assertEquals("-//W3C//DTD HTML 4.01//EN", documentType.attr("publicId"));
    }
    
    @Test
    public void testAttr_systemId() {
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.attr("systemId"));
    }
    
    @Test
    public void testAttr_invalidAttr() {
        assertEquals("", documentType.attr("invalidAttr"));
    }
    
    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);
        assertTrue(accum.toString().isEmpty());
    }
}