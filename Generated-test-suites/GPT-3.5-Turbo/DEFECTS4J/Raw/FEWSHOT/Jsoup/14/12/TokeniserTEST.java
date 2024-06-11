import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.Entities;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class TokeniserTest {
    private Tokeniser tokeniser;
    
    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        tokeniser = new Tokeniser(reader);
    }
    
    @Test
    public void testRead() {
        // tokenisation state is initially set to Data
        assertEquals(TokeniserState.Data, tokeniser.getState());
        
        // Test reading non-character token
        Token.Comment commentToken = new Token.Comment();
        tokeniser.emit(commentToken);
        assertEquals(commentToken, tokeniser.read());
        
        // Test reading character token
        Token.Character characterToken = new Token.Character("test");
        tokeniser.emit(characterToken);
        assertEquals(characterToken, tokeniser.read());
    }
    
    @Test
    public void testEmit() {
        Token.StartTag startTag = new Token.StartTag();
        tokeniser.emit(startTag);
        
        assertEquals(startTag, tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);
    }
    
    @Test
    public void testEmitString() {
        tokeniser.emit("test");
        
        assertEquals("test", tokeniser.charBuffer.toString());
    }
    
    @Test
    public void testEmitChar() {
        tokeniser.emit('t');
        
        assertEquals("t", tokeniser.charBuffer.toString());
    }
    
    @Test
    public void testGetState() {
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }
    
    @Test
    public void testTransition() {
        tokeniser.transition(TokeniserState.TagName);
        
        assertEquals(TokeniserState.TagName, tokeniser.getState());
    }
    
    @Test
    public void testAdvanceTransition() {
        tokeniser.reader.advance();
        tokeniser.advanceTransition(TokeniserState.CharacterReferenceInData);
        
        assertEquals(TokeniserState.CharacterReferenceInData, tokeniser.getState());
    }
    
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }
    
    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("test");
        tokeniser.reader = reader;
        
        assertNull(tokeniser.consumeCharacterReference(null, false));
    }
    
    @Test
    public void testCreateTagPending() {
        Token.Tag tagPending = tokeniser.createTagPending(true);
        
        assertTrue(tagPending instanceof Token.StartTag);
        assertEquals(tagPending, tokeniser.tagPending);
    }
    
    @Test
    public void testEmitTagPending() {
        Token.Tag tagPending = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        
        assertEquals(tagPending.tagName, tokeniser.lastStartTag.tagName);
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
        
        assertTrue(tokeniser.commentPending instanceof Token.Comment);
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
        
        assertTrue(tokeniser.doctypePending instanceof Token.Doctype);
    }
    
    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        
        assertEquals(new StringBuilder(), tokeniser.dataBuffer);
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.lastStartTag = new Token.StartTag();
        
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }
    
    @Test
    public void testIsTrackErrors() {
        assertTrue(tokeniser.isTrackErrors());
        
        tokeniser.setTrackErrors(false);
        
        assertFalse(tokeniser.isTrackErrors());
    }
    
    @Test
    public void testError() {
        TokeniserState state = TokeniserState.AttributeValue_doubleQuoted;
        tokeniser.error(state);
        
        List<ParseError> errors = new ArrayList<ParseError>();
        errors.add(new ParseError("Unexpected character in input", tokeniser.reader.current(), state, tokeniser.reader.pos()));
        
        assertEquals(errors, tokeniser.errors);
    }
    
    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.eofError(state);
        
        List<ParseError> errors = new ArrayList<ParseError>();
        errors.add(new ParseError("Unexpectedly reached end of file (EOF)", state, tokeniser.reader.pos()));
        
        assertEquals(errors, tokeniser.errors);
    }
    
    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError();
        
        List<ParseError> errors = new ArrayList<ParseError>();
        errors.add(new ParseError("Invalid character reference", tokeniser.reader.pos()));
        
        assertEquals(errors, tokeniser.errors);
    }
    
    @Test
    public void testErrorWithCustomMessage() {
        String errorMsg = "Custom error message";
        tokeniser.error(errorMsg);
        
        List<ParseError> errors = new ArrayList<ParseError>();
        errors.add(new ParseError(errorMsg, tokeniser.reader.pos()));
        
        assertEquals(errors, tokeniser.errors);
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}