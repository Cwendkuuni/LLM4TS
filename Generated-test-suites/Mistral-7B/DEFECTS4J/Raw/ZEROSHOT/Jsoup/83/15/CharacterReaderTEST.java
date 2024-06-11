package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderCreation() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertNotNull(characterReader.charBuf);
        Assert.assertNotNull(characterReader.reader);
        Assert.assertEquals(characterReader.bufLength, CharacterReader.maxBufferLen);
        Assert.assertEquals(characterReader.bufSplitPoint, CharacterReader.readAheadLimit);
        Assert.assertEquals(characterReader.bufPos, 0);
        Assert.assertEquals(characterReader.readerPos, 0);
        Assert.assertEquals(characterReader.bufMark, 0);
    }

    @Test
    public void testCharacterReaderPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals(characterReader.pos(), 0);
    }

    @Test
    public void testCharacterReaderIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderCurrent() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals(characterReader.current(), 'H');
    }

    @Test
    public void testCharacterReaderConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.consume();
        Assert.assertEquals(characterReader.current(), 'e');
    }

    @Test
    public void testCharacterReaderAdvance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.advance();
        Assert.assertEquals(characterReader.current(), 'e');
    }

    @Test(expected = IllegalStateException.class)
    public void testCharacterReaderAdvancePastEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.advance();
        characterReader.advance();
        characterReader.advance();
        characterReader.advance();
    }

    @Test
    public void testCharacterReaderMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        Assert.assertEquals(characterReader.current(), 'H');
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abracadabra"));
        Assert.assertEquals(characterReader.nextIndexOf('a'), 0);
        Assert.assertEquals(characterReader.nextIndexOf('b'), 2);
        Assert.assertEquals(characterReader.nextIndexOf('c'), -1);
    }

    @Test
    public void testCharacterReaderNextIndexOfSequences() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abrabracadabra"));
        Assert.assertEquals(characterReader.nextIndexOf("ab"), 0);
        Assert.assertEquals(characterReader.nextIndexOf("ra"), 2);
        Assert.assertEquals(characterReader.nextIndexOf("cd"), 3);
        Assert.assertEquals(characterReader.nextIndexOf("abra"), 4);
        Assert.assertEquals(characterReader.nextIndexOf("dab"), 7);
        Assert.assertEquals(characterReader.nextIndexOf("cad"), 8);
        Assert.assertEquals(characterReader.nextIndexOf("ab"), 11);
        Assert.assertEquals(characterReader.nextIndexOf("d"), -1);
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abracadabra"));
        String result = characterReader.consumeTo('a');
        Assert.assertEquals(result, "a");
        Assert.assertEquals(characterReader.current(), 'b');
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abracadabra"));
        String result = characterReader.consumeToEnd();
        Assert.assertEquals(result, "abracadabra");
        Assert.assertEquals(characterReader.isEmpty(), true);
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abracadabra"));
        String result = characterReader.consumeToAny(',');
        Assert.assertEquals(result, "");
        Assert.assertEquals(characterReader.current(), 'a');

        result = characterReader.consumeToAny(new char[]{'a', 'b'});
        Assert.assertEquals(result, "a");
        Assert.assertEquals(characterReader.current(), 'b');

        result = characterReader.consumeToAny(new char[]{'.', ','});
        Assert.assertEquals(result, "a");
        Assert.assertEquals(characterReader.current(), 'b');

        result = characterReader.consumeToAny(new char[]{'a', 'b', 'c'});
        Assert.assertEquals(result, "ab");
        Assert.assertEquals(characterReader.current(), 'c');

        result = characterReader.consumeToAny(new char[]{'d', 'e', 'f'});
        Assert.assertEquals(result, "");
        Assert.assertEquals(characterReader.current(), 'c');
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abracadabra"));
        String result = characterReader.consumeToAnySorted(new char[]{'b', 'a'});
        Assert.assertEquals(result, "a");
        Assert.assertEquals(characterReader.current(), 'b');

        result = characterReader.consumeToAnySorted(new char[]{'b', 'c'});
        Assert.assertEquals(result, "a");
        Assert.assertEquals(characterReader.current(), 'b');

        result = characterReader.consumeToAnySorted(new char[]{'b', 'a', 'c'});
        Assert.assertEquals(result, "ab");
        Assert.assertEquals(characterReader.current(), 'c');

        result = characterReader.consumeToAnySorted(new char[]{'c', 'a', 'b'});
        Assert.assertEquals(result, "a");
        Assert.assertEquals(characterReader.current(), 'b');

        result = characterReader.consumeToAnySorted(new char[]{'c'});
        Assert.assertEquals(result, "");
        Assert.assertEquals(characterReader.current(), 'b');
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&lt;script src=\"script.js\" type=\"text/javascript\"&gt;"));
        String result = characterReader.consumeData();
        Assert.assertEquals(result, "&lt;script src=\"script.js\" type=\"text/javascript\"");
        Assert.assertEquals(characterReader.isEmpty(), false);
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body>"));
        String result = characterReader.consumeTagName();
        Assert.assertEquals(result, "html");
        Assert.assertEquals(characterReader.current(), '<');

        result = characterReader.consumeTagName();
        Assert.assertEquals(result, "head");
        Assert.assertEquals(characterReader.current(), '<');

        result = characterReader.consumeTagName();
        Assert.assertEquals(result, "title");
        Assert.assertEquals(characterReader.current(), '>');

        result = characterReader.consumeTagName();
        Assert.assertEquals(result, "body");
        Assert.assertEquals(characterReader.current(), '<');
    }

    @Test
    public void testCharacterReaderToString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        String result = characterReader.toString();
        Assert.assertEquals(result, "Hello World!");
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matches('H'));
        Assert.assertTrue(characterReader.matches("ll"));
        Assert.assertTrue(characterReader.matches("o"));
        Assert.assertTrue(characterReader.matches("W"));
        Assert.assertTrue(characterReader.matches("!"));
        Assert.assertFalse(characterReader.matches('!'));
        Assert.assertFalse(characterReader.matches("!"));
        Assert.assertFalse(characterReader.matches("!"));
        Assert.assertFalse(characterReader.matches("!"));
        Assert.assertFalse(characterReader.matches("!"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("h"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("ll"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("o"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("W"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("!"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("!"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("!"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("!"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("!"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("!"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matchesAny('H'));
        Assert.assertTrue(characterReader.matchesAny('e'));
        Assert.assertTrue(characterReader.matchesAny('l'));
        Assert.assertTrue(characterReader.matchesAny('o'));
        Assert.assertTrue(characterReader.matchesAny('W'));
        Assert.assertTrue(characterReader.matchesAny('!'));
        Assert.assertFalse(characterReader.matchesAny('!'));
        Assert.assertFalse(characterReader.matchesAny('!'));
        Assert.assertFalse(characterReader.matchesAny('!'));
        Assert.assertFalse(characterReader.matchesAny('!'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertTrue(characterReader.matchesAnySorted('H'));
        Assert.assertTrue(characterReader.matchesAnySorted('e'));
        Assert.assertTrue(characterReader.matchesAnySorted('l'));
        Assert.assertTrue(characterReader.matchesAnySorted('o'));
        Assert.assertTrue(characterReader.matchesAnySorted('W'));
        Assert.assertFalse(characterReader.matchesAnySorted('!'));
        Assert.assertFalse(characterReader.matchesAnySorted('!'));
        Assert.assertFalse(characterReader.matchesAnySorted('!'));
        Assert.assertFalse(characterReader.matchesAnySorted('!'));
        Assert.assertFalse(characterReader.matchesAnySorted('!'));
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        String result = characterReader.consumeLetterSequence();
        Assert.assertEquals(result, "abcdefghijklmnopqrstuvwxyz");
        Assert.assertEquals(characterReader.current(), '\u0000');
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abc123def456ghi789jklm012"));
        String result = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals(result, "abc123def456");
        Assert.assertEquals(characterReader.current(), 'g');
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("0x1a 0x2b 0x3c 0x4d 0x5e 0x6f"));
        String result = characterReader.consumeHexSequence();
        Assert.assertEquals(result, "1a2b3c4d5e6f");
        Assert.assertEquals(characterReader.current(), ' ');
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("1234567890"));
        String result = characterReader.consumeDigitSequence();
        Assert.assertEquals(result, "1234567890");
        Assert.assertEquals(characterReader.current(), '\u0000');
    }

    @Test(expected = IOException.class)
    public void testCharacterReaderIOException() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
        });
        characterReader.consume();
    }
}