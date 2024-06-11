package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.jsoup.helper.StringUtil.isBlank;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "HTML TDNG", "http://www.w3.org/TR/html4/strict.dtd", null);

        assertEquals("html", docType.name());
        assertEquals("HTML TDNG", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.attr("systemId"));
        assertNull(docType.attr("xmlns"));
    }

    @Test
    public void testDocumentTypeCreationWithNullAttributes() {
        DocumentType docType = new DocumentType("html", null, null, null);

        assertEquals("html", docType.name());
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.attr("xmlns"));
    }

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", null, null, null);

        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "HTML TDNG", "http://www.w3.org/TR/html4/strict.dtd", null);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"HTML TDNG\" \"http://www.w3.org/TR/html4/strict.dtd\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyPublicId() {
        DocumentType docType = new DocumentType("html", "", "http://www.w3.org/TR/html4/strict.dtd", null);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html SYSTEM \"http://www.w3.org/TR/html4/strict.dtd\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptySystemId() {
        DocumentType docType = new DocumentType("html", "HTML TDNG", "", null);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"HTML TDNG\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithNullBaseUri() {
        DocumentType docType = new DocumentType("html", "HTML TDNG", "http://www.w3.org/TR/html4/strict.dtd", null);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"HTML TDNG\" \"http://www.w3.org/TR/html4/strict.dtd\">";
        assertEquals(expected, accum.toString());

        DocumentType docTypeWithBaseUri = new DocumentType("html", "HTML TDNG", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com/");

        StringBuilder accumWithBaseUri = new StringBuilder();
        docTypeWithBaseUri.outerHtmlHead(accumWithBaseUri, 0, null);

        String expectedWithBaseUri = "<!DOCTYPE html PUBLIC \"HTML TDNG\" \"http://www.w3.org/TR/html4/strict.dtd\" SYSTEM>";
        assertEquals(expectedWithBaseUri, accumWithBaseUri.toString());
    }

    @Test
    public void testOuterHtmlHeadWithBothPublicIdAndSystemIdSet() {
        DocumentType docType = new DocumentType("html", "HTML TDNG PUBLICID", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com/");

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"HTML TDNG PUBLICID\" \"http://www.w3.org/TR/html4/strict.dtd\" SYSTEM>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", null, null, null);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, null);

        String expected = "";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testEquals() {
        DocumentType docType1 = new DocumentType("html", "HTML TDNG", "http://www.w3.org/TR/html4/strict.dtd", null);
        DocumentType docType2 = new DocumentType("html", "HTML TDNG", "http://www.w3.org/TR/html4/strict.dtd", null);

        assertTrue(docType1.equals(docType2));
    }

    @Test
    public void testEqualsWithDifferentName() {
        DocumentType docType1 = new DocumentType("html", "HTML TDNG", "http://www.w3.org/TR/html4/strict.dtd", null);
        DocumentType docType2 = new DocumentType("head", "HTML TDNG", "http://www.w3.org/TR/html4/strict.dtd", null);

        assertNotSame(docType1, docType2);
    }

    @Test
    public void testEqualsWithDifferentPublicId() {
        DocumentType docType1 = new DocumentType("html", "HTML TDNG1", "http://www.w3.org/TR/html4/strict.dtd", null);
        DocumentType docType2 = new DocumentType("html", "HTML TDNG", "http://www.w3.org/TR/html4/strict.dtd", null);

        assertNotSame(docType1, docType2);
    }

    @Test
    public void testEqualsWithDifferentSystemId() {
        DocumentType docType1 = new DocumentType("html", "HTML TDNG", "http://www.w3.org/TR/html4/strict.dtd1", null);
        DocumentType docType2 = new DocumentType("html", "HTML TDNG", "http://www.w3.org/TR/html4/strict.dtd", null);

        assertNotSame(docType1, docType2);
    }

    @Test
    public void testEqualsWithDifferentBaseUri() {
        DocumentType docType1 = new DocumentType("html", null, null, null);
        DocumentType docType2 = new DocumentType("html", null, null, "http://example.com/");

        assertNotSame(docType1, docType2);
    }
}