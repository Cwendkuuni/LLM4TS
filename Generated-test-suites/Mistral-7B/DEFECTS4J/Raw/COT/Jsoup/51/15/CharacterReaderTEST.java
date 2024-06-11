package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.parser.CharacterReader.EOF;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderInstance() {
        String input = "TestCharacterReader";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(input.toCharArray().length, reader.length());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        reader = new CharacterReader("Test");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrentCharacter() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals('T', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsumeCharacter() {
        CharacterReader reader = new CharacterReader("Test");
        reader.consume();
        assertEquals('e', reader.current());

        reader.consume();
        assertEquals('s', reader.current());

        reader.consume();
        assertEquals('t', reader.current());

        reader.consume();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testConsumePastEndOfInput() {
        CharacterReader reader = new CharacterReader("Test");
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume(); // should throw an IndexOutOfBoundsException
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("Test");
        reader.consume();
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals("T", reader.consumeAsString());
        reader.consume();
        assertEquals("st", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOfCharacter() {
        CharacterReader reader = new CharacterReader("testTest");
        assertEquals(2, reader.nextIndexOf('t'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader("test test");
        assertEquals(2, reader.nextIndexOf("es"));
    }

    @Test
    public void testConsumeToCharacter() {
        CharacterReader reader = new CharacterReader("testTest");
        assertEquals("est", reader.consumeTo('t'));
    }

    @Test
    public void testConsumeToCharSequence() {
        CharacterReader reader = new CharacterReader("test test");
        assertEquals("es test", reader.consumeTo("es"));
    }

    @Test
    public void testConsumeToAnyCharacter() {
        CharacterReader reader = new CharacterReader("123ABCabc123");
        assertEquals("123", reader.consumeToAny('1', '2'));
    }

    @Test
    public void testConsumeToAnyCharacterSorted() {
        CharacterReader reader = new CharacterReader("123ABCabc123");
        assertEquals("123", reader.consumeToAnySorted('1', '2', '3'));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("&<token null");
        assertEquals("&<token null", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("<tag attribute=\"value\">content</tag>");
        assertEquals("<tag", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("<tag attribute=\"value\">content</tag>");
        assertEquals("content</tag>", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("LetterSequence");
        assertEquals("LetterSequence", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("Letter123DigitSequence");
        assertEquals("Letter123DigitSequence", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("&x1234;");
        assertEquals("&x1234;", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234567890");
        assertEquals("1234567890", reader.consumeDigitSequence());
    }

    @Test
    public void testMatchesCharacter() {
        CharacterReader reader = new CharacterReader("Test");
        assertTrue(reader.matches('T'));
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('s'));
    }

    @Test
    public void testMatchesString() {
        CharacterReader reader = new CharacterReader("TestTest");
        assertTrue(reader.matches("Te"));
        assertTrue(reader.matches("es"));
    }

    @Test
    public void testMatchesIgnoreCaseString() {
        CharacterReader reader = new CharacterReader("TestTest");
        assertTrue(reader.matchesIgnoreCase("te"));
        assertTrue(reader.matchesIgnoreCase("es"));
    }

    @Test
    public void testMatchesAnyCharacter() {
        CharacterReader reader = new CharacterReader("1234567890");
        assertTrue(reader.matchesAny('1', '2', '3', '4', '5', '6', '7', '8', '9'));
    }

    @Test
    public void testMatchesAnyCharacterSorted() {
        CharacterReader reader = new CharacterReader("1234567890");
        assertTrue(reader.matchesAnySorted('1', '2', '3', '4', '5', '6', '7', '8', '9'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("TestLetter");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("Test123");
        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsumeString() {
        CharacterReader reader = new CharacterReader("TestTest");
        assertTrue(reader.matchConsume("Te"));
        assertTrue(reader.matchConsume("es"));
    }

    @Test
    public void testMatchConsumeIgnoreCaseString() {
        CharacterReader reader = new CharacterReader("TestTest");
        assertTrue(reader.matchConsumeIgnoreCase("te"));
        assertTrue(reader.matchConsumeIgnoreCase("es"));
    }

    @Test
    public void testContainsIgnoreCaseString() {
        CharacterReader reader = new CharacterReader("<html><head><title>TestTitle</title></head><body>TestBody</body></html>");
        assertTrue(reader.containsIgnoreCase("title"));
        assertTrue(reader.containsIgnoreCase("body"));
    }

    @Test(expected = NullPointerException.class)
    public void testCtorNullInput() {
        new CharacterReader(null);
    }
}