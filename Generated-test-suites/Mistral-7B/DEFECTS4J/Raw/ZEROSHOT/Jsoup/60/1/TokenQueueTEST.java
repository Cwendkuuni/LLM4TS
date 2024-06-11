package org.jsoup.parser.test;

import static org.junit.Assert.*;

import org.jsoup.parser.TokenQueue;

import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testTokenQueue_constructor_withValidData() {
        String data = "abcdefg";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals(data, tokenQueue.queue);
        assertEquals(0, tokenQueue.pos);
        assertEquals(7, tokenQueue.remainingLength());
    }

    @Test(expected = NullPointerException.class)
    public void testTokenQueue_constructor_withNullData() {
        TokenQueue tokenQueue = new TokenQueue(null);
    }

    @Test
    public void testIsEmpty_whenQueueIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testIsEmpty_whenQueueIsNotEmpty() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek_whenQueueIsNotEmpty() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertEquals('a', tokenQueue.peek());
    }

    @Test
    public void testPeek_whenQueueIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertEquals(0, tokenQueue.peek());
    }

    @Test
    public void testAddFirst_withCharacter() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        tokenQueue.addFirst('x');
        assertEquals("xabc", tokenQueue.queue);
        assertEquals(0, tokenQueue.pos);
        assertEquals(5, tokenQueue.remainingLength());
    }

    @Test
    public void testAddFirst_withString() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        tokenQueue.addFirst("xyz");
        assertEquals("xyzabc", tokenQueue.queue);
        assertEquals(0, tokenQueue.pos);
        assertEquals(7, tokenQueue.remainingLength());
    }

    @Test
    public void testMatches_whenSequenceMatches() {
        TokenQueue tokenQueue = new TokenQueue("abcdefg");
        assertTrue(tokenQueue.matches("ef"));
    }

    @Test
    public void testMatches_whenSequenceDoesNotMatch() {
        TokenQueue tokenQueue = new TokenQueue("abcdefg");
        assertFalse(tokenQueue.matches("hij"));
    }

    @Test
    public void testMatchesCS_whenSequenceMatchesCaseSensitive() {
        TokenQueue tokenQueue = new TokenQueue("ABCDEFG");
        assertTrue(tokenQueue.matchesCS("EF"));
    }

    @Test
    public void testMatchesCS_whenSequenceDoesNotMatchCaseSensitive() {
        TokenQueue tokenQueue = new TokenQueue("ABCDEFG");
        assertFalse(tokenQueue.matchesCS("HIJ"));
    }

    @Test
    public void testMatchesAny_whenAnySequenceMatches() {
        TokenQueue tokenQueue = new TokenQueue("abcdefg");
        assertTrue(tokenQueue.matchesAny("ef", "cd"));
    }

    @Test
    public void testMatchesAny_whenNoneOfTheSequencesMatches() {
        TokenQueue tokenQueue = new TokenQueue("abcdefg");
        assertFalse(tokenQueue.matchesAny("hij", "klm"));
    }

    @Test
    public void testMatchesAny_whenSingleSequenceMatches() {
        TokenQueue tokenQueue = new TokenQueue("abcdefg");
        assertTrue(tokenQueue.matchesAny("ef"));
    }

    @Test
    public void testMatchesStartTag_whenQueueStartsWithTag() {
        TokenQueue tokenQueue = new TokenQueue("<tag attr=\"value\">");
        assertTrue(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchesStartTag_whenQueueDoesNotStartsWithTag() {
        TokenQueue tokenQueue = new TokenQueue("abcdefg");
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp_whenSequenceMatches() {
        TokenQueue tokenQueue = new TokenQueue("ef<tag attr=\"value\">");
        tokenQueue.matchChomp("ef");
        assertEquals("tag attr=\"value\">", tokenQueue.queue);
        assertEquals(3, tokenQueue.pos);
        assertEquals(11, tokenQueue.remainingLength());
    }

    @Test
    public void testMatchChomp_whenSequenceDoesNotMatch() {
        TokenQueue tokenQueue = new TokenQueue("abcdefg");
        assertFalse(tokenQueue.matchChomp("hij"));
    }

    @Test
    public void testMatchesWhitespace_whenQueueStartsWithWhitespace() {
        TokenQueue tokenQueue = new TokenQueue(" \t\n abc");
        assertTrue(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWhitespace_whenQueueDoesNotStartWithWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord_whenQueueStartsWithWordCharacter() {
        TokenQueue tokenQueue = new TokenQueue("a_1B");
        assertTrue(tokenQueue.matchesWord());
    }

    @Test
    public void testMatchesWord_whenQueueDoesNotStartWithWordCharacter() {
        TokenQueue tokenQueue = new TokenQueue("1a_B");
        assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance_whenQueueIsNotEmpty() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        tokenQueue.advance();
        assertEquals('b', tokenQueue.queue.charAt(tokenQueue.pos));
        assertEquals(1, tokenQueue.pos);
        assertEquals(6, tokenQueue.remainingLength());
    }

    @Test
    public void testAdvance_whenQueueIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        tokenQueue.advance();
        assertEquals(0, tokenQueue.pos);
        assertEquals(0, tokenQueue.remainingLength());
    }

    @Test
    public void testConsume_whenQueueIsNotEmpty() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertEquals('a', tokenQueue.consume());
        assertEquals(1, tokenQueue.pos);
        assertEquals(6, tokenQueue.remainingLength());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsume_whenQueueIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        tokenQueue.consume();
    }

    @Test
    public void testConsume_withSequence_whenQueueStartsWithSequence() {
        TokenQueue tokenQueue = new TokenQueue("ef<tag attr=\"value\">");
        String consumed = tokenQueue.consume("ef");
        assertEquals("tag attr=\"value\">", tokenQueue.queue);
        assertEquals(3, tokenQueue.pos);
        assertEquals(11, tokenQueue.remainingLength());
        assertEquals("ef", consumed);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsume_withSequence_whenQueueDoesNotStartsWithSequence() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        tokenQueue.consume("ef");
    }

    @Test
    public void testConsumeTo_whenSequenceIsFound() {
        TokenQueue tokenQueue = new TokenQueue("ef<tag attr=\"value\">abc");
        String consumed = tokenQueue.consumeTo("ef");
        assertEquals("ef<tag attr=\"value\">", tokenQueue.queue);
        assertEquals(3, tokenQueue.pos);
        assertEquals(18, tokenQueue.remainingLength());
        assertEquals("ef", consumed);
    }

    @Test
    public void testConsumeTo_whenSequenceIsNotFound() {
        TokenQueue tokenQueue = new TokenQueue("abcdefg");
        String consumed = tokenQueue.consumeTo("hij");
        assertEquals("abcdefg", tokenQueue.queue);
        assertEquals(0, tokenQueue.pos);
        assertEquals(7, tokenQueue.remainingLength());
        assertEquals("", consumed);
    }

    @Test
    public void testConsumeToIgnoreCase_whenSequenceIsFound() {
        TokenQueue tokenQueue = new TokenQueue("EF<TAG attr=\"VALUE\">ABC");
        String consumed = tokenQueue.consumeToIgnoreCase("ef");
        assertEquals("EF<TAG attr=\"VALUE\">", tokenQueue.queue);
        assertEquals(3, tokenQueue.pos);
        assertEquals(11, tokenQueue.remainingLength());
        assertEquals("ef", consumed);
    }

    @Test
    public void testConsumeToIgnoreCase_whenSequenceIsNotFound() {
        TokenQueue tokenQueue = new TokenQueue("abcdefg");
        String consumed = tokenQueue.consumeToIgnoreCase("hij");
        assertEquals("abcdefg", tokenQueue.queue);
        assertEquals(0, tokenQueue.pos);
        assertEquals(7, tokenQueue.remainingLength());
        assertEquals("", consumed);
    }

    @Test
    public void testConsumeToAny_whenSequenceIsFound() {
        TokenQueue tokenQueue = new TokenQueue("ef<tag attr=\"value\">xyz");
        String consumed = tokenQueue.consumeToAny("ef", "xy");
        assertEquals("ef<tag attr=\"value\">", tokenQueue.queue);
        assertEquals(3, tokenQueue.pos);
        assertEquals(14, tokenQueue.remainingLength());
        assertEquals("ef", consumed);
    }

    @Test
    public void testConsumeToAny_whenNoneOfTheSequencesIsFound() {
        TokenQueue tokenQueue = new TokenQueue("abcdefg");
        String consumed = tokenQueue.consumeToAny("hij", "klm");
        assertEquals("abcdefg", tokenQueue.queue);
        assertEquals(0, tokenQueue.pos);
        assertEquals(7, tokenQueue.remainingLength());
        assertEquals("", consumed);
    }

    @Test
    public void testConsumeToAny_whenSingleSequenceIsFound() {
        TokenQueue tokenQueue = new TokenQueue("ef<tag attr=\"value\">xyz");
        String consumed = tokenQueue.consumeToAny("ef");
        assertEquals("ef<tag attr=\"value\">", tokenQueue.queue);
        assertEquals(3, tokenQueue.pos);
        assertEquals(14, tokenQueue.remainingLength());
        assertEquals("ef", consumed);
    }

    @Test
    public void testChompBalanced_whenBalancedStringIsFound() {
        TokenQueue tokenQueue = new TokenQueue("(abc)def(ghi)ijk");
        String consumed = tokenQueue.chompBalanced('(', ')');
        assertEquals("(abc)def(ghi)", tokenQueue.queue);
        assertEquals(11, tokenQueue.pos);
        assertEquals(12, tokenQueue.remainingLength());
        assertEquals("(abc)", consumed);
    }

    @Test
    public void testChompBalanced_whenUnbalancedStringIsFound() {
        TokenQueue tokenQueue = new TokenQueue("(abc)def(ghi)ijk");
        String consumed = tokenQueue.chompBalanced('(', 'k');
        assertEquals("(abc)def(ghi)ijk", tokenQueue.queue);
        assertEquals(0, tokenQueue.pos);
        assertEquals(17, tokenQueue.remainingLength());
        assertEquals("", consumed);
    }

    @Test
    public void testChompBalanced_whenStringContainsQuotesOrEscapes() {
        TokenQueue tokenQueue = new TokenQueue("(\"ab\")cdef(ghi)\\(ijk)l");
        String consumed = tokenQueue.chompBalanced('(', ')');
        assertEquals("(\"ab\")cdef", tokenQueue.queue);
        assertEquals(7, tokenQueue.pos);
        assertEquals(12, tokenQueue.remainingLength());
        assertEquals("(\"ab\"", consumed);
    }

    @Test
    public void testUnescape_whenStringContainsEscapes() {
        TokenQueue tokenQueue = new TokenQueue("\\\\/\\\"\\\\\\\\");
        String unescaped = TokenQueue.unescape(tokenQueue.queue);
        assertEquals("/\"\\\\", unescaped);
    }

    @Test
    public void testConsumeWhitespace_whenQueueContainsWhitespace() {
        TokenQueue tokenQueue = new TokenQueue(" \t\n abc");
        assertTrue(tokenQueue.consumeWhitespace());
        assertEquals("abc", tokenQueue.queue);
        assertEquals(3, tokenQueue.pos);
        assertEquals(3, tokenQueue.remainingLength());
    }

    @Test
    public void testConsumeWhitespace_whenQueueDoesNotContainWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertFalse(tokenQueue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord_whenQueueContainsWordCharacter() {
        TokenQueue tokenQueue = new TokenQueue("_1aB");
        String consumed = tokenQueue.consumeWord();
        assertEquals("_1aB", tokenQueue.queue);
        assertEquals(1, tokenQueue.pos);
        assertEquals(7, tokenQueue.remainingLength());
        assertEquals("aB", consumed);
    }

    @Test
    public void testConsumeWord_whenQueueDoesNotContainWordCharacter() {
        TokenQueue tokenQueue = new TokenQueue("1a_B");
        String consumed = tokenQueue.consumeWord();
        assertEquals("1a_B", tokenQueue.queue);
        assertEquals(0, tokenQueue.pos);
        assertEquals(7, tokenQueue.remainingLength());
        assertEquals("", consumed);
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("abcd<TAG attr=\"value\">efg");
        String consumed = tokenQueue.consumeTagName();
        assertEquals("abcd<TAG attr=\"value\">efg", tokenQueue.queue);
        assertEquals(1, tokenQueue.pos);
        assertEquals(22, tokenQueue.remainingLength());
        assertEquals("TAG", consumed);
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("abcd<TAG attr=\"value\" class=\"class1 class2\" id=\"id1\">efg");
        String consumed = tokenQueue.consumeElementSelector();
        assertEquals("abcd<TAG attr=\"value\" class=\"class1 class2\" id=\"id1\">efg", tokenQueue.queue);
        assertEquals(1, tokenQueue.pos);
        assertEquals(31, tokenQueue.remainingLength());
        assertEquals("TAG", consumed);
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tokenQueue = new TokenQueue("abcd<TAG attr=\"class:class1 class:class2 id:id1\" id:id2>efg");
        String consumed = tokenQueue.consumeCssIdentifier();
        assertEquals("abcd<TAG attr=\"class:class1 class:class2 id:id1\" id:id2>efg", tokenQueue.queue);
        assertEquals(1, tokenQueue.pos);
        assertEquals(33, tokenQueue.remainingLength());
        assertEquals("class1", consumed);
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tokenQueue = new TokenQueue("abcd<TAG attr=\"class:class1 class:class2 id:id1 attr:attr1 attr:attr2\" >efg");
        String consumed = tokenQueue.consumeAttributeKey();
        assertEquals("abcd<TAG attr=\"class:class1 class:class2 id:id1 attr:attr1 attr:attr2\" >efg", tokenQueue.queue);
        assertEquals(1, tokenQueue.pos);
        assertEquals(40, tokenQueue.remainingLength());
        assertEquals("attr", consumed);
    }

    @Test
    public void testRemainder() {
        TokenQueue tokenQueue = new TokenQueue("abcdefg");
        String remainder = tokenQueue.remainder();
        assertEquals("abcdefg", tokenQueue.queue);
        assertEquals(7, tokenQueue.pos);
        assertEquals(0, tokenQueue.remainingLength());
        assertEquals("defg", remainder);
    }

    @Test
    public void testToString() {
        TokenQueue tokenQueue = new TokenQueue("abcdefg");
        assertEquals("defg", tokenQueue.toString());
    }
}