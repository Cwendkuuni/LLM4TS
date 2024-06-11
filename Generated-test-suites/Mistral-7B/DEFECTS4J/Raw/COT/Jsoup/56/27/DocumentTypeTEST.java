package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Attributes;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
        Assert.assertEquals("#doctype", docType.nodeName());
        Assert.assertEquals("html", docType.attr(DocumentType.NAME));
        Assert.assertEquals("publicID", docType.attr(DocumentType.PUBLIC_ID));
        Assert.assertEquals("systemID", docType.attr(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() throws Exception {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
        doc.appendChild(docType);

        Appendable accum = new StringBuffer();
        doc.outerHtml(accum, 0, Document.OutputSettings.syntaxHtml5());

        String expected = "<!doctype html PUBLIC \"publicID\" \"systemID\">";
        String actual = accum.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptyPublicIdAndSystemId() throws Exception {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", null, null, null);
        doc.appendChild(docType);

        Appendable accum = new StringBuffer();
        doc.outerHtml(accum, 0, Document.OutputSettings.syntaxHtml5());

        String expected = "<!doctype html>";
        String actual = accum.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IOException.class)
    public void testDocumentTypeOuterHtmlHeadWithNullOutputSettings() throws Exception {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
        doc.appendChild(docType);

        Appendable accum = new StringBuffer();
        doc.outerHtml(accum, 0, null);
    }

    @Test
    public void testDocumentTypeHasAttribute() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
        Assert.assertTrue(docType.has(DocumentType.NAME));
        Assert.assertTrue(docType.has(DocumentType.PUBLIC_ID));
        Assert.assertTrue(docType.has(DocumentType.SYSTEM_ID));
        Element element = new Element("test");
        Assert.assertFalse(element.has(DocumentType.NAME));
        Assert.assertFalse(element.has(DocumentType.PUBLIC_ID));
        Assert.assertFalse(element.has(DocumentType.SYSTEM_ID));
    }
}