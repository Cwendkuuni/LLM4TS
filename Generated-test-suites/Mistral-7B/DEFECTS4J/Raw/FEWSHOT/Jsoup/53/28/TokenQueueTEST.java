package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.StringUtil;

import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testEmptyTokenQueue() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testNonEmptyTokenQueue() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeekChar() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        assertEquals('a', tokenQueue.peek());
        tokenQueue.advance();
        assertEquals('b', tokenQueue.peek());
    }

    @Test
    public void testMatchesEmpty() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        assertFalse(tokenQueue.matches(""));
    }

    @Test
    public void testMatchesSingleCharacter() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        tokenQueue.advance();
        assertTrue(tokenQueue.matches("b"));
    }

    @Test
    public void testMatchesMultipleCharacters() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        tokenQueue.advance();
        tokenQueue.advance();
        assertTrue(tokenQueue.matches("ab"));
    }

    @Test
    public void testMatchesCaseSensitive() {
        TokenQueue tokenQueue = new TokenQueue("ABCDEF");
        tokenQueue.advance();
        tokenQueue.advance();
        assertFalse(tokenQueue.matchesCS("ab"));
        assertTrue(tokenQueue.matchesCS("AB"));
    }

    @Test
    public void testMatchesAnyEmpty() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        assertFalse(tokenQueue.matchesAny(new String[]{}));
    }

    @Test
    public void testMatchesAnySingleCharacter() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        tokenQueue.advance();
        assertTrue(tokenQueue.matchesAny(new String[]{"a"}));
    }

    @Test
    public void testMatchesAnyMultipleCharacters() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        tokenQueue.advance();
        tokenQueue.advance();
        assertTrue(tokenQueue.matchesAny(new String[]{"ab", "ba"}));
    }

    @Test
    public void testMatchesAnySingleCharacterEscaped() {
        TokenQueue tokenQueue = new TokenQueue("abc\\def");
        tokenQueue.advance();
        assertTrue(tokenQueue.matchesAny(new char[]{'\\'}));
    }

    @Test
    public void testMatchesAnyMultipleCharacters() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        tokenQueue.advance();
        tokenQueue.advance();
        assertTrue(tokenQueue.matchesAny(new char[]{'a', 'b'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("<tag>content");
        assertTrue(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchesStartTagWithEmptyContent() {
        TokenQueue tokenQueue = new TokenQueue("<tag />");
        assertTrue(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchesStartTagWithWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("<tag>\t\n content");
        assertTrue(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChompEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertFalse(tokenQueue.matchChomp(""));
    }

    @Test
    public void testMatchChompSingleCharacter() {
        TokenQueue tokenQueue = new TokenQueue("a");
        assertTrue(tokenQueue.matchChomp("a"));
        assertEquals(0, tokenQueue.pos);
    }

    @Test
    public void testMatchChompMultipleCharacters() {
        TokenQueue tokenQueue = new TokenQueue("abcd");
        assertTrue(tokenQueue.matchChomp("ab"));
        assertEquals("cd", tokenQueue.toString());
    }

    @Test
    public void testMatchChompWithWhitespace() {
        TokenQueue tokenQueue = new TokenQueue(" \t\n<tag>\t\n content");
        assertTrue(tokenQueue.matchChomp("<tag>"));
        assertEquals(" \t\n content", tokenQueue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("\t\n\r\f ");
        assertTrue(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue tokenQueue = new TokenQueue("word letter digit");
        assertTrue(tokenQueue.matchesWord());
    }

    @Test
    public void testMatchesWordWithDigit() {
        TokenQueue tokenQueue = new TokenQueue("word digit");
        assertTrue(tokenQueue.matchesWord());
    }

    @Test
    public void testMatchesWordWithDigitEscaped() {
        TokenQueue tokenQueue = new TokenQueue("word \\\\d");
        assertTrue(tokenQueue.matchesWord());
    }

    @Test
    public void testMatchChompWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("\t\n\r\f ");
        assertFalse(tokenQueue.matchChomp(""));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("\t\n\r\f ");
        assertTrue(tokenQueue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue("word letter digit");
        String consumed = tokenQueue.consumeWord();
        assertEquals("word", consumed);
        assertEquals("letter digit", tokenQueue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("tagName tagName:tagName tagName-tagName");
        String consumed = tokenQueue.consumeTagName();
        assertEquals("tagName", consumed);
        assertEquals("tagName:tagName tagName-tagName", tokenQueue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("tagName|tagName tagName|_tagName tagName-tagName");
        String consumed = tokenQueue.consumeElementSelector();
        assertEquals("tagName", consumed);
        assertEquals("tagName|_tagName", tokenQueue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tokenQueue = new TokenQueue("ID ID-class ID_class");
        String consumed = tokenQueue.consumeCssIdentifier();
        assertEquals("ID", consumed);
        assertEquals("ID-class ID_class", tokenQueue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tokenQueue = new TokenQueue("attributeKey attributeKey:attributeKey attributeKey-");
        String consumed = tokenQueue.consumeAttributeKey();
        assertEquals("attributeKey", consumed);
        assertEquals("attributeKey:attributeKey attributeKey-", tokenQueue.toString());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue tokenQueue = new TokenQueue("abcdabcdabcd");
        String consumed = tokenQueue.consumeTo("ab");
        assertEquals("abcd", consumed);
        assertEquals("cdabcd", tokenQueue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("AbCdAbCdAbCd");
        String consumed = tokenQueue.consumeToIgnoreCase("Ab");
        assertEquals("AbCd", consumed);
        assertEquals("cdAbCd", tokenQueue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue tokenQueue = new TokenQueue("abcdabcdabcd");
        String consumed = tokenQueue.consumeToAny(new String[]{"a", "b"});
        assertEquals("abcd", consumed);
        assertEquals("cd", tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(ab)(cd)ef");
        String consumed = tokenQueue.chompBalanced('(', ')');
        assertEquals("ab(cd)ef", tokenQueue.toString());
        assertEquals("(ab)", consumed);
        consumed = tokenQueue.chompBalanced('(', ')');
        assertEquals("cdef", tokenQueue.toString());
        assertEquals("(ab)", consumed);
    }

    @Test
    public void testChompBalancedWithEscapedCharacters() {
        TokenQueue tokenQueue = new TokenQueue("(\\(ab\\)\\)cd)ef");
        String consumed = tokenQueue.chompBalanced('(', ')');
        assertEquals("(\\(ab\\)\\)cd)ef", tokenQueue.toString());
        assertEquals("(\\(ab\\)\\)", consumed);
        consumed = tokenQueue.chompBalanced('(', ')');
        assertEquals("cd)ef", tokenQueue.toString());
        assertEquals("(\\(ab\\)\\)", consumed);
    }

    @Test
    public void testChompBalancedWithUnmatchedParentheses() {
        TokenQueue tokenQueue = new TokenQueue("(ab)(cd)ef");
        String consumed = tokenQueue.chompBalanced('(', '}');
        assertEquals("(ab)(cd)ef", tokenQueue.toString());
        assertNull(consumed);
    }

    @Test
    public void testUnescape() {
        String escaped = "\\abc\\def\\\\";
        String unescaped = TokenQueue.unescape(escaped);
        assertEquals("abcdef\\", unescaped);
    }
}