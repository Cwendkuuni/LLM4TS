package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void initialiseParse_withNormalInput_shouldSetDocumentSyntaxToXml() {
        String input = "<xml><tag></tag></xml>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void process_withStartTagToken_shouldInsertElementToCurrentElement() {
        Token.StartTag startTag = new Token.StartTag("tag", "name");
        Element expectedElement = new Element(Tag.valueOf("tag"), "https://example.com", "name");

        boolean result = xmlTreeBuilder.process(startTag);

        assertTrue(result);
        assertEquals(expectedElement, xmlTreeBuilder.currentElement().children().get(0));
    }

    @Test
    public void process_withEndTagToken_shouldPopStackToCloseElement() {
        Token.EndTag endTag = new Token.EndTag("tag");
        Element elementToInsert = new Element(Tag.valueOf("tag"), "https://example.com", "name");
        xmlTreeBuilder.stack.add(elementToInsert);

        boolean result = xmlTreeBuilder.process(endTag);

        assertTrue(result);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void process_withCommentToken_shouldInsertCommentNodeToCurrentElement() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        Comment expectedComment = new Comment(commentToken.getData(), "https://example.com");

        xmlTreeBuilder.process(commentToken);

        assertEquals(expectedComment, xmlTreeBuilder.currentElement().children().get(0));
    }

    @Test
    public void process_withCharacterToken_shouldInsertTextNodeToCurrentElement() {
        Token.Character characterToken = new Token.Character("data");
        TextNode expectedTextNode = new TextNode(characterToken.getData(), "https://example.com");

        xmlTreeBuilder.process(characterToken);

        assertEquals(expectedTextNode, xmlTreeBuilder.currentElement().children().get(0));
    }

    @Test
    public void process_withDoctypeToken_shouldInsertDoctypeNodeToCurrentElement() {
        Token.Doctype doctypeToken = new Token.Doctype("test", "public", "system");
        DocumentType expectedDoctype = new DocumentType("test", "public", "system", "https://example.com");

        xmlTreeBuilder.process(doctypeToken);

        assertEquals(expectedDoctype, xmlTreeBuilder.currentElement().children().get(0));
    }

    @Test
    public void insert_withStartTagToken_shouldInsertElementToCurrentElement() {
        Token.StartTag startTag = new Token.StartTag("tag", "name");
        Element expectedElement = new Element(Tag.valueOf("tag"), "https://example.com", "name");

        Element result = xmlTreeBuilder.insert(startTag);

        assertEquals(expectedElement, result);
        assertEquals(expectedElement, xmlTreeBuilder.currentElement().children().get(0));
    }

    @Test
    public void insert_withCommentToken_shouldInsertCommentNodeToCurrentElement() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        Comment expectedComment = new Comment(commentToken.getData(), "https://example.com");

        xmlTreeBuilder.insert(commentToken);

        assertEquals(expectedComment, xmlTreeBuilder.currentElement().children().get(0));
    }

    @Test
    public void insert_withCharacterToken_shouldInsertTextNodeToCurrentElement() {
        Token.Character characterToken = new Token.Character("data");
        TextNode expectedTextNode = new TextNode(characterToken.getData(), "https://example.com");

        xmlTreeBuilder.insert(characterToken);

        assertEquals(expectedTextNode, xmlTreeBuilder.currentElement().children().get(0));
    }

    @Test
    public void insert_withDoctypeToken_shouldInsertDoctypeNodeToCurrentElement() {
        Token.Doctype doctypeToken = new Token.Doctype("test", "public", "system");
        DocumentType expectedDoctype = new DocumentType("test", "public", "system", "https://example.com");

        xmlTreeBuilder.insert(doctypeToken);

        assertEquals(expectedDoctype, xmlTreeBuilder.currentElement().children().get(0));
    }

    @Test
    public void popStackToClose_withValidEndTagName_shouldRemoveElementFromStack() {
        Token.EndTag endTag = new Token.EndTag("tag");

        Element elementToRemove = new Element(Tag.valueOf("tag"), "https://example.com", "name");
        xmlTreeBuilder.stack.add(elementToRemove);

        xmlTreeBuilder.popStackToClose(endTag);

        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void popStackToClose_withInvalidEndTagName_shouldNotRemoveAnyElementFromStack() {
        Token.EndTag endTag = new Token.EndTag("tag");

        Element elementToKeep = new Element(Tag.valueOf("otherTag"), "https://example.com", "name");
        xmlTreeBuilder.stack.add(elementToKeep);

        xmlTreeBuilder.popStackToClose(endTag);

        assertFalse(xmlTreeBuilder.stack.isEmpty());
        assertTrue(xmlTreeBuilder.stack.contains(elementToKeep));
    }

    @Test
    public void parseFragment_withValidInput_shouldReturnParsedFragment() {
        String input = "<tag></tag>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        xmlTreeBuilder.runParser();

        assertFalse(xmlTreeBuilder.doc.childNodes().isEmpty());
    }

    @Test
    public void parseFragment_withInvalidInput_shouldReturnEmptyList() {
        String input = "<tag<</tag>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        xmlTreeBuilder.runParser();

        assertTrue(xmlTreeBuilder.doc.childNodes().isEmpty());
    }
}