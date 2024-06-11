package org.jsoup.nodes;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType docType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");

        assertEquals("HTML", docType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.attr("systemId"));
        assertEquals("http://www.example.com", docType.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");

        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() {
        DocumentType docType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01//EN", null, "http://www.example.com");

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        docType.outerHtmlHead(accum, 0, out);

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemId() {
        DocumentType docType = new DocumentType("HTML", null, "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        docType.outerHtmlHead(accum, 0, out);

        assertEquals("<!DOCTYPE html http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdAndSystemId() {
        DocumentType docType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        docType.outerHtmlHead(accum, 0, out);

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        docType.outerHtmlTail(accum, 0, out);

        assertEquals("", accum.toString());
    }
}