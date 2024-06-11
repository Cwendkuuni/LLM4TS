package org.jsoup.parser;
import org.junit.*;
import static org.junit.Assert.*;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertFalse(tokenQueue.isEmpty());
    }
    
    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertEquals('H', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("Hello");
        tokenQueue.addFirst('G');
        assertEquals('G', tokenQueue.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertTrue(tokenQueue.matches("Hel"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertTrue(tokenQueue.matchesCS("Hel"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertTrue(tokenQueue.matchesAny("Hel", "llo"));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("<html>");
        assertTrue(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertTrue(tokenQueue.matchChomp("Hel"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tokenQueue = new TokenQueue(" ");
        assertTrue(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord(){
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertTrue(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance(){
        TokenQueue tokenQueue = new TokenQueue("Hello");
        tokenQueue.advance();
        assertEquals('e', tokenQueue.peek());
    }

    @Test
    public void testConsume(){
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertEquals('H', tokenQueue.consume());
    }

    @Test
    public void testConsumeTo(){
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertEquals("H", tokenQueue.consumeTo("el"));
    }

    @Test
    public void testConsumeToIgnoreCase(){
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertEquals("He", tokenQueue.consumeToIgnoreCase("l"));
    }

    @Test
    public void testConsumeToAny(){
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertEquals("H", tokenQueue.consumeToAny("e", "l"));
    }

    @Test
    public void testChompTo(){
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertEquals("H", tokenQueue.chompTo("el"));
    }

    @Test
    public void testChompToIgnoreCase(){
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertEquals("He", tokenQueue.chompToIgnoreCase("l"));
    }

    @Test
    public void testChompBalanced(){
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
    }

    @Test
    public void testUnescape(){
        assertEquals("Hello", TokenQueue.unescape("Hello"));
    }

    @Test
    public void testConsumeWhitespace(){
        TokenQueue tokenQueue = new TokenQueue(" ");
        assertTrue(tokenQueue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord(){
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertEquals("Hello", tokenQueue.consumeWord());
    }

    @Test
    public void testConsumeTagName(){
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertEquals("Hello", tokenQueue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector(){
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertEquals("Hello", tokenQueue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier(){
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertEquals("Hello", tokenQueue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey(){
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertEquals("Hello", tokenQueue.consumeAttributeKey());
    }

    @Test
    public void testRemainder(){
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertEquals("Hello", tokenQueue.remainder());
    }
}