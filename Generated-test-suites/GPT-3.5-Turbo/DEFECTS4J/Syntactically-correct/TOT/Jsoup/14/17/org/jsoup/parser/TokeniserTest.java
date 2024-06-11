
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(null);
    }

    @Test
    public void testRead_method_returns_CharacterToken_with_correct_value_when_charBuffer_is_not_empty() {
        // Arrange
        Token.Character expectedToken = new Token.Character("Test");

        // Act
        tokeniser.emit("Test");
        Token actualToken = tokeniser.read();

        // Assert
        assertEquals(expectedToken, actualToken);
    }

    @Test
    public void testRead_method_returns_EmptyCharacterToken_with_empty_string_value_when_charBuffer_is_empty() {
        // Arrange
        Token.Character expectedToken = new Token.Character("");

        // Act
        Token actualToken = tokeniser.read();

        // Assert
        assertEquals(expectedToken, actualToken);
    }

    @Test
    public void testEmit_method_sets_isEmitPending_to_true() {
        // Arrange
        Token.Tag tagToken = new Token.Tag();

        // Act
        tokeniser.emit(tagToken);

        // Assert
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testEmit_method_sets_emitPending_to_given_token() {
        // Arrange
        Token.Doctype doctypeToken = new Token.Doctype();

        // Act
        tokeniser.emit(doctypeToken);

        // Assert
        assertEquals(doctypeToken, tokeniser.emitPending);
    }

    @Test
    public void testEmit_method_throws_IllegalArgumentException_when_isEmitPending_is_true() {
        // Arrange
        tokeniser.isEmitPending = true;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> tokeniser.emit(new Token.Doctype()));
    }

    @Test
    public void testCreateTagPending_method_returns_StartTagToken_when_start_is_true() {
        // Arrange
        boolean start = true;

        // Act
        Token.Tag actualToken = tokeniser.createTagPending(start);

        // Assert
        assertTrue(actualToken instanceof Token.StartTag);
    }

    @Test
    public void testCreateTagPending_method_returns_EndTagToken_when_start_is_false() {
        // Arrange
        boolean start = false;

        // Act
        Token.Tag actualToken = tokeniser.createTagPending(start);

        // Assert
        assertTrue(actualToken instanceof Token.EndTag);
    }

    @Test
    public void testCreateTagPending_method_sets_tagPending_to_returned_token() {
        // Arrange
        boolean start = true;

        // Act
        Token.Tag expectedToken = tokeniser.createTagPending(start);

        // Assert
        assertEquals(expectedToken, tokeniser.tagPending);
    }

    @Test
    public void testEmitTagPending_method_finalises_tagPending_tag_and_emits_it() {
        // Arrange
        Token.StartTag startTag = new Token.StartTag();
        tokeniser.tagPending = startTag;

        // Act
        tokeniser.emitTagPending();

        // Assert
        assertTrue(startTag.isFinalised);
    }

    @Test
    public void testCreateCommentPending_method_creates_new_CommentToken_object() {
        // Act
        tokeniser.createCommentPending();

        // Assert
        assertTrue(tokeniser.commentPending instanceof Token.Comment);
    }

    @Test
    public void testEmitCommentPending_method_emits_commentPending_token() {
        // Arrange
        Token.Comment commentToken = new Token.Comment();
        tokeniser.commentPending = commentToken;

        // Act
        tokeniser.emitCommentPending();

        // Assert
        assertEquals(commentToken, tokeniser.emitPending);
    }

    @Test
    public void testCreateDoctypePending_method_creates_new_DoctypeToken_object() {
        // Act
        tokeniser.createDoctypePending();

        // Assert
        assertTrue(tokeniser.doctypePending instanceof Token.Doctype);
    }

    @Test
    public void testEmitDoctypePending_method_emits_doctypePending_token() {
        // Arrange
        Token.Doctype doctypeToken = new Token.Doctype();
        tokeniser.doctypePending = doctypeToken;

        // Act
        tokeniser.emitDoctypePending();

        // Assert
        assertEquals(doctypeToken, tokeniser.emitPending);
    }

    @Test
    public void testConsumeCharacterReference_method_returns_NULL_when_reader_is_empty() {
        // Arrange
        tokeniser.reader = new CharacterReader("");

        // Act
        Character actualResult = tokeniser.consumeCharacterReference(null, false);

        // Assert
        assertNull(actualResult);
    }

    @Test
    public void testConsumeCharacterReference_method_returns_NULL_when_additionalAllowedCharacter_is_matched() {
        // Arrange
        tokeniser.reader = new CharacterReader("<");

        // Act
        Character actualResult = tokeniser.consumeCharacterReference('<', false);

        // Assert
        assertNull(actualResult);
    }

    @Test
    public void testConsumeCharacterReference_method_returns_NULL_when_matches_any_invalid_character_for_reference() {
        // Arrange
        tokeniser.reader = new CharacterReader("<");

        // Act
        Character actualResult = tokeniser.consumeCharacterReference(null, false);

        // Assert
        assertNull(actualResult);
    }

    @Test
    public void testConsumeCharacterReference_method_returns_NULL_when_named_reference_has_invalid_format() {
        // Arrange
        tokeniser.reader = new CharacterReader("&;");
        StringBuilder expectedBuffer = new StringBuilder();

        // Act
        Character actualResult = tokeniser.consumeCharacterReference(null, false);

        // Assert
        assertEquals(expectedBuffer.toString(), tokeniser.dataBuffer.toString());
        assertNull(actualResult);
    }

    @Test
    public void testConsumeCharacterReference_method_returns_replacementChar_when_named_reference_is_invalid() {
        // Arrange
        tokeniser.reader = new CharacterReader("&invalidReference;");
        StringBuilder expectedBuffer = new StringBuilder("&invalidReference;");

        // Act
        Character actualResult = tokeniser.consumeCharacterReference(null, false);

        // Assert
        assertEquals(expectedBuffer.toString(), tokeniser.dataBuffer.toString());
        assertEquals(Tokeniser.replacementChar, actualResult);
    }

    @Test
    public void testConsumeCharacterReference_method_returns_named_reference_value_when_named_reference_is_valid() {
        // Arrange
        tokeniser.reader = new CharacterReader("&amp;");

        // Act
        Character actualResult = tokeniser.consumeCharacterReference(null, false);

        // Assert
        assertEquals('&', actualResult.charValue());
    }

    @Test
    public void testConsumeCharacterReference_method_returns_NULL_when_inAttribute_and_next_character_is_valid() {
        // Arrange
        tokeniser.reader = new CharacterReader("&");

        // Act
        Character actualResult = tokeniser.consumeCharacterReference('=', true);

        // Assert
        assertNull(actualResult);
    }

    @Test
    public void testConsumeCharacterReference_method_returns_NULL_when_semi_is_missing_after_valid_named_reference() {
        // Arrange
        tokeniser.reader = new CharacterReader("&amp");

        // Act
        Character actualResult = tokeniser.consumeCharacterReference(null, false);

        // Assert
        assertNull(actualResult);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag_method_sets_selfClosingFlagAcknowledged_to_true() {
        // Act
        tokeniser.acknowledgeSelfClosingFlag();

        // Assert
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testGetState_method_returns_current_state_of_tokeniser() {
        // Arrange
        tokeniser.state = TokeniserState.Data;

        // Act
        TokeniserState actualState = tokeniser.getState();

        // Assert
        assertEquals(TokeniserState.Data, actualState);
    }

    @Test
    public void testTransition_method_updates_state_of_tokeniser_to_given_state() {
        // Arrange
        tokeniser.state = TokeniserState.Data;

        // Act
        tokeniser.transition(TokeniserState.RCDATA);

        // Assert
        assertEquals(TokeniserState.RCDATA, tokeniser.state);
    }

    @Test
    public void testAdvanceTransition_method_advances_reader_and_updates_state_of_tokeniser_to_given_state() {
        // Arrange
        tokeniser.state = TokeniserState.Data;
        tokeniser.reader = new CharacterReader("Test");

        // Act
        tokeniser.advanceTransition(TokeniserState.RCDATA);

        // Assert
        assertEquals(TokeniserState.RCDATA, tokeniser.state);
        assertEquals("est", tokeniser.reader.getSuffix());
    }

    @Test
    public void testCurrentNodeInHtmlNS_method_returns_true_always() {
        // Act
        boolean result = tokeniser.currentNodeInHtmlNS();

        // Assert
        assertTrue(result);
    }
}

