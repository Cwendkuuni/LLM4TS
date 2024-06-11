
package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void initialiseParseTest() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseErrorList parseErrorList = new ParseErrorList();
        xmlTreeBuilder.initialiseParse("input", "baseUri", parseErrorList);

        assertEquals(parseErrorList, xmlTreeBuilder.errors);
        assertFalse(xmlTreeBuilder.stack.isEmpty());
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void processStartTagTest() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(Token.TokenType.StartTag);
        token.data = "startTag";
        Element element = xmlTreeBuilder.insert(token.asStartTag());

        assertEquals("tag", element.tag.getName());
        assertEquals("baseUri", element.baseUri());
        assertEquals("startTag", element.attributes.get("name"));
        assertFalse(element.tag.isSelfClosing());
        assertTrue(xmlTreeBuilder.stack.contains(element));
    }

    @Test
    public void processStartTagSelfClosingTest() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(Token.TokenType.StartTag);
        token.data = "startTag";
        token.selfClosing = true;
        Element element = xmlTreeBuilder.insert(token.asStartTag());

        assertEquals("tag", element.tag.getName());
        assertEquals("baseUri", element.baseUri());
        assertEquals("startTag", element.attributes.get("name"));
        assertTrue(element.tag.isSelfClosing());
        assertFalse(xmlTreeBuilder.stack.contains(element));
    }

    @Test
    public void processEndTagTest() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(Token.TokenType.EndTag);
        token.data = "endTag";

        Element element1 = xmlTreeBuilder.insert(token.asStartTag());
        Element element2 = xmlTreeBuilder.stack.pop();
        xmlTreeBuilder.stack.add(element2);

        xmlTreeBuilder.popStackToClose(token.asEndTag());

        assertFalse(xmlTreeBuilder.stack.contains(element2));
    }

    @Test
    public void processEndTagNotFoundTest() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(Token.TokenType.EndTag);
        token.data = "endTag";

        Element element1 = xmlTreeBuilder.insert(token.asStartTag());
        Element element2 = xmlTreeBuilder.stack.pop();
        xmlTreeBuilder.stack.add(element2);

        token.data = "incorrectEndTag";
        xmlTreeBuilder.popStackToClose(token.asEndTag());

        assertTrue(xmlTreeBuilder.stack.contains(element2));
    }

    @Test
    public void processCommentTest() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(Token.TokenType.Comment);
        token.data = "comment";

        xmlTreeBuilder.insert(token.asComment());

        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof Comment);
        assertEquals("comment", xmlTreeBuilder.currentElement().childNodes().get(0).attributes.get("comment"));
    }

    @Test
    public void processCharacterTest() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(Token.TokenType.Character);
        token.data = "character";

        xmlTreeBuilder.insert(token.asCharacter());

        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof TextNode);
        assertEquals("character", xmlTreeBuilder.currentElement().childNodes().get(0).attributes.get("character"));
    }

    @Test
    public void processDoctypeTest() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(Token.TokenType.Doctype);
        token.data = "doctype";

        xmlTreeBuilder.insert(token.asDoctype());

        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof DocumentType);
        assertEquals("doctype", xmlTreeBuilder.currentElement().childNodes().get(0).attributes.get("doctype"));
    }
}
