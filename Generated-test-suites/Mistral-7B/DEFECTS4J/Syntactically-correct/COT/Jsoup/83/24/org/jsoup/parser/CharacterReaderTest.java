package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderBasic() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        Assert.assertEquals('H', reader.current());
        reader.consume();
        Assert.assertEquals('e', reader.current());
        reader.advance();
        reader.advance();
        reader.mark();
        reader.rewindToMark();

        Assert.assertEquals('e', reader.current());
        reader.unconsume();
        Assert.assertEquals('e', reader.current());

        reader.advance();
        reader.advance();
        reader.rewindToMark();

        Assert.assertEquals('e', reader.current());

        reader.isEmpty(); // no need to assert as it doesn't throw an exception when empty
        reader.isEmptyNoBufferUp(); // no need to assert as it returns true when empty

        reader.consume();

        Assert.assertFalse(reader.isEmptyNoBufferUp());
        Assert.assertEquals(1, reader.pos());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new IOException("Test IOException"));
    }

    @Test
    public void testCharacterReaderBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        reader.bufferUp();
        reader.bufferUp();

        Assert.assertEquals('H', reader.current());
        reader.consume();
        Assert.assertEquals('e', reader.current());
        reader.advance();
        reader.advance();

        reader.bufferUp();
        reader.bufferUp();

        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        reader.mark();
        reader.consume();
        reader.advance();
        reader.advance();

        reader.rewindToMark();

        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        Assert.assertEquals(5, reader.nextIndexOf('o'));
        Assert.assertEquals(6, reader.nextIndexOf('d'));
        Assert.assertEquals(-1, reader.nextIndexOf('x'));
    }

    @Test
    public void testCharacterReaderNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        Assert.assertEquals(5, reader.nextIndexOf("ll"));
        Assert.assertEquals(6, reader.nextIndexOf("ld"));
        Assert.assertEquals(-1, reader.nextIndexOf("xx"));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        String consumed = reader.consumeTo('o');
        Assert.assertEquals("llo", consumed);

        consumed = reader.consumeTo("ld");
        Assert.assertEquals("ld", consumed);

        consumed = reader.consumeToAny(new char[]{'o', 'd'});
        Assert.assertEquals("ld", consumed);

        consumed = reader.consumeToAnySorted(new char[]{'d', 'o'});
        Assert.assertEquals("ld", consumed);

        consumed = reader.consumeData();
        Assert.assertEquals("Hello World", consumed);

        consumed = reader.consumeTagName();
        Assert.assertEquals("<title", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        String consumed = reader.consumeToAny(new char[]{'o', 'd'});
        Assert.assertEquals("ld", consumed);

        consumed = reader.consumeToAny(new char[]{'o'});
        Assert.assertEquals("llo", consumed);

        consumed = reader.consumeToAny(new char[]{'x'});
        Assert.assertEquals("", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        String consumed = reader.consumeToAnySorted(new char[]{'d', 'o'});
        Assert.assertEquals("ld", consumed);

        consumed = reader.consumeToAnySorted(new char[]{'o'});
        Assert.assertEquals("llo", consumed);

        consumed = reader.consumeToAnySorted(new char[]{'x'});
        Assert.assertEquals("", consumed);
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        String consumed = reader.consumeLetterSequence();
        Assert.assertEquals("Hello", consumed);
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello123 World"));

        String consumed = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Hello123", consumed);
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("0x1234 0x5678"));

        String consumed1 = reader.consumeHexSequence();
        Assert.assertEquals("1234", consumed1);

        String consumed2 = reader.consumeHexSequence();
        Assert.assertEquals("5678", consumed2);
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("12345 67890"));

        String consumed1 = reader.consumeDigitSequence();
        Assert.assertEquals("12345", consumed1);

        String consumed2 = reader.consumeDigitSequence();
        Assert.assertEquals("67890", consumed2);
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        Assert.assertTrue(reader.matches('l'));
        Assert.assertTrue(reader.matches("ll"));
        Assert.assertFalse(reader.matches('x'));

        reader.consume();
        Assert.assertFalse(reader.matches('l'));
        Assert.assertTrue(reader.matches("llo"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        Assert.assertTrue(reader.matchesIgnoreCase("l"));
        Assert.assertTrue(reader.matchesIgnoreCase("ll"));
        Assert.assertFalse(reader.matchesIgnoreCase("X"));

        reader.consume();
        Assert.assertFalse(reader.matchesIgnoreCase("l"));
        Assert.assertTrue(reader.matchesIgnoreCase("llo"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        Assert.assertTrue(reader.matchesAny('l'));
        Assert.assertTrue(reader.matchesAny('o'));
        Assert.assertFalse(reader.matchesAny('x'));

        reader.consume();
        Assert.assertTrue(reader.matchesAny('l'));
        Assert.assertTrue(reader.matchesAny('o'));
        Assert.assertFalse(reader.matchesAny('x'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        Assert.assertTrue(reader.matchesAnySorted('l'));
        Assert.assertTrue(reader.matchesAnySorted('o'));
        Assert.assertFalse(reader.matchesAnySorted('x'));

        reader.consume();
        Assert.assertTrue(reader.matchesAnySorted('l'));
        Assert.assertTrue(reader.matchesAnySorted('o'));
        Assert.assertFalse(reader.matchesAnySorted('x'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        Assert.assertTrue(reader.matchesLetter());
        reader.consume();
        Assert.assertTrue(reader.matchesLetter());
        reader.consume();
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("12345 67890"));

        Assert.assertTrue(reader.matchesDigit());
        reader.consume();
        Assert.assertTrue(reader.matchesDigit());
        reader.consume();
        Assert.assertFalse(reader.matchesDigit());
    }
}