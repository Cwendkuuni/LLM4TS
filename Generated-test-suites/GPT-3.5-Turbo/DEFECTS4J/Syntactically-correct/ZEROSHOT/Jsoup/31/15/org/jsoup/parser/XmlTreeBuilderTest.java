package org.jsoup.parser;

import org.jsoup.nodes.*;
import org.junit.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @After
    public void tearDown() {
        xmlTreeBuilder = null;
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        XmlTreeBuilder initialXmlTreeBuilder = new XmlTreeBuilder();
        
        initialXmlTreeBuilder.initialiseParse(input, baseUri, errors);

        // Check if stack contains doc
        Assert.assertTrue(initialXmlTreeBuilder.stack.contains(initialXmlTreeBuilder.doc));
    }

    @Test
    public void testProcessStartTag() {
        String tagName = "div";
        Attributes attributes = new Attributes();
        Token.StartTag startTag = new Token.StartTag(tagName, attributes);
        
        xmlTreeBuilder.process(startTag);

        // Check if stack contains element with correct tag
        Assert.assertTrue(xmlTreeBuilder.stack.stream().anyMatch(element -> element.nodeName().equals(tagName)));
    }

    @Test
    public void testProcessEndTag() {
        String tagName = "div";
        Token.EndTag endTag = new Token.EndTag(tagName);
        
        xmlTreeBuilder.popStackToClose(endTag);

        // Check if stack does not contain element with defined tag
        Assert.assertFalse(xmlTreeBuilder.stack.stream().anyMatch(element -> element.nodeName().equals(tagName)));
    }

    @Test
    public void testInsertComment() {
        String commentData = "This is a comment";
        Token.Comment commentToken = new Token.Comment(commentData);
        
        xmlTreeBuilder.insert(commentToken);

        // Check if the last node inserted was a comment node
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        Assert.assertTrue(node instanceof Comment);
        Assert.assertEquals(commentData, ((Comment) node).getData());
    }

    @Test
    public void testInsertCharacter() {
        String characterData = "This is some text";
        Token.Character characterToken = new Token.Character(characterData);
        
        xmlTreeBuilder.insert(characterToken);

        // Check if the last node inserted was a text node
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        Assert.assertTrue(node instanceof TextNode);
        Assert.assertEquals(characterData, ((TextNode) node).getWholeText());
    }

    @Test
    public void testInsertDoctype() {
        String name = "html";
        String publicIdentifier = "-//W3C//DTD HTML 4.01//EN";
        String systemIdentifier = "http://www.w3.org/TR/html4/strict.dtd";
        Token.Doctype doctypeToken = new Token.Doctype(name, publicIdentifier, systemIdentifier);
        
        xmlTreeBuilder.insert(doctypeToken);

        // Check if the last node inserted was a doctype node
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        Assert.assertTrue(node instanceof DocumentType);
        Assert.assertEquals(name, ((DocumentType) node).getName());
        Assert.assertEquals(publicIdentifier, ((DocumentType) node).getPublicIdentifier());
        Assert.assertEquals(systemIdentifier, ((DocumentType) node).getSystemIdentifier());
    }
}