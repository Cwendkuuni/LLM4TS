package org.jsoup.parser;
import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertNotNull(reader);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());

        reader = new CharacterReader("Hello World");
        Assert.assertFalse(reader.isEmpty());
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

    @Test(expected = IllegalStateException.class)
    public void testConsumeBeyondEnd() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume(); // should throw an exception
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('H', reader.current());
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
        reader.rewindToMark();
        Assert.assertEquals('H', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdefg");
        String consumed = reader.consumeAsString();
        Assert.assertEquals("abc", consumed);
        reader.advance();
        consumed = reader.consumeAsString();
        Assert.assertEquals("defg", consumed);
    }

    @Test
    public void testNextIndexOfChar() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertEquals(8, reader.nextIndexOf('W'));
        Assert.assertEquals(-1, reader.nextIndexOf('Z'));
    }

    @Test
    public void testNextIndexOfString() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertEquals(6, reader.nextIndexOf("ll"));
        Assert.assertEquals(-1, reader.nextIndexOf("XX"));
    }

    @Test
    public void testConsumeToChar() {
        CharacterReader reader = new CharacterReader("Hello World");
        String consumed = reader.consumeTo('l');
        Assert.assertEquals("He", consumed);
        reader.advance();
        consumed = reader.consumeTo('l');
        Assert.assertEquals("ello", consumed);
        reader.advance();
        consumed = reader.consumeTo('l');
        Assert.assertEquals("orld", consumed);
    }

    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader("Hello World");
        String consumed = reader.consumeTo("ll");
        Assert.assertEquals("Hello", consumed);
        reader.advance();
        reader.advance();
        consumed = reader.consumeTo("ld");
        Assert.assertEquals("World", consumed);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abracadabra");
        String consumed = reader.consumeToAny('a', 'd');
        Assert.assertEquals("abra", consumed);
        reader.advance();
        consumed = reader.consumeToAny('a', 'd');
        Assert.assertEquals("cada", consumed);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Hello World");
        String consumed = reader.consumeToEnd();
        Assert.assertEquals("Hello World", consumed);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("alpha beta gamma");
        String consumed = reader.consumeLetterSequence();
        Assert.assertEquals("alpha", consumed);
        reader.advance();
        consumed = reader.consumeLetterSequence();
        Assert.assertEquals("beta", consumed);
        reader.advance();
        consumed = reader.consumeLetterSequence();
        Assert.assertEquals("gamma", consumed);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("alpha1 beta2 gamma3");
        String consumed = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("alpha1", consumed);
        reader.advance();
        consumed = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("beta2", consumed);
        reader.advance();
        consumed = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("gamma3", consumed);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0x1a 0x2b 0x3c");
        String consumed = reader.consumeHexSequence();
        Assert.assertEquals("1a", consumed);
        reader.advance();
        consumed = reader.consumeHexSequence();
        Assert.assertEquals("2b", consumed);
        reader.advance();
        consumed = reader.consumeHexSequence();
        Assert.assertEquals("3c", consumed);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345 67890");
        String consumed = reader.consumeDigitSequence();
        Assert.assertEquals("12345", consumed);
        reader.advance();
        consumed = reader.consumeDigitSequence();
        Assert.assertEquals("67890", consumed);
    }

    @Test
    public void testMatchesChar() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertTrue(reader.matches('H'));
        Assert.assertTrue(reader.matches('e'));
        Assert.assertFalse(reader.matches('Z'));
    }

    @Test
    public void testMatchesString() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertTrue(reader.matches("ll"));
        Assert.assertTrue(reader.matches("Hello"));
        Assert.assertFalse(reader.matches("XX"));
    }

    @Test
    public void testMatchesIgnoreCaseString() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertTrue(reader.matchesIgnoreCase("ll"));
        Assert.assertTrue(reader.matchesIgnoreCase("hello"));
        Assert.assertFalse(reader.matchesIgnoreCase("XX"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertTrue(reader.matchesAny('H', 'e'));
        Assert.assertFalse(reader.matchesAny('Z', 'e'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertTrue(reader.matchesLetter());
        reader.advance();
        Assert.assertTrue(reader.matchesLetter());
        reader.advance();
        Assert.assertTrue(reader.matchesLetter());
        reader.advance();
        Assert.assertTrue(reader.matchesLetter());
        reader.advance();
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("12345 67890");
        Assert.assertTrue(reader.matchesDigit());
        reader.advance();
        Assert.assertTrue(reader.matchesDigit());
        reader.advance();
        Assert.assertTrue(reader.matchesDigit());
        reader.advance();
        Assert.assertTrue(reader.matchesDigit());
        reader.advance();
        Assert.assertTrue(reader.matchesDigit());
        reader.advance();
        Assert.assertTrue(reader.matchesDigit());
        reader.advance();
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsumeString() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertTrue(reader.matchConsume("ll"));
        Assert.assertTrue(reader.matchConsume("Hello"));
        reader.advance();
        reader.advance();
        Assert.assertFalse(reader.matchConsume("XX"));
    }

    @Test
    public void testMatchConsumeIgnoreCaseString() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertTrue(reader.matchConsumeIgnoreCase("ll"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("hello"));
        reader.advance();
        reader.advance();
        Assert.assertFalse(reader.matchConsumeIgnoreCase("XX"));
    }

    @Test
    public void testContainsIgnoreCaseString() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head><body>Test</body></html>");
        Assert.assertTrue(reader.containsIgnoreCase("title"));
        Assert.assertTrue(reader.containsIgnoreCase("test"));
        Assert.assertFalse(reader.containsIgnoreCase("XXX"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertEquals("Hello World", reader.toString());
    }
}