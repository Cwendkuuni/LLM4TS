package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Attribute;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.Token.Tag;
import org.jsoup.parser.Tokeniser;

import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testTokeniser_InitialState() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.EndDocument, token.type);
    }

    @Test
    public void testTokeniser_Read_Character() {
        CharacterReader reader = new CharacterReader("a");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("a", token.data);
    }

    @Test
    public void testTokeniser_Read_Character_Eof() {
        CharacterReader reader = new CharacterReader("a");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.read();
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.EndDocument, token.type);
    }

    @Test
    public void testTokeniser_Read_EndTag() {
        CharacterReader reader = new CharacterReader("/html");;
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("html", token.data.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("html", token.data.tagName);
    }

    @Test
    public void testTokeniser_Read_StartTag() {
        CharacterReader reader = new CharacterReader("<html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("html", token.data.tagName);
    }

    @Test
    public void testTokeniser_Read_StartTag_SelfClosing() {
        CharacterReader reader = new CharacterReader("<img src='test.jpg' />");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertTrue(token.data.selfClosing);
        assertEquals("img", token.data.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("img", token.data.tagName);
    }

    @Test
    public void testTokeniser_Read_Comment() {
        CharacterReader reader = new CharacterReader("<!-- test -->");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Comment, token.type);
        assertEquals("test", token.data.getData());
    }

    @Test
    public void testTokeniser_Read_Doctype() {
        CharacterReader reader = new CharacterReader("<!DOCTYPE html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, token.type);
        assertEquals("html", token.data.getName());
    }

    @Test
    public void testTokeniser_Error() {
        CharacterReader reader = new CharacterReader("<html<");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.read();
        tokeniser.read();

        tokeniser.error("Test error");

        List<ParseError> parseErrors = errors.getErrors();
        assertEquals(1, parseErrors.size());
        assertEquals("Test error", parseErrors.get(0).getMessage());
        assertEquals("Unexpected character '<' in input state [Data]", parseErrors.get(0).getPositionMessage());
    }

    @Test
    public void testTokeniser_Error_EOF() {
        CharacterReader reader = new CharacterReader("<!--");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.read();
        tokeniser.read();
        tokeniser.read();

        tokeniser.eofError("Test error");

        List<ParseError> parseErrors = errors.getErrors();
        assertEquals(1, parseErrors.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", parseErrors.get(0).getMessage());
    }

    @Test
    public void testTokeniser_CharacterReference() {
        CharacterReader reader = new CharacterReader("&#x1F600;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals('\uD834\uDC00', character.intValue());

        reader.consume(';');

        character = tokeniser.consumeCharacterReference(null, false);
        assertNull(character);

        reader.rewindToMark();

        character = tokeniser.consumeCharacterReference("A", false);
        assertNull(character);
    }

    @Test
    public void testTokeniser_CharacterReference_Invalid() {
        CharacterReader reader = new CharacterReader("&#xZ;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals(Character.REPLACEMENT_CHAR, character);
    }

    @Test
    public void testTokeniser_CharacterReference_Invalid_Named() {
        CharacterReader reader = new CharacterReader("&foo;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertNull(character);
    }

    @Test
    public void testTokeniser_CreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Tag tagPending = tokeniser.createTagPending(true);
        assertEquals(StartTag.class, tagPending.getClass());
    }

    @Test
    public void testTokeniser_CreateTagPending_EndTag() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Tag tagPending = tokeniser.createTagPending(false);
        assertEquals(EndTag.class, tagPending.getClass());
    }

    @Test
    public void testTokeniser_EmitTagPending() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        TokeniserState state = new TokeniserStateData();
        tokeniser.transition(state);

        Token.StartTag startTag = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals(startTag.tagName, token.data.tagName);
    }

    @Test
    public void testTokeniser_EmitCommentPending() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        TokeniserState state = new TokeniserStateData();
        tokeniser.transition(state);

        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Comment, token.type);
        assertEquals("", token.data.getData());
    }

    @Test
    public void testTokeniser_EmitDoctypePending() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        TokeniserState state = new TokeniserStateData();
        tokeniser.transition(state);

        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, token.type);
        assertEquals("", token.data.getName());
    }

    @Test
    public void testTokeniser_IsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Token.StartTag startTag = new Token.StartTag("div");
        Token.EndTag endTag = new Token.EndTag("div");

        assertTrue(tokeniser.isAppropriateEndTagToken(startTag, endTag));
    }

    @Test
    public void testTokeniser_IsAppropriateEndTagToken_DifferentTag() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Token.StartTag startTag = new Token.StartTag("div");
        Token.EndTag endTag = new Token.EndTag("p");

        assertFalse(tokeniser.isAppropriateEndTagToken(startTag, endTag));
    }

    @Test
    public void testTokeniser_IsAppropriateEndTagToken_Null() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Token.StartTag startTag = new Token.StartTag("div");

        assertFalse(tokeniser.isAppropriateEndTagToken(startTag, null));
    }
}