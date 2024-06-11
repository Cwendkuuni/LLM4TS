package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        String name = "#doctype";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        Assert.assertEquals("#doctype", documentType.nodeName());
        Assert.assertEquals(name, documentType.attr("name"));
        Assert.assertEquals(publicId, documentType.attr("publicId"));
        Assert.assertEquals(systemId, documentType.attr("systemId"));
        Assert.assertEquals(baseUri, documentType.attributes().baseUri());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("#doctype", "publicId", "systemId", "baseUri");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"publicId\" systemId>\n";
       Assert.assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdOnly() {
        DocumentType documentType = new DocumentType("#doctype", "publicId", "", "baseUri");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"publicId\" \">\n";
        Assert.assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemIdOnly() {
        DocumentType documentType = new DocumentType("#doctype", "", "systemId", "baseUri");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html SYSTEM \"systemId\">\n";
        Assert.assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithBothIdAndSystem() {
        DocumentType documentType = new DocumentType("#doctype", "publicId", "systemId", "baseUri");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"publicId\" systemId>\n";
        Assert.assertEquals(expected, accum.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOuterHtmlHeadWithNullAccumulator() {
        DocumentType documentType = new DocumentType("#doctype", "publicId", "systemId", "baseUri");

        documentType.outerHtmlHead(null, 0, null);
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("#doctype", "publicId", "systemId", "baseUri");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);

        Assert.assertEquals("", accum.toString());
    }
}