package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "HTML TL Net DTD", "", "http://www.w3.org/TR/REC-html40/loose.dtd");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("HTML TL Net DTD", docType.attr("publicId"));
        assertEquals("", docType.attr("systemId"));
        assertEquals("http://www.w3.org/TR/REC-html40/loose.dtd", docType.attr("baseUri"));
    }

    @Test
    public void testEmptyDocumentTypeCreation() {
        DocumentType docType = new DocumentType("", "", "", "");
        assertEquals("#doctype", docType.nodeName());
        assertNull(docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.attr("baseUri"));
    }

    @Test
    public void testDocumentTypeToString() {
        DocumentType docType = new DocumentType("html", "HTML TL Net DTD", "", "http://www.w3.org/TR/REC-html40/loose.dtd");
        String docTypeString = docType.toString();
        String expected = "<!DOCTYPE html PUBLIC \"HTML TL Net DTD\" \"http://www.w3.org/TR/REC-html40/loose.dtd\">";
        assertEquals(expected, docTypeString);
    }

    @Test
    public void testEmptyDocumentTypeToString() {
        DocumentType docType = new DocumentType("", "", "", "");
        String docTypeString = docType.toString();
        String expected = "<!DOCTYPE>";
        assertEquals(expected, docTypeString);
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"HTML TL Net DTD\" \"http://www.w3.org/TR/REC-html40/loose.dtd\">");
        DocumentType docType = (DocumentType) doc.head();
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        String expected = "<!DOCTYPE html PUBLIC \"HTML TL Net DTD\" \"http://www.w3.org/TR/REC-html40/loose.dtd\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"HTML TL Net DTD\" \"http://www.w3.org/TR/REC-html40/loose.dtd\">");
        DocumentType docType = (DocumentType) doc.head();
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }

    @Test
    public void testDocumentTypeWithAttributesOuterHtmlHead() {
        Document doc = Jsoup.parse("<!DOCTYPE html SYSTEM \"about:local\"> PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        DocumentType docType = (DocumentType) doc.head();
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        String expected = "<!DOCTYPE html SYSTEM \"about:local\"> PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
        assertEquals(expected, accum.toString());
    }
}