
package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    
    @Test
    public void testConstructor() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://www.example.com");
        assertNotNull(doctype);
    }
    
    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://www.example.com");
        assertEquals("#doctype", doctype.nodeName());
    }
    
    @Test
    public void testOuterHtmlHeadWithHTML5Doctype() {
        DocumentType doctype = new DocumentType("html", null, null, "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        out.indentAmount(4);
        
        try {
            doctype.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertEquals("<!doctype", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHeadWithNonHTML5Doctype() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.xml);
        out.indentAmount(4);
        
        try {
            doctype.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHeadWithPublicIdOnly() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", null, "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.xml);
        out.indentAmount(4);
        
        try {
            doctype.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0//EN\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHeadWithSystemIdOnly() {
        DocumentType doctype = new DocumentType("html", null, "http://www.w3.org/TR/REC-html40/strict.dtd", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.xml);
        out.indentAmount(4);
        
        try {
            doctype.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertEquals("<!DOCTYPE html \"http://www.w3.org/TR/REC-html40/strict.dtd\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHeadWithNoPublicIdAndSystemId() {
        DocumentType doctype = new DocumentType("html", null, null, "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.xml);
        out.indentAmount(4);
        
        try {
            doctype.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertEquals("<!DOCTYPE html>", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.xml);
        out.indentAmount(4);
        
        doctype.outerHtmlTail(accum, 0, out);
        
        assertEquals("", accum.toString());
    }
    
    @Test
    public void testHasWithAttribute() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://www.example.com");
        assertTrue(doctype.has(DocumentType.NAME));
        assertTrue(doctype.has(DocumentType.PUBLIC_ID));
        assertTrue(doctype.has(DocumentType.SYSTEM_ID));
    }
    
    @Test
    public void testHasWithoutAttribute() {
        DocumentType doctype = new DocumentType("html", null, null, "http://www.example.com");
        assertFalse(doctype.has(DocumentType.PUBLIC_ID));
        assertFalse(doctype.has(DocumentType.SYSTEM_ID));
    }
}
