package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://example.com");
        assertEquals("html", documentType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.0//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/REC-html40/strict.dtd", documentType.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://example.com");
        assertEquals("#doctype", documentType.nodeName());
    }

    // Edge case: Testing for attributes with empty values
    @Test
    public void testOuterHtmlHeadEmptyValues() {
        DocumentType documentType = new DocumentType("", "", "", "http://example.com");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE >", accum.toString());
    }

    // Edge case: Testing for null values in constructor parameters
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullValues() {
        new DocumentType(null, null, null, "http://example.com");
    }

}