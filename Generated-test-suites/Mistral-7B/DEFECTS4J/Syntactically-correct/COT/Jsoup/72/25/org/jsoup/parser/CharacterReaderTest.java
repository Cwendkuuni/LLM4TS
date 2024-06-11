package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.jsoup.parser.CharacterReader.EOF;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", reader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertEquals("Hello World", reader.toString());
    }

    @Test
    public void testCharacterReaderIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(reader.isEmpty());

        reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderCurrent() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals('H', reader.current());
        reader.advance();
        Assert.assertEquals('e', reader.current());
        reader.consume();
        Assert.assertEquals('l', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("Test IOException");
            }
        });
        reader.consume(); // This method should throw UncheckedIOException
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(7, reader.nextIndexOf('o'));
        Assert.assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String expected = "Hello ";
        String result = reader.consumeTo(' ');
        Assert.assertEquals(expected, result);
        Assert.assertEquals(' ', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String expected = "Hello World";
        String result = reader.consumeToEnd();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld"));
        String expected = "Hello";
        String result = reader.consumeLetterSequence();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello123World"));
        String expected = "Hello123";
        String result = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("0xFFFF"));
        String expected = "FF";
        String result = reader.consumeHexSequence();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("12345"));
        String expected = "12345";
        String result = reader.consumeDigitSequence();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(reader.matches('l'));
        Assert.assertTrue(reader.matches("ld"));
        Assert.assertFalse(reader.matches('z'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(reader.matchesIgnoreCase("ld"));
        Assert.assertFalse(reader.matchesIgnoreCase("z"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(reader.matchesAny('l', 'd'));
        Assert.assertFalse(reader.matchesAny('z'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'l', 'd'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'z'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(reader.matchesLetter());
        reader.advance();
        Assert.assertTrue(reader.matchesLetter());
        reader.consume();
        Assert.assertTrue(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello123World"));
        Assert.assertTrue(reader.matchesDigit());
        reader.advance();
        Assert.assertTrue(reader.matchesDigit());
        reader.consume();
        reader.consume();
        Assert.assertTrue(reader.matchesDigit());
    }
}