import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testTokenQueueInstance() {
        TokenQueue tokenQueue = new TokenQueue("Hello World!");
        Assert.assertFalse("Queue should not be empty", tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("Hello World!");
        Assert.assertEquals('H', tokenQueue.peek());
        tokenQueue.advance();
        Assert.assertEquals('e', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("Hello World!");
        tokenQueue.addFirst('X');
        Assert.assertEquals('X', tokenQueue.consume());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFirstNull() {
        TokenQueue tokenQueue = new TokenQueue("Hello World!");
        tokenQueue.addFirst(null);
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("Hello World!");
        Assert.assertTrue("Should match 'He'", tokenQueue.matches("He"));
        Assert.assertTrue("Should match 'll'", tokenQueue.matches("ll"));
        Assert.assertFalse("Should not match 'Z'", tokenQueue.matches("Z"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("Hello World!");
        Assert.assertTrue("Should match 'He'", tokenQueue.matchesCS("He"));
        Assert.assertFalse("Should not match 'Z'", tokenQueue.matchesCS("Z"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("Hello World!");
        Assert.assertTrue("Should match 'l' or 'r'", tokenQueue.matchesAny('l', 'r'));
        Assert.assertTrue("Should match 'e' or 'o'", tokenQueue.matchesAny('e', 'o'));
        Assert.assertFalse("Should not match 'z'", tokenQueue.matchesAny('z'));
    }

    @Test
    public void testMatchesAnyStringArray() {
        TokenQueue tokenQueue = new TokenQueue("Hello World!");
        Assert.assertTrue("Should match 'He' or 'll'", tokenQueue.matchesAny("He", "ll"));
        Assert.assertFalse("Should not match 'z'", tokenQueue.matchesAny("z"));
    }

    @Test
    public void testMatchesAnyCharArray() {
        TokenQueue tokenQueue = new TokenQueue("Hello World!");
        Assert.assertTrue("Should match 'l' or 'r'", tokenQueue.matchesAny(new char[]{'l', 'r'}));
        Assert.assertFalse("Should not match 'z'", tokenQueue.matchesAny(new char[]{'z'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("<html><head><title>Hello World</title></head><body>");
        Assert.assertTrue("Should match '<html'", tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("Hello World!");
        tokenQueue.matchChomp("He");
        Assert.assertEquals("llo World!", tokenQueue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("   Hello World!");
        Assert.assertTrue("Queue should start with whitespace", tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue tokenQueue = new TokenQueue("Hello World!");
        Assert.assertTrue("Queue should match a word character", tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue tokenQueue = new TokenQueue("Hello World!");
        tokenQueue.advance();
        Assert.assertEquals('e', tokenQueue.consume());
    }

    @Test
    public void testConsume() {
        TokenQueue tokenQueue = new TokenQueue("Hello World!");
        Assert.assertEquals('H', tokenQueue.consume());
        Assert.assertEquals("ello World!", tokenQueue.toString());
    }

    @Test
    public void testConsumeString() {
        TokenQueue tokenQueue = new TokenQueue("Hello World!");
        tokenQueue.consume("ll");
        Assert.assertEquals("Hello W!", tokenQueue.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConsumeNull() {
        TokenQueue tokenQueue = new TokenQueue("Hello World!");
        tokenQueue.consume(null);
    }

    @Test
    public void testConsumeTo() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        String consumed = tokenQueue.consumeTo("(two)");
        Assert.assertEquals("(one three)", tokenQueue.toString());
        Assert.assertEquals("(two)", consumed);
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        String consumed = tokenQueue.consumeToIgnoreCase("Two");
        Assert.assertEquals("(one three) four", tokenQueue.toString());
        Assert.assertEquals("(two)", consumed);
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        String consumed = tokenQueue.consumeToAny("(one", "three");
        Assert.assertEquals("(two) four", tokenQueue.toString());
        Assert.assertEquals("(one", consumed);
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        String data = tokenQueue.chompBalanced('(', ')');
        Assert.assertEquals("one (two) three", data);
    }

    @Test
    public void testUnescape() {
        String escaped = "\\(one \\(two\\) three\\) four";
        String unescaped = TokenQueue.unescape(escaped);
        Assert.assertEquals("(one (two) three) four", unescaped);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("   Hello World!");
        boolean consumed = tokenQueue.consumeWhitespace();
        Assert.assertTrue("Should have consumed whitespace", consumed);
        Assert.assertEquals("Hello World!", tokenQueue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue("Hello World!");
        String consumed = tokenQueue.consumeWord();
        Assert.assertEquals("World!", tokenQueue.toString());
        Assert.assertEquals("Hello", consumed);
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("<html><head><title>Hello World</title></head><body>");
        String consumed = tokenQueue.consumeTagName();
        Assert.assertEquals("html", consumed);
        Assert.assertEquals("headtitlebody", tokenQueue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("div.foo.bar:hover");
        String consumed = tokenQueue.consumeElementSelector();
        Assert.assertEquals("div", consumed);
        Assert.assertEquals(".foo.bar:hover", tokenQueue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tokenQueue = new TokenQueue(".class1 .class2 #id1");
        String consumed = tokenQueue.consumeCssIdentifier();
        Assert.assertEquals("class1", consumed);
        Assert.assertEquals(".class2 #id1", tokenQueue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tokenQueue = new TokenQueue("style='color: red'");
        String consumed = tokenQueue.consumeAttributeKey();
        Assert.assertEquals("style", consumed);
        Assert.assertEquals("='color: red'", tokenQueue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue tokenQueue = new TokenQueue("Hello World!");
        String remainder = tokenQueue.remainder();
        Assert.assertEquals("World!", remainder);
    }

    @Test
    public void testToString() {
        TokenQueue tokenQueue = new TokenQueue("Hello World!");
        Assert.assertEquals("World!", tokenQueue.toString());
    }
}