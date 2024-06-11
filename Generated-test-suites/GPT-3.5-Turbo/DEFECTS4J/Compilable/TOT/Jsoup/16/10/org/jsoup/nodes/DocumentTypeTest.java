package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    // Test case for constructor
    @Test
    public void testConstructor() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
        assertNotNull(doctype);
        assertEquals("html", doctype.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.attr("systemId"));
    }

    // Test case for nodeName()
    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
        assertEquals("#doctype", doctype.nodeName());
    }

    // Test case for outerHtmlHead()
    @Test
    public void testOuterHtmlHeadNoPublicIdNoSystemId() {
        DocumentType doctype = new DocumentType("html", "", "", "https://www.example.com");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        doctype.outerHtmlHead(accum, depth, out);

        String expectedOutput = "<!DOCTYPE html>";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdNoSystemId() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "", "https://www.example.com");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        doctype.outerHtmlHead(accum, depth, out);

        String expectedOutput = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdAndSystemId() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        doctype.outerHtmlHead(accum, depth, out);

        String expectedOutput = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" http://www.w3.org/TR/html4/strict.dtd\">";
        assertEquals(expectedOutput, accum.toString());
    }

    // Test case for outerHtmlTail()
    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        doctype.outerHtmlTail(accum, depth, out);

        assertEquals("", accum.toString());
    }
}
