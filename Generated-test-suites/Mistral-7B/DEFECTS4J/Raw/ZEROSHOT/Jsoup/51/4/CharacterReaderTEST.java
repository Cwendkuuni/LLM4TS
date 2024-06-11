package org.jsoup.parser;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789<>& ";
        reader = new CharacterReader(input);
    }

    @After
    public void tearDown() {
        reader = null;
    }

    @Test
    public void testIsEmpty() {
        Assert.assertFalse(reader.isEmpty());
        reader.consumeToEnd();
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        char expected = reader.current();
        Assert.assertEquals('a', expected);
        reader.consume();
        expected = reader.current();
        Assert.assertEquals('b', expected);
    }

    @Test
    public void testConsume() {
        reader.consume();
        Assert.assertEquals('b', reader.current());
        reader.consume();
        Assert.assertEquals('c', reader.current());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        Assert.assertEquals('b', reader.current());
    }

    @Test
    public void testMark() {
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        String expected = reader.consumeAsString();
        Assert.assertEquals("a", expected);
        reader.consume();
        expected = reader.consumeAsString();
        Assert.assertEquals("b", expected);
    }

    @Test
    public void testNextIndexOf() {
        Assert.assertEquals(-1, reader.nextIndexOf('z'));
        Assert.assertEquals(0, reader.nextIndexOf('a'));
        Assert.assertEquals(1, reader.nextIndexOf('b'));
        Assert.assertEquals(25, reader.nextIndexOf('Z'));
        reader.consumeTo('a');
        Assert.assertEquals(-1, reader.nextIndexOf('a'));
        reader.consumeTo('Z');
        Assert.assertEquals(-1, reader.nextIndexOf('Z'));
    }

    @Test
    public void testConsumeTo() {
        String expected = reader.consumeTo('a');
        Assert.assertEquals("a", expected);
        reader.consume();
        expected = reader.consumeTo('a');
        Assert.assertEquals("a", expected);
        expected = reader.consumeTo('z');
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyz", expected);
        expected = reader.consumeTo("xyz");
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789<>& ", expected);
    }

    @Test
    public void testConsumeToAny() {
        String expected = reader.consumeToAny(new char[]{'a', 'b'});
        Assert.assertEquals("a", expected);
        reader.consume();
        expected = reader.consumeToAny(new char[]{'a', 'b'});
        Assert.assertEquals("a", expected);
        expected = reader.consumeToAny(new char[]{'z', 'x'});
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789<>& ", expected);
    }

    @Test
    public void testConsumeToAnySorted() {
        String expected = reader.consumeToAnySorted(new char[]{'z', 'x'});
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789<>& ", expected);
        expected = reader.consumeToAnySorted(new char[]{'a', 'b'});
        Assert.assertEquals("a", expected);
        reader.consume();
        expected = reader.consumeToAnySorted(new char[]{'a', 'b'});
        Assert.assertEquals("a", expected);
    }

    @Test
    public void testConsumeData() {
        String expected = reader.consumeData();
        Assert.assertEquals("&, <, null", expected);
        reader.consume();
        expected = reader.consumeData();
        Assert.assertEquals("&, <, null", expected);
    }

    @Test
    public void testConsumeTagName() {
        String expected = reader.consumeTagName();
        Assert.assertEquals("\\t, \\n, \\r, \\f, ", expected);
        reader.consume();
        expected = reader.consumeTagName();
        Assert.assertEquals("/, >, null", expected);
    }

    @Test
    public void testConsumeToEnd() {
        String expected = reader.consumeToEnd();
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789<>& ", expected);
    }

    @Test
    public void testConsumeLetterSequence() {
        String expected = reader.consumeLetterSequence();
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyz", expected);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String expected = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("ab0123456789", expected);
    }

    @Test
    public void testMatches() {
        Assert.assertTrue(reader.matches('a'));
        Assert.assertFalse(reader.matches('z'));
        reader.consume();
        Assert.assertTrue(reader.matches('b'));
        Assert.assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesString() {
        Assert.assertTrue(reader.matches("ab"));
        Assert.assertFalse(reader.matches("xyz"));
        reader.consumeTo('a');
        reader.consumeTo('b');
        Assert.assertTrue(reader.matches("ab"));
        reader.consumeTo("xyz");
        Assert.assertFalse(reader.matches("xyz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        Assert.assertTrue(reader.matchesIgnoreCase("Ab"));
        Assert.assertFalse(reader.matchesIgnoreCase("xyz"));
        reader.consumeTo('a');
        reader.consumeTo('b');
        Assert.assertTrue(reader.matchesIgnoreCase("Ab"));
        reader.consumeTo("xyz");
        Assert.assertFalse(reader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testMatchesAny() {
        Assert.assertTrue(reader.matchesAny(new char[]{'a', 'b'}));
        Assert.assertFalse(reader.matchesAny(new char[]{'z', 'x'}));
        reader.consumeTo('a');
        reader.consumeTo('b');
        Assert.assertTrue(reader.matchesAny(new char[]{'a', 'b'}));
        reader.consumeTo("xyz");
        Assert.assertFalse(reader.matchesAny(new char[]{'z', 'x'}));
    }

    @Test
    public void testMatchesAnySorted() {
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'a', 'b'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'z', 'x'}));
        reader.consumeTo('a');
        reader.consumeTo('b');
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'a', 'b'}));
        reader.consumeTo("xyz");
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'z', 'x'}));
    }

    @Test
    public void testMatchesLetter() {
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
        reader.consume();
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
        reader.consume();
        reader.consume();
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsumeString() {
        String expected = reader.consumeTo("ab");
        Assert.assertEquals("ab", expected);
        reader.consume();
        expected = reader.consumeTo("ab");
        Assert.assertEquals("ab", expected);
        reader.consumeTo("xyz");
        Assert.assertEquals("", reader.consumeTo("ab"));
    }

    @Test
    public void testMatchConsumeIgnoreCaseString() {
        String expected = reader.consumeToIgnoreCase("Ab");
        Assert.assertEquals("Ab", expected);
        reader.consume();
        expected = reader.consumeToIgnoreCase("Ab");
        Assert.assertEquals("Ab", expected);
        reader.consumeToIgnoreCase("xyz");
        Assert.assertEquals("", reader.consumeToIgnoreCase("Ab"));
    }

    @Test
    public void testContainsIgnoreCase() {
        Assert.assertFalse(reader.containsIgnoreCase("xyz"));
        reader.consumeTo('a');
        reader.consumeTo('b');
        Assert.assertTrue(reader.containsIgnoreCase("ab"));
        reader.consumeToIgnoreCase("xyz");
        Assert.assertFalse(reader.containsIgnoreCase("xyz"));
    }

    @Test
    public void testToString() {
        String expected = reader.toString();
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789<>& ", expected);
        reader.consumeToEnd();
        expected = reader.toString();
        Assert.assertEquals("", expected);
    }
}