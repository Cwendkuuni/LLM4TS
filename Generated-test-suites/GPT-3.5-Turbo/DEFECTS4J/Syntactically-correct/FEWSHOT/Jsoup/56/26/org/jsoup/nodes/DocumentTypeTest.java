
package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class DocumentTypeTest {

    private DocumentType documentType;
    
    @Before
    public void setup() {
        documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
    }
    
    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }
    
    @Test
    public void testAttr() {
        assertEquals("name", documentType.attr("name"));
        assertEquals("publicId", documentType.attr("publicId"));
        assertEquals("systemId", documentType.attr("systemId"));
        assertNull(documentType.attr("notExist"));
    }
    
    @Test
    public void testHas() {
        assertTrue(documentType.has("name"));
        assertTrue(documentType.has("publicId"));
        assertTrue(documentType.has("systemId"));
        assertFalse(documentType.has("notExist"));
    }
    
    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        documentType.outerHtmlHead(accum, 0, out);
        assertEquals("<!doctype name PUBLIC \"publicId\" \"systemId\">", accum.toString());
        
        accum = new StringBuilder();
        out.syntax(Document.OutputSettings.Syntax.xml);
        documentType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlTail(accum, 0, out);
        assertTrue(accum.toString().isEmpty()); 
    }
}
