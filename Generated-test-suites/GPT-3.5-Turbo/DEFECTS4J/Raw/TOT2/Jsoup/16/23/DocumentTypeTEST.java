package org.jsoup.nodes;

import org.jsoup.helper.StringUtil;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit test cases for DocumentType class
 */
public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", null, "http://www.w3.org/TR/html4/strict.dtd");
        assertEquals("html", doctype.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctype.attr("publicId"));
    }

    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", null, "http://www.w3.org/TR/html4/strict.dtd");
        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", null, "http://www.w3.org/TR/html4/strict.dtd");
        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", null, "http://www.w3.org/TR/html4/strict.dtd");
        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlTail(accum, 0, new Document.OutputSettings());
        assertEquals("", accum.toString());
    }
}