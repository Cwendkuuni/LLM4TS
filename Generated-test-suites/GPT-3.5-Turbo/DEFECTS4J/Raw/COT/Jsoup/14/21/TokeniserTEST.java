
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.CharacterReader;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    
    private Tokeniser tokeniser;
    
    @Before
    public void setUp() {
        // Initialize the Tokeniser with a CharacterReader
        tokeniser = new Tokeniser(new CharacterReader(""));
    }
    
    @Test
    public void testRead() {
        // Test the read() method
        
        // Test when selfClosingFlagAcknowledged is false
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.advanceTransition(TokeniserState.Data);
        tokeniser.emit(new Token.StartTag("tag"));
        Token token = tokeniser.read();
        
        assertNotNull(token);
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("tag", ((Token.StartTag) token).tagName);
        
        // Test when charBuffer has characters
        tokeniser.emit("Hello");
        tokeniser.emit("World");
        token = tokeniser.read();
        
        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("HelloWorld", ((Token.Character) token).getData());
        
        // Test when isEmitPending is true
        tokeniser.emit(new Token.EndTag("tag"));
        token = tokeniser.read();
        
        assertNotNull(token);
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("tag", ((Token.EndTag) token).tagName);
    }
    
    @Test
    public void testEmit() {
        // Test the emit() method
        
        // Test when str is not empty
        tokeniser.emit("Hello");
        
        assertEquals("Hello", tokeniser.charBuffer.toString());
        
        // Test when str is empty
        tokeniser.emit("");
        
        assertEquals("", tokeniser.charBuffer.toString());
        
        // Test when c is a character
        tokeniser.emit('H');
        
        assertEquals("H", tokeniser.charBuffer.toString());
    }
    
    @Test
    public void testTransition() {
        // Test the transition() method
        
        tokeniser.transition(TokeniserState.Rcdata);
        
        assertEquals(TokeniserState.Rcdata, tokeniser.getState());
    }
    
    @Test
    public void testAdvanceTransition() {
        // Test the advanceTransition() method
        
        tokeniser.advanceTransition(TokeniserState.Rcdata);
        
        assertEquals(TokeniserState.Rcdata, tokeniser.getState());
        assertEquals(1, tokeniser.reader.pos());
    }
    
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // Test the acknowledgeSelfClosingFlag() method
        
        tokeniser.acknowledgeSelfClosingFlag();
        
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }
    
    @Test
    public void testConsumeCharacterReference() {
        // Test the consumeCharacterReference() method
        
        // Test when additionalAllowedCharacter is null
        tokeniser.reader.current = ' ';
        Character character = tokeniser.consumeCharacterReference(null, false);
        
        assertNull(character);
        
        // Test when additionalAllowedCharacter is not null and matches current character
        tokeniser.reader.current = 'a';
        character = tokeniser.consumeCharacterReference('a', false);
        
        assertNull(character);
        
        // Test when matchesAny is true
        tokeniser.reader.current = '\n';
        character = tokeniser.consumeCharacterReference(null, false);
        
        assertNull(character);
    }
    
    @Test
    public void testCreateTagPending() {
        // Test the createTagPending() method
        
        tokeniser.tagPending = new Token.StartTag();
        Token.Tag tag = tokeniser.createTagPending(false);
        
        assertNotEquals(tokeniser.tagPending, tag);
        assertEquals(TokeniserState.Data, tag.state);
        assertFalse(tag.selfClosing);
    }
    
    @Test
    public void testEmitTagPending() {
        // Test the emitTagPending() method
        
        tokeniser.tagPending = new Token.StartTag("tag");
        tokeniser.emitTagPending();
        
        assertNotNull(tokeniser.emitPending);
        assertEquals(Token.TokenType.StartTag, tokeniser.emitPending.type);
        assertEquals("tag", ((Token.StartTag) tokeniser.emitPending).tagName);
    }
    
    @Test
    public void testCreateCommentPending() {
        // Test the createCommentPending() method
        
        tokeniser.createCommentPending();
        
        assertNotNull(tokeniser.commentPending);
    }
    
    @Test
    public void testEmitCommentPending() {
        // Test the emitCommentPending() method
        
        tokeniser.commentPending = new Token.Comment("comment");
        tokeniser.emitCommentPending();
        
        assertNotNull(tokeniser.emitPending);
        assertEquals(Token.TokenType.Comment, tokeniser.emitPending.type);
        assertEquals("comment", ((Token.Comment) tokeniser.emitPending).getData());
    }
    
    @Test
    public void testCreateDoctypePending() {
        // Test the createDoctypePending() method
        
        tokeniser.createDoctypePending();
        
        assertNotNull(tokeniser.doctypePending);
    }
    
    @Test
    public void testEmitDoctypePending() {
        // Test the emitDoctypePending() method
        
        tokeniser.doctypePending = new Token.Doctype("doctype");
        tokeniser.emitDoctypePending();
        
        assertNotNull(tokeniser.emitPending);
        assertEquals(Token.TokenType.Doctype, tokeniser.emitPending.type);
        assertEquals("doctype", ((Token.Doctype) tokeniser.emitPending).getName());
    }
    
    @Test
    public void testCreateTempBuffer() {
        // Test the createTempBuffer() method
        
        tokeniser.createTempBuffer();
        
        assertNotNull(tokeniser.dataBuffer);
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        // Test the isAppropriateEndTagToken() method
        
        tokeniser.tagPending = new Token.StartTag("tag");
        tokeniser.lastStartTag = new Token.StartTag("tag");
        
        assertTrue(tokeniser.isAppropriateEndTagToken());
        
        tokeniser.lastStartTag = new Token.StartTag("otherTag");
        
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }
    
    @Test
    public void testIsTrackErrors() {
        // Test the isTrackErrors() method
        
        boolean trackErrors = tokeniser.isTrackErrors();
        
        assertTrue(trackErrors);
    }
    
    @Test
    public void testSetTrackErrors() {
        // Test the setTrackErrors() method
        
        tokeniser.setTrackErrors(false);
        
        assertFalse(tokeniser.isTrackErrors());
    }
    
    @Test
    public void testError() {
        // Test the error() method
        
        tokeniser.error(TokeniserState.Rcdata);
        
        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpected character in input", tokeniser.errors.get(0).getMessage());
        assertEquals(TokeniserState.Rcdata, tokeniser.errors.get(0).getState());
    }
    
    @Test
    public void testEofError() {
        // Test the eofError() method
        
        tokeniser.eofError(TokeniserState.Rcdata);
        
        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpectedly reached end of file (EOF)", tokeniser.errors.get(0).getMessage());
        assertEquals(TokeniserState.Rcdata, tokeniser.errors.get(0).getState());
    }
    
    @Test
    public void testCharacterReferenceError() {
        // Test the characterReferenceError() method
        
        tokeniser.characterReferenceError();
        
        assertEquals(1, tokeniser.errors.size());
        assertEquals("Invalid character reference", tokeniser.errors.get(0).getMessage());
    }
    
    @Test
    public void testErrorStr() {
        // Test the error(String errorMsg) method
        
        tokeniser.error("Error");
        
        assertEquals(1, tokeniser.errors.size());
        assertEquals("Error", tokeniser.errors.get(0).getMessage());
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        // Test the currentNodeInHtmlNS() method
        
        boolean currentNodeInHtmlNS = tokeniser.currentNodeInHtmlNS();
        
        assertTrue(currentNodeInHtmlNS);
    }
}
