
package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // Typical use case
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertEquals(0, reader.pos());

        // Edge case
        reader = new CharacterReader("");
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        // Typical use case
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());

        // Edge case
        reader = new CharacterReader("abc");
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        // Typical use case
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertEquals('a', reader.current());

        // Edge case
        reader = new CharacterReader("");
        Assert.assertEquals(CharacterReader.EOF, reader.current());
    }

    // Continue with the remaining test cases for each method in the CharacterReader class
    
}

