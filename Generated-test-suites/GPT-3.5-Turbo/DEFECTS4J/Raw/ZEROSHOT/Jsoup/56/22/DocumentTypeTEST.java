package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setup() {
        documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            documentType.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            documentType.outerHtmlTail(accum, 0, out);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        String expected = "";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testHasWithExistingAttribute() {
        documentType.attr("name", "html");
        boolean hasAttribute = documentType.has("name");
        assertTrue(hasAttribute);
    }

    @Test
    public void testHasWithNonExistingAttribute() {
        boolean hasAttribute = documentType.has("charset");
        assertFalse(hasAttribute);
    }

}