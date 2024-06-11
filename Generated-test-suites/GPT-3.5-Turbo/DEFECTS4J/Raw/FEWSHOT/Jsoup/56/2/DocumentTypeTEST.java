
package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {
    
    @Test
    public void testConstructor() {
        DocumentType docType = new DocumentType("html", "", "", "http://example.com");
        assertEquals("html", docType.attr("name"));
        assertEquals("", docType.attr("publicId"));
        assertEquals("", docType.attr("systemId"));
        assertEquals("http://example.com", docType.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", "", "", "http://example.com");
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        Document.OutputSettings out = new Document.OutputSettings();
        DocumentType docType = new DocumentType("html", "", "", "http://example.com");
        
        // Test case for Syntax.html with no publicId and systemId
        out.syntax(Document.OutputSettings.Syntax.html);
        StringBuilder sb = new StringBuilder();
        docType.outerHtmlHead(sb, 0, out);
        assertEquals("<!doctype", sb.toString());
        
        // Test case for Syntax.html with publicId and systemId
        out.syntax(Document.OutputSettings.Syntax.html);
        docType.attr("publicId", "-//W3C//DTD HTML 4.01//EN");
        docType.attr("systemId", "http://www.w3.org/TR/html4/strict.dtd");
        sb = new StringBuilder();
        docType.outerHtmlHead(sb, 0, out);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", sb.toString());
        
        // Test case for Syntax.xhtml with publicId and systemId
        out.syntax(Document.OutputSettings.Syntax.xhtml);
        docType.attr("publicId", "-//W3C//DTD XHTML 1.0 Strict//EN");
        docType.attr("systemId", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        sb = new StringBuilder();
        docType.outerHtmlHead(sb, 0, out);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Document.OutputSettings out = new Document.OutputSettings();
        DocumentType docType = new DocumentType("html", "", "", "http://example.com");
        StringBuilder sb = new StringBuilder();
        docType.outerHtmlTail(sb, 0, out);
        assertEquals("", sb.toString());
    }

    @Test
    public void testHas() {
        DocumentType docType = new DocumentType("html", "", "", "http://example.com");
        assertFalse(docType.has("name"));
        assertFalse(docType.has("publicId"));
        assertFalse(docType.has("systemId"));

        docType.attr("name", "html");
        assertTrue(docType.has("name"));
    }

}
