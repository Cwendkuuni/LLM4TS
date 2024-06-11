package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/");
        assertEquals("#doctype", doctype.nodeName());
    }
    
    @Test
    public void testOuterHtmlHeadWithPublicId() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        doctype.outerHtmlHead(accum, 0, out);

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" http://www.w3.org/TR/html4/strict.dtd>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithoutPublicId() {
        DocumentType doctype = new DocumentType("html", "", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        doctype.outerHtmlHead(accum, 0, out);

        assertEquals("<!DOCTYPE html http://www.w3.org/TR/html4/strict.dtd>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithoutSystemId() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "", "http://www.w3.org/");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        doctype.outerHtmlHead(accum, 0, out);

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithoutPublicIdAndSystemId() {
        DocumentType doctype = new DocumentType("html", "", "", "http://www.w3.org/");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        doctype.outerHtmlHead(accum, 0, out);

        assertEquals("<!DOCTYPE html>", accum.toString());
    }

}