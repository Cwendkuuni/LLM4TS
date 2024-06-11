import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testTokenQueueEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testTokenQueuePeek() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertEquals('a', tokenQueue.peek());
    }

    @Test
    public void testTokenQueueAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("");
        tokenQueue.addFirst('a');
        assertEquals('a', tokenQueue.peek());
    }

    @Test
    public void testTokenQueueAddFirstString() {
        TokenQueue tokenQueue = new TokenQueue("");
        tokenQueue.addFirst("ab");
        assertEquals("ab", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueMatches() {
        TokenQueue tokenQueue = new TokenQueue("abcd");
        assertTrue(tokenQueue.matches("ab"));
    }

    @Test
    public void testTokenQueueMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("abCD");
        assertTrue(tokenQueue.matchesCS("ab"));
    }

    @Test
    public void testTokenQueueMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("abcd");
        assertTrue(tokenQueue.matchesAny("ab", "cd"));
    }

    @Test
    public void testTokenQueueMatchesAnyChar() {
        TokenQueue tokenQueue = new TokenQueue("abcd");
        assertTrue(tokenQueue.matchesAny('a', 'd'));
    }

    @Test
    public void testTokenQueueMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("<tag>");
        assertTrue(tokenQueue.matchesStartTag());
    }

    @Test
    public void testTokenQueueMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("ab<cd>ef");
        tokenQueue.matchChomp("<cd>");
        assertEquals("abef", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueMatchesWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("  ");
        assertTrue(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testTokenQueueMatchesWord() {
        TokenQueue tokenQueue = new TokenQueue("abcd");
        assertTrue(tokenQueue.matchesWord());
    }

    @Test
    public void testTokenQueueAdvance() {
        TokenQueue tokenQueue = new TokenQueue("abcd");
        tokenQueue.advance();
        assertEquals('b', tokenQueue.peek());
    }

    @Test
    public void testTokenQueueConsume() {
        TokenQueue tokenQueue = new TokenQueue("abcd");
        assertEquals('a', tokenQueue.consume());
        assertEquals('b', tokenQueue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testTokenQueueConsumeMismatched() {
        TokenQueue tokenQueue = new TokenQueue("ab<cd>ef");
        tokenQueue.consume("ef");
        tokenQueue.consume("ab");
        tokenQueue.consume("cd"); // should fail
    }

    @Test
    public void testTokenQueueConsumeTo() {
        TokenQueue tokenQueue = new TokenQueue("ab<cd>ef");
        assertEquals("cd", tokenQueue.consumeTo("cd"));
        assertEquals("<ef>", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueConsumeToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("AbCdEfGhIjKl");
        assertEquals("cd", tokenQueue.consumeToIgnoreCase("Cd"));
        assertEquals("AbEfGhIjKl", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueConsumeToAny() {
        TokenQueue tokenQueue = new TokenQueue("abcd");
        assertEquals("bc", tokenQueue.consumeToAny("ab", "cd"));
        assertEquals("a", tokenQueue.peek());
    }

    @Test
    public void testTokenQueueChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(ab(cd)ef)");
        assertEquals("ab(cd)", tokenQueue.chompBalanced('(', ')'));
        assertEquals("ef)", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueUnescape() {
        assertEquals("abc\\def\\ghi", TokenQueue.unescape("abc\\def\\ghi"));
    }

    @Test
    public void testTokenQueueConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue(" \t\r\n abcd");
        assertTrue(tokenQueue.consumeWhitespace());
        assertEquals('a', tokenQueue.peek());
    }

    @Test
    public void testTokenQueueConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue("abcd");
        assertEquals("ab", tokenQueue.consumeWord());
        assertEquals("cd", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("<tag>");
        assertEquals("tag", tokenQueue.consumeTagName());
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("tag|class:class1");
        assertEquals("tag", tokenQueue.consumeElementSelector());
        assertEquals("|class:class1", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueConsumeCssIdentifier() {
        TokenQueue tokenQueue = new TokenQueue("class:class1 id:id1");
        assertEquals("class1", tokenQueue.consumeCssIdentifier());
        assertEquals(" id:id1", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueConsumeAttributeKey() {
        TokenQueue tokenQueue = new TokenQueue("class:class1 id:id1 style:style1");
        assertEquals("class", tokenQueue.consumeAttributeKey());
        assertEquals(":class1 id:id1 style:style1", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueRemainder() {
        TokenQueue tokenQueue = new TokenQueue("abcd");
        assertEquals("cd", tokenQueue.remainder());
        assertEquals("", tokenQueue.toString());
    }
}