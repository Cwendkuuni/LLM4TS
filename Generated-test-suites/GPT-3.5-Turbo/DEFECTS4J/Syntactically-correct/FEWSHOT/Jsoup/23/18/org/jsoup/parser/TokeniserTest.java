import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;

public class TokeniserTest {
    private Tokeniser tokeniser;
    private ParseErrorList errors;
    private CharacterReader reader;

    @Before
    public void setup() {
        errors = new ParseErrorList();
        reader = new CharacterReader("");
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead_EmptyInput() {
        assertNull(tokeniser.read());
    }

    @Test
    public void testEmit() {
        Token token = new Token.StartTag();
        tokeniser.emit(token);

        assertTrue(tokeniser.isEmitPending);
        assertEquals(token, tokeniser.emitPending);
    }

    @Test
    public void testEmit_String() {
        String str = "example string";
        tokeniser.emit(str);

        assertTrue(tokeniser.isEmitPending);
        assertEquals(str, tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmit_Char() {
        char c = 'c';
        tokeniser.emit(c);

        assertTrue(tokeniser.isEmitPending);
        assertEquals(String.valueOf(c), tokeniser.charBuffer.toString());
    }

    @Test
    public void testCreateTagPending_Start() {
        boolean start = true;
        tokeniser.createTagPending(start);

        assertTrue(tokeniser.tagPending instanceof Token.StartTag);
    }

    @Test
    public void testCreateTagPending_End() {
        boolean start = false;
        tokeniser.createTagPending(start);

        assertTrue(tokeniser.tagPending instanceof Token.EndTag);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        assertTrue(tokeniser.isEmitPending);
        assertEquals(tokeniser.tagPending, tokeniser.emitPending);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();

        assertTrue(tokeniser.commentPending instanceof Token.Comment);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();

        assertTrue(tokeniser.isEmitPending);
        assertEquals(tokeniser.commentPending, tokeniser.emitPending);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();

        assertTrue(tokeniser.doctypePending instanceof Token.Doctype);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();

        assertTrue(tokeniser.isEmitPending);
        assertEquals(tokeniser.doctypePending, tokeniser.emitPending);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();

        assertTrue(tokeniser.dataBuffer instanceof StringBuilder);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.lastStartTag = (Token.StartTag) tokeniser.tagPending;

        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.createTagPending(true);
        tokeniser.lastStartTag = (Token.StartTag) tokeniser.tagPending;

        assertEquals(tokeniser.lastStartTag.tagName, tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError_State() {
        tokeniser.error(TokeniserState.Data);

        assertTrue(errors.size() > 0);
        assertEquals("Unexpected character '' in input state [Data]", errors.get(0).getErrorMessage());
    }

    @Test
    public void testEofError_State() {
        tokeniser.eofError(TokeniserState.Data);

        assertTrue(errors.size() > 0);
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.get(0).getErrorMessage());
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "example message";
        tokeniser.characterReferenceError(message);

        assertTrue(errors.size() > 0);
        assertEquals("Invalid character reference: " + message, errors.get(0).getErrorMessage());
    }

    @Test
    public void testError_String() {
        String errorMsg = "example error";
        tokeniser.error(errorMsg);

        assertTrue(errors.size() > 0);
        assertEquals(errorMsg, errors.get(0).getErrorMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}