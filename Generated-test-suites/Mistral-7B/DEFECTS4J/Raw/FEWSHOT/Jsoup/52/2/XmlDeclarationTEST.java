package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationWithNameAndUriAndProcessingInstructionFalse() {
        String baseUri = "http://example.com/";
        String name = "xml";
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertNull(xmlDeclaration.name());
        assertEquals(baseUri, xmlDeclaration.baseUri());
        assertEquals(false, xmlDeclaration.isProcessingInstruction());
    }

    @Test
    public void testXmlDeclarationWithNameAndUriAndProcessingInstructionTrue() {
        String baseUri = "http://example.com/";
        String name = "xml";
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, true);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertNull(xmlDeclaration.name());
        assertEquals(baseUri, xmlDeclaration.baseUri());
        assertEquals(true, xmlDeclaration.isProcessingInstruction());
    }

    @Test
    public void testXmlDeclarationWithNameAndAttributes() {
        String baseUri = "http://example.com/";
        String name = "xml";
        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        attributes.put("encoding", "UTF-8");
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false, attributes);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals(baseUri, xmlDeclaration.baseUri());
        assertEquals(false, xmlDeclaration.isProcessingInstruction());
        assertEquals("version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationWithEmptyAttributes() {
        String baseUri = "http://example.com/";
        String name = "xml";
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false);
        assertEquals(name, xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationWithVersionAttribute() {
        String baseUri = "http://example.com/";
        String name = "xml";
        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false, attributes);
        assertEquals("xml version=\"1.0\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationWithEncodingAttribute() {
        String baseUri = "http://example.com/";
        String name = "xml";
        Attributes attributes = new Attributes();
        attributes.put("encoding", "UTF-8");
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false, attributes);
        assertEquals("xml encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationWithVersionAndEncodingAttributes() {
        String baseUri = "http://example.com/";
        String name = "xml";
        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        attributes.put("encoding", "UTF-8");
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false, attributes);
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationToString() throws IOException {
        String baseUri = "http://example.com/";
        String name = "xml";
        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        attributes.put("encoding", "UTF-8");
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false, attributes);
        StringBuilder expected = new StringBuilder();
        expected.append("<?").append(xmlDeclaration.isProcessingInstruction ? "!" : "?")
                .append(xmlDeclaration.getWholeDeclaration())
                .append(">");
        assertEquals(expected.toString(), xmlDeclaration.toString());
    }
}