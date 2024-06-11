package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("test", "public", "system", "baseUri");
    }

    @Test
    public void testNodeName() {
        String expected = "#doctype";
        String actual = documentType.nodeName();
        assertEquals(expected, actual);
    }

    @Test
    public void testOuterHtmlHead() throws Exception {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, depth, out);

        String expected = "<!DOCTYPE test PUBLIC \"public\" \"system\">";
        String actual = accum.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlTail(accum, depth, out);

        String expected = "";
        String actual = accum.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testHasWithBlankAttribute() {
        boolean expected = false;
        boolean actual = documentType.has("");
        assertEquals(expected, actual);
    }

    @Test
    public void testHasWithNonBlankAttribute() {
        boolean expected = true;
        boolean actual = documentType.has("name");
        assertEquals(expected, actual);
    }
}