package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTypeTest {
    // Test case 1: constructor
    @Test
    public void testDocumentTypeConstruction() {
        String name = "html";
        String publicId = "PUBLIC -1";
        String systemId = "-//W3C//DTD XHTML 1.0 Strict//EN";
        String baseUri = "http://www.example.com/";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
        assertNotNull(documentType.outerHtml());
    }

    // Test case 2: nodeName()
    @Test
    public void testDocumentTypeNodeName() {
        DocumentType documentType = new DocumentType("html", "", "", "");
        assertEquals("#doctype", documentType.nodeName());
    }

    // Test case 3: outerHtml() - empty
    @Test
    public void testDocumentTypeOuterHtmlEmpty() {
        DocumentType documentType = new DocumentType("html", "", "", "");
        assertEquals("<!DOCTYPE html>\n", documentType.outerHtml());
    }

    // Test case 4: outerHtml() - with publicId
    @Test
    public void testDocumentTypeOuterHtmlPublicId() {
        DocumentType documentType = new DocumentType("html", "PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"", "", "");
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n", documentType.outerHtml());
    }

    // Test case 5: outerHtml() - with publicId and systemId
    @Test
    public void testDocumentTypeOuterHtmlPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("html", "PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"", "sysId", "");
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" SYSTEM \"sysId\"\n", documentType.outerHtml());
    }

    // Test case 6: outerHtml() - invalid publicId
    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeOuterHtmlInvalidPublicId() {
        DocumentType documentType = new DocumentType("html", "PUBLIC \"InvalidPublicId\"", "", "");
    }

    // Test case 7: outerHtml() - null baseUri
    @Test
    public void testDocumentTypeOuterHtmlNullBaseUri() {
        DocumentType documentType = new DocumentType("html", "", "", null);
        assertEquals("<!DOCTYPE html>\n", documentType.outerHtml());
    }
}
