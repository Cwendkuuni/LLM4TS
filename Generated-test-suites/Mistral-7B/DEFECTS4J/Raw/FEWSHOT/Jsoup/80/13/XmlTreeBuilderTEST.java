package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.jsoup.nodes.Attributes.fromMap;
import static org.jsoup.nodes.Elements.element;
import static org.jsoup.nodes.Elements.tag;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenStream;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlString = "<root><element attr=\"value\">Content</element></root>";
        Document xmlDoc = Jsoup.parse(xmlString, Parser.xmlParser());
        assertEquals("<root><element attr=\"value\">Content</element></root>", xmlDoc.html());

        Element root = xmlDoc.element("root");
        assertEquals(1, root.children().size());
        Element element = root.child(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr").get());
        assertEquals("Content", element.text());
    }

    @Test
    public void testParseWithComments() {
        String xmlString = "<root><!-- Comment1 --><element attr=\"value\">Content</element><!-- Comment2 --></root>";
        Document xmlDoc = Jsoup.parse(xmlString, Parser.xmlParser());
        assertEquals("<root><!-- Comment1 --><element attr=\"value\">Content</element><!-- Comment2 --></root>", xmlDoc.html());

        List<Comment> comments = xmlDoc.comments();
        assertEquals(2, comments.size());
        Comment comment1 = comments.get(0);
        assertEquals("Comment1", comment1.toString());
        Comment comment2 = comments.get(1);
        assertEquals("Comment2", comment2.toString());
    }

    @Test
    public void testParseWithCDataSection() {
        String xmlString = "<root><![CDATA[ <element attr=\"value\">Content</element> ]]></root>";
        Document xmlDoc = Jsoup.parse(xmlString, Parser.xmlParser());
        assertEquals("<root><![CDATA[ <element attr=\"value\">Content</element> ]]></root>", xmlDoc.html());

        Element root = xmlDoc.element("root");
        List<Node> cdataSection = root.child(0).child(0).child(0).children();
        assertEquals(1, cdataSection.size());
        CDataNode cdataNode = (CDataNode) cdataSection.get(0);
        assertEquals("<element attr=\"value\">Content</element>", cdataNode.toString());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element attr=\"value\">Content</element>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, null, ParseErrorList.noTracking(), ParseSettings.preserveCase).get(0).childNodes();
        assertEquals(1, nodes.size());
        Element element = (Element) nodes.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr").get());
        assertEquals("Content", element.text());
    }

    @Test
    public void testParseWithEmptyFragment() {
        List<Node> nodes = XmlTreeBuilder.parseFragment("", null, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertEquals(0, nodes.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidInput() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        TokenStream tokenStream = parser.parse(new StringReader("Invalid input"));
        tokenStream.nextToken();
        fail();
    }
}