package org.jsoup.parser;

import org.junit.Assert;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;

/**
 * Test case for TokenQueue class.
 */
public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());

        queue.addFirst("abc");
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertEquals('a', queue.peek());
        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals('b', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.addFirst("xyz");
        Assert.assertEquals('x', queue.peek());
        Assert.assertEquals('x', queue.consume());
        Assert.assertEquals('a', queue.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcabc");
        Assert.assertTrue(queue.matches("bc"));
        Assert.assertFalse(queue.matches("cd"));
        Assert.assertTrue(queue.matches(""));
        Assert.assertTrue(queue.matches("a"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("AbCaB");
        Assert.assertTrue(queue.matchesCS("AB"));
        Assert.assertFalse(queue.matchesCS("cd"));
        Assert.assertTrue(queue.matchesCS(""));
        Assert.assertTrue(queue.matchesCS("a"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("12345");
        Assert.assertTrue(queue.matchesAny("1", "2", "3", "4", "5"));
        Assert.assertTrue(queue.matchesAny("1", "5"));
        Assert.assertFalse(queue.matchesAny("6", "7"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertTrue(queue.matchesAny('a', 'b', 'c'));
        Assert.assertTrue(queue.matchesAny('a', 'f'));
        Assert.assertFalse(queue.matchesAny('g', 'h'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<abc>def");
        Assert.assertTrue(queue.matchesStartTag());
        Assert.assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abc<abc>def");
        Assert.assertEquals("abc", queue.matchChomp("<abc>"));
        Assert.assertEquals("def", queue.consumeTo("def"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   abc  def");
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("ab_c1Def");
        Assert.assertTrue(queue.matchesWord());
        Assert.assertFalse(queue.matchesWord());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithNoMatch() {
        TokenQueue queue = new TokenQueue("12345");
        queue.consume("6");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeToWithNoMatch() {
        TokenQueue queue = new TokenQueue("12345");
        queue.consumeTo("6");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("12345");
        String result = queue.consumeTo("34");
        Assert.assertEquals("12", result);
        Assert.assertEquals("5", queue.consume());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("AbCdEf");
        String result = queue.consumeToIgnoreCase("bc");
        Assert.assertEquals("Ab", result);
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("12345");
        String result = queue.consumeToAny("1", "2", "3", "4", "5");
        Assert.assertEquals("5", result);
        Assert.assertEquals("", queue.consume());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(())[]");
        String result = queue.chompBalanced('(', ')');
        Assert.assertEquals("()[]", queue.toString());
        Assert.assertEquals("(())", result);
    }

    @Test
    public void testUnescape() {
        String input = "\\\\\\/foo\\\\bar\\\\";
        String result = TokenQueue.unescape(input);
        Assert.assertEquals("/foo/bar/", result);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   abc  def");
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertEquals("abc", queue.consumeWord());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("ab_c1Def");
        Assert.assertEquals("ab_c", queue.consumeWord());
        Assert.assertEquals("1Def", queue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<ab:c _d>ef");
        Assert.assertEquals("ab:c", queue.consumeTagName());
        Assert.assertEquals("_d", queue.consumeAttributeKey());
        Assert.assertEquals("ef", queue.consume());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div.class1.class2[attr='value']");
        Assert.assertEquals("div", queue.consumeElementSelector());
        Assert.assertEquals(".class1", queue.consume());
        Assert.assertEquals(".class2[attr='value']", queue.consume());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("class1 class2 id1 id2 class1-class2 id_1");
        Assert.assertEquals("class1", queue.consumeCssIdentifier());
        Assert.assertEquals("id1", queue.consumeCssIdentifier());
        Assert.assertEquals("class1-class2", queue.consumeCssIdentifier());
        Assert.assertEquals("id_1", queue.consumeCssIdentifier());
        Assert.assertEquals("", queue.consume());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class='class1' id='id1' style='color: red'");
        Assert.assertEquals("class", queue.consumeAttributeKey());
        Assert.assertEquals("id", queue.consumeAttributeKey());
        Assert.assertEquals("style", queue.consumeAttributeKey());
        Assert.assertEquals("", queue.consume());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("12345");
        String result = queue.remainder();
        Assert.assertEquals("5", result);
        Assert.assertEquals("", queue.consume());
    }
}