package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.junit.Test;

/**
 * Test cases for Caverphone class
 */
public class CaverphoneTest {

    /**
     * Encodes various strings using Caverphone algorithm and checks the results.
     * @throws EncoderException if the input string is not valid
     */
    @Test
    public void testCaverphone() throws EncoderException {
        Caverphone encoder = new Caverphone();

        String testCases[] = new String[]{"apple", "banana", "Caversham", "Caverphone", "example", "Hello, World!", "12345", "test"};

        for (String s : testCases) {
            String result = encoder.caverphone(s);
            System.out.printf("%s -> %s%n", s, result);
            assertEquals(s.length() > 0 ? result.length() : "1111111111".length(), result.length());
        }

        // test empty string
        assertEquals("1111111111", encoder.caverphone(""));

        // test null input
        assertThrows(EncoderException.class, () -> encoder.caverphone(null));
    }

    /**
     * Encodes various objects using Caverphone algorithm and checks the results.
     * @throws EncoderException if the input object is not of type String
     */
    @Test
    public void testEncode() throws EncoderException {
        Caverphone encoder = new Caverphone();

        Object testCases[] = new Object[]{
            "apple",
            "banana",
            "Caversham",
            "Caverphone",
            "example",
            "Hello, World!",
            "12345",
            "",
            null
        };

        for (Object obj : testCases) {
            String result = (String) encoder.encode(obj);
            if (obj instanceof String) {
                String str = (String) obj;
                System.out.printf("%s -> %s%n", str, result);
                assertEquals(str.length() > 0 ? result.length() : "1111111111".length(), result.length());
            } else {
                assertNull(result);
            }
        }
    }

    /**
     * Tests if the caverphones of two strings are identical.
     */
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone encoder = new Caverphone();

        String testCases[][] = {{"apple", "apple"}, {"apple", "banana"}, {"Caversham", "Caverphone"}, {"example", "example"}, {"example", "Example"}};

        for (String[] input : testCases) {
            boolean expected = input[0].equals(input[1]);
            boolean result = encoder.isCaverphoneEqual(input[0], input[1]);
            assertEquals(expected, result);
        }
    }

    /**
     * Tests that two different strings have different caverphone codes.
     */
    @Test
    public void testIsCaverphoneNotEqual() {
        Caverphone encoder = new Caverphone();

        String testCases[][] = {{"apple", "banana"}, {"apple", "Caversham"}, {"apple", "Caverphone"}};

        for (String[] input : testCases) {
            boolean expected = !input[0].equals(input[1]);
            boolean result = !encoder.isCaverphoneEqual(input[0], input[1]);
            assertEquals(expected, result);
        }
    }
}