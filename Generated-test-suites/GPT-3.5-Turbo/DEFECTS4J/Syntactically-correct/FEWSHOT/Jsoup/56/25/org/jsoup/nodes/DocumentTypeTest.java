package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class DocumentTypeTest {
    @Test
    public void testConstructor() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.w3.org/1999/xhtml");

        assertEquals("html", doctype.attr("name"));
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", doctype.attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", doctype.attr("systemId"));
        assertEquals("http://www.w3.org/1999/xhtml", doctype.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.w3.org/1999/xhtml");

        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.w3.org/1999/xhtml");
        Document.OutputSettings out = new Document.OutputSettings();

        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlHead(accum, 0, out);

        assertTrue(accum.toString().startsWith("<!DOCTYPE"));
        assertTrue(accum.toString().endsWith(">"));
        assertTrue(accum.toString().contains("html"));
        assertTrue(accum.toString().contains("-//W3C//DTD XHTML 1.0 Transitional//EN"));
        assertTrue(accum.toString().contains("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"));
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.w3.org/1999/xhtml");

        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlTail(accum, 0, new Document.OutputSettings());

        assertEquals("", accum.toString());
    }

    @Test
    public void testHas() {
        DocumentType doctype1 = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.w3.org/1999/xhtml");
        DocumentType doctype2 = new DocumentType("html", "", "", "");

        assertTrue(doctype1.has("name"));
        assertTrue(doctype1.has("publicId"));
        assertTrue(doctype1.has("systemId"));

        assertFalse(doctype2.has("name"));
        assertFalse(doctype2.has("publicId"));
        assertFalse(doctype2.has("systemId"));
    }
}