package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndIsProcessingInstructionFalse() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("<?xml>", xmlDeclaration.getWholeDeclaration());
        assertEquals("<xml>\n", xmlDeclaration.outerHtml(null, -1, null));
        assertEquals("<?xml>\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndIsProcessingInstructionTrue() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = true;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("<?xml>", xmlDeclaration.getWholeDeclaration());
        assertEquals("<?xml>\n", xmlDeclaration.outerHtml(null, -1, null));
        assertEquals("<?xml>\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithVersionAndBaseUriAndIsProcessingInstructionFalse() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        String version = "1.0";

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction, "version", version);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("<?xml version=\"1.0\"?>", xmlDeclaration.getWholeDeclaration());
        assertEquals("<xml version=\"1.0\">\n", xmlDeclaration.outerHtml(null, -1, null));
        assertEquals("<?xml version=\"1.0\"?>\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithEncodingAndBaseUriAndIsProcessingInstructionFalse() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        String encoding = "UTF-8";

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction, "encoding", encoding);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("<?xml encoding=\"UTF-8\"?>", xmlDeclaration.getWholeDeclaration());
        assertEquals("<xml encoding=\"UTF-8\">\n", xmlDeclaration.outerHtml(null, -1, null));
        assertEquals("<?xml encoding=\"UTF-8\"?>\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithVersionAndEncodingAndBaseUriAndIsProcessingInstructionFalse() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        String version = "1.0";
        String encoding = "UTF-8";

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction, "version", version, "encoding", encoding);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getWholeDeclaration());
        assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\">\n", xmlDeclaration.outerHtml(null, -1, null));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", xmlDeclaration.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testXmlDeclarationWithNullName() {
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(null, baseUri, isProcessingInstruction);
    }

    @Test(expected = NullPointerException.class)
    public void testXmlDeclarationWithNullBaseUri() {
        String name = "xml";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, null, isProcessingInstruction);
    }

    @Test
    public void testXmlDeclarationWithEmptyName() {
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration("", baseUri, isProcessingInstruction);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("", xmlDeclaration.name());
        assertEquals("<?>\n", xmlDeclaration.getWholeDeclaration());
        assertEquals("<?>\n", xmlDeclaration.outerHtml(null, -1, null));
        assertEquals("<?>\n", xmlDeclaration.toString());
    }
}