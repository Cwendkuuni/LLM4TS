import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenizerTest {
  
    private Tokeniser tokeniser;
    private CharacterReader reader;
  
    @Before
    public void setUp() {
        reader = new CharacterReader("");
        tokeniser = new Tokeniser(reader);
    }
  
    @Test
    public void testRead() {
        assertNull(tokeniser.read());
      
        reader = new CharacterReader("<html>");
        tokeniser = new Tokeniser(reader);
        assertNotNull(tokeniser.read());
    }
  
    @Test
    public void testEmit() {
        Token token = new Token.Character("");
        tokeniser.emit(token);
        assertEquals(token, tokeniser.read());
    }
  
    @Test
    public void testEmitString() {
        String str = "<html>";
        tokeniser.emit(str);
        assertEquals(new Token.Character(str), tokeniser.read());
    }
  
    @Test
    public void testEmitChar() {
        char c = 'a';
        tokeniser.emit(c);
        assertEquals(new Token.Character(String.valueOf(c)), tokeniser.read());
    }
  
    @Test
    public void testCreateTagPending() {
        assertTrue(tokeniser.createTagPending(true) instanceof Token.StartTag);
        assertTrue(tokeniser.createTagPending(false) instanceof Token.EndTag);
    }
  
    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        assertTrue(tokeniser.read() instanceof Token.StartTag);
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
        assertTrue(tokeniser.read() instanceof Token.Comment);
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
        assertTrue(tokeniser.read() instanceof Token.Doctype);
    }
  
    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }
  
    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        assertTrue(tokeniser.isAppropriateEndTagToken());
      
        tokeniser.lastStartTag = new Token.StartTag();
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }
  
    @Test
    public void testIsTrackErrors() {
        assertTrue(tokeniser.isTrackErrors());
        tokeniser.setTrackErrors(false);
        assertFalse(tokeniser.isTrackErrors());
    }
  
    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Data);
        assertFalse(tokeniser.errors.isEmpty());
    }
  
    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);
        assertFalse(tokeniser.errors.isEmpty());
    }
  
    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError();
        assertFalse(tokeniser.errors.isEmpty());
    }
  
    @Test
    public void testErrorString() {
        String errorMsg = "Error";
        tokeniser.error(errorMsg);
        assertFalse(tokeniser.errors.isEmpty());
    }
  
    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}