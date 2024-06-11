package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class DocumentTypeTest {
    @Test
    public void testConstructor() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "https://www.example.com");
        assertEquals("html", docType.attr("name"));
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", docType.attr("systemId"));
        assertEquals("https://www.example.com", docType.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "https://www.example.com");
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType1 = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "https://www.example.com");
        DocumentType docType2 = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", null, "https://www.example.com");
        DocumentType docType3 = new DocumentType("html", null, "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "https://www.example.com");
        DocumentType docType4 = new DocumentType("html", null, null, "https://www.example.com");

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">", docType1.outerHtmlHead());
        assertEquals("<!DOCTYPE html \"https://www.example.com\">", docType2.outerHtmlHead());
        assertEquals("<!DOCTYPE html \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">", docType3.outerHtmlHead());
        assertEquals("<!doctype>", docType4.outerHtmlHead());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "https://www.example.com");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, new Document.OutputSettings());

        assertEquals("", accum.toString());
    }

    @Test
    public void testHas() {
        DocumentType docType1 = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "https://www.example.com");
        DocumentType docType2 = new DocumentType("html", null, null, "https://www.example.com");

        assertTrue(docType1.has("publicId"));
        assertFalse(docType1.has("systemId"));
        assertTrue(docType2.has("name"));
        assertFalse(docType2.has("publicId"));
        assertFalse(docType2.has("systemId"));
    }
}