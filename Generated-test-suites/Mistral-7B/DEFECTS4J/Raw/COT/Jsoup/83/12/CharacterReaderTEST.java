package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertEquals("Hello World!", characterReader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        Assert.assertEquals("Hello World!", characterReader.toString());
    }

    @Test
    public void testBufferUpMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.bufferUp();
        Assert.assertFalse(characterReader.isEmptyNoBufferUp());
    }

    @Test(expected = UncheckedIOException.class)
    public void testBufferUpMethodWithIOException() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!")) {
            @Override
            public int read(char[] cbuf, int off, int length) throws IOException {
                throw new IOException("IOException for testing.");
            }
        };
        characterReader.bufferUp();
    }

    @Test
    public void testPosMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals(0, characterReader.pos());
        characterReader.advance();
        Assert.assertEquals(1, characterReader.pos());
        characterReader.consume();
        Assert.assertEquals(2, characterReader.pos());
    }

    @Test
    public void testIsEmptyMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertFalse(characterReader.isEmpty());
        characterReader.consume();
        Assert.assertFalse(characterReader.isEmpty());
        characterReader.consumeTo('o');
        Assert.assertFalse(characterReader.isEmpty());
        characterReader.consumeToEnd();
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrentMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals('H', characterReader.current());
        characterReader.advance();
        Assert.assertEquals('H', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
        characterReader.consumeToEnd();
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals('H', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
        characterReader.consumeToEnd();
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testUnconsumeMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.consume();
        characterReader.unconsume();
        Assert.assertEquals('H', characterReader.current());
    }

    @Test
    public void testAdvanceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.advance();
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testMarkMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        Assert.assertEquals('H', characterReader.current());
    }

    @Test
    public void testNextIndexOfMethodWithChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals(5, characterReader.nextIndexOf('o'));
        Assert.assertEquals(-1, characterReader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfMethodWithCharSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals(6, characterReader.nextIndexOf("llo"));
        Assert.assertEquals(-1, characterReader.nextIndexOf("xxx"));
    }

    @Test
    public void testConsumeToMethodWithChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        String result = characterReader.consumeTo('o');
        Assert.assertEquals("Hello", result);
        characterReader.consume();
        result = characterReader.consumeTo('d');
        Assert.assertEquals("HelloWorld!", result);
    }

    @Test
    public void testConsumeToMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        String result = characterReader.consumeTo("llo");
        Assert.assertEquals("Hello", result);
        characterReader.consume();
        result = characterReader.consumeTo("World!");
        Assert.assertEquals("World!", result);
    }

    @Test
    public void testConsumeToAnyMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        String result = characterReader.consumeToAny(new char[]{'l', 'o'});
        Assert.assertEquals("Hello", result);
        result = characterReader.consumeToAny(new char[]{'>'});
        Assert.assertEquals("Hello World!", result);
    }

    @Test
    public void testConsumeToAnySortedMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        String result = characterReader.consumeToAnySorted(new char[]{'l', 'o'});
        Assert.assertEquals("Hello", result);
        result = characterReader.consumeToAnySorted(new char[]{'>'});
        Assert.assertEquals("Hello World!", result);
    }

    @Test
    public void testConsumeDataMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!& <null>"));
        String result = characterReader.consumeData();
        Assert.assertEquals("Hello World!", result);
    }

    @Test
    public void testConsumeTagNameMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head></head><body></body></html>"));
        String result = characterReader.consumeTagName();
        Assert.assertEquals("html", result);
        result = characterReader.consumeTagName();
        Assert.assertEquals("head", result);
        result = characterReader.consumeTagName();
        Assert.assertEquals("body", result);
        result = characterReader.consumeTagName();
        Assert.assertEquals(CharacterReader.EOF, result);
    }

    @Test
    public void testConsumeToEndMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        String result = characterReader.consumeToEnd();
        Assert.assertEquals("Hello World!", result);
    }

    @Test
    public void testConsumeLetterSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        String result = characterReader.consumeLetterSequence();
        Assert.assertEquals("Hello", result);
        characterReader.consume();
        result = characterReader.consumeLetterSequence();
        Assert.assertEquals("World!", result);
    }

    @Test
    public void testConsumeLetterThenDigitSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello123World456!"));
        String result = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Hello123", result);
        characterReader.consumeLetter();
        characterReader.consumeDigit();
        result = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("World456", result);
    }

    @Test
    public void testMatchesMethodWithChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matches('o'));
        Assert.assertFalse(characterReader.matches('x'));
    }

    @Test
    public void testMatchesMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matches("llo"));
        Assert.assertFalse(characterReader.matches("xxx"));
    }

    @Test
    public void testMatchesIgnoreCaseMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("llo"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("World"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("xxx"));
    }

    @Test
    public void testMatchesAnyMethodWithCharArray() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matchesAny(new char[]{'o'}));
        Assert.assertTrue(characterReader.matchesAny(new char[]{'l'}));
        Assert.assertTrue(characterReader.matchesAny(new char[]{'H'}));
        Assert.assertFalse(characterReader.matchesAny(new char[]{'x'}));
    }

    @Test
    public void testMatchesAnySortedMethodWithCharArray() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'l', 'o'}));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'o'}));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'H'}));
        Assert.assertFalse(characterReader.matchesAnySorted(new char[]{'x'}));
    }

    @Test
    public void testMatchesLetterMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.consume();
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigitMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello123 World456!"));
        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.consume();
        characterReader.consume();
        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.consumeLetter();
        characterReader.consumeDigit();
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsumeMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matchConsume("llo"));
        Assert.assertEquals("llo", characterReader.toString());
        characterReader.consume();
        Assert.assertTrue(characterReader.matchConsume("World"));
        Assert.assertEquals("World", characterReader.toString());
    }

    @Test
    public void testMatchConsumeIgnoreCaseMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("llo"));
        Assert.assertEquals("llo", characterReader.toString());
        characterReader.consume();
        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("World"));
        Assert.assertEquals("World", characterReader.toString());
    }

    @Test
    public void testContainsIgnoreCaseMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Hello World</title></head><body></body></html>"));
        Assert.assertTrue(characterReader.containsIgnoreCase("<title>"));
        Assert.assertTrue(characterReader.containsIgnoreCase("world"));
        Assert.assertFalse(characterReader.containsIgnoreCase("xxx"));
    }

    @Test
    public void testToStringMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        String result = characterReader.toString();
        Assert.assertEquals("Hello World!", result);
    }
}