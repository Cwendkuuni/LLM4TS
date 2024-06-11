package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.0 Transitional//EN", "http://www.w3.org/TR/REC-html40/loose.dtd", "http://www.example.com");

        assertEquals("html", documentType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.0 Transitional//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/REC-html40/loose.dtd", documentType.attr("systemId"));
        assertEquals("http://www.example.com", documentType.baseUri());
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.0 Transitional//EN", "http://www.w3.org/TR/REC-html40/loose.dtd", "http://www.example.com");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\" http://www.w3.org/TR/REC-html40/loose.dtd>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdOnly() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.0 Transitional//EN", null, "http://www.example.com");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemIdOnly() {
        DocumentType documentType = new DocumentType("html", null, "http://www.w3.org/TR/REC-html40/loose.dtd", "http://www.example.com");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());

        assertEquals("<!DOCTYPE html http://www.w3.org/TR/REC-html40/loose.dtd>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithNoPublicIdAndNoSystemId() {
        DocumentType documentType = new DocumentType("html", null, null, "http://www.example.com");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());

        assertEquals("<!DOCTYPE html>", accum.toString());
    }
}