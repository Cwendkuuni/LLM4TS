package org.apache.commons.lang3;
import static org.junit.Assert.*;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertTrue(randomAsciiString.matches("[\\x20-\\x7E]+"));
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertTrue(randomAlphabeticString.matches("[a-zA-Z]+"));
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertTrue(randomAlphanumericString.matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertTrue(randomNumericString.matches("[0-9]+"));
    }

    @Test
    public void testRandomCustomCharacters() {
        String customCharacters = "abc123";
        String randomCustomString = RandomStringUtils.random(10, customCharacters);
        assertTrue(randomCustomString.matches("[abc123]+"));
    }

    @Test
    public void testRandomCustomCharSet() {
        char[] customCharSet = {'a', 'b', 'c'};
        String randomCustomCharSetString = RandomStringUtils.random(10, customCharSet);
        assertTrue(randomCustomCharSetString.matches("[abc]+"));
    }

    @Test
    public void testRandomInvalidLength() {
        try {
            String randomInvalidLengthString = RandomStringUtils.random(-1);
            fail("An IllegalArgumentException should have been thrown for negative length");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testRandomEmptyCharSet() {
        try {
            char[] emptyCharSet = {};
            String randomEmptyCharSetString = RandomStringUtils.random(10, emptyCharSet);
            fail("An IllegalArgumentException should have been thrown for empty char set");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testRandomInvalidCharSetRange() {
        try {
            char[] invalidCharSetRange = {'A', 'Z'};
            String randomInvalidCharSetRangeString = RandomStringUtils.random(10, 97, 122, false, true, invalidCharSetRange);
            fail("An ArrayIndexOutOfBoundsException should have been thrown for invalid char set range");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

}