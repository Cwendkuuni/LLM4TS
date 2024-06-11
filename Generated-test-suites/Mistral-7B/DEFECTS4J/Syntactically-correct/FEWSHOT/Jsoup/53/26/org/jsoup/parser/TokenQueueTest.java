package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
        
        queue.addFirst("A");
        assertFalse(queue.isEmpty());
        
        queue.consume();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("ABC");
        assertEquals('A', queue.peek());
        
        queue.advance();
        assertEquals('B', queue.peek());
        
        queue.advance();
        assertEquals('C', queue.peek());
        
        queue.consume();
        assertEquals('', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst("ABC");
        assertEquals("ABC", queue.toString());
        
        queue.addFirst("DEF");
        assertEquals("DEFABC", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("ABC");
        
        assertTrue(queue.matches("A"));
        assertTrue(queue.matches("BC"));
        assertTrue(queue.matches("ABC"));
        assertFalse(queue.matches("D"));
        
        queue.advance();
        assertTrue(queue.matches("B"));
        assertFalse(queue.matches("A"));
        
        queue.advance();
        assertTrue(queue.matches("C"));
        assertFalse(queue.matches("B"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABC");
        
        assertTrue(queue.matchesCS("A"));
        assertTrue(queue.matchesCS("C"));
        assertFalse(queue.matchesCS("B"));
        
        queue.advance();
        assertTrue(queue.matchesCS("B"));
        assertFalse(queue.matchesCS("A"));
        
        queue.advance();
        assertTrue(queue.matchesCS("C"));
        assertFalse(queue.matchesCS("B"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("ABC");
        
        assertTrue(queue.matchesAny("A", "B", "C"));
        
        queue.advance();
        assertTrue(queue.matchesAny("B", "C", "A"));
        
        queue.advance();
        assertTrue(queue.matchesAny("C", "A", "B"));
        
        queue.addFirst("D");
        assertTrue(queue.matchesAny("D", "A", "B", "C"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("ABC");
        
        assertTrue(queue.matchesAny(new char[]{'A', 'B', 'C'}));
        
        queue.advance();
        assertTrue(queue.matchesAny(new char[]{'B', 'C', 'A'}));
        
        queue.advance();
        assertTrue(queue.matchesAny(new char[]{'C', 'A', 'B'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<ABC");
        assertTrue(queue.matchesStartTag());
        
        queue.consume();
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("ABC");
        
        queue.addFirst("ABC");
        assertTrue(queue.matchChomp("ABC"));
        assertEquals("", queue.toString());

        queue.addFirst("DEF");
        queue.consume();
        queue.consume();
        queue.consume("ABC");
        
        assertTrue(queue.matchChomp("ABC"));
        assertEquals("DEF", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" ABC");
        assertTrue(queue.matchesWhitespace());
        
        queue.consumeWhitespace();
        assertTrue(queue.matchesWhitespace());
        
        queue.consume();
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("ABC");
        assertTrue(queue.matchesWord());
        
        queue.consume();
        assertTrue(queue.matchesWord());
        
        queue.consume();
        assertFalse(queue.matchesWord());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithMismatchedSequence() {
        TokenQueue queue = new TokenQueue("ABC");
        queue.consume("DEF");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("ABCDEF");
        
        String consumed = queue.consumeTo("AB");
        assertEquals("AB", consumed);
        assertEquals("CDEF", queue.toString());

        consumed = queue.consumeTo("CD");
        assertEquals("CD", consumed);
        assertEquals("EF", queue.toString());

        consumed = queue.consumeTo("EF");
        assertEquals("EF", consumed);
        assertEquals("", queue.toString());

        consumed = queue.consumeTo("G");
        assertEquals("", consumed);
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("AbCdEf");
        
        String consumed = queue.consumeToIgnoreCase("Ab");
        assertEquals("Ab", consumed);
        assertEquals("CdEf", queue.toString());

        consumed = queue.consumeToIgnoreCase("Cd");
        assertEquals("Cd", consumed);
        assertEquals("Ef", queue.toString());

        consumed = queue.consumeToIgnoreCase("Ef");
        assertEquals("Ef", consumed);
        assertEquals("", queue.toString());

        consumed = queue.consumeToIgnoreCase("G");
        assertEquals("", consumed);
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("ABCDEF");
        
        String consumed = queue.consumeToAny("AB", "CD");
        assertEquals("AB", consumed);
        assertEquals("CDEF", queue.toString());

        consumed = queue.consumeToAny("CD", "AB");
        assertEquals("CD", consumed);
        assertEquals("ABEF", queue.toString());

        consumed = queue.consumeToAny("EF");
        assertEquals("EF", consumed);
        assertEquals("", queue.toString());

        consumed = queue.consumeToAny("G");
        assertEquals("", consumed);
        assertEquals("", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("((())())");
        String balanced = queue.chompBalanced('(', ')');
        assertEquals("((())", queue.toString());
        assertEquals("()", balanced);

        queue.addFirst("(())");
        balanced = queue.chompBalanced('(', ')');
        assertEquals("()()", queue.toString());
        assertEquals("()", balanced);

        queue.addFirst("(())");
        balanced = queue.chompBalanced('(', '(');
        assertEquals("()()()", queue.toString());
        assertEquals("(())", balanced);

        queue.addFirst("(())");
        balanced = queue.chompBalanced('(', '[');
        assertEquals("(())[", queue.toString());
        assertEquals("", balanced);

        queue.addFirst("(())");
        queue.consume();
        balanced = queue.chompBalanced('(', '[');
        assertEquals("[", queue.toString());
        assertEquals("", balanced);
    }

    @Test
    public void testUnescape() {
        String escaped = "\\(\\)";
        String unescaped = TokenQueue.unescape(escaped);
        assertEquals("()", unescaped);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" ABC ");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.matchesWhitespace());
        
        queue.advance();
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("ABC");
        String word = queue.consumeWord();
        assertEquals("ABC", queue.toString());
        assertEquals("A", word);

        word = queue.consumeWord();
        assertEquals("BC", queue.toString());
        assertEquals("B", word);

        word = queue.consumeWord();
        assertEquals("C", queue.toString());
        assertEquals("", word);
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("tagName");
        String tagName = queue.consumeTagName();
        assertEquals("tagName", queue.toString());
        assertEquals("tagName", tagName);

        queue.addFirst("tagName2");
        tagName = queue.consumeTagName();
        assertEquals("tagName2", queue.toString());
        assertEquals("tagName2", tagName);
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("tagName:class");
        String elementSelector = queue.consumeElementSelector();
        assertEquals("tagName:class", queue.toString());
        assertEquals("tagName", elementSelector);

        queue.addFirst("tagName:class2");
        elementSelector = queue.consumeElementSelector();
        assertEquals("tagName:class2", queue.toString());
        assertEquals("tagName:class2", elementSelector);
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("classId");
        String cssIdentifier = queue.consumeCssIdentifier();
        assertEquals("classId", queue.toString());
        assertEquals("classId", cssIdentifier);

        queue.addFirst("classId2");
        cssIdentifier = queue.consumeCssIdentifier();
        assertEquals("classId2", queue.toString());
        assertEquals("classId2", cssIdentifier);
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("name=\"attrValue\"");
        String attributeKey = queue.consumeAttributeKey();
        assertEquals("name=\"attrValue\"", queue.toString());
        assertEquals("name", attributeKey);

        queue.addFirst("name2=\"attrValue2\"");
        attributeKey = queue.consumeAttributeKey();
        assertEquals("name2=\"attrValue2\"", queue.toString());
        assertEquals("name2", attributeKey);
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("ABC");
        String remainder = queue.remainder();
        assertEquals("ABC", queue.toString());
        assertEquals("C", remainder);

        queue.addFirst("DEF");
        remainder = queue.remainder();
        assertEquals("DEFABC", queue.toString());
        assertEquals("A", remainder);

        queue.consumeTo("AB");
        remainder = queue.remainder();
        assertEquals("CDEF", queue.toString());
        assertEquals("EF", remainder);
    }
}