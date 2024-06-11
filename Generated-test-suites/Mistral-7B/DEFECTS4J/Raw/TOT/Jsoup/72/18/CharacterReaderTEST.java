package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertEquals(EOF, reader.consume());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderSimple() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('b', reader.current());
        //... continue testing all methods with simple usage and typical use cases
    }

    @Test
    public void testCharacterReaderWithError() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        try {
            reader.consumeTo('z');
            Assert.fail("Expected UncheckedIOException");
        } catch (UncheckedIOException e) {
            //expected exception
        }
        Assert.assertEquals(6, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('f', reader.current());
        //... continue testing all methods with error scenarios and edge cases
    }

    @Test
    public void testCharacterReaderWithLimit() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        String data = reader.consumeTo('z');
        Assert.assertEquals("abcdefghijklmnopqrstuvwxy", data);
        Assert.assertEquals(26, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('z', reader.current());
        //... continue testing all methods with limit cases
    }

    @Test
    public void testCharacterReaderWithLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        String sequence = reader.consumeLetterSequence();
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyz", sequence);
        Assert.assertEquals(26, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        //... continue testing all methods with letter sequence cases
    }

    @Test
    public void testCharacterReaderWithLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123def456ghi789jklm012opq345stuv678wxy90"));
        String sequence = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("abc123def456", sequence);
        Assert.assertEquals(11, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('d', reader.current());
        //... continue testing all methods with letter then digit sequence cases
    }

    @Test
    public void testCharacterReaderWithDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("12345678901234567890123456789012345678901234567890"));
        String sequence = reader.consumeDigitSequence();
        Assert.assertEquals("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890", sequence);
        Assert.assertEquals(22, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('0', reader.current());
        //... continue testing all methods with digit sequence cases
    }

    @Test
    public void testCharacterReaderWithHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("%#65%#66%#67%"));
        String sequence = reader.consumeHexSequence();
        Assert.assertEquals("%#65%#66%#67", sequence);
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        //... continue testing all methods with hex sequence cases
    }
}