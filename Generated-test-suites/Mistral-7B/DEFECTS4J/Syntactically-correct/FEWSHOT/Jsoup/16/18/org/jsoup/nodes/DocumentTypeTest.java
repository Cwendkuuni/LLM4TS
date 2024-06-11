package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;

import org.junit.Test;

public class DocumentTypeTest {
    @Test
    public void testNewDocumentType() {
        Document doc = new Document();
        DocumentType doctype = new DocumentType(doc, "#doctypeName", "publicId", "systemId");

        assertEquals("#doctype", doctype.nodeName());
        assertEquals("#doctypeName", doctype.attr("name"));
        assertEquals("publicId", doctype.attr("publicId"));
        assertEquals("systemId", doctype.attr("systemId"));
    }

    @Test
    public void testNullValues() {
        Document doc = new Document();
        DocumentType doctype = new DocumentType(doc, null, null, null);

        assertNull(doctype.attr("name"));
        assertNull(doctype.attr("publicId"));
        assertNull(doctype.attr("systemId"));
    }

    @Test
    public void testOuterHtmlHead() {
        Document doc = new Document();
        DocumentType doctype = new DocumentType(doc, "#doctypeName", "publicId", "systemId");

        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html\n" +
                "PUBLIC \"publicId\"\n" +
                "SYSTEM \"systemId\"\n" +
                ">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyPublicIdAndSystemId() {
        Document doc = new Document();
        DocumentType doctype = new DocumentType(doc, "#doctypeName", "", "");

        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html\n" +
                "PUBLIC \"\"\n" +
                "SYSTEM \"\"\n" +
                ">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithOnlyPublicId() {
        Document doc = new Document();
        DocumentType doctype = new DocumentType(doc, "#doctypeName", "publicId", "");

        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html\n" +
                "PUBLIC \"publicId\"\n" +
                ">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithOnlySystemId() {
        Document doc = new Document();
        DocumentType doctype = new DocumentType(doc, "#doctypeName", "", "systemId");

        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html\n" +
                "SYSTEM \"systemId\"\n" +
                ">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Document doc = new Document();
        DocumentType doctype = new DocumentType(doc, "#doctypeName", "publicId", "systemId");

        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlTail(accum, 0, null);

        assertTrue(accum.toString().isEmpty());
    }

    @Test
    public void testAddAttributes() {
        Document doc = new Document();
        DocumentType doctype = new DocumentType(doc, "#doctypeName", "publicId", "systemId");
        Attributes attrs = new Attributes();
        attrs.put("newAttribute", "newValue");
        doctype.attributes(attrs);

        assertEquals("#doctypeName newAttribute", doctype.attr("name"));
        assertEquals("newValue", doctype.attr("newAttribute"));
    }

    @Test
    public void testAddElementChildren() {
        Document doc = new Document();
        DocumentType doctype = new DocumentType(doc, "#doctypeName", "publicId", "systemId");
        Element child = new Element("childElement");
        doctype.child(child);

        assertEquals(1, doctype.childNodeSize());
        assertEquals("childElement", doctype.child(0).nodeName());
    }
}