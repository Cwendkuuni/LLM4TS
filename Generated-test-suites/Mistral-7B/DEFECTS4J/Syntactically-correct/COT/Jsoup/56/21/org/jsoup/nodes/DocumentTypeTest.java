package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.DocumentType.OutputSettings;
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
        DocumentType docType = new DocumentType("html", "public-id-html", "system-id-html", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(DocumentType.NAME));
        assertFalse(docType.has(DocumentType.PUBLIC_ID));
        assertFalse(docType.has(DocumentType.SYSTEM_ID));

        DocumentType docTypeWithPublicId = new DocumentType("html", "public-id-html", null, null);
        assertTrue(docTypeWithPublicId.has(DocumentType.PUBLIC_ID));
        assertEquals("public-id-html", docTypeWithPublicId.attr(DocumentType.PUBLIC_ID));

        DocumentType docTypeWithSystemId = new DocumentType("html", null, "system-id-html", null);
        assertTrue(docTypeWithSystemId.has(DocumentType.SYSTEM_ID));
        assertEquals("system-id-html", docTypeWithSystemId.attr(DocumentType.SYSTEM_ID));

        DocumentType docTypeWithNamePublicIdAndSystemId = new DocumentType("html", "public-id-html", "system-id-html", null);
        assertTrue(docTypeWithNamePublicIdAndSystemId.has(DocumentType.NAME));
        assertTrue(docTypeWithNamePublicIdAndSystemId.has(DocumentType.PUBLIC_ID));
        assertTrue(docTypeWithNamePublicIdAndSystemId.has(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() throws Exception {
        DocumentType docType = new DocumentType("html", "public-id-html", "system-id-html", null);

        Appendable accum = new StringBuilder();
        int depth = 0;
        OutputSettings out = OutputSettings.createPrettyPrint();
        docType.outerHtmlHead(accum, depth, out);

        String expectedHTML = "<!DOCTYPE html PUBLIC \"public-id-html\" \"system-id-html\">";
        assertEquals(expectedHTML, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithHTML5Syntax() throws Exception {
        DocumentType docType = new DocumentType("html", null, null, null);

        Appendable accum = new StringBuilder();
        int depth = 0;
        OutputSettings out = OutputSettings.createPrettyPrint();
        docType.outerHtmlHead(accum, depth, out);

        String expectedHTML = "<!doctype html>";
        assertEquals(expectedHTML, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() throws Exception {
        DocumentType docType = new DocumentType("html", null, null, null);

        Appendable accum = new StringBuilder();
        int depth = 0;
        OutputSettings out = OutputSettings.createPrettyPrint();
        docType.outerHtmlTail(accum, depth, out);

        assertNull(accum.toString());
    }

    @Test
    public void testDocumentTypeHasAttribute() {
        DocumentType docType = new DocumentType("html", "public-id-html", "system-id-html", null);

        assertTrue(docType.has(DocumentType.NAME));
        assertTrue(docType.has(DocumentType.PUBLIC_ID));
        assertTrue(docType.has(DocumentType.SYSTEM_ID));

        DocumentType docTypeWithoutName = new DocumentType("html", null, null, null);
        assertFalse(docTypeWithoutName.has(DocumentType.NAME));
    }

    @Test
    public void testDocumentTypeParsing() throws Exception {
        String documentTypeString = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">";
        Node node = Parser.parseFragment(documentTypeString);

        assertEquals(DocumentType.class, node.getClass());
        DocumentType docType = (DocumentType) node;

        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(DocumentType.NAME));
        assertTrue(docType.has(DocumentType.PUBLIC_ID));
        assertEquals("-//W3C//DTD HTML 4.01//EN", docType.attr(DocumentType.PUBLIC_ID));
        assertTrue(docType.has(DocumentType.SYSTEM_ID));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.attr(DocumentType.SYSTEM_ID));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeCreationWithInvalidArgument() {
        new DocumentType("", "", "", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeCreationWithNullName() {
        new DocumentType(null, "", "", null);
    }

}