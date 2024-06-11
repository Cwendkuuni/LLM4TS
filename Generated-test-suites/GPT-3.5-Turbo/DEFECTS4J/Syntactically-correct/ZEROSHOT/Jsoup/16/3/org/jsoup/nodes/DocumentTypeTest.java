package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("doctypeName", "publicIdValue", "systemIdValue", "baseUriValue");
    }

    @Test
    public void testNodeName() {
        String nodeName = documentType.nodeName();
        assertEquals("#doctype", nodeName);
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, depth, out);

        String expectedOutput = "<!DOCTYPE html PUBLIC \"publicIdValue\" systemIdValue\">";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlTail(accum, depth, out);

        assertEquals("", accum.toString());
    }
}