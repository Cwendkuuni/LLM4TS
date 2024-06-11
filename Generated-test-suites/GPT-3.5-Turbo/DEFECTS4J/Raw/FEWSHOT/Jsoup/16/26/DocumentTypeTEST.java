
package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType doctype = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE html PUBLIC \"publicId\" systemId\">");
        StringBuilder actual = new StringBuilder();
        doctype.outerHtmlHead(actual, 0, new Document.OutputSettings());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        StringBuilder expected = new StringBuilder();
        StringBuilder actual = new StringBuilder();
        doctype.outerHtmlTail(actual, 0, new Document.OutputSettings());
        assertEquals(expected.toString(), actual.toString());
    }
}
