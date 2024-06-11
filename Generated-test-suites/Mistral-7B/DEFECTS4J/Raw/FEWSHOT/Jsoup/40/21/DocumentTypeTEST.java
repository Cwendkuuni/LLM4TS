package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testCreateDocumentTypeWithValidParameters() {
        String name = "html";
        String publicId = "--pub-id--";
        String systemId = "--sys-id--";
        String baseUri = "http://example.com/";

        Node documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDocumentTypeWithEmptyName() {
        String publicId = "--pub-id--";
        String systemId = " --sys-id-- ";
        String baseUri = "http://example.com/";

        new DocumentType(null, publicId, systemId, baseUri);
    }

    @Test
    public void testCreateDocumentTypeWithNullPublicId() {
        String name = "html";
        String systemId = "--sys-id--";
        String baseUri = "http://example.com/";

        Node documentType = new DocumentType(name, null, systemId, baseUri);

        assertNull(documentType.attr("publicId"));
    }

    @Test
    public void testCreateDocumentTypeWithEmptyPublicId() {
        String name = "html";
        String publicId = " ";
        String systemId = "--sys-id--";
        String baseUri = "http://example.com/";

        Node documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertSame("", documentType.attr("publicId"));
    }

    @Test
    public void testCreateDocumentTypeWithEmptySystemId() {
        String name = "html";
        String publicId = "--pub-id--";
        String systemId = " ";
        String baseUri = "http://example.com/";

        Node documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertSame("", documentType.attr("systemId"));
    }

    @Test
    public void testCreateDocumentTypeWithNullBaseUri() {
        String name = "html";
        String publicId = "--pub-id--";
        String systemId = " --sys-id-- ";

        Node documentType = new DocumentType(name, publicId, systemId, null);

        assertEquals(null, documentType.baseUri());
    }

    @Test
    public void testOuterHtmlHead() {
        String name = "html";
        String publicId = "--pub-id--";
        String systemId = "--sys-id--";
        String baseUri = "http://example.com/";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"--pub-id--\" \"--sys-id--\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyPublicId() {
        String name = "html";
        String publicId = " ";
        String systemId = "--sys-id--";
        String baseUri = "http://example.com/";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptySystemId() {
        String name = "html";
        String publicId = "--pub-id--";
        String systemId = " ";
        String baseUri = "http://example.com/";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"--pub-id--\" \"\">";
        assertEquals(expected, accum.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testOuterHtmlHeadWithNullAccumulator() {
        String name = "html";
        String publicId = " --pub-id-- ";
        String systemId = " --sys-id-- ";
        String baseUri = "http://example.com/";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        documentType.outerHtmlHead(null, 0, null);
    }

    @Test
    public void testOuterHtmlTail() {
        String name = "html";
        String publicId = "--pub-id--";
        String systemId = " --sys-id-- ";
        String baseUri = "http://example.com/";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);

        assertTrue(accum.toString().isEmpty());
    }

    @Test
    public void testOuterHtmlWithValidParameters() {
        String name = "html";
        String publicId = "--pub-id--";
        String systemId = "--sys-id--";
        String baseUri = "http://example.com/";

        Document document = new Document(baseUri);
        Element root = document.createElement("html");
        document.appendChild(root);
        root.appendChild(new DocumentType(name, publicId, systemId, baseUri));

        StringBuilder accum = new StringBuilder();
        root.outerHtml(accum, null);

        String expected = "<html><!DOCTYPE html PUBLIC \"--pub-id--\" \"--sys-id--\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlWithEmptyPublicId() {
        String name = "html";
        String publicId = " ";
        String systemId = " --sys-id-- ";
        String baseUri = "http://example.com/";

        Document document = new Document(baseUri);
        Element root = document.createElement("html");
        document.appendChild(root);
        root.appendChild(new DocumentType(name, publicId, systemId, baseUri));

        StringBuilder accum = new StringBuilder();
        root.outerHtml(accum, null);

        String expected = "<html><!DOCTYPE html>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlWithEmptySystemId() {
        String name = "html";
        String publicId = " --pub-id-- ";
        String systemId = " ";
        String baseUri = "http://example.com/";

        Document document = new Document(baseUri);
        Element root = document.createElement("html");
        document.appendChild(root);
        root.appendChild(new DocumentType(name, publicId, systemId, baseUri));

        StringBuilder accum = new StringBuilder();
        root.outerHtml(accum, null);

        String expected = "<html><!DOCTYPE html PUBLIC \" --pub-id-- \"\">";
        assertEquals(expected, accum.toString());
    }
}