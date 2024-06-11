package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertNull(docType.attr("publicId")); // should be empty string if not provided
        assertNull(docType.attr("systemId")); // should be empty string if not provided

        DocumentType docTypeWithPublicId = new DocumentType("html", "publicID", "systemID", null);
        assertEquals("publicID", docTypeWithPublicId.attr("publicId"));

        DocumentType docTypeWithSystemId = new DocumentType("html", "", "systemID", null);
        assertEquals("systemID", docTypeWithSystemId.attr("systemId"));

        DocumentType docTypeWithBoth = new DocumentType("html", "publicID", "systemID", null);
        assertEquals("publicID", docTypeWithBoth.attr("publicId"));
        assertEquals("systemID", docTypeWithBoth.attr("systemId"));
    }

    @Test
    public void testDocumentTypeOutput() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        docType.outerHtmlTail(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"publicID\" systemID>\n";
        assertTrue(accum.toString().equals(expected));

        DocumentType docTypeWithoutPublicId = new DocumentType("html", "", "systemID", null);
        accum.setLength(0);
        docTypeWithoutPublicId.outerHtmlHead(accum, 0, null);
        docTypeWithoutPublicId.outerHtmlTail(accum, 0, null);

        expected = "<!DOCTYPE html systemID>\n";
        assertTrue(accum.toString().equals(expected));

        DocumentType docTypeWithQuirksMode = new DocumentType("html", "publicID", "systemID", null);
        docTypeWithQuirksMode.attr("quirksMode", "true");
        accum.setLength(0);
        docTypeWithQuirksMode.outerHtmlHead(accum, 0, null);
        docTypeWithQuirksMode.outerHtmlTail(accum, 0, null);

        expected = "<!DOCTYPE html PUBLIC \"publicID\" systemID [<!ENTITY % QUIRKS "
                + "SYSTEMS 'SYSTEM'>\n"
                + "]<!ENTITY % QUIRKS SYSTEM 'systemID'>\n"
                + "]<!DOCTYPE html>\n";
        assertTrue(accum.toString().contains(expected));
    }
}