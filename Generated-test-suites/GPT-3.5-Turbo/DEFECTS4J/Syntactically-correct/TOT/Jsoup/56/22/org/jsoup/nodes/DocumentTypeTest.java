
package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", "http://example.com");
    }

    @Test
    public void testName() {
        Assert.assertEquals("html", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setSyntax(Syntax.html);
        try {
            documentType.outerHtmlHead(sb, 0, outputSettings);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        Assert.assertEquals("<!doctype", sb.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() {
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        documentType.attr(DocumentType.PUBLIC_ID, "TEST PUBLIC ID");
        try {
            documentType.outerHtmlHead(sb, 0, outputSettings);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        Assert.assertEquals("<!DOCTYPE html PUBLIC \"TEST PUBLIC ID\">", sb.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemId() {
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        documentType.attr(DocumentType.SYSTEM_ID, "TEST SYSTEM ID");
        try {
            documentType.outerHtmlHead(sb, 0, outputSettings);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        Assert.assertEquals("<!DOCTYPE html \"TEST SYSTEM ID\">", sb.toString());
    }

    @Test
    public void testOuterHtmlHeadWithBothPublicIdAndSystemId() {
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        documentType.attr(DocumentType.PUBLIC_ID, "TEST PUBLIC ID");
        documentType.attr(DocumentType.SYSTEM_ID, "TEST SYSTEM ID");
        try {
            documentType.outerHtmlHead(sb, 0, outputSettings);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        Assert.assertEquals("<!DOCTYPE html PUBLIC \"TEST PUBLIC ID\" \"TEST SYSTEM ID\">", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        documentType.outerHtmlTail(sb, 0, outputSettings);
        Assert.assertEquals("", sb.toString());
    }

    @Test
    public void testHasWithBlankAttribute() {
        Assert.assertFalse(documentType.has(DocumentType.NAME));
    }

    @Test
    public void testHasWithFilledAttribute() {
        documentType.attr(DocumentType.NAME, "TEST NAME");
        Assert.assertTrue(documentType.has(DocumentType.NAME));
    }
}
