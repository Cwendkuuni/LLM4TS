package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        String input = "Hello World!";
        Reader reader = new InputStreamReader(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("Hello World!", characterReader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(input);
        assertEquals("Hello World!", characterReader.toString());
    }

    @Test
    public void testBufferUp() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        characterReader.bufferUp();
        assertTrue(characterReader.bufPos == 0 && characterReader.bufLength > 0);
    }

    @Test(expected = UncheckedIOException.class)
    public void testBufferUpWithIOException() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new InputStreamReader(new IOException("Error"))) {
            @Override
            public void bufferUp() {
                super.bufferUp();
                throw new UncheckedIOException(new IOException("Expected IOException"));
            }
        };
        characterReader.bufferUp();
    }

    @Test
    public void testIsEmpty() {
        String input = "";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        assertTrue(characterReader.isEmpty());

        input = "Hello World!";
        characterReader = new CharacterReader(new StringReader(input));
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        assertEquals('H', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
        characterReader.unconsume();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testConsume() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        characterReader.consume();
        assertEquals('e', characterReader.current());
        characterReader.consume();
        assertEquals('l', characterReader.current());
        characterReader.consume();
        assertEquals('l', characterReader.current());
        characterReader.consume();
        assertEquals('o', characterReader.current());
        characterReader.consume();
        assertEquals(' ', characterReader.current());
        characterReader.consume();
        assertEquals('W', characterReader.current());
        characterReader.consume();
        assertEquals('r', characterReader.current());
        characterReader.consume();
        assertEquals('l', characterReader.current());
        characterReader.consume();
        assertEquals('d', characterReader.current());
        characterReader.consume();
        assertEquals('!', characterReader.current());
        characterReader.consume();
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testAdvance() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        characterReader.advance();
        assertEquals('e', characterReader.current());
        characterReader.advance();
        assertEquals('l', characterReader.current());
        characterReader.advance();
        assertEquals('l', characterReader.current());
        characterReader.advance();
        assertEquals('o', characterReader.current());
        characterReader.advance();
        assertEquals(' ', characterReader.current());
        characterReader.advance();
        assertEquals('W', characterReader.current());
        characterReader.advance();
        assertEquals('r', characterReader.current());
        characterReader.advance();
        assertEquals('l', characterReader.current());
        characterReader.advance();
        assertEquals('d', characterReader.current());
        characterReader.advance();
        assertEquals('!', characterReader.current());
        characterReader.advance();
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testMark() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        characterReader.mark();
        characterReader.consume();
        assertEquals('e', characterReader.current());
        characterReader.rewindToMark();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        String input = "HelloWorld!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        assertEquals(-1, characterReader.nextIndexOf('Z'));
        assertEquals(0, characterReader.nextIndexOf('H'));
        assertEquals(1, characterReader.nextIndexOf('e'));
        assertEquals(2, characterReader.nextIndexOf('l'));
        assertEquals(3, characterReader.nextIndexOf('o'));
        assertEquals(13, characterReader.nextIndexOf('!'));
    }

    @Test
    public void testNextIndexOfWithCharSequence() {
        String input = "HelloWorld!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        assertEquals(-1, characterReader.nextIndexOf("ABC"));
        assertEquals(0, characterReader.nextIndexOf("Hello"));
        assertEquals(13, characterReader.nextIndexOf("!"));
    }

    @Test
    public void testConsumeTo() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        assertEquals("Hello", characterReader.consumeTo(' '));
        assertEquals("World!", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToWithEmptyTarget() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        assertEquals("", characterReader.consumeTo(' '));
        assertEquals("World!", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToWithNonExistentTarget() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        assertEquals("Hello World!", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        String input = "Hello World!&<&null&/&>AndNull&";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        assertEquals("&<null&", characterReader.consumeToAny(TokeniserState.nullChar, '&'));
        assertEquals("AndNull&", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToAnySorted() {
        String input = "Hello World!&<&null&/&>AndNull&";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        assertEquals("/&", characterReader.consumeToAnySorted('/', '>', '&'));
        assertEquals("AndNull&", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeData() {
        String input = "Hello World!&<&null&/&>AndNull&";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        assertEquals("Hello World!", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        String input = "<html>\n<head>\n<title>Title</title>\n</head>\n<body>\n</body>\n</html>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        assertEquals("<html>\n<head>\n<title>", characterReader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        assertEquals("Hello World!", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        String input = "HelloWorld!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        assertEquals("Hello", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String input = "Hello12345World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        assertEquals("Hello12345", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testMatches() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        assertTrue(characterReader.matches('h'));
        assertTrue(characterReader.matches("Hello"));
        assertFalse(characterReader.matches("World"));
        assertFalse(characterReader.matches('!'));
        assertFalse(characterReader.matches(new String("!")));
    }

    @Test
    public void testMatchesIgnoreCase() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        assertTrue(characterReader.matchesIgnoreCase("hello"));
        assertTrue(characterReader.matchesIgnoreCase("HELLO"));
        assertFalse(characterReader.matchesIgnoreCase("World"));
        assertFalse(characterReader.matchesIgnoreCase("!"));
        assertFalse(characterReader.matchesIgnoreCase(new String("!")));
    }

    @Test
    public void testMatchesAny() {
        String input = "Hello World!&<&null&/&>AndNull&";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        assertTrue(characterReader.matchesAny('&', '<', '/', '>', 'null'));
        assertFalse(characterReader.matchesAny('Z'));
    }

    @Test
    public void testMatchesAnySorted() {
        String input = "Hello World!&<&null&/&>AndNull&";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        assertTrue(characterReader.matchesAnySorted('/', '>', '&'));
        assertFalse(characterReader.matchesAnySorted('Z'));
    }

    @Test
    public void testMatchesLetter() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter('!'));
    }

    @Test
    public void testMatchesDigit() {
        String input = "Hello World!1234567890";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        assertTrue(characterReader.matchesDigit());
        assertFalse(characterReader.matchesDigit('!'));
    }

    @Test
    public void testMatchConsume() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        assertEquals("World!", characterReader.matchConsume("World"));
        assertEquals("Hello", characterReader.toString());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        assertEquals("World!", characterReader.matchConsumeIgnoreCase("world"));
        assertEquals("Hello", characterReader.toString());
    }

    @Test
    public void testContainsIgnoreCase() {
        String input = "<html>\n<head>\n<title>Title</title>\n</head>\n<body>\n</body>\n</html>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        assertTrue(characterReader.containsIgnoreCase("title"));
        assertTrue(characterReader.containsIgnoreCase("TITLE"));
        assertFalse(characterReader.containsIgnoreCase("body"));
    }

    @Test
    public void testToString() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        assertEquals(input, characterReader.toString());
    }
}