package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testConstructor() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertEquals(11, reader.length());
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals(0, reader.mark());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertFalse(reader.isEmpty());

        reader.consume();
        reader.consume();
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertEquals('H', reader.current());
        reader.consume();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.consume();
        Assert.assertEquals('e', reader.current());

        reader.consume();
        Assert.assertEquals('l', reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.consume();
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('l', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.advance();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.mark();
        reader.consume();
        Assert.assertEquals('e', reader.current());
        Assert.assertEquals(0, reader.pos());
        reader.rewindToMark();
        Assert.assertEquals('H', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("Hello World");
        String str = reader.consumeAsString();
        Assert.assertEquals("ll", str);
        reader.rewindToMark();
        str = reader.consumeAsString();
        Assert.assertEquals("Hello", str);
    }

    @Test
    public void testNextIndexOf_char() {
        CharacterReader reader = new CharacterReader("abcabc");
        Assert.assertEquals(1, reader.nextIndexOf('b'));
        Assert.assertEquals(-1, reader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOf_CharSequence() {
        CharacterReader reader = new CharacterReader("abcabc");
        Assert.assertEquals(1, reader.nextIndexOf("bc"));
        Assert.assertEquals(3, reader.nextIndexOf("abc"));
        Assert.assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test(expected = AssertionError.class)
    public void testNextIndexOf_char_edgeCase() {
        CharacterReader reader = new CharacterReader("abc");
        int index = reader.nextIndexOf('\uD800');
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testConsumeTo_char() {
        CharacterReader reader = new CharacterReader("HelloWorld");
        String str = reader.consumeTo('l');
        Assert.assertEquals("He", str);
        reader.mark();
        str = reader.consumeTo('l');
        Assert.assertEquals("Hel", str);
        reader.rewindToMark();
        str = reader.consumeTo('l');
        Assert.assertEquals("Hello", str);
    }

    @Test(expected = AssertionError.class)
    public void testConsumeTo_char_edgeCase() {
        CharacterReader reader = new CharacterReader("HelloWorld");
        String str = reader.consumeTo('\uD800');
        Assert.fail(); // should not reach this point
    }

    @Test
    public void testConsumeTo_String() {
        CharacterReader reader = new CharacterReader("HelloWorld");
        String str = reader.consumeTo("ll");
        Assert.assertEquals("HelloWorld", str);
        reader.mark();
        str = reader.consumeTo("ll");
        Assert.assertEquals("HelloWorldll", str);
        reader.rewindToMark();
        str = reader.consumeTo("ll");
        Assert.assertEquals("HelloWorld", str);
    }

    @Test(expected = AssertionError.class)
    public void testConsumeTo_String_edgeCase() {
        CharacterReader reader = new CharacterReader("HelloWorld");
        String str = reader.consumeTo("xyz");
        Assert.fail(); // should not reach this point
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("HelloWorld");
        String str = reader.consumeToAny(new char[]{'l', 'o'});
        Assert.assertEquals("HelloWorld", str);
        reader.mark();
        str = reader.consumeToAny(new char[]{'l', 'o'});
        Assert.assertEquals("HelloWorldll", str);
        reader.rewindToMark();
        str = reader.consumeToAny(new char[]{'l', 'o'});
        Assert.assertEquals("HelloWorld", str);
    }

    @Test(expected = AssertionError.class)
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("HelloWorld");
        String str = reader.consumeToEnd();
        Assert.assertEquals("World", str);
        reader.mark();
        str = reader.consumeToEnd();
        Assert.assertEquals("World", str);
        reader.rewindToMark();
        str = reader.consumeToEnd();
        Assert.assertEquals("HelloWorld", str);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("HelloWorld");
        String str = reader.consumeLetterSequence();
        Assert.assertEquals("Hello", str);
        reader.mark();
        str = reader.consumeLetterSequence();
        Assert.assertEquals("HelloWorld", str);
        reader.rewindToMark();
        str = reader.consumeLetterSequence();
        Assert.assertEquals("Hello", str);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("Hello123World");
        String str = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Hello123", str);
        reader.mark();
        str = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("HelloWorld", str);
        reader.rewindToMark();
        str = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Hello123", str);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("Hello%2FWorld");
        String str = reader.consumeHexSequence();
        Assert.assertEquals("%2F", str);
        reader.mark();
        str = reader.consumeHexSequence();
        Assert.assertEquals("HelloWorld", str);
        reader.rewindToMark();
        str = reader.consumeHexSequence();
        Assert.assertEquals("%2F", str);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("Hello123World");
        String str = reader.consumeDigitSequence();
        Assert.assertEquals("123", str);
        reader.mark();
        str = reader.consumeDigitSequence();
        Assert.assertEquals("HelloWorld", str);
        reader.rewindToMark();
        str = reader.consumeDigitSequence();
        Assert.assertEquals("123", str);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("HelloWorld");
        Assert.assertTrue(reader.matches('l'));
        Assert.assertTrue(reader.matches("ll"));
        Assert.assertFalse(reader.matches('x'));
        Assert.assertFalse(reader.matches("abc"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("HelloWorld");
        Assert.assertTrue(reader.matchesIgnoreCase("l"));
        Assert.assertTrue(reader.matchesIgnoreCase("LL"));
        Assert.assertFalse(reader.matchesIgnoreCase("X"));
        Assert.assertTrue(reader.matchesIgnoreCase("world"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("HelloWorld");
        Assert.assertTrue(reader.matchesAny(new char[]{'l', 'o'}));
        Assert.assertFalse(reader.matchesAny(new char[]{'x'}));
        Assert.assertTrue(reader.matchesAny(new char[]{'l', 'o', 'W'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("HelloWorld");
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("Hello123World");
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("HelloWorld");
        Assert.assertTrue(reader.matchConsume("ll"));
        Assert.assertEquals("ll", reader.consumed);
        reader.mark();
        Assert.assertTrue(reader.matchConsume("ll"));
        Assert.assertEquals("llll", reader.consumed);
        reader.rewindToMark();
        Assert.assertTrue(reader.matchConsume("ll"));
        Assert.assertEquals("ll", reader.consumed);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("HelloWorld");
        Assert.assertTrue(reader.matchConsumeIgnoreCase("ll"));
        Assert.assertEquals("ll", reader.consumed);
        reader.mark();
        Assert.assertTrue(reader.matchConsumeIgnoreCase("LL"));
        Assert.assertEquals("llll", reader.consumed);
        reader.rewindToMark();
        Assert.assertTrue(reader.matchConsumeIgnoreCase("ll"));
        Assert.assertEquals("ll", reader.consumed);
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello<title>World<title/><style>");
        Assert.assertTrue(reader.containsIgnoreCase("/title/"));
        Assert.assertTrue(reader.containsIgnoreCase("title"));
        Assert.assertFalse(reader.containsIgnoreCase("/head/"));
    }
}