package org.jsoup.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assertions.assertTrue(reader.isEmpty());
        Assertions.assertEquals(0, reader.pos());
    }

    @Test
    public void testCharacterReaderSimple() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assertions.assertFalse(reader.isEmpty());
        Assertions.assertEquals(0, reader.pos());

        reader.advance();
        Assertions.assertEquals('a', reader.current());
        reader.consume();
        Assertions.assertEquals('b', reader.current());
        reader.consume();
        Assertions.assertEquals('c', reader.current());
        reader.consume(); // EOF
        Assertions.assertEquals(CharacterReader.EOF, reader.current());

        reader.rewindToMark();
        reader.mark();
        reader.advance();
        reader.unconsume();
        reader.rewindToMark();
        Assertions.assertEquals(2, reader.pos());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.advance();
        reader.advance();

        Assertions.assertEquals(3, reader.nextIndexOf('c'));
        Assertions.assertEquals(-1, reader.nextIndexOf('z'));
        Assertions.assertEquals(5, reader.nextIndexOf("def"));

        reader.rewindToMark();
        Assertions.assertEquals(4, reader.nextIndexOf('d', 3));
    }

    @ParameterizedTest
    @MethodSource("dataForConsumeTo")
    public void testConsumeTo(String input, int index, String expected) {
        CharacterReader reader = new CharacterReader(new StringReader(input));
        reader.advance();
        reader.advance();
        // consume until index
        String consumed = reader.consumeTo(input.charAt(index));
        Assertions.assertEquals(expected, consumed);
    }

    static Stream<Arguments> dataForConsumeTo() {
        return Stream.of(
                Arguments.arguments("abcd", 2, "bc"),
                Arguments.arguments("abcd", 0, ""),
                Arguments.arguments("abcd", 3, "d")
        );
    }

    @Test
    public void testCharacterReaderException() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assertions.assertThrows(UncheckedIOException.class, () -> reader.bufferUp());
    }

    @Test
    public void testCharacterReaderBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.advance();
        reader.advance();

        reader.bufferUp();
        Assertions.assertEquals(3, reader.bufLength);
        Assertions.assertEquals('a', reader.charBuf[0]);
        Assertions.assertEquals('b', reader.charBuf[1]);
        Assertions.assertEquals('c', reader.charBuf[2]);

        reader.bufferUp();
        Assertions.assertEquals(1024 * 32, reader.bufLength);
        Assertions.assertArrayEquals(new char[1024 * 32], reader.charBuf);
    }

    @Test
    public void testCharacterReaderCacheString() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));

        String str1 = reader.consumeTo("bc");
        String str2 = reader.consumeTo("bc");
        Assertions.assertEquals(str1, str2);

        reader.rewindToMark();
        String str3 = reader.consumeTo("bc");
        Assertions.assertEquals(str1, str3);

        String str4 = reader.consumeTo("ef");
        Assertions.assertNotEquals(str1, str4);
    }

    @Test
    public void testCharacterReaderNextIndexOfEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assertions.assertEquals(-1, reader.nextIndexOf('a'));
    }

    @Test
    public void testCharacterReaderNextIndexOfNull() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assertions.assertEquals(-1, reader.nextIndexOf(null));
    }

    @Test
    public void testCharacterReaderNextIndexOfWithOffset() {
        CharacterReader reader = new CharacterReader(new StringReader("abcde"));
        Assertions.assertEquals(3, reader.nextIndexOf('c', 2));
    }

    @Test
    public void testCharacterReaderNextIndexOfWithSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assertions.assertEquals(5, reader.nextIndexOf("def"));
    }

    @Test
    public void testCharacterReaderNextIndexOfWithSequenceOffset() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assertions.assertEquals(2, reader.nextIndexOf("ef", 3));
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.advance();
        reader.advance();

        Assertions.assertTrue(reader.matches('b'));
        Assertions.assertTrue(reader.matches("ab"));
        Assertions.assertFalse(reader.matches("def"));
        Assertions.assertFalse(reader.matches("xyz"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.advance();
        reader.advance();

        Assertions.assertTrue(reader.matchesIgnoreCase("bc"));
        Assertions.assertTrue(reader.matchesIgnoreCase("AB"));
        Assertions.assertFalse(reader.matchesIgnoreCase("XYZ"));
    }

    @ParameterizedTest
    @MethodSource("dataForMatchesAny")
    public void testMatchesAny(char... chars) {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.advance();
        reader.advance();

        Assertions.assertTrue(reader.matchesAny(chars));
    }

    static Stream<Arguments> dataForMatchesAny() {
        return Stream.of(
                Arguments.arguments('a', 'a'),
                Arguments.arguments('a', 'b'),
                Arguments.arguments('b', new char[]{'a', 'b'})
        );
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.advance();
        reader.advance();

        Assertions.assertTrue(reader.matchesLetter());
        Assertions.assertFalse(reader.matchesLetter('0'));
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("123"));
        reader.advance();
        reader.advance();

        Assertions.assertTrue(reader.matchesDigit());
        Assertions.assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.advance();
        reader.advance();

        String input = "bc";
        Assertions.assertTrue(reader.matchConsume(input));
        Assertions.assertEquals(input.length(), reader.pos() - reader.readerPos);
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.advance();
        reader.advance();

        String input = "BC";
        Assertions.assertTrue(reader.matchConsumeIgnoreCase(input));
        Assertions.assertEquals(input.length(), reader.pos() - reader.readerPos);
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body>Hello World</body></html>"));
        Assertions.assertTrue(reader.containsIgnoreCase("/title"));
        Assertions.assertTrue(reader.containsIgnoreCase("/BODY"));
        Assertions.assertFalse(reader.containsIgnoreCase("/FOO"));
    }

    @Test
    public void testCharacterReaderToString() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.advance();
        reader.advance();

        String expected = "ab";
        Assertions.assertEquals(expected, reader.toString());
    }
}