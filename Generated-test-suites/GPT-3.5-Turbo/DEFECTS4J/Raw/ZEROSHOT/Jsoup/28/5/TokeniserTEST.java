package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        Assert.assertNull(token);
    }

    @Test
    public void testEmit() {
        Token token = tokeniser.createTagPending(true);
        tokeniser.emit(token);
        Token emittedToken = tokeniser.read();
        Assert.assertEquals(token, emittedToken);
    }

    @Test
    public void testEmitString() {
        String str = "example";
        tokeniser.emit(str);
        Token emittedToken = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, emittedToken.type);
        Assert.assertEquals(str, ((Token.Character) emittedToken).getData());
    }

    @Test
    public void testEmitChar() {
        char c = 'a';
        tokeniser.emit(c);
        Token emittedToken = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, emittedToken.type);
        Assert.assertEquals(Character.toString(c), ((Token.Character) emittedToken).getData());
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        Assert.assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.Doctype;
        tokeniser.transition(state);
        Assert.assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.TagName;
        tokeniser.advanceTransition(state);
        Assert.assertEquals(state, tokeniser.getState());
        Assert.assertEquals(1, tokeniser.reader.pos());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        Assert.assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = 'a';
        boolean inAttribute = true;
        Character character = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        Assert.assertNull(character);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tagPending = tokeniser.createTagPending(start);
        Assert.assertTrue(tagPending instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token emittedToken = tokeniser.read();
        Assert.assertTrue(emittedToken instanceof Token.Tag);
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
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.tagPending = new Token.StartTag();
        boolean isAppropriate = tokeniser.isAppropriateEndTagToken();
        Assert.assertTrue(isAppropriate);
    }

    @Test
    public void testAppropriateEndTagName() {
        String tagName = "example";
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = tagName;
        String endTagName = tokeniser.appropriateEndTagName();
        Assert.assertEquals(tagName, endTagName);
    }

    @Test
    public void testError() {
        TokeniserState state = TokeniserState.TagName;
        tokeniser.error(state);
        Assert.assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.Doctype;
        tokeniser.eofError(state);
        Assert.assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "Invalid reference";
        tokeniser.characterReferenceError(message);
        Assert.assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testErrorWithMessage() {
        String errorMsg = "Error message";
        tokeniser.error(errorMsg);
        Assert.assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean inHtmlNS = tokeniser.currentNodeInHtmlNS();
        Assert.assertTrue(inHtmlNS);
    }

}