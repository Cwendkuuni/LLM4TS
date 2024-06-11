import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {
    
    private Tokeniser tokeniser;
    
    @Before
    public void setUp() {
        tokeniser = new Tokeniser(new CharacterReader(""));
    }

    @Test
    public void testRead_EmptyReader_ReturnsNull() {
        assertNull(tokeniser.read());
    }

    @Test
    public void testEmit_ValidToken_CharacterTokenIsEmitted() {
        tokeniser.emit("Hello");
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("Hello", ((Token.Character) token).getData());
    }

    @Test
    public void testEmit_EmptyString_NoTokenIsEmitted() {
        tokeniser.emit("");
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit_CharackterToken_EmptyBufferAfterEmit() {
        tokeniser.emit("Hello");
        tokeniser.read();
        assertEquals(0, tokeniser.charBuffer.length());
    }

    @Test
    public void testEmit_Character_ValidCharacterTokenIsEmitted() {
        tokeniser.emit('A');
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("A", ((Token.Character) token).getData());
    }

    @Test
    public void testEmit_TagToken_TagTokenIsEmitted() {
        Token.Tag tag = new Token.Tag();
        tokeniser.emit(tag);
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(tag, token);
    }

    @Test
    public void testEmit_StartTagToken_StartTagTokenIsEmitted() {
        Token.StartTag startTag = new Token.StartTag();
        tokeniser.emit(startTag);
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(startTag, token);
    }

    @Test
    public void testEmit_EndTagToken_EmptyAttributes_NoErrorIsThrown() {
        Token.EndTag endTag = new Token.EndTag();
        tokeniser.emit(endTag);
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(endTag, token);
    }

    @Test
    public void testEmit_EndTagToken_NonEmptyAttributes_ErrorIsThrown() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.attributes.add(new Token.Attribute());
        tokeniser.emit(endTag);
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.ParseError, token.type);
    }

    @Test
    public void testCreateTagPending_StartTag_TagPendingIsCreated() {
        Token.Tag tagPending = tokeniser.createTagPending(true);
        assertNotNull(tagPending);
        assertTrue(tagPending instanceof Token.StartTag);
    }

    @Test
    public void testCreateTagPending_EndTag_TagPendingIsCreated() {
        Token.Tag tagPending = tokeniser.createTagPending(false);
        assertNotNull(tagPending);
        assertTrue(tagPending instanceof Token.EndTag);
    }

    @Test
    public void testEmitTagPending_TagPendingIsFinalized() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.attributes.add(new Token.Attribute());
        tokeniser.tagPending = startTag;
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(startTag, token);
        assertEquals(0, tokeniser.tagPending.attributes.size());
    }

    @Test
    public void testCreateCommentPending_CommentPendingIsCreated() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending_CommentPendingIsEmmited() {
        tokeniser.createCommentPending();
        tokeniser.commentPending.data = "This is a comment";
        tokeniser.emitCommentPending();
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Comment, token.type);
        assertEquals("This is a comment", ((Token.Comment) token).getData());
    }

    @Test
    public void testCreateDoctypePending_DoctypePendingIsCreated() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending_DoctypePendingIsEmitted() {
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.name = "html";
        tokeniser.doctypePending.publicIdentifier = "-//W3C//DTD HTML 4.01//EN";
        tokeniser.emitDoctypePending();
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Doctype, token.type);
        assertEquals("html", ((Token.Doctype) token).getName());
        assertEquals("-//W3C//DTD HTML 4.01//EN", ((Token.Doctype) token).getPublicIdentifier());
    }

    @Test
    public void testCreateTempBuffer_DataBufferIsCreated() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken_IsAppropriateEndTagToken() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "p";
        tokeniser.lastStartTag = startTag;
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.tagPending.tagName = "p";
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagToken_IsNotAppropriateEndTagToken() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "p";
        tokeniser.lastStartTag = startTag;
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.tagPending.tagName = "div";
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testSetTrackErrors_TrackErrorsIsSet() {
        tokeniser.setTrackErrors(false);
        assertFalse(tokeniser.isTrackErrors());
    }

    @Test
    public void testError_ErrorIsAddedToErrorsList() {
        tokeniser.error(TokeniserState.Data);
        assertFalse(tokeniser.errors.isEmpty());
        assertEquals("Unexpected character in input", tokeniser.errors.get(0).getErrorMessage());
        assertEquals(TokeniserState.Data, tokeniser.errors.get(0).getState());
    }

    @Test
    public void testEofError_ErrorIsAddedToErrorsList() {
        tokeniser.eofError(TokeniserState.Data);
        assertFalse(tokeniser.errors.isEmpty());
        assertEquals("Unexpectedly reached end of file (EOF)", tokeniser.errors.get(0).getErrorMessage());
        assertEquals(TokeniserState.Data, tokeniser.errors.get(0).getState());
    }

    @Test
    public void testCharacterReferenceError_ErrorIsAddedToErrorsList() {
        tokeniser.characterReferenceError();
        assertFalse(tokeniser.errors.isEmpty());
        assertEquals("Invalid character reference", tokeniser.errors.get(0).getErrorMessage());
    }

    @Test
    public void testError_CustomErrorMessage_ErrorIsAddedToErrorsList() {
        tokeniser.error("Custom error message");
        assertFalse(tokeniser.errors.isEmpty());
        assertEquals("Custom error message", tokeniser.errors.get(0).getErrorMessage());
    }
    
    @Test
    public void testConsumeCharacterReference_WithAdditionalAllowedCharacterAndInAttribute_ReturnsNull() {
        tokeniser.reader = new CharacterReader("\t");
        assertNull(tokeniser.consumeCharacterReference('\t', true));
    }
    
    @Test
    public void testConsumeCharacterReference_EmptyReader_ReturnsNull() {
        assertNull(tokeniser.consumeCharacterReference(null, true));
    }
    
    @Test
    public void testConsumeCharacterReference_NumberedCharacterReference_ReturnsCorrectCharacter() {
        tokeniser.reader = new CharacterReader("#65;");
        assertEquals("A", tokeniser.consumeCharacterReference(null, true).toString());
    }
    
    @Test
    public void testConsumeCharacterReference_NamedCharacterReference_ReturnsCorrectCharacter() {
        tokeniser.reader = new CharacterReader("&lt;");
        assertEquals("<", tokeniser.consumeCharacterReference(null, true).toString());
    }
}