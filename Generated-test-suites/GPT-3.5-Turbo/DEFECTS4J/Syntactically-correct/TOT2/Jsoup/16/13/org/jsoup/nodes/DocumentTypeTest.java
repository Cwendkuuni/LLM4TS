package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType doctype = new DocumentType("test", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.org");
        
        assertEquals("test", doctype.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("test", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.org");
        
        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType doctype = new DocumentType("test", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.org");
        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlHead(accum, 0, new Document.OutputSettings());
        
        assertTrue(accum.toString().contains("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" http://www.w3.org/TR/html4/strict.dtd\">"));
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("test", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.org");
        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlTail(accum, 0, new Document.OutputSettings());
        
        assertEquals("", accum.toString());
    }
}