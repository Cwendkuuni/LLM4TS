package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.helper.Validate;

import java.io.IOException;

import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationCreation() {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = true;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        assertNotNull(xmlDeclaration);
        assertEquals(name, xmlDeclaration.name());
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(baseUri, xmlDeclaration.baseUri());
        assertEquals(isProcessingInstruction, xmlDeclaration.isProcessingInstruction());
    }

    @Test(expected = NullPointerException.class)
    public void testXmlDeclarationCreationWithNullName() {
        String baseUri = "baseUri";
        boolean isProcessingInstruction = true;

        new XmlDeclaration(null, baseUri, isProcessingInstruction);
    }

    @Test
    public void testGetWholeDeclaration() {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = true;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        // Default case
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());

        // Version attribute
        xmlDeclaration.attributes().put("version", "1.0");
        assertEquals("xml version=\"1.0\"", xmlDeclaration.getWholeDeclaration());

        // Encoding attribute
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());

        // Both attributes
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());

        // No attributes
        xmlDeclaration.attributes().clear();
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = true;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        Appendable accum = new StringBuilder();
        xmlDeclaration.outerHtmlHead(accum, 0, null);

        assertEquals("<?xml", accum.toString());

        // Version attribute
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.outerHtmlHead(accum, 0, null);
        assertEquals("<?xml version=\"1.0\"", accum.toString());

        // Encoding attribute
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        xmlDeclaration.outerHtmlHead(accum, 0, null);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"", accum.toString());

        // Both attributes
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        xmlDeclaration.outerHtmlHead(accum, 0, null);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"", accum.toString());
    }

    @Test
    public void testToString() {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = true;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        assertEquals(xmlDeclaration.outerHtml(), xmlDeclaration.toString());
    }

    @Test
    public void testCreateXmlDeclarationFromString() throws IOException {
        String xmlDeclarationString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

        XmlDeclaration xmlDeclaration = XmlDeclaration.fromXml(xmlDeclarationString);
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("1.0", xmlDeclaration.attributes().get("version"));
        assertEquals("UTF-8", xmlDeclaration.attributes().get("encoding"));
        assertEquals(true, xmlDeclaration.isProcessingInstruction());
    }

    @Test(expected = IOException.class)
    public void testCreateXmlDeclarationFromInvalidString() throws IOException {
        String xmlDeclarationString = "<?xml version=\"1.0\" encoding=\"UTF-8\"<error";

        XmlDeclaration.fromXml(xmlDeclarationString);
    }

    @Test
    public void testXmlDeclarationWithElementChild() {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = true;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        Element element = new Element("root");
        xmlDeclaration.addChild(element);

        StringBuilder expectedXml = new StringBuilder();
        expectedXml.append("<?xml");
        if (isProcessingInstruction) {
            expectedXml.append("!");
        }
        expectedXml.append(" version=\"1.0\" encoding=\"UTF-8\"?>");
        expectedXml.append("<root></root>");

        StringBuilder actualXml = new StringBuilder();
        xmlDeclaration.outerHtml(actualXml);

        assertEquals(expectedXml.toString(), actualXml.toString());
    }
}