package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
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
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        StringReader input = new StringReader("");
        String baseUri = "http://www.example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "";
        String baseUri = "http://www.example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testInitialiseParse() {
        StringReader input = new StringReader("");
        String baseUri = "http://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        assertNotNull(xmlTreeBuilder.getStack());
        assertNotNull(xmlTreeBuilder.getDoc());
    }

    @Test
    public void testProcessStartTagToken() {
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes());
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
    }

    @Test
    public void testProcessEndTagToken() {
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.popStackToClose(endTag);
        List<Element> stack = xmlTreeBuilder.getStack();
        assertTrue(stack.isEmpty());
    }

    @Test
    public void testProcessCommentToken() {
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);
        List<Node> childNodes = xmlTreeBuilder.getDoc().childNodes();
        assertFalse(childNodes.isEmpty());
        assertTrue(childNodes.get(0) instanceof Comment);
    }

    @Test
    public void testProcessCharacterToken() {
        Token.Character characterToken = new Token.Character("character");
        xmlTreeBuilder.insert(characterToken);
        List<Node> childNodes = xmlTreeBuilder.getDoc().childNodes();
        assertFalse(childNodes.isEmpty());
        assertTrue(childNodes.get(0) instanceof TextNode);
    }

    @Test
    public void testProcessDoctypeToken() {
        Token.Doctype doctypeToken = new Token.Doctype("name", "publicId", "systemId", "pubSysKey");
        xmlTreeBuilder.insert(doctypeToken);
        List<Node> childNodes = xmlTreeBuilder.getDoc().childNodes();
        assertFalse(childNodes.isEmpty());
        assertTrue(childNodes.get(0) instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.popStackToClose(endTag);
        List<Element> stack = xmlTreeBuilder.getStack();
        assertTrue(stack.isEmpty());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assertNotNull(nodes);
    }
}