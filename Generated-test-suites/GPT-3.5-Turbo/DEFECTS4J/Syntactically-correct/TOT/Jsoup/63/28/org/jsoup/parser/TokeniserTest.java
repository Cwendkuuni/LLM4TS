import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class TokeniserTest {
    private static Tokeniser tokeniser;

    @BeforeClass
    public static void setUp() {
        CharacterReader reader = new CharacterReader("Hello world!");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead_emptyString_returnsNull() {
        assertNull(tokeniser.read());
    }

    @Test
    public void testRead_notEmptyString_returnsToken() {
        assertNotNull(tokeniser.read());
    }

    @Test
    public void testEmit_tokenTypeStartTag_setSelfClosingFlagAcknowledgedToFalse() {
        Token.StartTag token = new Token.StartTag();
        tokeniser.emit(token);
        assertFalse(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testEmit_tokenTypeEndTag_checkForAttributes() {
        Token.EndTag token = new Token.EndTag();
        token.attributes = "test";
        tokeniser.emit(token);
        assertEquals("Attributes incorrectly present on end tag", tokeniser.errors.get(0).getErrorMsg());
    }

    @Test
    public void testEmit_string_emitTokenWithData() {
        String str = "test";
        tokeniser.emit(str);
        assertEquals(Token.TokenType.Character, tokeniser.emitPending.type);
        assertEquals(str, tokeniser.emitPending.toString());
    }

    @Test
    public void testEmit_charArray_emitTokenWithString() {
        char[] chars = {'t', 'e', 's', 't'};
        tokeniser.emit(chars);
        assertEquals(Token.TokenType.Character, tokeniser.emitPending.type);
        assertEquals(String.valueOf(chars), tokeniser.emitPending.toString());
    }

    @Test
    public void testEmit_codepoints_emitTokenWithString() {
        int[] codepoints = {'t', 'e', 's', 't'};
        tokeniser.emit(codepoints);
        assertEquals(Token.TokenType.Character, tokeniser.emitPending.type);
        assertEquals(new String(codepoints, 0, codepoints.length), tokeniser.emitPending.toString());
    }

    @Test
    public void testEmit_char_emitTokenWithString() {
        char c = 't';
        tokeniser.emit(c);
        assertEquals(Token.TokenType.Character, tokeniser.emitPending.type);
        assertEquals(String.valueOf(c), tokeniser.emitPending.toString());
    }

    @Test
    public void testCreateTagPending_start_true() {
        boolean start = true;
        Token.Tag token = tokeniser.createTagPending(start);
        assertTrue(token instanceof Token.StartTag);
    }

    @Test
    public void testCreateTagPending_start_false() {
        boolean start = false;
        Token.Tag token = tokeniser.createTagPending(start);
        assertTrue(token instanceof Token.EndTag);
    }

    @Test
    public void testEmitTagPending_finalizeTag() {
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.emitTagPending();
        assertTrue(tokeniser.tagPending.isFinalised);
    }

    @Test
    public void testCreateCommentPending_resetCommentPending() {
        tokeniser.createCommentPending();
        assertTrue(tokeniser.commentPending.isReset);
    }

    @Test
    public void testEmitCommentPending_emitCommentToken() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        assertEquals(Token.TokenType.Comment, tokeniser.emitPending.type);
    }

    @Test
    public void testCreateDoctypePending_resetDoctypePending() {
        tokeniser.createDoctypePending();
        assertTrue(tokeniser.doctypePending.isReset);
    }

    @Test
    public void testEmitDoctypePending_emitDoctypeToken() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        assertEquals(Token.TokenType.Doctype, tokeniser.emitPending.type);
    }

    @Test
    public void testCreateTempBuffer_resetDataBuffer() {
        tokeniser.createTempBuffer();
        assertTrue(tokeniser.dataBuffer.isReset);
    }

    @Test
    public void testIsAppropriateEndTagToken_true() {
        tokeniser.lastStartTag = "div";
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagToken_false() {
        tokeniser.lastStartTag = null;
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName_null() {
        assertNull(tokeniser.appropriateEndTagName());
    }

    @Test
    public void testAppropriateEndTagName_notNull() {
        tokeniser.lastStartTag = "div";
        assertEquals("div", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError_state_checkErrorMessage() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.error(state);
        assertEquals("Unexpected character 'H' in input state [Data]", tokeniser.errors.get(0).getErrorMsg());
    }

    @Test
    public void testEofError_state_checkErrorMessage() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.eofError(state);
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", tokeniser.errors.get(0).getErrorMsg());
    }

    @Test
    public void testCharacterReferenceError_checkErrorMessage() {
        String message = "numeric reference with no numerals";
        tokeniser.characterReferenceError(message);
        assertEquals("Invalid character reference: " + message, tokeniser.errors.get(0).getErrorMsg());
    }

    @Test
    public void testError_errorMsg_checkErrorMessage() {
        String errorMsg = "Invalid input";
        tokeniser.error(errorMsg);
        assertEquals(errorMsg, tokeniser.errors.get(0).getErrorMsg());
    }

    @Test
    public void testCurrentNodeInHtmlNS_true() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }

    @Test
    public void testUnescapeEntities_inAttribute_checkUnescapedString() {
        String str = "&lt;&gt;&amp;&apos;&quot;";
        String expected = "<>&'\"";
        assertEquals(expected, tokeniser.unescapeEntities(true));
    }

    @Test
    public void testUnescapeEntities_notInAttribute_checkUnescapedString() {
        String str = "&lt;&gt;&amp;&apos;&quot;";
        String expected = "<>&'\"";
        assertEquals(expected, tokeniser.unescapeEntities(false));
    }

}