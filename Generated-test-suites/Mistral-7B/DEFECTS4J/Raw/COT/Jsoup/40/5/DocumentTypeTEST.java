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
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseUri");
        Assert.assertEquals("#doctype", documentType.nodeName());
        Assert.assertEquals("html", documentType.attr("name"));
        Assert.assertEquals("publicID", documentType.attr("publicId"));
        Assert.assertEquals("systemID", documentType.attr("systemId"));
        Assert.assertEquals("baseUri", documentType.baseUri());
    }

    @Test
    public void testEmptyDocumentTypeConstructor() {
        DocumentType documentType = new DocumentType("html");
        Assert.assertEquals("#doctype", documentType.nodeName());
        Assert.assertNull(documentType.attr("name"));
        Assert.assertNull(documentType.attr("publicId"));
        Assert.assertNull(documentType.attr("systemId"));
        Assert.assertNull(documentType.baseUri());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseUri");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"publicID\" \"systemID\">";
        String actual = accum.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testOuterHtmlHeadWithEmptyPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("html");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html>";
        String actual = accum.toString();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullNameInConstructor() {
        new DocumentType(null, "publicID", "systemID", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPublicId() {
        new DocumentType("html", null, "systemID", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSystemId() {
        new DocumentType("html", "publicID", null, "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        new DocumentType("html", "publicID", "systemID", null);
    }

    @Test
    public void testSetNameAttribute() {
        DocumentType documentType = new DocumentType("html");
        documentType.attr("name", "newName");
        assertEquals("newName", documentType.attr("name"));
    }

    @Test
    public void testSetPublicIdAttribute() {
        DocumentType documentType = new DocumentType("html");
        documentType.attr("publicId", "newPublicId");
        assertEquals("newPublicId", documentType.attr("publicId"));
    }

    @Test
    public void testSetSystemIdAttribute() {
        DocumentType documentType = new DocumentType("html");
        documentType.attr("systemId", "newSystemId");
        assertEquals("newSystemId", documentType.attr("systemId"));
    }

    @Test
    public void testSetBaseUriAttribute() {
        DocumentType documentType = new DocumentType("html");
        documentType.baseUri(new Element("html").baseUri());
        assertEquals(new Element("html").baseUri(), documentType.baseUri());
    }
}