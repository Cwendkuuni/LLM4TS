package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class DocumentTypeTest {
    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("doctype", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://www.example.com");
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHeadHtmlSyntax() throws IOException {
        documentType.attr("publicId", "");
        documentType.attr("systemId", "");
        Document.OutputSettings outputSettings = new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html);
        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlHead(stringBuilder, 0, outputSettings);
        assertEquals("<!doctype", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlHeadOtherSyntaxNoPublicIdNoSystemId() throws IOException {
        documentType.attr("publicId", "");
        documentType.attr("systemId", "");
        Document.OutputSettings outputSettings = new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.xml);
        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlHead(stringBuilder, 0, outputSettings);
        assertEquals("<!DOCTYPE doctype>", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlHeadOtherSyntaxWithPublicId() throws IOException {
        Document.OutputSettings outputSettings = new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.xml);
        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlHead(stringBuilder, 0, outputSettings);
        assertEquals("<!DOCTYPE doctype PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlHeadOtherSyntaxWithSystemId() throws IOException {
        Document.OutputSettings outputSettings = new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.xml);
        documentType.attr("publicId", "");
        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlHead(stringBuilder, 0, outputSettings);
        assertEquals("<!DOCTYPE doctype \"http://www.example.com\">", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlTail(stringBuilder, 0, null);
        assertEquals("", stringBuilder.toString());
    }

    @Test
    public void testHasTrue() {
        assertTrue(documentType.has("name"));
        assertTrue(documentType.has("publicId"));
        assertTrue(documentType.has("systemId"));
    }

    @Test
    public void testHasFalse() {
        assertFalse(documentType.has("invalidAttribute"));
    }
}