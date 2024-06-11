package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document.OutputSettings.*;
import org.junit.Before;
import org.junit.Test;

public class DocumentTypeTest {
    private DocumentType documentType;

    @Before
    public void setup() {
        documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/TR/html4/strict.dtd");
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");

        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlHead(actual, 0, new Document.OutputSettings(Syntax.html));

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testOuterHtmlHead_noPublicId() {
        documentType.attr(DocumentType.PUBLIC_ID, "");

        StringBuilder expected = new StringBuilder();
        expected.append("<!doctype html \"http://www.w3.org/TR/html4/strict.dtd\">");

        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlHead(actual, 0, new Document.OutputSettings(Syntax.html));

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testOuterHtmlHead_lowerCaseDoctype() {
        documentType.attr(DocumentType.PUBLIC_ID, "");

        StringBuilder expected = new StringBuilder();
        expected.append("<!doctype html \"http://www.w3.org/TR/html4/strict.dtd\">");

        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlHead(actual, 0, new Document.OutputSettings(Syntax.html));

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testOuterHtmlHead_noSystemId() {
        documentType.attr(DocumentType.SYSTEM_ID, "");

        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">");

        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlHead(actual, 0, new Document.OutputSettings(Syntax.html));

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testOuterHtmlHead_noPublicIdAndSystemId() {
        documentType.attr(DocumentType.PUBLIC_ID, "");
        documentType.attr(DocumentType.SYSTEM_ID, "");

        StringBuilder expected = new StringBuilder();
        expected.append("<!doctype html>");

        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlHead(actual, 0, new Document.OutputSettings(Syntax.html));

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder expected = new StringBuilder();

        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlTail(actual, 0, new Document.OutputSettings(Syntax.html));

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testHas() {
        assertTrue(documentType.has(DocumentType.NAME));
    }
}