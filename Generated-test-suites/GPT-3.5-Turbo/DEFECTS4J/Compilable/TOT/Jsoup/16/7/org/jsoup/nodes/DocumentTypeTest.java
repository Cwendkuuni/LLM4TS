package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class DocumentTypeTest {

    private DocumentType docType;

    @Before
    public void setUp() {
        docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "https://www.example.com");
    }

    @Test
    public void testConstructor() {
        assertEquals("html", docType.attr("name"));
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", docType.attr("systemId"));
        assertEquals("https://www.example.com", docType.baseUri());
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings();
        docType.outerHtmlHead(accum, 0, settings);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings();
        docType.outerHtmlTail(accum, 0, settings);
        assertEquals("", accum.toString());
    }
}