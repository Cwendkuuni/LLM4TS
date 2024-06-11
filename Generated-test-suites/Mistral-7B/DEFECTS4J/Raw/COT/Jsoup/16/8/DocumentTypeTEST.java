package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        String name = "html";
        String publicId = "PUBLIC";
        String systemId = "systemId";
        String baseUri = "baseUri";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        Assert.assertEquals("#doctype", documentType.nodeName());
        Assert.assertEquals(name, documentType.attr("name"));
        Assert.assertEquals(publicId, documentType.attr("publicId"));
        Assert.assertEquals(systemId, documentType.attr("systemId"));
        Assert.assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "PUBLIC", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html";
        if (!StringUtil.isBlank(documentType.attr("publicId"))) {
            expected += " PUBLIC \"";
            expected += documentType.attr("publicId");
            expected += "\"";
        }
        if (!StringUtil.isBlank(documentType.attr("systemId"))) {
            expected += ' ';
            expected += documentType.attr("systemId");
            expected += "\">";
        } else {
            expected += ">";
        }

        Assert.assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "PUBLIC", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);

        Assert.assertEquals("", accum.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOuterHtmlHeadNullAccumulator() {
        DocumentType documentType = new DocumentType("html", "PUBLIC", "systemId", "baseUri");
        documentType.outerHtmlHead(null, 0, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOuterHtmlHeadNullDocument() {
        DocumentType documentType = new DocumentType("html", "PUBLIC", "systemId", "baseUri");
        documentType.outerHtmlHead(new StringBuilder(), -1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOuterHtmlHeadNegativeDepth() {
        DocumentType documentType = new DocumentType("html", "PUBLIC", "systemId", "baseUri");
        documentType.outerHtmlHead(new StringBuilder(), -1, null);
    }

    @Test
    public void testCloneNode() {
        DocumentType originalDocumentType = new DocumentType("html", "PUBLIC", "systemId", "baseUri");
        DocumentType clonedDocumentType = (DocumentType) originalDocumentType.cloneNode(true);

        Assert.assertEquals(originalDocumentType.nodeName(), clonedDocumentType.nodeName());
        Assert.assertEquals(originalDocumentType.attr("name"), clonedDocumentType.attr("name"));
        Assert.assertEquals(originalDocumentType.attr("publicId"), clonedDocumentType.attr("publicId"));
        Assert.assertEquals(originalDocumentType.attr("systemId"), clonedDocumentType.attr("systemId"));
        Assert.assertEquals(originalDocumentType.baseUri(), clonedDocumentType.baseUri());
    }

    @Test
    public void testToString() {
        DocumentType documentType = new DocumentType("html", "PUBLIC", "systemId", "baseUri");
        String expected = "<html xmlns=\"http://www.w3.org/1999/xhtml\"><![ATTLIST html xml:id CDATA #IMPLIED]\n<![ATTLIST html xml:lang CDATA #IMPLIED]\n<![ATTLIST html dir CDATA #IMPLIED]\n<![ATTLIST html xml:base CDATA #IMPLIED]\n<![IGNORE[><![DOCTYPE html\nPUBLIC \"PUBLIC\" systemId\"]]>\n<html>";
        String actual = documentType.toString();

        Assert.assertEquals(expected, actual);
    }
}