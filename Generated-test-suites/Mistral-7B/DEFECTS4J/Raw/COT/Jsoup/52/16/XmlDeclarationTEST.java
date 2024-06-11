package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;
import org.jsoup.helper.Constants;

import java.io.IOException;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationWithName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void testXmlDeclarationWithNameAndAttributes() {
        Attributes attrs = new Attributes();
        attrs.put("version", "1.0");
        attrs.put("encoding", "UTF-8");
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false, attrs);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationAsProcessingInstruction() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("<?xml", null, true);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("<?xml", xmlDeclaration.name());
        assertEquals("<?xml", xmlDeclaration.getWholeDeclaration());
    }

    @Test(expected = NullPointerException.class)
    public void testNullNameException() {
        new XmlDeclaration(null, null, false);
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUriException() {
        new XmlDeclaration("xml", null, false);
    }

    @Test
    public void testGetWholeDeclarationWithNoAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationWithAttributes() {
        Attributes attrs = new Attributes();
        attrs.put("version", "1.0");
        attrs.put("encoding", "UTF-8");
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false, attrs);
        assertEquals("xml version=\"1.0\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        Element element = new Element("root");
        xmlDeclaration.outerHtmlHead(element.outputContent(Constants.UTF8), 0, null);
        xmlDeclaration.outerHtmlTail(element.outputContent(Constants.UTF8), 0, null);
        assertEquals("<xml/>", element.html());
    }

    @Test
    public void testOuterHtmlHeadAndTail() throws IOException {
        Appendable accum = new StringBuilder();
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.outerHtmlHead(accum, 0, null);
        xmlDeclaration.outerHtmlTail(accum, 0, null);
        assertEquals("<xml/>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithProcessingInstruction() throws IOException {
        Appendable accum = new StringBuilder();
        XmlDeclaration xmlDeclaration = new XmlDeclaration("<?xml", null, true);
        xmlDeclaration.outerHtmlHead(accum, 0, null);
        xmlDeclaration.outerHtmlTail(accum, 0, null);
        assertEquals("<?xml>", accum.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidXmlDeclarationName() {
        new XmlDeclaration("invalidXmlDeclaration", null, false);
    }
}