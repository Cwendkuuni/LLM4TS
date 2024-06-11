package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TokeniserTest {
    
    @Test
    public void testRead() {
        CharacterReader reader = new CharacterReader("Test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        Token token = tokeniser.read();
        
        Assert.assertEquals("Test", ((Token.Character) token).getData());
    }
    
    @Test
    public void testEmit() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Token token = new Token.Character("Test");
        
        tokeniser.emit(token);
        
        Assert.assertEquals(token, tokeniser.read());
    }
    
    @Test
    public void testEmitString() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        
        tokeniser.emit("Test");
        
        Assert.assertEquals("Test", ((Token.Character) tokeniser.read()).getData());
    }
    
    @Test
    public void testEmitChar() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        
        tokeniser.emit('T');
        
        Assert.assertEquals("T", ((Token.Character) tokeniser.read()).getData());
    }
    
    @Test
    public void testTransition() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        TokeniserState state = TokeniserState.TagName;
        
        tokeniser.transition(state);
        
        Assert.assertEquals(state, tokeniser.getState());
    }
    
    @Test
    public void testAdvanceTransition() {
        CharacterReader reader = new CharacterReader("Test");
        Tokeniser tokeniser = new Tokeniser(reader, null);
        TokeniserState state = TokeniserState.TagName;
        
        tokeniser.advanceTransition(state);
        
        Assert.assertEquals(state, tokeniser.getState());
        Assert.assertEquals('e', reader.current());
    }
    
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        
        tokeniser.acknowledgeSelfClosingFlag();
        
        Assert.assertTrue(tokeniser.isEmitPending());
    }
    
    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("&amp;");
        Tokeniser tokeniser = new Tokeniser(reader, null);
        
        Character character = tokeniser.consumeCharacterReference(null, false);
        
        Assert.assertEquals('&', character.charValue());
    }
    
    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        
        tokeniser.createTagPending(true);
        
        Assert.assertEquals(Token.TokenType.StartTag, tokeniser.getTagPending().type);
    }
    
    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Token.Tag tagPending = tokeniser.createTagPending(true);
        
        tokeniser.emitTagPending();
        
        Assert.assertEquals(tagPending, tokeniser.read());
    }
    
    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        
        tokeniser.createCommentPending();
        
        Assert.assertNotNull(tokeniser.getCommentPending());
    }
    
    @Test
    public void testEmitCommentPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Token.Comment commentPending = new Token.Comment();
        tokeniser.createCommentPending();
        
        tokeniser.emitCommentPending();
        
        Assert.assertEquals(commentPending, tokeniser.read());
    }
    
    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        
        tokeniser.createDoctypePending();
        
        Assert.assertNotNull(tokeniser.getDoctypePending());
    }
    
    @Test
    public void testEmitDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Token.Doctype doctypePending = new Token.Doctype();
        tokeniser.createDoctypePending();
        
        tokeniser.emitDoctypePending();
        
        Assert.assertEquals(doctypePending, tokeniser.read());
    }
    
    @Test
    public void testCreateTempBuffer() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        
        tokeniser.createTempBuffer();
        
        Assert.assertNotNull(tokeniser.getDataBuffer());
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        tokeniser.createTagPending(true);
        tokeniser.getTagPending().tagName = "div";
        tokeniser.setLastStartTag(startTag);
        
        Assert.assertTrue(tokeniser.isAppropriateEndTagToken());
    }
    
    @Test
    public void testAppropriateEndTagName() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        tokeniser.createTagPending(true);
        tokeniser.getTagPending().tagName = "div";
        tokeniser.setLastStartTag(startTag);
        
        Assert.assertEquals("div", tokeniser.appropriateEndTagName());
    }
    
    @Test
    public void testError() {
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(null, errors);
        TokeniserState state = TokeniserState.TagName;
        
        tokeniser.error(state);
        
        Assert.assertEquals(1, errors.size());
    }
    
    @Test
    public void testEofError() {
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(null, errors);
        TokeniserState state = TokeniserState.TagName;
        
        tokeniser.eofError(state);
        
        Assert.assertEquals(1, errors.size());
    }
    
    @Test
    public void testCharacterReferenceError() {
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(null, errors);
        
        tokeniser.characterReferenceError("Invalid character reference");
        
        Assert.assertEquals(1, errors.size());
    }
    
    @Test
    public void testErrorWithMessage() {
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(null, errors);
        
        tokeniser.error("Error message");
        
        Assert.assertEquals(1, errors.size());
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        
        Assert.assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}