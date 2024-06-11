package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        String name = "html";
        String publicId = "-//W3C//DTD HTML 4.01 Transitional//EN";
        String systemId = "http://www.w3.org/TR/html4/loose.dtd";
        String baseUri = "";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals(name, docType.attr("name"));
        assertEquals(publicId, docType.attr("publicId"));
        assertEquals(systemId, docType.attr("systemId"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEmptyName() {
        String name = "";
        String publicId = "-//W3C//DTD HTML 4.01 Transitional//EN";
        String systemId = "http://www.w3.org/TR/html4/loose.dtd";
        String baseUri = "";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);
    }

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "");

        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "");

        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();

        docType.outerHtmlHead(accum, depth, out);

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "");

        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();

        docType.outerHtmlTail(accum, depth, out);

        assertEquals("", accum.toString());
    }
}