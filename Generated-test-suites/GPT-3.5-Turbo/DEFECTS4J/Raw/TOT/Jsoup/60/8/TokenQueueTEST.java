import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TokenQueueTest {

    private TokenQueue tokenQueue;
    
    @Before
    public void setup() {
        tokenQueue = new TokenQueue("TestingTokenQueue");
    }
    
    @Test
    public void testIsEmpty() {
        Assert.assertFalse(tokenQueue.isEmpty());
    }
    
    @Test
    public void testPeek() {
        Assert.assertEquals('T', tokenQueue.peek());
    }
    
    @Test
    public void testAddFirst() {
        tokenQueue.addFirst('A');
        Assert.assertEquals('A', tokenQueue.peek());
    }
    
    @Test
    public void testMatches() {
        Assert.assertTrue(tokenQueue.matches("Testing"));
    }
    
    @Test
    public void testMatchesCS() {
        Assert.assertTrue(tokenQueue.matchesCS("Testing"));
    }
    
    @Test
    public void testMatchesAny_String() {
        Assert.assertTrue(tokenQueue.matchesAny("Testing", "TokenQueue"));
    }
    
    @Test
    public void testMatchesAny_char() {
        Assert.assertTrue(tokenQueue.matchesAny('T', 'Q'));
    }
    
    @Test
    public void testMatchesStartTag() {
        Assert.assertFalse(tokenQueue.matchesStartTag());
    }
    
    @Test
    public void testMatchChomp() {
        Assert.assertTrue(tokenQueue.matchChomp("Testing"));
        Assert.assertEquals(7, tokenQueue.remainingLength());
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
        Assert.assertEquals('T', tokenQueue.consume());
    }
    
    @Test
    public void testConsume_String() {
        tokenQueue.consume("TestingTokenQueue");
        Assert.assertTrue(tokenQueue.isEmpty());
    }
    
    @Test
    public void testConsumeTo() {
        Assert.assertEquals("Test", tokenQueue.consumeTo("Token"));
    }
    
    @Test
    public void testConsumeToIgnoreCase() {
        Assert.assertEquals("Test", tokenQueue.consumeToIgnoreCase("token"));
    }
    
    @Test
    public void testConsumeToAny() {
        Assert.assertEquals("Test", tokenQueue.consumeToAny("o", "e"));
    }
    
    @Test
    public void testChompTo() {
        Assert.assertEquals("Test", tokenQueue.chompTo("Token"));
        Assert.assertEquals(6, tokenQueue.remainingLength());
    }
    
    @Test
    public void testChompToIgnoreCase() {
        Assert.assertEquals("Test", tokenQueue.chompToIgnoreCase("token"));
        Assert.assertEquals(6, tokenQueue.remainingLength());
    }
    
    @Test
    public void testChompBalanced() {
        tokenQueue.addFirst("(Testing(Token)Queue)");
        Assert.assertEquals("Testing(Token)Queue", tokenQueue.chompBalanced('(', ')'));
    }
    
    @Test
    public void testUnescape() {
        Assert.assertEquals("Testing", TokenQueue.unescape("Testing"));
    }
    
    @Test
    public void testConsumeWhitespace() {
        Assert.assertFalse(tokenQueue.consumeWhitespace());
    }
    
    @Test
    public void testConsumeWord() {
        Assert.assertEquals("TestingTokenQueue", tokenQueue.consumeWord());
    }
    
    @Test
    public void testConsumeTagName() {
        tokenQueue.addFirst(":TagName");
        Assert.assertEquals(":TagName", tokenQueue.consumeTagName());
    }
    
    @Test
    public void testConsumeElementSelector() {
        tokenQueue.addFirst("|TagName");
        Assert.assertEquals("|TagName", tokenQueue.consumeElementSelector());
    }
    
    @Test
    public void testConsumeCssIdentifier() {
        tokenQueue.addFirst("-CssIdentifier");
        Assert.assertEquals("-CssIdentifier", tokenQueue.consumeCssIdentifier());
    }
    
    @Test
    public void testConsumeAttributeKey() {
        tokenQueue.addFirst(":AttributeKey");
        Assert.assertEquals(":AttributeKey", tokenQueue.consumeAttributeKey());
    }
    
    @Test
    public void testRemainder() {
        tokenQueue.consume("Testing");
        Assert.assertEquals("TokenQueue", tokenQueue.remainder());
    }
    
}