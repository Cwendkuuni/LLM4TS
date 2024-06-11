package org.jsoup.parser; // Make sure to import necessary packages

import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        String xml = "< root/>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader(xml), "baseUrl");
        assertNotNull(document);
        assertTrue(document.childNodes().isEmpty());
    }

    @Test
    public void testParseSimpleXml() {
        String xml = "< root>< child></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader(xml), "baseUrl");
        Element root = document.rootElement();
        Element child = root.child(0);
        assertEquals("child", child.tagName());
        assertEquals("baseUrl", root.baseUri());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        String xml = "< root>< img src='image.png'/></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader(xml), "baseUrl");
        Element img = document.select("img").first();
        assertTrue(img.isSelfClosing());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidXml() {
        String xml = "< root>< child>< invalidTag/></child>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader(xml), "baseUrl"); // This should throw an exception
    }
}