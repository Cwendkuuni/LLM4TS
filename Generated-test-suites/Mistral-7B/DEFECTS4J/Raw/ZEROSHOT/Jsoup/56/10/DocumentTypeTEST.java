package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import org.junit.Test;

/**
 * Test class for DocumentType.
 */
public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", "HTML", "HTML", null);
        assertNotNull(documentType);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr(DocumentType.NAME));
        assertFalse(documentType.has(DocumentType.PUBLIC_ID));
        assertFalse(documentType.has(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeCreationWithPublicId() {
        DocumentType documentType = new DocumentType("html", "HTML PUBLIC", "DTD HTML 1.0", null);
        assertNotNull(documentType);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("HTML PUBLIC", documentType.attr(DocumentType.PUBLIC_ID));
        assertNull(documentType.attr(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeCreationWithSystemId() {
        DocumentType documentType = new DocumentType("html", null, "DTD HTML 1.0", null);
        assertNotNull(documentType);
        assertEquals("#doctype", documentType.nodeName());
        assertNull(documentType.attr(DocumentType.PUBLIC_ID));
        assertEquals("DTD HTML 1.0", documentType.attr(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeCreationWithAllAttributes() {
        DocumentType documentType = new DocumentType("html", "HTML PUBLIC", "DTD HTML 1.0", "http://example.com/DTDs/");
        assertNotNull(documentType);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("HTML PUBLIC", documentType.attr(DocumentType.PUBLIC_ID));
        assertEquals("DTD HTML 1.0", documentType.attr(DocumentType.SYSTEM_ID));
        assertEquals("http://example.com/DTDs/", documentType.attr(DocumentType.BASE_URI));
    }

    @Test
    public void testDocumentTypeParser() throws Exception {
        String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">";
        Document document = Parser.parseBodyFragment(html);
        Node node = document.body();
        assertEquals(Node.DOCTYPE_NODE, node.nodeType());
        DocumentType documentType = (DocumentType) node;
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr(DocumentType.NAME));
        assertEquals("HTML PUBLIC", documentType.attr(DocumentType.PUBLIC_ID));
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", documentType.attr(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeOuterHtml() throws Exception {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "HTML PUBLIC", "DTD HTML 1.0", null);
        document.appendChild(documentType);

        String expected = "<!DOCTYPE html PUBLIC \"HTML PUBLIC\";>";
        assertEquals(expected, documentType.outerHtml());
    }

    @Test
    public void testDocumentTypeHasAttribute() {
        DocumentType documentType = new DocumentType("html", null, null, null);

        assertFalse(documentType.has(DocumentType.NAME));
        assertFalse(documentType.has(DocumentType.PUBLIC_ID));
        assertFalse(documentType.has(DocumentType.SYSTEM_ID));

        documentType.attr(DocumentType.NAME, "html");
        assertTrue(documentType.has(DocumentType.NAME));

        documentType.attr(DocumentType.PUBLIC_ID, "HTML PUBLIC");
        assertTrue(documentType.has(DocumentType.PUBLIC_ID));

        documentType.attr(DocumentType.SYSTEM_ID, "DTD HTML 1.0");
        assertTrue(documentType.has(DocumentType.SYSTEM_ID));
    }
}