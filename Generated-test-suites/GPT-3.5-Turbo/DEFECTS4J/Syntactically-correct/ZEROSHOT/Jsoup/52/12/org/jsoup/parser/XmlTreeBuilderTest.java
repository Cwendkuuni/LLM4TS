package org.jsoup.parser;

import org.jsoup.nodes.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    private String inputFragment;
    private String baseUri;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        inputFragment = "<xml><tag1>text1</tag1><tag2>text2</tag2></xml>";
        baseUri = "http://www.example.com";
        errors = new ParseErrorList();
    }

    @Test
    public void testInitialiseParse() {
        xmlTreeBuilder.initialiseParse(inputFragment, baseUri, errors);

        Assert.assertEquals("http://www.example.com", xmlTreeBuilder.baseUri);
        Assert.assertNotNull(xmlTreeBuilder.errors);
        Assert.assertEquals(3, xmlTreeBuilder.stack.size());
        Assert.assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        Token token = new Token.StartTag("<tag1>");

        Assert.assertTrue(xmlTreeBuilder.process(token));
        Assert.assertEquals("xml", xmlTreeBuilder.doc.nodeName());
        Assert.assertEquals(2, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessEndTag() {
        Token token = new Token.EndTag("</tag1>");

        xmlTreeBuilder.stack.add(xmlTreeBuilder.doc);
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("tag1"), baseUri));

        Assert.assertTrue(xmlTreeBuilder.process(token));
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessComment() {
        Token token = new Token.Comment("This is a comment");

        Assert.assertTrue(xmlTreeBuilder.process(token));
        Assert.assertEquals("comment", xmlTreeBuilder.doc.childNode(0).nodeName());
    }

    @Test
    public void testProcessCharacter() {
        Token token = new Token.Character("text");

        Assert.assertTrue(xmlTreeBuilder.process(token));
        Assert.assertEquals("text", xmlTreeBuilder.doc.childNode(0).outerHtml());
    }

    @Test
    public void testProcessDoctype() {
        Token token = new Token.Doctype("doctype");

        Assert.assertTrue(xmlTreeBuilder.process(token));
        Assert.assertEquals("doctype", xmlTreeBuilder.doc.childNode(0).nodeName());
    }

    @Test
    public void testInsertNode() {
        Node node = new Element(Tag.valueOf("tag"), baseUri);

        xmlTreeBuilder.insertNode(node);

        Assert.assertEquals(node, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("<tag>");

        Element element = xmlTreeBuilder.insert(startTag);

        Assert.assertEquals("tag", element.nodeName());
        Assert.assertEquals(baseUri, element.baseUri());
        Assert.assertEquals(0, element.childNodeSize());
        Assert.assertEquals(0, element.attributes().size());
    }

    @Test
    public void testInsertSelfClosingStartTag() {
        Token.StartTag startTag = new Token.StartTag("<tag/>");

        Element element = xmlTreeBuilder.insert(startTag);

        Assert.assertEquals("tag", element.nodeName());
        Assert.assertEquals(baseUri, element.baseUri());
        Assert.assertEquals(0, element.childNodeSize());
        Assert.assertEquals(0, element.attributes().size());
    }

    @Test
    public void testInsertComment() {
        Token.Comment comment = new Token.Comment("This is a comment");

        xmlTreeBuilder.insert(comment);

        Assert.assertEquals("comment", xmlTreeBuilder.currentElement().childNode(0).nodeName());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character character = new Token.Character("text");

        xmlTreeBuilder.insert(character);

        Assert.assertEquals("text", xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("doctype");

        xmlTreeBuilder.insert(doctype);

        Assert.assertEquals("doctype", xmlTreeBuilder.currentElement().childNode(0).nodeName());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("</tag1>");

        Element element1 = new Element(Tag.valueOf("tag2"), baseUri);
        Element element2 = new Element(Tag.valueOf("tag1"), baseUri);
        xmlTreeBuilder.stack.add(xmlTreeBuilder.doc);
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);

        xmlTreeBuilder.popStackToClose(endTag);

        Assert.assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragment() {
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("xml", nodes.get(0).nodeName());
    }
}