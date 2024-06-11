package org.jsoup.nodes;

import org.jsoup.helper.StringUtil;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");

        assertEquals("html", docType.nodeName());
        assertEquals("-//W3C//DTD HTML 4.01//EN", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.attr("systemId"));
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        docType.outerHtmlHead(accum, 0, out);

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        docType.outerHtmlTail(accum, 0, out);

        assertEquals("", accum.toString());
    }
}