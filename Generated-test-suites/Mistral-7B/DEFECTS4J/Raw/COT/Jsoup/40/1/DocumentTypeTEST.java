package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.helper.*;
import org.jsoup.nodes.DocumentType.*;
import org.junit.Test;

/**
 * Tests for {@link DocumentType} class.
 */
public class DocumentTypeTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        DocumentType documentType = new DocumentType("html", "publicId1", "systemId1", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals("publicId1", documentType.attr("publicId"));
        assertEquals("systemId1", documentType.attr("systemId"));
        assertEquals("baseUri", documentType.baseUri());
    }

    /**
     * Test nodeName method.
     */
    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "", "", "");
        assertEquals("#doctype", documentType.nodeName());
    }

    /**
     * Test outerHtmlHead method for basic functionality.
     */
    @Test
    public void testOuterHtmlHeadBasic() {
        DocumentType documentType = new DocumentType("html", "publicId1", "systemId1", "baseUri");
        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE html>");

        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlHead(actual, 0, null);
        assertEquals(expected.toString(), actual.toString());
    }

    /**
     * Test outerHtmlHead method for edge cases and exceptions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOuterHtmlHeadEdgeCasesAndExceptions() {
        DocumentType documentType = new DocumentType("", "", "", "");
        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlHead(actual, 0, null);
        fail("Expected IllegalArgumentException, but no exception was thrown.");
    }

    /**
     * Test outerHtmlTail method for basic functionality.
     */
    @Test
    public void testOuterHtmlTailBasic() {
        DocumentType documentType = new DocumentType("html", "publicId1", "systemId1", "baseUri");
        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlTail(actual, 0, null);
        assertTrue(StringUtil.isBlank(actual.toString()));
    }

    /**
     * Test outerHtmlTail method for edge cases and exceptions.
     */
    @Test
    public void testOuterHtmlTailEdgeCasesAndExceptions() {
        DocumentType documentType = new DocumentType("html", "publicId1", "systemId1", "baseUri");
        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlTail(actual, 0, null);

        DocumentType documentTypeNull = null;
        try {
            documentTypeNull.outerHtmlTail(actual, 0, null);
        } catch (NullPointerException e) {
            // Expected
        }
    }
}