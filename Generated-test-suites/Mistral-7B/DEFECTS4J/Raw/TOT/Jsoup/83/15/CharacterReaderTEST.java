package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * Test class for CharacterReader class.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        // Test constructor with Reader input
        CharacterReader reader1 = new CharacterReader(new StringReader("Hello World"));
        // Test constructor with String input
        CharacterReader reader2 = new CharacterReader("Hello World");

        // Test initial state
        Assert.assertEquals(0, reader1.pos());
        Assert.assertEquals(0, reader1.bufPos);
        Assert.assertEquals(0, reader1.readerPos);
        Assert.assertEquals(0, reader1.bufLength);
        Assert.assertEquals(0, reader1.bufMark);
        Assert.assertEquals(0, reader1.bufSplitPoint);

        // Test current character
        Assert.assertEquals('H', reader1.current());
        reader1.consume();
        Assert.assertEquals('e', reader1.current());

        // Test isEmpty
        Assert.assertFalse(reader1.isEmpty());

        // Test consume
        Assert.assertEquals('l', reader1.consume());
        Assert.assertEquals('o', reader1.consume());
        Assert.assertEquals(' ', reader1.consume());
        Assert.assertEquals('W', reader1.consume());
        Assert.assertEquals('r', reader1.consume());
        Assert.assertEquals('d', reader1.consume());
        Assert.assertEquals(CharacterReader.EOF, reader1.current());
        Assert.assertTrue(reader1.isEmpty());

        // Test advance
        reader1.advance();
        Assert.assertEquals(CharacterReader.EOF, reader1.current());
        reader1.advance();
        Assert.assertEquals(CharacterReader.EOF, reader1.current());

        // Test mark and rewind
        reader1.mark();
        reader1.consume();
        reader1.rewindToMark();
        Assert.assertEquals('l', reader1.current());

        // Test nextIndexOf
        Assert.assertEquals(6, reader1.nextIndexOf('o'));
        Assert.assertEquals(-1, reader1.nextIndexOf('z'));

        // Test nextIndexOf with CharSequence
        Assert.assertEquals(6, reader1.nextIndexOf("lo"));
        Assert.assertEquals(-1, reader1.nextIndexOf("zoo"));

        // Test consumeTo
        String str1 = reader1.consumeTo('o');
        Assert.assertEquals("Hello ", str1);
        String str2 = reader1.consumeTo('d');
        Assert.assertEquals("Hello Wo", str2);
        String str3 = reader1.consumeTo('r');
        Assert.assertEquals("Hello Wor", str3);
        String str4 = reader1.consumeToEnd();
        Assert.assertEquals("Hello World", str4);

        // Test consumeTo with CharSequence
        String str5 = reader1.consumeTo("lo");
        Assert.assertEquals("Hello ", str5);
        String str6 = reader1.consumeTo("Wor");
        Assert.assertEquals("Hello World", str6);

        // Test consumeToAny
        String str7 = reader1.consumeToAny('l', 'o', 'r');
        Assert.assertEquals("Hello ", str7);

        // Test consumeToAnySorted
        String str8 = reader1.consumeToAnySorted('r', 'o', 'l');
        Assert.assertEquals("Hello World", str8);

        // Test consumeData
        String str9 = reader1.consumeData();
        Assert.assertEquals("Hello World", str9);

        // Test consumeTagName
        String str10 = reader1.consumeTagName();
        Assert.assertEquals("Hello", str10);

        // Test consumeToEnd
        String str11 = reader1.consumeToEnd();
        Assert.assertEquals("Hello World", str11);

        // Test consumeLetterSequence
        String str12 = reader1.consumeLetterSequence();
        Assert.assertEquals("Hello", str12);

        // Test consumeLetterThenDigitSequence
        String str13 = reader1.consumeLetterThenDigitSequence();
        Assert.assertEquals("HelloWorld", str13);

        // Test matches
        Assert.assertFalse(reader1.matches('Z'));
        Assert.assertTrue(reader1.matches('o'));

        // Test matches with CharSequence
        Assert.assertTrue(reader1.matches("lo"));

        // Test matchesIgnoreCase
        Assert.assertTrue(reader1.matchesIgnoreCase("lo"));
        Assert.assertTrue(reader1.matchesIgnoreCase("Lo"));

        // Test matchesAny
        Assert.assertTrue(reader1.matchesAny('l', 'o'));
        Assert.assertFalse(reader1.matchesAny('z'));

        // Test matchesAnySorted
        Assert.assertTrue(reader1.matchesAnySorted('l', 'o'));
        Assert.assertTrue(reader1.matchesAnySorted('o', 'l'));
        Assert.assertFalse(reader1.matchesAnySorted('z'));

        // Test matchesLetter
        Assert.assertTrue(reader1.matchesLetter());
        Assert.assertFalse(reader1.matchesLetter('5'));

        // Test matchesDigit
        Assert.assertFalse(reader1.matchesDigit());
        Assert.assertTrue(reader1.matchesDigit('1'));

        // Test matchConsume
        reader1.mark();
        reader1.consume();
        Assert.assertEquals("ello", reader1.consumeToEnd());
        String str14 = reader1.consumeTo('r');
        Assert.assertEquals("Hello Wor", str14);
        Assert.assertEquals("World", reader1.consumeToEnd());
        Assert.assertEquals("World", reader1.toString());
        Assert.assertEquals("World", reader1.matchesConsumeIgnoreCase("World"));

        // Test containsIgnoreCase
        Assert.assertTrue(reader1.containsIgnoreCase("world"));
        Assert.assertFalse(reader1.containsIgnoreCase("Hello"));

        // Test toString
        Assert.assertEquals("World", reader1.toString());

        // Test empty constructor
        CharacterReader reader3 = new CharacterReader();
        Assert.assertTrue(reader3.isEmpty());
        Assert.assertEquals(CharacterReader.EOF, reader3.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        // Test constructor with IOException input
        CharacterReader reader4 = new CharacterReader(new StringReader("Hello World"), "IOExceptionTest") {
            @Override
            public void close() throws IOException {
                throw new IOException("Test IOException");
            }
        };
    }
}