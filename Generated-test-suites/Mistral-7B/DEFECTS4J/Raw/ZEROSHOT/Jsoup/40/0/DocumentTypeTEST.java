package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;
import org.junit.Test;

/**
 * Test class for DocumentType class.
 */
public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"",
                "http://www.w3.org/TR/html4/loose.dtd", document.baseUri());
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals("HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", documentType.attr("systemId"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyName() {
        Document document = new Document();
        new DocumentType(null, "", "", document.baseUri());
    }

    @Test
    public void testOuterHtmlHead() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"",
                "http://www.w3.org/TR/html4/loose.dtd", document.baseUri());
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" >", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdAndSystemIdEmpty() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", null, null, document.baseUri());
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdAndSystemId() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"",
                "http://www.w3.org/TR/html4/loose.dtd", document.baseUri());
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" >", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"",
                "http://www.w3.org/TR/html4/loose.dtd", document.baseUri());
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", null, null, document.baseUri());
        Element element = document.createElement("html");
        element.setDocumentType(documentType);
        Node child = document.createTextNode("Test");
        element.appendChild(child);
        DocumentType documentTypeFromElement = (DocumentType) element.getFirstChild();
        assertEquals("#doctype", documentTypeFromElement.nodeName());
        assertEquals("html", documentTypeFromElement.attr("name"));
        assertEquals(null, documentTypeFromElement.attr("publicId"));
        assertEquals(null, documentTypeFromElement.attr("systemId"));
    }
}