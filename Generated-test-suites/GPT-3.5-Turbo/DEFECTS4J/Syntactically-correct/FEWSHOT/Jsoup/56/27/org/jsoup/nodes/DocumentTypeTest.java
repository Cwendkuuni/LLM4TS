package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document.OutputSettings.Syntax;
import org.junit.Test;

public class DocumentTypeTest {
    @Test
    public void testConstructor() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("name", documentType.attr("name"));
        assertEquals("publicId", documentType.attr("publicId"));
        assertEquals("systemId", documentType.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHeadWithHtmlSyntax() throws IOException {
        DocumentType documentType = new DocumentType("name", null, null, "baseUri");
        Appendable accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Syntax.html);
        documentType.outerHtmlHead(accum, 0, outputSettings);
        assertEquals("<!doctype", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithoutPublicIdAndSystemId() throws IOException {
        DocumentType documentType = new DocumentType("name", null, null, "baseUri");
        Appendable accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Syntax.xml);
        documentType.outerHtmlHead(accum, 0, outputSettings);
        assertEquals("<!DOCTYPE name>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdAndSystemId() throws IOException {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        Appendable accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Syntax.xml);
        documentType.outerHtmlHead(accum, 0, outputSettings);
        assertEquals("<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail(){
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        Appendable accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }

    @Test
    public void testHasWithNonBlankAttribute(){
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertTrue(documentType.has("name"));
    }
}