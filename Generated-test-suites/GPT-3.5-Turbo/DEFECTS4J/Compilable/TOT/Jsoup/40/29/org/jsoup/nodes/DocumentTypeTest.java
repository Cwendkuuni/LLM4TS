package org.jsoup.nodes;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setup() {
        documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
    }

    @Test
    public void testConstructor() {
        assertNotNull(documentType);
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, depth, out);

        String expectedOutput = "<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlHead_NoAttributes() {
        documentType = new DocumentType("", "", "", "");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, depth, out);

        String expectedOutput = "<!DOCTYPE>";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlTail(accum, depth, out);

        String expectedOutput = "";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlTail_NoAttributes() {
        documentType = new DocumentType("", "", "", "");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlTail(accum, depth, out);

        String expectedOutput = "";
        assertEquals(expectedOutput, accum.toString());
    }
}