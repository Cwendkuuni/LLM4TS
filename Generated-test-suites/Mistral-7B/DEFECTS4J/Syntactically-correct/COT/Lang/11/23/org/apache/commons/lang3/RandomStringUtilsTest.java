package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Length should match", count, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        int count = 10;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        assertEquals("Length should match", count, randomAsciiString.length());
        for (int i = 0; i < randomAsciiString.length(); i++) {
            int randomChar = randomAsciiString.charAt(i);
            assertTrue("Character should be ASCII", (randomChar >= 32 && randomChar <= 126));
        }
    }

    @Test
    public void testRandomAlphabetic() {
        int count = 10;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        assertEquals("Length should match", count, randomAlphabeticString.length());
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            int randomChar = randomAlphabeticString.charAt(i);
            assertTrue("Character should be alphabetic", Character.isAlphabetic((char) randomChar));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        int count = 10;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        assertEquals("Length should match", count, randomAlphanumericString.length());
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            int randomChar = randomAlphanumericString.charAt(i);
            assertTrue("Character should be alphanumeric", Character.isAlphabetic((char) randomChar) || Character.isDigit((char) randomChar));
        }
    }

    @Test
    public void testRandomNumeric() {
        int count = 10;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        assertEquals("Length should match", count, randomNumericString.length());
        for (int i = 0; i < randomNumericString.length(); i++) {
            int randomChar = randomNumericString.charAt(i);
            assertTrue("Character should be numeric", Character.isDigit((char) randomChar));
        }
    }

    @Test
    public void testRandomWithRangeAndBooleanFlags() {
        int count = 10;
        boolean letters = true;
        boolean numbers = false;
        String randomString = RandomStringUtils.random(count, letters, numbers);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            int randomChar = randomString.charAt(i);
            if (letters) {
                assertTrue("Character should be alphabetic", Character.isAlphabetic((char) randomChar));
            } else {
                assertFalse("Character should not be alphabetic", Character.isAlphabetic((char) randomChar));
            }
            if (numbers) {
                assertTrue("Character should be numeric", Character.isDigit((char) randomChar));
            } else {
                assertFalse("Character should not be numeric", Character.isDigit((char) randomChar));
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharsArray() {
        String emptyCharsArray = "";
        RandomStringUtils.random(1, emptyCharsArray.toCharArray());
    }

}