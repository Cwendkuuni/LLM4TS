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
        Token token = new Token.StartTag();
        tokeniser.emit(token);
        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals(str, token.toString());
    }

    @Test
    public void testEmitChar() {
        char c = 'a';
        tokeniser.emit(c);
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals(c, token.toString().charAt(0));
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        Assert.assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.Rcdata;
        tokeniser.transition(state);
        Assert.assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.Rcdata;
        tokeniser.advanceTransition(state);
        Assert.assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        Assert.assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = null;
        boolean inAttribute = false;
        Character characterReference = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        Assert.assertNull(characterReference);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        Assert.assertNotNull(tag);
        Assert.assertEquals(start, tag.start);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "tag";
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("tag", ((Token.StartTag) token).tagName);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        Assert.assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.commentPending.data = "comment";
        tokeniser.emitCommentPending();
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Comment, token.type);
        Assert.assertEquals("comment", ((Token.Comment) token).data);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        Assert.assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.name = "doctype";
        tokeniser.emitDoctypePending();
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Doctype, token.type);
        Assert.assertEquals("doctype", ((Token.Doctype) token).name);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        Assert.assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "tag";
        tokeniser.lastStartTag = tokeniser.tagPending;
        boolean isAppropriate = tokeniser.isAppropriateEndTagToken();
        Assert.assertTrue(isAppropriate);
    }

    @Test
    public void testIsTrackErrors() {
        boolean trackErrors = tokeniser.isTrackErrors();
        Assert.assertTrue(trackErrors);
    }

    @Test
    public void testSetTrackErrors() {
        boolean trackErrors = false;
        tokeniser.setTrackErrors(trackErrors);
        Assert.assertFalse(tokeniser.isTrackErrors());
    }

    @Test
    public void testErrorTokeniserState() {
        tokeniser.error(TokeniserState.Data);
        Assert.assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        Assert.assertEquals("Unexpected character in input", error.errorMsg);
        Assert.assertEquals(TokeniserState.Data, error.state);
    }

    @Test
    public void testEofErrorTokeniserState() {
        tokeniser.eofError(TokeniserState.Data);
        Assert.assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        Assert.assertEquals("Unexpectedly reached end of file (EOF)", error.errorMsg);
        Assert.assertEquals(TokeniserState.Data, error.state);
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError();
        Assert.assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        Assert.assertEquals("Invalid character reference", error.errorMsg);
    }

    @Test
    public void testError() {
        String errorMsg = "Error message";
        tokeniser.error(errorMsg);
        Assert.assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        Assert.assertEquals(errorMsg, error.errorMsg);
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean currentNodeInHtmlNS = tokeniser.currentNodeInHtmlNS();
        Assert.assertTrue(currentNodeInHtmlNS);
    }

}