package org.jsoup.nodes;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "");
    }

    @Test
    public void testConstructor() {
        assertEquals("html", documentType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, out);

        String expectedOutput = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" http://www.w3.org/TR/html4/strict.dtd>";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlHead_NoPublicId() {
        documentType = new DocumentType("html", "", "http://www.w3.org/TR/html4/strict.dtd", "");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, out);

        String expectedOutput = "<!DOCTYPE html http://www.w3.org/TR/html4/strict.dtd>";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlHead_NoSystemId() {
        documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "", "");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, out);

        String expectedOutput = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlTail(accum, 0, out);

        assertEquals("", accum.toString());
    }
}