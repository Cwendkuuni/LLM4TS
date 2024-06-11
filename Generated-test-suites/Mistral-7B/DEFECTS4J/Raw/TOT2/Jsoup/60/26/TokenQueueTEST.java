import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());

        queue = new TokenQueue("abc");
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abc");
        Assert.assertEquals('a', queue.peek());
        queue.advance();
        Assert.assertEquals('b', queue.peek());
        queue.consume();
        Assert.assertEquals('c', queue.peek());
        Assert.assertEquals("", queue.consumeTo(""));
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('x');
        Assert.assertEquals('x', queue.peek());
        queue.addFirst("hello");
        Assert.assertEquals('h', queue.peek());
    }

    @Test(expected = NullPointerException.class)
    public void testAddFirstNullString() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst(null);
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertTrue(queue.matches("ab"));
        Assert.assertTrue(queue.matches("bcde"));
        Assert.assertFalse(queue.matches("efg"));
        Assert.assertFalse(queue.matches("abcdefg"));
        queue.consume("abc");
        Assert.assertFalse(queue.matches("abc"));
        Assert.assertFalse(queue.matches("efg"));
        Assert.assertTrue(queue.matches("def"));
        Assert.assertTrue(queue.matches(""));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCDEF");
        Assert.assertTrue(queue.matchesCS("AB"));
        Assert.assertTrue(queue.matchesCS("BCDE"));
        Assert.assertFalse(queue.matchesCS("efg"));
        Assert.assertFalse(queue.matchesCS("abcdefg"));
        queue.consume("ABC");
        Assert.assertFalse(queue.matchesCS("ABC"));
        Assert.assertFalse(queue.matchesCS("efg"));
        Assert.assertTrue(queue.matchesCS("DEF"));
        Assert.assertTrue(queue.matchesCS(""));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertTrue(queue.matchesAny("ab", "bc"));
        Assert.assertTrue(queue.matchesAny("ef", "cd"));
        Assert.assertFalse(queue.matchesAny("gh"));
        Assert.assertTrue(queue.matchesAny("", ""));
        queue.consume("abc");
        Assert.assertTrue(queue.matchesAny("def", ""));
        Assert.assertFalse(queue.matchesAny("gh"));
        Assert.assertFalse(queue.matchesAny("", "gh"));
    }

    @Test
    public void testMatchesAnyChars() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertTrue(queue.matchesAny('a', 'b', 'c'));
        Assert.assertTrue(queue.matchesAny('e', 'f'));
        Assert.assertFalse(queue.matchesAny('g', 'h'));
        Assert.assertTrue(queue.matchesAny(""));

        queue.consume("abc");
        Assert.assertTrue(queue.matchesAny('d', 'e', 'f'));
        Assert.assertFalse(queue.matchesAny('g', 'h'));
        Assert.assertFalse(queue.matchesAny("", 'g'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head><body>");
        Assert.assertTrue(queue.matchesStartTag());
        Assert.assertFalse(queue.matchesStartTag());
        queue.consume("<html>");
        queue.consume("<head>");
        Assert.assertFalse(queue.matchesStartTag());
        queue.consume("<title>");
        Assert.assertTrue(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcdefg");
        Assert.assertFalse(queue.matchChomp(""));
        Assert.assertFalse(queue.matchChomp("efg"));
        Assert.assertTrue(queue.matchChomp("ab"));
        Assert.assertTrue(queue.matchChomp("cd"));
        queue.consume("abcd");
        Assert.assertFalse(queue.matchChomp("ab"));
        Assert.assertFalse(queue.matchChomp("efg"));
        Assert.assertTrue(queue.matchChomp("ef"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   abc   ");
        Assert.assertTrue(queue.matchesWhitespace());
        queue.advance();
        Assert.assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abc def ghi jkl");
        Assert.assertTrue(queue.matchesWord());
        Assert.assertTrue(queue.matchesWord());
        Assert.assertTrue(queue.matchesWord());
        queue.consume("abc");
        Assert.assertTrue(queue.matchesWord());
        Assert.assertTrue(queue.matchesWord());
        Assert.assertTrue(queue.matchesWord());
        queue.consume("def");
        Assert.assertTrue(queue.matchesWord());
        Assert.assertTrue(queue.matchesWord());
        Assert.assertTrue(queue.matchesWord());
        queue.consume("ghi");
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
        queue.consume();
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abc");
        String data = queue.consume(2);
        Assert.assertEquals("c", data);
        Assert.assertEquals("", queue.toString());

        queue = new TokenQueue("abcd");
        data = queue.consume("ab");
        Assert.assertEquals("cd", data);
        Assert.assertEquals("", queue.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConsumeTooShort() {
        TokenQueue queue = new TokenQueue("abc");
        queue.consume("ab");
        queue.consume("cd");
        queue.consume("ef");
        queue.consume("xyz");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abcdef");
        String data = queue.consumeTo("bc");
        Assert.assertEquals("abc", data);
        Assert.assertEquals("def", queue.toString());

        queue = new TokenQueue("abcdefg");
        data = queue.consumeTo("efg");
        Assert.assertEquals("abcdef", data);
        Assert.assertEquals("", queue.toString());

        queue = new TokenQueue("abcdefg");
        data = queue.consumeTo("xyz");
        Assert.assertEquals("", data);
        Assert.assertEquals("abcdefg", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("ABCDEFG");
        String data = queue.consumeToIgnoreCase("BC");
        Assert.assertEquals("ABC", data);
        Assert.assertEquals("DEFG", queue.toString());

        queue = new TokenQueue("ABCDEFG");
        data = queue.consumeToIgnoreCase("EFG");
        Assert.assertEquals("ABCDEF", data);
        Assert.assertEquals("", queue.toString());

        queue = new TokenQueue("ABCDEFG");
        data = queue.consumeToIgnoreCase("XYZ");
        Assert.assertEquals("", data);
        Assert.assertEquals("ABCDEFG", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abcdef");
        String data = queue.consumeToAny("ab", "cd");
        Assert.assertEquals("abc", data);
        Assert.assertEquals("def", queue.toString());

        queue = new TokenQueue("abcdefg");
        data = queue.consumeToAny("efg", "xyz");
        Assert.assertEquals("abcdef", data);
        Assert.assertEquals("", queue.toString());

        queue = new TokenQueue("abcdefg");
        data = queue.consumeToAny("xyz", "xyz");
        Assert.assertEquals("", data);
        Assert.assertEquals("abcdefg", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("((())())");
        String data = queue.chompBalanced('(', ')');
        Assert.assertEquals("()()", data);
        Assert.assertEquals("()()", queue.toString());

        queue = new TokenQueue("((())())");
        data = queue.chompBalanced('(', '(');
        Assert.assertEquals("((())", data);
        Assert.assertEquals("()()", queue.toString());

        queue = new TokenQueue("((())())");
        data = queue.chompBalanced('(', '[');
        Assert.assertEquals("", data);
        Assert.assertEquals("((())())", queue.toString());

        queue = new TokenQueue("((())())");
        data = queue.chompBalanced(']', ')');
        Assert.assertEquals("", data);
        Assert.assertEquals("((())())", queue.toString());

        queue = new TokenQueue("((())())");
        data = queue.chompBalanced('{', '}');
        Assert.assertEquals("", data);
        Assert.assertEquals("((())())", queue.toString());
    }

    @Test
    public void testUnescape() {
        Assert.assertEquals("", TokenQueue.unescape(""));
        Assert.assertEquals("\\", TokenQueue.unescape("\\"));
        Assert.assertEquals("\\\\", TokenQueue.unescape("\\\\"));
        Assert.assertEquals("abc", TokenQueue.unescape("abc"));
        Assert.assertEquals("def\\ghi", TokenQueue.unescape("def\\ghi"));
        Assert.assertEquals("def\\\\ghi", TokenQueue.unescape("def\\\\ghi"));
        Assert.assertEquals("def\\\\\\ghi", TokenQueue.unescape("def\\\\\\\\ghi"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" abc def ");
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertEquals(" def ", queue.toString());

        queue = new TokenQueue(" abc def ");
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertEquals(" def ", queue.toString());

        queue = new TokenQueue(" abc def ");
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertEquals(" def ", queue.toString());

        queue = new TokenQueue(" abc def ");
        Assert.assertFalse(queue.consumeWhitespace());
        Assert.assertEquals(" def ", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue(" abc def ghi jkl ");
        Assert.assertEquals("abc", queue.consumeWord());
        Assert.assertEquals("def ghi jkl", queue.toString());

        queue = new TokenQueue(" abc def ghi jkl ");
        Assert.assertEquals("def", queue.consumeWord());
        Assert.assertEquals("ghi jkl", queue.toString());

        queue = new TokenQueue(" abc def ghi jkl ");
        Assert.assertEquals("ghi", queue.consumeWord());
        Assert.assertEquals("jkl", queue.toString());

        queue = new TokenQueue(" abc def ghi jkl ");
        Assert.assertEquals("jkl", queue.consumeWord());
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue(" <html><head><title>Test</title></head><body>");
        Assert.assertEquals("html", queue.consumeTagName());
        Assert.assertEquals("<head><title>Test</title></head><body>", queue.toString());

        queue = new TokenQueue("<html><head><title>Test</title></head><body>");
        Assert.assertEquals("head", queue.consumeTagName());
        Assert.assertEquals("<title>Test</title></head><body>", queue.toString());

        queue = new TokenQueue("<html><head><title>Test</title></head><body>");
        Assert.assertEquals("title", queue.consumeTagName());
        Assert.assertEquals("Test", queue.consumeWord());
        Assert.assertEquals("</title><body>", queue.toString());

        queue = new TokenQueue("<html><head><title>Test</title></head><body>");
        Assert.assertEquals("body", queue.consumeTagName());
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("<html><head><link rel=\"stylesheet\" href=\"styles.css\"/>");
        Assert.assertEquals("link", queue.consumeElementSelector());
        Assert.assertEquals(" rel=\"stylesheet\" href=\"styles.css\"", queue.toString());

        queue = new TokenQueue("<html><head><link rel=\"stylesheet\" href=\"styles.css\"/>");
        Assert.assertEquals("link rel=\"stylesheet\"", queue.consumeElementSelector());
        Assert.assertEquals(" href=\"styles.css\"", queue.toString());

        queue = new TokenQueue("<html><head><link rel=\"stylesheet\" href=\"styles.css\"/>");
        Assert.assertEquals("href", queue.consumeElementSelector());
        Assert.assertEquals("=\"styles.css\"", queue.toString());

        queue = new TokenQueue("<html><head><link rel=\"stylesheet\" href=\"styles.css\"/>");
        Assert.assertEquals("styles.css", queue.consumeElementSelector());
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue(" class=\"class1 class2 id=\"id1\" id=\"id2\"");
        Assert.assertEquals("class1", queue.consumeCssIdentifier());
        Assert.assertEquals(" class2 id=\"id1\" id=\"id2\"", queue.toString());

        queue = new TokenQueue(" class=\"class1 class2 id=\"id1\" id=\"id2\"");
        Assert.assertEquals("class2", queue.consumeCssIdentifier());
        Assert.assertEquals(" id=\"id1\" id=\"id2\"", queue.toString());

        queue = new TokenQueue(" class=\"class1 class2 id=\"id1\" id=\"id2\"");
        Assert.assertEquals("id1", queue.consumeCssIdentifier());
        Assert.assertEquals(" id=\"id2\"", queue.toString());

        queue = new TokenQueue(" class=\"class1 class2 id=\"id1\" id=\"id2\"");
        Assert.assertEquals("id2", queue.consumeCssIdentifier());
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue(" rel=\"stylesheet\" href=\"styles.css\"");
        Assert.assertEquals("rel", queue.consumeAttributeKey());
        Assert.assertEquals(" href=\"styles.css\"", queue.toString());

        queue = new TokenQueue(" rel=\"stylesheet\" href=\"styles.css\"");
        Assert.assertEquals("href", queue.consumeAttributeKey());
        Assert.assertEquals("", queue.toString());
    }
}