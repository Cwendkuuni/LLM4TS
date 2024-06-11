package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;

/**
 * Test class for DocumentType.
 */
public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "$-//W3C//DTD XHTML 1.0 Strict//EN";
        String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
        String baseUri = "https://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeCreationWithEmptyName() {
        String name = "";
        String publicId = "test";
        String systemId = "test";
        String baseUri = "test";

        new DocumentType(name, publicId, systemId, baseUri);
    }

    @Test
    public void testDocumentTypeOuterHtml() {
        DocumentType documentType = new DocumentType("html", "", "", "");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        documentType.outerHtmlTail(accum, 0, null);

        String expected = "<!DOCTYPE html>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlWithPublicId() {
        DocumentType documentType = new DocumentType("html", "test:publicId", "", "");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        documentType.outerHtmlTail(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"test:publicId\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlWithSystemId() {
        DocumentType documentType = new DocumentType("html", "", "test:systemId", "");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        documentType.outerHtmlTail(accum, 0, null);

        String expected = "<!DOCTYPE html SYSTEM \"test:systemId\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlWithNamePublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("html", "test:publicId", "test:systemId", "");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        documentType.outerHtmlTail(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"test:publicId\" SYSTEM \"test:systemId\">";
        assertEquals(expected, accum.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testDocumentTypeOuterHtmlWithNullAccumulator() {
        DocumentType documentType = new DocumentType("html", "", "", "");

        documentType.outerHtmlHead(null, 0, null);
    }

    @Test(expected = NullPointerException.class)
    public void testDocumentTypeOuterHtmlWithNullOutputSettings() {
        DocumentType documentType = new DocumentType("html", "", "", "");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        documentType.outerHtmlTail(accum, 0, null);
        documentType.outerHtmlHead(accum, 0, null);

        assertThrows(NullPointerException.class, () -> documentType.outerHtmlTail(null, 0, null));
    }

    @Test
    public void testDocumentTypeEquals() {
        DocumentType documentType1 = new DocumentType("html", "", "", "");
        DocumentType documentType2 = new DocumentType("html", "", "", "");

        assertEquals(documentType1, documentType2);
    }

    @Test
    public void testDocumentTypeNotEquals() {
        DocumentType documentType1 = new DocumentType("html", "", "", "");
        Element element = new Element("p");

        assertNotEquals(documentType1, element);
    }

    @Test
    public void testDocumentTypeHashCode() {
        DocumentType documentType1 = new DocumentType("html", "", "", "");
        DocumentType documentType2 = new DocumentType("html", "", "", "");

        assertEquals(documentType1.hashCode(), documentType2.hashCode());
    }

    @Test
    public void testDocumentTypeToString() {
        DocumentType documentType = new DocumentType("html", "", "", "");

        String expected = "<html></html>";
        String actual = documentType.toString();

        assertEquals(expected, actual);
    }
}