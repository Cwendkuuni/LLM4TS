package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
        Token token = new Token.Character("test");

        tokeniser.emit(token);

        Token emitted = tokeniser.read();
        Assert.assertEquals(token, emitted);
    }

    @Test
    public void testEmitString() {
        String str = "test";

        tokeniser.emit(str);

        Token emitted = tokeniser.read();
        Assert.assertEquals(str, ((Token.Character) emitted).getData());
    }

    @Test
    public void testEmitCharArray() {
        char[] chars = "test".toCharArray();

        tokeniser.emit(chars);

        Token emitted = tokeniser.read();
        Assert.assertEquals(new String(chars), ((Token.Character) emitted).getData());
    }

    @Test
    public void testEmitCodepoints() {
        int[] codepoints = "test".codePoints().toArray();

        tokeniser.emit(codepoints);

        Token emitted = tokeniser.read();
        Assert.assertEquals(new String(codepoints, 0, codepoints.length), ((Token.Character) emitted).getData());
    }

    @Test
    public void testEmitChar() {
        char c = 't';

        tokeniser.emit(c);

        Token emitted = tokeniser.read();
        Assert.assertEquals(Character.toString(c), ((Token.Character) emitted).getData());
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);

        tokeniser.emitTagPending();

        Token emitted = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, emitted.type);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();

        Token pending = tokeniser.commentPending;
        Assert.assertNotNull(pending);
        Assert.assertEquals(Token.TokenType.Comment, pending.type);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();

        tokeniser.emitCommentPending();

        Token emitted = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Comment, emitted.type);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();

        Token pending = tokeniser.doctypePending;
        Assert.assertNotNull(pending);
        Assert.assertEquals(Token.TokenType.Doctype, pending.type);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();

        tokeniser.emitDoctypePending();

        Token emitted = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Doctype, emitted.type);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();

        StringBuilder buffer = tokeniser.dataBuffer;
        Assert.assertEquals(0, buffer.length());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.lastStartTag = "tag";

        Assert.assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagTokenWithoutStartTag() {
        Assert.assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = "tag";

        Assert.assertEquals(tokeniser.lastStartTag, tokeniser.appropriateEndTagName());
    }

    @Test
    public void testAppropriateEndTagNameWithoutStartTag() {
        Assert.assertNull(tokeniser.appropriateEndTagName());
    }

    @Test
    public void testErrorTokeniserState() {
        TokeniserState state = TokeniserState.Data;

        tokeniser.error(state);

        Assert.assertFalse(tokeniser.errors.isEmpty());
        Assert.assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testEofErrorTokeniserState() {
        TokeniserState state = TokeniserState.Data;

        tokeniser.eofError(state);

        Assert.assertFalse(tokeniser.errors.isEmpty());
        Assert.assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "Invalid character reference";

        tokeniser.characterReferenceError(message);

        Assert.assertFalse(tokeniser.errors.isEmpty());
        Assert.assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testError() {
        String errorMsg = "Error message";

        tokeniser.error(errorMsg);

        Assert.assertFalse(tokeniser.errors.isEmpty());
        Assert.assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        Assert.assertTrue(tokeniser.currentNodeInHtmlNS());
    }

    @Test
    public void testUnescapeEntities() {
        String input = "test&amp;123&lt;";

        String unescaped = tokeniser.unescapeEntities(false);

        Assert.assertEquals("test&123<", unescaped);
    }
}