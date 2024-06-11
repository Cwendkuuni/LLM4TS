package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Document;
import org.jsoup.helper.Validate;

import java.io.IOException;

/**
 * Test class for XmlDeclaration.
 */
public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndProcessingInstructionFalse() {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        String expectedName = "#declaration";
        String expectedDeclaration = "xml version=\"\" encoding=\"\"";

        assertEquals(expectedName, xmlDeclaration.nodeName());
        assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndProcessingInstructionTrue() {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = true;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        String expectedName = "#declaration";
        String expectedDeclaration = "<?xml version=\"\" encoding=\"\"?>";

        assertEquals(expectedName, xmlDeclaration.nodeName());
        assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
    }

    @Test(expected = NullPointerException.class)
    public void testXmlDeclarationWithNullName() {
        String baseUri = "baseUri";
        boolean isProcessingInstruction = false;
        String name = null;

        try {
            new XmlDeclaration(name, baseUri, isProcessingInstruction);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testXmlDeclarationWithNameAndAttributes() {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        xmlDeclaration.attributes().set("version", "1.0");
        xmlDeclaration.attributes().set("encoding", "UTF-8");

        String expectedDeclaration = "xml version=\"1.0\" encoding=\"UTF-8\"";

        assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationToString() {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        String expectedDeclaration = "<?xml version=\"\" encoding=\"\"?>";

        assertEquals(expectedDeclaration, xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationOuterHtmlHead() throws IOException {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        Document document = new Document();
        Appendable accum = document.outputSettings().getTarget();
        xmlDeclaration.outerHtmlHead(accum, 0, document.outputSettings());

        String expectedDeclaration = "<?xml version=\"\" encoding=\"\"?>";

        assertEquals(expectedDeclaration, accum.toString());
    }

    @Test
    public void testXmlDeclarationOuterHtmlTail() throws IOException {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        Document document = new Document();
        Appendable accum = document.outputSettings().getTarget();
        xmlDeclaration.outerHtmlTail(accum, 0, document.outputSettings());

        assertEquals("", accum.toString());
    }
}