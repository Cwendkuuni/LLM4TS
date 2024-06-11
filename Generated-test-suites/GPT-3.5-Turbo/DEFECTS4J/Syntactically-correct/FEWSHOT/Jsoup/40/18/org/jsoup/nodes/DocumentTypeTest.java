package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class DocumentTypeTest {
    @Test
    public void testConstructor() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.w3.org/xhtml1/");
        
        assertEquals("html", docType.nodeName());
        assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", docType.attr("systemId"));
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.w3.org/xhtml1/");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();

        docType.outerHtmlHead(accum, depth, out);
        
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.w3.org/xhtml1/");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();

        docType.outerHtmlTail(accum, depth, out);
        
        assertFalse(accum.toString().contains(">"));
    }
}