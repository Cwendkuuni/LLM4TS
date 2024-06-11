package org.jsoup.nodes;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/TR/html4/");

        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHeadWithBlankPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("HTML", "", "", "");

        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE html>");

        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlHead(actual, 0, null);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdOnly() {
        DocumentType documentType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01//EN", "", "");

        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">");

        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlHead(actual, 0, null);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemIdOnly() {
        DocumentType documentType = new DocumentType("HTML", "", "http://www.w3.org/TR/html4/strict.dtd", "");

        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE html http://www.w3.org/TR/html4/strict.dtd\">");

        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlHead(actual, 0, null);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "");

        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" http://www.w3.org/TR/html4/strict.dtd\">");

        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlHead(actual, 0, null);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("HTML", "", "", "");

        StringBuilder expected = new StringBuilder();
        // No tail for DocumentType

        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlTail(actual, 0, null);

        assertEquals(expected.toString(), actual.toString());
    }

}