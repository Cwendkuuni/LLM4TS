package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.StringUtil;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        Assert.assertEquals("#doctype", documentType.nodeName());
        Assert.assertEquals("html", documentType.attr("name"));
        Assert.assertEquals("publicId", documentType.attr("publicId"));
        Assert.assertEquals("systemId", documentType.attr("systemId"));
        Assert.assertEquals("baseUri", documentType.baseUri());
    }

    @Test
    public void testEmptyConstructor() {
        DocumentType documentType = new DocumentType();
        Assert.assertEquals("#doctype", documentType.nodeName());
        Assert.assertNull(documentType.attr("name"));
        Assert.assertNull(documentType.attr("publicId"));
        Assert.assertNull(documentType.attr("systemId"));
        Assert.assertNull(documentType.baseUri());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyAttributes() {
        DocumentType documentType = new DocumentType("html");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        String expected = "<!DOCTYPE html>";
        assertEquals(expected, accum.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateEmptyName() {
        new DocumentType("", "publicId", "systemId", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateNullPublicId() {
        new DocumentType("html", null, "systemId", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateNullSystemId() {
        new DocumentType("html", "publicId", null, "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateNullBaseUri() {
        new DocumentType("html", "publicId", "systemId", null);
    }
}