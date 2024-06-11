package org.jsoup.parser;

import static org.junit.Assert.assertEquals;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Token.*;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        Document doc = xmlTreeBuilder.parse(new StringReader("<html></html>"), "https://www.example.com");
        assertEquals("https://www.example.com", doc.baseUri());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        Document doc = xmlTreeBuilder.parse("<html></html>", "https://www.example.com");
        assertEquals("https://www.example.com", doc.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(new StringReader("<html></html>"), "https://www.example.com", errors, settings);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("https://www.example.com", xmlTreeBuilder.baseUri);
        assertEquals(errors, xmlTreeBuilder.errors);
        assertEquals(settings, xmlTreeBuilder.settings);
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTagToken() {
        StartTag startTag = new StartTag(Tag.valueOf("div"), "https://www.example.com");
        xmlTreeBuilder.process(startTag);
        
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("div", xmlTreeBuilder.currentElement().nodeName());
        assertEquals("https://www.example.com", xmlTreeBuilder.currentElement().baseUri());
    }

    @Test
    public void testProcessEndTagToken() {
        EndTag endTag = new EndTag(Tag.valueOf("div"));
        Element divElement = new Element(Tag.valueOf("div"), "https://www.example.com");
        xmlTreeBuilder.stack.add(divElement);

        xmlTreeBuilder.process(endTag);

        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessCommentToken() {
        Comment comment = new Comment("This is a comment");
        xmlTreeBuilder.process(comment);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        Node commentNode = xmlTreeBuilder.currentElement().childNodes().get(0);
        assertEquals("#comment", commentNode.nodeName());
        assertEquals("This is a comment", commentNode.outerHtml());
    }

    @Test
    public void testProcessCharacterToken() {
        Character character = new Character("This is some text");
        xmlTreeBuilder.process(character);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        Node characterNode = xmlTreeBuilder.currentElement().childNodes().get(0);
        assertEquals("#text", characterNode.nodeName());
        assertEquals("This is some text", characterNode.outerHtml());
    }

    @Test
    public void testProcessDoctypeToken() {
        Doctype doctype = new Doctype("html");
        xmlTreeBuilder.process(doctype);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        Node doctypeNode = xmlTreeBuilder.currentElement().childNodes().get(0);
        assertEquals("!DOCTYPE", doctypeNode.nodeName());
        assertEquals("html", doctypeNode.outerHtml());
    }

    @Test(expected = RuntimeException.class)
    public void testProcessUnexpectedToken() {
        Token unexpectedToken = new Token(TokenType.Unknown);
        xmlTreeBuilder.process(unexpectedToken);
    }

    @Test
    public void testInsertNode() {
        Element divElement = new Element(Tag.valueOf("div"), "https://www.example.com");
        xmlTreeBuilder.stack.add(divElement);

        Element pElement = new Element(Tag.valueOf("p"), "https://www.example.com");
        xmlTreeBuilder.insertNode(pElement);

        assertEquals(2, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals(pElement, xmlTreeBuilder.currentElement().childNodes().get(1));
    }

    @Test
    public void testInsertStartTag() {
        StartTag startTag = new StartTag(Tag.valueOf("div"), "https://www.example.com");
        startTag.setSelfClosing(true);
        Element el = xmlTreeBuilder.insert(startTag);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals(el, xmlTreeBuilder.currentElement().childNodes().get(0));
        assertEquals("div", el.nodeName());
        assertEquals("https://www.example.com", el.baseUri());
        assertEquals(true, el.tag().isKnownTag());
        assertEquals(true, el.tag().isSelfClosing());
    }

    @Test
    public void testInsertComment() {
        Comment commentToken = new Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        Node commentNode = xmlTreeBuilder.currentElement().childNodes().get(0);
        assertEquals("#comment", commentNode.nodeName());
        assertEquals("This is a comment", commentNode.outerHtml());
    }

    @Test
    public void testInsertCharacter() {
        Character characterToken = new Character("This is some text");
        xmlTreeBuilder.insert(characterToken);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        Node characterNode = xmlTreeBuilder.currentElement().childNodes().get(0);
        assertEquals("#text", characterNode.nodeName());
        assertEquals("This is some text", characterNode.outerHtml());
    }

    @Test
    public void testInsertDoctype() {
        Doctype doctype = new Doctype("html");
        xmlTreeBuilder.insert(doctype);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        Node doctypeNode = xmlTreeBuilder.currentElement().childNodes().get(0);
        assertEquals("!DOCTYPE", doctypeNode.nodeName());
        assertEquals("html", doctypeNode.outerHtml());
    }

    @Test
    public void testPopStackToClose() {
        Element divElement1 = new Element(Tag.valueOf("div"), "https://www.example.com");
        Element divElement2 = new Element(Tag.valueOf("div"), "https://www.example.com");
        Element divElement3 = new Element(Tag.valueOf("div"), "https://www.example.com");
        xmlTreeBuilder.stack.add(divElement1);
        xmlTreeBuilder.stack.add(divElement2);
        xmlTreeBuilder.stack.add(divElement3);

        Element endTag = new Element(Tag.valueOf("div"), "https://www.example.com");

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(divElement1, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<div><p></p></div>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "https://www.example.com", new ParseErrorList(), ParseSettings.preserveCase);

        assertEquals(1, nodes.size());
        assertEquals(NodeType.Element, nodes.get(0).nodeType());
        Element element = (Element) nodes.get(0);
        assertEquals("div", element.nodeName());
        assertEquals("https://www.example.com", element.baseUri());
        assertEquals(1, element.childNodes().size());
        assertEquals("p", element.childNodes().get(0).nodeName());
    }
}