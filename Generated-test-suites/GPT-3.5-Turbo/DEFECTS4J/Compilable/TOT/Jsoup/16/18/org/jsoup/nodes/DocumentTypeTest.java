package org.jsoup.nodes;
import org.jsoup.nodes.DocumentType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setup() {
        documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlHead(actual, 0, null);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testOuterHtmlHeadWithoutPublicId() {
        documentType.attr("publicId", "");
        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE html \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlHead(actual, 0, null);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testOuterHtmlHeadWithoutSystemId() {
        documentType.attr("systemId", "");
        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\">");
        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlHead(actual, 0, null);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testOuterHtmlHeadWithoutPublicIdAndSystemId() {
        documentType.attr("publicId", "");
        documentType.attr("systemId", "");
        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE html>");
        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlHead(actual, 0, null);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder expected = new StringBuilder();
        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlTail(actual, 0, null);
        assertEquals(expected.toString(), actual.toString());
    }
}