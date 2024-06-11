package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        // Test the constructor with an InputStream
        char[] input = {'h', 'e', 'l', 'l', 'o', '\n'};
        Reader reader = new InputStreamReader(new ByteArrayInputStream(input));
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertEquals(input.length, characterReader.pos());

        // Test the constructor with a String
        CharacterReader characterReaderString = new CharacterReader("hello\n");
        Assert.assertEquals(5, characterReaderString.pos());

        // Test isEmpty method
        Assert.assertFalse(characterReader.isEmpty());
        characterReader.consume();
        Assert.assertTrue(characterReader.isEmpty());

        // Test current method
        Assert.assertEquals('h', characterReader.current());

        // Test consume method
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());

        // Test unconsume method
        characterReader.unconsume();
        Assert.assertEquals('e', characterReader.current());

        // Test advance method
        characterReader.advance();
        Assert.assertEquals('l', characterReader.current());

        // Test mark and rewindToMark methods
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        Assert.assertEquals('l', characterReader.current());

        // Test nextIndexOf method with char
        Assert.assertEquals(1, characterReader.nextIndexOf('l'));

        // Test nextIndexOf method with CharSequence
        CharacterReader charSequenceCharacterReader = new CharacterReader("helloworld");
        Assert.assertEquals(6, charSequenceCharacterReader.nextIndexOf("world"));

        // Test consumeTo method with char
        String string = characterReader.consumeTo('l');
        Assert.assertEquals("he", string);

        // Test consumeTo method with CharSequence
        string = characterReader.consumeTo("ll");
        Assert.assertEquals("hello", string);

        // Test consumeToAny method with char array
        string = characterReader.consumeToAny(new char[]{'l', 'o'});
        Assert.assertEquals("hello", string);

        // Test consumeToAnySorted method with char array
        string = characterReader.consumeToAnySorted(new char[]{'o', 'l'});
        Assert.assertEquals("hello", string);

        // Test consumeData method
        string = characterReader.consumeData();
        Assert.assertEquals("ello\u0026>", string);

        // Test consumeTagName method
        string = characterReader.consumeTagName();
        Assert.assertEquals("hellotagname>", string);

        // Test consumeToEnd method
        string = characterReader.consumeToEnd();
        Assert.assertEquals("hellotagname>", string);

        // Test consumeLetterSequence method
        string = characterReader.consumeLetterSequence();
        Assert.assertEquals("hello", string);

        // Test consumeLetterThenDigitSequence method
        string = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("hello123", string);

        // Test consumeHexSequence method
        string = characterReader.consumeHexSequence();
        Assert.assertEquals("hello%23", string);

        // Test consumeDigitSequence method
        string = characterReader.consumeDigitSequence();
        Assert.assertEquals("hello12345", string);

        // Test matches method with char
        Assert.assertTrue(characterReader.matches('l'));

        // Test matches method with CharSequence
        Assert.assertTrue(characterReader.matches("ll"));

        // Test matchesIgnoreCase method with CharSequence
        Assert.assertTrue(characterReader.matchesIgnoreCase("hello"));

        // Test matchesAny method with char array
        Assert.assertTrue(characterReader.matchesAny(new char[]{'l', 'o'}));

        // Test matchesAnySorted method with char array
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'o', 'l'}));

        // Test matchesLetter method
        Assert.assertTrue(characterReader.matchesLetter());

        // Test matchesDigit method
        Assert.assertTrue(characterReader.matchesDigit());

        // Test matchConsume method with CharSequence
        characterReader.mark();
        Assert.assertTrue(characterReader.matchConsume("ll"));
        Assert.assertEquals(2, characterReader.pos());

        // Test matchConsumeIgnoreCase method with CharSequence
        characterReader.mark();
        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("hello"));
        Assert.assertEquals(5, characterReader.pos());

        // Test containsIgnoreCase method with CharSequence
        Assert.assertTrue(characterReader.containsIgnoreCase("world"));

        // Test toString method
        Assert.assertEquals("hellotagname>", characterReader.toString());

        // Test cacheString method
        String cacheString = characterReader.cacheString(charBuf, stringCache, bufPos, bufLength - bufPos);
        Assert.assertEquals("hellotagname>", cacheString);
    }

    // Test data for CharacterReaderTest
    private static final char[] charBuf = new char[100];
    private static final String[] stringCache = new String[512];

    // Test helper variables for CharacterReaderTest
    private static CharacterReader characterReader;
    private static int bufPos = 0;
    private static int bufLength = 100;
}