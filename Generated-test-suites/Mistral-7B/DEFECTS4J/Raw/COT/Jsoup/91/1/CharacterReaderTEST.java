package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class CharacterReaderTest {

    private CharacterReader characterReader;
    private static final String TEST_STRING = "This is a test string with special characters: &<\"'/>";

    @Before
    public void setup() {
        characterReader = new CharacterReader(new StringReader(TEST_STRING));
    }

    

    @Test
    public void testCharacterReaderConstruction() {
        CharacterReader reader = new CharacterReader(null);
        Assert.assertNull(reader);

        CharacterReader reader1 = new CharacterReader(new StringReader(""));
        CharacterReader reader2 = new CharacterReader(new StringReader("abcdefg"));
        CharacterReader reader3 = new CharacterReader(new Reader() {
            @Override
            public int read(char[] c, int off, int len) throws IOException {
                return 0;
            }

            @Override
            public boolean markSupported() {
                return true;
            }

            @Override
            public void mark(int readlimit) throws IOException {

            }

            @Override
            public void reset() throws IOException {

            }

            @Override
            public long skip(long n) throws IOException, NoSuchElementException {
                return 0;
            }

            @Override
            public int available() throws IOException {
                return 0;
            }

            @Override
            public boolean isOpen() {
                return false;
            }

            @Override
            public void close() throws IOException {

            }
        }, 10);

        Assert.assertNotNull(reader1);
        Assert.assertNotNull(reader2);
        Assert.assertNotNull(reader3);
    }

    @Test
    public void testEmptyReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(characterReader.isEmpty());
        Assert.assertEquals(0, characterReader.pos());
    }

    @Test
    public void testBufferUp() {
        characterReader.bufferUp();
        Assert.assertEquals(TEST_STRING.length(), characterReader.bufLength);
        characterReader.bufferUp();
        Assert.assertEquals(TEST_STRING.length() * 2, characterReader.bufLength);
    }

    @Test
    public void testPos() {
        Assert.assertEquals(0, characterReader.pos());
        characterReader.advance();
        characterReader.advance();
        characterReader.consume();
        Assert.assertEquals(3, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        Assert.assertTrue(characterReader.isEmpty());
        characterReader.advance();
        characterReader.consume();
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        Assert.assertEquals(TEST_STRING.charAt(0), characterReader.current());
        characterReader.advance();
        Assert.assertEquals(TEST_STRING.charAt(1), characterReader.current());
    }

    @Test
    public void testConsume() {
        characterReader.consume();
        Assert.assertEquals(TEST_STRING.charAt(0), characterReader.current());
        characterReader.consume();
        Assert.assertEquals(TEST_STRING.charAt(1), characterReader.current());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        Assert.assertEquals(TEST_STRING.charAt(0), characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        Assert.assertEquals(TEST_STRING.charAt(1), characterReader.current());

        characterReader.advance(3);
        Assert.assertEquals(TEST_STRING.charAt(4), characterReader.current());
    }

    @Test
    public void testMark() {
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        Assert.assertEquals(TEST_STRING.charAt(0), characterReader.current());
    }

    @Test
    public void testRewindToMarkWithInvalidMark() {
        characterReader.mark();
        characterReader.rewindToMark();
        Assert.assertEquals(TEST_STRING.charAt(0), characterReader.current());

        characterReader.consume();
        characterReader.consume();
        characterReader.rewindToMark();
        Assert.assertEquals(TEST_STRING.charAt(1), characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        Assert.assertEquals(-1, characterReader.nextIndexOf('z'));
        Assert.assertEquals(0, characterReader.nextIndexOf('T'));
        Assert.assertEquals(2, characterReader.nextIndexOf('&'));
        characterReader.consume();
        Assert.assertEquals(3, characterReader.nextIndexOf('&'));
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        Reader reader = new StringReader("");
        CharacterReader characterReader = new CharacterReader(reader, 5);
        characterReader.bufferUp();
        characterReader.reader.close();
        characterReader.consume();
    }

    @Test
    public void testConsumeTo() {
        String result = characterReader.consumeTo('&');
        Assert.assertEquals("This is a test ", result);
    }

    @Test
    public void testConsumeToEnd() {
        String result = characterReader.consumeToEnd();
        Assert.assertEquals(TEST_STRING, result);
    }

    @Test
    public void testConsumeLetterSequence() {
        String result = characterReader.consumeLetterSequence();
        Assert.assertEquals("isateststringwithspecialcharacters", result);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String result = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("isateststringwithspecialcharacters: &<\"'/>", result);
    }

    @Test
    public void testConsumeHexSequence() {
        String result = characterReader.consumeHexSequence();
        Assert.assertEquals("&lt;\"'&gt;", result);
    }

    @Test
    public void testConsumeDigitSequence() {
        String result = characterReader.consumeDigitSequence();
        Assert.assertEquals("2", result);
    }

    @Test
    public void testMatches() {
        Assert.assertTrue(characterReader.matches('T'));
        Assert.assertTrue(characterReader.matches("is"));
        characterReader.consume();
        Assert.assertFalse(characterReader.matches('T'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        Assert.assertTrue(characterReader.matchesIgnoreCase("is"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("Is"));
        characterReader.consume();
        Assert.assertFalse(characterReader.matchesIgnoreCase("is"));
    }

    @Test
    public void testMatchesAny() {
        Assert.assertTrue(characterReader.matchesAny('&'));
        Assert.assertTrue(characterReader.matchesAny('>'));
        Assert.assertTrue(characterReader.matchesAny('T'));
        Assert.assertFalse(characterReader.matchesAny('X'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abc"));
        Assert.assertTrue(characterReader.matchesAnySorted('a'));
        Assert.assertTrue(characterReader.matchesAnySorted('b'));
        Assert.assertTrue(characterReader.matchesAnySorted('c'));
        Assert.assertFalse(characterReader.matchesAnySorted('d'));
    }

    @Test
    public void testMatchesLetter() {
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.consume();
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.consume();
        Assert.assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        Assert.assertEquals("isateststringwithspecialcharacters", characterReader.consumeTo("is"));
        Assert.assertEquals(": &<\"'/>", characterReader.consumeTo(":"));
        Assert.assertEquals("", characterReader.consumeTo("x"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        Assert.assertEquals("isateststringwithspecialcharacters", characterReader.consumeToIgnoreCase("is"));
        Assert.assertEquals(": &<\"'/>", characterReader.consumeToIgnoreCase(":"));
        Assert.assertEquals("", characterReader.consumeToIgnoreCase("x"));
    }

    @Test
    public void testContainsIgnoreCase() {
        Assert.assertTrue(characterReader.containsIgnoreCase("<title>"));
        Assert.assertTrue(characterReader.containsIgnoreCase("</style>"));
        characterReader.consume();
        characterReader.consume();
        Assert.assertTrue(characterReader.containsIgnoreCase("<title>"));
        Assert.assertFalse(characterReader.containsIgnoreCase("</title>"));
    }

    @Test
    public void testToString() {
        Assert.assertEquals(TEST_STRING, characterReader.toString());
    }

    @Test
    public void testCacheString() {
        String result = characterReader.consumeToEnd();
        Assert.assertEquals(TEST_STRING, result);

        String string1 = characterReader.cacheString(charBuf, stringCache, bufPos, bufLength - bufPos);
        String string2 = characterReader.cacheString(charBuf, stringCache, bufPos, bufLength - bufPos);

        Assert.assertEquals(TEST_STRING, string1);
        Assert.assertEquals(TEST_STRING, string2);
    }

    
}