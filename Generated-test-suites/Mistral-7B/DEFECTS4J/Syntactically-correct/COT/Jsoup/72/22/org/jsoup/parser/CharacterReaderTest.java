package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReaderInput() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertNotNull(characterReader);
    }

    @Test
    public void testCharacterReaderConstructorWithStringInput() {
        CharacterReader characterReader = new CharacterReader("test");
        assertNotNull(characterReader);
    }

    @Test
    public void testCharacterReaderConstructorWithReaderInputAndSize() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"), 5);
        assertNotNull(characterReader);
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOExceptionWhenBufferingUpFails() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"), 5);
        characterReader.reader.close();
        characterReader.bufferUp();
    }

    @Test
    public void testPosMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertEquals(0, characterReader.pos());

        characterReader.consume('t');
        characterReader.consume('e');
        characterReader.consume('s');
        assertEquals(3, characterReader.pos());
    }

    @Test
    public void testIsEmptyMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertFalse(characterReader.isEmpty());

        characterReader.consume('t');
        characterReader.consume('e');
        characterReader.consume('s');
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrentMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));

        assertEquals('t', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
        characterReader.consume();
        assertEquals('s', characterReader.current());
        characterReader.consume();
        assertEquals('t', characterReader.current());
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));

        characterReader.consume();
        assertEquals('t', characterReader.current());
        assertEquals('e', characterReader.current());

        characterReader.consume();
        characterReader.consume();
        assertEquals('s', characterReader.current());
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testUnconsumeMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));

        characterReader.consume();
        assertEquals('t', characterReader.current());
        characterReader.unconsume();
        assertEquals('t', characterReader.current());

        characterReader.consume();
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('s', characterReader.current());
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testAdvanceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));

        characterReader.consume();
        characterReader.advance();
        assertEquals('e', characterReader.current());

        characterReader.consume();
        characterReader.consume();
        characterReader.advance();
        assertEquals('s', characterReader.current());
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testMarkMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));

        characterReader.consume();
        characterReader.mark();

        characterReader.consume();
        characterReader.consume();
        assertEquals('s', characterReader.current());

        characterReader.rewindToMark();
        assertEquals('t', characterReader.current());
    }

    @Test
    public void testNextIndexOfMethodWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));

        assertEquals(1, characterReader.nextIndexOf('e'));
        assertEquals(4, characterReader.nextIndexOf('n'));
        assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfMethodWithMultiChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("testtest"));

        assertEquals(1, characterReader.nextIndexOf("es"));
        assertEquals(5, characterReader.nextIndexOf("est"));
        assertEquals(-1, characterReader.nextIndexOf("zzz"));
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOExceptionWhenConsumeToMethodFails() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"), 5);
        characterReader.consumeTo("test".length());
    }

    @Test
    public void testConsumeToMethodWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));

        String result = characterReader.consumeTo('t');
        assertEquals("est", result);

        String result2 = characterReader.consumeTo('s');
        assertEquals("est", result2);
    }

    @Test
    public void testConsumeToMethodWithMultiChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));

        String result = characterReader.consumeTo("es");
        assertEquals("est", result);

        String result2 = characterReader.consumeTo("est");
        assertEquals("test", result2);
    }

    @Test
    public void testConsumeToAnyMethodWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));

        String result = characterReader.consumeToAny('s');
        assertEquals("", result);

        String result2 = characterReader.consumeToAny('t');
        assertEquals("test", result2);
    }

    @Test
    public void testConsumeToAnyMethodWithMultiCharSorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));

        String result = characterReader.consumeToAnySorted(new char[]{'e', 's'});
        assertEquals("es", result);

        String result2 = characterReader.consumeToAnySorted(new char[]{'s', 'e'});
        assertEquals("es", result2);
    }

    @Test
    public void testConsumeDataMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&<test"));

        String result = characterReader.consumeData();
        assertEquals("&<", result);
    }

    @Test
    public void testConsumeTagNameMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<\ntest>"));

        String result = characterReader.consumeTagName();
        assertEquals("<", result);
    }

    @Test
    public void testConsumeToEndMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));

        String result = characterReader.consumeToEnd();
        assertEquals("test", result);
    }

    @Test
    public void testConsumeLetterSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));

        String result = characterReader.consumeLetterSequence();
        assertEquals("test", result);
    }

    @Test
    public void testConsumeLetterThenDigitSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test123"));

        String result = characterReader.consumeLetterThenDigitSequence();
        assertEquals("test123", result);
    }

    @Test
    public void testConsumeHexSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&#x65;"));

        String result = characterReader.consumeHexSequence();
        assertEquals("e", result);
    }

    @Test
    public void testConsumeDigitSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("123"));

        String result = characterReader.consumeDigitSequence();
        assertEquals("123", result);
    }

    @Test
    public void testMatchesMethodWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));

        assertTrue(characterReader.matches('t'));
        assertFalse(characterReader.matches('e'));
    }

    @Test
    public void testMatchesMethodWithMultiChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));

        assertTrue(characterReader.matches("es"));
        assertFalse(characterReader.matches("esx"));
    }

    @Test
    public void testMatchesIgnoreCaseMethodWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));

        assertTrue(characterReader.matchesIgnoreCase("T"));
        assertFalse(characterReader.matchesIgnoreCase("z"));
    }

    @Test
    public void testMatchesIgnoreCaseMethodWithMultiChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));

        assertTrue(characterReader.matchesIgnoreCase("Es"));
        assertFalse(characterReader.matchesIgnoreCase("Zs"));
    }

    @Test
    public void testMatchesAnyMethodWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));

        assertTrue(characterReader.matchesAny('e'));
        assertFalse(characterReader.matchesAny('z'));
    }

    @Test
    public void testMatchesAnySortedMethodWithMultiChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));

        assertTrue(characterReader.matchesAnySorted(new char[]{'e', 's'}));
        assertFalse(characterReader.matchesAnySorted(new char[]{'z', 's'}));
    }

    @Test
    public void testMatchesLetterMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));

        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchesDigitMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("123"));

        assertFalse(characterReader.matchesLetter());
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsumeMethodWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));

        assertTrue(characterReader.matchConsume('t'));
        assertFalse(characterReader.matchConsume('e'));

        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testMatchConsumeMethodWithMultiChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));

        assertTrue(characterReader.matchConsume("es"));
        assertFalse(characterReader.matchConsume("esx"));

        assertEquals(3, characterReader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCaseMethodWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));

        assertTrue(characterReader.matchConsumeIgnoreCase("T"));
        assertFalse(characterReader.matchConsumeIgnoreCase("z"));

        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCaseMethodWithMultiChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));

        assertTrue(characterReader.matchConsumeIgnoreCase("Es"));
        assertFalse(characterReader.matchConsumeIgnoreCase("Zs"));

        assertEquals(3, characterReader.pos());
    }

    @Test
    public void testContainsIgnoreCaseMethodWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));

        assertTrue(characterReader.containsIgnoreCase("t"));
        assertFalse(characterReader.containsIgnoreCase("z"));
    }

    @Test
    public void testContainsIgnoreCaseMethodWithMultiChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));

        assertTrue(characterReader.containsIgnoreCase("es"));
        assertFalse(characterReader.containsIgnoreCase("zz"));
    }

    @Test
    public void testToStringMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));

        assertEquals("test", characterReader.toString());
    }
}