import static org.junit.Assert.*;
import org.junit.Test;

public class TokeniserTest {
    
    @Test
    public void testRead() {
        CharacterReader reader = new CharacterReader("test");
        Tokeniser tokeniser = new Tokeniser(reader);
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
    }
    
    @Test
    public void testEmit() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        Token.Character token = new Token.Character("test");
        tokeniser.emit(token);
        assertEquals(token, tokeniser.read());
    }
    
    @Test
    public void testTransition() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        tokeniser.transition(TokeniserState.EOF);
        assertEquals(TokeniserState.EOF, tokeniser.getState());
    }
    
    @Test
    public void testAdvanceTransition() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        tokeniser.advanceTransition(TokeniserState.Data);
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }
    
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }
    
    @Test
    public void testConsumeCharacterReference() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        Character character = tokeniser.consumeCharacterReference(null, false);
        assertNull(character);
    }
    
    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        Token.Tag tag = tokeniser.createTagPending(true);
        assertTrue(tag instanceof Token.StartTag);
    }
    
    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        assertEquals(Token.TokenType.StartTag, tokeniser.read().type);
    }
    
    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }
    
    @Test
    public void testEmitCommentPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        assertEquals(Token.TokenType.Comment, tokeniser.read().type);
    }
    
    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }
    
    @Test
    public void testEmitDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        assertEquals(Token.TokenType.Doctype, tokeniser.read().type);
    }
    
    @Test
    public void testCreateTempBuffer() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        tokeniser.createTagPending(true);
        tokeniser.lastStartTag = (Token.StartTag) tokeniser.createTagPending(true);
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }
    
    @Test
    public void testIsTrackErrors() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        assertTrue(tokeniser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        tokeniser.setTrackErrors(false);
        assertFalse(tokeniser.isTrackErrors());
    }
    
    @Test
    public void testError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        tokeniser.error(TokeniserState.Data);
        assertFalse(tokeniser.errors.isEmpty());
    }
    
    @Test
    public void testEofError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        tokeniser.eofError(TokeniserState.Data);
        assertFalse(tokeniser.errors.isEmpty());
    }
    
    @Test
    public void testCharacterReferenceError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        tokeniser.characterReferenceError();
        assertFalse(tokeniser.errors.isEmpty());
    }
    
    @Test
    public void testErrorWithMsg() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        tokeniser.error("Test Error");
        assertFalse(tokeniser.errors.isEmpty());
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}