package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testTokeniserStateRead() {
        Tokeniser tokeniser = new Tokeniser(null);
        TokeniserState[] states = TokeniserState.values();

        for (TokeniserState state : states) {
            tokeniser.transition(state);
            assertEquals(state, tokeniser.getState());
            tokeniser.advanceTransition(TokeniserState.Data);
        }
    }

    @Test
    public void testRead() {
        CharacterReader characterReader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        Tokeniser tokeniser = new Tokeniser(characterReader);

        Token token;
        for (int i = 0; i < characterReader.size(); i++) {
            token = tokeniser.read();
            assertEquals(Token.Character.class, token.getClass());
            assertEquals(Character.toString(characterReader.current()), token.getData());
            characterReader.advance();
        }

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        CharacterReader characterReader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        Tokeniser tokeniser = new Tokeniser(characterReader);

        for (int i = 0; i < characterReader.size(); i++) {
            Token token = tokeniser.read();
            tokeniser.emit(token);
            token = tokeniser.read();
        }

        tokeniser.emit(new Token.Character("X"));
        token = tokeniser.read();
        assertEquals(Token.Character.class, token.getClass());
        assertEquals("X", token.getData());
    }

    @Test
    public void testEmitString() {
        CharacterReader characterReader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        Tokeniser tokeniser = new Tokeniser(characterReader);

        for (int i = 0; i < 5; i++) {
            tokeniser.emit("ab");
        }

        Token token = tokeniser.read();
        assertEquals(Token.Character.class, token.getClass());
        assertEquals("c", token.getData());
    }

    @Test
    public void testEmitCharacter() {
        CharacterReader characterReader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        Tokeniser tokeniser = new Tokeniser(characterReader);

        for (int i = 0; i < 5; i++) {
            tokeniser.emit((char) 97);
        }

        Token token = tokeniser.read();
        assertEquals(Token.Character.class, token.getClass());
        assertEquals("e", token.getData());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.createTagPending(true);
        tokeniser.createTagPending(true);
        tokeniser.transition(TokeniserState.Empty);
        tokeniser.emitTagPending();
        tokeniser.emitTagPending();

        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagTokenWithNullLastStartTag() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.createTagPending(true);
        tokeniser.createTagPending(true);
        tokeniser.transition(TokeniserState.Empty);
        tokeniser.emitTagPending();

        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagTokenWithDifferentTags() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.createTagPending(true);
        tokeniser.createTagPending(false);
        tokeniser.transition(TokeniserState.Empty);
        tokeniser.emitTagPending();
        tokeniser.emitTagPending();

        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test(expected = IllegalStateException.class)
    public void testReadWithEmitPending() {
        CharacterReader characterReader = new CharacterReader("abc");
        Tokeniser tokeniser = new Tokeniser(characterReader);
        tokeniser.emit(new Token.Character('a'));
        tokeniser.read();
    }

    @Test
    public void testTransition() {
        Tokeniser tokeniser = new Tokeniser(null);
        TokeniserState[] states = TokeniserState.values();

        for (TokeniserState state : states) {
            tokeniser.transition(state);
            assertEquals(state, tokeniser.getState());
        }

        tokeniser.transition(null);
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        Tokeniser tokeniser = new Tokeniser(null);
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testCharacterConsumed() {
        CharacterReader characterReader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        Tokeniser tokeniser = new Tokeniser(characterReader);

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals('a', character);
        character = tokeniser.consumeCharacterReference('b', false);
        assertNull(character);
        character = tokeniser.consumeCharacterReference("cd", false);
        assertEquals('d', character);

        character = tokeniser.consumeCharacterReference(null, true);
        assertNull(character);
    }

    @Test
    public void testCharacterConsumedWithAttribute() {
        CharacterReader characterReader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        Tokeniser tokeniser = new Tokeniser(characterReader);

        Character character = tokeniser.consumeCharacterReference(null, true);
        assertNull(character);

        character = tokeniser.consumeCharacterReference("x", true);
        assertEquals('x', character);
        character = tokeniser.consumeCharacterReference("y=", true);
        assertNull(character);
    }
}