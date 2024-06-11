package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

import org.junit.Test;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test method for {@link RandomStringUtils#random(int)}.
     */
    @Test
    public void testRandom() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assertNotNull("Random string should contain a character", randomString.charAt(i));
        }
        testRandomCharacterSet(randomString, false, false);
    }

    /**
     * Test method for {@link RandomStringUtils#randomAscii(int)}.
     */
    @Test
    public void testRandomAscii() {
        int count = 10;
        String randomString = RandomStringUtils.randomAscii(count);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assertFalse("Random string should only contain ASCII characters", Character.isHighSurrogate(randomString.charAt(i)));
            assertFalse("Random string should only contain ASCII characters", Character.isLowSurrogate(randomString.charAt(i)));
            assertTrue("Random string should only contain ASCII characters", Character.isISOControl(randomString.charAt(i)) || Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)) || Character.isWhitespace(randomString.charAt(i)) || Character.isGraphic(randomString.charAt(i)) || Character.isVisible(randomString.charAt(i)));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphabetic(int)}.
     */
    @Test
    public void testRandomAlphabetic() {
        int count = 10;
        String randomString = RandomStringUtils.randomAlphabetic(count);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assertTrue("Random string should only contain alphabetic characters", Character.isAlphabetic(randomString.charAt(i)));
        }
        testRandomCharacterSet(randomString, true, false);
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphanumeric(int)}.
     */
    @Test
    public void testRandomAlphanumeric() {
        int count = 10;
        String randomString = RandomStringUtils.randomAlphanumeric(count);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assertTrue("Random string should only contain alphanumeric characters", Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)));
        }
        testRandomCharacterSet(randomString, true, true);
    }

    /**
     * Test method for {@link RandomStringUtils#randomNumeric(int)}.
     */
    @Test
    public void testRandomNumeric() {
        int count = 10;
        String randomString = RandomStringUtils.randomNumeric(count);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assertTrue("Random string should only contain numeric characters", Character.isDigit(randomString.charAt(i)));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean)}.
     */
    @Test
    public void testRandomWithCharacterSet() {
        int count = 10;
        char start = 'A';
        char end = 'Z';
        String randomString = RandomStringUtils.random(count, start, end, true, false);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assertTrue("Random string should only contain alphabetic characters", Character.isAlphabetic(randomString.charAt(i)));
            assertTrue("Random string should only contain characters within the set", randomString.charAt(i) >= start && randomString.charAt(i) <= end);
        }

        start = '0';
        end = '9';
        randomString = RandomStringUtils.random(count, start, end, false, true);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assertTrue("Random string should only contain numeric characters", Character.isDigit(randomString.charAt(i)));
            assertTrue("Random string should only contain characters within the set", randomString.charAt(i) >= start && randomString.charAt(i) <= end);
        }

        start = 'A';
        end = 'Z';
        start = (char) (start + RandomStringUtils.random(1).length());
        randomString = RandomStringUtils.random(count, start, end, true, false);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assertTrue("Random string should only contain alphabetic characters", Character.isAlphabetic(randomString.charAt(i)));
            assertTrue("Random string should only contain characters within the set", randomString.charAt(i) >= start && randomString.charAt(i) <= end);
        }

        start = 'A';
        end = 'Z';
        end = (char) (end + RandomStringUtils.random(1).length());
        randomString = RandomStringUtils.random(count, start, end, true, false);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assertTrue("Random string should only contain alphabetic characters", Character.isAlphabetic(randomString.charAt(i)));
            assertTrue("Random string should only contain characters within the set", randomString.charAt(i) >= start && randomString.charAt(i) <= end);
        }

        start = 0;
        end = Character.MAX_VALUE;
        randomString = RandomStringUtils.random(count, start, end, true, true);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assertTrue("Random string should only contain printable characters", Character.isISOControl(randomString.charAt(i)) || Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)) || Character.isWhitespace(randomString.charAt(i)) || Character.isGraphic(randomString.charAt(i)) || Character.isVisible(randomString.charAt(i)));
        }

        start = 0;
        end = Character.MIN_VALUE;
        randomString = RandomStringUtils.random(count, start, end, true, true);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assertFalse("Random string should not contain non-printable characters", Character.isISOControl(randomString.charAt(i)));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}.
     */
    @Test
    public void testRandomWithCustomCharacterSetAndRandomSource() {
        int count = 10;
        char start = 'A';
        char end = 'Z';
        char[] customCharacterSet = new char[] { 'A', 'B' };
        Random customRandom = new Random();
        String randomString = RandomStringUtils.random(count, start, end, true, false, customCharacterSet, customRandom);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assertTrue("Random string should only contain alphabetic characters", Character.isAlphabetic(randomString.charAt(i)));
            assertTrue("Random string should only contain characters within the set", randomString.charAt(i) >= start && randomString.charAt(i) <= end || (customCharacterSet != null && (customCharacterSet[0] == randomString.charAt(i) || customCharacterSet[1] == randomString.charAt(i))));
        }
    }

    /**
     * Helper method to test if a string only contains the expected character set.
     *
     * @param randomString the string to test
     * @param allowLetters if true, the string should only contain letters
     * @param allowNumbers if true, the string should only contain digits
     */
    private void testRandomCharacterSet(String randomString, boolean allowLetters, boolean allowNumbers) {
        for (int i = 0; i < randomString.length(); i++) {
            if (allowLetters && !Character.isAlphabetic(randomString.charAt(i))) {
                fail("Random string should only contain alphabetic characters but contains " + randomString.charAt(i) + " at position " + i);
            }
            if (!allowLetters && Character.isAlphabetic(randomString.charAt(i))) {
                fail("Random string should not contain alphabetic characters but contains " + randomString.charAt(i) + " at position " + i);
            }
            if (allowNumbers && !Character.isDigit(randomString.charAt(i))) {
                fail("Random string should only contain numeric characters but contains " + randomString.charAt(i) + " at position " + i);
            }
            if (!allowNumbers && Character.isDigit(randomString.charAt(i))) {
                fail("Random string should not contain numeric characters but contains " + randomString.charAt(i) + " at position " + i);
            }
            if (Character.isHighSurrogate(randomString.charAt(i))) {
                fail("Random string should not contain high surrogates");
            }
            if (Character.isLowSurrogate(randomString.charAt(i))) {
                fail("Random string should not contain low surrogates");
            }
        }
    }

}