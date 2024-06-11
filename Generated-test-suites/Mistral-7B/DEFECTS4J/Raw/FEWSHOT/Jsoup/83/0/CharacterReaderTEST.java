package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderBasicUsage() {
        String input = "abcd123eF45g";
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        reader.advance();
        assertEquals('b', reader.current());
        reader.unconsume();
        reader.mark();
        reader.rewindToMark();
        assertEquals('b', reader.current());

        reader.advance();
        assertEquals('c', reader.current());
        reader.consume();
        assertEquals('1', reader.current());
        reader.consume();
        assertEquals('2', reader.current());
        reader.consume();
        assertEquals('3', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
        reader.consume();
        assertEquals('F', reader.current());
        reader.consume();
        assertEquals('4', reader.current());
        reader.consume();
        assertEquals('5', reader.current());
        reader.consume();
        assertEquals('g', reader.current());
        reader.consume();
        assertEquals(CharacterReader.EOF, reader.current());

        assertTrue(reader.isEmpty());
        assertFalse(reader.isEmptyNoBufferUp());

        assertEquals(0, reader.pos());
        assertEquals(input.length(), reader.pos() + reader.bufLength);
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        String input = "abcd123eF45g";
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        assertEquals(-1, reader.nextIndexOf('z'));
        assertEquals(3, reader.nextIndexOf('d'));
        assertEquals(7, reader.nextIndexOf('1'));
        assertEquals(10, reader.nextIndexOf('e'));
        assertEquals(13, reader.nextIndexOf('F'));
        assertEquals(16, reader.nextIndexOf('4'));
        assertEquals(19, reader.nextIndexOf('g'));
        assertEquals(20, reader.nextIndexOf('h')); // not present, returns -1

        reader.consumeTo('e');

        assertEquals(10, reader.pos());
        assertEquals(13, reader.nextIndexOf('F'));
    }

    @Test
    public void testCharacterReaderNextIndexOfString() {
        String input = "abcd<html><body>123eF45g</body></html>";
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        assertEquals(-1, reader.nextIndexOf("abc"));
        assertEquals(3, reader.nextIndexOf("<html>"));
        assertEquals(11, reader.nextIndexOf("<body>"));
        assertEquals(17, reader.nextIndexOf("123"));
        assertEquals(22, reader.nextIndexOf("eF45"));
        assertEquals(26, reader.nextIndexOf("g</body>"));
        assertEquals(30, reader.nextIndexOf("</html>"));
        assertEquals(36, reader.nextIndexOf("</body>")); // not present, returns -1

        reader.consumeTo("eF45");

        assertEquals(17, reader.pos());
        assertEquals(22, reader.nextIndexOf("g</body>"));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        String input = "abcd123eF45g";
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        String consumed = reader.consumeTo('e');
        assertEquals("bc1", consumed);
        assertEquals(7, reader.pos());

        consumed = reader.consumeTo("F45");
        assertEquals("123", consumed);
        assertEquals(13, reader.pos());

        consumed = reader.consumeToAny(new char[]{'g'});
        assertEquals("g", consumed);
        assertEquals(19, reader.pos());

        consumed = reader.consumeToAnySorted(new char[]{'e', 'g'});
        assertEquals("eF45g", consumed);
        assertEquals(23, reader.pos());

        consumed = reader.consumeData();
        assertEquals("abcd123eF45", consumed);
        assertEquals(23, reader.pos());

        consumed = reader.consumeTagName();
        assertEquals("<html><body>", consumed);
        assertEquals(11, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        String input = "abcd123eF45g";
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        String consumed = reader.consumeLetterSequence();
        assertEquals("abcd", consumed);
        assertEquals(5, reader.pos());

        consumed = reader.consumeLetterThenDigitSequence();
        assertEquals("abcd123", consumed);
        assertEquals(10, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        String input = "abcd123eF45g";
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        String consumed = reader.consumeHexSequence();
        assertEquals("123", consumed);
        assertEquals(10, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        String input = "abcd123eF45g";
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        String consumed = reader.consumeDigitSequence();
        assertEquals("123", consumed);
        assertEquals(10, reader.pos());
    }

    @Test
    public void testCharacterReaderMatches() {
        String input = "abcd123eF45g";
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        assertTrue(reader.matches('d'));
        assertTrue(reader.matches('1'));
        assertTrue(reader.matches('e'));
        assertTrue(reader.matches('F'));
        assertFalse(reader.matches('z'));
        assertFalse(reader.matches('H'));

        assertTrue(reader.matches("123"));
        assertTrue(reader.matches("eF45"));
        assertFalse(reader.matches("124"));
        assertFalse(reader.matches("eF46"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        String input = "abcd123eF45g";
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        assertTrue(reader.matchesIgnoreCase("d"));
        assertTrue(reader.matchesIgnoreCase("1"));
        assertTrue(reader.matchesIgnoreCase("e"));
        assertTrue(reader.matchesIgnoreCase("F"));
        assertFalse(reader.matchesIgnoreCase("z"));
        assertFalse(reader.matchesIgnoreCase("H"));

        assertTrue(reader.matchesIgnoreCase("123"));
        assertTrue(reader.matchesIgnoreCase("eF45"));
        assertFalse(reader.matchesIgnoreCase("124"));
        assertFalse(reader.matchesIgnoreCase("eF46"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        String input = "abcd123eF45g";
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        assertTrue(reader.matchesAny('d'));
        assertTrue(reader.matchesAny('1'));
        assertTrue(reader.matchesAny('e'));
        assertTrue(reader.matchesAny('F'));
        assertFalse(reader.matchesAny('z'));
        assertFalse(reader.matchesAny('H'));

        assertTrue(reader.matchesAny("123"));
        assertTrue(reader.matchesAny("eF45"));
        assertFalse(reader.matchesAny("124"));
        assertFalse(reader.matchesAny("eF46"));

        assertTrue(reader.matchesAny(new char[]{'d'}));
        assertTrue(reader.matchesAny(new char[]{'1'}));
        assertTrue(reader.matchesAny(new char[]{'e'}));
        assertTrue(reader.matchesAny(new char[]{'F'}));
        assertFalse(reader.matchesAny(new char[]{'z'}));
        assertFalse(reader.matchesAny(new char[]{'H'}));

        assertTrue(reader.matchesAnySorted(new char[]{'d', 'e'}));
        assertTrue(reader.matchesAnySorted(new char[]{'1', 'd'}));
        assertTrue(reader.matchesAnySorted(new char[]{'1', 'e'}));
        assertTrue(reader.matchesAnySorted(new char[]{'F', 'e'}));
        assertFalse(reader.matchesAnySorted(new char[]{'z', 'd'}));
        assertFalse(reader.matchesAnySorted(new char[]{'z', 'e'}));
        assertFalse(reader.matchesAnySorted(new char[]{'z', 'H'}));
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        String input = "abcd123eF45g";
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        reader.mark();
        reader.advance();

        reader.rewindToMark();

        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        String input = "abcd123eF45g";
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        String consumed = reader.consumeLetterThenDigitSequence();
        assertEquals("abcd123", consumed);
        assertEquals(10, reader.pos());
    }
}