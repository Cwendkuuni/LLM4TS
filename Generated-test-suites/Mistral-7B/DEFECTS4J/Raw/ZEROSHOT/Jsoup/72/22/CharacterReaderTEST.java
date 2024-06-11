package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

    private CharacterReader characterReader;
    private char[] testData = new char[1024 * 32];

    @Before
    public void setUp() {
        String testString = new String(testData, 0, testData.length);
        characterReader = new CharacterReader(new ByteArrayInputStream(testData), testData.length);
    }

    @After
    public void tearDown() {
        characterReader = null;
    }

    @Test
    public void testCharacterReaderConstructorWithReader() {
        Validate.notNull(characterReader.reader);
        characterReader.reader.markSupported();
    }

    @Test
    public void testCharacterReaderConstructorWithReaderAndSize() {
        Validate.notNull(characterReader.reader);
        characterReader.reader.markSupported();
        assertEquals(characterReader.charBuf.length, maxBufferLen);
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        characterReader = new CharacterReader(new StringReader("test"), "test".length());
        assertEquals(characterReader.charBuf.length, maxBufferLen);
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOExceptionWhenBufferUpFails() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        CharacterReader characterReader = new CharacterReader(input, maxBufferLen);
        characterReader.bufferUp();
    }

    @Test
    public void testPos() {
        int initialPosition = characterReader.pos();

        for (int i = 0; i < testData.length / 2; i++) {
            characterReader.consume();
        }

        int currentPosition = characterReader.pos();

        assertEquals(initialPosition + testData.length / 2, currentPosition);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(characterReader.isEmpty());

        characterReader.bufferUp();
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() throws IOException {
        characterReader.bufferUp();

        assertEquals(characterReader.current(), testData[0]);
        assertEquals(characterReader.consume(), testData[0]);
        assertEquals(characterReader.current(), testData[1]);
        assertEquals(characterReader.consume(), testData[1]);
        // ...

        assertEquals(EOF, characterReader.current());
    }

    @Test
    public void testConsume() {
        characterReader.bufferUp();

        for (int i = 0; i < testData.length; i++) {
            assertEquals(testData[i], characterReader.consume());
        }

        assertEquals(EOF, characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.bufferUp();

        characterReader.consume();
        characterReader.unconsume();

        assertEquals(testData[0], characterReader.current());
    }

    @Test
    public void testAdvance() throws IOException {
        characterReader.bufferUp();

        characterReader.advance();
        assertEquals(testData[1], characterReader.current());

        characterReader.advance();
        assertEquals(testData[2], characterReader.current());

        // ...

        characterReader.advance();
        assertEquals(EOF, characterReader.current());
    }

    @Test
    public void testMark() {
        characterReader.bufferUp();

        characterReader.mark();
        assertEquals(characterReader.pos(), characterReader.bufPos);

        characterReader.advance();
        characterReader.advance();

        characterReader.rewindToMark();

        assertEquals(characterReader.pos(), characterReader.bufPos - 2);
    }

    @Test
    public void testNextIndexOf() throws IOException {
        characterReader.bufferUp();

        assertEquals(nextIndexOf('a'), 0);
        assertEquals(nextIndexOf('z'), -1);
        assertEquals(nextIndexOf('z', 10), -1);

        characterReader.consume();
        assertEquals(nextIndexOf('z'), 0);

        // ...

        characterReader.consumeToEnd();
        assertEquals(nextIndexOf('z'), -1);
    }

    @Test
    public void testNextIndexOfWithSequence() throws IOException {
        characterReader.bufferUp();

        assertEquals(nextIndexOf("test"), -1);
        assertEquals(nextIndexOf("test", 10), -1);

        characterReader.consume();

        assertEquals(nextIndexOf("test"), 0);

        // ...

        characterReader.consumeToEnd();
        assertEquals(nextIndexOf("test"), -1);
    }

    @Test
    public void testConsumeTo() throws IOException {
        characterReader.bufferUp();

        String expected = new String(testData, 0, 5);
        String consumed = characterReader.consumeTo('e');

        assertEquals(expected, consumed);
        assertEquals(testData[5], characterReader.current());

        characterReader.unconsume();
        assertEquals(expected, characterReader.toString());
    }

    @Test
    public void testConsumeToWithSequence() throws IOException {
        characterReader.bufferUp();

        String expected = new String(testData, 0, 5);
        String consumed = characterReader.consumeTo("test");

        assertEquals(expected, consumed);
        assertEquals(testData[5], characterReader.current());

        characterReader.unconsume();
        assertEquals(expected, characterReader.toString());
    }

    @Test
    public void testConsumeToAny() throws IOException {
        characterReader.bufferUp();

        assertEquals(consumeToAny(','), "");
        assertEquals(consumeToAny(']'), new String(testData, 0, characterReader.pos()));

        characterReader.advance();
        assertEquals(consumeToAny(']'), new String(testData, 1, characterReader.pos()-1));

        // ...

        characterReader.consumeToEnd();
        assertEquals(consumeToAny(']'), "");
    }

    @Test
    public void testConsumeToAnySorted() throws IOException {
        characterReader.bufferUp();

        assertEquals(consumeToAnySorted('z'), "");
        assertEquals(consumeToAnySorted('a'), new String(testData, 0, characterReader.pos()));

        characterReader.advance();
        assertEquals(consumeToAnySorted('a'), new String(testData, 1, characterReader.pos()-1));

        // ...

        characterReader.consumeToEnd();
        assertEquals(consumeToAnySorted('z'), "");
    }

    @Test
    public void testConsumeData() throws IOException {
        characterReader.bufferUp();

        String expected = new String(testData, 0, characterReader.pos());
        String consumedData = characterReader.consumeData();

        assertEquals(expected, consumedData);
        characterReader.advance();
    }

    @Test
    public void testConsumeTagName() throws IOException {
        characterReader.bufferUp();

        String expected = new String(testData, 0, characterReader.pos());
        String consumedTagName = characterReader.consumeTagName();

        assertEquals(expected, consumedTagName);
        characterReader.advance();
    }

    @Test
    public void testConsumeToEnd() throws IOException {
        characterReader.bufferUp();

        String expected = new String(testData, 0, characterReader.pos());
        String consumed = characterReader.consumeToEnd();

        assertEquals(expected, consumed);
        assertEquals(EOF, characterReader.current());
    }

    @Test
    public void testConsumeLetterSequence() throws IOException {
        characterReader.bufferUp();

        String expected = new String(testData, 0, characterReader.pos());
        String consumedLetterSequence = characterReader.consumeLetterSequence();

        assertEquals(expected, consumedLetterSequence);
        characterReader.advance();
    }

    @Test
    public void testConsumeLetterThenDigitSequence() throws IOException {
        characterReader.bufferUp();

        String expected = new String(testData, 0, characterReader.pos());
        String consumedLetterThenDigitSequence = characterReader.consumeLetterThenDigitSequence();

        assertEquals(expected, consumedLetterThenDigitSequence);
        characterReader.advance();
    }

    @Test
    public void testMatches() throws IOException {
        characterReader.bufferUp();

        char testChar = testData[characterReader.pos()];
        assertTrue(characterReader.matches(testChar));
        characterReader.advance();

        assertFalse(characterReader.matches(testChar));
    }

    @Test
    public void testMatchesSequence() throws IOException {
        characterReader.bufferUp();

        String testSequence = new String(testData, characterReader.pos(), 5);
        assertTrue(characterReader.matches(testSequence));
        characterReader.consume(5);

        assertFalse(characterReader.matches(testSequence));
    }

    @Test
    public void testMatchesIgnoreCaseSequence() throws IOException {
        characterReader.bufferUp();

        String testSequence = new String(testData, characterReader.pos(), 5).toLowerCase();
        assertTrue(characterReader.matchesIgnoreCase(testSequence));
        characterReader.consume(5);

        assertFalse(characterReader.matchesIgnoreCase(testSequence));
    }

    @Test
    public void testMatchesAny() throws IOException {
        characterReader.bufferUp();

        assertTrue(characterReader.matchesAny(testData[characterReader.pos()]));
        characterReader.advance();

        assertFalse(characterReader.matchesAny(testData[characterReader.pos()]));
    }

    @Test
    public void testMatchesAnySorted() throws IOException {
        characterReader.bufferUp();

        char testChar = testData[characterReader.pos()];
        assertTrue(characterReader.matchesAnySorted(new char[]{testChar}));
        characterReader.advance();

        assertFalse(characterReader.matchesAnySorted(new char[]{testChar}));
    }

    @Test
    public void testMatchesLetter() throws IOException {
        characterReader.bufferUp();

        assertTrue(characterReader.matchesLetter());
        characterReader.advance();

        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() throws IOException {
        characterReader.bufferUp();

        assertTrue(characterReader.matchesDigit());
        characterReader.advance();

        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsumeSequence() throws IOException {
        characterReader.bufferUp();

        String testSequence = new String(testData, characterReader.pos(), 5);
        assertTrue(characterReader.matchConsume(testSequence));
        characterReader.advance(5);

        assertFalse(characterReader.matchConsume(testSequence));
    }

    @Test
    public void testMatchConsumeIgnoreCaseSequence() throws IOException {
        characterReader.bufferUp();

        String testSequence = new String(testData, characterReader.pos(), 5).toLowerCase();
        assertTrue(characterReader.matchConsumeIgnoreCase(testSequence));
        characterReader.advance(5);

        assertFalse(characterReader.matchConsumeIgnoreCase(testSequence));
    }

    @Test
    public void testContainsIgnoreCaseSequence() throws IOException {
        characterReader.bufferUp();

        String testSequence = new String(testData, characterReader.pos(), 5).toLowerCase();
        assertTrue(characterReader.containsIgnoreCase(testSequence));
        characterReader.advance(testSequence.length());

        assertFalse(characterReader.containsIgnoreCase(testSequence));
    }

    @Test
    public void testToString() throws IOException {
        characterReader.bufferUp();

        String expected = new String(testData, characterReader.pos(), characterReader.pos() - characterReader.bufPos);
        assertEquals(expected, characterReader.toString());
        characterReader.advance();
    }
}