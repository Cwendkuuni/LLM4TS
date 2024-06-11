package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", characterReader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertEquals("Hello World", characterReader.toString());
    }

    @Test
    public void testBufferUp() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        Assert.assertEquals("Hello World", characterReader.toString());
        characterReader.advance();
        Assert.assertEquals("ello World", characterReader.toString());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOExceptionOnBufferUp() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.reader.reset(); // Simulating IOException
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
        characterReader.consumeToEnd();
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
    }

    @Test
    public void testUnconsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.consume();
        characterReader.unconsume();
        Assert.assertEquals('H', characterReader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());
        characterReader.advance();
        Assert.assertEquals('l', characterReader.current());
    }

    @Test
    public void testMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.mark();
        characterReader.consumeTo('l');
        characterReader.rewindToMark();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testMarkInvalid() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.rewindToMark();
    }

    @Test
    public void testNextIndexOfSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(4, characterReader.nextIndexOf('l'));
        characterReader.consumeTo('l');
        Assert.assertEquals(5, characterReader.nextIndexOf('l'));
    }

    @Test
    public void testNextIndexOfEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfMultiChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(6, characterReader.nextIndexOf("lo"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("ello", characterReader.consumeTo('l'));
        Assert.assertEquals("World", characterReader.toString());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", characterReader.consumeToEnd());
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeToAnySingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("World", characterReader.consumeToAny('l'));
    }

    @Test
    public void testConsumeToAnyMultipleChars() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("llo", characterReader.consumeToAny(new char[]{'l', 'l'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("& <null", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World</p>\n</body>\n</html>"));
        Assert.assertEquals("<html>", characterReader.consumeTagName());
        Assert.assertEquals("html", characterReader.matchesIgnoreCase(characterReader.consumeTagName()));
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("llo", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello123 World"));
        Assert.assertEquals("llo123", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&nbsp;"));
        Assert.assertEquals("nbsp", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("1234567890"));
        Assert.assertEquals("0123456789", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matches('l'));
        Assert.assertTrue(characterReader.matches("ll"));
        Assert.assertFalse(characterReader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("ll"));
        Assert.assertFalse(characterReader.matchesIgnoreCase('z'));
    }

    @Test
    public void testMatchesAnySingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesAny('l'));
        Assert.assertFalse(characterReader.matchesAny('z'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'l', 'l'}));
        Assert.assertFalse(characterReader.matchesAnySorted(new char[]{'z'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter('0'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello123 World"));
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertFalse(characterReader.matchesDigit('a'));
    }

    @Test
    public void testMatchConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("llo", characterReader.matchConsume("llo"));
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("llo", characterReader.matchConsumeIgnoreCase("llo"));
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World</p>\n</body>\n</html>"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/title"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/body"));
        Assert.assertFalse(characterReader.containsIgnoreCase("/footer"));
    }
}