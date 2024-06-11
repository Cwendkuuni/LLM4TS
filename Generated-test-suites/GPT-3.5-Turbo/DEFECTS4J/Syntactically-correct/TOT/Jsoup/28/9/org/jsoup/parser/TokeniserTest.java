package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(new CharacterReader(""), errors);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        Assert.assertNull(token);
    }

    @Test
    public void testEmit() {
        Token token = new Token.Tag();
        tokeniser.emit(token);
        Assert.assertEquals(token, tokeniser.read());
    }

    @Test
    public void testEmitString() {
        tokeniser.emit("Some string");
        Token token = tokeniser.read();
        Assert.assertEquals("Some string", ((Token.Character) token).getData());
    }

    @Test
    public void testEmitChar() {
        tokeniser.emit('A');
        Token token = tokeniser.read();
        Assert.assertEquals("A", ((Token.Character) token).getData());
    }

    @Test
    public void testTransition() {
        tokeniser.transition(TokeniserState.CharRefInData);
        Assert.assertEquals(TokeniserState.CharRefInData, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        tokeniser.advanceTransition(TokeniserState.CharRefInData);
        Assert.assertEquals(TokeniserState.CharRefInData, tokeniser.getState());
        Assert.assertEquals("", tokeniser.read());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        Assert.assertTrue(tokeniser.isSelfClosingFlagAcknowledged());
    }

    @Test
    public void testConsumeCharacterReference() {
        Character result = tokeniser.consumeCharacterReference(null, false);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateTagPending() {
        tokeniser.createTagPending(true);
        Token.Tag tag = tokeniser.getTagPending();
        Assert.assertTrue(tag instanceof Token.StartTag);
        Assert.assertTrue(tag.start);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        Assert.assertTrue(token instanceof Token.StartTag);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        Token.Comment comment = tokeniser.getCommentPending();
        Assert.assertNotNull(comment);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token token = tokeniser.read();
        Assert.assertTrue(token instanceof Token.Comment);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        Token.Doctype doctype = tokeniser.getDoctypePending();
        Assert.assertNotNull(doctype);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token token = tokeniser.read();
        Assert.assertTrue(token instanceof Token.Doctype);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        StringBuilder tempBuffer = tokeniser.getDataBuffer();
        Assert.assertNotNull(tempBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.getTagPending().tagName = "div";
        tokeniser.setLastStartTag(tokeniser.getTagPending());
        Assert.assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.createTagPending(true);
        tokeniser.getTagPending().tagName = "div";
        tokeniser.setLastStartTag(tokeniser.getTagPending());
        Assert.assertEquals("div", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testErrorByTokeniserState() {
        tokeniser.error(TokeniserState.TagName);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpected character 'null' in input state [TagName]",
                errors.get(0).getMessage());
    }

    @Test
    public void testEofErrorByTokeniserState() {
        tokeniser.eofError(TokeniserState.TagName);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpectedly reached end of file (EOF) in input state [TagName]",
                errors.get(0).getMessage());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("Error");
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Invalid character reference: Error", errors.get(0).getMessage());
    }

    @Test
    public void testErrorByString() {
        tokeniser.error("Some error");
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Some error", errors.get(0).getMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        Assert.assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}