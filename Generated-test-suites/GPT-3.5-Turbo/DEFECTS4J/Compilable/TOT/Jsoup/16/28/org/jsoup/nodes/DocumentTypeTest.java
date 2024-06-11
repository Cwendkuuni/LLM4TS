package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.0 Transitional//EN", "http://www.w3.org/TR/REC-html40/loose.dtd", "http://www.example.com");

        assertEquals("html", doctype.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.0 Transitional//EN", doctype.attr("publicId"));
        assertEquals("http://www.w3.org/TR/REC-html40/loose.dtd", doctype.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.0 Transitional//EN", "http://www.w3.org/TR/REC-html40/loose.dtd", "http://www.example.com");

        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.0 Transitional//EN", "http://www.w3.org/TR/REC-html40/loose.dtd", "http://www.example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        StringBuilder expectedResult = new StringBuilder();
        expectedResult.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\" \"http://www.w3.org/TR/REC-html40/loose.dtd\">");

        StringBuilder actualResult = new StringBuilder();
        doctype.outerHtmlHead(actualResult, 0, outputSettings);

        assertEquals(expectedResult.toString(), actualResult.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.0 Transitional//EN", "http://www.w3.org/TR/REC-html40/loose.dtd", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        doctype.outerHtmlTail(accum, 0, outputSettings);

        assertEquals("", accum.toString());
    }
}