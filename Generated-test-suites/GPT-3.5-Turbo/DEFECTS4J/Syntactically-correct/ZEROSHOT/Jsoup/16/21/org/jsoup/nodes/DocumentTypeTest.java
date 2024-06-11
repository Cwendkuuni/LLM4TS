package org.jsoup.nodes;

import org.jsoup.helper.StringUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    
    private DocumentType documentType;
    
    @Before
    public void setUp() {
        documentType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
    }
    
    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        int depth = 1;
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, depth, out);
        
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" systemId\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHead_noPublicId() {
        documentType.attr("publicId", "");
        StringBuilder accum = new StringBuilder();
        int depth = 1;
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, depth, out);
        
        assertEquals("<!DOCTYPE html >", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHead_noSystemId() {
        documentType.attr("systemId", "");
        StringBuilder accum = new StringBuilder();
        int depth = 1;
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, depth, out);
        
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHead_noPublicIdAndSystemId() {
        documentType.attr("publicId", "");
        documentType.attr("systemId", "");
        StringBuilder accum = new StringBuilder();
        int depth = 1;
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, depth, out);
        
        assertEquals("<!DOCTYPE html >", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        int depth = 1;
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlTail(accum, depth, out);
        
        assertEquals("", accum.toString());
    }
}