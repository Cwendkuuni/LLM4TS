import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TokeniserTest {
    
    private Tokeniser tokeniser;
    
    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        tokeniser = new Tokeniser(reader);
    }
    
    @Test
    public void testRead() {
        Token result = tokeniser.read();
        
        assertEquals(null, result);
    }
    
    @Test
    public void testEmit() {
        Token token = new Token.Character("test");
        tokeniser.emit(token);
        
        assertEquals(token, tokeniser.emitPending);
        assertEquals(true, tokeniser.isEmitPending);
    }
    
    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        
        assertEquals(str, tokeniser.charBuffer.toString());
    }
    
    @Test
    public void testEmitChar() {
        char c = 'c';
        tokeniser.emit(c);

        assertEquals(String.valueOf(c), tokeniser.charBuffer.toString());
    }
    
    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        
        assertEquals(TokeniserState.Data, state);
    }
    
    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.ScriptData;
        tokeniser.transition(state);
        
        assertEquals(state, tokeniser.getState());
    }
    
    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.ScriptData;
        tokeniser.advanceTransition(state);
        
        assertEquals(state, tokeniser.getState());
    }
    
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        
        assertEquals(true, tokeniser.selfClosingFlagAcknowledged);
    }
    
    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = null;
        boolean inAttribute = false;
        
        Character result = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        
        assertEquals(null, result);
    }
    
    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        
        assertNotNull(tag);
        assertEquals(start, tag instanceof Token.StartTag);
        assertEquals(tag, tokeniser.tagPending);
    }
    
    @Test
    public void testEmitTagPending() {
        Token.StartTag tag = new Token.StartTag();
        tag.data = "div";
        tokeniser.tagPending = tag;
        
        tokeniser.emitTagPending();
        
        assertEquals(tag, tokeniser.emitPending);
    }
    
    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        
        assertNotNull(tokeniser.commentPending);
    }
    
    @Test
    public void testEmitCommentPending() {
        Token.Comment comment = new Token.Comment();
        comment.data = "test comment";
        tokeniser.commentPending = comment;
        
        tokeniser.emitCommentPending();
        
        assertEquals(comment, tokeniser.emitPending);
    }
    
    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        
        assertNotNull(tokeniser.doctypePending);
    }
    
    @Test
    public void testEmitDoctypePending() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.data = "test doctype";
        tokeniser.doctypePending = doctype;
        
        tokeniser.emitDoctypePending();
        
        assertEquals(doctype, tokeniser.emitPending);
    }
    
    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        
        assertNotNull(tokeniser.dataBuffer);
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        tokeniser.lastStartTag = startTag;
        tokeniser.tagPending = new Token.EndTag();
        
        boolean result = tokeniser.isAppropriateEndTagToken();
        
        assertEquals(true, result);
    }
    
    @Test
    public void testIsAppropriateEndTagTokenFalse() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        tokeniser.lastStartTag = startTag;
        tokeniser.tagPending = new Token.EndTag();
        tokeniser.tagPending.tagName = "p";
        
        boolean result = tokeniser.isAppropriateEndTagToken();
        
        assertEquals(false, result);
    }
    
    @Test
    public void testIsTrackErrors() {
        boolean result = tokeniser.isTrackErrors();
        
        assertEquals(true, result);
    }
    
    @Test
    public void testSetTrackErrors() {
        boolean trackErrors = false;
        tokeniser.setTrackErrors(trackErrors);
        
        assertEquals(trackErrors, tokeniser.isTrackErrors());
    }
    
    @Test
    public void testError() {
        TokeniserState state = TokeniserState.TagName;
        tokeniser.error(state);
        
        assertEquals(1, tokeniser.errors.size());
    }
    
    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.TagName;
        tokeniser.eofError(state);
        
        assertEquals(1, tokeniser.errors.size());
    }
    
    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError();
        
        assertEquals(1, tokeniser.errors.size());
    }
    
    @Test
    public void testErrorWithMsg() {
        String errorMsg = "Error message";
        tokeniser.error(errorMsg);
        
        assertEquals(1, tokeniser.errors.size());
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean result = tokeniser.currentNodeInHtmlNS();
        
        assertEquals(true, result);
    }
}