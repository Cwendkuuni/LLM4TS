import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private StringBuilder outputStringBuilder;

    @Before
    public void setUp() {
        outputStringBuilder = new StringBuilder();
    }

    @Test
    public void testRead() {
        tokeniser = new Tokeniser(new CharacterReader("input"), new ParseErrorList());
        Token token = tokeniser.read();
        assertEquals(Token.Character.class, token.getClass());
        assertEquals("input", ((Token.Character) token).getData());
    }

    @Test
    public void testEmit() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        Token tagToken = new Token.Tag("");
        tokeniser.emit(tagToken);
        assertTrue(tokeniser.isEmitPending);
        assertEquals(tagToken, tokeniser.emitPending);
    }

    @Test
    public void testEmitString() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.emit("input");
        assertEquals("input", tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmitChar() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.emit('c');
        assertEquals("c", tokeniser.charBuffer.toString());
    }

    @Test
    public void testTransition() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        TokeniserState newState = TokeniserState.Comment;
        tokeniser.transition(newState);
        assertEquals(newState, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        tokeniser = new Tokeniser(new CharacterReader("input"), new ParseErrorList());
        TokeniserState newState = TokeniserState.Comment;
        tokeniser.advanceTransition(newState);
        assertEquals(newState, tokeniser.getState());
        assertEquals('i', tokeniser.reader.current());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        tokeniser = new Tokeniser(new CharacterReader("&lt;"), new ParseErrorList());
        assertNull(tokeniser.consumeCharacterReference(null, false));
        assertEquals("&lt;", tokeniser.reader.getWholeData());
    }

    @Test
    public void testCreateTagPending() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        Token.Tag tag = tokeniser.createTagPending(true);
        assertNotNull(tokeniser.tagPending);
        assertEquals(Token.StartTag.class, tag.getClass());
        assertEquals(tokeniser.tagPending, tag);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        Token.Tag tag = new Token.StartTag();
        tokeniser.tagPending = tag;
        tokeniser.emitTagPending();
        assertEquals(tag, tokeniser.emitPending);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        Token.Comment comment = new Token.Comment();
        tokeniser.commentPending = comment;
        tokeniser.emitCommentPending();
        assertEquals(comment, tokeniser.emitPending);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        Token.Doctype doctype = new Token.Doctype();
        tokeniser.doctypePending = doctype;
        tokeniser.emitDoctypePending();
        assertEquals(doctype, tokeniser.emitPending);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.lastStartTag = new Token.StartTag("tag");
        assertFalse(tokeniser.isAppropriateEndTagToken());
        tokeniser.tagPending = new Token.EndTag("tag");
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.lastStartTag = new Token.StartTag("tag");
        assertEquals("tag", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.error(TokeniserState.Comment);
        ParseErrorList errors = tokeniser.errors;
        assertNotNull(errors);
        assertFalse(errors.isEmpty());
        assertEquals("Unexpected character '' in input state [Comment]", errors.get(0).toString());
    }

    @Test
    public void testEofError() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.eofError(TokeniserState.Comment);
        ParseErrorList errors = tokeniser.errors;
        assertNotNull(errors);
        assertFalse(errors.isEmpty());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Comment]", errors.get(0).toString());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.characterReferenceError("message");
        ParseErrorList errors = tokeniser.errors;
        assertNotNull(errors);
        assertFalse(errors.isEmpty());
        assertEquals("Invalid character reference: message", errors.get(0).toString());
    }

    @Test
    public void testErrorWithMessage() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.error("errorMsg");
        ParseErrorList errors = tokeniser.errors;
        assertNotNull(errors);
        assertFalse(errors.isEmpty());
        assertEquals("errorMsg", errors.get(0).toString());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }

}