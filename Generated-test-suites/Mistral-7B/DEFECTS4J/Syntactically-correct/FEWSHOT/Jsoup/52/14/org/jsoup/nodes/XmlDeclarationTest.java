package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationWithName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("<xml>\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameAndVersion() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attrs().put("version", "1.0");
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml version=\"1.0\"\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameAndEncoding() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attrs().put("encoding", "UTF-8");
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml encoding=\"UTF-8\"\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameAndVersionAndEncoding() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attrs().put("version", "1.0");
        xmlDeclaration.attrs().put("encoding", "UTF-8");
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationAsProcessingInstruction() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, true);
        assertEquals("#processing-instruction", xmlDeclaration.nodeName());
        assertEquals("<?xml>\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameAndProcessingInstruction() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, true);
        xmlDeclaration.attrs().put("version", "1.0");
        assertEquals("#processing-instruction", xmlDeclaration.nodeName());
        assertEquals("<?xml version=\"1.0\"?>\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameAndVersionAndEncodingAndData() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attrs().put("version", "1.0");
        xmlDeclaration.attrs().put("encoding", "UTF-8");
        xmlDeclaration.text(new TextNode("Data"));
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\">Data\n</xml>", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNullName() {
        try {
            new XmlDeclaration(null, null, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}