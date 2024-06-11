package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;

import java.util.Character;

/**
 * TokenQueue test cases.
 */
public class TokenQueueTest {

    @org.junit.Before
    public void setUp() {
        // Initialize test data
        String testData = "test<tag> <tag1> <tag2:attr=\"value\"> <tag3 class=\"class1 class2\"> <tag4 id=\"id1\" id=\"id2\"/>";
        TokenQueue tokenQueue = new TokenQueue(testData);
    }

    @org.junit.Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());

        TokenQueue tokenQueue1 = new TokenQueue("test");
        assertFalse(tokenQueue1.isEmpty());
    }

    @org.junit.Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertEquals('t', tokenQueue.peek());

        TokenQueue tokenQueue1 = new TokenQueue("<tag>");
        assertEquals('<', tokenQueue1.peek());
    }

    @org.junit.Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("");
        tokenQueue.addFirst('a');
        assertEquals("a", tokenQueue.toString());

        TokenQueue tokenQueue1 = new TokenQueue("test");
        tokenQueue1.addFirst('a');
        assertEquals("atest", tokenQueue1.toString());

        TokenQueue tokenQueue2 = new TokenQueue("<tag>");
        tokenQueue2.addFirst("test");
        assertEquals("test<tag>", tokenQueue2.toString());
    }

    @org.junit.Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertTrue(tokenQueue.matches("est"));
        assertTrue(tokenQueue.matches("test"));
        assertFalse(tokenQueue.matches("tes"));

        TokenQueue tokenQueue1 = new TokenQueue("<tag>");
        assertTrue(tokenQueue1.matches("tag"));
        assertFalse(tokenQueue1.matches("tag1"));

        TokenQueue tokenQueue2 = new TokenQueue("test<tag>");
        assertTrue(tokenQueue2.matches("test"));
        assertTrue(tokenQueue2.matches("tag"));
    }

    @org.junit.Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("Test");
        assertFalse(tokenQueue.matchesCS("test"));
        assertTrue(tokenQueue.matchesCS("Test"));

        TokenQueue tokenQueue1 = new TokenQueue("<TAG>");
        assertFalse(tokenQueue1.matchesCS("tag"));
        assertTrue(tokenQueue1.matchesCS("TAG"));
    }

    @org.junit.Test
    public void testMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertFalse(tokenQueue.matchesAny("test1"));
        assertTrue(tokenQueue.matchesAny("test", "est"));

        TokenQueue tokenQueue1 = new TokenQueue("<tag>");
        assertTrue(tokenQueue1.matchesAny("tag", "TAG"));
        assertFalse(tokenQueue1.matchesAny("TAG1"));

        TokenQueue tokenQueue2 = new TokenQueue("test<tag>");
        assertTrue(tokenQueue2.matchesAny("test", "tag"));
        assertFalse(tokenQueue2.matchesAny("TAG"));
    }

    @org.junit.Test
    public void testMatchesAnyChars() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertTrue(tokenQueue.matchesAny(new Character[]{'e', 's', 't'}));
        assertFalse(tokenQueue.matchesAny(new Character[]{'a', 'b'}));

        TokenQueue tokenQueue1 = new TokenQueue("<tag>");
        assertTrue(tokenQueue1.matchesAny(new Character[]{'t', 'a', 'g'}));
        assertFalse(tokenQueue1.matchesAny(new Character[]{'a', 'b'}));

        TokenQueue tokenQueue2 = new TokenQueue("test<tag>");
        assertTrue(tokenQueue2.matchesAny(new Character[]{'t', 'e', 's', 't', 'a', 'g'}));
        assertFalse(tokenQueue2.matchesAny(new Character[]{'a', 'b'}));
    }

    @org.junit.Test
    public void testMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertFalse(tokenQueue.matchesStartTag());

        TokenQueue tokenQueue1 = new TokenQueue("test");
        assertFalse(tokenQueue1.matchesStartTag());

        TokenQueue tokenQueue2 = new TokenQueue("test<tag>");
        assertTrue(tokenQueue2.matchesStartTag());
    }

    @org.junit.Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("test<tag>");
        assertTrue(tokenQueue.matchChomp("test"));
        assertEquals("tag", tokenQueue.toString());

        TokenQueue tokenQueue1 = new TokenQueue("<tag> <tag1> test<tag2>");
        assertTrue(tokenQueue1.matchChomp("tag"));
        assertEquals("<tag1> test<tag2>", tokenQueue1.toString());
    }

    @org.junit.Test
    public void testMatchesWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertFalse(tokenQueue.matchesWhitespace());

        TokenQueue tokenQueue1 = new TokenQueue("test \t");
        assertTrue(tokenQueue1.matchesWhitespace());

        TokenQueue tokenQueue2 = new TokenQueue("test\n");
        assertTrue(tokenQueue2.matchesWhitespace());
    }

    @org.junit.Test
    public void testMatchesWord() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertTrue(tokenQueue.matchesWord());

        TokenQueue tokenQueue1 = new TokenQueue("test123");
        assertTrue(tokenQueue1.matchesWord());

        TokenQueue tokenQueue2 = new TokenQueue("test_test");
        assertTrue(tokenQueue2.matchesWord());

        TokenQueue tokenQueue3 = new TokenQueue("test 123");
        assertTrue(tokenQueue3.matchesWord());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testConsumeWrongSequence() {
        TokenQueue tokenQueue = new TokenQueue("test<tag>");
        tokenQueue.consume("tag1");
    }

    @org.junit.Test
    public void testConsume() {
        TokenQueue tokenQueue = new TokenQueue("test<tag>");
        String consumed = tokenQueue.consume("tag");
        assertEquals("test", tokenQueue.toString());
        assertEquals("tag", consumed);

        TokenQueue tokenQueue1 = new TokenQueue("<tag> <tag1> test<tag2>");
        consumed = tokenQueue1.consume("tag");
        assertEquals("<tag1> test<tag2>", tokenQueue1.toString());
        assertEquals("tag", consumed);
    }

    @org.junit.Test
    public void testConsumeTo() {
        TokenQueue tokenQueue = new TokenQueue("test<tag1> test<tag2> test<tag3>");
        String consumed = tokenQueue.consumeTo("tag1");
        assertEquals("test<tag2> test<tag3>", tokenQueue.toString());
        assertEquals("test<tag1>", consumed);

        consumed = tokenQueue.consumeTo("tag3");
        assertEquals("test<tag2>", tokenQueue.toString());
        assertEquals("test<tag3>", consumed);

        consumed = tokenQueue.consumeTo("tag4");
        assertEquals("", tokenQueue.toString());
        assertEquals("", consumed);
    }

    @org.junit.Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("test<TAG1> test<TAG2> test<TAG3>");
        String consumed = tokenQueue.consumeToIgnoreCase("TAG1");
        assertEquals("test<TAG2> test<TAG3>", tokenQueue.toString());
        assertEquals("test<TAG1>", consumed);

        consumed = tokenQueue.consumeToIgnoreCase("TAG3");
        assertEquals("test<TAG2>", tokenQueue.toString());
        assertEquals("test<TAG3>", consumed);

        consumed = tokenQueue.consumeToIgnoreCase("TAG4");
        assertEquals("", tokenQueue.toString());
        assertEquals("", consumed);
    }

    @org.junit.Test
    public void testConsumeToAny() {
        TokenQueue tokenQueue = new TokenQueue("test<tag> tag1 tag2 tag3 tag1");
        String consumed = tokenQueue.consumeToAny("tag1", "tag2");
        assertEquals("tag3 tag1", tokenQueue.toString());
        assertEquals("tag1", consumed);

        consumed = tokenQueue.consumeToAny("tag1", "tag4");
        assertEquals("tag3", tokenQueue.toString());
        assertEquals("", consumed);
    }

    @org.junit.Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(test) (test1) (test2) (test3) (test4)");
        String consumed = tokenQueue.chompBalanced('(', ')');
        assertEquals("test) (test1) (test2) (test3) (test4)", tokenQueue.toString());
        assertEquals("(test)", consumed);

        consumed = tokenQueue.chompBalanced('[', ']');
        assertEquals("test) (test1) (test2) (test3) (test4)", tokenQueue.toString());
        assertEquals("", consumed);

        consumed = tokenQueue.chompBalanced('{', '}');
        assertEquals("test) (test1) (test2) (test3) (test4)", tokenQueue.toString());
        assertEquals("", consumed);
    }

    @org.junit.Test
    public void testUnescape() {
        String in = "\\(test\\) \\[test1\\] \\{test2\\}";
        String out = TokenQueue.unescape(in);
        assertEquals("(test) [test1] {test2}", out);
    }

    @org.junit.Test
    public void testConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue(" test test \t test ");
        boolean seen = tokenQueue.consumeWhitespace();
        String remaining = tokenQueue.toString();
        assertTrue(seen);
        assertEquals("test test", remaining);
    }

    @org.junit.Test
    public void testConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue("test test word word test");
        String consumed = tokenQueue.consumeWord();
        String remaining = tokenQueue.toString();
        assertEquals("test test", remaining);
        assertEquals("word", consumed);
    }

    @org.junit.Test
    public void testConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("tag test tag1 tag2 tag test tag3");
        String consumed = tokenQueue.consumeTagName();
        String remaining = tokenQueue.toString();
        assertEquals("tag1 tag2 tag test tag3", remaining);
        assertEquals("tag", consumed);
    }

    @org.junit.Test
    public void testConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("tag test tag1 tag:class1 tag2 tag:class2 tag test tag3");
        String consumed = tokenQueue.consumeElementSelector();
        String remaining = tokenQueue.toString();
        assertEquals("tag1 tag:class1 tag2 tag:class2 tag test tag3", remaining);
        assertEquals("tag", consumed);
    }

    @org.junit.Test
    public void testConsumeCssIdentifier() {
        TokenQueue tokenQueue = new TokenQueue("tag id1 id2 id:class1 id:class2 tag");
        String consumed = tokenQueue.consumeCssIdentifier();
        String remaining = tokenQueue.toString();
        assertEquals("tag", remaining);
        assertEquals("id1", consumed);
    }

    @org.junit.Test
    public void testConsumeAttributeKey() {
        TokenQueue tokenQueue = new TokenQueue("tag attr:key1 attr:key2 attr:key3 tag");
        String consumed = tokenQueue.consumeAttributeKey();
        String remaining = tokenQueue.toString();
        assertEquals("tag", remaining);
        assertEquals("attr:key1", consumed);
    }

    @org.junit.Test
    public void testRemainder() {
        TokenQueue tokenQueue = new TokenQueue("test<tag> test<tag1> test<tag2> test<tag3> test<tag4>");
        String remainder = tokenQueue.remainder();
        assertEquals("test<tag4>", tokenQueue.toString());
        assertEquals("test<tag3> test<tag4>", remainder);
    }

    @org.junit.Test
    public void testToString() {
        TokenQueue tokenQueue = new TokenQueue("test<tag> test<tag1> test<tag2> test<tag3> test<tag4>");
        assertEquals("test<tag4>", tokenQueue.toString());
    }
}