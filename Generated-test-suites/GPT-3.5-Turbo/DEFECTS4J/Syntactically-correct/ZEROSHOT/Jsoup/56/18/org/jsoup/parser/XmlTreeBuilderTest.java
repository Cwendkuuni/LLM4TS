package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings expected = ParseSettings.preserveCase;
        ParseSettings actual = xmlTreeBuilder.defaultSettings();
        assertEquals(expected, actual);
    }

    @Test
    public void testParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "some xml input";
        String baseUri = "http://example.com";
        Document expected = new Document("");
        Document actual = xmlTreeBuilder.parse(input, baseUri);
        assertEquals(expected, actual);
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "some xml input";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        // assert something
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(Token.TokenType.StartTag, "tag");
        boolean expected = true;
        boolean actual = xmlTreeBuilder.process(token);
        assertEquals(expected, actual);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Node node = new Node();
        xmlTreeBuilder.insertNode(node);
        // assert something
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag");
        Element expected = new Element();
        Element actual = xmlTreeBuilder.insert(startTag);
        assertEquals(expected, actual);
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);
        // assert something
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("character");
        xmlTreeBuilder.insert(characterToken);
        // assert something
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype d = new Token.Doctype("doctype");
        xmlTreeBuilder.insert(d);
        // assert something
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.popStackToClose(endTag);
        // assert something
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "some xml input";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> expected = new ArrayList<Node>();
        List<Node> actual = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assertEquals(expected, actual);
    }
}