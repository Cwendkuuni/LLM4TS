package org.jsoup.nodes;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
    }

    @Test
    public void testNodeName() {
        String expected = "#doctype";
        assertEquals(expected, documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        String expected = "<!DOCTYPE doctype PUBLIC \"publicId\" \"systemId\">";
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, depth, out);
        String actual = accum.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testOuterHtmlTail() {
        String expected = "";
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlTail(accum, depth, out);
        String actual = accum.toString();
        assertEquals(expected, actual);
    }
}