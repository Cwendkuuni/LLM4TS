package org.jsoup.parser;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

public class CharacterReaderTest {
    private CharacterReader characterReader;

    @Before
    public void setUp() {
        String input = "abcd123e<>&abc&def<1234>";
        characterReader = new CharacterReader(input);
    }

    @After
    public void tearDown() {
        characterReader = null;
    }

    @Test
    public void testIsEmpty() {
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testPos() {
        Assert.assertEquals(0, characterReader.pos());
    }

    @Test
    public void testConsume() {
        Assert.assertEquals('a', characterReader.consume());
        Assert.assertEquals(1, characterReader.pos());
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        Assert.assertEquals('a', characterReader.consume());
        characterReader.unconsume();
        Assert.assertEquals('a', characterReader.current());
        Assert.assertEquals(1, characterReader.pos());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        Assert.assertEquals('b', characterReader.current());
        Assert.assertEquals(1, characterReader.pos());
    }

    @Test
    public void testMark() {
        characterReader.mark();
        Assert.assertEquals(1, characterReader.pos());
        Assert.assertEquals('b', characterReader.current());

        characterReader.consume();
        Assert.assertEquals('c', characterReader.current());
        Assert.assertEquals(2, characterReader.pos());

        characterReader.rewindToMark();
        Assert.assertEquals('b', characterReader.current());
        Assert.assertEquals(1, characterReader.pos());
    }

    @Test
    public void testConsumeAsString() {
        String string = characterReader.consumeAsString();
        Assert.assertEquals("a", string);
        Assert.assertEquals(1, characterReader.pos());
    }

    @Test
    public void testNextIndexOf() {
        Assert.assertEquals(1, characterReader.nextIndexOf('b'));
        Assert.assertEquals(5, characterReader.nextIndexOf('e'));
        Assert.assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfWithSequence() {
        Assert.assertEquals(5, characterReader.nextIndexOf("def"));
        Assert.assertEquals(-1, characterReader.nextIndexOf("xyz"));
    }

    @Test
    public void testConsumeTo() {
        String string = characterReader.consumeTo('d');
        Assert.assertEquals("abcd", string);
        Assert.assertEquals(5, characterReader.pos());
    }

    @Test
    public void testConsumeToWithSequence() {
        String string = characterReader.consumeTo("def");
        Assert.assertEquals("abcd123e<>&", string);
        Assert.assertEquals(11, characterReader.pos());
    }

    @Test
    public void testConsumeToAny() {
        String string = characterReader.consumeToAny('a', 'b');
        Assert.assertEquals("a", string);
        Assert.assertEquals(1, characterReader.pos());

        string = characterReader.consumeToAny('a', 'z');
        Assert.assertEquals("abcd123e<>&", string);
        Assert.assertEquals(14, characterReader.pos());
    }

    @Test
    public void testConsumeToAnySorted() {
        String string = characterReader.consumeToAnySorted(new char[]{'a', 'e'});
        Assert.assertEquals("a", string);
        Assert.assertEquals(1, characterReader.pos());

        string = characterReader.consumeToAnySorted(new char[]{'a', 'z'});
        Assert.assertEquals("abcd123e<>&", string);
        Assert.assertEquals(14, characterReader.pos());
    }

    @Test
    public void testConsumeData() {
        String string = characterReader.consumeData();
        Assert.assertEquals("abcd123e<>&", string);
        Assert.assertEquals(14, characterReader.pos());
    }

    @Test
    public void testConsumeTagName() {
        String string = characterReader.consumeTagName();
        Assert.assertEquals("abcd", string);
        Assert.assertEquals(5, characterReader.pos());
    }

    @Test
    public void testConsumeLetterSequence() {
        String string = characterReader.consumeLetterSequence();
        Assert.assertEquals("abcd", string);
        Assert.assertEquals(5, characterReader.pos());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String string = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("abcd123", string);
        Assert.assertEquals(9, characterReader.pos());
    }

    @Test
    public void testConsumeHexSequence() {
        String string = characterReader.consumeHexSequence();
        Assert.assertEquals("123", string);
        Assert.assertEquals(7, characterReader.pos());
    }

    @Test
    public void testConsumeDigitSequence() {
        String string = characterReader.consumeDigitSequence();
        Assert.assertEquals("1234", string);
        Assert.assertEquals(11, characterReader.pos());
    }

    @Test
    public void testMatches() {
        Assert.assertTrue(characterReader.matches('d'));
        Assert.assertFalse(characterReader.matches('z'));
    }

    @Test
    public void testMatchesWithSequence() {
        Assert.assertTrue(characterReader.matches("def"));
        Assert.assertFalse(characterReader.matches("xyz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        Assert.assertTrue(characterReader.matchesIgnoreCase("def"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("DEF"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testMatchesAny() {
        Assert.assertTrue(characterReader.matchesAny('d', 'e'));
        Assert.assertFalse(characterReader.matchesAny('z', 'e'));
    }

    @Test
    public void testMatchesAnySorted() {
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'d', 'e'}));
        Assert.assertFalse(characterReader.matchesAnySorted(new char[]{'z', 'e'}));
    }

    @Test
    public void testMatchesLetter() {
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter('0'));
    }

    @Test
    public void testMatchesDigit() {
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertFalse(characterReader.matchesDigit('a'));
    }

    @Test
    public void testMatchConsume() {
        Assert.assertTrue(characterReader.matchConsume("def"));
        Assert.assertEquals(7, characterReader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("def"));
        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("DEF"));
        Assert.assertEquals(7, characterReader.pos());
    }

    @Test
    public void testContainsIgnoreCase() {
        Assert.assertTrue(characterReader.containsIgnoreCase("def"));
        Assert.assertTrue(characterReader.containsIgnoreCase("DEF"));
        Assert.assertFalse(characterReader.containsIgnoreCase("xyz"));
    }
}