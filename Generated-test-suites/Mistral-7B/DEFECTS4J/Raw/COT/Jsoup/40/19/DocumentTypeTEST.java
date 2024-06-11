package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

/**
 * Test class for DocumentType.
 */
public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstruction() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithNameOnly() {
        DocumentType documentType = new DocumentType("html", null, null, null);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithNamePublicId() {
        DocumentType documentType = new DocumentType("html", "publicId", null, null);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithNameSystemId() {
        DocumentType documentType = new DocumentType("html", null, "systemId", null);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html SYSTEM \"systemId\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithNamePublicIdSystemId() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">";
        assertEquals(expected, accum.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeNullName() {
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";

        new DocumentType(null, publicId, systemId, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeNullPublicId() {
        String name = "html";
        String systemId = "systemId";
        String baseUri = "baseUri";

        new DocumentType(name, null, systemId, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeNullSystemId() {
        String name = "html";
        String publicId = "publicId";
        String baseUri = "baseUri";

        new DocumentType(name, publicId, null, baseUri);
    }

    @Test
    public void testDocumentTypeBaseUri() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testDocumentTypeWithEmptyStringAttributes() {
        DocumentType documentType = new DocumentType("html", "", "", "");

        assertEquals("#doctype", documentType.nodeName());
        assertEquals("", documentType.attr("name"));
        assertEquals("", documentType.attr("publicId"));
        assertEquals("", documentType.attr("systemId"));
        assertNull(documentType.baseUri());
    }

    @Test
    public void testDocumentTypeClone() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";

        DocumentType originalDocumentType = new DocumentType(name, publicId, systemId, baseUri);
        DocumentType clonedDocumentType = (DocumentType) originalDocumentType.cloneNode(true);

        assertEquals(originalDocumentType.nodeName(), clonedDocumentType.nodeName());
        assertEquals(originalDocumentType.attr("name"), clonedDocumentType.attr("name"));
        assertEquals(originalDocumentType.attr("publicId"), clonedDocumentType.attr("publicId"));
        assertEquals(originalDocumentType.attr("systemId"), clonedDocumentType.attr("systemId"));
        assertEquals(originalDocumentType.baseUri(), clonedDocumentType.baseUri());
    }

    @Test
    public void testDocumentTypeToString() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);

        String expected = "<html>";
        Node node = new Element("html");
        node.appendChild(documentType);

        String actual = node.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testDocumentTypeToStringWithEmptyStringAttributes() {
        DocumentType documentType = new DocumentType("html", "", "", "");

        String expected = "<html>";
        Node node = new Element("html");
        node.appendChild(documentType);

        String actual = node.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testDocumentTypeToStringWithNameOnly() {
        DocumentType documentType = new DocumentType("html", null, null, null);

        String expected = "<html>";
        Node node = new Element("html");
        node.appendChild(documentType);

        String actual = node.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testDocumentTypeToStringWithNamePublicId() {
        DocumentType documentType = new DocumentType("html", "publicId", null, null);

        String expected = "<!DOCTYPE html PUBLIC \"\" \"\">";
        Node node = new Element("html");
        node.appendChild(documentType);

        String actual = node.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testDocumentTypeToStringWithNameSystemId() {
        DocumentType documentType = new DocumentType("html", null, "systemId", null);

        String expected = "<!DOCTYPE html SYSTEM \"systemId\">";
        Node node = new Element("html");
        node.appendChild(documentType);

        String actual = node.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testDocumentTypeToStringWithNamePublicIdSystemId() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);

        String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">";
        Node node = new Element("html");
        node.appendChild(documentType);

        String actual = node.toString();

        assertEquals(expected, actual);
    }
}