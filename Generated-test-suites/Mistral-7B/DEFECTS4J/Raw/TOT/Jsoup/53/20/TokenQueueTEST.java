package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for TokenQueue class.
 */
public class TokenQueueTest {

    @Test
    public void testEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testSimpleQueue() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals("hello world", queue.toString());
        Assert.assertEquals('h', queue.peek());
        Assert.assertEquals("hello", queue.consume(6).toString());
        Assert.assertEquals('w', queue.consume());
        Assert.assertEquals("ld", queue.consume(2).toString());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdefg");
        Assert.assertFalse(queue.matches(""));
        Assert.assertFalse(queue.matches("xyz"));
        Assert.assertTrue(queue.matches("ab"));
        Assert.assertTrue(queue.matches("def"));
        Assert.assertTrue(queue.matches("g"));
        Assert.assertFalse(queue.matches("abcxyz"));
        Assert.assertFalse(queue.matches("hijk"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCDEFG");
        Assert.assertFalse(queue.matchesCS("xyz"));
        Assert.assertFalse(queue.matchesCS("abc"));
        Assert.assertTrue(queue.matchesCS("AB"));
        Assert.assertTrue(queue.matchesCS("DEF"));
        Assert.assertTrue(queue.matchesCS("G"));
        Assert.assertFalse(queue.matchesCS("abcXYZ"));
        Assert.assertFalse(queue.matchesCS("HIJK"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcdabcdabcd");
        Assert.assertTrue(queue.matchesAny("ab", "cd"));
        Assert.assertTrue(queue.matchesAny("cd", "ab"));
        Assert.assertFalse(queue.matchesAny("ef"));
        Assert.assertFalse(queue.matchesAny("xy"));
    }

    @Test
    public void testMatchesAnyChars() {
        TokenQueue queue = new TokenQueue("abcdabcdabcd");
        Assert.assertTrue(queue.matchesAny('a', 'd'));
        Assert.assertTrue(queue.matchesAny('d', 'a'));
        Assert.assertFalse(queue.matchesAny('e', 'd'));
        Assert.assertFalse(queue.matchesAny('x', 'y'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><body>");
        Assert.assertTrue(queue.matchesStartTag());
        Assert.assertFalse(queue.matchesStartTag()); // consume first <
        Assert.assertFalse(queue.matchesStartTag()); // consume <body>
        Assert.assertFalse(queue.matchesStartTag()); // consume >
        Assert.assertFalse(queue.matchesStartTag()); // consume <html
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("<html><body>");
        Assert.assertTrue(queue.matchChomp("<html>"));
        Assert.assertFalse(queue.matchChomp("body"));
        Assert.assertFalse(queue.matchChomp("head"));
        Assert.assertFalse(queue.matchChomp("foo"));
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" \t\r\n hello world");
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertTrue(queue.matchChomp(" ").isEmpty());
        Assert.assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abcd123efg");
        Assert.assertTrue(queue.matchesWord());
        Assert.assertTrue(queue.matchesWord());
        Assert.assertTrue(queue.matchesWord());
        Assert.assertTrue(queue.matchesWord());
        Assert.assertFalse(queue.matchesWord());
        Assert.assertFalse(queue.matchesWord());
        Assert.assertFalse(queue.matchesWord());
        Assert.assertFalse(queue.matchesWord());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeFail() {
        TokenQueue queue = new TokenQueue("</html>");
        queue.consume("body");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        String data = queue.consumeTo("(");
        Assert.assertEquals("(one (two) three)", data);
        Assert.assertEquals(" four", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        String data = queue.consumeToIgnoreCase("(");
        Assert.assertEquals("(one (two) three)", data);
        Assert.assertEquals(" four", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("hello world hello world");
        String data = queue.consumeToAny("hel", "wor");
        Assert.assertEquals("hello world", data);
        Assert.assertEquals("hello world", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        String data = queue.chompBalanced('(', ')');
        Assert.assertEquals("one (two) three", data);
        Assert.assertEquals(" four", queue.toString());
    }

    @Test
    public void testUnescape() {
        String escaped = "\\([\\{]123\\)";
        String unescaped = TokenQueue.unescape(escaped);
        Assert.assertEquals("(123)", unescaped);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" hello world ");
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertFalse(queue.consumeWhitespace());
        Assert.assertEquals("hello", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abcd123efg");
        String data = queue.consumeWord();
        Assert.assertEquals("abcd", data);
        Assert.assertEquals("123efg", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><body><div id=\"myDiv\"></div></body></html>");
        String data = queue.consumeTagName();
        Assert.assertEquals("html", data);
        Assert.assertEquals("<body><div id=\"myDiv\"></div></body>", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div.class1 .class2");
        String data = queue.consumeElementSelector();
        Assert.assertEquals("div", data);
        Assert.assertEquals(".class1 .class2", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("class1 idMyDiv");
        String data = queue.consumeCssIdentifier();
        Assert.assertEquals("class1", data);
        Assert.assertEquals("idMyDiv", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class=\"class1\" id=\"myDiv\"");
        String data = queue.consumeAttributeKey();
        Assert.assertEquals("class", data);
        Assert.assertEquals("\"class1\" id=\"myDiv\"", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("hello world hello world");
        String data = queue.remainder();
        Assert.assertEquals("hello world", queue.toString());
        Assert.assertEquals(data, "hello world");
    }
}