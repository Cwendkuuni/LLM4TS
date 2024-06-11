package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlDeclarationTest {

    private XmlDeclaration xmlDeclaration;

    @Before
    public void setUp() {
        xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
    }

    @Test
    public void nodeName_shouldReturnDeclaration() {
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void name_shouldReturnGivenName() {
        assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void getWholeDeclaration_givenXmlWithVersionAndEncoding_shouldReturnValidDeclaration() {
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");

        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void getWholeDeclaration_givenXmlWithVersionOnly_shouldReturnValidDeclaration() {
        xmlDeclaration.attributes().put("version", "1.0");

        assertEquals("xml version=\"1.0\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void getWholeDeclaration_givenXmlWithEncodingOnly_shouldReturnValidDeclaration() {
        xmlDeclaration.attributes().put("encoding", "UTF-8");

        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void getWholeDeclaration_givenNonXmlDeclaration_shouldReturnName() {
        xmlDeclaration = new XmlDeclaration("declaration", "http://example.com", true);

        assertEquals("declaration", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void outerHtmlHead_givenProcessingInstruction_shouldReturnValidOuterHtml() {
        StringBuilder stringBuilder = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        xmlDeclaration.outerHtmlHead(stringBuilder, 0, outputSettings);

        assertEquals("<?xml?>", stringBuilder.toString());
    }

    @Test
    public void outerHtmlHead_givenDeclaration_shouldReturnValidOuterHtml() {
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        StringBuilder stringBuilder = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        xmlDeclaration.outerHtmlHead(stringBuilder, 0, outputSettings);

        assertEquals("<!xml version=\"1.0\" encoding=\"UTF-8\">", stringBuilder.toString());
    }

    @Test
    public void outerHtmlTail_shouldDoNothing() {
        StringBuilder stringBuilder = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        xmlDeclaration.outerHtmlTail(stringBuilder, 0, outputSettings);

        assertEquals("", stringBuilder.toString());
    }

    @Test
    public void toString_shouldReturnOuterHtml() {
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");

        assertEquals("<!xml version=\"1.0\" encoding=\"UTF-8\">", xmlDeclaration.toString());
    }
}