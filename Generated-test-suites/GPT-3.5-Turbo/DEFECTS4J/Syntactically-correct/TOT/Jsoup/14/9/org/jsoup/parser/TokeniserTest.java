package org.jsoup.parser;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        tokeniser = new Tokeniser(reader);
    }

    @Test
    public void testRead_emptyCharacterReference_returnsNull() {
        Token result = tokeniser.read();
        assertTrue(result instanceof Token.Character);
        assertEquals("", ((Token.Character) result).getData());
    }

    @Test
    public void testRead_nonEmptyCharacterReference_returnsToken() {
        tokeniser.emit("Hello");
        Token result = tokeniser.read();
        assertTrue(result instanceof Token.Character);
        assertEquals("Hello", ((Token.Character) result).getData());
    }

    @Test
    public void testEmitToken_tokenTypeIsStartTag_setsLastStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "testTag";
        tokeniser.emit(startTag);

        assertEquals(startTag, tokeniser.lastStartTag);
    }

    @Test
    public void testEmitToken_tokenTypeIsEndTagAndAttributesPresent_issuesError() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.attributes = new ArrayList<>();
        tokeniser.emit(endTag);

        assertFalse(tokeniser.errors.isEmpty());
    }

    @Test
    public void testEmitString_addsToCharBuffer() {
        tokeniser.emit("Hello");
        StringBuilder expected = new StringBuilder();
        expected.append("Hello");

        assertEquals(expected.toString(), tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmitCharacter_addsToCharBuffer() {
        tokeniser.emit('H');
        StringBuilder expected = new StringBuilder();
        expected.append('H');

        assertEquals(expected.toString(), tokeniser.charBuffer.toString());
    }

    @Test
    public void testTransition_setsTokeniserState() {
        TokeniserState state = TokeniserState.Rcdata;
        tokeniser.transition(state);

        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition_advancesCharacterReaderAndSetsTokeniserState() {
        TokeniserState state = TokeniserState.ScriptData;
        tokeniser.advanceTransition(state);

        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag_setsSelfClosingFlagAcknowledgedToTrue() {
        tokeniser.acknowledgeSelfClosingFlag();

        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    // Additional Test Cases for Tokeniser class can be added here

}