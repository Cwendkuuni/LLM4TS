import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.UncheckedIOException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReaderAndSize() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"), 10);
        Assert.assertNotNull(characterReader);
    }

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertNotNull(characterReader);
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"), "Hello World".length());
        Assert.assertNotNull(characterReader);
    }

    @Test
    public void testBufferUp() throws IOException, UncheckedIOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        Assert.assertEquals(CharacterReader.maxBufferLen, characterReader.bufLength);
    }

    @Test(expected = UncheckedIOException.class)
    public void testBufferUpWithIOException() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World")) {
            @Override
            public void bufferUp() throws IOException {
                throw new IOException("Test IOException");
            }
        };
        characterReader.bufferUp();
    }

    @Test
    public void testPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(0, characterReader.pos());
        characterReader.advance();
        Assert.assertEquals(1, characterReader.pos());
        characterReader.consume();
        Assert.assertEquals(2, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertFalse(characterReader.isEmpty());
        characterReader.consumeTo('W');
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals('H', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('l', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('l', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('o', characterReader.current());
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.consume();
        characterReader.unconsume();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        Assert.assertEquals(1, characterReader.pos());
    }

    @Test
    public void testMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        Assert.assertEquals(0, characterReader.pos());
    }

    @Test(expected = UncheckedIOException.class)
    public void testRewindToMarkWithInvalidMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.rewindToMark();
        characterReader.consume();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.rewindToMark();
        characterReader.consume();
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(8, characterReader.nextIndexOf('W'));
        Assert.assertEquals(0, characterReader.nextIndexOf('H'));
        Assert.assertEquals(-1, characterReader.nextIndexOf('Z'));
        characterReader.consumeTo('l');
        Assert.assertEquals(-1, characterReader.nextIndexOf('l'));
    }

    @Test
    public void testNextIndexOfWithCharSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(8, characterReader.nextIndexOf("World"));
        Assert.assertEquals(-1, characterReader.nextIndexOf("Test"));
        characterReader.consumeTo('l');
        Assert.assertEquals(-1, characterReader.nextIndexOf("World"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String result = characterReader.consumeTo('l');
        Assert.assertEquals("Hello", result);
        result = characterReader.consumeTo('d');
        Assert.assertEquals("Hello World", result);
    }

    @Test
    public void testConsumeToWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String result = characterReader.consumeTo("World");
        Assert.assertEquals("Hello ", result);
        result = characterReader.consumeTo("WorldHello");
        Assert.assertEquals("Hello World", result);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String result = characterReader.consumeToAny('l', 'o');
        Assert.assertEquals("Hello", result);
        result = characterReader.consumeToAny('l', 'o', 'h');
        Assert.assertEquals("Hello World", result);
        result = characterReader.consumeToAny('a', 'z');
        Assert.assertEquals("Hello World", result);
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String result = characterReader.consumeToAnySorted('l', 'o');
        Assert.assertEquals("Hello", result);
        result = characterReader.consumeToAnySorted('l', 'o', 'h');
        Assert.assertEquals("Hello World", result);
        result = characterReader.consumeToAnySorted('a', 'z');
        Assert.assertEquals("Hello World", result);
    }

    @Test
    public void testConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&lt;Hello&gt; World"));
        String result = characterReader.consumeData();
        Assert.assertEquals("&lt;Hello&gt; ", result);
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Hello</title></head><body>World</body>"));
        String result = characterReader.consumeTagName();
        Assert.assertEquals("html", result);
        result = characterReader.consumeTagName();
        Assert.assertEquals("head", result);
        result = characterReader.consumeTagName();
        Assert.assertEquals("title", result);
        result = characterReader.consumeTagName();
        Assert.assertEquals("body", result);
        result = characterReader.consumeToEnd();
        Assert.assertEquals("World", result);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String result = characterReader.consumeToEnd();
        Assert.assertEquals("Hello World", result);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abc123"));
        String result = characterReader.consumeLetterSequence();
        Assert.assertEquals("abc", result);
        result = characterReader.consumeLetterSequence();
        Assert.assertEquals("", result);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abc123"));
        String result = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("abc", result);
        result = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("123", result);
        result = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("", result);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&x01;&x02;&x03"));
        String result = characterReader.consumeHexSequence();
        Assert.assertEquals("\u0001\u0002\u0003", result);
        result = characterReader.consumeHexSequence();
        Assert.assertEquals("", result);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("123"));
        String result = characterReader.consumeDigitSequence();
        Assert.assertEquals("123", result);
        result = characterReader.consumeDigitSequence();
        Assert.assertEquals("", result);
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matches('l'));
        Assert.assertTrue(characterReader.matches('l'));
        Assert.assertTrue(characterReader.matches('l'));
        Assert.assertFalse(characterReader.matches('k'));
    }

    @Test
    public void testMatchesWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matches("lll"));
        Assert.assertFalse(characterReader.matches("llk"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("lll"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("llk"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesAny('l', 'l', 'l'));
        Assert.assertTrue(characterReader.matchesAny('l', 'l', 'k'));
        Assert.assertFalse(characterReader.matchesAny('k', 'l', 'l'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesAnySorted('l', 'l', 'l'));
        Assert.assertTrue(characterReader.matchesAnySorted('l', 'l', 'k'));
        Assert.assertFalse(characterReader.matchesAnySorted('k', 'l', 'l'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("123"));
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String result = characterReader.matchConsume("lll");
        Assert.assertEquals("Hello", result);
        result = characterReader.matchConsume("llk");
        Assert.assertEquals("Hello World", result);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String result = characterReader.matchConsumeIgnoreCase("lll");
        Assert.assertEquals("Hello", result);
        result = characterReader.matchConsumeIgnoreCase("llk");
        Assert.assertEquals("Hello World", result);
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Hello</title></head><body>World</body>"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/title"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/style"));
        Assert.assertFalse(characterReader.containsIgnoreCase("/script"));
    }
}