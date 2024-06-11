package org.jsoup.parser;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+-=[]{};:<>,./? ";
        reader = new CharacterReader(new StringReader(input));
    }

    @After
    public void tearDown() {
        reader.close();
    }

    @Test
    public void testIsEmpty() {
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testPos() {
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testConsume() {
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.consume());
        // check that isEmpty returns true after all characters have been consumed
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        Assert.assertEquals('b', reader.current());
    }

    @Test
    public void testNextIndexOf() {
        Assert.assertEquals(0, reader.nextIndexOf('a'));
        Assert.assertEquals(1, reader.nextIndexOf('b'));
        Assert.assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfString() {
        Assert.assertEquals(0, reader.nextIndexOf("ab"));
        Assert.assertEquals(11, reader.nextIndexOf("AB"));
        Assert.assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test
    public void testConsumeTo() {
        String expected = "abc";
        String actual = reader.consumeTo('c');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToString() {
        String expected = "defghijkl";
        String actual = reader.consumeTo("ijkl");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAny() {
        String expected = "0123456789!@#$%^&*()_+-=[]{};:<>,./? ";
        String actual = reader.consumeToAny(new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '-', '=', '[', ']', ':', '<', '>', '>', '.', '/', '?', ' '});
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAnySorted() {
        String expected = "0123456789!@#$%^&*()_+-=[]{};:<>,./? ";
        String actual = reader.consumeToAnySorted(new char[]{' ', '@', '(', '!', '.', '/', '?', '+', '-', '[', ']', '_', '=', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'});
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeData() {
        String expected = "&<";
        String actual = reader.consumeData();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTagName() {
        String expected = "<title>";
        String actual = reader.consumeTagName();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToEnd() {
        String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String actual = reader.consumeToEnd();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterSequence() {
        String expected = "abcdef";
        String actual = reader.consumeLetterSequence();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String expected = "ab012";
        String actual = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeHexSequence() {
        String expected = "ABcdEf";
        String actual = reader.consumeHexSequence();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeDigitSequence() {
        String expected = "123456";
        String actual = reader.consumeDigitSequence();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        Assert.assertTrue(reader.matches('a'));
        Assert.assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesString() {
        Assert.assertTrue(reader.matches("ab"));
        Assert.assertFalse(reader.matches("xyz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        Assert.assertTrue(reader.matchesIgnoreCase("Ab"));
        Assert.assertFalse(reader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testMatchesAny() {
        Assert.assertTrue(reader.matchesAny('a'));
        Assert.assertTrue(reader.matchesAny('b'));
        Assert.assertTrue(reader.matchesAny(' '));
        Assert.assertFalse(reader.matchesAny('z'));
    }

    @Test
    public void testMatchesAnySorted() {
        Assert.assertTrue(reader.matchesAnySorted('0'));
        Assert.assertTrue(reader.matchesAnySorted('1'));
        Assert.assertTrue(reader.matchesAnySorted(' '));
        Assert.assertTrue(reader.matchesAnySorted('9'));
        Assert.assertFalse(reader.matchesAnySorted('z'));
    }

    @Test
    public void testMatchesLetter() {
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter('0'));
    }

    @Test
    public void testMatchesDigit() {
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit('1'));
    }

    @Test
    public void testMatchConsumeString() {
        String expected = "ab";
        reader.matchConsume("ab");
        String actual = reader.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsumeIgnoreCaseString() {
        String expected = "AB";
        reader.matchConsumeIgnoreCase("ab");
        String actual = reader.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testContainsIgnoreCase() {
        Assert.assertTrue(reader.containsIgnoreCase("</title>"));
        Assert.assertTrue(reader.containsIgnoreCase("</style>"));
        Assert.assertFalse(reader.containsIgnoreCase("</head>"));
    }
}