
package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {
    
    private Tokeniser tokeniser;
    private ParseErrorList errors;
    private CharacterReader reader;
    
    @Before
    public void setUp() {
        reader = new CharacterReader("");
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }
    
    @Test
    public void testRead_emptyString_returnsNull() {
        Token token = tokeniser.read();
        assertNull(token);
    }
    
    @Test
    public void testEmit_stringEmit_stringReturned() {
        String str = "test";
        tokeniser.emit(str);
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        Token.Character characterToken = (Token.Character) token;
        assertEquals(str, characterToken.getData());    
    }
    
    @Test
    public void testEmit_charArrayEmit_stringReturned() {
        char[] chars = {'t', 'e', 's', 't'};
        tokeniser.emit(chars);
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        Token.Character characterToken = (Token.Character) token;
        assertEquals("test", characterToken.getData());
    }
    
    @Test
    public void testEmit_emptyStringEmit_stringReturned() {
        tokeniser.emit("");
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        Token.Character characterToken = (Token.Character) token;
        assertEquals("", characterToken.getData());
    }
    
    @Test
    public void testEmit_intArrayEmit_stringReturned() {
        int[] codepoints = {116, 101, 115, 116};
        tokeniser.emit(codepoints);
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        Token.Character characterToken = (Token.Character) token;
        assertEquals("test", characterToken.getData());   
    }
    
    @Test
    public void testAdvanceTransition_stateChanged() {
        TokeniserState state = TokeniserState.Attributes; // arbitrary state
        tokeniser.advanceTransition(state);
        assertEquals(state, tokeniser.getState());
    }
    
    @Test
    public void testAcknowledgeSelfClosingFlag_flagAcknowledged() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }
    
    @Test
    public void testConsumeCharacterReference_validReference_returnsCodepoint() {
        String characterReference = "&#65;";
        CharacterReader reader = new CharacterReader(characterReference);
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        int[] expected = {65};
        
        int[] actual = tokeniser.consumeCharacterReference(null, false);
        
        assertTrue(Arrays.equals(expected, actual));
    }
    
    @Test
    public void testConsumeCharacterReference_invalidReference_returnsNull() {
        String characterReference = "&#999999;";
        CharacterReader reader = new CharacterReader(characterReference);
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        int[] actual = tokeniser.consumeCharacterReference(null, false);
        
        assertNull(actual);
    }
    
    @Test
    public void testCreateTagPending_start_true() {
        Token.Tag tagPending = tokeniser.createTagPending(true);
        assertTrue(tagPending instanceof Token.StartTag);
    }
    
    @Test
    public void testCreateTagPending_start_false() {
        Token.Tag tagPending = tokeniser.createTagPending(false);
        assertTrue(tagPending instanceof Token.EndTag);
    }
    
    @Test
    public void testEmitTagPending_emitStartTag_tokenEmitted() {
        Token.StartTag startTag = new Token.StartTag();
        tokeniser.tagPending = startTag;
        
        tokeniser.emitTagPending();
        
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.StartTag);
        assertEquals(startTag, token);
    }
    
    @Test
    public void testEmitTagPending_emitEndTag_tokenEmitted() {
        Token.EndTag endTag = new Token.EndTag();
        tokeniser.tagPending = endTag;
        
        tokeniser.emitTagPending();
        
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.EndTag);
        assertEquals(endTag, token);
    }
    
    @Test
    public void testCreateCommentPending_commentPendingCreated() {
        tokeniser.createCommentPending();
        
        assertTrue(tokeniser.commentPending instanceof Token.Comment);
    }
    
    @Test
    public void testEmitCommentPending_commentPendingEmitted() {
        tokeniser.commentPending = new Token.Comment();
        
        tokeniser.emitCommentPending();
        
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Comment);
        assertEquals(tokeniser.commentPending, token);
    }
    
    @Test
    public void testCreateDoctypePending_doctypePendingCreated() {
        tokeniser.createDoctypePending();
        
        assertTrue(tokeniser.doctypePending instanceof Token.Doctype);
    }
    
    @Test
    public void testEmitDoctypePending_doctypePendingEmitted() {
        tokeniser.doctypePending = new Token.Doctype();
        
        tokeniser.emitDoctypePending();
        
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Doctype);
        assertEquals(tokeniser.doctypePending, token);
    }
    
    @Test
    public void testCreateTempBuffer_bufferCleared() {
        tokeniser.dataBuffer.append("test");
        tokeniser.createTempBuffer();
        
        assertEquals("", tokeniser.dataBuffer.toString());
    }
    
    @Test
    public void testIsAppropriateEndTagToken_lastStartTagIsNull_returnsFalse() {
        boolean isAppropriateEndTagToken = tokeniser.isAppropriateEndTagToken();
        
        assertFalse(isAppropriateEndTagToken);
    }
    
    @Test
    public void testIsAppropriateEndTagToken_tagPendingNameEqualsLastStartTag_returnsTrue() {
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.lastStartTag = "p";
        
        boolean isAppropriateEndTagToken = tokeniser.isAppropriateEndTagToken();
        
        assertTrue(isAppropriateEndTagToken);
    }
    
    @Test
    public void testAppropriateEndTagName_lastStartTagIsNull_returnsNull() {
        tokeniser.lastStartTag = null;
        
        String endTagName = tokeniser.appropriateEndTagName();
        
        assertNull(endTagName);
    }
    
    @Test
    public void testAppropriateEndTagName_returnsLastStartTag() {
        tokeniser.lastStartTag = "p";
        
        String endTagName = tokeniser.appropriateEndTagName();
        
        assertEquals("p", endTagName);
    }
    
    @Test
    public void testError_errorAddedToErrorsList() {
        String errorMsg = "Test error";
        
        tokeniser.error(errorMsg);
        
        assertEquals(1, errors.size());
        ParseError parseError = errors.get(0);
        assertEquals(errorMsg, parseError.getErrorMessage());
    }
    
    @Test
    public void testCurrentNodeInHtmlNS_returnsTrue() {
        boolean currentNodeInHtmlNS = tokeniser.currentNodeInHtmlNS();
        
        assertTrue(currentNodeInHtmlNS);
    }
    
    @Test
    public void testUnescapeEntities_entitiesUnescaped() {
        String str = "&nbsp;";
        String expected = "\u00A0";
        tokeniser.reader = new CharacterReader(str);
        
        String actual = tokeniser.unescapeEntities(false);
        
        assertEquals(expected, actual);
    }
}

