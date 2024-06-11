package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import static org.jsoup.helper.StringUtil.isBlank;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "html PUBLIC -//W3C//DTD HTML 4.01//EN", "DTD_HTML_4_STRICT.dtd", "http://example.com/");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("html PUBLIC -//W3C//DTD HTML 4.01//EN", docType.attr("outerHTML"));
        assertEquals("DTD_HTML_4_STRICT.dtd", docType.attr("systemId"));
        assertEquals("http://example.com/", docType.baseUri());
    }

    @Test
    public void testEmptyPublicId() {
        DocumentType docType = new DocumentType("html", "", "", "http://example.com/");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("<!DOCTYPE html>", docType.attr("outerHTML"));
        assertEquals("", docType.attr("publicId"));
        assertEquals("", docType.attr("systemId"));
        assertEquals("http://example.com/", docType.baseUri());
    }

    @Test
    public void testEmptySystemId() {
        DocumentType docType = new DocumentType("html", "html PUBLIC -//W3C//DTD HTML 4.01//EN", "", "http://example.com/");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("<!DOCTYPE html PUBLIC -//W3C//DTD HTML 4.01//EN>", docType.attr("outerHTML"));
        assertEquals("", docType.attr("publicId"));
        assertEquals("", docType.attr("systemId"));
        assertEquals("http://example.com/", docType.baseUri());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullName() {
        new DocumentType(null, "html PUBLIC -//W3C//DTD HTML 4.01//EN", "", "http://example.com/");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPublicId() {
        new DocumentType("html", null, "", "http://example.com/");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSystemId() {
        new DocumentType("html", "html PUBLIC -//W3C//DTD HTML 4.01//EN", null, "http://example.com/");
    }

    @Test
    public void testToString() {
        DocumentType docType = new DocumentType("html", "html PUBLIC -//W3C//DTD HTML 4.01//EN", "DTD_HTML_4_STRICT.dtd", "http://example.com/");
        String result = docType.toString();
        String expected = "<html>";
        assertEquals(expected, result);
    }

    @Test
    public void testOuterHtmlHead() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "html PUBLIC -//W3C//DTD HTML 4.01//EN", "DTD_HTML_4_STRICT.dtd", "http://example.com/");
        Element node = doc.parseBodyFragment("<!DOCTYPE html>");
        node.outerHtmlHead(new StringBuilder(), 0, null);
        String result = node.outerHtml();
        String expected = "<!DOCTYPE html>";
        assertEquals(expected, result);
    }

    @Test
    public void testOuterHtmlTail() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "html PUBLIC -//W3C//DTD HTML 4.01//EN", "DTD_HTML_4_STRICT.dtd", "http://example.com/");
        Element node = doc.parseBodyFragment("<!DOCTYPE html>");
        StringBuilder accum = new StringBuilder();
        node.outerHtmlTail(accum, 0, null);
        String result = accum.toString();
        String expected = "";
        assertEquals(expected, result);
    }

    @Test
    public void testOuterHtmlWithPublicId() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "html PUBLIC -//W3C//DTD HTML 4.01//EN", "DTD_HTML_4_STRICT.dtd", "http://example.com/");
        Element node = doc.parseBodyFragment("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"DTD_HTML_4_STRICT.dtd\">");
        StringBuilder accum = new StringBuilder();
        node.outerHtmlHead(accum, 0, null);
        String resultHead = accum.toString();
        String expectedHead = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"DTD_HTML_4_STRICT.dtd\">";
        assertEquals(expectedHead, resultHead);

        accum.setLength(0);
        node.outerHtmlTail(accum, 0, null);
        String resultTail = accum.toString();
        String expectedTail = "";
        assertEquals(expectedTail, resultTail);
    }

    @Test
    public void testOuterHtmlWithSystemId() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "html PUBLIC -//W3C//DTD HTML 4.01//EN", "DTD_HTML_4_STRICT.dtd", "http://example.com/");
        Element node = doc.parseBodyFragment("<!DOCTYPE html SYSTEM \"DTD_HTML_4_STRICT.dtd\">");
        StringBuilder accum = new StringBuilder();
        node.outerHtmlHead(accum, 0, null);
        String resultHead = accum.toString();
        String expectedHead = "<!DOCTYPE html SYSTEM \"DTD_HTML_4_STRICT.dtd\">";
        assertEquals(expectedHead, resultHead);

        accum.setLength(0);
        node.outerHtmlTail(accum, 0, null);
        String resultTail = accum.toString();
        String expectedTail = "";
        assertEquals(expectedTail, resultTail);
    }

    @Test
    public void testOuterHtmlWithPublicIdAndSystemId() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"DTD_HTML_4_STRICT.dtd\"", "DTD_HTML_4_STRICT.dtd", "http://example.com/");
        Element node = doc.parseBodyFragment("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"DTD_HTML_4_STRICT.dtd\">");
        StringBuilder accum = new StringBuilder();
        node.outerHtmlHead(accum, 0, null);
        String resultHead = accum.toString();
        String expectedHead = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"DTD_HTML_4_STRICT.dtd\">";
        assertEquals(expectedHead, resultHead);

        accum.setLength(0);
        node.outerHtmlTail(accum, 0, null);
        String resultTail = accum.toString();
        String expectedTail = "";
        assertEquals(expectedTail, resultTail);
    }
}