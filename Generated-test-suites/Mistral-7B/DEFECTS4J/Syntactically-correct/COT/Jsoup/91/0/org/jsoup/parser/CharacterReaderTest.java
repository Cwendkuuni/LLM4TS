package org.jsoup.parser; // update the package name if necessary

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class CharacterReaderTest {

    

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharSequence expectedInput = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(expectedInput.toString()));
        Assert.assertEquals(expectedInput.length(), characterReader.pos());
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReaderConstructorWithNullReader() {
        new CharacterReader(null);
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharSequence expectedInput = "Hello World!";
        CharacterReader characterReader = new CharacterReader(expectedInput.toString());
        Assert.assertEquals(expectedInput.length(), characterReader.pos());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterReaderConstructorWithNegativeStringLength() {
        new CharacterReader("Hello World!", -1);
    }

    @Test
    public void testBufferUp() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.bufferUp();
        Assert.assertEquals("Hello World!", characterReader.toString());
    }

    @Test(expected = IOException.class)
    public void testBufferUpWithIOException() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!")) {
            @Override
            public void bufferUp() throws IOException {
                throw new IOException("Simulated IOException");
            }
        };

        characterReader.bufferUp();
    }

    @Test
    public void testPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals(0, characterReader.pos());

        characterReader.consume();
        Assert.assertEquals(1, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertFalse(characterReader.isEmpty());

        characterReader.consumeToEnd();
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals('H', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());

        characterReader.consume();
        Assert.assertEquals('l', characterReader.current());

        characterReader.consumeToEnd();
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.consume();
        characterReader.unconsume();
        Assert.assertEquals('H', characterReader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        Assert.assertEquals('H', characterReader.current());
    }

    @Test(expected = IOException.class)
    public void testRewindToMarkWithIOException() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!")) {
            @Override
            public void mark() {
                throw new IOException("Simulated IOException");
            }
        };

        characterReader.mark();
        characterReader.rewindToMark();
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals(-1, characterReader.nextIndexOf('z'));
        Assert.assertEquals(1, characterReader.nextIndexOf('e'));
        Assert.assertEquals(10, characterReader.nextIndexOf('W'));
        characterReader.consumeToEnd();
        Assert.assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfWithCharSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals(-1, characterReader.nextIndexOf("xyz"));
        Assert.assertEquals(2, characterReader.nextIndexOf("ll"));
        Assert.assertEquals(6, characterReader.nextIndexOf("World"));
        characterReader.consumeToEnd();
        Assert.assertEquals(-1, characterReader.nextIndexOf("xyz"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        String expectedOutput = "Hello ";
        String actualOutput = characterReader.consumeTo(' ');
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        String expectedOutput = "Hello World!";
        String actualOutput = characterReader.consumeToEnd();
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        String expectedOutput = "Hello";
        String actualOutput = characterReader.consumeLetterSequence();
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello123World!"));
        String expectedOutput = "Hello123";
        String actualOutput = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("0x1A"));
        String expectedOutput = "0x1A";
        String actualOutput = characterReader.consumeHexSequence();
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("12345"));
        String expectedOutput = "12345";
        String actualOutput = characterReader.consumeDigitSequence();
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matches('H'));
        Assert.assertTrue(characterReader.matches('e'));
        Assert.assertFalse(characterReader.matches('z'));
        characterReader.consumeToEnd();
        Assert.assertFalse(characterReader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("hello"));
        characterReader.consumeToEnd();
        Assert.assertFalse(characterReader.matchesIgnoreCase("world"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matchesAny('H', 'e'));
        characterReader.consumeToEnd();
        Assert.assertFalse(characterReader.matchesAny('z'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Character[] charArray = {'H', 'e', 'z', 'W'};
        Assert.assertTrue(characterReader.matchesAnySorted(charArray));
        characterReader.consumeToEnd();
        Assert.assertFalse(characterReader.matchesAnySorted(new Character[]{'z'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.consumeToEnd();
        Assert.assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("12345"));
        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.consumeToEnd();
        Assert.assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        String expectedOutput = "Hello";
        String actualOutput = characterReader.matchConsume("Hello");
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        String expectedOutput = "Hello";
        String actualOutput = characterReader.matchConsumeIgnoreCase("hello");
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><title>Hello World</title></html>"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/title"));
        characterReader.consumeToEnd();
        Assert.assertFalse(characterReader.containsIgnoreCase("/title"));
    }

    
}