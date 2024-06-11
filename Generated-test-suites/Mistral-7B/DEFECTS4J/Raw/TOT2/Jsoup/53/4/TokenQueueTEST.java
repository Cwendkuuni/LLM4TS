import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());
        
        queue.addFirst('a');
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abc");
        Assert.assertEquals('a', queue.peek());
        
        queue.advance();
        Assert.assertEquals('b', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddFirstAndConsumeWithEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.consume("test");
        queue.addFirst('a');
        queue.consume("anotherTest");
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcabc");
        Assert.assertTrue(queue.matches("ab"));
        Assert.assertFalse(queue.matches("cd"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCabc");
        Assert.assertTrue(queue.matchesCS("AB"));
        Assert.assertFalse(queue.matchesCS("cd"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("123456");
        Assert.assertTrue(queue.matchesAny('1', '2', '3'));
        Assert.assertTrue(queue.matchesAny('3', '4', '5'));
        Assert.assertFalse(queue.matchesAny('7'));
    }

    @Test
    public void testMatchesAnyWithEmptyArray() {
        TokenQueue queue = new TokenQueue("abc");
        Assert.assertFalse(queue.matchesAny());
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head>");
        Assert.assertTrue(queue.matchesStartTag());
        Assert.assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abc<test>def");
        Assert.assertTrue(queue.matchChomp("<test>"));
        Assert.assertEquals('d', queue.peek());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" \t\n\r\f");
        Assert.assertTrue(queue.matchesWhitespace());
        queue.advance();
        Assert.assertTrue(queue.matchesWhitespace());
        queue.advance();
        Assert.assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("aBc123");
        Assert.assertTrue(queue.matchesWord());
        Assert.assertTrue(queue.matchesWord());
        Assert.assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abc");
        queue.advance();
        Assert.assertEquals('b', queue.peek());
        queue.advance();
        Assert.assertEquals('c', queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abc");
        queue.consume('a');
        Assert.assertEquals('b', queue.peek());
        queue.consume("bc");
        Assert.assertFalse(queue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.consume('a');
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertEquals("abc", queue.consumeTo("abc"));
        Assert.assertEquals("def", queue.consumeToIgnoreCase("Def"));
        Assert.assertEquals("", queue.consumeToIgnoreCase("GHI"));
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeToWithEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.consumeTo("test");
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("123456");
        Assert.assertEquals("123", queue.consumeToAny("1", "2", "3"));
        Assert.assertEquals("56", queue.consumeToAny("3", "4", "5"));
        Assert.assertEquals("", queue.consumeToAny("7", "8", "9"));
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(())[]{}");
        Assert.assertEquals("(())", queue.chompBalanced('(', ')'));
        Assert.assertEquals("[]{}", queue.chompBalanced('[', ']'));
        Assert.assertEquals("", queue.chompBalanced('{', '}'));
    }

    @Test
    public void testUnescape() {
        String escapedString = "\\(\\)\\[\\]\\{\\}\\<\\>\\/";
        Assert.assertEquals("()[]{}<>", TokenQueue.unescape(escapedString));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" \t\n\r\f");
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abc123");
        Assert.assertEquals("abc", queue.consumeWord());
        Assert.assertEquals("123", queue.consumeWord());
        Assert.assertEquals("", queue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head>");
        Assert.assertEquals("html", queue.consumeTagName());
        Assert.assertEquals("head", queue.consumeTagName());
        Assert.assertEquals("title", queue.consumeTagName());
        Assert.assertEquals("", queue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("<div class='class1 class2' id='id1'>Test</div>");
        Assert.assertEquals("div", queue.consumeElementSelector());
        Assert.assertEquals("class1 class2", queue.consumeElementSelector());
        Assert.assertEquals("id1", queue.consumeElementSelector());
        Assert.assertEquals("", queue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("id:class1 class:class2");
        Assert.assertEquals("id", queue.consumeCssIdentifier());
        Assert.assertEquals("class1", queue.consumeCssIdentifier());
        Assert.assertEquals("class2", queue.consumeCssIdentifier());
        Assert.assertEquals("", queue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("id='id1' class='class2' data-test='test'");
        Assert.assertEquals("id", queue.consumeAttributeKey());
        Assert.assertEquals("class", queue.consumeAttributeKey());
        Assert.assertEquals("data-test", queue.consumeAttributeKey());
        Assert.assertEquals("", queue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertEquals("def", queue.remainder());
    }
}