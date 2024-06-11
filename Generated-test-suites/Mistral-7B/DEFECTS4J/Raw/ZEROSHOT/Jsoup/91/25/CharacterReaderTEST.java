package org.jsoup.parser;

import org.jsoup.UncheckedIOException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

/**
 * CharacterReaderTest class to test the CharacterReader class.
 */
public final class CharacterReaderTest {

    @Test
    public void testCharacterReaderInstance() {
        Reader reader = new StringReader("Hello World");
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertNotNull(characterReader);
    }

    @Test
    public void testCharacterReaderMaxBufferLen() {
        Reader reader = new StringReader("Hello World");
        CharacterReader characterReader = new CharacterReader(reader, 5);
        Assert.assertEquals("Size should be equal to maxBufferLen.", 5, characterReader.charBuf.length);
    }

    @Test
    public void testCharacterReaderDefaultBufferLen() {
        Reader reader = new StringReader("Hello World");
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertEquals("Size should be equal to maxBufferLen by default.", CharacterReader.maxBufferLen, characterReader.charBuf.length);
    }

    @Test
    public void testCharacterReaderStringInput() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Current should return the first character.", 'H', characterReader.current());
        characterReader.consume();
        Assert.assertEquals("Current should return the second character.", 'e', characterReader.current());
        characterReader.unconsume();
        Assert.assertEquals("Current should return the second character after unconsuming.", 'e', characterReader.current());
        characterReader.consume();
        Assert.assertEquals("Current should return the third character.", 'l', characterReader.current());
        characterReader.mark();
        characterReader.rewindToMark();
        Assert.assertEquals("Current should return the second character after rewinding.", 'e', characterReader.current());
        characterReader.consume();
        Assert.assertEquals("Current should return the third character after consuming.", 'l', characterReader.current());
        Assert.assertFalse("Should not be empty.", characterReader.isEmpty());
        characterReader.bufferUp();
        Assert.assertTrue("Should be empty after bufferUp.", characterReader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        Reader reader = new StringReader("");
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.reader.close();
        characterReader.consume();
    }

    @Test
    public void testCharacterReaderPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Position should be equal to 0.", 0, characterReader.pos());
        for (int i = 0; i < 10; i++) {
            characterReader.consume();
        }
        Assert.assertEquals("Position should be equal to 10.", 10, characterReader.pos());
    }

    @Test
    public void testCharacterReaderIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertFalse("Should not be empty.", characterReader.isEmpty());
        characterReader.consumeToEnd();
        Assert.assertTrue("Should be empty.", characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Next index of 'l' should be 3.", 3, characterReader.nextIndexOf('l'));
        Assert.assertEquals("Next index of 'x' should be -1.", -1, characterReader.nextIndexOf('x'));
        characterReader.consumeToEnd();
        Assert.assertEquals("Next index of 'l' should be -1 after consuming all characters.", -1, characterReader.nextIndexOf('l'));
    }

    @Test
    public void testCharacterReaderNextIndexOfString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Next index of 'Hello' should be 0.", 0, characterReader.nextIndexOf("Hello"));
        Assert.assertEquals("Next index of 'world' should be 6.", 6, characterReader.nextIndexOf("world"));
        Assert.assertEquals("Next index of 'World' should be -1.", -1, characterReader.nextIndexOf("World"));
        characterReader.consumeToEnd();
        Assert.assertEquals("Next index of 'Hello' should be -1 after consuming all characters.", -1, characterReader.nextIndexOf("Hello"));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String consumed = characterReader.consumeTo('l');
        Assert.assertEquals("Consumed should be 'Hello'.", "Hello", consumed);
        Assert.assertEquals("Current should be 'l'.", 'l', characterReader.current());
        characterReader.consume();
        Assert.assertEquals("Current should be 'o'.", 'o', characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String consumed = characterReader.consumeToEnd();
        Assert.assertEquals("Consumed should be 'Hello World'.", "Hello World", consumed);
        Assert.assertTrue("Should be empty after consuming all characters.", characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String consumed = characterReader.consumeToAny(',', '.', '!');
        Assert.assertEquals("Consumed should be 'Hello'.", "Hello", consumed);
        Assert.assertEquals("Current should be 'W'.", 'W', characterReader.current());
        characterReader.consume();
        Assert.assertEquals("Current should be 'o'.", 'o', characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String consumed = characterReader.consumeToAnySorted('!', '.', ',');
        Assert.assertEquals("Consumed should be 'Hello'.", "Hello", consumed);
        Assert.assertEquals("Current should be 'W'.", 'W', characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String consumed = characterReader.consumeData();
        Assert.assertEquals("Consumed should be 'Hello World' minus &, <, null.', '/', '>', and special characters.'.", "Hello World".substring(0, 5) + "....", consumed);
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader(" <html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n"));
        String consumed = characterReader.consumeTagName();
        Assert.assertEquals("Consumed should be '<title>'.", "<title>", consumed);
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String consumed = characterReader.consumeLetterSequence();
        Assert.assertEquals("Consumed should be 'Hello'.", "Hello", consumed);
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello1234World5678"));
        String consumed = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Consumed should be 'Hello123'.", "Hello123", consumed);
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&#x61; &#x6C; &#x65; &#x72; &#x74; &#x41; &#x53; &#x53; &#x45; &#x54;"));
        String consumed = characterReader.consumeHexSequence();
        Assert.assertEquals("Consumed should be 'abcDEF'.", "abcDEF", consumed);
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("12345678901234567890"));
        String consumed = characterReader.consumeDigitSequence();
        Assert.assertEquals("Consumed should be '12345678901234567890' minus last character.", "12345678901234567890".substring(0, 15), consumed);
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue("Matches 'l'.", characterReader.matches('l'));
        Assert.assertTrue("Matches 'o'.", characterReader.matches('o'));
        Assert.assertFalse("Does not match 'x'.", characterReader.matches('x'));
    }

    @Test
    public void testCharacterReaderMatchesString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue("Matches 'Hello World'.", characterReader.matches("Hello World"));
        Assert.assertFalse("Does not match 'Hello World1'.", characterReader.matches("Hello World1"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue("Matches 'hello' ignore case.", characterReader.matchesIgnoreCase("hello"));
        Assert.assertTrue("Matches 'Hello World' ignore case.", characterReader.matchesIgnoreCase("Hello World"));
        Assert.assertFalse("Does not match 'HELLO WORLD' ignore case.", characterReader.matchesIgnoreCase("HELLO WORLD"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue("Matches 'o'.", characterReader.matchesAny('o'));
        Assert.assertTrue("Matches 'l'.", characterReader.matchesAny('l'));
        Assert.assertFalse("Does not match 'x'.", characterReader.matchesAny('x'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue("Matches 'l' sorted.", characterReader.matchesAnySorted('l'));
        Assert.assertTrue("Matches 'o' sorted.", characterReader.matchesAnySorted('o'));
        Assert.assertFalse("Does not match 'x' sorted.", characterReader.matchesAnySorted('x'));
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.matchConsume("Hello");
        Assert.assertEquals("Current should be 'W'.", 'W', characterReader.current());
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.matchConsumeIgnoreCase("hello");
        Assert.assertEquals("Current should be 'W'.", 'W', characterReader.current());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader(" <html>\n<head>\n<title>Ignore Case</title>\n</head>\n<body>\n</body>\n"));
        Assert.assertTrue("Should contain '<title>Ignore Case</title>' ignore case.", characterReader.containsIgnoreCase("<title>Ignore Case</title>"));
        Assert.assertTrue("Should contain '</title>' ignore case.", characterReader.containsIgnoreCase("</title>"));
        Assert.assertFalse("Should not contain '<title>Ignore CASE</title>' ignore case.", characterReader.containsIgnoreCase("<title>Ignore CASE</title>"));
    }
}