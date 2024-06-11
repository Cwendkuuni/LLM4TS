package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TokeniserTest {
    
    private Tokeniser tokeniser;
    
    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        tokeniser = new Tokeniser(reader);
    }
    
    @Test
    public void testRead() {
        Token token = tokeniser.read();
        Assert.assertNull(token);
    }

    @Test
    public void testEmit() {
        Token tagToken = new Token.StartTag("tag");
        tokeniser.emit(tagToken);
        Token emittedToken = tokeniser.read();
        Assert.assertEquals(tagToken, emittedToken);
    }

    @Test
    public void testEmitWithString() {
        String str = "test";
        tokeniser.emit(str);
        Token emittedToken = tokeniser.read();
        Token expectedToken = new Token.Character(str);
        Assert.assertEquals(expectedToken, emittedToken);
    }

    @Test
    public void testEmitWithChar() {
        char c = 'a';
        tokeniser.emit(c);
        Token emittedToken = tokeniser.read();
        Token expectedToken = new Token.Character(Character.toString(c));
        Assert.assertEquals(expectedToken, emittedToken);
    }

    @Test
    public void testGetState() {
        TokeniserState expectedState = TokeniserState.Data;
        TokeniserState currentState = tokeniser.getState();
        Assert.assertEquals(expectedState, currentState);
    }

    @Test
    public void testTransition() {
        TokeniserState newState = TokeniserState.TagName;
        tokeniser.transition(newState);
        TokeniserState currentState = tokeniser.getState();
        Assert.assertEquals(newState, currentState);
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState newState = TokeniserState.TagName;
        tokeniser.advanceTransition(newState);
        TokeniserState currentState = tokeniser.getState();
        Assert.assertEquals(newState, currentState);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        boolean selfClosingFlagAcknowledged = tokeniser.isSelfClosingFlagAcknowledged();
        Assert.assertTrue(selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = 'a';
        boolean inAttribute = false;
        Character character = tokeniser.consumeCharacterReference(aditionalAllowedCharacter, inAttribute);
        Assert.assertNull(character);
    }

    @Test
    public void testCreateTagPending() {
        boolean isStartTag = true;
        Token.Tag tag = tokeniser.createTagPending(isStartTag);
        Assert.assertNotNull(tag);
        Assert.assertTrue(tag instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token emittedToken = tokeniser.read();
        Assert.assertTrue(emittedToken instanceof Token.StartTag);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        Assert.assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token emittedToken = tokeniser.read();
        Assert.assertTrue(emittedToken instanceof Token.Comment);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        Assert.assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token emittedToken = tokeniser.read();
        Assert.assertTrue(emittedToken instanceof Token.Doctype);
    }
    
    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        Assert.assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        boolean isAppropriateEndTagToken = tokeniser.isAppropriateEndTagToken();
        Assert.assertFalse(isAppropriateEndTagToken);
    }

    @Test
    public void testIsTrackErrors() {
        boolean isTrackErrors = tokeniser.isTrackErrors();
        Assert.assertTrue(isTrackErrors);
    }

    @Test
    public void testSetTrackErrors() {
        boolean trackErrors = false;
        tokeniser.setTrackErrors(trackErrors);
        boolean isTrackErrors = tokeniser.isTrackErrors();
        Assert.assertFalse(isTrackErrors);
    }

    @Test
    public void testError() {
        TokeniserState state = TokeniserState.TagName;
        tokeniser.error(state);
        // assert error handling
    }

    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.eofError(state);
        // assert error handling
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError();
        // assert error handling
    }

    @Test
    public void testErrorWithMessage() {
        String errorMsg = "Invalid input";
        tokeniser.error(errorMsg);
        // assert error handling
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean currentNodeInHtmlNS = tokeniser.currentNodeInHtmlNS();
        // assert
    }
}