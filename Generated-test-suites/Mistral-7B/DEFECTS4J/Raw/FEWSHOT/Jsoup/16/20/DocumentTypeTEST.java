package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "HTML}", "HTML 4.01 Frameset", "http://www.w3.org/TR/html4/frameset.dtd");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("HTML", docType.attr("publicId"));
        assertEquals("HTML 4.01 Frameset", docType.attr("systemId"));
        assertEquals("http://www.w3.org/TR/html4/frameset.dtd", docType.attr("baseUri"));
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
    public void testDocumentTypeOuterHtml() {
        DocumentType docType = new DocumentType("html", "HTML", "HTML 4.01 Frameset", "http://www.w3.org/TR/html4/frameset.dtd");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        docType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html PUBLIC \"HTML\"", accum.toString());

        docType.outerHtmlTail(accum, 0, out);
        assertEquals(">\n", accum.toString());

        accum.setLength(0);
        docType.outerHtml(accum, 0, out);
        assertEquals("<!DOCTYPE html PUBLIC \"HTML\"", accum.toString());
        assertEquals(">\n", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlWithPublicId() {
        DocumentType docType = new DocumentType("html", "PUBLIC \"-//W3C//DTD HTML 4.01//EN\"", "HTML 4.01 Transitional", "http://www.w3.org/TR/html4/transitional.dtd");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        docType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\"", accum.toString());

        docType.outerHtmlTail(accum, 0, out);
        assertEquals(">\n", accum.toString());

        accum.setLength(0);
        docType.outerHtml(accum, 0, out);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\"", accum.toString());
        assertEquals(">\n", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlWithSystemId() {
        DocumentType docType = new DocumentType("html", "", "SYSTEM \"about:legacy-compat\"", "");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        docType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html SYSTEM \"about:legacy-compat\">", accum.toString());

        docType.outerHtmlTail(accum, 0, out);
        assertEquals(">\n", accum.toString());

        accum.setLength(0);
        docType.outerHtml(accum, 0, out);
        assertEquals("<!DOCTYPE html SYSTEM \"about:legacy-compat\">", accum.toString());
        assertEquals(">\n", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlWithPublicIdAndSystemId() {
        DocumentType docType = new DocumentType("html", "PUBLIC \"-//W3C//DTD HTML 4.01//EN\" SystemRoot \"http://www.w3.org/TR/REC-html401/DTD/html1.dtd\" [<!ENTITY % W3C SYSTEM \"http://www.w3.org/2000/01/rdf-schema.dtd\">]", "HTML 4.01 Transitional", "http://www.w3.org/TR/html4/transitional.dtd");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        docType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" SystemRoot=\"http://www.w3.org/TR/REC-html401/DTD/html1.dtd\" [<!ENTITY % W3C SYSTEM \"http://www.w3.org/2000/01/rdf-schema.dtd\">]", accum.toString());

        docType.outerHtmlTail(accum, 0, out);
        assertEquals(">\n", accum.toString());

        accum.setLength(0);
        docType.outerHtml(accum, 0, out);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" SystemRoot=\"http://www.w3.org/TR/REC-html401/DTD/html1.dtd\" [<!ENTITY % W3C SYSTEM \"http://www.w3.org/2000/01/rdf-schema.dtd\">]", accum.toString());
        assertEquals(">\n", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlWithEmptyValues() {
        DocumentType docType = new DocumentType("html", "", "", "");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        docType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html>", accum.toString());

        docType.outerHtmlTail(accum, 0, out);
        assertEquals(">\n", accum.toString());

        accum.setLength(0);
        docType.outerHtml(accum, 0, out);
        assertEquals("<!DOCTYPE html>", accum.toString());
        assertEquals(">\n", accum.toString());
    }

    @Test
    public void testEquals() {
        DocumentType docType1 = new DocumentType("html", "HTML", "HTML 4.01 Frameset", "http://www.w3.org/TR/html4/frameset.dtd");
        DocumentType docType2 = new DocumentType("html", "HTML", "HTML 4.01 Frameset", "http://www.w3.org/TR/html4/frameset.dtd");
        DocumentType docType3 = new DocumentType("html", "HTML", "HTML 4.01 Transitional", "http://www.w3.org/TR/html4/transitional.dtd");

        assertSame(docType1, docType1);
        assertSame(docType1, docType1);

        assertNotSame(docType1, docType2);
        assertNotSame(docType1, docType3);
    }

    @Test
    public void testHashCode() {
        DocumentType docType1 = new DocumentType("html", "HTML", "HTML 4.01 Frameset", "http://www.w3.org/TR/html4/frameset.dtd");
        DocumentType docType2 = new DocumentType("html", "HTML", "HTML 4.01 Frameset", "http://www.w3.org/TR/html4/frameset.dtd");
        DocumentType docType3 = new DocumentType("html", "HTML", "HTML 4.01 Transitional", "http://www.w3.org/TR/html4/transitional.dtd");

        assertEquals(docType1.hashCode(), docType1.hashCode());
        assertEquals(docType1.hashCode(), docType2.hashCode());
        assertNotEquals(docType1.hashCode(), docType3.hashCode());
    }
}