
package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHeadWithNoPublicIdAndNoSystemId() {
        DocumentType documentType = new DocumentType("html", "", "", "http://www.example.com");
        String expectedOutput = "<!doctype";
        assertEquals(expectedOutput, documentType.outerHtmlHead());
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "", "http://www.example.com");
        String expectedOutput = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\">";
        assertEquals(expectedOutput, documentType.outerHtmlHead());
    }

    @Test
    public void testOuterHtmlHeadWithSystemId() {
        DocumentType documentType = new DocumentType("html", "", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
        String expectedOutput = "<!DOCTYPE html \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
        assertEquals(expectedOutput, documentType.outerHtmlHead());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdAndSystemId() {
        String expectedOutput = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
        assertEquals(expectedOutput, documentType.outerHtmlHead());
    }

    @Test
    public void testOuterHtmlTail() {
        assertEquals("", documentType.outerHtmlTail());
    }

    @Test
    public void testHasReturnsTrueWhenAttributeExist() {
        assertTrue(documentType.has(DocumentType.PUBLIC_ID));
    }

    @Test
    public void testHasReturnsFalseWhenAttributeDoesNotExist() {
        assertFalse(documentType.has("nonExistingAttribute"));
    }

}
