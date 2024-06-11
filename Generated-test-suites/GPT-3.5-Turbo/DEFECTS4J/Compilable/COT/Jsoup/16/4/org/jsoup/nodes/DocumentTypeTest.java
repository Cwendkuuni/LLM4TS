package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    @Test
    public void testConstructorWithValidArguments() {
        DocumentType documentType = new DocumentType("doctype", "-//W3C//DTD HTML 4.0 Transitional//EN", "http://www.w3.org/TR/REC-html40/loose.dtd", "http://example.com");
        assertEquals("doctype", documentType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.0 Transitional//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/REC-html40/loose.dtd", documentType.attr("systemId"));
        assertEquals("http://example.com", documentType.baseUri());
    }

    @Test
    public void testConstructorWithEmptyArguments() {
        DocumentType documentType = new DocumentType("", "", "", "");
        assertEquals("", documentType.attr("name"));
        assertEquals("", documentType.attr("publicId"));
        assertEquals("", documentType.attr("systemId"));
        assertEquals("", documentType.baseUri());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullArguments() {
        new DocumentType(null, null, null, null);
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("doctype", "-//W3C//DTD HTML 4.0 Transitional//EN", "http://www.w3.org/TR/REC-html40/loose.dtd", "http://example.com");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() {
        DocumentType documentType = new DocumentType("doctype", "-//W3C//DTD HTML 4.0 Transitional//EN", "", "");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        StringBuilder accum = new StringBuilder();
        
        documentType.outerHtmlHead(accum, 0, outputSettings);
        
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemId() {
        DocumentType documentType = new DocumentType("doctype", "", "http://www.w3.org/TR/REC-html40/loose.dtd", "");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        StringBuilder accum = new StringBuilder();
        
        documentType.outerHtmlHead(accum, 0, outputSettings);
        
        assertEquals("<!DOCTYPE html http://www.w3.org/TR/REC-html40/loose.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithoutPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("doctype", "", "", "");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        StringBuilder accum = new StringBuilder();
        
        documentType.outerHtmlHead(accum, 0, outputSettings);
        
        assertEquals("<!DOCTYPE html>", accum.toString());
    }
}