package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.helper.StringUtil;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "HTML Ted Ngwenya", "W3C Recommendation", "http://www.w3.org/TR/REC-html40/");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("HTML Ted Ngwenya", docType.attr("publicId"));
        assertEquals("W3C Recommendation", docType.attr("systemId"));
        assertEquals("http://www.w3.org/TR/REC-html40/", docType.baseUri());
    }

    @Test
    public void testDocumentTypeWithEmptyAttributes() {
        DocumentType docType = new DocumentType("html");
        assertEquals("#doctype", docType.nodeName());
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.baseUri());
    }

    @Test
    public void testDocumentTypeOuterHtml() {
        Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
        DocumentType docType = doc.document().child(0);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlWithPublicId() {
        Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"HTML Templates\" \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        DocumentType docType = doc.document().child(0);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML Templates\" \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlWithSystemId() {
        Document doc = Jsoup.parse("<!DOCTYPE html SYSTEM \"about:legacy-compat\">");
        DocumentType docType = doc.document().child(0);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html SYSTEM \"about:legacy-compat\">", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlWithBothPublicIdAndSystemId() {
        Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\" [<!ENTITY % html SYSTEM \"http://www.w3.org/1999/xhtml\"> %html]>");
        DocumentType docType = doc.document().child(0);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\" [<!ENTITY % html SYSTEM \"http://www.w3.org/1999/xhtml\"> %html]", accum.toString());
    }

    @Test
    public void testDocumentTypeWithIsBlankPublicIdAndSystemId() {
        DocumentType docType = new DocumentType("html", "", "", "");
        assertEquals("#doctype", docType.nodeName());
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.baseUri());

        Document doc = Jsoup.parse("<!DOCTYPE html>");
        DocumentType docType2 = doc.document().child(0);
        assertEquals("#doctype", docType2.nodeName());
        assertNull(docType2.attr("publicId"));
        assertNull(docType2.attr("systemId"));
        assertNull(docType2.baseUri());
    }
}