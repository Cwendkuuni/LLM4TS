
package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() throws Exception {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, Document.OutputSettings.html());
        
        String expected = "<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">";
        assertEquals(expected, accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() throws Exception {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, Document.OutputSettings.html());
        
        assertEquals("", accum.toString());
    }

}
