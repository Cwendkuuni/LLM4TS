package org.jsoup.parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    @Test
    public void testRead() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Token token = tokeniser.read();

        // Verify that the read method returns a Token object
        assertNotNull(token);
    }

    @Test
    public void testEmit() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Token token = new Token.Character("example");
        tokeniser.emit(token);

        // Verify that the emitted token is the same as the token passed to the emit method
        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testEmitOfString() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        String str = "example";
        tokeniser.emit(str);

        // Verify that the emitted token is a Character token with the same string value as the string passed to the emit method
        assertEquals(new Token.Character(str), tokeniser.read());
    }

    @Test
    public void testEmitOfChar() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        char c = 'a';
        tokeniser.emit(c);

        // Verify that the emitted token is a Character token with the same character value as the character passed to the emit method
        assertEquals(new Token.Character(Character.toString(c)), tokeniser.read());
    }

    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        // Verify that the emitted token is the same as the tag pending token
        assertEquals(tokeniser.tagPending, tokeniser.read());
    }

    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createCommentPending();

        // Verify that the comment pending token is not null
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();

        // Verify that the emitted token is the same as the comment pending token
        assertEquals(tokeniser.commentPending, tokeniser.read());
    }

    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createDoctypePending();

        // Verify that the doctype pending token is not null
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();

        // Verify that the emitted token is the same as the doctype pending token
        assertEquals(tokeniser.doctypePending, tokeniser.read());
    }
}