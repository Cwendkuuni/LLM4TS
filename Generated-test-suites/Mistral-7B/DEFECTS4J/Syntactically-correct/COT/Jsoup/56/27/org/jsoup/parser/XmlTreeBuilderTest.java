package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.List;

/**
 * XmlTreeBuilderTest class
 */
public class XmlTreeBuilderTest {

    /**
     * Test method for {@link XmlTreeBuilder#defaultSettings()}.
     */
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = XmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
    }

    /**
     * Test method for {@link XmlTreeBuilder#parse(String, String)}.
     */
    @Test
    public void testParse() {
        String xmlString = "<xml><root><child>Hello</child></root></xml>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlString, "");
        assertNotNull(xmlDoc);
        Element root = xmlDoc.rootElement();
        assertEquals("xml", root.tagName());
        Element child = root.child(0);
        assertEquals("root", child.tagName());
        assertEquals("Hello", child.text());
    }

    /**
     * Test method for {@link XmlTreeBuilder#initialiseParse(String, String, ParseErrorList, ParseSettings)}.
     */
    @Test
    public void testInitialiseParse() {
        String xmlString = "<xml><root><child>Hello</child></root></xml>";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase();

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(xmlString, "", errors, settings);

        assertEquals("xml", doc.tagName());
        assertEquals("", doc.baseUri());
        assertNotNull(errors);
        assertEquals(0, errors.size());
        assertNotNull(stack);
        assertEquals(1, stack.size());
    }

    /**
     * Test method for {@link XmlTreeBuilder#process(Token)}.
     */
    @Test
    public void testProcessStartTag() {
        String xmlString = "<xml><root><child>Hello</child></root></xml>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTag = new Token.StartTag("root");

        xmlTreeBuilder.process(startTag);

        Element root = (Element) stack.peek();
        assertEquals("root", root.tagName());
        assertEquals(1, stack.size());
    }

    /**
     * Test method for {@link XmlTreeBuilder#process(Token)}.
     */
    @Test
    public void testProcessEndTag() {
        String xmlString = "<xml><root><child><endtag/></child></root></xml>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token endTag = new Token.EndTag("endtag");

        xmlTreeBuilder.process(endTag);

        assertEquals(0, stack.size());
    }

    /**
     * Test method for {@link XmlTreeBuilder#process(Token)}.
     */
    @Test
    public void testProcessComment() {
        String xmlString = "<xml><!-- Comment --></xml>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token comment = new Token.Comment("Comment");

        xmlTreeBuilder.process(comment);

        assertEquals(1, stack.size());
        assertEquals(Comment.class, stack.peek().getClass());
    }

    /**
     * Test method for {@link XmlTreeBuilder#process(Token)}.
     */
    @Test
    public void testProcessCharacter() {
        String xmlString = "<xml>Hello World</xml>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token character = new Token.Character(' ');

        xmlTreeBuilder.process(character);

        assertEquals(2, stack.size());
        TextNode textNode = (TextNode) stack.peek();
        assertEquals("Hello World", textNode.text());
    }

    /**
     * Test method for {@link XmlTreeBuilder#process(Token)}.
     */
    @Test(expected = AssertionError.class)
    public void testProcessUnexpectedToken() {
        String xmlString = "<xml><root><child>Hello</child></root></xml>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(Token.TokenType.UNEXPECTED_TOKEN, "UNEXPECTED_TOKEN_TYPE");

        xmlTreeBuilder.process(token);
    }

    /**
     * Test method for {@link XmlTreeBuilder#insertNode(Node)}.
     */
    @Test
    public void testInsertNode() {
        String xmlString = "<xml><root><child>Hello</child></root></xml>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element("child", "", null);

        xmlTreeBuilder.insertNode(element);

        assertEquals(1, stack.size());
        Element stackTopElement = (Element) stack.peek();
        assertEquals("child", stackTopElement.tagName());
    }

    /**
     * Test method for {@link XmlTreeBuilder#parseFragment(String, String, ParseErrorList, ParseSettings)}.
     */
    @Test
    public void testParseFragment() {
        String xmlFragment = "<child>Hello</child>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", new ParseErrorList(), ParseSettings.preserveCase());

        assertEquals(1, nodes.size());
        Element node = (Element) nodes.get(0);
        assertEquals("child", node.tagName());
        assertEquals("Hello", node.text());
    }

    /**
     * Test method for {@link XmlTreeBuilder#popStackToClose(Token.EndTag)}.
     */
    @Test
    public void testPopStackToClose() {
        String xmlString = "<xml><root><child><grandChild/></child></root></xml>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element child = new Element("child", "", null);
        Element grandChild = new Element("grandChild", "", null);
        stack.add(child);
        stack.add(doc);

        xmlTreeBuilder.popStackToClose(new Token.EndTag("grandChild"));

        assertEquals(1, stack.size());
        assertEquals(child, stack.peek());
    }
}