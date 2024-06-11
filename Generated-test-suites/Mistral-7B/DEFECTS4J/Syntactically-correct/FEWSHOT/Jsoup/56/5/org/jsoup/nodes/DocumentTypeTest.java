package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document.OutputSettings.*;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "HTML", "html", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(NAME));
        assertFalse(docType.has(PUBLIC_ID));
        assertFalse(docType.has(SYSTEM_ID));

        DocumentType docTypeWithPublicId = new DocumentType("html", "HTML PUBLIC", "html", null);
        assertEquals("#doctype", docTypeWithPublicId.nodeName());
        assertEquals("html", docTypeWithPublicId.attr(NAME));
        assertTrue(docTypeWithPublicId.has(PUBLIC_ID));
        assertEquals("HTML PUBLIC", docTypeWithPublicId.attr(PUBLIC_ID));
        assertFalse(docTypeWithPublicId.has(SYSTEM_ID));

        DocumentType docTypeWithSystemId = new DocumentType("html", "HTML", "html-system.dtd", null);
        assertEquals("#doctype", docTypeWithSystemId.nodeName());
        assertEquals("html", docTypeWithSystemId.attr(NAME));
        assertFalse(docTypeWithSystemId.has(PUBLIC_ID));
        assertTrue(docTypeWithSystemId.has(SYSTEM_ID));
        assertEquals("html-system.dtd", docTypeWithSystemId.attr(SYSTEM_ID));

        DocumentType docTypeWithPublicAndSystemId = new DocumentType("html", "HTML PUBLIC -//W3C//DTD HTML 4.01 Transitional//EN", "html-system.dtd", null);
        assertEquals("#doctype", docTypeWithPublicAndSystemId.nodeName());
        assertEquals("HTML PUBLIC -//W3C//DTD HTML 4.01 Transitional//EN", docTypeWithPublicAndSystemId.attr(NAME));
        assertTrue(docTypeWithPublicAndSystemId.has(PUBLIC_ID));
        assertEquals("HTML PUBLIC", docTypeWithPublicAndSystemId.attr(PUBLIC_ID));
        assertTrue(docTypeWithPublicAndSystemId.has(SYSTEM_ID));
        assertEquals("html-system.dtd", docTypeWithPublicAndSystemId.attr(SYSTEM_ID));
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        DocumentType docType = new DocumentType("html", "HTML", "html", null);
        StringBuilder sb = new StringBuilder();
        docType.outerHtmlHead(sb, 0, Syntax.html);
        assertEquals("<!doctype html>", sb.toString());

        DocumentType docTypeWithPublicId = new DocumentType("html", "HTML PUBLIC", "html", null);
        sb = new StringBuilder();
        docTypeWithPublicId.outerHtmlHead(sb, 0, Syntax.html);
        assertEquals("<!DOCTYPE html PUBLIC \"HTML PUBLIC\">", sb.toString());

        DocumentType docTypeWithSystemId = new DocumentType("html", "HTML", "html-system.dtd", null);
        sb = new StringBuilder();
        docTypeWithSystemId.outerHtmlHead(sb, 0, Syntax.html);
        assertEquals("<!DOCTYPE html SYSTEM \"html-system.dtd\">", sb.toString());

        DocumentType docTypeWithPublicAndSystemId = new DocumentType("html", "HTML PUBLIC -//W3C//DTD HTML 4.01 Transitional//EN", "html-system.dtd", null);
        sb = new StringBuilder();
        docTypeWithPublicAndSystemId.outerHtmlHead(sb, 0, Syntax.html);
        assertEquals("<!DOCTYPE HTML PUBLIC \"HTML PUBLIC -//W3C//DTD HTML 4.01 Transitional//EN\" \"html-system.dtd\">", sb.toString());
    }

    @Test
    public void testHasAttribute() {
        DocumentType docType = new DocumentType("html", "HTML", "html", null);
        assertFalse(docType.has("nonExistentAttribute"));

        DocumentType docTypeWithPublicId = new DocumentType("html", "HTML PUBLIC", "html", null);
        assertFalse(docTypeWithPublicId.has("nonExistentAttribute"));

        DocumentType docTypeWithSystemId = new DocumentType("html", "HTML", "html-system.dtd", null);
        assertFalse(docTypeWithSystemId.has("nonExistentAttribute"));

        DocumentType docTypeWithPublicAndSystemId = new DocumentType("html", "HTML PUBLIC -//W3C//DTD HTML 4.01 Transitional//EN", "html-system.dtd", null);
        assertFalse(docTypeWithPublicAndSystemId.has("nonExistentAttribute"));

        DocumentType docTypeWithName = new DocumentType("html", "HTML", "html", null);
        assertTrue(docTypeWithName.has(NAME));

        DocumentType docTypeWithPublicIdName = new DocumentType("html", "HTML PUBLIC", "html", null);
        assertTrue(docTypeWithPublicIdName.has(NAME));
        assertTrue(docTypeWithPublicIdName.has(PUBLIC_ID));

        DocumentType docTypeWithSystemIdName = new DocumentType("html", "HTML", "html-system.dtd", null);
        assertTrue(docTypeWithSystemIdName.has(NAME));
        assertTrue(docTypeWithSystemIdName.has(SYSTEM_ID));

        DocumentType docTypeWithPublicAndSystemIdName = new DocumentType("html", "HTML PUBLIC -//W3C//DTD HTML 4.01 Transitional//EN", "html-system.dtd", null);
        assertTrue(docTypeWithPublicAndSystemIdName.has(NAME));
        assertTrue(docTypeWithPublicAndSystemIdName.has(PUBLIC_ID));
        assertTrue(docTypeWithPublicAndSystemIdName.has(SYSTEM_ID));
    }

    @Test
    public void testNullBaseUri() {
        DocumentType docType = new DocumentType("html", "HTML", "html", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(NAME));
        assertFalse(docType.has(PUBLIC_ID));
        assertFalse(docType.has(SYSTEM_ID));

        DocumentType docTypeWithPublicId = new DocumentType("html", "HTML PUBLIC", "html", null);
        assertEquals("#doctype", docTypeWithPublicId.nodeName());
        assertEquals("html", docTypeWithPublicId.attr(NAME));
        assertTrue(docTypeWithPublicId.has(PUBLIC_ID));
        assertEquals("HTML PUBLIC", docTypeWithPublicId.attr(PUBLIC_ID));
        assertFalse(docTypeWithPublicId.has(SYSTEM_ID));

        DocumentType docTypeWithSystemId = new DocumentType("html", "HTML", "html-system.dtd", null);
        assertEquals("#doctype", docTypeWithSystemId.nodeName());
        assertEquals("html", docTypeWithSystemId.attr(NAME));
        assertFalse(docTypeWithSystemId.has(PUBLIC_ID));
        assertTrue(docTypeWithSystemId.has(SYSTEM_ID));
        assertEquals("html-system.dtd", docTypeWithSystemId.attr(SYSTEM_ID));

        DocumentType docTypeWithPublicAndSystemId = new DocumentType("html", "HTML PUBLIC -//W3C//DTD HTML 4.01 Transitional//EN", "html-system.dtd", null);
        assertEquals("#doctype", docTypeWithPublicAndSystemId.nodeName());
        assertEquals("HTML PUBLIC -//W3C//DTD HTML 4.01 Transitional//EN", docTypeWithPublicAndSystemId.attr(NAME));
        assertTrue(docTypeWithPublicAndSystemId.has(PUBLIC_ID));
        assertEquals("HTML PUBLIC", docTypeWithPublicAndSystemId.attr(PUBLIC_ID));
        assertTrue(docTypeWithPublicAndSystemId.has(SYSTEM_ID));
        assertEquals("html-system.dtd", docTypeWithPublicAndSystemId.attr(SYSTEM_ID));
    }
}