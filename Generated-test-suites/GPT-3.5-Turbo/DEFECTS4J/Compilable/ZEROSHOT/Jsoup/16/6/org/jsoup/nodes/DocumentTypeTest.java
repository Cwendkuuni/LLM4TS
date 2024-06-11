package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("doctype", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "https://www.example.com");
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

        String expectedResult = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">";
        assertEquals(expectedResult, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithNoPublicId() {
        documentType = new DocumentType("doctype", null, "http://www.w3.org/TR/html4/loose.dtd", "https://www.example.com");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, depth, out);

        String expectedResult = "<!DOCTYPE html http://www.w3.org/TR/html4/loose.dtd\">";
        assertEquals(expectedResult, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithNoSystemId() {
        documentType = new DocumentType("doctype", "-//W3C//DTD HTML 4.01 Transitional//EN", null, "https://www.example.com");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, depth, out);

        String expectedResult = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">";
        assertEquals(expectedResult, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlTail(accum, depth, out);

        String expectedResult = "";
        assertEquals(expectedResult, accum.toString());
    }
}