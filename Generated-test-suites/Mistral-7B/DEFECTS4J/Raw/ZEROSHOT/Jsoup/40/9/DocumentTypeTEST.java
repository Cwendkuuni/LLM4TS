package org.jsoup.nodes.test;

import org.jsoup.Helper$;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertNotNull(documentType);
        assertEquals(name, documentType.nodeName());
        assertNotNull(documentType.attr("name"));
        assertNotNull(documentType.attr("publicId"));
        assertNotNull(documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeCreationWithEmptyName() {
        String name = "";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";

        new DocumentType(name, publicId, systemId, baseUri);
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        Document document = Jsoup.parse("<html><head><!DOCTYPE html><title>Test</title></head><body></body></html>");
        Element doctype = document.selectFirst("!DOCTYPE");

        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlHead(accum, 0, Helper$.OUT_STRING);

        String expected = "<!DOCTYPE html>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithPublicId() {
        Document document = Jsoup.parse("<html><head><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"><title>Test</title></head><body></body></html>");
        Element doctype = document.selectFirst("!DOCTYPE");

        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlHead(accum, 0, Helper$.OUT_STRING);

        String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithSystemId() {
        Document document = Jsoup.parse("<html><head><!DOCTYPE html SYSTEM \"about:legacy-compat\"><title>Test</title></head><body></body></html>");
        Element doctype = document.selectFirst("!DOCTYPE");

        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlHead(accum, 0, Helper$.OUT_STRING);

        String expected = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithNamePublicIdAndSystemId() {
        Document document = Jsoup.parse("<html><head><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\" SYSTEM \"about:legacy-compat\"><title>Test</title></head><body></body></html>");
        Element doctype = document.selectFirst("!DOCTYPE");

        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlHead(accum, 0, Helper$.OUT_STRING);

        String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\" SYSTEM \"about:legacy-compat\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        Document document = Jsoup.parse("<html><head><!DOCTYPE html><title>Test</title></head><body></body></html>");
        Element doctype = document.selectFirst("!DOCTYPE");

        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlTail(accum, 0, Helper$.OUT_STRING);

        assertTrue(accum.toString().isEmpty());
    }

    @Test
    public void testDocumentTypeAttr() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertNull(documentType.attr("nonexistent"));
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
    }
}