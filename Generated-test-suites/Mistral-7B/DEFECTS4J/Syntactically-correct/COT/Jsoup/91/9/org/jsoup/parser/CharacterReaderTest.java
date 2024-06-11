package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class CharacterReaderTest {

    private CharacterReader characterReader;
    private static final String TEST_STRING = "Hello <World>! 123 & <abc> 你好 你好 你好";

    @Before
    public void setUp() {
        characterReader = new CharacterReader(new StringReader(TEST_STRING));
    }

    

    @Test
    public void testCharacterReaderConstructorWithInput() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test Input"));
        Assert.assertNotNull(characterReader.charBuf);
        Assert.assertNotNull(characterReader.reader);
        Assert.assertEquals(maxBufferLen, characterReader.charBuf.length);
        characterReader.bufferUp();
        try {
            characterReader.reader.close();
        } catch (IOException e) {
            Assert.fail("Failed to close reader in test");
        }
    }

    @Test
    public void testCharacterReaderConstructorWithInputAndSize() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test Input"), 100);
        Assert.assertEquals(100, characterReader.charBuf.length);
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader(TEST_STRING);
        Assert.assertNotNull(characterReader.charBuf);
        Assert.assertNotNull(characterReader.reader);
        Assert.assertEquals(TEST_STRING.length(), characterReader.pos());
    }

    @Test
    public void testBufferUp() throws IOException {
        characterReader.bufferUp();
        Assert.assertFalse(characterReader.isEmptyNoBufferUp());
    }

    @Test
    public void testIsEmpty() {
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        Assert.assertEquals(TEST_STRING.charAt(0), characterReader.current());
        characterReader.consume();
        Assert.assertEquals(TEST_STRING.charAt(1), characterReader.current());
    }

    @Test
    public void testConsume() {
        characterReader.consume();
        Assert.assertEquals(TEST_STRING.charAt(1), characterReader.current());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        Assert.assertEquals(TEST_STRING.charAt(0), characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        Assert.assertEquals(TEST_STRING.charAt(1), characterReader.current());
    }

    @Test
    public void testMark() {
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        Assert.assertEquals(TEST_STRING.charAt(0), characterReader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testMarkAndReadPastEnd() {
        characterReader.mark();
        characterReader.consumeToEnd();
        characterReader.rewindToMark();
    }

    @Test
    public void testNextIndexOf() {
        Assert.assertEquals(TEST_STRING.indexOf("<"), characterReader.nextIndexOf('<'));
        Assert.assertEquals(-1, characterReader.nextIndexOf('Z'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        Assert.assertEquals(TEST_STRING.indexOf("<World>"), characterReader.nextIndexOf("<World>"));
        Assert.assertEquals(-1, characterReader.nextIndexOf("ZZZ"));
    }

    @Test
    public void testConsumeTo() {
        String consumed = characterReader.consumeTo('>');
        Assert.assertEquals("<World>", consumed);
        Assert.assertEquals(TEST_STRING.indexOf("你好", characterReader.pos()), characterReader.pos());
    }

    @Test
    public void testConsumeToEnd() {
        String consumed = characterReader.consumeToEnd();
        Assert.assertEquals(TEST_STRING, consumed);
        Assert.assertEquals(-1, characterReader.pos());
    }

    @Test
    public void testConsumeToAny() {
        String consumed = characterReader.consumeToAny(new char[]{'<', '>', '&'});
        Assert.assertEquals("Hello <World>! 123 & ", consumed);
    }

    @Test
    public void testConsumeToAnySorted() {
        String consumed = characterReader.consumeToAnySorted(new char[]{'&', '>', '<'});
        Assert.assertEquals("Hello <World>! 123 & ", consumed);
    }

    @Test
    public void testConsumeData() {
        String consumed = characterReader.consumeData();
        Assert.assertEquals("&, <, null", consumed);
    }

    @Test
    public void testConsumeTagName() {
        String consumed = characterReader.consumeTagName();
        Assert.assertEquals("Hello <World>", consumed);
    }

    @Test
    public void testConsumeToEndWithBufferUp() {
        characterReader.bufferUp();
        String consumed = characterReader.consumeToEnd();
        Assert.assertEquals(TEST_STRING, consumed);
        Assert.assertEquals(-1, characterReader.pos());
    }

    @Test
    public void testConsumeLetterSequence() {
        String consumed = characterReader.consumeLetterSequence();
        Assert.assertEquals("Hello", consumed);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String consumed = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("World123", consumed);
    }

    @Test
    public void testMatches() {
        Assert.assertTrue(characterReader.matches('H'));
        Assert.assertTrue(characterReader.matches("<"));
        Assert.assertFalse(characterReader.matches('Z'));
    }

    @Test
    public void testMatchesString() {
        Assert.assertTrue(characterReader.matches("Hello"));
        Assert.assertFalse(characterReader.matches("HelloWorld"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        Assert.assertTrue(characterReader.matchesIgnoreCase("hello"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("HELLO"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("HelloWorld"));
    }

    @Test
    public void testMatchesAny() {
        Assert.assertTrue(characterReader.matchesAny(new char[]{'H', 'h'}));
        Assert.assertTrue(characterReader.matchesAny(new char[]{'<', '>'}));
        Assert.assertFalse(characterReader.matchesAny(new char[]{'Z'}));
    }

    @Test
    public void testMatchesAnySorted() {
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'&', '>', '<'}));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'<', '>', '&'}));
        Assert.assertFalse(characterReader.matchesAnySorted(new char[]{'Z'}));
    }

    @Test
    public void testMatchesLetter() {
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter('5'));
    }

    @Test
    public void testMatchesDigit() {
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertFalse(characterReader.matchesDigit('A'));
    }

    @Test
    public void testMatchConsume() {
        characterReader.matchConsume("World");
        Assert.assertEquals(TEST_STRING.indexOf("你好", characterReader.pos()), characterReader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        characterReader.matchConsumeIgnoreCase("World");
        Assert.assertEquals(TEST_STRING.indexOf("你好", characterReader.pos()), characterReader.pos());
    }

    @Test
    public void testContainsIgnoreCase() {
        Assert.assertTrue(characterReader.containsIgnoreCase("/title"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/STYLE"));
        Assert.assertFalse(characterReader.containsIgnoreCase("/Z"));
    }

    
}