package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private Document doc;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        doc = new Document("");
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParse() {
        String input = "";
        String baseUri = "";
        Document result = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(result);
    }

    @Test
    public void testInitialiseParse() {
        String input = "";
        String baseUri = "";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        assertEquals(doc, xmlTreeBuilder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }

    @Test
    public void testProcess() {
        Token token = new Token(Token.TokenType.StartTag);
        boolean result = xmlTreeBuilder.process(token);
        assertTrue(result);
    }

    @Test
    public void testInsertNode() {
        Node node = new Node();
        xmlTreeBuilder.insertNode(node);
        assertEquals(node, xmlTreeBuilder.currentElement().child(0));
    }

    @Test
    public void testInsert() {
        Token.StartTag startTag = new Token.StartTag("tag");
        Element result = xmlTreeBuilder.insert(startTag);
        assertNotNull(result);
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("character");
        xmlTreeBuilder.insert(characterToken);
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("name", "public", "system");
        xmlTreeBuilder.insert(doctype);
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.stack.add(new Element("tag"));
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "";
        String baseUri = "";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings();
        List<Node> resultList = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assertNotNull(resultList);
    }
}