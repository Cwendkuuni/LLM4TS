package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://www.example.com");

        assertEquals("html", dt.nodeName());
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", dt.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", dt.attr("systemId"));
        assertEquals("http://www.example.com", dt.baseUri());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdAndSystemId() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = null;

        dt.outerHtmlHead(accum, 0, out);
        String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" http://www.w3.org/TR/html4/loose.dtd>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdOnly() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = null;

        dt.outerHtmlHead(accum, 0, out);
        String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemIdOnly() {
        DocumentType dt = new DocumentType("html", "", "http://www.w3.org/TR/html4/loose.dtd", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = null;

        dt.outerHtmlHead(accum, 0, out);
        String expected = "<!DOCTYPE html http://www.w3.org/TR/html4/loose.dtd>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithoutPublicAndSystemId() {
        DocumentType dt = new DocumentType("html", "", "", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = null;

        dt.outerHtmlHead(accum, 0, out);
        String expected = "<!DOCTYPE html>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = null;

        dt.outerHtmlTail(accum, 0, out);
        assertEquals("", accum.toString());
    }
}