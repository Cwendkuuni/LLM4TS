package org.jsoup.parser;

import org.junit.Assert;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("", "");
        Assert.assertNotNull(document);
        Assert.assertEquals(0, document.childNodes().size());
    }

    @Test
    public void testParseRootElement() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("<root/>", "");
        Assert.assertNotNull(document);
        Element root = document.childNodes().get(0);
        Assert.assertEquals("root", root.nodeName());
        Assert.assertEquals(0, root.childNodes().size());
    }

    @Test
    public void testParseElementWithAttributes() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("<element attr1='value1' attr2='value2'/>", "");
        Element element = document.childNodes().get(0);
        Assert.assertEquals("element", element.nodeName());
        Assert.assertEquals("value1", element.attr("attr1"));
        Assert.assertEquals("value2", element.attr("attr2"));
        Assert.assertEquals(0, element.childNodes().size());
    }

    @Test
    public void testParseElementWithTextContent() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("<element>Content</element>", "");
        Element element = document.childNodes().get(0);
        Assert.assertEquals("element", element.nodeName());
        Assert.assertEquals("Content", element.text());
        Assert.assertEquals(0, element.childNodes().size());
    }

    @Test
    public void testParseElementWithNestedElements() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("<root><element><subElement/></element></root>", "");
        Element root = document.childNodes().get(0);
        Element element = root.childNodes().get(0);
        Element subElement = element.childNodes().get(0);
        Assert.assertEquals("root", root.nodeName());
        Assert.assertEquals("element", element.nodeName());
        Assert.assertEquals(1, element.childNodes().size());
        Assert.assertEquals("subElement", subElement.nodeName());
        Assert.assertEquals(0, subElement.childNodes().size());
    }

    @Test
    public void testParseComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("<!-- This is a comment -->", "");
        List<Node> commentNodes = document.childNodes();
        Assert.assertEquals(1, commentNodes.size());
        Comment comment = (Comment) commentNodes.get(0);
        Assert.assertEquals("This is a comment", comment.data());
    }

    @Test
    public void testParseCDataSection() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("<![CDATA[This is a CDATA section]]>", "");
        List<Node> cdataNodes = document.childNodes();
        Assert.assertEquals(1, cdataNodes.size());
        CDataNode cdata = (CDataNode) cdataNodes.get(0);
        Assert.assertEquals("This is a CDATA section", cdata.text());
    }

    @Test
    public void testParseDoctypeDeclaration() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("<!DOCTYPE html SYSTEM \"about:legacy-compat\">", "");
        List<Node> doctypeNodes = document.childNodes();
        Assert.assertEquals(1, doctypeNodes.size());
        DocumentType doctype = (DocumentType) doctypeNodes.get(0);
        Assert.assertEquals("html", doctype.nodeName());
        Assert.assertEquals("about:legacy-compat", doctype.systemIdentifier());
    }

    @Test
    public void testParseEmptyElement() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("<empty/>", "");
        Element emptyElement = document.childNodes().get(0);
        Assert.assertEquals("empty", emptyElement.nodeName());
        Assert.assertEquals(0, emptyElement.childNodes().size());
    }

    @Test
    public void testParseSelfClosingTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("<img src='image.jpg'/>", "");
        Element imgElement = document.childNodes().get(0);
        Assert.assertEquals("img", imgElement.nodeName());
        Assert.assertEquals("image.jpg", imgElement.attr("src"));
        Assert.assertEquals(0, imgElement.childNodes().size());
        Assert.assertFalse(imgElement.hasChildNodes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidElement() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("<invalid>", "");
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document fragmentDocument = xmlTreeBuilder.parse("<root><element>Content</element></root>", "");
        List<Node> fragmentNodes = xmlTreeBuilder.parseFragment("<root><element>Content</element></root>", "", "").childNodes();
        Assert.assertEquals(fragmentNodes.size(), 3);
        Element root = (Element) fragmentNodes.get(0);
        Element element = (Element) fragmentNodes.get(1);
        TextNode content = (TextNode) fragmentNodes.get(2);
        Assert.assertEquals("root", root.nodeName());
        Assert.assertEquals("element", element.nodeName());
        Assert.assertEquals("Content", content.text());
    }

    @Test
    public void testParseFragmentWithContext() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document baseDocument = xmlTreeBuilder.parse("<html><body><root/></body></html>", "");
        Element root = baseDocument.selectFirst("root");
        List<Node> fragmentNodes = xmlTreeBuilder.parseFragment("<root><element>Content</element></root>", root, "", "").childNodes();
        Assert.assertEquals(fragmentNodes.size(), 3);
        Element fragmentRoot = (Element) fragmentNodes.get(0);
        Element fragmentElement = (Element) fragmentNodes.get(1);
        TextNode fragmentContent = (TextNode) fragmentNodes.get(2);
        Assert.assertEquals("root", fragmentRoot.nodeName());
        Assert.assertEquals("element", fragmentElement.nodeName());
        Assert.assertEquals("Content", fragmentContent.text());
        Assert.assertEquals(root, fragmentRoot.parent());
    }

    @Test
    public void testParseCharacterEntities() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("<root>&lt; &gt; &amp; &apos; &quot;</root>", "");
        List<Node> rootNodes = document.childNodes();
        Assert.assertEquals(1, rootNodes.size());
        Element root = (Element) rootNodes.get(0);
        List<Node> contentNodes = root.childNodes();
        Assert.assertEquals(1, contentNodes.size());
        TextNode content = (TextNode) contentNodes.get(0);
        Assert.assertEquals("&lt; &gt; &amp; &apos; &quot;", content.text());
        Assert.assertEquals("<", content.text().codePointAt(0));
        Assert.assertEquals(">", content.text().codePointAt(1));
        Assert.assertEquals("&", content.text().codePointAt(2));
        Assert.assertEquals('&', content.text().charAt(3));
        Assert.assertEquals("'", content.text().charAt(4));
        Assert.assertEquals('\'', content.text().codePointAt(4));
        Assert.assertEquals("\"", content.text().charAt(5));
        Assert.assertEquals("\"", content.text().codePointAt(5));
    }
}