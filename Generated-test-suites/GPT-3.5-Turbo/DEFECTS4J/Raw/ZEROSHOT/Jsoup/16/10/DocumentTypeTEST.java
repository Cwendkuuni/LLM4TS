package org.jsoup.nodes;

import org.jsoup.helper.StringUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {
    
    private DocumentType documentType;
    
    @Before
    public void setUp() {
        documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
    }
    
    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead_notBlankPublicId() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        
        documentType.attr("publicId", "-//W3C//DTD HTML 4.01 Transitional//EN");
        
        documentType.outerHtmlHead(accum, 0, out);
        
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHead_blankPublicId() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        
        documentType.attr("publicId", "");
        
        documentType.outerHtmlHead(accum, 0, out);
        
        assertEquals("<!DOCTYPE html>", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHead_notBlankSystemId() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        
        documentType.attr("systemId", "http://www.example.com/DTD/xhtml1-transitional.dtd");
        
        documentType.outerHtmlHead(accum, 0, out);
        
        assertEquals("<!DOCTYPE html http://www.example.com/DTD/xhtml1-transitional.dtd\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHead_blankSystemId() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        
        documentType.attr("systemId", "");
        
        documentType.outerHtmlHead(accum, 0, out);
        
        assertEquals("<!DOCTYPE html>", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHead_blankPublicIdAndSystemId() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        
        documentType.attr("publicId", "");
        documentType.attr("systemId", "");
        
        documentType.outerHtmlHead(accum, 0, out);
        
        assertEquals("<!DOCTYPE html>", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        
        documentType.outerHtmlTail(accum, 0, out);
        
        assertEquals("", accum.toString());
    }
}