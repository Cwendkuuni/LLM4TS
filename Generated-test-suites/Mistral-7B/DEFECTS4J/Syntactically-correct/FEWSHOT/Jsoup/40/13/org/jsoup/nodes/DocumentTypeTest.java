package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.helper.StringUtil;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "public-id", "system-id", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("public-id", docType.attr("publicId"));
        assertEquals("system-id", docType.attr("systemId"));
        assertNull(docType.children());
    }

    @Test
    public void testDocumentTypeCreationWithNullBaseUri() {
        DocumentType docType = new DocumentType("html", "public-id", "system-id", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("public-id", docType.attr("publicId"));
        assertEquals("system-id", docType.attr("systemId"));
        assertNull(docType.children());
    }

    @Test
    public void testDocumentTypeCreationWithEmptyPublicIdAndSystemId() {
        DocumentType docType = new DocumentType("html", "", "", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.children());
    }

    @Test
    public void testDocumentTypeCreationWithEmptyName() {
        DocumentType docType = new DocumentType("", "public-id", "system-id", null);
        assertEquals("#doctype", docType.nodeName());
        assertNull(docType.attr("name"));
        assertEquals("public-id", docType.attr("publicId"));
        assertEquals("system-id", docType.attr("systemId"));
        assertNull(docType.children());
    }

    @Test
    public void testDocumentTypeToString() {
        DocumentType docType = new DocumentType("html", "public-id", "system-id", null);
        StringBuilder sb = new StringBuilder();
        docType.outerHtmlHead(sb, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"public-id\" \"system-id\">", sb.toString());
    }

    @Test
    public void testDocumentTypeToStringWithEmptyPublicIdAndSystemId() {
        DocumentType docType = new DocumentType("html", "", "", null);
        StringBuilder sb = new StringBuilder();
        docType.outerHtmlHead(sb, 0, null);
        assertEquals("<!DOCTYPE html>", sb.toString());
    }

    @Test
    public void testDocumentTypeToStringWithEmptyName() {
        DocumentType docType = new DocumentType("", "public-id", "system-id", null);
        StringBuilder sb = new StringBuilder();
        docType.outerHtmlHead(sb, 0, null);
        assertEquals("<!DOCTYPE", sb.toString());
    }

    @Test
    public void testDocumentTypeToStringWithAttributes() {
        DocumentType docType = new DocumentType("html", "public-id", "system-id", null);
        docType.attr("foo", "bar");
        StringBuilder sb = new StringBuilder();
        docType.outerHtmlHead(sb, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"public-id\" \"system-id\" foo=\"bar\">", sb.toString());
    }

    @Test
    public void testDocumentTypeToStringWithBlankPublicIdAndSystemId() {
        DocumentType docType = new DocumentType("html", "", "", null);
        StringBuilder sb = new StringBuilder();
        docType.outerHtmlHead(sb, 0, null);
        assertEquals("<!DOCTYPE html>", sb.toString());
    }

    @Test
    public void testDocumentTypeToStringWithNullAttributes() {
        DocumentType docType = new DocumentType("html", "public-id", "system-id", null);
        docType.outerHtmlHead(null, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"public-id\" \"system-id\">", docType.outerHtml());
    }

    @Test
    public void testDocumentTypeToStringWithNullBaseUri() {
        DocumentType docType = new DocumentType("html", "public-id", "system-id", null);
        StringBuilder sb = new StringBuilder();
        docType.outerHtmlHead(sb, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"public-id\" \"system-id\">", sb.toString());
    }

    @Test
    public void testDocumentTypeToStringWithNullAttributesAndNullBaseUri() {
        DocumentType docType = new DocumentType("html", "public-id", "system-id", null);
        docType.outerHtmlHead(null, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"public-id\" \"system-id\">", docType.outerHtml());
    }

    @Test
    public void testDocumentTypeToStringWithNullAttributesAndBaseUri() {
        DocumentType docType = new DocumentType("html", "public-id", "system-id", "base-uri");
        StringBuilder sb = new StringBuilder();
        docType.outerHtmlHead(sb, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"public-id\" \"system-id\">", sb.toString());
    }

    @Test
    public void testDocumentTypeToStringWithBaseUri() {
        DocumentType docType = new DocumentType("html", "public-id", "system-id", "base-uri");
        StringBuilder sb = new StringBuilder();
        docType.outerHtmlHead(sb, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"public-id\" \"system-id\">", sb.toString());
    }

    @Test
    public void testDocumentTypeToStringWithChildren() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "public-id", "system-id", null);
        Element child = new Element("html");
        doc.appendChild(docType);
        doc.appendChild(child);
        StringBuilder sb = new StringBuilder();
        docType.outerHtmlHead(sb, 0, null);
        sb.append(docType.outerHtmlTail(sb, 0, null));
        sb.append(child.outerHtml());
        assertEquals("<!DOCTYPE html PUBLIC \"public-id\" \"system-id\">\n<html></html>", sb.toString());
    }

    @Test
    public void testDocumentTypeToStringWithChildrenAndBaseUri() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "public-id", "system-id", "base-uri");
        Element child = new Element("html");
        doc.appendChild(docType);
        doc.appendChild(child);
        StringBuilder sb = new StringBuilder();
        docType.outerHtmlHead(sb, 0, null);
        sb.append(docType.outerHtmlTail(sb, 0, null));
        sb.append(child.outerHtml());
        assertEquals("<!DOCTYPE html PUBLIC \"public-id\" \"system-id\">\n<html></html>", sb.toString());
    }
}