package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
        
        queue.addFirst("a");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.peek());
        
        queue.advance();
        assertEquals('b', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('a');
        queue.addFirst('b');
        queue.addFirst("abc");
        
        assertEquals("abc", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdef");
        
        assertTrue(queue.matches("ab"));
        assertTrue(queue.matches("bc"));
        assertTrue(queue.matches("cd"));
        assertFalse(queue.matches("ef"));
        assertFalse(queue.matches("xyz"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCDEF");
        
        assertTrue(queue.matchesCS("ab"));
        assertTrue(queue.matchesCS("bc"));
        assertTrue(queue.matchesCS("cd"));
        assertFalse(queue.matchesCS("ef"));
        assertFalse(queue.matchesCS("xyz"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcdef");
        
        assertTrue(queue.matchesAny("ab", "cd"));
        assertFalse(queue.matchesAny("xyz", "ab"));
    }

    @Test
    public void testMatchesAnyWithSingleCharacter() {
        TokenQueue queue = new TokenQueue("abcdef");
        
        assertTrue(queue.matchesAny('a', 'b', 'c', 'd', 'e', 'f'));
        assertFalse(queue.matchesAny('g', 'a'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<x>");
        assertTrue(queue.matchesStartTag());
        
        queue.advance();
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcdef");
        
        assertTrue(queue.matchChomp("ab"));
        assertEquals("cdef", queue.toString());
        
        assertTrue(queue.matchChomp("cd"));
        assertEquals("ef", queue.toString());
        
        assertFalse(queue.matchChomp("xyz"));
        assertEquals("ef", queue.toString());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abcdef");
        
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertEquals('d', queue.consume());
        assertEquals('e', queue.consume());
        assertEquals('f', queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abcdef");
        
        assertEquals("abc", queue.consumeTo("bc"));
        assertEquals("def", queue.toString());
        
        assertEquals("def", queue.consumeTo("xyz"));
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("ABCDef");
        
        assertEquals("abC", queue.consumeToIgnoreCase("AbC"));
        assertEquals("Def", queue.toString());
        
        assertEquals("Def", queue.consumeToIgnoreCase("xyz"));
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abcdef");
        
        assertEquals("abcd", queue.consumeToAny("ef", "ab"));
        assertEquals("", queue.toString());
        
        assertEquals("def", queue.consumeToAny("xyz", "ef"));
        assertEquals("", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("((())())");
        
        assertEquals("((())", queue.chompBalanced('(', ')'));
        assertEquals("()()", queue.toString());
        
        assertEquals("(())", queue.chompBalanced('(', ')'));
        assertEquals("()", queue.toString());
        
        assertEquals("()", queue.chompBalanced('(', ')'));
        assertEquals("", queue.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("\\abc \\def \\\\\\", TokenQueue.unescape("\\abc \\def \\\\\\\\"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   abc  def   ");
        assertTrue(queue.consumeWhitespace());
        assertEquals("abc def", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("word1 word2 word3");
        assertEquals("word1", queue.consumeWord());
        assertEquals("word2 word3", queue.toString());
        
        assertEquals("word2", queue.consumeWord());
        assertEquals("word3", queue.toString());
        
        assertEquals("word3", queue.consumeWord());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("div class1 id1 tagName1");
        assertEquals("div", queue.consumeTagName());
        assertEquals("class1 id1 tagName1", queue.toString());
        
        assertEquals("class1", queue.consumeTagName());
        assertEquals("id1 tagName1", queue.toString());
        
        assertEquals("id1", queue.consumeTagName());
        assertEquals("tagName1", queue.toString());
        
        assertEquals("tagName1", queue.consumeTagName());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div.class1#id1.tagName1");
        assertEquals("div", queue.consumeElementSelector());
        assertEquals(".class1#id1.tagName1", queue.toString());
        
        assertEquals(".class1", queue.consumeElementSelector());
        assertEquals("#id1.tagName1", queue.toString());
        
        assertEquals("#id1", queue.consumeElementSelector());
        assertEquals(".tagName1", queue.toString());
        
        assertEquals(".tagName1", queue.consumeElementSelector());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("class1 id1 tagName1 identifier1");
        assertEquals("class1", queue.consumeCssIdentifier());
        assertEquals("id1 tagName1 identifier1", queue.toString());
        
        assertEquals("id1", queue.consumeCssIdentifier());
        assertEquals("tagName1 identifier1", queue.toString());
        
        assertEquals("tagName1", queue.consumeCssIdentifier());
        assertEquals("identifier1", queue.toString());
        
        assertEquals("identifier1", queue.consumeCssIdentifier());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class='class1' id='id1' style='style1'");
        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("='class1' id='id1' style='style1'", queue.toString());
        
        assertEquals("id", queue.consumeAttributeKey());
        assertEquals("='id1' style='style1'", queue.toString());
        
        assertEquals("style", queue.consumeAttributeKey());
        assertTrue(queue.isEmpty());
    }
}