
package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        tokeniser = new Tokeniser(reader);
    }

    @Test
    public void testRead_returnsEmptyCharacterTokenWhenCharBufferIsEmpty() {
        Token result = tokeniser.read();
        assertEquals(Token.TokenType.Character, result.type);
        assertEquals("", ((Token.Character) result).getData());
    }

    @Test
    public void testRead_returnsPendingEmitWhenCharBufferIsNotEmpty() {
        tokeniser.emit("test");
        Token result = tokeniser.read();
        assertEquals(Token.TokenType.Character, result.type);
        assertEquals("test", ((Token.Character) result).getData());
    }

    @Test
    public void testRead_returnsPendingEmitWhenCharBufferIsNotEmptyAfterMultipleReads() {
        tokeniser.emit("hello ");
        tokeniser.emit("world");
        Token result1 = tokeniser.read();
        Token result2 = tokeniser.read();
        assertEquals(Token.TokenType.Character, result1.type);
        assertEquals("hello world", ((Token.Character) result1).getData());
        assertEquals(Token.TokenType.Character, result2.type);
        assertEquals("", ((Token.Character) result2).getData());
    }

    @Test
    public void testEmit_tagsAreEqual_setsEmitPendingAndEmitStartTag() {
        Token.Tag tag = tokeniser.createTagPending(true);
        tag.attributes.put("key", "value");
        tokeniser.emitTagPending();
        Token result = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, result.type);
        assertEquals(true, ((Token.StartTag) result).selfClosing);
        assertEquals("key", ((Token.StartTag) result).attributes.get(0).getKey());
        assertEquals("value", ((Token.StartTag) result).attributes.get(0).getValue());
    }

    @Test
    public void testEmit_attributesIncorrectlyPresent_errorIsAdded() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.attributes.put("key", "value");
        tokeniser.emit(endTag);
        Token result = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, result.type);
        assertEquals(1, tokeniser.errors.size());
        assertEquals("Attributes incorrectly present on end tag", tokeniser.errors.get(0).getErrorMsg());
    }

    @Test
    public void testEmit_acknowledgeSelfClosingFlag_setsSelfClosingFlagAcknowledgedToTrue() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference_additionalAllowedCharacterIsNull_returnsNull() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        Character result = tokeniser.consumeCharacterReference(null, false);
        assertNull(result);
    }

    @Test
    public void testConsumeCharacterReference_additionalAllowedCharacterIsEqualToCurrentCharacter_returnsNull() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("a"));
        Character result = tokeniser.consumeCharacterReference('a', false);
        assertNull(result);
    }

    @Test
    public void testConsumeCharacterReference_additionalAllowedCharacterIsNotEqualToCurrentCharacter_returnsCurrentCharacter() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("a"));
        Character result = tokeniser.consumeCharacterReference('b', false);
        assertEquals(Character.valueOf('a'), result);
    }

    @Test
    public void testConsumeCharacterReference_matchConsumeNumbered_succesfullyParsesCharacterReference() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("&#65;"));
        Character result = tokeniser.consumeCharacterReference(null, false);
        assertEquals(Character.valueOf('A'), result);
    }

    @Test
    public void testConsumeCharacterReference_mismatchConsumeNumbered_errorOccurs() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("&#;"));
        Character result = tokeniser.consumeCharacterReference(null, false);
        assertNull(result);
        assertEquals(1, tokeniser.errors.size());
        assertEquals("Invalid character reference", tokeniser.errors.get(0).getErrorMsg());
    }

    @Test
    public void testConsumeCharacterReference_doesNotMatchAny_returnsNull() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("\t"));
        Character result = tokeniser.consumeCharacterReference(null, false);
        assertNull(result);
    }

    @Test
    public void testConsumeCharacterReference_matchesLetterSequence_returnsCharacter() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("amp;"));
        Character result = tokeniser.consumeCharacterReference(null, false);
        assertEquals(Character.valueOf('&'), result);
    }

    @Test
    public void testConsumeCharacterReference_doesNotMatchLetterSequence_returnsNull() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("&abc;"));
        Character result = tokeniser.consumeCharacterReference(null, false);
        assertNull(result);
    }

    @Test
    public void testEmitError_errorNotTracked_noErrorIsAdded() {
        tokeniser.setTrackErrors(false);
        tokeniser.error("Error");
        assertEquals(0, tokeniser.errors.size());
    }

    @Test
    public void testEmitError_errorTracked_errorIsAdded() {
        tokeniser.setTrackErrors(true);
        tokeniser.error("Error");
        assertEquals(1, tokeniser.errors.size());
        assertEquals("Error", tokeniser.errors.get(0).getErrorMsg());
    }

    @Test
    public void testCurrentNodeInHtmlNS_nodeIsNull_returnsFalse() {
        boolean result = tokeniser.currentNodeInHtmlNS();
        assertFalse(result);
    }

    /*
     * Add more test methods for other methods in the Tokeniser class
     */

}

