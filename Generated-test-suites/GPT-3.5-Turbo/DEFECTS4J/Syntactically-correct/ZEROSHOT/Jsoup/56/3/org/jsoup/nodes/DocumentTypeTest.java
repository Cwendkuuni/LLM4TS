import org.jsoup.nodes.DocumentType;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");

        // Test for Syntax.html, has publicId and systemId
        StringBuilder expectedHtml1 = new StringBuilder("<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">");
        Document.OutputSettings outputSettings1 = new Document.OutputSettings(Document.OutputSettings.Syntax.html);
        StringBuilder actualHtml1 = new StringBuilder();
        try {
            documentType.outerHtmlHead(actualHtml1, 0, outputSettings1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expectedHtml1.toString(), actualHtml1.toString());

        // Test for Syntax.html, no publicId and systemId
        StringBuilder expectedHtml2 = new StringBuilder("<!doctype name>");
        Document.OutputSettings outputSettings2 = new Document.OutputSettings(Document.OutputSettings.Syntax.html);
        StringBuilder actualHtml2 = new StringBuilder();
        try {
            documentType.outerHtmlHead(actualHtml2, 0, outputSettings2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expectedHtml2.toString(), actualHtml2.toString());

        // Test for Syntax.xml, has publicId and systemId
        StringBuilder expectedHtml3 = new StringBuilder("<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">");
        Document.OutputSettings outputSettings3 = new Document.OutputSettings(Document.OutputSettings.Syntax.xml);
        StringBuilder actualHtml3 = new StringBuilder();
        try {
            documentType.outerHtmlHead(actualHtml3, 0, outputSettings3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expectedHtml3.toString(), actualHtml3.toString());

        // Test for Syntax.xml, no publicId and systemId
        StringBuilder expectedHtml4 = new StringBuilder("<!DOCTYPE name>");
        Document.OutputSettings outputSettings4 = new Document.OutputSettings(Document.OutputSettings.Syntax.xml);
        StringBuilder actualHtml4 = new StringBuilder();
        try {
            documentType.outerHtmlHead(actualHtml4, 0, outputSettings4);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expectedHtml4.toString(), actualHtml4.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        String expected = "";

        try {
            documentType.outerHtmlTail(accum, 0, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testHas() {
        DocumentType documentType1 = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertTrue(documentType1.has("name"));
        assertTrue(documentType1.has("publicId"));
        assertTrue(documentType1.has("systemId"));
        assertFalse(documentType1.has("foo"));

        DocumentType documentType2 = new DocumentType("", "", "", "baseUri");
        assertFalse(documentType2.has("name"));
        assertFalse(documentType2.has("publicId"));
        assertFalse(documentType2.has("systemId"));
        assertFalse(documentType2.has("foo"));
    }
}