package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "", "http://www.w3.org/TR/html4/strict.dtd");
        
        assertEquals("html", docType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", docType.attr("publicId"));
        assertEquals("", docType.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "", "http://www.w3.org/TR/html4/strict.dtd");
        
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "", "http://www.w3.org/TR/html4/strict.dtd");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        
        docType.outerHtmlHead(accum, depth, out);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "", "http://www.w3.org/TR/html4/strict.dtd");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        
        docType.outerHtmlTail(accum, depth, out);
        assertEquals("", accum.toString());
    }

    // Additional test cases for edge cases and exceptions can be added here

}