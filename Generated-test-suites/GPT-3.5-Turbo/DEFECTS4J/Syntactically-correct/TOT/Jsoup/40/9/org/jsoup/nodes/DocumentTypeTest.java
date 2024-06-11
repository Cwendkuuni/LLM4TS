package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "");
    }

    // Test for constructor
    @Test
    public void testConstructor() {
        assertNotNull(documentType);
        assertEquals("html", documentType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.attr("systemId"));
        assertNotNull(documentType.baseUri());
    }

    // Test for nodeName
    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    // Test for outerHtmlHead
    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());

        // Test when name attribute is empty
        documentType.attr("name", "");
        accum.setLength(0);
        documentType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());


        // Test when publicId attribute is empty
        documentType.attr("name", "html");
        documentType.attr("publicId", "");
        accum.setLength(0);
        documentType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());

        // Test when systemId attribute is empty
        documentType.attr("publicId", "-//W3C//DTD HTML 4.01//EN");
        documentType.attr("systemId", "");
        accum.setLength(0);
        documentType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">", accum.toString());

        // Test when all attributes are empty
        documentType.attr("systemId", "");
        accum.setLength(0);
        documentType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    // Test for outerHtmlTail
    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlTail(accum, 0, out);
        assertEquals("", accum.toString());
    }

}