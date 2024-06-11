package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {
    

    @Test
    public void testCharacterReaderEmptyInput() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderSingleCharacterInput() {
        CharacterReader reader = new CharacterReader("a");
        Assert.assertEquals('a', reader.current());
        reader.consume();
        Assert.assertFalse(reader.matches('a'));
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMultipleCharactersInput() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertEquals('a', reader.current());
        reader.consume();
        Assert.assertEquals('b', reader.current());
        reader.consume();
        Assert.assertEquals('c', reader.current());
        reader.consume();
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        reader.advance();
        reader.advance();
        Assert.assertEquals('c', reader.current());
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterReaderNullInput() {
        CharacterReader reader = new CharacterReader(null);
    }

    
}