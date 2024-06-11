package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * CharacterReaderTest class for testing the CharacterReader class.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        Reader reader = new StringReader("This is a test");
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertNotNull(characterReader);
    }

    @Test
    public void testCharacterReaderConstructorWithSize() {
        Reader reader = new StringReader("This is a test");
        CharacterReader characterReader = new CharacterReader(reader, 10);
        Assert.assertEquals("Size should be 10", 10, characterReader.charBuf.length);
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        String input = "This is a test";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertEquals("Input should be set correctly", input, characterReader.toString());
    }

    @Test
    public void testBufferUp() {
        CharacterReader characterReader = new CharacterReader(new StringReader("This is a test"));
        characterReader.bufferUp();

        Assert.assertFalse("Buffer should not be empty after bufferUp call", characterReader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testBufferUpWithIOException() {
        CharacterReader characterReader = new CharacterReader(new StringReader("This is a test")) {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("Test IOException");
            }
        };

        characterReader.bufferUp();
    }

    @Test
    public void testPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("This is a test"));
        characterReader.bufferUp();

        Assert.assertEquals("Initial position should be 0", 0, characterReader.pos());
        characterReader.advance();
        Assert.assertEquals("Position should be 1 after advancing", 1, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader("This is a test"));
        Assert.assertFalse("Initial state should not be empty", characterReader.isEmpty());

        characterReader.unconsume();
        characterReader.unconsume();
        Assert.assertTrue("State should be empty after unconsuming twice", characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader(new StringReader("This is a test"));
        characterReader.bufferUp();
        characterReader.advance();

        Assert.assertEquals("Current char should be 'T'", 'T', characterReader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("This is a test"));
        characterReader.bufferUp();

        Assert.assertEquals("Initial char should be 'T'", 'T', characterReader.current());
        characterReader.consume();

        Assert.assertEquals("Current char should be 'h'", 'h', characterReader.current());
    }

    @Test
    public void testConsumeEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("This is a test"));
        characterReader.bufferUp();
        characterReader.consumeToEnd();

        Assert.assertEquals("Consumed string should be 'This is a test'", "This is a test", characterReader.toString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("This is a test"));
        characterReader.bufferUp();
        characterReader.advance();
        characterReader.advance();

        Assert.assertEquals("Consumed string should be 'is a'", "is a", characterReader.consumeTo(' ').trim());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConsumeToWithInvalidChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("This is a test"));
        characterReader.bufferUp();

        characterReader.consumeTo((char) 0);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcd1234efgh"));
        characterReader.bufferUp();

        Assert.assertEquals("Consumed string should be 'abcd'", "abcd", characterReader.consumeToAny(new char[]{'c', 'd'}));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcd1234efgh"));
        characterReader.bufferUp();

        Assert.assertEquals("Consumed string should be 'abcd1234efgh'", "abcd1234efgh", characterReader.consumeToAnySorted(new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&<null"));
        characterReader.bufferUp();

        Assert.assertEquals("Consumed string should be '&<null'", "&<null", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html>\n<head>\n<title>Test</title>\n</head>\n<body>"));
        characterReader.bufferUp();

        Assert.assertEquals("Consumed string should be '<title>', '<title>'", "<title>", characterReader.consumeTagName());
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("This is a test"));
        characterReader.bufferUp();

        Assert.assertTrue("Should match 'T'", characterReader.matches('T'));
        Assert.assertTrue("Should match 'h'", characterReader.matches('h'));
        Assert.assertFalse("Should not match 'x'", characterReader.matches('x'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("This is a test"));
        characterReader.bufferUp();

        Assert.assertTrue("Should match 'T'", characterReader.matchesIgnoreCase("t"));
        Assert.assertTrue("Should match 'h'", characterReader.matchesIgnoreCase("H"));
        Assert.assertFalse("Should not match 'x'", characterReader.matchesIgnoreCase("x"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("This is a test"));
        characterReader.bufferUp();

        Assert.assertTrue("Should match 'T'", characterReader.matchesAny('T'));
        Assert.assertTrue("Should match 'h'", characterReader.matchesAny('h'));
        Assert.assertFalse("Should not match 'x'", characterReader.matchesAny('x'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("This is a test"));
        characterReader.bufferUp();

        Assert.assertTrue("Should match 'T'", characterReader.matchesAnySorted(new char[]{'T'}));
        Assert.assertTrue("Should match 'h'", characterReader.matchesAnySorted(new char[]{'h'}));
        Assert.assertFalse("Should not match 'x'", characterReader.matchesAnySorted(new char[]{'x'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("This is a test"));
        characterReader.bufferUp();

        Assert.assertTrue("Should match 'T'", characterReader.matchesLetter());
        Assert.assertTrue("Should match 'h'", characterReader.matchesLetter());
        Assert.assertFalse("Should not match 'x'", characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("This is a test"));
        characterReader.bufferUp();

        Assert.assertFalse("Should not match 'T'", characterReader.matchesDigit());
        Assert.assertFalse("Should not match 'h'", characterReader.matchesDigit());
        Assert.assertTrue("Should match '1'", characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("This is a test"));
        characterReader.bufferUp();

        Assert.assertTrue("Should match 'is a test'", characterReader.matchConsume("is a test"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("This is a test"));
        characterReader.bufferUp();

        Assert.assertTrue("Should match 'is a test'", characterReader.matchConsumeIgnoreCase("Is a test"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html>\n<head>\n<title> Test </title>\n<body>"));
        Assert.assertTrue("Should contain '<title>', '<title>'", characterReader.containsIgnoreCase("<title>"));
        Assert.assertTrue("Should contain '</title>', '</title>'", characterReader.containsIgnoreCase("</title>"));
    }

    @Test
    public void testToString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("This is a test"));
        characterReader.bufferUp();

        Assert.assertEquals("ToString should be 'This is a test'", "This is a test", characterReader.toString());
    }
}