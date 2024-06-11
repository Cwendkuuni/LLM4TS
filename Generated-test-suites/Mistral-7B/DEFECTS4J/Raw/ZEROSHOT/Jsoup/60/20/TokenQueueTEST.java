import org.jsoup.parser.TokenQueue;
import org.junit.Assert;
import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testTokenQueueEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        Assert.assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testTokenQueueNonEmpty() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        Assert.assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        Assert.assertEquals('a', tokenQueue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekAfterConsume() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        tokenQueue.consume();
        tokenQueue.peek();
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        tokenQueue.addFirst('x');
        Assert.assertEquals('x', tokenQueue.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        Assert.assertTrue(tokenQueue.matches("ab"));
        Assert.assertFalse(tokenQueue.matches("cd"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("ABCDEF");
        Assert.assertTrue(tokenQueue.matchesCS("Ab"));
        Assert.assertFalse(tokenQueue.matchesCS("cd"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        Assert.assertTrue(tokenQueue.matchesAny("ab", "cd"));
        Assert.assertFalse(tokenQueue.matchesAny("pq", "cd"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        Assert.assertTrue(tokenQueue.matchesAny('a', 'd'));
        Assert.assertFalse(tokenQueue.matchesAny('p', 'd'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("<abc>def");
        Assert.assertTrue(tokenQueue.matchesStartTag());
        Assert.assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("abcdef<ab>cd");
        Assert.assertTrue(tokenQueue.matchChomp("ab"));
        Assert.assertEquals("cd", tokenQueue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tokenQueue = new TokenQueue(" \t\r\n abcdef ");
        Assert.assertTrue(tokenQueue.matchesWhitespace());
        Assert.assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        Assert.assertTrue(tokenQueue.matchesWord());
        Assert.assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        tokenQueue.advance();
        Assert.assertEquals('b', tokenQueue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        Assert.assertEquals('a', tokenQueue.consume());
        Assert.assertEquals('b', tokenQueue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeEmptyQueue() {
        TokenQueue tokenQueue = new TokenQueue("");
        tokenQueue.consume();
    }

    @Test
    public void testConsumeSequence() {
        TokenQueue tokenQueue = new TokenQueue("abcdef<ab>cd");
        String seq = "ab";
        tokenQueue.consume(seq);
        Assert.assertEquals("cd", tokenQueue.toString());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue tokenQueue = new TokenQueue("abcdef<ab>cd<ef>");
        Assert.assertEquals("ab", tokenQueue.consumeTo("ab").toString());
        Assert.assertEquals("cd<ef>", tokenQueue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("AbCdEf<Ab>Cd");
        Assert.assertEquals("Ab", tokenQueue.consumeToIgnoreCase("ab").toString());
        Assert.assertEquals("Cd<Cd>", tokenQueue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue tokenQueue = new TokenQueue("abcdef<ab>cd<ef>");
        Assert.assertEquals("def", tokenQueue.consumeToAny("ef", "ab").toString());
        Assert.assertEquals("cd<ef>", tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(<abc>(def)>(pqr)>)</abc>");
        Assert.assertEquals("(<abc>(def)", tokenQueue.chompBalanced('(', ')').toString());
        Assert.assertEquals(">(pqr)>)</abc>", tokenQueue.toString());
    }

    @Test
    public void testUnescape() {
        String in = "\\<abc\\>\\def\\";
        String out = TokenQueue.unescape(in);
        Assert.assertEquals("<abc>def\\", out);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue(" \t\r\n abcdef ");
        Assert.assertTrue(tokenQueue.consumeWhitespace());
        Assert.assertFalse(tokenQueue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        Assert.assertEquals("abc", tokenQueue.consumeWord().toString());
        Assert.assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("<abc:def>ghi>");
        Assert.assertEquals("abc:", tokenQueue.consumeTagName().toString());
        Assert.assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("<div#id:class.class class>");
        Assert.assertEquals("div", tokenQueue.consumeElementSelector().toString());
        Assert.assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tokenQueue = new TokenQueue("id:foo class:bar");
        Assert.assertEquals("foo", tokenQueue.consumeCssIdentifier().toString());
        Assert.assertEquals("class:bar", tokenQueue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tokenQueue = new TokenQueue("class=\"foo\" id=\"bar\"");
        Assert.assertEquals("class", tokenQueue.consumeAttributeKey().toString());
        Assert.assertEquals("\"foo\" id=\"bar\"", tokenQueue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        Assert.assertEquals("def", tokenQueue.remainder().toString());
        Assert.assertEquals("", tokenQueue.toString());
    }
}