package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    private CharacterReader characterReader;
    private String testInput = "Hello World! <html><head><title>Test Title</title></head><body>Test Body</body></html>";

    @Before
    public void setup() {
        characterReader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(testInput.getBytes())));
    }

    @Test
    public void testCharacterReaderConstructor() {
        Assert.assertNotNull(characterReader);
    }

    @Test
    public void testPos() {
        Assert.assertEquals(0, characterReader.pos());

        characterReader.advance();
        Assert.assertEquals(1, characterReader.pos());

        characterReader.consume();
        Assert.assertEquals(2, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        Assert.assertFalse(characterReader.isEmpty());

        characterReader.consumeAll(); // consume all characters
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        Assert.assertEquals('H', characterReader.current());
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('l', characterReader.current());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumePastEnd() {
        characterReader.consumeAll();
        characterReader.consume();
    }

    @Test
    public void testConsume() {
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());

        characterReader.consume();
        Assert.assertEquals('l', characterReader.current());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());

        characterReader.consume();
        characterReader.advance();
        Assert.assertEquals('l', characterReader.current());
    }

    @Test
    public void testMark() {
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        Assert.assertEquals(13, characterReader.nextIndexOf('<'));
        Assert.assertEquals(-1, characterReader.nextIndexOf('Z'));

        characterReader.consumeAll();
        Assert.assertEquals(-1, characterReader.nextIndexOf('<'));
    }

    @Test
    public void testNextIndexOfWithCharSequence() {
        Assert.assertEquals(32, characterReader.nextIndexOf("<html>"));
        Assert.assertEquals(-1, characterReader.nextIndexOf("<body>"));

        characterReader.consumeAll();
        Assert.assertEquals(-1, characterReader.nextIndexOf("<html>"));
    }

    @Test
    public void testConsumeTo() {
        String expected = "llo Wor";
        String actual = characterReader.consumeTo(' ');
        Assert.assertEquals(expected, actual);

        characterReader.consumeAll();
        Assert.assertEquals("", characterReader.consumeTo(' '));
    }

    @Test
    public void testConsumeToWithString() {
        String expected = "llo Wor";
        String actual = characterReader.consumeTo(" Wor");
        Assert.assertEquals(expected, actual);

        characterReader.consumeAll();
        Assert.assertEquals("", characterReader.consumeTo(" Wor"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConsumeToWithEmptyString() {
        characterReader.consumeTo("");
    }

    @Test
    public void testConsumeToAny() {
        String expected = "Hello World! <html><head><title>Test Title</title></head><body>Test Body</body></html>";
        String actual = characterReader.consumeToAny(',', '.');
        Assert.assertEquals(expected.length() - 1, actual.length());
        Assert.assertFalse(actual.contains("!"));
        Assert.assertFalse(actual.contains("<html>"));
        Assert.assertTrue(actual.contains("World"));

        characterReader.consumeAll();
        Assert.assertEquals("", characterReader.consumeToAny(',', '.'));
    }

    @Test
    public void testConsumeToAnySorted() {
        String expected = "Hello World! <html><head><title>Test Title</title></head><body>Test Body</body></html>";
        String actual = characterReader.consumeToAnySorted(',', '.', '>');
        Assert.assertEquals(expected.length() - 1, actual.length());
        Assert.assertFalse(actual.contains("!"));
        Assert.assertFalse(actual.contains("<html>"));
        Assert.assertTrue(actual.contains("World"));

        characterReader.consumeAll();
        Assert.assertEquals("", characterReader.consumeToAnySorted(',', '.', '>'));
    }

    @Test
    public void testConsumeData() {
        String expected = "Hello World! <html><head><title>Test Title</title></head><body>Test Body</body></html>";
        String actual = characterReader.consumeData();
        Assert.assertEquals(expected, actual);

        characterReader.consumeAll();
        Assert.assertEquals("", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        String expected = "html";
        String actual = characterReader.consumeTagName();
        Assert.assertEquals(expected, actual);

        characterReader.consumeAll();
        Assert.assertEquals("", characterReader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        String expected = "Hello World! <html><head><title>Test Title</title></head><body>Test Body</body></html>";
        String actual = characterReader.consumeToEnd();
        Assert.assertEquals(expected, actual);

        characterReader.consumeAll();
        Assert.assertEquals("", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        String expected = "llo Wor";
        String actual = characterReader.consumeLetterSequence();
        Assert.assertEquals(expected, actual);

        characterReader.consumeAll();
        Assert.assertEquals("", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String expected = "llo123 Wor";
        String actual = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals(expected, actual);

        characterReader.consumeAll();
        Assert.assertEquals("", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testMatches() {
        Assert.assertTrue(characterReader.matches('l'));
        Assert.assertTrue(characterReader.matches("ll"));

        characterReader.consumeAll();
        Assert.assertFalse(characterReader.matches('l'));
        Assert.assertFalse(characterReader.matches("ll"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        Assert.assertTrue(characterReader.matchesIgnoreCase("LL"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("ll"));

        characterReader.consumeAll();
        Assert.assertFalse(characterReader.matchesIgnoreCase("LL"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("ll"));
    }

    @Test
    public void testMatchesAny() {
        Assert.assertTrue(characterReader.matchesAny('l', 'o'));
        Assert.assertTrue(characterReader.matchesAny('l', 'o', 'w'));

        characterReader.consumeAll();
        Assert.assertFalse(characterReader.matchesAny('l', 'o'));
        Assert.assertFalse(characterReader.matchesAny('l', 'o', 'w'));
    }

    @Test
    public void testMatchesAnySorted() {
        Assert.assertTrue(characterReader.matchesAnySorted('l', 'o', 'w'));
        Assert.assertTrue(characterReader.matchesAnySorted('l', 'w', 'o'));

        characterReader.consumeAll();
        Assert.assertFalse(characterReader.matchesAnySorted('l', 'o', 'w'));
        Assert.assertFalse(characterReader.matchesAnySorted('l', 'w', 'o'));
    }

    @Test
    public void testMatchesLetter() {
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertTrue(characterReader.matchesLetter());

        characterReader.consumeAll();
        Assert.assertFalse(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        Assert.assertFalse(characterReader.matchesDigit());
        characterReader.consume('0');
        Assert.assertTrue(characterReader.matchesDigit());

        characterReader.consumeAll();
        Assert.assertFalse(characterReader.matchesDigit());
        Assert.assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        String expected = "llo Wor";
        String actual = characterReader.toString();
        Assert.assertEquals(expected, actual);

        characterReader.matchConsume("llo");
        Assert.assertEquals(" Wor", characterReader.toString());

        characterReader.consumeAll();
        Assert.assertEquals("", characterReader.toString());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String expected = "llo Wor";
        String actual = characterReader.toString();
        Assert.assertEquals(expected, actual);

        characterReader.matchConsumeIgnoreCase("llo");
        Assert.assertEquals(" Wor", characterReader.toString());

        characterReader.consumeAll();
        Assert.assertEquals("", characterReader.toString());
    }

    @Test
    public void testContainsIgnoreCase() {
        Assert.assertTrue(characterReader.containsIgnoreCase("html"));
        Assert.assertTrue(characterReader.containsIgnoreCase("head"));
        Assert.assertTrue(characterReader.containsIgnoreCase("title"));
        Assert.assertTrue(characterReader.containsIgnoreCase("body"));

        characterReader.consumeAll();
        Assert.assertFalse(characterReader.containsIgnoreCase("html"));
        Assert.assertFalse(characterReader.containsIgnoreCase("head"));
        Assert.assertFalse(characterReader.containsIgnoreCase("title"));
        Assert.assertFalse(characterReader.containsIgnoreCase("body"));
    }
}