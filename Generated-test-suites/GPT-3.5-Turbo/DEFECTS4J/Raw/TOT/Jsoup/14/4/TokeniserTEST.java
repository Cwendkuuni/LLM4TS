package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        tokeniser = new Tokeniser(reader);
    }

    @Test
    public void testRead_emptyInput_returnsNull() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit_string_returnsCharacterToken() {
        tokeniser.emit("test");
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("test", ((Token.Character) token).getData());
    }

    @Test
    public void testEmit_char_returnsCharacterToken() {
        tokeniser.emit('a');
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("a", ((Token.Character) token).getData());
    }

    @Test
    public void testEmitStartTag_selfClosingFlagSet_emitsStartTagToken() {
        Token.StartTag expectedStartTag = new Token.StartTag();
        expectedStartTag.selfClosing = true;

        tokeniser.emit(expectedStartTag);

        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals(expectedStartTag, token);
    }

    @Test
    public void testEmitEndTag_withAttributes_present_errors() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.attributes.add(new Attribute("name", "value"));

        tokeniser.emit(endTag);

        // Error should be logged
        assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        assertEquals("Attributes incorrectly present on end tag", error.getErrorMessage());
    }

    @Test
    public void testAdvanceTransition_movesToNextState() {
        TokeniserState nextState = TokeniserState.BeforeDoctypeName;
        tokeniser.advanceTransition(nextState);
        assertEquals(nextState, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag_setsFlagTrue() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference_emptyReader_returnsNull() {
        Character character = tokeniser.consumeCharacterReference(null, false);
        assertNull(character);
    }

    @Test
    public void testConsumeCharacterReference_additionalCharacterSameAsCurrent_returnsNull() {
        CharacterReader reader = new CharacterReader("a");
        tokeniser = new Tokeniser(reader);

        Character character = tokeniser.consumeCharacterReference('a', false);
        assertNull(character);
    }

    @Test
    public void testConsumeCharacterReference_characterIsTabNewlineFormfeedLessThanAmpersand_returnsNull() {
        CharacterReader reader = new CharacterReader("\t\n\f<&");
        tokeniser = new Tokeniser(reader);

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertNull(character);
    }

    @Test
    public void testConsumeCharacterReference_numberedCharacterReference_withoutSemi_returnsNull() {
        CharacterReader reader = new CharacterReader("#1234");
        tokeniser = new Tokeniser(reader);

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertNull(character);
    }

    @Test
    public void testConsumeCharacterReference_numberedCharacterReference_hexMode_withSemi_returnsCharacter() {
        CharacterReader reader = new CharacterReader("#x20;");
        tokeniser = new Tokeniser(reader);

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertNotNull(character);
        assertEquals(' ', character.charValue());
    }

    @Test
    public void testConsumeCharacterReference_numberedCharacterReference_hexMode_withoutSemi_returnsReplacementCharacter() {
        CharacterReader reader = new CharacterReader("#x20");
        tokeniser = new Tokeniser(reader);

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertNotNull(character);
        assertEquals(Tokeniser.replacementChar, character.charValue());
    }

    @Test
    public void testConsumeCharacterReference_namedCharacterReference_withoutSemi_returnsNull() {
        CharacterReader reader = new CharacterReader("amp");
        tokeniser = new Tokeniser(reader);

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertNull(character);
    }

    @Test
    public void testConsumeCharacterReference_namedCharacterReference_withSemi_returnsCharacter() {
        CharacterReader reader = new CharacterReader("lt;");
        tokeniser = new Tokeniser(reader);

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertNotNull(character);
        assertEquals('<', character.charValue());
    }

    @Test
    public void testConsumeCharacterReference_namedCharacterReference_withAdditionalCharactersInAttribute_returnsNull() {
        CharacterReader reader = new CharacterReader("gt=");
        tokeniser = new Tokeniser(reader);

        Character character = tokeniser.consumeCharacterReference('=', true);
        assertNull(character);
    }

    @Test
    public void testCreateTagPending_start_returnsStartTag() {
        Token.Tag startTag = tokeniser.createTagPending(true);
        assertNotNull(startTag);
        assertTrue(startTag instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending_emitsTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(tag, token);
    }

    @Test
    public void testCreateCommentPending_createsCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending_emitsCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.commentPending.setData("Comment");
        tokeniser.emitCommentPending();

        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Comment, token.type);
        assertEquals("Comment", ((Token.Comment) token).getData());
    }

    @Test
    public void testCreateDoctypePending_createsDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending_emitsDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.setName("doctype");
        tokeniser.emitDoctypePending();

        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Doctype, token.type);
        assertEquals("doctype", ((Token.Doctype) token).getName());
    }

    @Test
    public void testCreateTempBuffer_createsTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken_tagNameEqualsLastStartTag_returnsTrue() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        tokeniser.lastStartTag = startTag;
        tokeniser.tagPending = startTag;

        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagToken_tagNameNotEqualsLastStartTag_returnsFalse() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        tokeniser.lastStartTag = startTag;

        Token.Tag endTag = new Token.EndTag();
        endTag.tagName = "p";
        tokeniser.tagPending = endTag;

        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors_defaultValue_returnsTrue() {
        assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors_changesValue() {
        tokeniser.setTrackErrors(false);
        assertFalse(tokeniser.isTrackErrors());
    }

    @Test
    public void testError_logsError() {
        TokeniserState state = TokeniserState.BeforeDoctype;

        tokeniser.error(state);

        assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        assertEquals("Unexpected character in input", error.getErrorMessage());
        assertEquals(state, error.getState());
    }

    @Test
    public void testEofError_logsEofError() {
        TokeniserState state = TokeniserState.BeforeDoctype;

        tokeniser.eofError(state);

        assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        assertEquals("Unexpectedly reached end of file (EOF)", error.getErrorMessage());
        assertEquals(state, error.getState());
    }

    @Test
    public void testCharacterReferenceError_logsCharacterReferenceError() {
        tokeniser.characterReferenceError();

        assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        assertEquals("Invalid character reference", error.getErrorMessage());
    }

    @Test
    public void testError_withMessage_logsError() {
        String errorMsg = "Error message";

        tokeniser.error(errorMsg);

        assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        assertEquals(errorMsg, error.getErrorMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS_alwaysReturnsTrue() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}