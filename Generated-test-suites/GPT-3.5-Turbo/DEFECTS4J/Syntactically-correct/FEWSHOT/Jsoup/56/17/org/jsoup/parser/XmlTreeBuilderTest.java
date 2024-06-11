package org.jsoup.parser;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParse() {
        String input = "<root></root>";
        String baseUri = "http://www.example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertEquals("<root></root>", document.outerHtml());
        assertEquals("http://www.example.com", document.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root></root>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);

        assertEquals(ParseErrorList.noTracking(), errors);
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testProcess() {
        Token token = new Token(Token.TokenType.StartTag);
        Element element = new Element(Tag.valueOf("div"), "http://www.example.com");
        token.asStartTag().name = "div";

        xmlTreeBuilder.process(token);

        Node expectedNode = xmlTreeBuilder.currentElement().appendChild(element);
        assertEquals(expectedNode, xmlTreeBuilder.currentElement().childNodes().get(0));
    }

    @Test
    public void testInsertNode() {
        Node node = new Element(Tag.valueOf("div"), "http://www.example.com");
        Element currentElement = new Element(Tag.valueOf("body"), "http://www.example.com");
        xmlTreeBuilder.stack.add(currentElement);

        xmlTreeBuilder.insertNode(node);
        Node expectedNode = xmlTreeBuilder.currentElement().appendChild(node);
        assertEquals(expectedNode, xmlTreeBuilder.currentElement().childNodes().get(0));
    }

    @Test
    public void testInsert() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.data = "This is a comment";

        xmlTreeBuilder.insert(commentToken);

        Node expectedNode = new Comment("This is a comment", "http://www.example.com");
        assertEquals(expectedNode, xmlTreeBuilder.currentElement().childNodes().get(0));
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character();
        characterToken.data = "This is a character";

        xmlTreeBuilder.insert(characterToken);

        Node expectedNode = new TextNode("This is a character", "http://www.example.com");
        assertEquals(expectedNode, xmlTreeBuilder.currentElement().childNodes().get(0));
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name = "html";
        doctype.publicIdentifier = "-//W3C//DTD HTML 4.0 Transitional//EN";

        xmlTreeBuilder.insert(doctype);

        Node expectedNode = new DocumentType("html", "-//W3C//DTD HTML 4.0 Transitional//EN", "", "http://www.example.com");
        assertEquals(expectedNode, xmlTreeBuilder.currentElement().childNodes().get(0));
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name = "div";
        Element element = new Element(Tag.valueOf("div"), "http://www.example.com");
        xmlTreeBuilder.stack.add(element);

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<p>This is a paragraph</p>";
        String baseUri = "http://www.example.com";
        ParseSettings settings = ParseSettings.preserveCase;
        ParseErrorList errors = new ParseErrorList();

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        Element expectedElement = new Element(Tag.valueOf("p"), "http://www.example.com");
        expectedElement.appendChild(new TextNode("This is a paragraph", "http://www.example.com"));
        assertEquals(expectedElement, nodes.get(0));
    }
}