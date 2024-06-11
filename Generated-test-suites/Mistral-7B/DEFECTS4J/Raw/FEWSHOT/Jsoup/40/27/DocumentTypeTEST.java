package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testCreateDocumentType() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr("name"));
        assertEquals(publicId, docType.attr("publicId"));
        assertEquals(systemId, docType.attr("systemId"));
        assertEquals(baseUri, docType.baseUri());
    }

    @Test
    public void testCreateDocumentTypeWithEmptyAttributes() {
        DocumentType docType = new DocumentType("html");

        assertEquals("#doctype", docType.nodeName());
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html");

        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"publicId\" ";
        assertEquals(expected, accum.subSequence(0, expected.length()).toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemId() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"publicId\" ";
        assertEquals(expected, accum.subSequence(0, expected.length()).toString());

        String actual = accum.subSequence(expected.length(), accum.length()).toString();
        String expectedSysId = "systemId";
        assertEquals(expectedSysId, actual);
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdAndSystemId() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\" ";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html");

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, null);

        assertTrue(accum.toString().isEmpty());
    }

    @Test
    public void testAddChild() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", null, null, null);
        Element element = new Element("html");

        doc.appendChild(docType);
        docType.appendChild(element);

        assertEquals(1, doc.childNodeSize());
        assertEquals(docType, doc.childNode(0));
        assertEquals(1, docType.childNodeSize());
        assertEquals(element, docType.childNode(0));
    }

    @Test
    public void testAddTextNode() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", null, null, null);
        TextNode textNode = new TextNode("text");

        doc.appendChild(docType);
        docType.appendChild(textNode);

        assertEquals(1, doc.childNodeSize());
        assertEquals(docType, doc.childNode(0));
        assertEquals(1, docType.childNodeSize());
        assertEquals(textNode, docType.childNode(0));
    }
}