package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue tq = new TokenQueue("");
        assertTrue(tq.isEmpty());

        tq = new TokenQueue("abc");
        assertFalse(tq.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tq = new TokenQueue("abcd");
        assertEquals('a', tq.peek());
        tq.advance();
        assertEquals('b', tq.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tq = new TokenQueue("");
        tq.addFirst('a');
        tq.addFirst("abc");
        assertEquals("abc", tq.consumeToIgnoreCase("abc"));
        assertEquals("aabc", tq.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue tq = new TokenQueue("abcd");
        assertTrue(tq.matches("ab"));
        assertFalse(tq.matches("ef"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tq = new TokenQueue("abCD");
        assertTrue(tq.matchesCS("ab"));
        assertFalse(tq.matchesCS("ef"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tq = new TokenQueue("abcd");
        assertTrue(tq.matchesAny("ab", "cd"));
        assertFalse(tq.matchesAny("ef", "xy"));
        assertTrue(tq.matchesAny("ef", "ef"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue tq = new TokenQueue("abcd");
        assertTrue(tq.matchesAny('a', 'd'));
        assertFalse(tq.matchesAny('e', 'x'));
        assertTrue(tq.matchesAny('e', 'e'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue tq = new TokenQueue("<abcd>");
        assertTrue(tq.matchesStartTag());
        assertFalse(tq.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tq = new TokenQueue("ab<cd>ef");
        assertTrue(tq.matchChomp("ab"));
        assertEquals("cdef", tq.toString());
        assertTrue(tq.matchChomp("cd"));
        assertEquals("ef", tq.toString());
        assertTrue(tq.matchChomp("ef"));
        assertEquals("", tq.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tq = new TokenQueue("   abcd   ");
        assertTrue(tq.matchesWhitespace());
        assertTrue(tq.matchesWhitespace());
        assertFalse(tq.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue tq = new TokenQueue("abcdef");
        assertTrue(tq.matchesWord());
        assertTrue(tq.matchesWord());
        assertFalse(tq.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue tq = new TokenQueue("abcd");
        tq.advance();
        assertEquals('b', tq.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue tq = new TokenQueue("abcd");
        assertEquals('a', tq.consume());
        assertEquals('b', tq.consume());
        assertEquals('c', tq.consume());
        assertEquals('d', tq.consume());
        assertEquals('', tq.toString());
    }

    @Test
    public void testConsumeString() {
        TokenQueue tq = new TokenQueue("abcd");
        tq.consume("ab");
        assertEquals("cd", tq.toString());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue tq = new TokenQueue("abcd");
        String consumed = tq.consumeTo("bc");
        assertEquals("ab", consumed);
        assertEquals("cd", tq.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tq = new TokenQueue("abCDef");
        String consumed = tq.consumeToIgnoreCase("bc");
        assertEquals("abCD", consumed);
        assertEquals("ef", tq.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue tq = new TokenQueue("abcd");
        String consumed = tq.consumeToAny("ef", "gh");
        assertEquals("abcd", consumed);
        assertEquals("", tq.toString());
    }

    @Test
    public void testConsumeToAnyChomp() {
        TokenQueue tq = new TokenQueue("ab<cd>ef");
        String consumed = tq.consumeToAny("cd", "ef");
        assertEquals("ab", consumed);
        assertEquals("", tq.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tq = new TokenQueue("(ab)cd(ef)");
        String consumed = tq.chompBalanced('(', ')');
        assertEquals("abcd", consumed);
        assertEquals("ef", tq.toString());
    }

    @Test
    public void testChompBalancedEmpty() {
        TokenQueue tq = new TokenQueue("");
        String consumed = tq.chompBalanced('(', ')');
        assertEquals("", consumed);
        assertEquals("", tq.toString());
    }

    @Test
    public void testChompBalancedMismatched() {
        TokenQueue tq = new TokenQueue("(ab)cd)");
        String consumed = tq.chompBalanced('(', ')');
        assertEquals("abcd", consumed);
        assertEquals("", tq.toString());
    }

    @Test
    public void testChompBalancedQuoted() {
        TokenQueue tq = new TokenQueue("(ab)'cd)");
        String consumed = tq.chompBalanced('(', ')');
        assertEquals("abcd", consumed);
        assertEquals("", tq.toString());
    }

    @Test
    public void testChompBalancedEscaped() {
        TokenQueue tq = new TokenQueue("(ab)\\cd)");
        String consumed = tq.chompBalanced('(', ')');
        assertEquals("abcd", consumed);
        assertEquals("", tq.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("abc\\def\\g\\h", TokenQueue.unescape("abc\\def\\g\\h"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tq = new TokenQueue(" abc def ");
        assertTrue(tq.consumeWhitespace());
        assertTrue(tq.consumeWhitespace());
        assertFalse(tq.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tq = new TokenQueue("abcdef");
        String consumed = tq.consumeWord();
        assertEquals("abc", consumed);
        assertEquals("def", tq.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tq = new TokenQueue(" <abcd> ");
        String consumed = tq.consumeTagName();
        assertEquals("abcd", consumed);
        assertEquals("", tq.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tq = new TokenQueue(" <ab:cd> ");
        String consumed = tq.consumeElementSelector();
        assertEquals("ab:cd", consumed);
        assertEquals("", tq.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tq = new TokenQueue(" id:abc def id:xyz ");
        String consumed = tq.consumeCssIdentifier();
        assertEquals("abc", consumed);
        assertEquals("def id:xyz", tq.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tq = new TokenQueue(" id:abc class:def id:xyz ");
        String consumed = tq.consumeAttributeKey();
        assertEquals("id", consumed);
        assertEquals("abc class:def id:xyz", tq.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue tq = new TokenQueue("abcd");
        String remainder = tq.remainder();
        assertEquals("d", remainder);
        assertEquals("ab", tq.toString());
    }

    @Test
    public void testToString() {
        TokenQueue tq = new TokenQueue("abcd");
        assertEquals("abcd", tq.toString());
    }
}