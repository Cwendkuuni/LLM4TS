package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://www.w3.org/TR/html4/strict.dtd");
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType1 = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://www.w3.org/TR/html4/strict.dtd");
        StringBuilder accum1 = new StringBuilder();
        Document.OutputSettings out1 = new Document.OutputSettings();
        docType1.outerHtmlHead(accum1, 0, out1);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">", accum1.toString());

        DocumentType docType2 = new DocumentType("html", "", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://www.w3.org/TR/html4/strict.dtd");
        StringBuilder accum2 = new StringBuilder();
        Document.OutputSettings out2 = new Document.OutputSettings();
        docType2.outerHtmlHead(accum2, 0, out2);
        assertEquals("<!DOCTYPE html \"http://www.w3.org/TR/REC-html40/strict.dtd\">", accum2.toString());

        DocumentType docType3 = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "", "http://www.w3.org/TR/html4/strict.dtd");
        StringBuilder accum3 = new StringBuilder();
        Document.OutputSettings out3 = new Document.OutputSettings();
        docType3.outerHtmlHead(accum3, 0, out3);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0//EN\">", accum3.toString());

        DocumentType docType4 = new DocumentType("html", "", "", "http://www.w3.org/TR/html4/strict.dtd");
        StringBuilder accum4 = new StringBuilder();
        Document.OutputSettings out4 = new Document.OutputSettings();
        docType4.outerHtmlHead(accum4, 0, out4);
        assertEquals("<!DOCTYPE html>", accum4.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://www.w3.org/TR/html4/strict.dtd");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        docType.outerHtmlTail(accum, 0, out);
        assertEquals("", accum.toString());
    }
}
