package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationWithNameAndBaseUri() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration(" Declaration", "baseUri", false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(" Declaration", xmlDeclaration.name());
        assertEquals("baseUri", xmlDeclaration.baseUri());
        assertEquals("Declaration", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndProcessingInstructionTrue() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration(" Declaration", "baseUri", true);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(" Declaration", xmlDeclaration.name());
        assertEquals("baseUri", xmlDeclaration.baseUri());
        assertEquals("!Declaration", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration(" xml", "baseUri", false);
        xmlDeclaration.attributes().put("version", "1.1");
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("baseUri", xmlDeclaration.baseUri());
        assertEquals("<declaration version=\"1.1\" encoding=\"UTF-8\"/>", xmlDeclaration.getWholeDeclaration());
    }

    @Test(expected = NullPointerException.class)
    public void testXmlDeclarationWithNullName() {
        new XmlDeclaration(null, "baseUri", false);
    }

    @Test
    public void testXmlDeclarationWithEmptyName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("", "baseUri", false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("", xmlDeclaration.name());
        assertEquals("baseUri", xmlDeclaration.baseUri());
        assertEquals("", xmlDeclaration.getWholeDeclaration());
    }
}