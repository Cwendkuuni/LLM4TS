package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;

/**
 * Test cases for SoundexUtils class.
 */
public class SoundexUtilsTest {

    /**
     * Test for clean method.
     */
    @Test
    public void testClean() {
        String input = null;
        String expected = "";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "";
        expected = "";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "abc123";
        expected = "";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "ABCdefGhiJklmNOPQRSTUVWXYZ";
        expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "Hello World!";
        expected = "HELLO WORLD";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "123Hello World!";
        expected = "HELLO WORLD";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "Hello World! 123";
        expected = "HELLO WORLD";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "Hello World! 12345";
        expected = "HELLO WORLD";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "Hello World! 123456";
        expected = "HELLO WORLD";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "1234567890Hello World!";
        expected = "HELLO WORLD";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "HeLLo WoRlD!";
        expected = "HELLO WORLD";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "HeLLo WoRlD! $%^&*()_+={}[]|;:\"'<>,.?/~`";
        expected = "HELLO WORLD";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "HeLLo WoRlD! @#$%^&*()_+={}[]|;:\"'<>,.?/~` 1234567890";
        expected = "HELLO WORLD";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "HeLLo WoRlD! @#$%^&*()_+={}[]|;:\"'<>,.?/~` 1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        expected = "HELLO WORLDABCDEFGHIJKLMNOPQRSTUVWXYZ";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "HeLLo WoRlD! @#$%^&*()_+={}[]|;:\"'<>,.?/~` 1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        expected = "HELLO WORLDABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    /**
     * Test for difference method with valid inputs.
     */
    @Test
    public void testDifferenceValid() {
        String encoder = "Soundex";
        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int result = SoundexUtils.difference(new SoundexEncoder(), s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Apple";
        expected = 4;
        result = SoundexUtils.difference(new SoundexEncoder(), s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Apricot";
        expected = 3;
        result = SoundexUtils.difference(new SoundexEncoder(), s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Apricot";
        expected = 3;
        result = SoundexUtils.difference(new SoundexEncoder(), s2, s1);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Apricot";
        expected = 3;
        result = SoundexUtils.difference(new RefinedSoundexEncoder(), s1, s2);
        assertEquals(expected, result);

        s1 = "Apricot";
        s2 = "Apricot";
        expected = 4;
        result = SoundexUtils.difference(new RefinedSoundexEncoder(), s1, s2);
        assertEquals(expected, result);
    }

    /**
     * Test for difference method with null input.
     */
    @Test
    public void testDifferenceNullInput() {
        String encoder = "Soundex";
        String s1 = null;
        String s2 = "Orange";
        assertThrows(NullPointerException.class, () -> SoundexUtils.difference(new SoundexEncoder(), s1, s2));

        s1 = "Apple";
        s2 = null;
        assertThrows(NullPointerException.class, () -> SoundexUtils.difference(new SoundexEncoder(), s1, s2));
    }

    /**
     * Test for difference method with empty strings.
     */
    @Test
    public void testDifferenceEmptyStrings() {
        String encoder = "Soundex";
        String s1 = "";
        String s2 = "";
        int expected = 4;
        int result = SoundexUtils.difference(new SoundexEncoder(), s1, s2);
        assertEquals(expected, result);

        s1 = "";
        s2 = "Apple";
        expected = 0;
        result = SoundexUtils.difference(new SoundexEncoder(), s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "";
        expected = 0;
        result = SoundexUtils.difference(new SoundexEncoder(), s1, s2);
        assertEquals(expected, result);
    }

    /**
     * Test for difference method with different encoders.
     */
    @Test
    public void testDifferenceDifferentEncoders() {
        String encoder1 = "Soundex";
        String encoder2 = "RefinedSoundex";
        String s1 = "Apple";
        String s2 = "Apricot";
        int expected = 3;
        int result = SoundexUtils.difference(new SoundexEncoder(), s1, s2);
        assertEquals(expected, result);

        expected = 4;
        result = SoundexUtils.difference(new RefinedSoundexEncoder(), s1, s2);
        assertEquals(expected, result);

        s1 = "Apricot";
        s2 = "Apricot";
        expected = 4;
        result = SoundexUtils.difference(new RefinedSoundexEncoder(), s1, s2);
        assertEquals(expected, result);

        s1 = "Apricot";
        s2 = "Apple";
        expected = 3;
        result = SoundexUtils.difference(new RefinedSoundexEncoder(), s1, s2);
        assertEquals(expected, result);
    }

    /**
     * Test for differenceEncoded method with valid inputs.
     */
    @Test
    public void testDifferenceEncodedValid() {
        String es1 = "1234567812345678";
        String es2 = "2345678923456789";
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = "1234567812345678";
        es2 = "1234567812345678";
        expected = 4;
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = "1234567812345678";
        es2 = "1234567802345678";
        expected = 1;
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = "1234567812345678";
        es2 = "1234567802345678";
        expected = 1;
        result = SoundexUtils.differenceEncoded(es2, es1);
        assertEquals(expected, result);

        es1 = "1234567812345678";
        es2 = "1234567812345679";
        expected = 1;
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = "1234567812345679";
        es2 = "1234567812345678";
        expected = 1;
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = "1234567812345678";
        es2 = "1234567812345678";
        expected = 4;
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = "1234567812345678";
        es2 = "2345678923456789";
        expected = 0;
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = "2345678923456789";
        es2 = "1234567812345678";
        expected = 0;
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    /**
     * Test for differenceEncoded method with null input.
     */
    @Test
    public void testDifferenceEncodedNullInput() {
        String es1 = null;
        String es2 = "1234567812345678";
        assertThrows(NullPointerException.class, () -> SoundexUtils.differenceEncoded(es1, es2));

        es1 = "1234567812345678";
        es2 = null;
        assertThrows(NullPointerException.class, () -> SoundexUtils.differenceEncoded(es1, es2));
    }
}

class SoundexEncoder implements StringEncoder {
    @Override
    public String encode(String input) throws EncoderException {
        return SoundexUtils.clean(input);
    }

    @Override
    public String decode(String encoded) throws EncoderException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String encode(byte[] input) throws EncoderException {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte[] decode(String encoded) throws EncoderException {
        throw new UnsupportedOperationException();
    }
}

class RefinedSoundexEncoder implements StringEncoder {
    @Override
    public String encode(String input) throws EncoderException {
        String cleanInput = SoundexUtils.clean(input);
        StringBuilder encoded = new StringBuilder();
        for (int i = 0; i < cleanInput.length(); i++) {
            char c = cleanInput.charAt(i);
            int type = SoundexUtils.getSoundexType(c);
            if (encoded.length() == 0 || encoded.charAt(encoded.length() - 1) != type) {
                encoded.append(SoundexUtils.getSoundexCode(type));
            }
        }
        return encoded.toString();
    }

    @Override
    public String decode(String encoded) throws EncoderException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String encode(byte[] input) throws EncoderException {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte[] decode(String encoded) throws EncoderException {
        throw new UnsupportedOperationException();
    }
}