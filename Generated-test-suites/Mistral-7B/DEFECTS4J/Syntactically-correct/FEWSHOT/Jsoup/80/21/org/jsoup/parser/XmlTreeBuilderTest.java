package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParseXml() {
        String xml = "<root><element attr=\"value\">Content<child></child></element></root>";
        Document document = XmlTreeBuilder.parse(new StringReader(xml), "https://www.example.com/");
        assertEquals("root", document.tagName());
        Element root = document.getElementsByTag("root").first();
        assertEquals(1, root.childElementCount());
        Element element = root.child(0);
        assertEquals("element", element.tagName());
        assertEquals(1, element.childElementCount());
        assertEquals("Content", element.child(0).text());
        assertEquals("value", element.attr("attr"));
    }

    @Test
    public void testParseEmptyXml() {
        Document document = XmlTreeBuilder.parse(new StringReader(""), "https://www.example.com/");
        assertNull(document);
    }

    @Test
    public void testParseXmlWithDeclaration() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><element attr=\"value\">Content<child></child></element></root>";
        Document document = XmlTreeBuilder.parse(new StringReader(xml), "https://www.example.com/");
        Element declaration = document.child(0);
        assertEquals("xml", declaration.tagName());
        assertEquals("1.0", declaration.attr("version"));
        assertEquals("UTF-8", declaration.attr("encoding"));
        assertEquals("root", document.getElementsByTag("root").first().tagName());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        String xml = "<root><img src=\"image.jpg\" /> </root>";
        Document document = XmlTreeBuilder.parse(new StringReader(xml), "https://www.example.com/");
        Element img = document.getElementsByTag("img").first();
        assertNotNull(img);
        assertEquals(0, img.childElementCount());
    }

    @Test
    public void testParseXmlWithComments() {
        String xml = "<root><!-- comment1 --><element attr=\"value\">Content<!-- comment2 --><child></child></element></root>";
        Document document = XmlTreeBuilder.parse(new StringReader(xml), "https://www.example.com/");
        Element root = document.getElementsByTag("root").first();
        assertEquals(1, root.childElementCount());
        Element element = root.child(0);
        assertEquals(1, element.childElementCount());
        assertEquals("Content", element.child(0).text());
        assertEquals("value", element.attr("attr"));
        assertEquals(2, document.getElementsByTag("comment").size());
    }

    @Test
    public void testParseXmlWithWhitespace() {
        String xml = "<root>\n <element attr=\"value\">Content\n <child></child>\n</element>\n</root>";
        Document document = XmlTreeBuilder.parse(new StringReader(xml), "https://www.example.com/");
        Element root = document.getElementsByTag("root").first();
        assertEquals(1, root.childElementCount());
        Element element = root.child(0);
        assertEquals(1, element.childElementCount());
        assertEquals("Content", element.child(0).text());
        assertEquals("value", element.attr("attr"));
    }

    @Test
    public void testParseXmlWithCDataSection() {
        String xml = "<root><![CDATA[Content of CData]]></root>";
        Document document = XmlTreeBuilder.parse(new StringReader(xml), "https://www.example.com/");
        Element root = document.getElementsByTag("root").first();
        Element cdata = root.child(0);
        assertEquals(1, cdata.childElementCount());
        assertNotNull(cdata.child(0));
        assertEquals("Content of CData", cdata.child(0).text());
    }

    @Test
    public void testParseXmlWithEmptyElements() {
        String xml = "<root><element attr=\"value\" /> </root>";
        Document document = XmlTreeBuilder.parse(new StringReader(xml), "https://www.example.com/");
        Element root = document.getElementsByTag("root").first();
        assertEquals(1, root.childElementCount());
        Element element = root.child(0);
        assertEquals(0, element.childElementCount());
        assertEquals("value", element.attr("attr"));
    }

    @Test
    public void testParseXmlWithNestedEmptyElements() {
        String xml = "<root><element attr=\"value\"><element attr=\"value\" /> </element></root>";
        Document document = XmlTreeBuilder.parse(new StringReader(xml), "https://www.example.com/");
        Element root = document.getElementsByTag("root").first();
        assertEquals(1, root.childElementCount());
        Element element = root.child(0);
        assertEquals(1, element.childElementCount());
        Element nestedElement = element.child(0);
        assertEquals(0, nestedElement.childElementCount());
        assertEquals("value", element.attr("attr"));
        assertEquals("value", nestedElement.attr("attr"));
    }

    @Test
    public void testParseXmlWithMixedContent() {
        String xml = "<root><element attr=\"value\">Content<child><![CDATA[CData]]></child></element></root>";
        Document document = XmlTreeBuilder.parse(new StringReader(xml), "https://www.example.com/");
        Element root = document.getElementsByTag("root").first();
        assertEquals(1, root.childElementCount());
        Element element = root.child(0);
        assertEquals(1, element.childElementCount());
        assertEquals("Content", element.child(0).text());
        Element child = element.child(0).child(0);
        assertEquals(1, child.childElementCount());
        assertNotNull(child.child(0));
        assertEquals("CData", child.tagName());
        assertEquals("value", element.attr("attr"));
    }

    @Test
    public void testParseXmlWithProcessingInstruction() {
        String xml = "<root<?xml-model href=\"model.xml\"?>><element attr=\"value\">Content</element></root>";
        Document document = XmlTreeBuilder.parse(new StringReader(xml), "https://www.example.com/");
        Element root = document.getElementsByTag("root").first();
        assertEquals(1, root.childElementCount());
        Element element = root.child(0);
        assertEquals(0, element.childElementCount());
        assertEquals("value", element.attr("attr"));
        assertNotNull(root.processingInstruction("xml-model"));
        assertEquals("model.xml", root.processingInstruction("xml-model").getData());
    }
}