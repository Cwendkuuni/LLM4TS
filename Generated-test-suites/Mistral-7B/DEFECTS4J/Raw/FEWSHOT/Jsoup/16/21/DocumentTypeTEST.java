package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        Document document = new Document();
        DocumentType doctype = new DocumentType("html", "HTML TLNet DTD", "", document);
        assertEquals("#doctype", doctype.nodeName());
        assertEquals("html", doctype.attr("name"));
        assertNull(doctype.attr("publicId"));
        assertNull(doctype.attr("systemId"));
        assertEquals(document, doctype.parent());
    }

    @Test
    public void testDocumentTypeWithPublicId() {
        Document document = new Document();
        DocumentType doctype = new DocumentType("html", "HTML TLNet DTD", "PUBLIC html 1.0", document);
        assertEquals("#doctype", doctype.nodeName());
        assertEquals("html", doctype.attr("name"));
        assertEquals("PUBLIC html 1.0", doctype.attr("publicId"));
        assertNull(doctype.attr("systemId"));
        assertEquals(document, doctype.parent());
    }

    @Test
    public void testDocumentTypeWithSystemId() {
        Document document = new Document();
        DocumentType doctype = new DocumentType("html", "", "SYSTEM html.dtd", document);
        assertEquals("#doctype", doctype.nodeName());
        assertEquals("html", doctype.attr("name"));
        assertNull(doctype.attr("publicId"));
        assertEquals("SYSTEM html.dtd", doctype.attr("systemId"));
        assertEquals(document, doctype.parent());
    }

    @Test
    public void testDocumentTypeWithPublicIdAndSystemId() {
        Document document = new Document();
        DocumentType doctype = new DocumentType("html", "HTML TLNet DTD", "PUBLIC html 1.0 System Identifier", "SYSTEM system-identifier.dtd");
        assertEquals("#doctype", doctype.nodeName());
        assertEquals("html", doctype.attr("name"));
        assertEquals("PUBLIC html 1.0 System Identifier", doctype.attr("publicId"));
        assertEquals("SYSTEM system-identifier.dtd", doctype.attr("systemId"));
        assertEquals(document, doctype.parent());
    }

    @Test
    public void testOuterHtmlHead() {
        Document document = new Document();
        DocumentType doctype = new DocumentType("html", "HTML TLNet DTD", "PUBLIC html 1.0", document);

        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML TLNet DTD\"", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyPublicId() {
        Document document = new Document();
        DocumentType doctype = new DocumentType("html", "", "", document);

        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyPublicIdAndSystemId() {
        Document document = new Document();
        DocumentType doctype = new DocumentType("html", "", "", document);

        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemId() {
        Document document = new Document();
        DocumentType doctype = new DocumentType("html", "", "SYSTEM html.dtd", document);

        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html SYSTEM html.dtd", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdAndSystemId() {
        Document document = new Document();
        DocumentType doctype = new DocumentType("html", "HTML TLNet DTD", "PUBLIC html 1.0 System Identifier", "SYSTEM system-identifier.dtd");

        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML TLNet DTD\" SYSTEM system-identifier.dtd", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Document document = new Document();
        DocumentType doctype = new DocumentType("html", "HTML TLNet DTD", "PUBLIC html 1.0", document);

        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlTail(accum, 0, null);

        assertEquals("", accum.toString());
    }

    @Test
    public void testOuterHtmlWithDocumentType() {
        Document document = new Document();
        DocumentType doctype = new DocumentType("html", "HTML TLNet DTD", "PUBLIC html 1.0", document);
        Element docElement = new Element("html");
        docElement.append(doctype);

        StringBuilder accum = new StringBuilder();
        document.outerHtml(accum, null);

        String expected = "<!DOCTYPE html PUBLIC \"HTML TLNet DTD\"\n<html>\n";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlWithEmptyDocumentType() {
        Document document = new Document();
        DocumentType doctype = new DocumentType("html", "", "", document);
        Element docElement = new Element("html");
        docElement.append(doctype);

        StringBuilder accum = new StringBuilder();
        document.outerHtml(accum, null);

        String expected = "<!DOCTYPE html\n<html>\n";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlWithDocumentTypeAndContent() {
        Document document = new Document();
        DocumentType doctype = new DocumentType("html", "HTML TLNet DTD", "PUBLIC html 1.0", document);
        Element docElement = new Element("html");
        docElement.append(doctype);
        docElement.append(new TextNode("Hello World!"));

        StringBuilder accum = new StringBuilder();
        document.outerHtml(accum, null);

        String expected = "<!DOCTYPE html PUBLIC \"HTML TLNet DTD\"\n<html>\nHello World!\n";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlWithDocumentTypeAndNestedElements() {
        Document document = new Document();
        DocumentType doctype = new DocumentType("html", "HTML TLNet DTD", "PUBLIC html 1.0", document);
        Element docElement = new Element("html");
        docElement.append(doctype);
        Element head = new Element("head");
        Element body = new Element("body");
        docElement.append(head);
        docElement.append(body);

        StringBuilder accum = new StringBuilder();
        document.outerHtml(accum, null);

        String expected = "<!DOCTYPE html PUBLIC \"HTML TLNet DTD\"\n<html>\n<head>\n</head>\n<body>\n</body>\n";
        assertEquals(expected, accum.toString());
    }
}