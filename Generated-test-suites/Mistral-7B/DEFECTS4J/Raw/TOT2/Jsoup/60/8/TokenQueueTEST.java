import org.junit.Assert;
import org.junit.Test;

import static org.jsoup.parser.TokenQueue.ESC;
import static org.jsoup.parser.TokenQueue.consume;
import static org.jsoup.parser.TokenQueue.consumeCS;
import static org.jsoup.parser.TokenQueue.consumeTo;
import static org.jsoup.parser.TokenQueue.consumeToIgnoreCase;
import static org.jsoup.parser.TokenQueue.matches;
import static org.jsoup.parser.TokenQueue.matchesAny;
import static org.jsoup.parser.TokenQueue.matchesAnyCS;
import static org.jsoup.parser.TokenQueue.matchesCS;
import static org.jsoup.parser.TokenQueue.matchesWord;
import static org.jsoup.parser.TokenQueue.matchesWhitespace;
import static org.jsoup.parser.TokenQueue.peek;

/**
 * Test class for TokenQueue.
 */
public class TokenQueueTest {

    private static final String TEST_DATA = "this is a test <tag> with <escaped> special <characters> and some whitespace";

    @Test
    public void testConstructor() {
        TokenQueue queue = new TokenQueue(TEST_DATA);
        Assert.assertEquals(TEST_DATA, queue.toString());
    }

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());
        
        queue = new TokenQueue(TEST_DATA);
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue(TEST_DATA);
        Assert.assertEquals(TEST_DATA.charAt(0), peek(queue));
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('A');
        Assert.assertEquals("A", queue.toString());
        
        queue = new TokenQueue(TEST_DATA);
        queue.addFirst('B');
        Assert.assertEquals("B" + TEST_DATA.substring(0, 1), queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue(TEST_DATA);
        String seq = "test";
        Assert.assertTrue(matches(queue, seq));
        Assert.assertEquals("test", consume(queue));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue(TEST_DATA);
        String seq = "TAG";
        Assert.assertFalse(matchesCS(queue, seq));
        Assert.assertFalse(consumeCS(queue, seq));
        
        seq = "tag";
        Assert.assertTrue(matchesCS(queue, seq));
        Assert.assertEquals("tag", consumeCS(queue, seq));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue(TEST_DATA);
        String[] seq = {"test", "TAG"};
        Assert.assertTrue(matchesAny(queue, seq));
        Assert.assertEquals("test", consume(queue));
        
        Assert.assertTrue(matchesAny(queue, seq));
        Assert.assertEquals("TAG", consume(queue));
    }

    @Test
    public void testMatchesAnyCS() {
        TokenQueue queue = new TokenQueue(TEST_DATA);
        String[] seq = {"Tag", "tag"};
        Assert.assertTrue(matchesAnyCS(queue, seq));
        Assert.assertEquals("Tag", consumeCS(queue, seq[0]));
        
        Assert.assertTrue(matchesAnyCS(queue, seq));
        Assert.assertEquals("tag", consumeCS(queue, seq[1]));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(TEST_DATA);
        Assert.assertTrue(matchesWhitespace(queue));
        Assert.assertTrue(consumeWhitespace(queue));
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue(TEST_DATA);
        Assert.assertTrue(matchesWord(queue));
        Assert.assertEquals("this", consumeWord(queue));
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue(TEST_DATA);
        Assert.assertEquals("t", consume(queue));
        Assert.assertEquals("his", consume(queue));
        Assert.assertEquals(" ", consume(queue));
        // Test consuming a tag
        Assert.assertTrue(matchesAnyCS(queue, new String[]{"tag"}));
        Assert.assertEquals("<tag", consume(queue));
        Assert.assertEquals("with", consume(queue));
    }

    @Test
    public void testConsumeCS() {
        TokenQueue queue = new TokenQueue(TEST_DATA);
        Assert.assertEquals("T", consumeCS(queue, "Tag"));
        Assert.assertEquals("his", consume(queue));
        Assert.assertEquals(" ", consume(queue));
        // Test consuming a tag
        Assert.assertTrue(matchesCS(queue, "tag"));
        Assert.assertEquals("tag", consumeCS(queue, "tag"));
        Assert.assertEquals("with", consume(queue));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue(TEST_DATA);
        Assert.assertTrue(matchesStartTag(queue));
        Assert.assertEquals("<tag", consume(queue));
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue(TEST_DATA);
        String seq = "<tag>";
        Assert.assertTrue(matches(queue, seq));
        Assert.assertTrue(matchChomp(queue, seq));
        Assert.assertEquals("with", consume(queue));
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue(TEST_DATA);
        Assert.assertEquals("this is a", chompBalanced(queue, ' '));
        Assert.assertEquals("test <tag>", queue.toString());

        Assert.assertEquals("<tag", chompBalanced(queue, '<'));
        Assert.assertEquals("with <escaped> special <characters> and some whitespace", queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeToInvalid() {
        TokenQueue queue = new TokenQueue(TEST_DATA);
        consumeTo(queue, "TAG");
        consumeTo(queue, "TAG");
        consumeTo(queue, "TAG");
        consumeTo(queue, "TAG");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue(TEST_DATA);
        Assert.assertEquals("this is a", consumeTo(queue, " "));
        Assert.assertEquals("<tag", queue.toString());

        Assert.assertEquals("<tag", consumeTo(queue, "<tag>"));
        Assert.assertEquals("with <escaped> special <characters> and some whitespace", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue(TEST_DATA);
        Assert.assertEquals("this is a", consumeToIgnoreCase(queue, " "));
        Assert.assertEquals("<tag", queue.toString());

        Assert.assertEquals("<tag", consumeToIgnoreCase(queue, "<TAG>"));
        Assert.assertEquals("with <escaped> special <characters> and some whitespace", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue(TEST_DATA);
        Assert.assertEquals("this is a test <tag>", consumeToAny(queue, " ", "tag"));
        Assert.assertEquals("with <escaped> special <characters> and some whitespace", queue.toString());
    }

    @Test
    public void testChompTo() {
        TokenQueue queue = new TokenQueue(TEST_DATA);
        String seq = "<tag>";
        Assert.assertEquals("this is a", chompTo(queue, seq));
        Assert.assertEquals("with <escaped> special <characters> and some whitespace", queue.toString());
    }

    @Test
    public void testChompToIgnoreCase() {
        TokenQueue queue = new TokenQueue(TEST_DATA);
        String seq = "<TAG>";
        Assert.assertEquals("this is a", chompToIgnoreCase(queue, seq));
        Assert.assertEquals("with <escaped> special <characters> and some whitespace", queue.toString());
    }

    @Test
    public void testAddFirstString() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst("Hello");
        Assert.assertEquals("Hello", queue.toString());
    }

    @Test
    public void testAddFirstCharacter() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('H');
        Assert.assertEquals("H", queue.toString());
    }

    @Test
    public void testUnescape() {
        String testDataEscaped = "this is a test \\<tag> with \\<escaped> special \\<characters> and some whitespace";
        String result = unescape(testDataEscaped);
        Assert.assertEquals(TEST_DATA, result);
    }
}