package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * CharacterReaderTest class to test CharacterReader methods.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstruction() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertNotNull(characterReader);

        characterReader = new CharacterReader(reader, 10);
        Assert.assertEquals("CharacterReader buffer size should match the supplied value", 10, characterReader.charBuf.length);
    }

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        Assert.assertTrue("CharacterReader should be empty", characterReader.isEmpty());
        Assert.assertEquals("Current char should be EOF", CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testCharacterReaderNonEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        Assert.assertFalse("CharacterReader should not be empty", characterReader.isEmpty());
        Assert.assertEquals("Current char should be T", 'T', characterReader.current());
    }

    @Test
    public void testBufferUp() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString123"));
        characterReader.bufferUp();
        Assert.assertEquals("CharacterReader buffer position should be updated", 2, characterReader.bufPos);
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader characterReader = new CharacterReader(new IOException("Test IOException").getReader());
        characterReader.bufferUp();
    }

    @Test
    public void testPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        Assert.assertEquals("CharacterReader position should match the reader position", 0, characterReader.pos());

        characterReader.consume();
        Assert.assertEquals("CharacterReader position should be updated", 1, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        Assert.assertFalse("CharacterReader should not be empty", characterReader.isEmpty());

        characterReader.consumeToEnd();
        Assert.assertTrue("CharacterReader should be empty after consuming all characters", characterReader.isEmpty());
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        Assert.assertEquals("Current char should be T", 'T', characterReader.current());
        characterReader.consume();
        Assert.assertEquals("Current char should be S", 'S', characterReader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        characterReader.consume();
        characterReader.unconsume();
        Assert.assertEquals("Current char should be T", 'T', characterReader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        characterReader.advance();
        Assert.assertEquals("Current char should be S", 'S', characterReader.current());
    }

    @Test
    public void testMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        characterReader.mark();
        characterReader.consume();
        Assert.assertTrue("CharacterReader should be able to rewind to the mark", characterReader.bufPos == 0);
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Next index of 'l' should be 3", 3, characterReader.nextIndexOf('l'));
        Assert.assertEquals("Next index of 'x' should be -1", -1, characterReader.nextIndexOf('x'));

        characterReader = new CharacterReader(new StringReader("TestString123"));
        Assert.assertEquals("Next index of '2' should be 2", 2, characterReader.nextIndexOf('2'));
        Assert.assertEquals("Next index of 'z' should be -1", -1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Next index of 'lle' should be 3", 3, characterReader.nextIndexOf("lle"));
        Assert.assertEquals("Next index of 'xxx' should be -1", -1, characterReader.nextIndexOf("xxx"));

        characterReader = new CharacterReader(new StringReader("TestString123"));
        Assert.assertEquals("Next index of '123' should be 2", 2, characterReader.nextIndexOf("123"));
        Assert.assertEquals("Next index of 'zzz' should be -1", -1, characterReader.nextIndexOf("zzz"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String result = characterReader.consumeTo('l');
        Assert.assertEquals("Consumed string should be 'ello'", "ello", result);

        result = characterReader.consumeTo("ll");
        Assert.assertEquals("Consumed string should be 'elloWorld'", "elloWorld", characterReader.toString());
        Assert.assertEquals("Consumed string should be 'll'", "ll", result);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString123"));
        String result = characterReader.consumeToEnd();
        Assert.assertEquals("Consumed string should be 'TestString123'", "TestString123", result);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestLetterSequence"));
        String result = characterReader.consumeLetterSequence();
        Assert.assertEquals("Consumed string should be 'TestLetterSequence'", "TestLetterSequence", result);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestLetterThenDigitSequence"));
        String result = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Consumed string should be 'TestLetterThenDigitSequence123'", "TestLetterThenDigitSequence123", result);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestHexSequence"));
        String result = characterReader.consumeHexSequence();
        Assert.assertEquals("Consumed string should be 'Test%25Sequence'", "Test%25Sequence", result);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestDigitSequence"));
        String result = characterReader.consumeDigitSequence();
        Assert.assertEquals("Consumed string should be 'Test1234567890'", "Test1234567890", result);
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        Assert.assertTrue("CharacterReader should match 'T'", characterReader.matches('T'));
        Assert.assertFalse("CharacterReader should not match 'S'", characterReader.matches('S'));

        characterReader.consume();
        Assert.assertFalse("CharacterReader should not match 'T' after consuming it", characterReader.matches('T'));
        Assert.assertTrue("CharacterReader should match 'S'", characterReader.matches('S'));
    }

    @Test
    public void testMatchesString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        Assert.assertTrue("CharacterReader should match 'TestString'", characterReader.matches("TestString"));
        Assert.assertFalse("CharacterReader should not match 'HelloWorld'", characterReader.matches("HelloWorld"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        Assert.assertTrue("CharacterReader should match 'teststring'", characterReader.matchesIgnoreCase("teststring"));
        Assert.assertFalse("CharacterReader should not match 'HelloWorld'", characterReader.matchesIgnoreCase("HelloWorld"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        Assert.assertTrue("CharacterReader should match 'T'", characterReader.matchesAny('T'));
        Assert.assertTrue("CharacterReader should match 'S'", characterReader.matchesAny('S'));
        Assert.assertFalse("CharacterReader should not match 'X'", characterReader.matchesAny('X'));

        characterReader.consume();
        Assert.assertFalse("CharacterReader should not match 'T' after consuming it", characterReader.matchesAny('T'));
        Assert.assertTrue("CharacterReader should match 'S'", characterReader.matchesAny('S'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        Assert.assertTrue("CharacterReader should match 'S'", characterReader.matchesAnySorted('S'));
        Assert.assertTrue("CharacterReader should match 'T'", characterReader.matchesAnySorted('T'));
        Assert.assertFalse("CharacterReader should not match 'X'", characterReader.matchesAnySorted('X'));

        characterReader.consume();
        Assert.assertFalse("CharacterReader should not match 'T' after consuming it", characterReader.matchesAnySorted('T'));
        Assert.assertTrue("CharacterReader should match 'S'", characterReader.matchesAnySorted('S'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        Assert.assertTrue("CharacterReader should match 'T'", characterReader.matchesLetter());
        Assert.assertTrue("CharacterReader should match 'S'", characterReader.matchesLetter());
        Assert.assertFalse("CharacterReader should not match 'X'", characterReader.matchesLetter());

        characterReader.consume();
        Assert.assertFalse("CharacterReader should not match 'T' after consuming it", characterReader.matchesLetter());
        Assert.assertTrue("CharacterReader should match 'S'", characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        Assert.assertFalse("CharacterReader should not match '0'", characterReader.matchesDigit());
        Assert.assertTrue("CharacterReader should match '1'", characterReader.matchesDigit());
        Assert.assertTrue("CharacterReader should match '2'", characterReader.matchesDigit());
        Assert.assertTrue("CharacterReader should match '3'", characterReader.matchesDigit());
        Assert.assertTrue("CharacterReader should match '4'", characterReader.matchesDigit());
        Assert.assertTrue("CharacterReader should match '5'", characterReader.matchesDigit());
        Assert.assertTrue("CharacterReader should match '6'", characterReader.matchesDigit());
        Assert.assertTrue("CharacterReader should match '7'", characterReader.matchesDigit());
        Assert.assertTrue("CharacterReader should match '8'", characterReader.matchesDigit());
        Assert.assertTrue("CharacterReader should match '9'", characterReader.matchesDigit());

        characterReader.consume();
        Assert.assertFalse("CharacterReader should not match '1' after consuming it", characterReader.matchesDigit());
        Assert.assertTrue("CharacterReader should match '2'", characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsumeString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        String result = characterReader.consumeTo("ll").toString();
        Assert.assertEquals("Consumed string should be 'Testll'", "Testll", characterReader.toString());
        Assert.assertTrue("CharacterReader should match 'll'", characterReader.matchConsume("ll"));
    }

    @Test
    public void testMatchConsumeStringIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        String result = characterReader.consumeTo("ll").toString();
        Assert.assertEquals("Consumed string should be 'Testll'", "Testll", characterReader.toString());
        Assert.assertTrue("CharacterReader should match 'll'", characterReader.matchConsumeIgnoreCase("ll"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test<title>TestString<title>Test</title>"));
        Assert.assertTrue("CharacterReader should contain '<title>'", characterReader.containsIgnoreCase("<title>"));
        Assert.assertTrue("CharacterReader should contain '</title>'", characterReader.containsIgnoreCase("</title>"));
        Assert.assertFalse("CharacterReader should not contain '<head>'", characterReader.containsIgnoreCase("<head>"));
    }

    @Test
    public void testToString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        Assert.assertEquals("CharacterReader toString should return the remaining string", "TestString", characterReader.toString());
    }
}