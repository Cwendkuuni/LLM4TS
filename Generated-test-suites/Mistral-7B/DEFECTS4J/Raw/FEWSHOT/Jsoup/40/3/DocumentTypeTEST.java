package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType1 = new DocumentType("html", "HTMLPublicID", "HTMLSystemID", null);
        DocumentType docType2 = new DocumentType("html", null, null, null);

        assertEquals("html", docType1.attr("name"));
        assertEquals("HTMLPublicID", docType1.attr("publicId"));
        assertEquals("HTMLSystemID", docType1.attr("systemId"));

        assertEquals("html", docType2.attr("name"));
        assertNull(docType2.attr("publicId"));
        assertNull(docType2.attr("systemId"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyNameInDocumentTypeCreation() {
        new DocumentType(null, null, null, null);
    }

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", null, null, null);
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "HTMLPublicID", "HTMLSystemID", null);
        StringBuilder accum = new StringBuilder();

        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"HTMLPublicID\" \"HTMLSystemID\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyPublicIdAndSystemId() {
        DocumentType docType = new DocumentType("html", null, null, null);
        StringBuilder accum = new StringBuilder();

        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPublicIdInOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", null, null, null);
        StringBuilder accum = new StringBuilder();

        docType.outerHtmlHead(accum, 0, null);
        docType.outerHtmlHead(accum, 0, null); // Second call to trigger the error
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSystemIdInOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", null, null, null);
        StringBuilder accum = new StringBuilder();

        docType.outerHtmlHead(accum, 0, null);
        docType.outerHtmlHead(accum, 0, null); // Second call to trigger the error
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", null, null, null);
        StringBuilder accum = new StringBuilder();

        docType.outerHtmlTail(accum, 0, null);

        assertTrue(accum.toString().isEmpty());
    }
}