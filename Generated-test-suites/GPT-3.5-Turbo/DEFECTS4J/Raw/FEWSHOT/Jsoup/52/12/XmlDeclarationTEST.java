package org.jsoup.nodes;

import org.jsoup.helper.Validate;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class XmlDeclarationTest {
    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", true);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", true);
        assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration xmlDeclaration1 = new XmlDeclaration("xml", "http://www.example.com", true);
        assertEquals("xml", xmlDeclaration1.getWholeDeclaration());

        XmlDeclaration xmlDeclaration2 = new XmlDeclaration("xml", "http://www.example.com", true);
        xmlDeclaration2.attributes.put("version", "1.0");
        assertEquals("xml version=\"1.0\"", xmlDeclaration2.getWholeDeclaration());

        XmlDeclaration xmlDeclaration3 = new XmlDeclaration("xml", "http://www.example.com", true);
        xmlDeclaration3.attributes.put("encoding", "UTF-8");
        assertEquals("xml encoding=\"UTF-8\"", xmlDeclaration3.getWholeDeclaration());

        XmlDeclaration xmlDeclaration4 = new XmlDeclaration("xml", "http://www.example.com", true);
        xmlDeclaration4.attributes.put("version", "1.0");
        xmlDeclaration4.attributes.put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration4.getWholeDeclaration());

        XmlDeclaration xmlDeclaration5 = new XmlDeclaration("declaration", "http://www.example.com", true);
        xmlDeclaration5.attributes.put("version", "1.0");
        xmlDeclaration5.attributes.put("encoding", "UTF-8");
        assertEquals("declaration", xmlDeclaration5.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Appendable accum = new StringBuilder();
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", true);
        xmlDeclaration.outerHtmlHead(accum, 0, outputSettings);
        assertEquals("<!xml>", accum.toString());

        accum = new StringBuilder();
        XmlDeclaration xmlDeclaration2 = new XmlDeclaration("declaration", "http://www.example.com", false);
        xmlDeclaration2.outerHtmlHead(accum, 0, outputSettings);
        assertEquals("<?declaration>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Appendable accum = new StringBuilder();
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", true);
        xmlDeclaration.outerHtmlTail(accum, 0, outputSettings);
        assertEquals("", accum.toString());

        accum = new StringBuilder();
        XmlDeclaration xmlDeclaration2 = new XmlDeclaration("declaration", "http://www.example.com", false);
        xmlDeclaration2.outerHtmlTail(accum, 0, outputSettings);
        assertEquals("", accum.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", true);
        assertEquals("<!xml>", xmlDeclaration.toString());
    }
}