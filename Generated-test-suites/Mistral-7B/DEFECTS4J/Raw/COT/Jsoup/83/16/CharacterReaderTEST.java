package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertNotNull(characterReader);
        Assert.assertEquals("Hello World!", characterReader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        Assert.assertNotNull(characterReader);
        Assert.assertEquals("Hello World!", characterReader.toString());
    }

    @Test
    public void testEmptyCharacterReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(characterReader.isEmpty());
        Assert.assertEquals("", characterReader.toString());
    }

    @Test
    public void testIsEmptyWithData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertFalse(characterReader.isEmpty());
        Assert.assertEquals("Hello World!", characterReader.toString());
    }

    @Test
    public void testPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals(0, characterReader.pos());
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.advance();
        Assert.assertEquals('H', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('H', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('l', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('l', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('o', characterReader.current());
        characterReader.consume();
        Assert.assertEquals(' ', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('W', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('o', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('r', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('l', characterReader.current());
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());
        Assert.assertTrue(characterReader.isEmpty());
        Assert.assertEquals("", characterReader.toString());
    }

    @Test
    public void testNextIndexOfWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.advance();
        int index = characterReader.nextIndexOf('l');
        Assert.assertEquals(5, index);

        characterReader.rewindToMark();
        index = characterReader.nextIndexOf('l');
        Assert.assertEquals(5, index);

        characterReader.rewindToMark();
        characterReader.consume();
        index = characterReader.nextIndexOf('l');
        Assert.assertEquals(6, index);
    }

    @Test
    public void testNextIndexOfWithMultiChars() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.advance();
        characterReader.advance();
        int index = characterReader.nextIndexOf("ll");
        Assert.assertEquals(7, index);

        characterReader.rewindToMark();
        index = characterReader.nextIndexOf("ll");
        Assert.assertEquals(7, index);

        characterReader.rewindToMark();
        characterReader.consume();
        characterReader.consume();
        index = characterReader.nextIndexOf("ll");
        Assert.assertEquals(8, index);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNextIndexOfWithMissingChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.consume();
        characterReader.consume();
        int index = characterReader.nextIndexOf('k');
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        String data = characterReader.consumeToEnd();
        Assert.assertEquals("Hello World!", data);
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeToWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.advance();
        characterReader.advance();
        String data = characterReader.consumeTo('l');
        Assert.assertEquals("ll", data);
        Assert.assertEquals(8, characterReader.pos());
    }

    @Test
    public void testConsumeToWithMultiChars() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.advance();
        characterReader.advance();
        characterReader.advance();
        characterReader.advance();
        String data = characterReader.consumeTo("ll");
        Assert.assertEquals("ll", data);
        Assert.assertEquals(10, characterReader.pos());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConsumeToWithEmptySeq() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.consumeTo("");
    }

    @Test
    public void testConsumeToAnyWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        String data = characterReader.consumeToAny('l');
        Assert.assertEquals("ll", data);
        Assert.assertEquals(8, characterReader.pos());
    }

    @Test
    public void testConsumeToAnyWithMultiChars() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        String data = characterReader.consumeToAny(new char[]{'l', 'l'});
        Assert.assertEquals("ll", data);
        Assert.assertEquals(8, characterReader.pos());
    }

    @Test
    public void testConsumeToAnySortedWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        String data = characterReader.consumeToAnySorted('l');
        Assert.assertEquals("ll", data);
        Assert.assertEquals(8, characterReader.pos());
    }

    @Test
    public void testConsumeToAnySortedWithMultiChars() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        String data = characterReader.consumeToAnySorted(new char[]{'l', 'l'});
        Assert.assertEquals("ll", data);
        Assert.assertEquals(8, characterReader.pos());
    }

    @Test
    public void testConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&lt;script src=&quot;js &amp;amp;amp; amp; type=&quot;text/javascript&quot; charset=&quot;UTF-8&quot;/&gt;"));
        String data = characterReader.consumeData();
        Assert.assertEquals("&lt;script src=&quot;js &amp; amp; amp; amp; type=&quot;text/javascript&quot; charset=&quot;UTF-8&quot;", data);
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body>"));
        String data = characterReader.consumeTagName();
        Assert.assertEquals("html", data);

        characterReader.advance();
        data = characterReader.consumeTagName();
        Assert.assertEquals("head", data);

        characterReader.advance();
        data = characterReader.consumeTagName();
        Assert.assertEquals("title", data);

        characterReader.consume();
        data = characterReader.consumeTagName();
        Assert.assertEquals("body", data);

        characterReader.consumeToEnd();
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testToString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals("Hello World!", characterReader.toString());
    }

    @Test
    public void testMatchsWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.advance();
        Assert.assertTrue(characterReader.matches('H'));
        Assert.assertFalse(characterReader.matches('l'));
    }

    @Test
    public void testMatchsWithMultiChars() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.advance();
        characterReader.advance();
        Assert.assertTrue(characterReader.matches("ll"));
        Assert.assertFalse(characterReader.matches("llk"));
    }

    @Test
    public void testMatchsWithIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.advance();
        characterReader.advance();
        Assert.assertTrue(characterReader.matchesIgnoreCase("ll"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("LL"));
    }

    @Test
    public void testMatchsWithAnySingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.advance();
        Assert.assertTrue(characterReader.matchesAny('l'));
        Assert.assertTrue(characterReader.matchesAny('h'));
        Assert.assertFalse(characterReader.matchesAny('k'));
    }

    @Test
    public void testMatchsWithAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.advance();
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'l', 'h'}));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'h', 'l'}));
        Assert.assertFalse(characterReader.matchesAnySorted(new char[]{'k', 'l'}));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body>"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/title"));
        Assert.assertTrue(characterReader.containsIgnoreCase("head"));
        Assert.assertTrue(characterReader.containsIgnoreCase("body"));
        Assert.assertFalse(characterReader.containsIgnoreCase("/script"));
    }
}