package org.jsoup.parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TokenQueueTest {
    TokenQueue tokenQueue;
    
    @Before
    public void setup() {
        tokenQueue = new TokenQueue("testToken");
    }
    
    @Test
    public void testIsEmpty() {
        Assert.assertFalse(tokenQueue.isEmpty());
    }
    
    @Test
    public void testPeek() {
        Assert.assertEquals('t', tokenQueue.peek());
    }
    
    @Test
    public void testAddFirst() {
        tokenQueue.addFirst('A');
        Assert.assertEquals('A', tokenQueue.peek());
    }
    
    @Test
    public void testMatches() {
        Assert.assertTrue(tokenQueue.matches("test"));
        Assert.assertFalse(tokenQueue.matches("fail"));
    }
    
    @Test
    public void testMatchesCS() {
        Assert.assertTrue(tokenQueue.matchesCS("test"));
        Assert.assertFalse(tokenQueue.matchesCS("Test"));
    }
    
    @Test
    public void testMatchesAny_String() {
        Assert.assertTrue(tokenQueue.matchesAny("test", "token"));
        Assert.assertFalse(tokenQueue.matchesAny("fail", "fail2"));
    }
    
    @Test
    public void testMatchesAny_char() {
        Assert.assertTrue(tokenQueue.matchesAny('t', 'o'));
        Assert.assertFalse(tokenQueue.matchesAny('f', 'a'));
    }
    
    @Test
    public void testMatchesStartTag() {
        Assert.assertFalse(tokenQueue.matchesStartTag());
    }
    
    @Test
    public void testMatchChomp() {
        Assert.assertTrue(tokenQueue.matchChomp("test"));
        Assert.assertFalse(tokenQueue.matchChomp("fail"));
    }
    
    @Test
    public void testMatchesWhitespace() {
        Assert.assertFalse(tokenQueue.matchesWhitespace());
    }
    
    @Test
    public void testMatchesWord() {
        Assert.assertTrue(tokenQueue.matchesWord());
    }
    
    @Test
    public void testAdvance() {
        tokenQueue.advance();
        Assert.assertEquals('e', tokenQueue.peek());
    }
    
    @Test
    public void testConsume() {
        Assert.assertEquals('t', tokenQueue.consume());
    }
    
    @Test
    public void testConsume_String() {
        tokenQueue.consume("test");
        Assert.assertEquals('T', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeTo() {
        Assert.assertEquals("", tokenQueue.consumeTo(" "));
        Assert.assertEquals("Token", tokenQueue.consumeTo("en"));
    }
    
    @Test
    public void testConsumeToIgnoreCase() {
        Assert.assertEquals("", tokenQueue.consumeToIgnoreCase(" "));
        Assert.assertEquals("token", tokenQueue.consumeToIgnoreCase("EN"));
    }
    
    @Test
    public void testConsumeToAny() {
        Assert.assertEquals("test", tokenQueue.consumeToAny(" ", "token"));
        Assert.assertEquals("", tokenQueue.consumeToAny("fail", "fail2"));
    }
    
    @Test
    public void testChompTo() {
        Assert.assertEquals("", tokenQueue.chompTo("test"));
        Assert.assertEquals("Token", tokenQueue.chompTo("en"));
    }
    
    @Test
    public void testChompToIgnoreCase() {
        Assert.assertEquals("", tokenQueue.chompToIgnoreCase("test"));
        Assert.assertEquals("Token", tokenQueue.chompToIgnoreCase("EN"));
    }
    
    @Test
    public void testChompBalanced() {
        Assert.assertEquals("testToken", tokenQueue.chompBalanced('(', ')'));
    }
    
    @Test
    public void testUnescape() {
        Assert.assertEquals("\\", TokenQueue.unescape("\\\\"));
    }
    
    @Test
    public void testConsumeWhitespace() {
        Assert.assertTrue(tokenQueue.consumeWhitespace());
    }
    
    @Test
    public void testConsumeWord() {
        Assert.assertEquals("testToken", tokenQueue.consumeWord());
    }
    
    @Test
    public void testConsumeTagName() {
        Assert.assertEquals("testToken", tokenQueue.consumeTagName());
    }
    
    @Test
    public void testConsumeElementSelector() {
        Assert.assertEquals("testToken", tokenQueue.consumeElementSelector());
    }
    
    @Test
    public void testConsumeCssIdentifier() {
        Assert.assertEquals("testToken", tokenQueue.consumeCssIdentifier());
    }
    
    @Test
    public void testConsumeAttributeKey() {
        Assert.assertEquals("testToken", tokenQueue.consumeAttributeKey());
    }
    
    @Test
    public void testRemainder() {
        Assert.assertEquals("testToken", tokenQueue.remainder());
    }
    
    @Test
    public void testToString() {
        Assert.assertEquals("testToken", tokenQueue.toString());
    }
}