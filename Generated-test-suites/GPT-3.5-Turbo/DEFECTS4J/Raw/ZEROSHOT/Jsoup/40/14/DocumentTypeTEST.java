package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    
    private DocumentType documentType;
    
    @Before
    public void setUp() {
        documentType = new DocumentType("testName", "testPublicId", "testSystemId", "testBaseUri");
    }
    
    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        
        documentType.outerHtmlHead(accum, depth, out);
        assertEquals("<!DOCTYPE testName PUBLIC \"testPublicId\" \"testSystemId\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        
        documentType.outerHtmlTail(accum, depth, out);
        assertEquals("", accum.toString());
    }
}