package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.parser.Tokeniser;
import org.jsoup.helper.Validate;
import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testRead() {
        CharacterReader reader = new CharacterReader("abcd<e>efg");
        Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
        Token token;

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('a', token.charPositionInLine, 1);
        assertEquals('a', token.charPositionInElement, 1);
        assertEquals('a', token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('b', token.charPositionInLine, 2);
        assertEquals('b', token.charPositionInElement, 2);
        assertEquals('b', token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("e", token.tagName);
        assertEquals(1, token.charPositionInLine);
        assertEquals(2, token.charPositionInElement);
        assertNull(token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('e', token.charPositionInLine, 3);
        assertEquals('e', token.charPositionInElement, 3);
        assertEquals('e', token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('f', token.charPositionInLine, 4);
        assertEquals('f', token.charPositionInElement, 4);
        assertEquals('f', token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("e", token.tagName);
        assertEquals(4, token.charPositionInLine);
        assertEquals(5, token.charPositionInElement);
        assertNull(token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('g', token.charPositionInLine, 5);
        assertEquals('g', token.charPositionInElement, 5);
        assertEquals('g', token.data);

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        CharacterReader reader = new CharacterReader("abcd<e>efg");
        Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
        Token token;

        token = tokeniser.read();
        tokeniser.emit(token);

        token = tokeniser.read();
        tokeniser.emit(token);

        token = tokeniser.read();
        Token tagToken = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tokeniser.emit(tagToken);

        token = tokeniser.read();
        tokeniser.emit(token);

        token = tokeniser.read();
        tokeniser.emit(token);

        token = tokeniser.read();
        tokeniser.emit(token);

        token = tokeniser.read();
        tokeniser.emit(token);

        token = tokeniser.read();
        assertNull(token);

        Validate.assertEquals(Token.TokenType.StartTag, tokeniser.getState().getStateType());
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("#x1F4A9;");
        Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
        int[] result;

        result = tokeniser.consumeCharacterReference(null, false);
        assertEquals(0xD835, result[0]);
        assertEquals(0xDD1E, result[1]);

        reader.setPosition(0);
        result = tokeniser.consumeCharacterReference(null, true);
        assertNull(result);

        reader.setPosition(0);
        result = tokeniser.consumeCharacterReference('X', false);
        assertNull(result);

        reader.setPosition(0);
        result = tokeniser.consumeCharacterReference('x', true);
        assertNull(result);

        reader.setPosition(0);
        result = tokeniser.consumeCharacterReference('a', false);
        assertNull(result);

        reader.setPosition(0);
        result = tokeniser.consumeCharacterReference("abc", false);
        assertNull(result);
    }

    @Test
    public void testErrorHandling() {
        CharacterReader reader = new CharacterReader("abcd<e>efg");
        Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());

        tokeniser.error(TokeniserState.TagStart);
        tokeniser.read();
        tokeniser.read();
        tokeniser.error(TokeniserState.TagStart);

        assertEquals(2, tokeniser.errors.size());
        assertEquals("Unexpected character 'e' in input state [TagStart]", tokeniser.errors.get(0).toString());
        assertEquals("Unexpected character '<' in input state [TagStart]", tokeniser.errors.get(1).toString());
    }
}