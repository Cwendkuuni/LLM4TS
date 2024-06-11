package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    // Test case for the constructor
    @Test
    public void testConstructor() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://example.com");

        assertEquals("html", docType.nodeName());
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", docType.attr("systemId"));
        assertEquals("http://example.com", docType.baseUri());
    }
    
    // Test case for nodeName() method
    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://example.com");
        
        assertEquals("#doctype", docType.nodeName());
    }
    
    // Test case for outerHtmlHead() method
    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://example.com");
        
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        
        docType.outerHtmlHead(accum, 0, out);
        
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" http://www.w3.org/TR/html4/loose.dtd>", accum.toString());
    }
    
    // Test case for outerHtmlTail() method
    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://example.com");
        
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        
        docType.outerHtmlTail(accum, 0, out);
        
        assertEquals("", accum.toString());
    }
}