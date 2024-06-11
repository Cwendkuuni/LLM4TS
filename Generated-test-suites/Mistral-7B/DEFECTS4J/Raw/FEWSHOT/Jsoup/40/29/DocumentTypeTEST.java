package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "W3C HTML";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        Attributes attributes = new Attributes();
        attributes.put("xml:version", "1.0");
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
        assertEquals(attributes, documentType.attributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyDocumentTypeName() {
        String name = "";
        String publicId = "W3C HTML";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";

        new DocumentType(name, publicId, systemId, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullDocumentTypeName() {
        String name = null;
        String publicId = "W3C HTML";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";

        new DocumentType(name, publicId, systemId, baseUri);
    }

    @Test
    public void testDocumentTypeToString() {
        String name = "html";
        String publicId = "W3C HTML";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("<!DOCTYPE html PUBLIC \"W3C HTML\" \"http://www.w3.org/TR/html4/strict.dtd\">", documentType.toString());
    }

    @Test
    public void testDocumentTypeWithEmptyAttributes() {
        String name = "html";
        String publicId = "W3C HTML";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        Element element = new Element("html");
        element.attributes().put("xml:version", "1.0");

        documentType.outerHtmlHead(new StringBuilder(), 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"W3C HTML\" \"http://www.w3.org/TR/html4/strict.dtd\">", element.html());
    }

    @Test
    public void testDocumentTypeWithAttributes() {
        String name = "html";
        String publicId = "W3C HTML";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("xml:version", "1.0");

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri, attributes);

        Element element = new Element("html");
        element.attributes().putAll(attributes);

        documentType.outerHtmlHead(new StringBuilder(), 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"W3C HTML\" \"http://www.w3.org/TR/html4/strict.dtd\">\n<html xml:version=\"1.0\">", element.html());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPublicId() {
        String name = "html";
        String publicId = "";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";

        new DocumentType(name, publicId, systemId, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPublicId() {
        String name = "html";
        String publicId = null;
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";

        new DocumentType(name, publicId, systemId, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptySystemId() {
        String name = "html";
        String publicId = "W3C HTML";
        String systemId = "";
        String baseUri = "http://example.com";

        new DocumentType(name, publicId, systemId, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSystemId() {
        String name = "html";
        String publicId = "W3C HTML";
        String systemId = null;
        String baseUri = "http://example.com";

        new DocumentType(name, publicId, systemId, baseUri);
    }

    @Test
    public void testDocumentTypeWithNullBaseUri() {
        String name = "html";
        String publicId = "W3C HTML";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = null;

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals(null, documentType.baseUri());
    }
}