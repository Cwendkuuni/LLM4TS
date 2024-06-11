package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test for random method with default arguments.
     */
    @Test
    public void testRandomMethod() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
    }

    /**
     * Test for randomAscii method with default arguments.
     */
    @Test
    public void testRandomAsciiMethod() {
        String randomString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(c >= 32 && c <= 126);
        }
    }

    /**
     * Test for randomAlphabetic method with default arguments.
     */
    @Test
    public void testRandomAlphabeticMethod() {
        String randomString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c));
        }
    }

    /**
     * Test for randomAlphanumeric method with default arguments.
     */
    @Test
    public void testRandomAlphanumericMethod() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test for randomNumeric method with default arguments.
     */
    @Test
    public void testRandomNumericMethod() {
        String randomString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isDigit(c));
        }
    }

    /**
     * Test for random method with specified length and character set.
     */
    @Test
    public void testRandomMethodWithCharacterSet() {
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String randomString = RandomStringUtils.random(5, characterSet.toCharArray());
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(characterSet.indexOf(c) != -1);
        }
    }

    /**
     * Test for randomAscii method with specified character set.
     */
    @Test
    public void testRandomAsciiMethodWithCharacterSet() {
        String characterSet = "!@#$%^&*()_+-=[]{}|;:,.<>?/ ";
        String randomString = RandomStringUtils.randomAscii(5, characterSet.toCharArray());
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(characterSet.indexOf(c) != -1);
            assert(c >= 32 && c <= 126);
        }
    }

    /**
     * Test for randomAlphabetic method with specified character set.
     */
    @Test
    public void testRandomAlphabeticMethodWithCharacterSet() {
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String randomString = RandomStringUtils.randomAlphabetic(5, characterSet.toCharArray());
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(characterSet.indexOf(c) != -1);
            assert(Character.isAlphabetic(c));
        }
    }

    /**
     * Test for randomAlphanumeric method with specified character set.
     */
    @Test
    public void testRandomAlphanumericMethodWithCharacterSet() {
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String randomString = RandomStringUtils.randomAlphanumeric(5, characterSet.toCharArray());
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(characterSet.indexOf(c) != -1);
            assert(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test for randomNumeric method with specified character set.
     */
    @Test
    public void testRandomNumericMethodWithCharacterSet() {
        String characterSet = "0123456789";
        String randomString = RandomStringUtils.randomNumeric(5, characterSet.toCharArray());
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(characterSet.indexOf(c) != -1);
            assert(Character.isDigit(c));
        }
    }

    /**
     * Test for random method with specified start, end and character set.
     */
    @Test
    public void testRandomMethodWithStartEndAndCharacterSet() {
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String randomString = RandomStringUtils.random(5, 0, characterSet.length(), false, false, null, RANDOM);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(characterSet.indexOf(c) != -1);
        }
    }
}