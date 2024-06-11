package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

    @Test
    public void testRead_method() {
        // Test typical use case
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("html"));
        Token token = tokeniser.read();
        assertEquals("html", token.getValue());

        // Test edge case with empty input
        Tokeniser tokeniser2 = new Tokeniser(new CharacterReader(""));
        Token token2 = tokeniser2.read();
        assertNull(token2);

        // Test error scenario
        Tokeniser tokeniser3 = new Tokeniser(new CharacterReader("<div>"));
        Token token3 = tokeniser3.read();
        assertTrue(token3 instanceof Token.Character);
        assertEquals("<div>", token3.getValue());
    }

    @Test
    public void testEmit_method() {
        // Test typical use case with StartTag token
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        Token.StartTag startTagToken = new Token.StartTag();
        startTagToken.setTagName("div");
        tokeniser.emit(startTagToken);
        Token emittedToken = tokeniser.read();
        assertEquals("div", emittedToken.getValue());

        // Test typical use case with EndTag token
        Tokeniser tokeniser2 = new Tokeniser(new CharacterReader(""));
        Token.EndTag endTagToken = new Token.EndTag();
        endTagToken.setTagName("div");
        tokeniser2.emit(endTagToken);
        Token emittedToken2 = tokeniser2.read();
        assertEquals("div", emittedToken2.getValue());

        // Test error scenario
        Tokeniser tokeniser3 = new Tokeniser(new CharacterReader(""));
        Token.Comment commentToken = new Token.Comment();
        commentToken.setValue("This is a comment");
        tokeniser3.emit(commentToken);
        Token emittedToken3 = tokeniser3.read();
        assertNull(emittedToken3);
    }

    @Test
    public void testCreateTagPending_method() {
        // Test typical use case for StartTag
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        Token.Tag tagPending = tokeniser.createTagPending(true);
        assertTrue(tagPending instanceof Token.StartTag);

        // Test typical use case for EndTag
        Tokeniser tokeniser2 = new Tokeniser(new CharacterReader(""));
        Token.Tag tagPending2 = tokeniser2.createTagPending(false);
        assertTrue(tagPending2 instanceof Token.EndTag);

        // Test edge case for StartTag
        Tokeniser tokeniser3 = new Tokeniser(new CharacterReader(""));
        tokeniser3.createTagPending(true);
        assertNull(tokeniser3.tagPending);

        // Test edge case for EndTag
        Tokeniser tokeniser4 = new Tokeniser(new CharacterReader(""));
        tokeniser4.createTagPending(false);
        assertNull(tokeniser4.tagPending);
    }

    @Test
    public void testIsAppropriateEndTagToken_method() {
        // Test typical use case with matching tag names
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        Token.StartTag startTag = new Token.StartTag();
        startTag.setTagName("div");
        tokeniser.tagPending = startTag;
        tokeniser.lastStartTag = startTag;
        assertTrue(tokeniser.isAppropriateEndTagToken());

        // Test typical use case with different tag names
        Tokeniser tokeniser2 = new Tokeniser(new CharacterReader(""));
        Token.StartTag startTag2 = new Token.StartTag();
        startTag2.setTagName("div");
        tokeniser2.tagPending = startTag2;
        Token.StartTag lastStartTag2 = new Token.StartTag();
        lastStartTag2.setTagName("p");
        tokeniser2.lastStartTag = lastStartTag2;
        assertFalse(tokeniser2.isAppropriateEndTagToken());

        // Test edge case with null tag names
        Tokeniser tokeniser3 = new Tokeniser(new CharacterReader(""));
        tokeniser3.tagPending = null;
        tokeniser3.lastStartTag = null;
        assertFalse(tokeniser3.isAppropriateEndTagToken());
    }

    @Test
    public void testError_method() {
        // Test typical use case
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.error(TokeniserState.Data);
        assertEquals("Unexpected character in input", tokeniser.errors.get(0).getErrorMessage());

        // Test edge case
        Tokeniser tokeniser2 = new Tokeniser(new CharacterReader(""));
        tokeniser2.setTrackErrors(false);
        tokeniser2.error(TokeniserState.Data);
        assertTrue(tokeniser2.errors.isEmpty());
    }
}