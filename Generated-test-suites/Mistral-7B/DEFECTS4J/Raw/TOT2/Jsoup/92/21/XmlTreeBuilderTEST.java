package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("", "");
        Assert.assertEquals(0, document.childNodes().size());
        Assert.assertNull(document.body());
    }

    @Test
    public void testParseSimpleXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root><child>Hello World</child></root>";
        Document document = xmlTreeBuilder.parse(xml, "");
        Element root = document.elementSibling(0);
        Element child = root.child(0);
        Assert.assertEquals("Hello World", child.text());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root><img src=\"image.png\" /></root>";
        Document document = xmlTreeBuilder.parse(xml, "");
        Element img = document.getElementsByTag("img").get(0);
        Assert.assertTrue(img.isSelfClosing());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root><child><"; // invalid XML
        Document document = xmlTreeBuilder.parse(xml, "");
        // this test should fail with an IllegalArgumentException
    }
}