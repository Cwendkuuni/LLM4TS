package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DocumentTypeTest {

    // Test case for DocumentType constructor
    @Test
    public void testDocumentTypeConstructor() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/");
        
        assertEquals("html", doctype.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.attr("systemId"));
    }

    // Test case for nodeName method
    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/");
        
        assertEquals("#doctype", doctype.nodeName());
    }

    // Test case for outerHtmlHead method with publicId
    @Test
    public void testOuterHtmlHeadWithPublicId() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "", "http://www.w3.org/");
        StringBuilder sb = new StringBuilder();
        doctype.outerHtmlHead(sb, 0, new Document.OutputSettings());
        
        assertTrue(sb.toString().contains("PUBLIC \"-//W3C//DTD HTML 4.01//EN\""));
    }

    // Test case for outerHtmlHead method with systemId
    @Test
    public void testOuterHtmlHeadWithSystemId() {
        DocumentType doctype = new DocumentType("html", "", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/");
        StringBuilder sb = new StringBuilder();
        doctype.outerHtmlHead(sb, 0, new Document.OutputSettings());
        
        assertTrue(sb.toString().contains("http://www.w3.org/TR/html4/strict.dtd"));
    }

    // Test case for outerHtmlTail method
    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/");
        StringBuilder sb = new StringBuilder();
        doctype.outerHtmlTail(sb, 0, new Document.OutputSettings());
        
        assertEquals("", sb.toString());
    }
}