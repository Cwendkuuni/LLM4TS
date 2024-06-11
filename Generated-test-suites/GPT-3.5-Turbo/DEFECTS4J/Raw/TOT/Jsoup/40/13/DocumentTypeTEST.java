package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://example.com/");
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">";
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, 0, out);

        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlTail(accum, 0, out);

        assertTrue(accum.toString().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyNameConstructor() {
        new DocumentType("", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://example.com/");
    }

    @Test
    public void testBlankPublicId() {
        documentType = new DocumentType("html", "", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://example.com/");

        assertEquals("", documentType.attr("publicId"));
    }

    @Test
    public void testBlankSystemId() {
        documentType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "", "http://example.com/");

        assertEquals("", documentType.attr("systemId"));
    }

    @Test
    public void testBlankNameInOuterHtmlHead() {
        documentType = new DocumentType("", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://example.com/");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, 0, out);

        assertFalse(accum.toString().contains("null"));
    }
}