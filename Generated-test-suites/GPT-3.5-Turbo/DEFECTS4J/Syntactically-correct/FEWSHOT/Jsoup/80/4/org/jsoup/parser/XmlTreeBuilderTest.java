package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Doctype;
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
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
        assertNotNull(defaultSettings);
        assertEquals(ParseSettings.preserveCase, defaultSettings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        Reader input = new StringReader("<xml>test</xml>");
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals("<xml>test</xml>", document.html());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<xml>test</xml>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals("<xml>test</xml>", document.html());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<xml>test</xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(new StringReader(input), baseUri, errors, settings);
        List<Node> stack = xmlTreeBuilder.getStack();

        assertEquals(1, stack.size());
        assertTrue(stack.get(0) instanceof Document);
        assertEquals(baseUri, stack.get(0).baseUri());
        assertEquals(settings, stack.get(0).outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        Element element = xmlTreeBuilder.createNewElement(new StartTag("tag", ""));
        assertNotNull(element);
        assertEquals("tag", element.tagName());
    }

    @Test
    public void testProcessEndTag() {
        Element parentElement = new Element("parent");
        Element childElement = new Element("child");
        parentElement.appendChild(childElement);
        xmlTreeBuilder.getStack().add(parentElement);
        
        xmlTreeBuilder.process(new Token.EndTag("child"));
        assertEquals(0, parentElement.childNodes().size());
        assertEquals(0, xmlTreeBuilder.getStack().size());
    }

    @Test
    public void testProcessComment() {
        Comment comment = xmlTreeBuilder.createNewComment(new Comment("test"));
        assertNotNull(comment);
        assertEquals("<!--test-->", comment.outerHtml());
    }

    @Test
    public void testProcessCharacter() {
        Character character = xmlTreeBuilder.createNewCharacter(new Character("test"));
        assertNotNull(character);
        assertEquals("test", character.getWholeData());
    }

    @Test
    public void testProcessDoctype() {
        Doctype doctype = xmlTreeBuilder.createNewDoctype(new Doctype("html"));
        assertNotNull(doctype);
        assertEquals("html", doctype.getName());
    }

    @Test
    public void testInsertNode() {
        Element parentElement = new Element("parent");
        Element childElement = new Element("child");
        xmlTreeBuilder.getStack().add(parentElement);
        
        xmlTreeBuilder.insertNode(childElement);
        assertEquals(1, parentElement.childNodes().size());
        assertTrue(parentElement.childNodes().contains(childElement));
    }

    @Test
    public void testInsertStartTag() {
        StartTag startTag = new StartTag("tag", "attr=value");
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("tag", element.tagName());
        assertEquals("attr=value", element.attributes().toString());
    }

    @Test
    public void testInsertComment() {
        Comment commentToken = new Comment("test comment");
        xmlTreeBuilder.insert(commentToken);
        assertEquals(1, xmlTreeBuilder.doc().childNodes().size());
        assertTrue(xmlTreeBuilder.doc().childNodes().get(0) instanceof Comment);
    }

    @Test
    public void testInsertCharacter() {
        Character characterToken = new Character("test");
        xmlTreeBuilder.insert(characterToken);
        assertEquals(1, xmlTreeBuilder.doc().childNodes().size());
        assertTrue(xmlTreeBuilder.doc().childNodes().get(0) instanceof TextNode);
    }

    @Test
    public void testInsertDoctype() {
        Doctype doctype = new Doctype("html");
        xmlTreeBuilder.insert(doctype);
        assertEquals(1, xmlTreeBuilder.doc().childNodes().size());
        assertTrue(xmlTreeBuilder.doc().childNodes().get(0) instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        Element parentElement1 = new Element("parent");
        Element childElement1 = new Element("child");
        Element parentElement2 = new Element("parent");
        Element childElement2 = new Element("child");
        parentElement1.appendChild(childElement1);
        parentElement2.appendChild(childElement2);
        xmlTreeBuilder.getStack().add(parentElement1);
        xmlTreeBuilder.getStack().add(childElement1);
        xmlTreeBuilder.getStack().add(parentElement2);
        xmlTreeBuilder.getStack().add(childElement2);

        xmlTreeBuilder.popStackToClose(new Token.EndTag("child"));
        assertEquals(2, xmlTreeBuilder.getStack().size());
        assertEquals(parentElement1, xmlTreeBuilder.getStack().get(0));
        assertEquals(parentElement2, xmlTreeBuilder.getStack().get(1));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<tag>test</tag>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("tag", nodes.get(0).nodeName());
    }
}