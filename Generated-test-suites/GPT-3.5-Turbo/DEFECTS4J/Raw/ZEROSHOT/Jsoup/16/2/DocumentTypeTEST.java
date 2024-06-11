package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    private DocumentType documentType;

    @Before
    public void setUp() {
        String name = "doctype";
        String publicId = "-//W3C//DTD XHTML 1.0 Transitional//EN";
        String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
        String baseUri = "http://www.example.com";
        documentType = new DocumentType(name, publicId, systemId, baseUri);
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        documentType.attr("publicId", "-//W3C//DTD HTML 3.2//EN");

        documentType.outerHtmlHead(accum, depth, outputSettings);

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 3.2//EN\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemId() {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        documentType.attr("systemId", "http://www.example.com");

        documentType.outerHtmlHead(accum, depth, outputSettings);

        assertEquals("<!DOCTYPE html http://www.example.com\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        documentType.outerHtmlTail(accum, depth, outputSettings);

        assertEquals("", accum.toString());
    }
}