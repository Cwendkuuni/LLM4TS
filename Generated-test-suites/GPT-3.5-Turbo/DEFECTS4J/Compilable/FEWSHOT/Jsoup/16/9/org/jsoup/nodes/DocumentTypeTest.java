package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentType() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/TR/html4/");
        
        assertEquals("html", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/TR/html4/");
        
        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" http://www.w3.org/TR/html4/strict.dtd>");
        
        StringBuilder actualHtml = new StringBuilder();
        docType.outerHtmlHead(actualHtml, 0, null);
        
        assertEquals(expectedHtml.toString(), actualHtml.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/TR/html4/");
        
        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append("");
        
        StringBuilder actualHtml = new StringBuilder();
        docType.outerHtmlTail(actualHtml, 0, null);
        
        assertEquals(expectedHtml.toString(), actualHtml.toString());
    }
}