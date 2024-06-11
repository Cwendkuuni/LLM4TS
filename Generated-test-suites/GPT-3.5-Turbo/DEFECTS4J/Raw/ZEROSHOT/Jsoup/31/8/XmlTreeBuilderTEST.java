package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class XmlTreeBuilderTest {

    @Mock
    private TreeBuilder treeBuilder;

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<xml>Testing</xml>";
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        verify(treeBuilder).initialiseParse(input, baseUri, errors);
        assertEquals(xmlTreeBuilder.getCurrentElement(), xmlTreeBuilder.getDocument());
        assertEquals(xmlTreeBuilder.getStack().size(), 1);
    }
    
    @Test
    public void testProcessStartTag() {
        Token token = new Token.StartTag("xml", "attr", true);
        Element element = new Element(Tag.valueOf("xml"), "http://example.com/", token.getAttributes());
        xmlTreeBuilder.insert(token);

        assertEquals(xmlTreeBuilder.getCurrentElement(), element);
        assertTrue(element.isSelfClosing());
        assertTrue(element.tag().isKnownTag());
    }

    @Test
    public void testProcessEndTag() {
        Token token = new Token.EndTag("xml");
        xmlTreeBuilder.getStack().push(new Element(Tag.valueOf("xml")));
        xmlTreeBuilder.insert(token);

        assertTrue(xmlTreeBuilder.getStack().isEmpty());
    }

    @Test
    public void testProcessComment() {
        Token token = new Token.Comment("Test Comment");
        Comment comment = new Comment(token.getData(), xmlTreeBuilder.getBaseUri());
        xmlTreeBuilder.insert(token);

        assertTrue(xmlTreeBuilder.getCurrentElement().childNode(0) instanceof Comment);
        assertEquals(xmlTreeBuilder.getCurrentElement().childNode(0), comment);
    }

    @Test
    public void testProcessCharacter() {
        Token token = new Token.Character("Test Character");
        TextNode textNode = new TextNode(token.getData(), xmlTreeBuilder.getBaseUri());
        xmlTreeBuilder.insert(token);

        assertTrue(xmlTreeBuilder.getCurrentElement().childNode(0) instanceof TextNode);
        assertEquals(xmlTreeBuilder.getCurrentElement().childNode(0), textNode);
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype token = new Token.Doctype("html", "", "", xmlTreeBuilder.getBaseUri());
        DocumentType doctypeNode = new DocumentType(token.getName(), token.getPublicIdentifier(), token.getSystemIdentifier(), xmlTreeBuilder.getBaseUri());
        xmlTreeBuilder.insert(token);

        assertTrue(xmlTreeBuilder.getCurrentElement().childNode(0) instanceof DocumentType);
        assertEquals(xmlTreeBuilder.getCurrentElement().childNode(0), doctypeNode);
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag token = new Token.EndTag("xml");
        Element element = new Element(Tag.valueOf("xml"));

        xmlTreeBuilder.getStack().push(element);
        xmlTreeBuilder.popStackToClose(token);

        assertTrue(xmlTreeBuilder.getStack().isEmpty());
    }
}