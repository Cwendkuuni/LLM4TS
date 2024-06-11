package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        assertEquals("http://example.com", xmlTreeBuilder.baseUri);
        assertEquals("root", xmlTreeBuilder.stack.get(0).nodeName());
        assertFalse(xmlTreeBuilder.stack.get(0).isKnownTag());
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess() {
        Token startTagToken = new Token.StartTag("div");
        Token endTagToken = new Token.EndTag("div");
        Token commentToken = new Token.Comment("Test comment");
        Token characterToken = new Token.Character("Test character");
        Token doctypeToken = new Token.Doctype();
        Token eofToken = new Token.EOF();

        // Test StartTag
        assertTrue(xmlTreeBuilder.process(startTagToken));
        assertEquals("div", xmlTreeBuilder.currentElement().nodeName());

        // Test EndTag
        xmlTreeBuilder.stack.add(new Element("div"));
        assertTrue(xmlTreeBuilder.process(endTagToken));
        assertTrue(xmlTreeBuilder.stack.isEmpty());

        // Test Comment
        assertTrue(xmlTreeBuilder.process(commentToken));
        assertEquals("Test comment", xmlTreeBuilder.currentElement().childNode(0).toString());

        // Test Character
        assertTrue(xmlTreeBuilder.process(characterToken));
        assertEquals("Test character", xmlTreeBuilder.currentElement().childNode(0).toString());

        // Test Doctype
        assertTrue(xmlTreeBuilder.process(doctypeToken));
        assertEquals("Test Doctype", xmlTreeBuilder.currentElement().childNode(0).toString());

        // Test EOF
        assertTrue(xmlTreeBuilder.process(eofToken));
    }

    @Test
    public void testInsertNode() {
        Element parentElement = new Element("div");
        Element childElement = new Element("span");
        TextNode textNode = new TextNode("Test text");

        parentElement.appendChild(childElement);

        xmlTreeBuilder.stack.add(parentElement);
        xmlTreeBuilder.insertNode(textNode);

        assertEquals("Test text", xmlTreeBuilder.currentElement().childNode(1).toString());
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes.put("attr", "value");

        assertNotNull(xmlTreeBuilder.insert(startTag));
        assertEquals("div", xmlTreeBuilder.currentElement().nodeName());
        assertEquals("value", xmlTreeBuilder.currentElement().attributes().get("attr"));
        assertFalse(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("Test comment");

        xmlTreeBuilder.insert(commentToken);

        assertEquals("Test comment", xmlTreeBuilder.currentElement().childNode(0).toString());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("Test character");

        xmlTreeBuilder.insert(characterToken);

        assertEquals("Test character", xmlTreeBuilder.currentElement().childNode(0).toString());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctypeToken = new Token.Doctype();

        xmlTreeBuilder.insert(doctypeToken);

        assertEquals("Test Doctype", xmlTreeBuilder.currentElement().childNode(0).toString());
    }

    @Test
    public void testPopStackToClose() {
        xmlTreeBuilder.stack.add(new Element("div"));
        xmlTreeBuilder.stack.add(new Element("span"));
        xmlTreeBuilder.stack.add(new Element("div"));

        Token.EndTag endTag = new Token.EndTag("div");

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals("span", xmlTreeBuilder.stack.get(0).nodeName());
        assertTrue(xmlTreeBuilder.stack.size() == 1);
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<div><span></span></div>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        assertEquals("div", nodes.get(0).nodeName());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("span", ((Element) nodes.get(0)).childNode(0).nodeName());
        assertTrue(nodes.get(0).childNodes().size() == 1);
    }
}