package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType docType = new DocumentType("html5", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.org");
        assertEquals("html5", docType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html5", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.org");
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("html5", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.org");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html5 PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html5", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.org");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, new Document.OutputSettings());
        assertEquals("", accum.toString());
    }

}