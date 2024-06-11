package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        String name = "html";
        String publicId = "HTML5";
        String systemId = "http://www.w3.org/TR/html5/";
        String baseUri = "http://example.com/";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(DocumentType.NAME));
        assertEquals("HTML5", docType.attr(DocumentType.PUBLIC_ID));
        assertEquals("http://www.w3.org/TR/html5/", docType.attr(DocumentType.SYSTEM_ID));
        assertEquals("http://example.com/", docType.baseUri());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        Document doc = new Document();
        doc.outputSettings(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));

        DocumentType docType = new DocumentType("html", null, null, null);
        doc.appendChild(docType);

        String expected = "<!doctype html>";
        String actual = doc.body().outerHtml();

        assertEquals(expected, actual);
    }

    @Test
    public void testHasAttribute() {
        DocumentType docType = new DocumentType("html", "HTML5", "http://www.w3.org/TR/html5", null);

        assertTrue(docType.has(DocumentType.NAME));
        assertTrue(docType.has(DocumentType.PUBLIC_ID));
        assertFalse(docType.has("non-existent-attribute"));
    }

    @Test
    public void testEmptyAttributes() {
        DocumentType docType = new DocumentType("html", null, null, null);

        assertFalse(docType.has(DocumentType.NAME));
        assertFalse(docType.has(DocumentType.PUBLIC_ID));
        assertFalse(docType.has(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", null, null, null);
        doc.appendChild(docType);

        String expected = ">";
        String actual = docType.outerHtmlTail(null, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)).toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testOuterHtmlTailWithPublicId() throws IOException {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "HTML5", null, null);
        doc.appendChild(docType);

        String expected = " PUBLIC \"HTML5\"";
        String actual = docType.outerHtmlTail(null, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)).toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testOuterHtmlTailWithSystemId() throws IOException {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", null, "http://www.w3.org/TR/html5/", null);
        doc.appendChild(docType);

        String expected = " SYSTEM \"http://www.w3.org/TR/html5/\">";
        String actual = docType.outerHtmlTail(null, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)).toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testOuterHtmlTailWithPublicIdAndSystemId() throws IOException {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "HTML5", "http://www.w3.org/TR/html5/", null);
        doc.appendChild(docType);

        String expected = " PUBLIC \"HTML5\" SYSTEM \"http://www.w3.org/TR/html5/\">";
        String actual = docType.outerHtmlTail(null, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)).toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testOuterHtmlTailWithEmptyAttributes() throws IOException {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", null, null, null);
        doc.appendChild(docType);

        String expected = ">";
        String actual = docType.outerHtmlTail(null, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)).toString();

        assertEquals(expected, actual);
    }
}