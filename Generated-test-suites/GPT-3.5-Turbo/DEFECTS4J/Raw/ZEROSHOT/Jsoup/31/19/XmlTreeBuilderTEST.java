package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        assertEquals(input, xmlTreeBuilder.getInput());
        assertEquals(baseUri, xmlTreeBuilder.getBaseUri());
        assertEquals(errors, xmlTreeBuilder.getErrors());
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertEquals(Tag.valueOf("html"), xmlTreeBuilder.getCurrentElement().tag());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("div", "class", "container");

        Element element = xmlTreeBuilder.insert(startTag);

        assertEquals(Tag.valueOf("div"), element.tag());
        assertEquals("class", element.attributes().get("class"));
        assertEquals("container", element.attributes().get("container"));
        assertEquals(1, xmlTreeBuilder.getStack().size());
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("div");

        xmlTreeBuilder.insert(endTag);

        assertEquals(0, xmlTreeBuilder.getStack().size());
    }

    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment("This is a comment");

        xmlTreeBuilder.insert(comment);

        assertEquals(1, xmlTreeBuilder.getCurrentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.getCurrentElement().childNode(0) instanceof Comment);
        Comment commentNode = (Comment) xmlTreeBuilder.getCurrentElement().childNode(0);
        assertEquals("This is a comment", commentNode.getData());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character("This is a character");

        xmlTreeBuilder.insert(character);

        assertEquals(1, xmlTreeBuilder.getCurrentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.getCurrentElement().childNode(0) instanceof TextNode);
        TextNode textNode = (TextNode) xmlTreeBuilder.getCurrentElement().childNode(0);
        assertEquals("This is a character", textNode.getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");

        xmlTreeBuilder.insert(doctype);

        assertEquals(1, xmlTreeBuilder.getCurrentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.getCurrentElement().childNode(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) xmlTreeBuilder.getCurrentElement().childNode(0);
        assertEquals("html", documentType.getName());
        assertEquals("", documentType.getPublicIdentifier());
        assertEquals("", documentType.getSystemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("div");
        Element element1 = new Element(Tag.valueOf("div"));
        Element element2 = new Element(Tag.valueOf("span"));
        xmlTreeBuilder.getStack().push(element1);
        xmlTreeBuilder.getStack().push(element2);

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertEquals(element2, xmlTreeBuilder.getStack().peek());
    }

}