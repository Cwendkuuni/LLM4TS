package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor_validInputs() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN",
                                                "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd",
                                                "http://example.com");

        assertEquals("html", docType.attr("name"));
        assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", docType.attr("systemId"));
        assertEquals("http://example.com", docType.baseUri());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_emptyName() {
        DocumentType docType = new DocumentType("", "-//W3C//DTD XHTML 1.0 Strict//EN",
                                                "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd",
                                                "http://example.com");
    }

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN",
                                                "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd",
                                                "http://example.com");

        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead_validInputs() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN",
                                                "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd",
                                                "http://example.com");

        StringBuilder expectedResult = new StringBuilder();
        expectedResult.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" ");
        expectedResult.append("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");

        StringBuilder actualResult = new StringBuilder();
        docType.outerHtmlHead(actualResult, 0, null);

        assertEquals(expectedResult.toString(), actualResult.toString());
    }

    @Test
    public void testOuterHtmlHead_withoutPublicId() {
        DocumentType docType = new DocumentType("html", null,
                                                "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd",
                                                "http://example.com");

        StringBuilder expectedResult = new StringBuilder();
        expectedResult.append("<!DOCTYPE html \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");

        StringBuilder actualResult = new StringBuilder();
        docType.outerHtmlHead(actualResult, 0, null);

        assertEquals(expectedResult.toString(), actualResult.toString());
    }

    @Test
    public void testOuterHtmlHead_withoutSystemId() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN",
                                                null,
                                                "http://example.com");

        StringBuilder expectedResult = new StringBuilder();
        expectedResult.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\">");

        StringBuilder actualResult = new StringBuilder();
        docType.outerHtmlHead(actualResult, 0, null);

        assertEquals(expectedResult.toString(), actualResult.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN",
                                                "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd",
                                                "http://example.com");

        StringBuilder actualResult = new StringBuilder();
        docType.outerHtmlTail(actualResult, 0, null);

        assertEquals("", actualResult.toString());
    }
}