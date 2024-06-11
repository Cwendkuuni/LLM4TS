package org.jsoup.parser; // replace with the actual package name if needed

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        Reader input = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(input);

        Assert.assertEquals("Hello World!", characterReader.toString());
        Assert.assertEquals(13, characterReader.pos());
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        Assert.assertEquals("Hello World!", characterReader.toString());
        Assert.assertEquals(13, characterReader.pos());
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testBufferUpMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.bufferUp();

        Assert.assertEquals(1, characterReader.bufLength);
        Assert.assertEquals(0, characterReader.bufPos);
        Assert.assertEquals(0, characterReader.bufMark);
        Assert.assertEquals(13, characterReader.readerPos);
        Assert.assertEquals(13, characterReader.pos());
        Assert.assertFalse(characterReader.isEmpty());

        characterReader.bufferUp();

        Assert.assertEquals(13, characterReader.bufLength);
        Assert.assertEquals(0, characterReader.bufPos);
        Assert.assertEquals(0, characterReader.bufMark);
        Assert.assertEquals(26, characterReader.readerPos);
        Assert.assertEquals(13, characterReader.pos());
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testConsumeMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        Assert.assertEquals('H', characterReader.consume());
        Assert.assertEquals('e', characterReader.consume());
        Assert.assertEquals('l', characterReader.consume());
        Assert.assertEquals('l', characterReader.consume());
        Assert.assertEquals('o', characterReader.consume());
        Assert.assertEquals(' ', characterReader.consume());
        Assert.assertEquals('W', characterReader.consume());
        Assert.assertEquals('o', characterReader.consume());
        Assert.assertEquals('r', characterReader.consume());
        Assert.assertEquals('l', characterReader.consume());
        Assert.assertEquals('d', characterReader.consume());
        Assert.assertEquals('!', characterReader.consume());
        Assert.assertEquals(EOF, characterReader.consume());
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeToMethodWithSingleCharacter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        String consumed = characterReader.consumeTo('l');
        Assert.assertEquals("He", consumed);
        Assert.assertEquals(5, characterReader.pos());
    }

    @Test
    public void testConsumeToMethodWithEmptyInput() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));

        String consumed = characterReader.consumeTo('a');
        Assert.assertEquals("", consumed);
        Assert.assertEquals(0, characterReader.pos());
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testConsumeToMethodWithIOException() {
        CharacterReader characterReader = new CharacterReader(new StringReader("") {
            @Override
            public int read() throws IOException {
                throw new IOException("Test IOException");
            }
        });

        String consumed = characterReader.consumeTo('a');
    }

    @Test
    public void testConsumeToMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        String consumed = characterReader.consumeTo("orl");
        Assert.assertEquals("Hello Wo", consumed);
        Assert.assertEquals(9, characterReader.pos());
    }

    @Test
    public void testConsumeToAnyMethodWithSingleCharacter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        String consumed = characterReader.consumeToAny('l');
        Assert.assertEquals("Hello Wo", consumed);
        Assert.assertEquals(9, characterReader.pos());
    }

    @Test
    public void testConsumeToAnyMethodWithMultipleCharacters() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        String consumed = characterReader.consumeToAny(new char[]{'l', 'd', '!'});
        Assert.assertEquals("Hello W", consumed);
        Assert.assertEquals(7, characterReader.pos());
    }

    @Test
    public void testConsumeToAnyMethodWithEmptyInput() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));

        String consumed = characterReader.consumeToAny(new char[0]);
        Assert.assertEquals("", consumed);
        Assert.assertEquals(0, characterReader.pos());
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testConsumeToAnyMethodWithIOException() {
        CharacterReader characterReader = new CharacterReader(new StringReader("") {
            @Override
            public int read() throws IOException {
                throw new IOException("Test IOException");
            }
        });

        characterReader.consumeToAny(new char[0]);
    }

    @Test
    public void testConsumeDataMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&<test>"));

        String consumed = characterReader.consumeData();
        Assert.assertEquals("&<test>", consumed);
        Assert.assertEquals(6, characterReader.pos());
    }

    @Test
    public void testConsumeTagNameMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<title>Hello World!</title>"));

        String consumed = characterReader.consumeTagName();
        Assert.assertEquals("title", consumed);
        Assert.assertEquals(7, characterReader.pos());
    }

    @Test
    public void testConsumeToEndMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        String consumed = characterReader.consumeToEnd();
        Assert.assertEquals("Hello World!", consumed);
        Assert.assertEquals(13, characterReader.pos());
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        String consumed = characterReader.consumeLetterSequence();
        Assert.assertEquals("Hello", consumed);
        Assert.assertEquals(6, characterReader.pos());
    }

    @Test
    public void testConsumeLetterThenDigitSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello1234World!"));

        String consumed = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Hello123", consumed);
        Assert.assertEquals(10, characterReader.pos());
    }

    @Test
    public void testMatchesMethodWithSingleCharacter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        Assert.assertTrue(characterReader.matches('H'));
        Assert.assertTrue(characterReader.matches('e'));
        Assert.assertFalse(characterReader.matches('!'));
    }

    @Test
    public void testMatchesMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        Assert.assertTrue(characterReader.matches("He"));
        Assert.assertTrue(characterReader.matches("hel"));
        Assert.assertFalse(characterReader.matches("xl"));
    }

    @Test
    public void testMatchesIgnoreCaseMethodWithSingleCharacter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        Assert.assertTrue(characterReader.matchesIgnoreCase("h"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("e"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("!"));
    }

    @Test
    public void testMatchesIgnoreCaseMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        Assert.assertTrue(characterReader.matchesIgnoreCase("he"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("hel"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("xl"));
    }

    @Test
    public void testMatchesAnyMethodWithSingleCharacter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        Assert.assertTrue(characterReader.matchesAny('e'));
        Assert.assertTrue(characterReader.matchesAny('l'));
        Assert.assertFalse(characterReader.matchesAny('!'));
    }

    @Test
    public void testMatchesAnyMethodWithMultipleCharacters() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        Assert.assertTrue(characterReader.matchesAny(new char[]{'e', 'l'}));
        Assert.assertTrue(characterReader.matchesAny(new char[]{'l', 'o'}));
        Assert.assertFalse(characterReader.matchesAny(new char[]{'!', 'o'}));
    }

    @Test
    public void testMatchesLetterMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertTrue(characterReader.matchesLetter());
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
    public void testMatchesDigitMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello12345World!"));

        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsumeMethodWithSingleCharacter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        Assert.assertTrue(characterReader.matchConsume("He"));
        Assert.assertEquals(2, characterReader.pos());
    }

    @Test
    public void testMatchConsumeMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        Assert.assertTrue(characterReader.matchConsume("Hello"));
        Assert.assertEquals(6, characterReader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCaseMethodWithSingleCharacter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("h"));
        Assert.assertEquals(1, characterReader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCaseMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));

        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("hel"));
        Assert.assertEquals(4, characterReader.pos());
    }

    @Test
    public void testContainsIgnoreCaseMethodWithSingleCharacter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Hello World!</title></head><body>"));

        Assert.assertTrue(characterReader.containsIgnoreCase("t"));
        Assert.assertTrue(characterReader.containsIgnoreCase("h"));
        Assert.assertFalse(characterReader.containsIgnoreCase("!"));
    }

    @Test
    public void testContainsIgnoreCaseMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Hello World!</title></head><body>"));

        Assert.assertTrue(characterReader.containsIgnoreCase("title"));
        Assert.assertTrue(characterReader.containsIgnoreCase("hello"));
        Assert.assertFalse(characterReader.containsIgnoreCase("body"));
    }
}