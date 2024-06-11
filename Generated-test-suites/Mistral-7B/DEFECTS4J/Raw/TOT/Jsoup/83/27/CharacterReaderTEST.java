package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * Test class for CharacterReader.
 */
public class CharacterReaderTest {

    /**
     * Test instantiating CharacterReader with different input sources.
     */
    @Test
    public void testInstantiate() {
        // Given
        Reader reader1 = new StringReader("Hello World!");
        CharacterReader reader1Instance = new CharacterReader(reader1);

        Reader reader2 = new StringReader("Hello");
        CharacterReader reader2Instance = new CharacterReader(reader2);

        CharacterReader reader3 = new CharacterReader("Java is awesome!");

        // When

        // Then
        Assert.assertNotNull(reader1Instance);
        Assert.assertNotNull(reader2Instance);
        Assert.assertNotNull(reader3);
    }

    /**
     * Test reading a single character using CharacterReader.
     */
    @Test
    public void testReadSingleCharacter() {
        // Given
        String input = "Hello World!";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        // When
        char result = reader.consume();

        // Then
        Assert.assertEquals('H', result);
    }

    /**
     * Test reading multiple characters using CharacterReader.
     */
    @Test
    public void testReadMultipleCharacters() {
        // Given
        String input = "Hello World!";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        // When
        String consumed = reader.consumeTo('o');

        // Then
        Assert.assertEquals("ello", consumed);
        Assert.assertEquals('o', reader.current());
    }

    /**
     * Test reading multiple characters until end using CharacterReader.
     */
    @Test
    public void testReadToEnd() {
        // Given
        String input = "Hello World!";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        // When
        String consumed = reader.consumeToEnd();

        // Then
        Assert.assertEquals(input, consumed);
        Assert.assertEquals(CharacterReader.EOF, reader.current());
    }

    /**
     * Test reading a sequence of characters using CharacterReader.
     */
    @Test
    public void testReadSequence() {
        // Given
        String input = "Hello World!";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        // When
        String consumed = reader.consumeTo("ld!");

        // Then
        Assert.assertEquals("Hello World!", reader.toString());
        Assert.assertEquals("ld!", consumed);
    }

    /**
     * Test reading a sequence of characters sorted in ascending order using CharacterReader.
     */
    @Test
    public void testReadSequenceSorted() {
        // Given
        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        // When
        String consumed = reader.consumeToAnySorted('a', 'z', 'A', 'Z');

        // Then
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyz", consumed);
    }

    /**
     * Test reading a sequence of characters containing digits using CharacterReader.
     */
    @Test
    public void testReadDigitSequence() {
        // Given
        String input = "1234567890";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        // When
        String consumed = reader.consumeDigitSequence();

        // Then
        Assert.assertEquals(consumed, "1234567890");
    }

    /**
     * Test reading a sequence of letters using CharacterReader.
     */
    @Test
    public void testReadLetterSequence() {
        // Given
        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        // When
        String consumed = reader.consumeLetterSequence();

        // Then
        Assert.assertEquals(consumed, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /**
     * Test reading a sequence of letters followed by digits using CharacterReader.
     */
    @Test
    public void testReadLetterThenDigitSequence() {
        // Given
        String input = "abc123def456";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        // When
        String consumed = reader.consumeLetterThenDigitSequence();

        // Then
        Assert.assertEquals(consumed, "abc123def456");
    }

    /**
     * Test reading a sequence of hexadecimal digits using CharacterReader.
     */
    @Test
    public void testReadHexSequence() {
        // Given
        String input = "0123456789ABCDEFABCDEF";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        // When
        String consumed = reader.consumeHexSequence();

        // Then
        Assert.assertEquals(consumed, "0123456789ABCDEFABCDEF");
    }

    /**
     * Test reading until certain character is found using CharacterReader.
     */
    @Test
    public void testReadUntilCharacter() {
        // Given
        String input = "Hello<World>";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        // When
        String consumed = reader.consumeTo('<');

        // Then
        Assert.assertEquals("Hello", consumed);
        Assert.assertEquals('<', reader.current());
    }

    /**
     * Test reading until any character is found using CharacterReader.
     */
    @Test
    public void testReadUntilAnyCharacter() {
        // Given
        String input = "Hello<World>";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        // When
        String consumed = reader.consumeToAny('H', 'e', 'l', 'l', 'o', '<');

        // Then
        Assert.assertEquals("Hello", consumed);
        Assert.assertEquals('<', reader.current());
    }

    /**
     * Test reading until any character is found sorted in ascending order using CharacterReader.
     */
    @Test
    public void testReadUntilAnyCharacterSorted() {
        // Given
        String input = "Hello<World>";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        // When
        String consumed = reader.consumeToAnySorted('H', 'e', 'l', 'l', 'o');

        // Then
        Assert.assertEquals("Hello", consumed);
        Assert.assertEquals('<', reader.current());
    }

    /**
     * Test reading until character sequence is found using CharacterReader.
     */
    @Test
    public void testReadUntilCharacterSequence() {
        // Given
        String input = "Hello<title>";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        // When
        String consumed = reader.consumeTo("<title>");

        // Then
        Assert.assertEquals("Hello<title>", reader.toString());
        Assert.assertEquals("<title>", consumed);
    }

    /**
     * Test reading until character sequence is found ignoring case using CharacterReader.
     */
    @Test
    public void testReadUntilCharacterSequenceIgnoreCase() {
        // Given
        String input = "Hello<TITLE>";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        // When
        String consumed = reader.consumeToIgnoreCase("<title>");

        // Then
        Assert.assertEquals("Hello<TITLE>", reader.toString());
        Assert.assertEquals("<title>", consumed);
    }

    /**
     * Test reading until character sequence is found with matchesAny using CharacterReader.
     */
    @Test
    public void testReadUntilCharacterSequenceWithMatchesAny() {
        // Given
        String input = "Hello<title>";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        // When
        String consumed = reader.consumeToAny( '<', 't', 'i', 't', 'l', 'e');

        // Then
        Assert.assertEquals("Hello<title>", reader.toString());
        Assert.assertEquals("<title>", consumed);
    }

    /**
     * Test reading until character sequence is found with matchesAnySorted using CharacterReader.
     */
    @Test
    public void testReadUntilCharacterSequenceWithMatchesAnySorted() {
        // Given
        String input = "Hello<title>";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        // When
        String consumed = reader.consumeToAnySorted('<', 't', 'i', 't', 'l', 'e');

        // Then
        Assert.assertEquals("Hello<title>", reader.toString());
        Assert.assertEquals("<title>", consumed);
    }
}