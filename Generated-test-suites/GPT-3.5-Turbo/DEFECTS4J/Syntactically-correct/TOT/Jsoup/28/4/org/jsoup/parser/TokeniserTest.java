import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(null, errors);
    }
    
    @Test
    public void testRead_characterToken() {
        tokeniser.charBuffer.append("hello");
        
        Token result = tokeniser.read();
        
        assertTrue(result instanceof Token.Character);
        assertEquals("hello", ((Token.Character) result).getData());
    }
    
    @Test
    public void testRead_emitPendingToken() {
        Token pendingToken = new Token.StartTag();
        pendingToken.type = Token.TokenType.StartTag;
        tokeniser.emitPending = pendingToken;
        tokeniser.isEmitPending = true;
        
        Token result = tokeniser.read();
        
        assertEquals(pendingToken, result);
        assertNull(tokeniser.emitPending);
        assertFalse(tokeniser.isEmitPending);
    }
    
    @Test
    public void testEmit_startTagToken() {
        Token tagToken = new Token.StartTag();
        tagToken.type = Token.TokenType.StartTag;
        tokeniser.emit(tagToken);
        
        Token result = tokeniser.emitPending;
        
        assertEquals(tagToken, result);
        assertTrue(tokeniser.isEmitPending);
    }
    
    @Test
    public void testEmit_endTagToken_withAttributes() {
        Token tagToken = new Token.EndTag();
        tagToken.type = Token.TokenType.EndTag;
        Token.Attribute attribute = new Token.Attribute();
        attribute.key = "testAttr";
        attribute.value = "testValue";
        tagToken.attributes = new ArrayList<>();
        tagToken.attributes.add(attribute);
        tokeniser.emit(tagToken);
        
        Token result = tokeniser.emitPending;
        
        assertEquals(tagToken, result);
        assertTrue(tokeniser.isEmitPending);
        assertTrue(errors.hasErrors());
    } 
    
    @Test
    public void testEmit_character() {
        tokeniser.emit("a");
        
        String charBuffer = tokeniser.charBuffer.toString();
        
        assertEquals("a", charBuffer);
    }
    
    @Test
    public void testTransition() {
        TokeniserState newState = TokeniserState.RCDATA;
        
        tokeniser.transition(newState);
        
        assertEquals(newState, tokeniser.getState());
    }
    
    @Test
    public void testAdvanceTransition() {
        TokeniserState newState = TokeniserState.RCDATA;
        tokeniser.reader = new CharacterReader("hello");
        
        tokeniser.advanceTransition(newState);
        
        assertEquals(newState, tokeniser.getState());
        assertEquals(1, tokeniser.reader.pos());
    }
    
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.selfClosingFlagAcknowledged = false;
        
        tokeniser.acknowledgeSelfClosingFlag();
        
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }
    
    @Test
    public void testConsumeCharacterReference_emptyReader() {
        Character result = tokeniser.consumeCharacterReference(null, false);
        
        assertNull(result);
    }
    
    @Test
    public void testConsumeCharacterReference_additionalAllowedCharacterMatching() {
        tokeniser.reader = new CharacterReader("a");
        
        Character result = tokeniser.consumeCharacterReference('a', false);
        
        assertNull(result);
    }
    
    @Test
    public void testConsumeCharacterReference_matchesNonCharacterTokens() {
        tokeniser.reader = new CharacterReader(" \t\n\r\f<&");
        
        Character result = tokeniser.consumeCharacterReference(null, false);
        
        assertNull(result);
    }
    
    @Test
    public void testConsumeCharacterReference_numericReference() {
        tokeniser.reader = new CharacterReader("#xA9;");
        
        Character result = tokeniser.consumeCharacterReference(null, false);
        
        assertEquals('\u00A9', (char) result);
    }
    
    @Test
    public void testConsumeCharacterReference_invalidNumericReference() {
        tokeniser.reader = new CharacterReader("#;");
        
        Character result = tokeniser.consumeCharacterReference(null, false);
        
        assertNull(result);
        assertTrue(errors.hasErrors());
    }
    
    @Test
    public void testConsumeCharacterReference_namedReference_withInAttribute() {
        tokeniser.reader = new CharacterReader("&lt;");
        
        Character result = tokeniser.consumeCharacterReference(null, true);
        
        assertEquals('<', (char) result);
    }
    
    @Test
    public void testConsumeCharacterReference_namedReference_invalidNamedReference() {
        tokeniser.reader = new CharacterReader("&invalid;");
        
        Character result = tokeniser.consumeCharacterReference(null, false);
        
        assertNull(result);
        assertTrue(errors.hasErrors());
    }
    
    @Test
    public void testConsumeCharacterReference_namedReference_illegalCharacterAfterReference() {
        tokeniser.reader = new CharacterReader("&lt;=");
        
        Character result = tokeniser.consumeCharacterReference(null, false);
        
        assertNull(result);
    }
    
    @Test
    public void testCreateTagPending_startTag() {
        Token.Tag result = tokeniser.createTagPending(true);
        
        assertTrue(result instanceof Token.StartTag);
        assertNull(tokeniser.tagPending.attributes);
    }
    
    @Test
    public void testCreateTagPending_endTag() {
        Token.Tag result = tokeniser.createTagPending(false);
        
        assertTrue(result instanceof Token.EndTag);
        assertNull(tokeniser.tagPending.attributes);
    }
    
    @Test
    public void testEmitTagPending() {
        Token.Tag tagPending = new Token.StartTag();
        tagPending.tagName = "div";
        tagPending.attributes = new ArrayList<>();
        Token.Attribute attribute = new Token.Attribute();
        attribute.key = "class";
        attribute.value = "test";
        tagPending.attributes.add(attribute);
        tokeniser.tagPending = tagPending;
        
        tokeniser.emitTagPending();
        
        Token result = tokeniser.emitPending;
        
        assertEquals(tagPending, result);
    }
    
    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        
        assertTrue(tokeniser.commentPending instanceof Token.Comment);
    }
    
    @Test
    public void testEmitCommentPending() {
        Token.Comment comment = new Token.Comment();
        comment.data = "test comment";
        tokeniser.commentPending = comment;
        
        tokeniser.emitCommentPending();
        
        Token result = tokeniser.emitPending;
        
        assertEquals(comment, result);
    }
    
    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        
        assertTrue(tokeniser.doctypePending instanceof Token.Doctype);
    }
    
    @Test
    public void testEmitDoctypePending() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name = "html";
        doctype.publicIdentifier = "-//W3C//DTD XHTML 1.0 Transitional//EN";
        doctype.systemIdentifier = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
        doctype.forceQuirks = true;
        tokeniser.doctypePending = doctype;
        
        tokeniser.emitDoctypePending();
        
        Token result = tokeniser.emitPending;
        
        assertEquals(doctype, result);
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
        tokeniser.tagPending.tagName = "div";
        
        boolean result = tokeniser.isAppropriateEndTagToken();
        
        assertTrue(result);
    }
    
    @Test
    public void testAppropriateEndTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        tokeniser.lastStartTag = startTag;
        
        String result = tokeniser.appropriateEndTagName();
        
        assertEquals("div", result);
    }
    
    @Test
    public void testError_withState() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.reader = new CharacterReader("a");
        
        tokeniser.error(state);
        
        assertTrue(errors.hasErrors());
    }
    
    @Test
    public void testEofError_withState() {
        TokeniserState state = TokeniserState.Data;
        
        tokeniser.eofError(state);
        
        assertTrue(errors.hasErrors());
    }
    
    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("Invalid character reference");
        
        assertTrue(errors.hasErrors());
    }
    
    @Test
    public void testError_withErrorMessage() {
        String errorMsg = "Error occurred";
        
        tokeniser.error(errorMsg);
        
        assertTrue(errors.hasErrors());
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean result = tokeniser.currentNodeInHtmlNS();
        
        assertTrue(result);
    }
}