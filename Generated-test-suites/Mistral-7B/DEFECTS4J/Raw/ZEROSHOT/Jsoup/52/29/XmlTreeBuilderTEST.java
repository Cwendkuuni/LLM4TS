package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.helper.Validate;
import org.junit.Test;

import java.util.List;

/**
 * Tests for the XmlTreeBuilder class.
 */
public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><element attr='value'/></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        assertEquals("http://example.com", xmlTreeBuilder.doc.baseUri());
        // Add more assertions as needed to check the initialised state of the XmlTreeBuilder
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><element attr='value'/></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        Token startTagToken = new Token.StartTag("element", null, null);
        xmlTreeBuilder.process(startTagToken);
        Element expectedElement = new Element("element", baseUri, null);
        Element actualElement = xmlTreeBuilder.currentElement();
        assertEquals(expectedElement.nodeName(), actualElement.nodeName());
        assertEquals(expectedElement.baseUri(), actualElement.baseUri());
        // Add more assertions as needed to check the state of the Element after processing a start tag
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><element><subelement/></element></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        Token startTagToken = new Token.StartTag("element", null, null);
        xmlTreeBuilder.process(startTagToken);
        Element element = xmlTreeBuilder.currentElement();
        stack.push(element);

        Token endTagToken = new Token.EndTag("element");
        xmlTreeBuilder.process(endTagToken);

        Element expectedElement = element;
        Element actualElement = xmlTreeBuilder.currentElement();
        assertTrue(xmlTreeBuilder.stack.isEmpty());
        assertEquals(expectedElement, actualElement);
        // Add more assertions as needed to check the state of the Element and stack after processing an end tag
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><!-- comment --></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        Token commentToken = new Token.Comment("comment");
        xmlTreeBuilder.process(commentToken);
        Comment expectedComment = new Comment("comment", baseUri);
        Comment actualComment = (Comment) xmlTreeBuilder.currentElement().child(0);
        assertEquals(expectedComment.getData(), actualComment.getData());
        // Add more assertions as needed to check the state of the Comment after processing a comment
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root>&#x26;<character>hello</character>&#xA;</root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        Token characterToken = new Token.Character("#x26;");
        xmlTreeBuilder.process(characterToken);
        assertEquals("&amp;", characterToken.getData());
        Token nextCharacterToken = new Token.Character("hello");
        xmlTreeBuilder.process(nextCharacterToken);
        TextNode expectedTextNode = new TextNode("hello", baseUri);
        TextNode actualTextNode = (TextNode) xmlTreeBuilder.currentElement().child(0).child(0);
        assertEquals(expectedTextNode.text(), actualTextNode.text());
        // Add more assertions as needed to check the state of the TextNode after processing a character
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<!DOCTYPE root SYSTEM \"http://example.com/root.dtd\">";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        Token doctypeToken = new Token.Doctype("root", null, "http://example.com/root.dtd", baseUri);
        xmlTreeBuilder.process(doctypeToken);
        DocumentType expectedDocumentType = new DocumentType("root", null, "http://example.com/root.dtd", baseUri);
        DocumentType actualDocumentType = (DocumentType) xmlTreeBuilder.currentElement();
        assertEquals(expectedDocumentType.nodeName(), actualDocumentType.nodeName());
        assertEquals(expectedDocumentType.getPublicIdentifier(), actualDocumentType.getPublicIdentifier());
        assertEquals(expectedDocumentType.getSystemIdentifier(), actualDocumentType.getSystemIdentifier());
        assertEquals(expectedDocumentType.baseUri(), actualDocumentType.baseUri());
        // Add more assertions as needed to check the state of the DocumentType after processing a doctype
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><element attr='value'><subelement/></element></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        List<Node> expectedNodes = xmlTreeBuilder.parseFragment("<root><element attr='value'><subelement/></element></root>", baseUri, errors).iterator().toList();

        List<Node> actualNodes = xmlTreeBuilder.parseFragment(input, baseUri, errors);
        assertEquals(expectedNodes.size(), actualNodes.size());
        // Add more assertions as needed to check each Node in the parsed fragment
    }
}