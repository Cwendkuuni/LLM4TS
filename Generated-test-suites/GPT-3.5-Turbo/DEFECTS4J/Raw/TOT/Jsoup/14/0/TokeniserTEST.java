import org.jsoup.helper.Validate;
import org.jsoup.parser.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        tokeniser = new Tokeniser(reader);
    }

    @Test
    public void testRead_EmptyToken_ReturnsNull() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testRead_CharacterToken_ReturnsCharacterToken() {
        CharacterReader reader = new CharacterReader("hello");
        tokeniser = new Tokeniser(reader);

        Token token = tokeniser.read();

        assertTrue(token instanceof Token.Character);
        assertEquals("hello", ((Token.Character) token).getData());
    }

    @Test
    public void testRead_StartTagToken_ReturnsStartTagToken() {
        CharacterReader reader = new CharacterReader("<div>");
        tokeniser = new Tokeniser(reader);

        Token token = tokeniser.read();

        assertTrue(token instanceof Token.StartTag);
        assertEquals("div", ((Token.StartTag) token).tagName());
    }

    @Test
    public void testRead_EndTagToken_ReturnsEndTagToken() {
        CharacterReader reader = new CharacterReader("</div>");
        tokeniser = new Tokeniser(reader);

        Token token = tokeniser.read();

        assertTrue(token instanceof Token.EndTag);
        assertEquals("div", ((Token.EndTag) token).tagName());
    }

    @Test
    public void testEmit_StartTagToken_SetsLastStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.setTagName("div");
        tokeniser.emit(startTag);

        assertEquals(startTag, tokeniser.getLastStartTag());
    }

    @Test
    public void testEmit_EndTagToken_ErrorIfAttributesPresent() {
        Token.EndTag endTag = new Token.EndTag();
        Attribute attribute = new Attribute("id", "myId");
        endTag.addAttribute(attribute);
        tokeniser.emit(endTag);

        assertEquals(1, tokeniser.getErrors().size());
        assertEquals("Attributes incorrectly present on end tag", tokeniser.getErrors().get(0).getErrorMsg());
    }

    @Test
    public void testEmit_StringToken_AppendsToStringBuilder() {
        String str = "hello";
        tokeniser.emit(str);

        assertEquals(str, tokeniser.getCharBuffer().toString());
    }

    @Test
    public void testEmit_CharToken_AppendsToStringBuilder() {
        char c = 'a';
        tokeniser.emit(c);

        assertEquals(String.valueOf(c), tokeniser.getCharBuffer().toString());
    }

    @Test
    public void testEmit_TagToken_SetsSelfClosingFlagAcknowledgedToFalse() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.setSelfClosing(true);
        tokeniser.emit(startTag);

        assertFalse(tokeniser.isSelfClosingFlagAcknowledged());
    }

    @Test
    public void testTransition_SetsState() {
        TokeniserState state = TokeniserState.RCDATA;
        tokeniser.transition(state);

        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition_AdvancesCharacterAndSetsState() {
        CharacterReader reader = new CharacterReader("hello");
        tokeniser = new Tokeniser(reader);

        TokeniserState state = TokeniserState.RCDATA;
        tokeniser.advanceTransition(state);

        assertEquals(state, tokeniser.getState());
        assertEquals('e', reader.current());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag_SetsSelfClosingFlagAcknowledgedToTrue() {
        tokeniser.acknowledgeSelfClosingFlag();

        assertTrue(tokeniser.isSelfClosingFlagAcknowledged());
    }

    @Test
    public void testConsumeCharacterReference_EmptyReader_ReturnsNull() {
        Character character = tokeniser.consumeCharacterReference(null, false);

        assertNull(character);
    }

    @Test
    public void testConsumeCharacterReference_AdditionalAllowedCharacterMatches_ReturnsNull() {
        CharacterReader reader = new CharacterReader("&#65;");
        tokeniser = new Tokeniser(reader);

        Character character = tokeniser.consumeCharacterReference('A', false);

        assertNull(character);
    }

    @Test
    public void testConsumeCharacterReference_ConsumeNumbered_ReturnsCharacter() {
        CharacterReader reader = new CharacterReader("&#65;");
        tokeniser = new Tokeniser(reader);

        Character character = tokeniser.consumeCharacterReference(null, false);

        assertNotNull(character);
        assertEquals('A', character.charValue());
    }

    @Test
    public void testConsumeCharacterReference_ConsumeNumberedWithHex_ReturnsCharacter() {
        CharacterReader reader = new CharacterReader("&#x41;");
        tokeniser = new Tokeniser(reader);

        Character character = tokeniser.consumeCharacterReference(null, false);

        assertNotNull(character);
        assertEquals('A', character.charValue());
    }

    @Test
    public void testConsumeCharacterReference_ConsumeNumbered_EmptyReference_ReturnsReplacementCharacter() {
        CharacterReader reader = new CharacterReader("&#;");
        tokeniser = new Tokeniser(reader);

        Character character = tokeniser.consumeCharacterReference(null, false);

        assertNotNull(character);
        assertEquals('\uFFFD', character.charValue());
    }

    @Test
    public void testConsumeCharacterReference_ConsumeNamed_ReturnsCharacter() {
        CharacterReader reader = new CharacterReader("&quot;");
        tokeniser = new Tokeniser(reader);

        Character character = tokeniser.consumeCharacterReference(null, false);

        assertNotNull(character);
        assertEquals('"', character.charValue());
    }

    @Test
    public void testConsumeCharacterReference_ConsumeNamed_InvalidCharacter_ReturnsNull() {
        CharacterReader reader = new CharacterReader("&invalid;");
        tokeniser = new Tokeniser(reader);

        Character character = tokeniser.consumeCharacterReference(null, false);

        assertNull(character);
    }

    @Test
    public void testCreateTagPending_CreateStartTag_ReturnsStartTag() {
        Token.Tag tagPending = tokeniser.createTagPending(true);

        assertNotNull(tagPending);
        assertTrue(tagPending instanceof Token.StartTag);
    }

    @Test
    public void testCreateTagPending_CreateEndTag_ReturnsEndTag() {
        Token.Tag tagPending = tokeniser.createTagPending(false);

        assertNotNull(tagPending);
        assertTrue(tagPending instanceof Token.EndTag);
    }

    @Test
    public void testEmitTagPending_EmitsTagPending() {
        Token.Tag tagPending = new Token.StartTag();
        tagPending.setTagName("div");
        tokeniser.setTagPending(tagPending);

        tokeniser.emitTagPending();

        assertEquals(tagPending, tokeniser.getEmitPending());
    }

    @Test
    public void testCreateCommentPending_CreatesCommentPending() {
        tokeniser.createCommentPending();

        assertNotNull(tokeniser.getCommentPending());
    }

    @Test
    public void testEmitCommentPending_EmitsCommentPending() {
        Token.Comment commentPending = new Token.Comment();
        tokeniser.setCommentPending(commentPending);

        tokeniser.emitCommentPending();

        assertEquals(commentPending, tokeniser.getEmitPending());
    }

    @Test
    public void testCreateDoctypePending_CreatesDoctypePending() {
        tokeniser.createDoctypePending();

        assertNotNull(tokeniser.getDoctypePending());
    }

    @Test
    public void testEmitDoctypePending_EmitsDoctypePending() {
        Token.Doctype doctypePending = new Token.Doctype();
        tokeniser.setDoctypePending(doctypePending);

        tokeniser.emitDoctypePending();

        assertEquals(doctypePending, tokeniser.getEmitPending());
    }

    @Test
    public void testCreateTempBuffer_CreatesDataBuffer() {
        tokeniser.createTempBuffer();

        assertNotNull(tokeniser.getDataBuffer());
    }

    @Test
    public void testIsAppropriateEndTagToken_TagPendingSameAsLastStartTag_ReturnsTrue() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.setTagName("div");
        tokeniser.setLastStartTag(startTag);
        tokeniser.setTagPending(startTag);

        boolean isAppropriateEndTagToken = tokeniser.isAppropriateEndTagToken();

        assertTrue(isAppropriateEndTagToken);
    }

    @Test
    public void testIsTrackErrors_TrackErrorsEnabled_ReturnsTrue() {
        tokeniser.setTrackErrors(true);

        boolean isTrackErrors = tokeniser.isTrackErrors();

        assertTrue(isTrackErrors);
    }

    @Test
    public void testIsTrackErrors_TrackErrorsDisabled_ReturnsFalse() {
        tokeniser.setTrackErrors(false);

        boolean isTrackErrors = tokeniser.isTrackErrors();

        assertFalse(isTrackErrors);
    }

    @Test
    public void testError_AddsErrorToErrorsList() {
        tokeniser.setTrackErrors(true);
        TokeniserState state = TokeniserState.Data;

        tokeniser.error(state);

        assertEquals(1, tokeniser.getErrors().size());
        assertEquals("Unexpected character in input", tokeniser.getErrors().get(0).getErrorMsg());
    }

    @Test
    public void testEofError_AddsEofErrorToErrorsList() {
        tokeniser.setTrackErrors(true);
        TokeniserState state = TokeniserState.Data;

        tokeniser.eofError(state);

        assertEquals(1, tokeniser.getErrors().size());
        assertEquals("Unexpectedly reached end of file (EOF)", tokeniser.getErrors().get(0).getErrorMsg());
    }

    @Test
    public void testCharacterReferenceError_AddsCharacterReferenceErrorToErrorsList() {
        tokeniser.setTrackErrors(true);

        tokeniser.characterReferenceError();

        assertEquals(1, tokeniser.getErrors().size());
        assertEquals("Invalid character reference", tokeniser.getErrors().get(0).getErrorMsg());
    }

    @Test
    public void testError_AddsCustomErrorToErrorsList() {
        tokeniser.setTrackErrors(true);
        String errorMsg = "Custom error message";

        tokeniser.error(errorMsg);

        assertEquals(1, tokeniser.getErrors().size());
        assertEquals(errorMsg, tokeniser.getErrors().get(0).getErrorMsg());
    }

    @Test
    public void testCurrentNodeInHtmlNS_ReturnsTrue() {
        boolean currentNodeInHtmlNS = tokeniser.currentNodeInHtmlNS();

        assertTrue(currentNodeInHtmlNS);
    }
}