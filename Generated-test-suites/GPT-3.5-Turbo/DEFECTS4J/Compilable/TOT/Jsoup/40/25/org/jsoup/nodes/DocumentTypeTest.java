package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("doctype", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.w3.org/1999/xhtml");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType1 = new DocumentType("doctype", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.w3.org/1999/xhtml");
        StringBuilder accum1 = new StringBuilder();
        Document.OutputSettings out1 = new Document.OutputSettings();
        documentType1.outerHtmlHead(accum1, 0, out1);
        assertEquals("<!DOCTYPE doctype PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", accum1.toString());

        DocumentType documentType2 = new DocumentType("doctype", "", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.w3.org/1999/xhtml");
        StringBuilder accum2 = new StringBuilder();
        Document.OutputSettings out2 = new Document.OutputSettings();
        documentType2.outerHtmlHead(accum2, 0, out2);
        assertEquals("<!DOCTYPE doctype \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", accum2.toString());

        DocumentType documentType3 = new DocumentType("doctype", "-//W3C//DTD XHTML 1.0 Strict//EN", "", "http://www.w3.org/1999/xhtml");
        StringBuilder accum3 = new StringBuilder();
        Document.OutputSettings out3 = new Document.OutputSettings();
        documentType3.outerHtmlHead(accum3, 0, out3);
        assertEquals("<!DOCTYPE doctype PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\">", accum3.toString());

        DocumentType documentType4 = new DocumentType("doctype", "", "", "http://www.w3.org/1999/xhtml");
        StringBuilder accum4 = new StringBuilder();
        Document.OutputSettings out4 = new Document.OutputSettings();
        documentType4.outerHtmlHead(accum4, 0, out4);
        assertEquals("<!DOCTYPE doctype>", accum4.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("doctype", "-", "-", "http://www.w3.org/1999/xhtml");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlTail(accum, 0, out);
        assertEquals("", accum.toString());
    }

    // Additional test cases for error scenarios

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeEmptyName() {
        DocumentType documentType = new DocumentType("", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.w3.org/1999/xhtml");
    }

}