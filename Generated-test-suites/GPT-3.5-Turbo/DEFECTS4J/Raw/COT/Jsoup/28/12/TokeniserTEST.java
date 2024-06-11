
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {
    
    @Test
    public void testRead() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        
        // Test case with no tokens
        assertNull(tokeniser.read());
        
        // Test case with character token
        tokeniser.emit("test");
        Token.Character charToken = (Token.Character) tokeniser.read();
        assertEquals("test", charToken.getData());
        
        // Test case with start tag token
        Token.Tag startTagToken = new Token.StartTag();
        tokeniser.emit(startTagToken);
        Token emittedToken = tokeniser.read();
        assertSame(startTagToken, emittedToken);
    }
    
    @Test
    public void testEmit() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        
        // Test case with string emission
        tokeniser.emit("test");
        assertEquals("test", tokeniser.emitPending.getData());
        
        // Test case with char emission
        tokeniser.emit('a');
        assertEquals('a', tokeniser.emitPending.getData());
    }
    
    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        
        // Test case with start tag pending
        Token.Tag tagPending = tokeniser.createTagPending(true);
        tagPending.addAttributeName("attr1", "value1");
        tagPending.finaliseTag();
        tokeniser.emitTagPending();
        Token emittedToken = tokeniser.read();
        assertSame(tagPending, emittedToken);
        
        // Test case with end tag pending
        tagPending = tokeniser.createTagPending(false);
        tagPending.setTagName("tag");
        tagPending.finaliseTag();
        tokeniser.emitTagPending();
        emittedToken = tokeniser.read();
        assertSame(tagPending, emittedToken);
    }
    
    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        
        // Test case with comment pending
        tokeniser.createCommentPending();
        tokeniser.emit("test");
        tokeniser.emitCommentPending();
        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken instanceof Token.Comment);
        assertEquals("test", ((Token.Comment) emittedToken).getData());
    }
    
    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        
        // Test case with doctype pending
        tokeniser.createDoctypePending();
        Token.Doctype doctypePending = tokeniser.doctypePending;
        tokeniser.emit("test");
        tokeniser.emitDoctypePending();
        Token emittedToken = tokeniser.read();
        assertSame(doctypePending, emittedToken);
        assertEquals("test", doctypePending.getName());
    }
    
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        
        // Test case with self closing flag acknowledged
        tokeniser.selfClosingFlagAcknowledged = false;
        assertFalse(tokeniser.selfClosingFlagAcknowledged);
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }
    
    @Test
    public void testConsumeCharacterReference() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        
        // Test case with empty reader
        assertNull(tokeniser.consumeCharacterReference(null, false));
        
        // Test case with additional allowed character matching current character
        tokeniser.reader = new CharacterReader("a");
        assertNull(tokeniser.consumeCharacterReference('a', false));
        
        // Test case with current character matching several characters to skip
        tokeniser.reader = new CharacterReader(" \t\r\n\f<&");
        assertNull(tokeniser.consumeCharacterReference(null, false));
        
        // Test case with numeric reference
        tokeniser.reader = new CharacterReader("&#65;");
        Character charReference = tokeniser.consumeCharacterReference(null, false);
        assertEquals('A', charReference.charValue());
        
        // Test case with named reference
        tokeniser.reader = new CharacterReader("&amp;");
        charReference = tokeniser.consumeCharacterReference(null, false);
        assertEquals('&', charReference.charValue());
        
        // Test case with invalid named reference
        tokeniser.reader = new CharacterReader("&invalid;");
        assertNull(tokeniser.consumeCharacterReference(null, false));
    }
    
    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        
        // Test case with start tag pending
        Token.Tag tagPending = tokeniser.createTagPending(true);
        assertNotNull(tokeniser.tagPending);
        assertTrue(tokeniser.tagPending instanceof Token.StartTag);
        
        // Test case with end tag pending
        tagPending = tokeniser.createTagPending(false);
        assertNotNull(tokeniser.tagPending);
        assertTrue(tokeniser.tagPending instanceof Token.EndTag);
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        
        // Test case with null last start tag
        assertFalse(tokeniser.isAppropriateEndTagToken());
        
        // Test case with non-matching tag names
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.setTagName("start");
        tokeniser.tagPending = new Token.EndTag();
        tokeniser.tagPending.setTagName("end");
        assertFalse(tokeniser.isAppropriateEndTagToken());
        
        // Test case with matching tag names
        tokeniser.tagPending.setTagName("start");
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }
    
    @Test
    public void testAppropriateEndTagName() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        
        // Test case with null last start tag
        assertNull(tokeniser.appropriateEndTagName());
        
        // Test case with last start tag
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.setTagName("tag");
        assertEquals("tag", tokeniser.appropriateEndTagName());
    }
    
    @Test
    public void testError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.error(TokeniserState.Data);
        assertFalse(tokeniser.errors.isEmpty());
    }
    
    @Test
    public void testEofError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.eofError(TokeniserState.Data);
        assertFalse(tokeniser.errors.isEmpty());
    }
    
    @Test
    public void testCharacterReferenceError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.characterReferenceError("Invalid reference");
        assertFalse(tokeniser.errors.isEmpty());
    }
    
    @Test
    public void testErrorWithString() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.error("Error");
        assertFalse(tokeniser.errors.isEmpty());
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
    
}
