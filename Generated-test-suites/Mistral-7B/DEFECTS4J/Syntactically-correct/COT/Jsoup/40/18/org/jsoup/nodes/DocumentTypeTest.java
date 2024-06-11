package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.helper.StringUtil;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
        Assert.assertEquals("#doctype", documentType.nodeName());
        Assert.assertEquals("html", documentType.attr("name"));
        Assert.assertEquals("publicId", documentType.attr("publicId"));
        Assert.assertEquals("systemId", documentType.attr("systemId"));
    }

    @Test
    public void testEmptyDocumentTypeConstructor() {
        DocumentType documentType = new DocumentType("html");
        Assert.assertEquals("#doctype", documentType.nodeName());
        Assert.assertNull(documentType.attr("name"));
        Assert.assertNull(documentType.attr("publicId"));
        Assert.assertNull(documentType.attr("systemId"));
    }

    @Test
    public void testEmptyOuterHtmlHead() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html");
        document.appendChild(documentType);

        StringBuilder accum = new StringBuilder();
        document.body().outerHtml(accum);

        String expected = "<!DOCTYPE html>";
        String actual = accum.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testValidOuterHtmlHead() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
        document.appendChild(documentType);

        StringBuilder accum = new StringBuilder();
        document.body().outerHtml(accum);

        String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">";
        String actual = accum.toString();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullNameInDocumentTypeConstructor() {
        new DocumentType(null, "publicId", "systemId", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlankNameInDocumentTypeConstructor() {
        new DocumentType("", "publicId", "systemId", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPublicIdInDocumentTypeConstructor() {
        new DocumentType("name", null, "systemId", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlankPublicIdInDocumentTypeConstructor() {
        new DocumentType("name", "", "systemId", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSystemIdInDocumentTypeConstructor() {
        new DocumentType("name", "publicId", null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlankSystemIdInDocumentTypeConstructor() {
        new DocumentType("name", "publicId", "", null);
    }
}