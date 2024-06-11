package org.jsoup.nodes;

import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("declaration", "", true);
        Assert.assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("declaration", "", true);
        Assert.assertEquals("declaration", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclarationWithoutAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("declaration", "", true);
        Assert.assertEquals("declaration", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationWithAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", true);
        xmlDeclaration.attributes.put("version", "1.0");
        xmlDeclaration.attributes.put("encoding", "UTF-8");
        Assert.assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHeadWithProcessingInstruction() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", true);
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            xmlDeclaration.outerHtmlHead(sb, 0, outputSettings);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertEquals("<!xml>", sb.toString());
    }

    @Test
    public void testOuterHtmlHeadWithDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", false);
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            xmlDeclaration.outerHtmlHead(sb, 0, outputSettings);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertEquals("<?xml>", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", true);
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            xmlDeclaration.outerHtmlTail(sb, 0, outputSettings);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("declaration", "", true);
        Assert.assertEquals("<!declaration>", xmlDeclaration.toString());
    }
}
