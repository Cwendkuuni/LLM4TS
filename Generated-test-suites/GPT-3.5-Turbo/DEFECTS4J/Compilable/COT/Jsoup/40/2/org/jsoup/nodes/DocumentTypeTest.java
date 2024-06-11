package org.jsoup.nodes;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Node;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("", "", "", "");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("", "", "", "");
        StringBuilder sb = new StringBuilder();
        documentType.outerHtmlHead(sb, 0, null);
        assertEquals("<!DOCTYPE>", sb.toString());
    }

    @Test
    public void testOuterHtmlHeadWithName() {
        DocumentType documentType = new DocumentType("html", "", "", "");
        StringBuilder sb = new StringBuilder();
        documentType.outerHtmlHead(sb, 0, null);
        assertEquals("<!DOCTYPE html>", sb.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "", "");
        StringBuilder sb = new StringBuilder();
        documentType.outerHtmlHead(sb, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">", sb.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemId() {
        DocumentType documentType = new DocumentType("html", "", "http://www.w3.org/TR/html4/strict.dtd", "");
        StringBuilder sb = new StringBuilder();
        documentType.outerHtmlHead(sb, 0, null);
        assertEquals("<!DOCTYPE html \"http://www.w3.org/TR/html4/strict.dtd\">", sb.toString());
    }

}
