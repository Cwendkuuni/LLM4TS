
package org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.helper.StringUtil;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("publicId", documentType.attr("publicId"));
        assertEquals("systemId", documentType.attr("systemId"));
        assertEquals("baseUri", documentType.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE htmlPUBLIC \"publicId\" systemId=\"systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlTail(accum, 0, out);
        assertEquals("", accum.toString());
    }

    @Test
    public void testNullDocumentTypeNodeName() {
        assertNull(new DocumentType("name", "publicId", "systemId", "baseUri").nodeName());
    }

    @Test(expected = NullPointerException.class)
    public void testDocumentTypeConstructorNullName() {
        try {
            new DocumentType(null, "publicId", "systemId", "baseUri");
            fail();
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(expected = NullPointerException.class)
    public void testDocumentTypeConstructorNullPublicId() {
        try {
            new DocumentType("name", null, "systemId", "baseUri");
            fail();
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(expected = NullPointerException.class)
    public void testDocumentTypeConstructorNullSystemId() {
        try {
            new DocumentType("name", "publicId", null, "baseUri");
            fail();
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(expected = NullPointerException.class)
    public void testDocumentTypeConstructorNullBaseUri() {
        try {
            new DocumentType("name", "publicId", "systemId", null);
            fail();
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeConstructorInvalidBaseUri() {
        try {
            new DocumentType("name", "publicId", "systemId", "/\\\\/.*");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(expected = NullPointerException.class)
    public void testOuterHtmlHeadNullAccumulator() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder accum = null;
        documentType.outerHtmlHead(accum, 0, null);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testOuterHtmlHeadNullOut() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        Document.OutputSettings out = null;
        documentType.outerHtmlHead(null, 0, out);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testOuterHtmlTailNullDocumentType() {
        assertNull(new DocumentType("name", "publicId", "systemId", "baseUri").outerHtmlTail(null, 0, null));
    }

    @Test(expected = NullPointerException.class)
    public void testOuterHtmlTailNullAccumulator() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder accum = null;
        documentType.outerHtmlTail(accum, 0, null);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testOuterHtmlTailNullOut() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        Document.OutputSettings out = null;
        documentType.outerHtmlTail(null, 0, out);
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testOuterHtmlWithNullOutputSettings() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = null;
        documentType.outerHtml(accum, 0, out);
        fail();
    }
}