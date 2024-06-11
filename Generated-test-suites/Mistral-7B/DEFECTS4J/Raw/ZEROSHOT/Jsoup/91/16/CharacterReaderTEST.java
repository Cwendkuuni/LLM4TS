package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

/**
 * CharacterReaderTest class to test all methods in CharacterReader class
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderWithReaderInput() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", characterReader.toString());
        Assert.assertEquals('H', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
        characterReader.unconsume();
        Assert.assertEquals('H', characterReader.current());
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());
        characterReader.mark();
        characterReader.rewindToMark();
        Assert.assertEquals('H', characterReader.current());
        characterReader.isEmpty();
        Assert.assertFalse(characterReader.isEmpty());
        characterReader.isEmptyNoBufferUp();
        Assert.assertTrue(characterReader.isEmptyNoBufferUp());
        characterReader.pos();
        Assert.assertEquals(13, characterReader.pos());
        characterReader.isEmpty();
        Assert.assertFalse(characterReader.isEmpty());
        characterReader.consumeTo('l');
        Assert.assertEquals("Hello ", characterReader.toString());
        characterReader.consumeToAny('l');
        Assert.assertEquals("Hello Wor", characterReader.toString());
        characterReader.consumeToEnd();
        Assert.assertEquals("", characterReader.toString());
        characterReader.consumeLetterSequence();
        Assert.assertEquals("orld", characterReader.toString());
        characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("orld0", characterReader.toString());
        characterReader.consumeHexSequence();
        Assert.assertEquals("orld", characterReader.toString());
        characterReader.consumeDigitSequence();
        Assert.assertEquals("orld", characterReader.toString());
        characterReader.matches('l');
        Assert.assertTrue(characterReader.matches('l'));
        characterReader.matches("Hello");
        Assert.assertFalse(characterReader.matches("Hello"));
        characterReader.matchesIgnoreCase("hello");
        Assert.assertTrue(characterReader.matchesIgnoreCase("hello"));
        characterReader.matchesAny('l', 'o');
        Assert.assertTrue(characterReader.matchesAny('l', 'o'));
        characterReader.matchesAnySorted(new char[]{'l', 'o'});
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'l', 'o'}));
        characterReader.matchesLetter();
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.matchesDigit();
        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.matchConsume("ello");
        Assert.assertEquals("orld", characterReader.toString());
        characterReader.matchConsumeIgnoreCase("hello");
        Assert.assertEquals("", characterReader.toString());
        characterReader.containsIgnoreCase("llo");
        Assert.assertTrue(characterReader.containsIgnoreCase("llo"));
        characterReader.consumeData();
        Assert.assertEquals("orld", characterReader.toString());
        characterReader.consumeTagName();
        Assert.assertEquals("", characterReader.toString());
        characterReader.consumeToEnd();
        Assert.assertEquals("", characterReader.toString());
        characterReader.consumeTo('>');
        Assert.assertEquals("", characterReader.toString());
        characterReader.consumeTo("<");
        Assert.assertEquals("orld", characterReader.toString());
        characterReader.consumeTo("<title");
        Assert.assertEquals("orld<title", characterReader.toString());
        characterReader.consumeTo("<title>");
        Assert.assertEquals("orld<title>", characterReader.toString());
        characterReader.consumeTo("<title>Hello");
        Assert.assertEquals("orld<title>Hello", characterReader.toString());

        try {
            characterReader.consumeTo('z');
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.consumeToAny(new char[]{'z', 'x'});
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.consumeToAny(new char[]{'z', 'x', 'y'});
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.consumeToAnySorted(new char[]{'z', 'x', 'y'});
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.consumeToAnySorted(new char[]{'z', 'y', 'x'});
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.consumeHexSequence();
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.consumeDigitSequence();
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.consumeLetterSequence();
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.consumeLetterThenDigitSequence();
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.matches('z');
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.matches("zz");
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.matchesIgnoreCase("ZZ");
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.matchesAny('z');
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.matchesAnySorted(new char[]{'z'});
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.matchesLetter();
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.matchesDigit();
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.matchConsume("zz");
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.matchConsumeIgnoreCase("ZZ");
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.containsIgnoreCase("ZZ");
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }
    }

    @Test
    public void testCharacterReaderWithStringInput() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertEquals("Hello World", characterReader.toString());
        Assert.assertEquals('H', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
        characterReader.unconsume();
        Assert.assertEquals('H', characterReader.current());
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());
        characterReader.mark();
        characterReader.rewindToMark();
        Assert.assertEquals('H', characterReader.current());
        characterReader.isEmpty();
        Assert.assertFalse(characterReader.isEmpty());
        characterReader.isEmptyNoBufferUp();
        Assert.assertTrue(characterReader.isEmptyNoBufferUp());
        characterReader.pos();
        Assert.assertEquals(13, characterReader.pos());
        characterReader.isEmpty();
        Assert.assertFalse(characterReader.isEmpty());
        characterReader.consumeTo('l');
        Assert.assertEquals("Hello ", characterReader.toString());
        characterReader.consumeToAny('l');
        Assert.assertEquals("Hello Wor", characterReader.toString());
        characterReader.consumeToEnd();
        Assert.assertEquals("", characterReader.toString());
        characterReader.consumeLetterSequence();
        Assert.assertEquals("orld", characterReader.toString());
        characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("orld0", characterReader.toString());
        characterReader.consumeHexSequence();
        Assert.assertEquals("orld", characterReader.toString());
        characterReader.consumeDigitSequence();
        Assert.assertEquals("orld", characterReader.toString());
        characterReader.matches('l');
        Assert.assertTrue(characterReader.matches('l'));
        characterReader.matches("Hello");
        Assert.assertFalse(characterReader.matches("Hello"));
        characterReader.matchesIgnoreCase("hello");
        Assert.assertTrue(characterReader.matchesIgnoreCase("hello"));
        characterReader.matchesAny('l', 'o');
        Assert.assertTrue(characterReader.matchesAny('l', 'o'));
        characterReader.matchesAnySorted(new char[]{'l', 'o'});
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'l', 'o'}));
        characterReader.matchesLetter();
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.matchesDigit();
        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.matchConsume("ello");
        Assert.assertEquals("orld", characterReader.toString());
        characterReader.matchConsumeIgnoreCase("hello");
        Assert.assertEquals("", characterReader.toString());
        characterReader.containsIgnoreCase("llo");
        Assert.assertTrue(characterReader.containsIgnoreCase("llo"));
        characterReader.consumeData();
        Assert.assertEquals("orld", characterReader.toString());
        characterReader.consumeTagName();
        Assert.assertEquals("", characterReader.toString());
        characterReader.consumeToEnd();
        Assert.assertEquals("", characterReader.toString());
        characterReader.consumeTo('>');
        Assert.assertEquals("", characterReader.toString());
        characterReader.consumeTo("<");
        Assert.assertEquals("orld", characterReader.toString());
        characterReader.consumeTo("<title");
        Assert.assertEquals("orld<title", characterReader.toString());
        characterReader.consumeTo("<title>");
        Assert.assertEquals("orld<title>", characterReader.toString());
        characterReader.consumeTo("<title>Hello");
        Assert.assertEquals("orld<title>Hello", characterReader.toString());

        try {
            characterReader.consumeTo('z');
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.consumeToAny(new char[]{'z', 'x'});
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.consumeToAny(new char[]{'z', 'x', 'y'});
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.consumeToAnySorted(new char[]{'z', 'x', 'y'});
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.consumeToAnySorted(new char[]{'z', 'y', 'x'});
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.consumeHexSequence();
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.consumeDigitSequence();
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.consumeLetterSequence();
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.consumeLetterThenDigitSequence();
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.matches('z');
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.matches("zz");
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.matchesIgnoreCase("ZZ");
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.matchesAny('z');
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.matchesAnySorted(new char[]{'z'});
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.matchesLetter();
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.matchesDigit();
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.matchConsume("zz");
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.matchConsumeIgnoreCase("ZZ");
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }

        try {
            characterReader.containsIgnoreCase("ZZ");
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            //expected
        }
    }

    @Test(expected = IOException.class)
    public void testIOExceptionWhenConsumingCharacterWithInvalidInput() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        characterReader.consume();
    }

    @Test(expected = IOException.class)
    public void testIOExceptionWhenConsumingStringWithInvalidInput() {
        CharacterReader characterReader = new CharacterReader("");
        characterReader.consumeTo('a');
    }
}