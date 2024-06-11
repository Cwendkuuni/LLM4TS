package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Token.StartTag startTag = tokeniser.createTagPending(true);
        Token.EndTag endTag = tokeniser.createTagPending(false);

        assertEquals("startTag", startTag.name());
        assertEquals("endTag", endTag.name());
    }

    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Token.StartTag startTag = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        assertNull(tokeniser.tagPending);
    }

    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createCommentPending();

        assertEquals("", tokeniser.commentPending.data());
        assertTrue(tokeniser.commentPending.isBlank());
    }

    @Test
    public void testEmitCommentPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();

        assertNull(tokeniser.commentPending);
    }

    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createDoctypePending();

        assertSame(Token.Type.Doctype, tokeniser.doctypePending.type);
    }

    @Test
    public void testEmitDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();

        assertNull(tokeniser.doctypePending);
    }

    @Test
    public void testCreateTempBuffer() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createTempBuffer();

        assertEquals("", tokeniser.dataBuffer.toString());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.emitTagPending();
        tokeniser.transition(TokeniserState.EndTag);

        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagToken_empty() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.transition(TokeniserState.StartTag);

        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagToken_wrongTagName() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.emitTagPending();
        tokeniser.transition(TokeniserState.StartTag);

        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testError() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.error(TokeniserState.StartTag);

        assertNull(tokeniser.tagPending);
    }

    @Test
    public void testEofError() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.error(TokeniserState.EndOfInput);

        assertNull(tokeniser.tagPending);
    }

    @Test
    public void testCharacterReferenceError() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.error("character reference error");

        assertNull(tokeniser.tagPending);
    }

    @Test
    public void testUnescapeEntities() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        String input = "&amp; &lt; &gt; &apos; &quot; &#123; &#125;";
        String expected = "&amp; < > ' \" { }";

        String result = tokeniser.unescapeEntities(false);
        assertEquals(expected, result);
    }

    @Test
    public void testUnescapeEntities_inAttribute() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        String input = "&amp; class='&quot;red&quot;'";
        String expected = "&amp; class='\"red\"'";

        String result = tokeniser.unescapeEntities(true);
        assertEquals(expected, result);
    }

    @Test(expected = AssertionError.class)
    public void testUnexpectedCharacters() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.unescapeEntities(false);
    }
}