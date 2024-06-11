package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    @Test
    public void testReadCharacter() {
        CharacterReader reader = new CharacterReader("abc");
        Tokeniser tokeniser = new Tokeniser(reader);
        Token token;

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('a', token.getData());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('b', token.getData());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('c', token.getData());

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadStartTag() {
        CharacterReader reader = new CharacterReader("<html>");
        Tokeniser tokeniser = new Tokeniser(reader);
        Token token;

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("html", token.getData().tagName);

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadEndTag() {
        CharacterReader reader = new CharacterReader("<html></html>");
        Tokeniser tokeniser = new Tokeniser(reader);
        Token token;

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("html", token.getData().tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("html", token.getData().tagName);

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadComment() {
        CharacterReader reader = new CharacterReader("<!-- This is a comment -->");
        Tokeniser tokeniser = new Tokeniser(reader);
        Token token;

        token = tokeniser.read();
        assertEquals(Token.TokenType.Comment, token.type);
        assertEquals(" This is a comment ", token.getData().text);

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadDoctype() {
        CharacterReader reader = new CharacterReader("<!DOCTYPE html>");
        Tokeniser tokeniser = new Tokeniser(reader);
        Token token;

        token = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, token.type);
        assertEquals("html", token.getData().systemId);
        assertEquals("", token.getData().publicId);
        assertEquals("", token.getData().internalSubset);

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadEmptyElement() {
        CharacterReader reader = new CharacterReader("<img />");
        Tokeniser tokeniser = new Tokeniser(reader);
        Token token;

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("img", token.getData().tagName);
        assertEquals(true, token.getData().isSelfClosing());

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadElementWithAttributes() {
        CharacterReader reader = new CharacterReader("<img src=\"image.png\" alt=\"Image\">");
        Tokeniser tokeniser = new Tokeniser(reader);
        Token token;

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("img", token.getData().tagName);

        List<Token.Attribute> attributes = token.getData().attributes;
        assertEquals(2, attributes.size());

        Token.Attribute srcAttribute = attributes.get(0);
        assertEquals("src", srcAttribute.name);
        assertEquals("image.png", srcAttribute.value);

        Token.Attribute altAttribute = attributes.get(1);
        assertEquals("alt", altAttribute.name);
        assertEquals("Image", altAttribute.value);

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadText() {
        CharacterReader reader = new CharacterReader("Some text <img src=\"image.png\">");
        Tokeniser tokeniser = new Tokeniser(reader);
        Token token;

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("Some ", token.getData().getText());

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("img", token.getData().tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(" src=\"image.png\">", token.getData().getText());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("", token.getData().getText());

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadCharacterReference() {
        CharacterReader reader = new CharacterReader("&eacute;");
        Tokeniser tokeniser = new Tokeniser(reader);
        Token token;

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("&#233;", token.getData().getText());

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadNamedCharacterReference() {
        CharacterReader reader = new CharacterReader("&euro;");
        Tokeniser tokeniser = new Tokeniser(reader);
        Token token;

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("\u20AC", token.getData().getText());

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadInvalidCharacterReference() {
        CharacterReader reader = new CharacterReader("&foo;");
        Tokeniser tokeniser = new Tokeniser(reader);
        Token token;

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(Tokeniser.replacementChar, token.getData().getText());

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadCharacterReferenceWithAdditionalCharacter() {
        CharacterReader reader = new CharacterReader("&eacute;x");
        Tokeniser tokeniser = new Tokeniser(reader);
        Token token;

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("&#233;", token.getData().getText());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("x", token.getData().getText());

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadCharacterReferenceInAttributeValue() {
        CharacterReader reader = new CharacterReader("<img src=\"image.png&eacute;.jpg\" alt=\"Image\">");
        Tokeniser tokeniser = new Tokeniser(reader);
        Token token;

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("img", token.getData().tagName);

        List<Token.Attribute> attributes = token.getData().attributes;
        Token.Attribute srcAttribute = attributes.get(0);
        assertEquals("src", srcAttribute.name);
        assertEquals("image.png&#233;.jpg", srcAttribute.value);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("", token.getData().getText());

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadCharacterReferenceInScriptTag() {
        CharacterReader reader = new CharacterReader("<script type=\"text/javascript\">var a = '\u00E9';</script>");
        Tokeniser tokeniser = new Tokeniser(reader);
        Token token;

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("script", token.getData().tagName);

        List<Token.Attribute> attributes = token.getData().attributes;
        Token.Attribute typeAttribute = attributes.get(0);
        assertEquals("type", typeAttribute.name);
        assertEquals("text/javascript", typeAttribute.value);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("var a = ", token.getData().getText());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("'\u00E9';", token.getData().getText());

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("script", token.getData().tagName);

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadCharacterReferenceInHtmlComment() {
        CharacterReader reader = new CharacterReader("<!-- &eacute; -->");
        Tokeniser tokeniser = new Tokeniser(reader);
        Token token;

        token = tokeniser.read();
        assertEquals(Token.TokenType.Comment, token.type);
        assertEquals("&eacute;", token.getData().getText());

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadCharacterReferenceInXmlComment() {
        CharacterReader reader = new CharacterReader("<!-- &eacute; -->");
        Tokeniser tokeniser = new Tokeniser(reader);
        Token token;

        token = tokeniser.read();
        assertEquals(Token.TokenType.Comment, token.type);
        assertEquals(Tokeniser.replacementChar, token.getData().getText());

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadCharacterReferenceInScriptTagWithAdditionalCharacter() {
        CharacterReader reader = new CharacterReader("<!-- <script type=\"text/javascript\">var a = '\u00E9'x; </script> -->");
        Tokeniser tokeniser = new Tokeniser(reader);
        Token token;

        token = tokeniser.read();
        assertEquals(Token.TokenType.Comment, token.type);
        assertEquals("<!-- ", token.getData().getText());

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("script", token.getData().tagName);

        List<Token.Attribute> attributes = token.getData().attributes;
        Token.Attribute typeAttribute = attributes.get(0);
        assertEquals("type", typeAttribute.name);
        assertEquals("text/javascript", typeAttribute.value);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("var a = ", token.getData().getText());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("'\u00E9'x;", token.getData().getText());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("", token.getData().getText());

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("script", token.getData().tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Comment, token.type);
        assertEquals(" -->", token.getData().getText());

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadCharacterReferenceInScriptTagWithXmlDeclaration() {
        CharacterReader reader = new CharacterReader("<?!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n<script type=\"text/javascript\">var a = '\u00E9';</script>");
        Tokeniser tokeniser = new Tokeniser(reader);
        Token token;

        token = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, token.type);
        // Doctype details omitted for brevity

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("\n", token.getData().getText());

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("script", token.getData().tagName);

        List<Token.Attribute> attributes = token.getData().attributes;
        Token.Attribute typeAttribute = attributes.get(0);
        assertEquals("type", typeAttribute.name);
        assertEquals("text/javascript", typeAttribute.value);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("var a = ", token.getData().getText());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("'\u00E9';", token.getData().getText());

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("script", token.getData().tagName);

        token = tokeniser.read();
        assertNull(token);
    }
}