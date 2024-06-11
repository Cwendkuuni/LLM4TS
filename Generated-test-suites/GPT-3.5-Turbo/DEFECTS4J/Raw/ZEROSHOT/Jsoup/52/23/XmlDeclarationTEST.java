package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("test", "http://example.com", false);
        String nodeName = declaration.nodeName();
        Assert.assertEquals("#declaration", nodeName);
    }

    @Test
    public void testName() {
        XmlDeclaration declaration = new XmlDeclaration("test", "http://example.com", false);
        String name = declaration.name();
        Assert.assertEquals("test", name);
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        String wholeDeclaration = declaration.getWholeDeclaration();
        Assert.assertEquals("xml", wholeDeclaration);

        declaration = new XmlDeclaration("test", "http://example.com", false);
        wholeDeclaration = declaration.getWholeDeclaration();
        Assert.assertEquals("test", wholeDeclaration);
    }

    @Test
    public void testOuterHtmlHead() throws Exception {
        XmlDeclaration declaration = new XmlDeclaration("test", "http://example.com", false);
        StringBuilder sb = new StringBuilder();
        declaration.outerHtmlHead(sb, 0, new Document.OutputSettings());
        Assert.assertEquals("<?test>", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() throws Exception {
        XmlDeclaration declaration = new XmlDeclaration("test", "http://example.com", false);
        StringBuilder sb = new StringBuilder();
        declaration.outerHtmlTail(sb, 0, new Document.OutputSettings());
        Assert.assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("test", "http://example.com", false);
        String toString = declaration.toString();
        Assert.assertEquals("<?test>", toString);
    }
}