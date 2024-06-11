package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "HTML";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://www.example.com";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr("name"));
        assertEquals(publicId, docType.attr("publicId"));
        assertEquals(systemId, docType.attr("systemId"));
        assertEquals(baseUri, docType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithEmptyAttributes() {
        String name = "html";
        DocumentType docType = new DocumentType(name, null, null, null);

        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.baseUri());
    }

    @Test
    public void testDocumentTypeToString() {
        String name = "html";
        String publicId = "HTML";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://www.example.com";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML\" http://www.w3.org/TR/html4/strict.dtd>", docType.toString());
    }

    @Test
    public void testDocumentTypeToStringWithEmptyAttributes() {
        String name = "html";
        DocumentType docType = new DocumentType(name, null, null, null);

        assertEquals("<!DOCTYPE html>", docType.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        String name = "html";
        String publicId = "HTML";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://www.example.com";

        StringBuilder accum = new StringBuilder();
        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML\" http://www.w3.org/TR/html4/strict.dtd>", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptyAttributes() {
        String name = "html";
        StringBuilder accum = new StringBuilder();
        DocumentType docType = new DocumentType(name, null, null, null);

        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", null, null, null);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, null);

        assertEquals("", accum.toString());
    }

    @Test
    public void testDocumentTypeAttrMethods() {
        String name = "html";
        String publicId = "HTML";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://www.example.com";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals(name, docType.attr("name"));
        assertEquals(publicId, docType.attr("publicId"));
        assertEquals(systemId, docType.attr("systemId"));
        assertEquals(baseUri, docType.baseUri());

        docType.attr("newAttr", "newValue");

        assertEquals("newValue", docType.attr("newAttr"));

        docType.removeAttr("name");

        assertEquals(null, docType.attr("name"));
    }

    @Test
    public void testDocumentTypeRemoveAttrWithEmptyAttributeName() {
        String name = "html";
        DocumentType docType = new DocumentType(name, null, null, null);

        docType.removeAttr("foo");

        assertNull(docType.attr("foo"));
    }
}