package org.jsoup.parser;

import org.jsoup.parser.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

    @Test
    public void testRead() {
        CharacterReader reader = new CharacterReader("test");
        Tokeniser tokeniser = new Tokeniser(reader);

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("test", ((Token.Character)token).getData());
    }

    @Test
    public void testEmit() {
        CharacterReader reader = new CharacterReader("data");
        Tokeniser tokeniser = new Tokeniser(reader);

        tokeniser.emit("data");

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("data", ((Token.Character)token).getData());
    }

    @Test
    public void testEmitTagPending() {
        CharacterReader reader = new CharacterReader("tagName");
        Tokeniser tokeniser = new Tokeniser(reader);

        Token.Tag tagPending = tokeniser.createTagPending(true);

        tagPending.tagName = "tagName";

        tokeniser.emitTagPending();

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("tagName", ((Token.StartTag)token).tagName);
    }

    @Test
    public void testEmitCommentPending() {
        CharacterReader reader = new CharacterReader("comment");
        Tokeniser tokeniser = new Tokeniser(reader);

        tokeniser.createCommentPending();

        tokeniser.emitCommentPending();

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.Comment, token.type);
        assertEquals("comment", ((Token.Comment)token).getData());
    }

    @Test
    public void testEmitDoctypePending() {
        CharacterReader reader = new CharacterReader("doctype");
        Tokeniser tokeniser = new Tokeniser(reader);

        tokeniser.createDoctypePending();

        tokeniser.emitDoctypePending();

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.Doctype, token.type);
        assertEquals("doctype", ((Token.Doctype)token).getName());
    }

    @Test
    public void testTransition() {
        CharacterReader reader = new CharacterReader("data");
        Tokeniser tokeniser = new Tokeniser(reader);

        tokeniser.transition(TokeniserState.Data);

        TokeniserState state = tokeniser.getState();

        assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void testAdvanceTransition() {
        CharacterReader reader = new CharacterReader("data");
        Tokeniser tokeniser = new Tokeniser(reader);

        tokeniser.advanceTransition(TokeniserState.Data);

        TokeniserState state = tokeniser.getState();

        assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void testAknowledgeSelfClosingFlag() {
        CharacterReader reader = new CharacterReader("data");
        Tokeniser tokeniser = new Tokeniser(reader);

        tokeniser.acknowledgeSelfClosingFlag();

        assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("ref");
        Tokeniser tokeniser = new Tokeniser(reader);

        Character reference = tokeniser.consumeCharacterReference(null, true);

        assertEquals('r', reference.charValue());
    }

    @Test
    public void createTagPending() {
        CharacterReader reader = new CharacterReader("data");
        Tokeniser tokeniser = new Tokeniser(reader);

        Token.Tag tagPending = tokeniser.createTagPending(true);

        tagPending.tagName = "tagName";

        assertEquals("tagName", tagPending.tagName);
    }

    @Test
    public void testGetCurrentNodeInHtmlNS() {
        CharacterReader reader = new CharacterReader("data");
        Tokeniser tokeniser = new Tokeniser(reader);

        boolean inHtmlNS = tokeniser.currentNodeInHtmlNS();

        assertTrue(inHtmlNS);
    }
}
