package org.jsoup.nodes;
import org.jsoup.nodes.DocumentType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", null, "http://www.w3.org/TR/html4/strict.dtd");

        assertEquals("html", documentType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", documentType.attr("publicId"));
        assertEquals("", documentType.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", null, "http://www.w3.org/TR/html4/strict.dtd");

        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", null, "http://www.w3.org/TR/html4/strict.dtd");
        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlHead(stringBuilder, 0, new Document.OutputSettings());

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", null, "http://www.w3.org/TR/html4/strict.dtd");
        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlTail(stringBuilder, 0, new Document.OutputSettings());

        assertEquals("", stringBuilder.toString());
    }
}