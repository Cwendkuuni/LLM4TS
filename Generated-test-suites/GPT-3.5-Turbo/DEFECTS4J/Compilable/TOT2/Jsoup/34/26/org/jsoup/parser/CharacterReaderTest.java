package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("Hello World!");
        characterReader.mark();
    }

    @Test
    public void testPos() {
        Assert.assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        Assert.assertFalse(characterReader.isEmpty());
        characterReader.consume();
        Assert.assertFalse(characterReader.isEmpty());
        characterReader.consumeToEnd();
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        Assert.assertEquals('H', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsume() {
        Assert.assertEquals('H', characterReader.consume());
        Assert.assertEquals('e', characterReader.consume());
    }

    @Test
    public void testMarkRewindToMark() {
        characterReader.advance();
        Assert.assertEquals(1, characterReader.pos());
        characterReader.rewindToMark();
        Assert.assertEquals(0, characterReader.pos());
    }

    // Add more test cases for other methods...

}