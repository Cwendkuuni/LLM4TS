package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.helper.StringUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
        assertNotNull(documentType.attr("name"));
        assertNotNull(documentType.attr("publicId"));
        assertNotNull(documentType.attr("systemId"));
        assertNotNull(documentType.attr("baseUri"));
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"publicId\" systemId>\n";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyPublicId() {
        DocumentType documentType = new DocumentType("html", "", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html SYSTEM systemId>\n";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptySystemId() {
        DocumentType documentType = new DocumentType("html", "publicId", "", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"publicId\" >\n";
        assertEquals(expected, accum.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testOuterHtmlHeadWithNullAccumulator() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        documentType.outerHtmlHead(null, 0, null);
    }

    @Test(expected = NullPointerException.class)
    public void testOuterHtmlHeadWithNullOutputSettings() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
    }
}