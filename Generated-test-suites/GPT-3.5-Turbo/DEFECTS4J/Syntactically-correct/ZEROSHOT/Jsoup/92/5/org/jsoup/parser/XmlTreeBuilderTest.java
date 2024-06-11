package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        assertNotNull(parseSettings);
        assertEquals(ParseSettings.preserveCase, parseSettings);
    }

    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("");
        String baseUri = "";
        Parser parser = new Parser(xmlTreeBuilder);
        xmlTreeBuilder.initialiseParse(input, baseUri, parser);
        // add assertions
    }

    @Test
    public void testParseReaderString() {
        Reader input = new StringReader("");
        String baseUri = "";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        // add assertions
    }

    @Test
    public void testParseStringString() {
        String input = "";
        String baseUri = "";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        // add assertions
    }

    @Test
    public void testProcess() {
        Token token = new Token(Token.TokenType.StartTag, new Token.StartTag());
        boolean result = xmlTreeBuilder.process(token);
        assertTrue(result);
        // add assertions
    }

    @Test
    public void testInsertNode() {
        Node node = new Node();
        xmlTreeBuilder.insertNode(node);
        // add assertions
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        // add assertions
    }

    @Test
    public void testInsertComment() {
        Token.Comment comment = new Token.Comment();
        xmlTreeBuilder.insert(comment);
        // add assertions
    }

    @Test
    public void testInsertCharacter() {
        Token.Character character = new Token.Character();
        xmlTreeBuilder.insert(character);
        // add assertions
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        xmlTreeBuilder.insert(doctype);
        // add assertions
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag();
        xmlTreeBuilder.popStackToClose(endTag);
        // add assertions
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "";
        String baseUri = "";
        Parser parser = new Parser(xmlTreeBuilder);
        List<Node> nodeList = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);
        assertNotNull(nodeList);
        // add assertions
    }

    @Test
    public void testParseFragmentWithContext() {
        String inputFragment = "";
        Element context = new Element();
        String baseUri = "";
        Parser parser = new Parser(xmlTreeBuilder);
        List<Node> nodeList = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, parser);
        assertNotNull(nodeList);
        // add assertions
    }
}