package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.jsoup.helper.Validate.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Entities;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;

import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testTokeniserStateData() {
        CharacterReader reader = new CharacterReader("foo<bar>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        TokeniserState dataState = TokeniserState.Data;

        tokeniser.transition(dataState);
        Token stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('f', stateToken.charSequence().toString().charAt(0));

        tokeniser.emit('o');
        tokeniser.emit('o');

        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, stateToken.type);
        StartTag startTag = (StartTag) stateToken;
        assertEquals("bar", startTag.tagName);
        assertNull(startTag.selfClosing);
        assertFalse(startTag.finalised);
        assertEquals(0, startTag.attributes.size());

        tokeniser.emitTagPending();

        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('>', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(TokeniserState.AfterStartTag);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('a', stateToken.charSequence().toString().charAt(0));

        tokeniser.emit('r');

        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('>', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(TokeniserState.BeforeEndTag);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, stateToken.type);
        EndTag endTag = (EndTag) stateTag;
        assertEquals("bar", endTag.tagName);
        assertNull(endTag.attributes);
        assertFalse(endTag.finalised);

        tokeniser.emitCommentPending();
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('/', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(TokeniserState.AfterComment);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('*', stateToken.charSequence().toString().charAt(0));

        tokeniser.consumeCharacterReference((char) 0x00, false);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals(';', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(TokeniserState.BeforeCharacterReference);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('&', stateToken.charSequence().toString().charAt(0));

        tokeniser.emit(new Character(""));
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals(';', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(TokeniserState.AfterCharacterReference);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('a', stateToken.charSequence().toString().charAt(0));

        // Test error handling
        tokeniser.error(TokeniserState.Data);
        stateToken = tokeniser.read();
        assertNotNull(stateToken);
        assertTrue(stateToken.type == Token.TokenType.Error);
        ParseError error = (ParseError) stateToken;
        assertEquals("Unexpected character 'a' in input state [Data]", error.getMessage());
    }

    @Test
    public void testTokeniserStateAfterStartTag() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        TokeniserState dataState = TokeniserState.Data;
        TokeniserState afterStartTag = TokeniserState.AfterStartTag;

        tokeniser.transition(dataState);
        Token stateToken = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, stateToken.type);
        StartTag startTag = (StartTag) stateToken;
        assertEquals("html", startTag.tagName);
        assertNull(startTag.selfClosing);
        assertFalse(startTag.finalised);
        assertEquals(0, startTag.attributes.size());

        tokeniser.emitTagPending();

        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals(' ', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(afterStartTag);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('<', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(TokeniserState.BeforeTagName);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('h', stateToken.charSequence().toString().charAt(0));

        tokeniser.consumeCharacterReference((char) 0x00, true);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('t', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(TokeniserState.AfterTagName);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('>', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(TokeniserState.BeforeEndTag);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('/', stateToken.charSequence().toString().charAt(0));

        tokeniser.consumeCharacterReference((char) 0x00, false);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('h', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(TokeniserState.AfterTagName);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('>', stateToken.charSequence().toString().charAt(0));

        tokeniser.emitCommentPending();
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('!', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(TokeniserState.AfterComment);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('-', stateToken.charSequence().toString().charAt(0));

        tokeniser.advanceTransition(TokeniserState.Comment);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('-', stateToken.charSequence().toString().charAt(0));

        tokeniser.consumeCharacterReference((char) 0x00, false);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('-', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(TokeniserState.AfterCharacterReference);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('>', stateToken.charSequence().toString().charAt(0));

        tokeniser.emit(new Comment(""));
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals(' ', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(TokeniserState.BeforeEndTag);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, stateToken.type);
        EndTag endTag = (EndTag) stateTag;
        assertEquals("title", endTag.tagName);
        assertNull(endTag.attributes);
        assertFalse(endTag.finalised);

        tokeniser.emitEndTagPending();

        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('>', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(dataState);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('<', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(TokeniserState.BeforeTagName);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('h', stateToken.charSequence().toString().charAt(0));

        tokeniser.consumeCharacterReference((char) 0x00, true);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('e', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(TokeniserState.AfterTagName);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals(' ', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(afterStartTag);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('d', stateToken.charSequence().toString().charAt(0));

        tokeniser.consumeCharacterReference((char) 0x00, false);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('d', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(TokeniserState.AfterTagName);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('>', stateToken.charSequence().toString().charAt(0));

        tokeniser.emitDoctypePending();
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('<', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(TokeniserState.BeforeDoctypeName);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('d', stateToken.charSequence().toString().charAt(0));

        tokeniser.consumeCharacterReference((char) 0x00, true);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('o', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(TokeniserState.AfterDoctypeName);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals(' ', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(TokeniserState.BeforePublicID);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('<', stateToken.charSequence().toString().charAt(0));

        tokeniser.consumeCharacterReference((char) 0x00, false);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('p', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(TokeniserState.AfterPublicID);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals(' ', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(TokeniserState.BeforeSystemID);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('>', stateToken.charSequence().toString().charAt(0));

        tokeniser.emitDoctypePending();
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals(' ', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(dataState);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('<', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(TokeniserState.BeforeTagName);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('b', stateToken.charSequence().toString().charAt(0));

        tokeniser.consumeCharacterReference((char) 0x00, true);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('o', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(TokeniserState.AfterTagName);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals(' ', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(afterStartTag);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('t', stateToken.charSequence().toString().charAt(0));

        tokeniser.consumeCharacterReference((char) 0x00, false);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals('i', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(TokeniserState.AfterTagName);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, stateToken.type);
        assertEquals(' ', stateToken.charSequence().toString().charAt(0));

        tokeniser.transition(afterStartTag);
        stateToken = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, stateToken.type);
        StartTag tag = (StartTag) stateToken;
        assertEquals("script", tag.tagName);
        assertFalse(tag.selfClosing);
        assertFalse(tag.finalised);
        List<Attribute> attributes = tag.attributes;
        List<Attribute> expectedAttributes = new ArrayList<>();
        expectedAttributes.add(new Attribute("type", "text/javascript"));
        expectedAttributes.add(new Attribute("src", ""));
        assertEquals(expectedAttributes, attributes);
    }
}