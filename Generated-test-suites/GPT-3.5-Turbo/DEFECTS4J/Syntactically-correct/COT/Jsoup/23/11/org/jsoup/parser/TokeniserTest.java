import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;
    
    @Before
    public void setUp() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
    }
    
    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);
    }
    
    @Test
    public void testEmit() {
        Token token = new Token.Character("test");
        tokeniser.emit(token);
        Token emittedToken = tokeniser.read();
        assertEquals(emittedToken, token);
    }
    
    @Test
    public void testEmitString() {
        tokeniser.emit("test");
        Token emittedToken = tokeniser.read();
        assertEquals(emittedToken.getValue(), "test");
    }
    
    @Test
    public void testEmitChar() {
        tokeniser.emit('t');
        tokeniser.emit('e');
        tokeniser.emit('s');
        tokeniser.emit('t');
        Token emittedToken = tokeniser.read();
        assertEquals(emittedToken.getValue(), "test");
    }
    
    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        assertEquals(state, TokeniserState.Data);
    }
    
    @Test
    public void testTransition() {
        tokeniser.transition(TokeniserState.TagName);
        TokeniserState state = tokeniser.getState();
        assertEquals(state, TokeniserState.TagName);
    }
    
    @Test
    public void testAdvanceTransition() {
        tokeniser.advanceTransition(TokeniserState.TagName);
        TokeniserState state = tokeniser.getState();
        assertEquals(state, TokeniserState.TagName);
    }
    
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }
    
    @Test
    public void testConsumeCharacterReference() {
        Character characterRef = tokeniser.consumeCharacterReference(null, false);
        assertNull(characterRef);
    }
    
    @Test
    public void testCreateTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);
        assertTrue(tokeniser.tagPending instanceof Token.StartTag);
        assertEquals(tokeniser.tagPending, tag);
    }
    
    @Test
    public void testEmitTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token emittedToken = tokeniser.read();
        assertEquals(emittedToken, tag);
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
        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken instanceof Token.Comment);
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
        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken instanceof Token.Doctype);
    }
    
    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        boolean isAppropriateEndTagToken = tokeniser.isAppropriateEndTagToken();
        assertFalse(isAppropriateEndTagToken);
    }
    
    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = new Token.StartTag("div");
        String tagName = tokeniser.appropriateEndTagName();
        assertEquals(tagName, "div");
    }
    
    @Test
    public void testError() {
        tokeniser.error(TokeniserState.EndTag);
        ParseErrorList errors = tokeniser.errors;
        assertFalse(errors.isEmpty());
    }
    
    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.EndTag);
        ParseErrorList errors = tokeniser.errors;
        assertFalse(errors.isEmpty());
    }
    
    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("Invalid character reference");
        ParseErrorList errors = tokeniser.errors;
        assertFalse(errors.isEmpty());
    }
    
    @Test
    public void testErrorString() {
        tokeniser.error("Error occurred");
        ParseErrorList errors = tokeniser.errors;
        assertFalse(errors.isEmpty());
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean currentNodeInHtmlNS = tokeniser.currentNodeInHtmlNS();
        assertTrue(currentNodeInHtmlNS);
    }
}