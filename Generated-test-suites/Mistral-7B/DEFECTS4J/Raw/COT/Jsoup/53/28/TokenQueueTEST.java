import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testConstructor() {
        TokenQueue queue = new TokenQueue("test");
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals("test", queue.toString());
    }

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());

        queue = new TokenQueue("test");
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("test");
        Assert.assertEquals('t', queue.peek());
        Assert.assertEquals('t', queue.consume());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddFirstNullString() {
        TokenQueue queue = new TokenQueue("test");
        queue.addFirst(null);
    }

    @Test
    public void testAddFirstChar() {
        TokenQueue queue = new TokenQueue("test");
        queue.addFirst('a');
        Assert.assertEquals('a', queue.peek());
    }

    @Test
    public void testAddFirstString() {
        TokenQueue queue = new TokenQueue("test");
        queue.addFirst("abcd");
        Assert.assertEquals('a', queue.peek());
        Assert.assertEquals("abcdtest", queue.toString());
    }

    @Test
    public void testMatchesNullSeq() {
        TokenQueue queue = new TokenQueue("test");
        Assert.assertFalse(queue.matches(null));
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("test");
        Assert.assertTrue(queue.matches("est"));
        Assert.assertFalse(queue.matches("tes"));
        Assert.assertFalse(queue.matches("foo"));
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("Test");
        Assert.assertTrue(queue.matchesCS("Test"));
        Assert.assertFalse(queue.matchesCS("test"));
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testMatchesAnyEmptySeq() {
        TokenQueue queue = new TokenQueue("test");
        Assert.assertFalse(queue.matchesAny(new String[0]));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("testest");
        Assert.assertTrue(queue.matchesAny("est", "test"));
        Assert.assertFalse(queue.matchesAny("foo", "bar"));
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("test");
        Assert.assertTrue(queue.matchesAny('s', 't'));
        Assert.assertFalse(queue.matchesAny('x', 'z'));
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<test>");
        Assert.assertTrue(queue.matchesStartTag());
        Assert.assertFalse(queue.matchesStartTag());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("test");
        Assert.assertTrue(queue.matchChomp("est"));
        Assert.assertFalse(queue.matches("foo"));
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" \t test");
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertFalse(queue.matchesWhitespace());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("test");
        Assert.assertTrue(queue.matchesWord());
        Assert.assertFalse(queue.matchesWord());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("test");
        queue.advance();
        Assert.assertEquals('e', queue.peek());
        Assert.assertEquals('e', queue.consume());
        Assert.assertEquals('s', queue.peek());
        Assert.assertEquals('s', queue.consume());
        Assert.assertEquals('', queue.toString());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("test");
        Assert.assertEquals('t', queue.consume());
        Assert.assertEquals('e', queue.consume());
        Assert.assertEquals('s', queue.consume());
        Assert.assertEquals('', queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeNullSeq() {
        TokenQueue queue = new TokenQueue("test");
        queue.consume(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeNotMatchedSeq() {
        TokenQueue queue = new TokenQueue("test");
        queue.consume("foo");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("testest");
        Assert.assertEquals("test", queue.consumeTo("est"));
        Assert.assertEquals("", queue.toString());

        queue = new TokenQueue("(one (two) three)");
        Assert.assertEquals("one (two)", queue.consumeToAny('(', ')'));
        Assert.assertEquals("three)", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("TESTest");
        Assert.assertEquals("test", queue.consumeToIgnoreCase("est"));
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("testest");
        Assert.assertEquals("test", queue.consumeToAny("est"));
        Assert.assertEquals("", queue.toString());

        queue = new TokenQueue("(one (two) three)");
        Assert.assertEquals("one (two)", queue.consumeToAny("("));
        Assert.assertEquals(")", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("((test)(test)))");
        Assert.assertEquals("testtest", queue.chompBalanced('(', ')'));
        Assert.assertEquals(")", queue.toString());

        queue = new TokenQueue("((test)(test)test)");
        Assert.assertEquals("testtest", queue.chompBalanced('(', ')'));
        Assert.assertEquals("test", queue.toString());
    }

    @Test
    public void testUnescape() {
        Assert.assertEquals("", TokenQueue.unescape(""));
        Assert.assertEquals("\\", TokenQueue.unescape("\\"));
        Assert.assertEquals("test\\test", TokenQueue.unescape("test\\test"));
        Assert.assertEquals("test\\\\test", TokenQueue.unescape("test\\\\test"));
        Assert.assertEquals("test\"test", TokenQueue.unescape("test\"test"));
        Assert.assertEquals("test'test", TokenQueue.unescape("test'test"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("test \t test");
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertEquals("test", queue.toString());

        queue = new TokenQueue(" test ");
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("testwordtest");
        Assert.assertEquals("test", queue.consumeWord());
        Assert.assertEquals("wordtest", queue.toString());

        queue = new TokenQueue("test");
        Assert.assertEquals("", queue.consumeWord());
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("test<tagname>test");
        Assert.assertEquals("tagname", queue.consumeTagName());
        Assert.assertEquals("test", queue.toString());

        queue = new TokenQueue("test<tagname>test");
        Assert.assertEquals("", queue.consumeTagName());
        Assert.assertEquals("test", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("test<tagname:element>test");
        Assert.assertEquals("element", queue.consumeElementSelector());
        Assert.assertEquals("test", queue.toString());

        queue = new TokenQueue("test<tagname:element>test");
        Assert.assertEquals("", queue.consumeElementSelector());
        Assert.assertEquals("test", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("test#id test");
        Assert.assertEquals("id", queue.consumeCssIdentifier());
        Assert.assertEquals("test", queue.toString());

        queue = new TokenQueue("test#id test");
        Assert.assertEquals("", queue.consumeCssIdentifier());
        Assert.assertEquals("test", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("test name='value' test");
        Assert.assertEquals("name", queue.consumeAttributeKey());
        Assert.assertEquals("value", queue.consume());
        Assert.assertEquals("test", queue.toString());

        queue = new TokenQueue("test name='value' test");
        Assert.assertEquals("", queue.consumeAttributeKey());
        Assert.assertEquals("test", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("test");
        Assert.assertEquals("", queue.remainder());
        Assert.assertEquals("", queue.toString());

        queue = new TokenQueue("testremainder");
        Assert.assertEquals("remainder", queue.remainder());
        Assert.assertEquals("test", queue.toString());
    }
}