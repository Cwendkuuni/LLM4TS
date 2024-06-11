package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/TR/html4/");
        assertEquals("html", dt.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", dt.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", dt.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/TR/html4/");
        assertEquals("#doctype", dt.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/TR/html4/");
        StringBuilder expected = new StringBuilder("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
        StringBuilder actual = new StringBuilder();
        dt.outerHtmlHead(actual, 0, new Document.OutputSettings());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/TR/html4/");
        StringBuilder expected = new StringBuilder();
        StringBuilder actual = new StringBuilder();
        dt.outerHtmlTail(actual, 0, new Document.OutputSettings());
        assertEquals(expected.toString(), actual.toString());
    }

}