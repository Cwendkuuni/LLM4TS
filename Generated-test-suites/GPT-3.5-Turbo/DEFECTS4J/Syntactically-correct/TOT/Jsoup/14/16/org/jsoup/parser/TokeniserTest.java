import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(new CharacterReader(""));
    }

    @Test
    public void read_whenEmptyCharBuffer_shouldReturnNull() {
        Token token = tokeniser.read();
        assertEquals(null, token);
    }

    @Test
    public void read_whenCharacterBufferNotEmpty_shouldReturnCharacterToken() {
        tokeniser.emitToken(new Token.Character("a"));
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.getType());
        assertEquals("a", token.getValue());
    }

    @Test
    public void emit_whenTokenTypeStartTag_shouldSetLastStartTagAndSelfClosingFlag() {
        Token.StartTag tag = new Token.StartTag("div");
        tokeniser.emit(tag);
        assertEquals(tag, tokeniser.getLastStartTag());
        assertEquals(true, tokeniser.isSelfClosingFlagAcknowledged());
    }

    @Test
    public void emit_whenTokenTypeEndTagAndAttributesPresent_shouldAddError() {
        Token.EndTag tag = new Token.EndTag("div");
        tag.addAttribute("class", "test");
        tokeniser.emit(tag);
        assertEquals(1, tokeniser.getErrors().size());
        assertEquals("Attributes incorrectly present on end tag", tokeniser.getErrors().get(0).getErrorMessage());
    }

    @Test
    public void emit_whenTokenTypeNotStartTag_shouldNotSetSelfClosingFlag() {
        Token token = new Token.Doctype();
        tokeniser.emit(token);
        assertEquals(true, tokeniser.isSelfClosingFlagAcknowledged());
    }

    @Test
    public void consumeCharacterReference_whenAdditionalAllowedCharacterNull_shouldReturnNull() {
        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals(null, character);
    }

    @Test
    public void consumeCharacterReference_whenAdditionalAllowedCharacterMatchesCurrentCharacter_shouldReturnNull() {
        tokeniser.emit('&');
        Character character = tokeniser.consumeCharacterReference('&', false);
        assertEquals(null, character);
    }

    @Test
    public void consumeCharacterReference_whenCurrentCharacterIsSpecialCharacter_shouldReturnNull() {
        tokeniser.emit('<');
        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals(null, character);
    }

    @Test
    public void consumeCharacterReference_whenCurrentCharacterIsNumberedEntity_shouldReturnCharacter() {
        tokeniser.emit("&#65;");
        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals(new Character('A'), character);
    }

    @Test
    public void consumeCharacterReference_whenCurrentCharacterIsHexNumberedEntity_shouldReturnCharacter() {
        tokeniser.emit("&#x41;");
        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals(new Character('A'), character);
    }

    @Test
    public void consumeCharacterReference_whenCurrentCharacterIsInvalidNumberedEntity_shouldReturnReplacementChar() {
        tokeniser.emit("&#100000;");
        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals(new Character('\uFFFD'), character);
    }

    @Test
    public void consumeCharacterReference_whenCurrentCharacterIsNamedEntity_shouldReturnCharacter() {
        tokeniser.emit("&#65;");
        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals(new Character('A'), character);
    }

    @Test
    public void consumeCharacterReference_whenCurrentCharacterIsInvalidNamedEntity_shouldReturnNull() {
        tokeniser.emit("&invalid;");
        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals(null, character);
    }

    @Test
    public void createTagPending_whenStartIsTrue_shouldCreateStartTagPending() {
        tokeniser.createTagPending(true);
        assertEquals(Token.StartTag.class, tokeniser.getTagPending().getClass());
    }

    @Test
    public void createTagPending_whenStartIsFalse_shouldCreateEndTagPending() {
        tokeniser.createTagPending(false);
        assertEquals(Token.EndTag.class, tokeniser.getTagPending().getClass());
    }

    @Test
    public void emitTagPending_shouldFinaliseTagAndEmit() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        assertEquals(true, tokeniser.getTagPending().isFinalised());
    }

    @Test
    public void createCommentPending_shouldCreateCommentPending() {
        tokeniser.createCommentPending();
        assertEquals(Token.Comment.class, tokeniser.getCommentPending().getClass());
    }

    @Test
    public void emitCommentPending_shouldEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        assertEquals(Token.Comment.class, tokeniser.emitPending.getClass());
    }

    @Test
    public void createDoctypePending_shouldCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertEquals(Token.Doctype.class, tokeniser.getDoctypePending().getClass());
    }

    @Test
    public void emitDoctypePending_shouldEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        assertEquals(Token.Doctype.class, tokeniser.emitPending.getClass());
    }

    @Test
    public void createTempBuffer_shouldCreateDataBuffer() {
        tokeniser.createTempBuffer();
        assertEquals(StringBuilder.class, tokeniser.getDataBuffer().getClass());
    }

    @Test
    public void isAppropriateEndTagToken_whenTagPendingTagNameEqualsLastStartTagTagName_shouldReturnTrue() {
        tokeniser.createTagPending(true);
        tokeniser.getTagPending().setTagName("div");
        tokeniser.setLastStartTag(tokeniser.getTagPending());
        assertEquals(true, tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void isAppropriateEndTagToken_whenTagPendingTagNameNotEqualsLastStartTagTagName_shouldReturnFalse() {
        tokeniser.createTagPending(true);
        tokeniser.getTagPending().setTagName("div");
        tokeniser.setLastStartTag(new Token.StartTag());
        tokeniser.getLastStartTag().setTagName("p");
        assertEquals(false, tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void isTrackErrors_whenTrackErrorsIsTrue_shouldReturnTrue() {
        tokeniser.setTrackErrors(true);
        assertEquals(true, tokeniser.isTrackErrors());
    }

    @Test
    public void isTrackErrors_whenTrackErrorsIsFalse_shouldReturnFalse() {
        tokeniser.setTrackErrors(false);
        assertEquals(false, tokeniser.isTrackErrors());
    }

    @Test
    public void error_shouldAddParseErrorToErrorsList() {
        tokeniser.setTrackErrors(true);
        tokeniser.error(TokeniserState.Data);
        assertEquals(1, tokeniser.getErrors().size());
        assertEquals("Unexpected character in input", tokeniser.getErrors().get(0).getErrorMessage());
    }

    @Test
    public void eofError_shouldAddParseErrorToErrorsList() {
        tokeniser.setTrackErrors(true);
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, tokeniser.getErrors().size());
        assertEquals("Unexpectedly reached end of file (EOF)", tokeniser.getErrors().get(0).getErrorMessage());
    }

    @Test
    public void characterReferenceError_shouldLogInvalidCharacterReference() {
        tokeniser.setTrackErrors(true);
        tokeniser.characterReferenceError();
        assertEquals(1, tokeniser.getErrors().size());
        assertEquals("Invalid character reference", tokeniser.getErrors().get(0).getErrorMessage());
    }

    @Test
    public void error_withErrorMessage_shouldAddCustomErrorToErrorsList() {
        tokeniser.setTrackErrors(true);
        tokeniser.error("Custom error message");
        assertEquals(1, tokeniser.getErrors().size());
        assertEquals("Custom error message", tokeniser.getErrors().get(0).getErrorMessage());
    }

    @Test
    public void currentNodeInHtmlNS_shouldReturnTrue() {
        assertEquals(true, tokeniser.currentNodeInHtmlNS());
    }

}