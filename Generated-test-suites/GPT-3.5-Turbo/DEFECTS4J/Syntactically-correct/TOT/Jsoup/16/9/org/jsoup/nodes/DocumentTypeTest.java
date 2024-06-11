package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("doctype", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://example.com");
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead_withPublicIdAndSystemId() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, 0, out);

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" http://www.w3.org/TR/html4/loose.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHead_withPublicIdOnly() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.attr("systemId", ""); // Set systemId to empty string
        documentType.outerHtmlHead(accum, 0, out);

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHead_withSystemIdOnly() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.attr("publicId", ""); // Set publicId to empty string
        documentType.outerHtmlHead(accum, 0, out);

        assertEquals("<!DOCTYPE html http://www.w3.org/TR/html4/loose.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHead_withoutPublicIdAndSystemId() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.attr("publicId", ""); // Set publicId to empty string
        documentType.attr("systemId", ""); // Set systemId to empty string
        documentType.outerHtmlHead(accum, 0, out);

        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlTail(accum, 0, out);

        assertEquals("", accum.toString());
    }

}