package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.AttributeToken;
import org.jsoup.parser.Token.CharacterToken;
import org.jsoup.parser.Token.CommentToken;
import org.jsoup.parser.Token.DoctypeToken;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder builder;

    @Before
    public void setup() {
        builder = new XmlTreeBuilder();
    }

    @Test
    public void testParseXmlDocument() {
        String xmlString = "<root><child attr=\"value\">Content</child></root>";
        Document parsedXml = builder.parse(new StringReader(xmlString));

        assertEquals("root", parsedXml.tagName());
        assertEquals(1, parsedXml.childNodeSize());

        Element root = parsedXml.child(0);
        assertEquals("child", root.tagName());
        assertEquals(1, root.childNodeSize());

        Element child = root.child(0);
        assertEquals("value", child.attr("attr"));
        assertEquals("Content", child.text());
    }

    @Test
    public void testParseXmlFragment() {
        String xmlFragment = "<child attr=\"value\">Content</child>";
        List<Node> parsedFragment = builder.parseFragment(xmlFragment, null, null, null);

        assertEquals(1, parsedFragment.size());

        Element child = (Element) parsedFragment.get(0);
        assertEquals("child", child.tagName());
        assertEquals(1, child.childNodeSize());

        Element content = child.child(0);
        assertEquals(1, content.childNodeSize());
        assertNull(content.attr("attr"));
        assertEquals("Content", content.text());
    }

    @Test
    public void testParseXmlWithEmptyElement() {
        String xmlFragment = "<element />";
        List<Node> parsedFragment = builder.parseFragment(xmlFragment, null, null, null);

        assertEquals(1, parsedFragment.size());

        Element emptyElement = (Element) parsedFragment.get(0);
        assertEquals("element", emptyElement.tagName());
        assertEquals(0, emptyElement.childNodeSize());
    }

    @Test
    public void testParseXmlWithDoctype() {
        String xmlString = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">\n<html>\n<head>\n<title>Title</title>\n</head>\n<body>\n</body>\n</html>";
        Document parsedXml = builder.parse(new StringReader(xmlString));

        DocumentType doctype = parsedXml.doctype();
        assertEquals("html", doctype.tagName());
        assertEquals("about:legacy-compat", doctype.getSystemIdentifier());
    }

    @Test
    public void testParseXmlWithComment() {
        String xmlString = "<!-- This is a comment -->\n<root>\n<child>\n<!-- Another comment -->\nContent\n</child>\n</root>";
        Document parsedXml = builder.parse(new StringReader(xmlString));

        assertEquals(2, parsedXml.childNodeSize());

        Element root = parsedXml.child(0);
        assertEquals(1, root.childNodeSize());

        Element child = root.child(0);
        assertEquals(2, child.childNodeSize());

        Comment comment1 = (Comment) child.child(0);
        assertEquals("This is a comment", comment1.data());

        Element contents = child.child(1);
        assertEquals(1, contents.childNodeSize());
        assertNull(contents.attr("attr"));
        assertEquals("Content", contents.text());
    }

    @Test
    public void testParseXmlWithCData() {
        String xmlString = "<root><![CDATA[This is CData]]></root>";
        Document parsedXml = builder.parse(new StringReader(xmlString));

        assertEquals(1, parsedXml.childNodeSize());

        Element root = parsedXml.child(0);
        assertEquals(1, root.childNodeSize());

        CDataNode cData = (CDataNode) root.child(0);
        assertEquals("This is CData", cData.text());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        String xmlString = "<root><img src=\"image.jpg\" /></root>";
        Document parsedXml = builder.parse(new StringReader(xmlString));

        assertEquals(1, parsedXml.childNodeSize());

        Element root = parsedXml.child(0);
        assertEquals(1, root.childNodeSize());

        Element img = root.child(0);
        assertEquals("img", img.tagName());
        assertEquals("image.jpg", img.attr("src"));
        assertEquals(0, img.childNodeSize());
    }

    @Test
    public void testParseXmlWithNestedTags() {
        String xmlString = "<root><child><grandchild>Content</grandchild></child></root>";
        Document parsedXml = builder.parse(new StringReader(xmlString));

        assertEquals(1, parsedXml.childNodeSize());

        Element root = parsedXml.child(0);
        assertEquals(1, root.childNodeSize());

        Element child = root.child(0);
        assertEquals(1, child.childNodeSize());

        Element grandchild = child.child(0);
        assertEquals("Content", grandchild.text());
    }

    @Test
    public void testParseXmlWithAttributes() {
        String xmlString = "<root attr1=\"value1\" attr2='value2'><child attr3=\"value3\" /></root>";
        Document parsedXml = builder.parse(new StringReader(xmlString));

        assertEquals(1, parsedXml.childNodeSize());

        Element root = parsedXml.child(0);
        assertEquals(1, root.childNodeSize());

        Element child = root.child(0);
        assertEquals(0, child.childNodeSize());

        assertEquals(2, root.numAttributes());

        Attribute attr1 = root.attr("attr1");
        assertEquals("value1", attr1.value());

        Attribute attr2 = root.attr("attr2");
        assertEquals("value2", attr2.value());

        assertEquals(null, child.attr("attr3"));
    }
}