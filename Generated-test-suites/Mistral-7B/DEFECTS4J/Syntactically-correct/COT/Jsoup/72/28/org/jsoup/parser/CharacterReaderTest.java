package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.jsoup.parser.CharacterReader.EOF;

/**
 * CharacterReaderTest class to test the CharacterReader class methods.
 */
public final class CharacterReaderTest {

    /**
     * Test constructor with valid Reader input.
     */
    @Test
    public void testCharacterReaderWithValidInput() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertNotNull(characterReader.charBuf);
        Assert.assertNotNull(characterReader.reader);
    }

    /**
     * Test constructor with null Reader input.
     */
    @Test(expected = NullPointerException.class)
    public void testCharacterReaderWithNullInput() {
        CharacterReader characterReader = new CharacterReader(null);
    }

    /**
     * Test constructor with maxBufferLen size input.
     */
    @Test
    public void testCharacterReaderWithMaxBufferLenInput() {
        Reader reader = new StringReader("This is a test with maxBufferLen size input.");
        CharacterReader characterReader = new CharacterReader(reader, CharacterReader.maxBufferLen);
        Assert.assertEquals(CharacterReader.maxBufferLen, characterReader.charBuf.length);
    }

    /**
     * Test constructor with String input.
     */
    @Test
    public void testCharacterReaderWithStringInput() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String Input"), "Test String Input".length());
        Assert.assertEquals("Test String Input".length(), characterReader.charBuf.length);
    }

    /**
     * Test method pos().
     */
    @Test
    public void testPos() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertEquals(0, characterReader.pos());
        characterReader.advance();
        Assert.assertEquals(1, characterReader.pos());
    }

    /**
     * Test method isEmpty().
     */
    @Test
    public void testIsEmpty() {
        Reader reader = new StringReader("");
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertTrue(characterReader.isEmpty());

        reader = new StringReader("Hello World!");
        characterReader = new CharacterReader(reader);
        Assert.assertFalse(characterReader.isEmpty());
    }

    /**
     * Test method current().
     */
    @Test
    public void testCurrent() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.advance();
        Assert.assertEquals('H', characterReader.current());
    }

    /**
     * Test method consume().
     */
    @Test
    public void testConsume() throws IOException {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertEquals('H', characterReader.consume());
        Assert.assertEquals('H', characterReader.current());
    }

    /**
     * Test method unconsume().
     */
    @Test
    public void testUnconsume() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.advance();
        characterReader.consume();
        characterReader.unconsume();
        Assert.assertEquals('H', characterReader.current());
    }

    /**
     * Test method advance().
     */
    @Test
    public void testAdvance() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.advance();
        Assert.assertEquals('H', characterReader.current());
        characterReader.advance();
        Assert.assertEquals('E', characterReader.current());
    }

    /**
     * Test method mark().
     */
    @Test
    public void testMark() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.mark();
        characterReader.advance();
        characterReader.consume();
        characterReader.rewindToMark();
        Assert.assertEquals('H', characterReader.current());
    }

    /**
     * Test method rewindToMark().
     */
    @Test
    public void testRewindToMark() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.mark();
        characterReader.advance();
        characterReader.consume();
        characterReader.rewindToMark();
        Assert.assertEquals('H', characterReader.current());
    }

    /**
     * Test method nextIndexOf(char).
     */
    @Test
    public void testNextIndexOfChar() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertEquals(-1, characterReader.nextIndexOf('Z'));
        characterReader.consume();
        characterReader.consume();
        Assert.assertEquals(2, characterReader.nextIndexOf('l'));
    }

    /**
     * Test method nextIndexOf(CharSequence).
     */
    @Test
    public void testNextIndexOfCharSequence() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertEquals(-1, characterReader.nextIndexOf("XYZ"));
        characterReader.consume();
        characterReader.consume();
        Assert.assertEquals(3, characterReader.nextIndexOf("ll"));
    }

    /**
     * Test method consumeTo(char).
     */
    @Test
    public void testConsumeToChar() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.advance();
        String consumed = characterReader.consumeTo('l');
        Assert.assertEquals("He", consumed);
    }

    /**
     * Test method consumeTo(String).
     */
    @Test
    public void testConsumeToString() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.advance();
        characterReader.advance();
        String consumed = characterReader.consumeTo("ll");
        Assert.assertEquals("ello World!", consumed);
    }

    /**
     * Test method consumeToAny(char[]).
     */
    @Test
    public void testConsumeToAnyChars() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        String consumed = characterReader.consumeToAny(new char[]{'l', 'o'});
        Assert.assertEquals("ello World!", consumed);
    }

    /**
     * Test method consumeToAnySorted(char[]).
     */
    @Test
    public void testConsumeToAnySortedChars() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        String consumed = characterReader.consumeToAnySorted(new char[]{'>', '<'});
        Assert.assertEquals("World!", consumed);
    }

    /**
     * Test method consumeData().
     */
    @Test
    public void testConsumeData() {
        Reader reader = new StringReader("&lt;html&gt; &amp; &gt;");
        CharacterReader characterReader = new CharacterReader(reader);
        String consumed = characterReader.consumeData();
        Assert.assertEquals("&lt;html&gt; ", consumed);
    }

    /**
     * Test method consumeTagName().
     */
    @Test
    public void testConsumeTagName() {
        Reader reader = new StringReader("<html><head></head><body></body></html>");
        CharacterReader characterReader = new CharacterReader(reader);
        String consumed = characterReader.consumeTagName();
        Assert.assertEquals("html", consumed);
    }

    /**
     * Test method consumeToEnd().
     */
    @Test
    public void testConsumeToEnd() throws IOException {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        String consumed = characterReader.consumeToEnd();
        Assert.assertEquals("Hello World!", consumed);
    }

    /**
     * Test method consumeLetterSequence().
     */
    @Test
    public void testConsumeLetterSequence() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        String consumed = characterReader.consumeLetterSequence();
        Assert.assertEquals("Hello", consumed);
    }

    /**
     * Test method consumeLetterThenDigitSequence().
     */
    @Test
    public void testConsumeLetterThenDigitSequence() {
        Reader reader = new StringReader("Hello123 World456!");
        CharacterReader characterReader = new CharacterReader(reader);
        String consumed = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Hello123", consumed);
    }

    /**
     * Test method matches(char).
     */
    @Test
    public void testMatchesChar() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertTrue(characterReader.matches('H'));
        Assert.assertFalse(characterReader.matches('Z'));
    }

    /**
     * Test method matches(String).
     */
    @Test
    public void testMatchesString() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertTrue(characterReader.matches("He"));
        Assert.assertFalse(characterReader.matches("XY"));
    }

    /**
     * Test method matchesIgnoreCase(String).
     */
    @Test
    public void testMatchesIgnoreCaseString() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertTrue(characterReader.matchesIgnoreCase("He"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("he"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("XY"));
    }

    /**
     * Test method matchesAny(char[]).
     */
    @Test
    public void testMatchesAnyChars() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertTrue(characterReader.matchesAny(new char[]{'H', 'E'}));
        Assert.assertFalse(characterReader.matchesAny(new char[]{'Z', 'O'}));
    }

    /**
     * Test method matchesAnySorted(char[]).
     */
    @Test
    public void testMatchesAnySortedChars() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'H', 'E'}));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'E', 'H'}));
        Assert.assertFalse(characterReader.matchesAnySorted(new char[]{'Z', 'O'}));
    }

    /**
     * Test method matchesLetter().
     */
    @Test
    public void testMatchesLetter() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.consume();
        Assert.assertFalse(characterReader.matchesLetter());
    }

    /**
     * Test method matchesDigit().
     */
    @Test
    public void testMatchesDigit() {
        Reader reader = new StringReader("Hello123 World456!");
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.consume();
        characterReader.consume();
        Assert.assertFalse(characterReader.matchesDigit());
    }

    /**
     * Test method matchConsume(String).
     */
    @Test
    public void testMatchConsumeString() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertTrue(characterReader.matchConsume("He"));
        characterReader.advance();
        characterReader.advance();
        String consumed = characterReader.consumeToEnd();
        Assert.assertEquals("ld World!", consumed);
        Assert.assertFalse(characterReader.matchConsume("He"));
    }

    /**
     * Test method matchConsumeIgnoreCase(String).
     */
    @Test
    public void testMatchConsumeIgnoreCaseString() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("He"));
        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("he"));
        Assert.assertFalse(characterReader.matchConsumeIgnoreCase("He"));
        characterReader.advance();
        characterReader.advance();
        String consumed = characterReader.consumeToEnd();
        Assert.assertEquals("ld World!", consumed);
        Assert.assertFalse(characterReader.matchConsumeIgnoreCase("He"));
    }

    /**
     * Test method containsIgnoreCase(String).
     */
    @Test
    public void testContainsIgnoreCaseString() {
        Reader reader = new StringReader("<html><head><title>Test</title></head><body></body></html>");
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertTrue(characterReader.containsIgnoreCase("<title>"));
        Assert.assertTrue(characterReader.containsIgnoreCase("title>"));
        Assert.assertFalse(characterReader.containsIgnoreCase("<body>"));
    }
}